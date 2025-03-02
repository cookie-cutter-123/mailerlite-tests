package net.loncarevic.utils;

import com.manybrain.mailinator.client.MailinatorClient;
import com.manybrain.mailinator.client.message.GetInboxRequest;
import com.manybrain.mailinator.client.message.Inbox;
import org.testng.Assert;

public class EmailUtils {
  private static final String MAILINATOR_API_KEY = System.getenv("MAILINATOR_API_KEY");
  private static final String MAILINATOR_DOMAIN = System.getenv("MAILINATOR_DOMAIN");

  /**
   * Checks Mailinator API for an email with the given subject.
   *
   * @param subject The expected email subject
   * @param timeoutSeconds How long to wait for the email to arrive
   */
  public static void assertEmailReceived(String subject, int timeoutSeconds) throws Exception {
    MailinatorClient mailinatorClient = new MailinatorClient(MAILINATOR_API_KEY);
    long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

    while (System.currentTimeMillis() < endTime) {
      if (emailExists(mailinatorClient, subject)) {
        return;
      }
      Thread.sleep(5000);
    }

    Assert.fail("Email with subject [" + subject + "] was not received in Mailinator.");
  }

  /**
   * Fetches inbox and checks if an email with the given subject exists.
   *
   * @param mailinatorClient The Mailinator API client instance
   * @param subject The expected email subject
   * @return true if found, otherwise false
   */
  private static boolean emailExists(MailinatorClient mailinatorClient, String subject) {
    Inbox inbox = mailinatorClient.request(new GetInboxRequest(MAILINATOR_DOMAIN));

    return inbox.getMsgs().stream().anyMatch(message -> message.getSubject().equals(subject));
  }
}
