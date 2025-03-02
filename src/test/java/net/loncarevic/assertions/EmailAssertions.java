package net.loncarevic.assertions;

import static net.loncarevic.utils.Constants.*;

import com.manybrain.mailinator.client.MailinatorClient;
import com.manybrain.mailinator.client.message.GetInboxRequest;
import com.manybrain.mailinator.client.message.GetMessageRequest;
import com.manybrain.mailinator.client.message.Inbox;
import com.manybrain.mailinator.client.message.Message;
import java.time.Duration;
import java.util.Optional;
import net.loncarevic.utils.EmailUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/** Provides assertion methods for verifying email content received via Mailinator. */
public class EmailAssertions {
  private static final Logger logger = LoggerFactory.getLogger(EmailAssertions.class);

  private static final String MAILINATOR_API_KEY = System.getenv("MAILINATOR_API_KEY");
  private static final String MAILINATOR_DOMAIN = System.getenv("MAILINATOR_DOMAIN");

  /**
   * Checks Mailinator API for an email with the given sender and subject.
   *
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected email subject
   * @param timeoutSeconds How long to wait for the email to arrive
   */
  public static void assertEmailReceived(
      String expectedSender, String expectedSubject, int timeoutSeconds) throws Exception {
    MailinatorClient mailinatorClient = new MailinatorClient(MAILINATOR_API_KEY);
    long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

    while (System.currentTimeMillis() < endTime) {
      Optional<Message> email = findEmail(mailinatorClient, expectedSender, expectedSubject);
      if (email.isPresent()) {
        verifyEmailDetails(mailinatorClient, email.get(), expectedSender, expectedSubject);
        return;
      }
      Thread.sleep(5000);
    }

    // Fail test case if email with the expected details is not present
    Assert.fail(String.format(ASSERT_FAIL_EMAIL_NOT_RECEIVED, expectedSubject, expectedSender));
  }

  /**
   * Fetches the inbox and searches for an email matching the given sender and subject.
   *
   * @param mailinatorClient The Mailinator API client instance
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected email subject
   * @return Optional containing the found email, or empty if not found
   */
  private static Optional<Message> findEmail(
      MailinatorClient mailinatorClient, String expectedSender, String expectedSubject) {
    Inbox inbox = mailinatorClient.request(new GetInboxRequest(MAILINATOR_DOMAIN));

    if (!inbox.getMsgs().isEmpty()) {
      Message latestMessage = inbox.getMsgs().get(inbox.getMsgs().size() - 1);
      logger.info("Latest email: {} | {}", latestMessage.getFrom(), latestMessage.getSubject());
    } else {
      logger.info("No emails received.");
    }

    return inbox.getMsgs().stream()
        .filter(
            message ->
                message.getSubject().equalsIgnoreCase(expectedSubject)
                    && message.getFrom().equalsIgnoreCase(expectedSender))
        .findFirst();
  }

  /**
   * Verifies the email details including sender, subject, and email content.
   *
   * @param mailinatorClient The Mailinator API client instance
   * @param message The retrieved message object
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected subject
   */
  private static void verifyEmailDetails(
      MailinatorClient mailinatorClient,
      Message message,
      String expectedSender,
      String expectedSubject) {
    Assert.assertEquals(message.getFrom(), expectedSender, ASSERT_SENDER_MISMATCH);
    Assert.assertEquals(message.getSubject(), expectedSubject, ASSERT_SUBJECT_MISMATCH);

    // Fetch full email content
    Message fullMessage =
        mailinatorClient.request(
            new GetMessageRequest(MAILINATOR_DOMAIN, message.getTo(), message.getId()));

    // Log email body for debugging
    String emailBody = fullMessage.getParts().get(0).getBody();
    logger.info("Received Email Body: {}", emailBody);

    // Extract the newsletter link from the email body
    String newsletterUrl = EmailUtils.extractUrlFromText(emailBody);
    Assert.assertNotNull(newsletterUrl, ASSERT_NO_NEWSLETTER_URL);

    // Validate the email content on the actual webpage
    validateEmailContentOnWebpage(newsletterUrl);
  }

  /**
   * Opens the newsletter URL and validates the content using Selenium.
   *
   * @param url The newsletter URL
   */
  private static void validateEmailContentOnWebpage(String url) {
    WebDriver driver = new ChromeDriver();
    try {
      driver.get(url);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

      // Check for iframes; if present, switch to the first one
      if (!driver.findElements(By.tagName("iframe")).isEmpty()) {
        driver.switchTo().frame(0);
      }

      // Wait until the body contains the sender's name, which is expected to be present in the
      // newsletter
      boolean isTextPresent =
          wait.until(
              d -> {
                String bodyText = d.findElement(By.tagName("body")).getText();
                return bodyText.contains(TEXT_SENDER_LASTNAME);
              });

      Assert.assertTrue(isTextPresent, ASSERT_NEWSLETTER_CONTENT_NOT_FOUND);
    } finally {
      driver.quit();
    }
  }
}
