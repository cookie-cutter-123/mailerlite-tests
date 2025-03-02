package net.loncarevic.utils;

import com.manybrain.mailinator.client.MailinatorClient;
import com.manybrain.mailinator.client.message.GetInboxRequest;
import com.manybrain.mailinator.client.message.GetMessageRequest;
import com.manybrain.mailinator.client.message.Inbox;
import com.manybrain.mailinator.client.message.Message;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class EmailUtils {
  private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

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

    Assert.fail(
        "Email with subject ["
            + expectedSubject
            + "] and sender ["
            + expectedSender
            + "] was not received in Mailinator.");
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

    logger.info("Received emails:");
    inbox.getMsgs().forEach(msg -> logger.info("{} | {}", msg.getFrom(), msg.getSubject()));

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
    Assert.assertEquals(message.getFrom(), expectedSender, "Sender does not match!");
    Assert.assertEquals(message.getSubject(), expectedSubject, "Subject does not match!");

    // Fetch full email content
    Message fullMessage =
        mailinatorClient.request(
            new GetMessageRequest(MAILINATOR_DOMAIN, message.getTo(), message.getId()));

    // Log email body for debugging
    String emailBody = fullMessage.getParts().get(0).getBody();
    logger.info("Received Email Body: {}", emailBody);

    // Extract the newsletter link from the email body
    String newsletterUrl = extractUrlFromText(emailBody);
    Assert.assertNotNull(newsletterUrl, "No newsletter URL found in the email body!");

    // Validate the email content on the actual webpage
    validateEmailContentOnWebpage(newsletterUrl);
  }

  /**
   * Extracts the first URL found in the given text.
   *
   * @param text The input text
   * @return Extracted URL or null if not found
   */
  private static String extractUrlFromText(String text) {
    Pattern pattern = Pattern.compile("https?://\\S+");
    Matcher matcher = pattern.matcher(text);
    return matcher.find() ? matcher.group() : null;
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

      // Check for iframes
      if (!driver.findElements(By.tagName("iframe")).isEmpty()) {
        driver.switchTo().frame(0);
      }

      // Adjust expected text or element based on actual webpage content
      String expectedText = "Newsletter Title";
      boolean isTextPresent =
          wait.until(
              ExpectedConditions.textToBePresentInElementLocated(
                  By.xpath("//h1[contains(text(), 'Newsletter Title')]"), expectedText));

      Assert.assertTrue(isTextPresent, "Expected content not found on the webpage!");

    } catch (Exception e) {
      throw new RuntimeException("Failed to validate email content on the webpage.", e);
    } finally {
      driver.quit();
    }
  }
}

// TODO magic strings
// TODO Move to pages?
// TODO fix logs not to show all emails
// TODO make it more simple
