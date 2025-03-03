package net.loncarevic;

import static net.loncarevic.utils.Constants.*;
import static net.loncarevic.utils.CookieUtils.injectSessionIntoSelenium;

import net.loncarevic.assertions.EmailAssertions;
import net.loncarevic.base.BaseTest;
import net.loncarevic.pages.DashboardPage;
import net.loncarevic.pages.SubscribersPage;
import net.loncarevic.pages.UnsubscribePage;
import net.loncarevic.utils.EmailUtils;
import org.testng.annotations.Test;

public class TestCase0002 extends BaseTest {

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

    new SubscribersPage(driver, wait)
        // The actual test
        .openSubscribersPage()
        .clickDropdownButton()
        .selectUnsubscribedOption()
        .assertEmailPresent(SUBSCRIBER_EMAIL)
        .assertUnsubscribeReason(SUBSCRIBER_EMAIL, "I no longer want to receive these emails")
        // Add email back to the subscribers group
        .openSubscribersPage()
        .clickDropdownButton()
        .selectAllOption()
        .selectSubscriberCheckbox(SUBSCRIBER_EMAIL)
        .clickActionsButton()
        .clickAddSubscriberToGroupDropdownItem()
        .selectSubsCheckbox()
        .clickSaveButton()
        // Subscribe again
        .openSubscribersPage()
        .clickDropdownButton()
        .selectAllOption()
        .clickOnSubscriberEmail(SUBSCRIBER_EMAIL)
        .clickActionsButtonForSubscriber()
        .clickSubscribe()
        .clickConfirmActionButton();

    Thread.sleep(4000); // TODO remove
  }
}

// TODO subs assertions
// TODO restructure this
// TODO restructure EmailAssertions
// TODO Prepare TC1 with adding an email to subs!
// TODO constants
