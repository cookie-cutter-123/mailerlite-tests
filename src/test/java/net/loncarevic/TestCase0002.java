package net.loncarevic;

import static net.loncarevic.utils.Constants.*;
import static net.loncarevic.utils.CookieUtils.injectSessionIntoSelenium;

import net.loncarevic.assertions.EmailAssertions;
import net.loncarevic.base.BaseTest;
import net.loncarevic.pages.DashboardPage;
import net.loncarevic.pages.SubscribersPage;
import net.loncarevic.pages.UnsubscribePage;
import net.loncarevic.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TestCase0002 extends BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(TestCase0002.class);

  @Test
  public void testUnsubscribeFlow() throws Exception {
    // Fetch the latest email and extract the unsubscribe link
    String emailBody =
        EmailAssertions.getEmailBody(TEXT_SENDER_LASTNAME, TEXT_CAMPAIGN_SUBJECT, 60);
    String unsubscribeLink = EmailUtils.extractUrlFromText(emailBody);

    // Open the unsubscribe link
    assert unsubscribeLink != null;
    driver.get(unsubscribeLink);

    // Perform the unsubscribe flow using the UnsubscribePage
    new UnsubscribePage(driver, wait)
        .clickUnsubscribeButton()
        .clickYesButtonIfPresent()
        .selectUnsubscribeReason()
        .clickSubmitButton()
        .assertUnsubscriptionSuccess();

    // Inject session cookie to bypass login because of the reCAPTCHA
    injectSessionIntoSelenium(driver);

    // Navigate to the dashboard explicitly
    new DashboardPage(driver, wait).openDashboard().dismissPopups().assertDashboardTitle();

    // Inject cookie again
    injectSessionIntoSelenium(driver);

    // Navigate to subscribers page
    SubscribersPage subscribersPage = new SubscribersPage(driver, wait).openSubscribersPage();

    // Check if email exists, if not, add it
    if (!subscribersPage.isEmailPresent(SUBSCRIBER_EMAIL)) {
      logger.info("Subscriber email not present. Adding subscriber.");
      subscribersPage
          .clickAddSubscribersButton()
          .clickAddSingleSubscriber()
          .enterEmail(SUBSCRIBER_EMAIL)
          .selectGroupSubs()
          .clickSaveButton();
    } else {
      logger.info("Subscriber email found.");
    }

    // Verify email appears in Unsubscribed list with correct reason
    subscribersPage
        .openSubscribersPage()
        .clickDropdownButton()
        .selectUnsubscribedOption()
        .assertEmailPresent(SUBSCRIBER_EMAIL)
        .assertUnsubscribeReason(SUBSCRIBER_EMAIL, "I no longer want to receive these emails");

    // Re-subscribe if necessary
    if (subscribersPage.isUnsubscribed(SUBSCRIBER_EMAIL)) {
      logger.info("Subscriber is unsubscribed. Re-subscribing...");
      subscribersPage
          .openSubscribersPage()
          .clickDropdownButton()
          .selectAllOption()
          .clickOnSubscriberEmail(SUBSCRIBER_EMAIL)
          .clickActionsButtonForSubscriber()
          .clickSubscribe()
          .clickConfirmActionButton();
    } else {
      logger.info("Subscriber is already active.");
    }
  }
}

// TODO restructure EmailAssertions
// TODO constants
