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

/** Provides methods for interacting with and verifying email content via Mailinator. */
public class EmailAssertions {
  private static final Logger logger = LoggerFactory.getLogger(EmailAssertions.class);

  private static final String MAILINATOR_API_KEY = System.getenv("MAILINATOR_API_KEY");
  private static final String MAILINATOR_DOMAIN = System.getenv("MAILINATOR_DOMAIN");

  private static final MailinatorClient mailinatorClient = new MailinatorClient(MAILINATOR_API_KEY);

  /**
   * Waits for an email with the given sender and subject to arrive in Mailinator.
   *
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected email subject
   * @param timeoutSeconds Maximum wait time for the email to arrive
   * @return The received email message
   * @throws Exception If the email does not arrive within the timeout
   */
  private static Message waitForEmail(
      String expectedSender, String expectedSubject, int timeoutSeconds) throws Exception {
    long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);
    Optional<Message> email;

    while (System.currentTimeMillis() < endTime) {
      email = findEmail(expectedSender, expectedSubject);
      if (email.isPresent()) {
        return email.get();
      }
      Thread.sleep(5000); // TODO
    }

    Assert.fail(String.format(ASSERT_FAIL_EMAIL_NOT_RECEIVED, expectedSubject, expectedSender));
    return null; // Will never reach this line due to Assert.fail
  }

  /**
   * Fetches the inbox and searches for an email matching the given sender and subject.
   *
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected email subject
   * @return Optional containing the found email, or empty if not found
   */
  private static Optional<Message> findEmail(String expectedSender, String expectedSubject) {
    Inbox inbox = mailinatorClient.request(new GetInboxRequest(MAILINATOR_DOMAIN));

    return inbox.getMsgs().stream()
        .filter(
            message ->
                message.getSubject().equalsIgnoreCase(expectedSubject)
                    && message.getFrom().equalsIgnoreCase(expectedSender))
        .findFirst();
  }

  /**
   * Retrieves the body content of an email with the given sender and subject.
   *
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected email subject
   * @param timeoutSeconds Maximum wait time for the email to arrive
   * @return The body content of the email
   * @throws Exception If the email does not arrive within the timeout
   */
  public static String getEmailBody(
      String expectedSender, String expectedSubject, int timeoutSeconds) throws Exception {
    Message email = waitForEmail(expectedSender, expectedSubject, timeoutSeconds);
      assert email != null;
      Message fullMessage =
        mailinatorClient.request(
            new GetMessageRequest(MAILINATOR_DOMAIN, email.getTo(), email.getId()));

    String emailBody = fullMessage.getParts().get(0).getBody();
    logger.info("Received Email Body: {}", emailBody);
    return emailBody;
  }

  /**
   * Asserts that an email with the given sender and subject is received and verifies its content.
   *
   * @param expectedSender The expected sender email/name
   * @param expectedSubject The expected email subject
   * @param timeoutSeconds Maximum wait time for the email to arrive
   * @throws Exception If the email does not arrive or does not match expected content
   */
  public static void assertEmailReceived(
      String expectedSender, String expectedSubject, int timeoutSeconds) throws Exception {
    Message email = waitForEmail(expectedSender, expectedSubject, timeoutSeconds);
      assert email != null;
      Message fullMessage =
        mailinatorClient.request(
            new GetMessageRequest(MAILINATOR_DOMAIN, email.getTo(), email.getId()));

    Assert.assertEquals(email.getFrom(), expectedSender, ASSERT_SENDER_MISMATCH);
    Assert.assertEquals(email.getSubject(), expectedSubject, ASSERT_SUBJECT_MISMATCH);

    String emailBody = fullMessage.getParts().get(0).getBody();
    logger.info("Received Email Body: {}", emailBody);

    String newsletterUrl = EmailUtils.extractUrlFromText(emailBody);
    Assert.assertNotNull(newsletterUrl, ASSERT_NO_NEWSLETTER_URL);

    validateEmailContentOnWebpage(newsletterUrl);
  }

  /**
   * Opens a newsletter URL and verifies its content using Selenium.
   *
   * @param url The newsletter URL
   */
  private static void validateEmailContentOnWebpage(String url) {
    WebDriver driver = new ChromeDriver();
    try {
      driver.get(url);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

      if (!driver.findElements(By.tagName("iframe")).isEmpty()) {
        driver.switchTo().frame(0);
      }

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
