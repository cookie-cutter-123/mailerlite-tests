package net.loncarevic;

import static net.loncarevic.utils.Constants.*;
import static net.loncarevic.utils.CookieUtils.injectSessionIntoSelenium;

import net.loncarevic.assertions.EmailAssertions;
import net.loncarevic.base.BaseTest;
import net.loncarevic.pages.CampaignsPage;
import net.loncarevic.pages.DashboardPage;
import net.loncarevic.pages.LoginPage;
import net.loncarevic.utils.DebugUtils;
import org.testng.annotations.Test;

public class TestCase0001 extends BaseTest {

  @Test
  public void testDashboardUI() throws Exception {
    // Test login page
    new LoginPage(driver, wait)
        .openLoginPage()
        .assertLoginTitle()
        .assertLoginPageUiElements()
        .assertInputFields();

    // Inject session cookie to bypass login because of the reCAPTCHA
    injectSessionIntoSelenium(driver);

    // DEBUG: Dump page after cookie injection
    DebugUtils.debugPage(driver, "AfterCookieInjection", true);

    // Navigate to the dashboard explicitly
    DashboardPage dashboardPage = new DashboardPage(driver, wait).openDashboard();

    // DEBUG: Dump page right before dismissing popups
    DebugUtils.debugPage(driver, "BeforePopups", false);

    // Dismiss popups and assert dashboard
    dashboardPage.dismissPopups().assertDashboardTitle();

    // DEBUG: Dump page before clicking "Campaigns"
    DebugUtils.debugPage(driver, "BeforeClickCampaigns", true);

    // Create and send the campaign
    new CampaignsPage(driver, wait)
        .clickCampaignsLink()
        .assertCampaignsTitle()
        .createRegularCampaign()
        .fillCampaignNameAndSubject()
        .selectSubscribersGroup()
        .clickContinue()
        .startFromScratchAndDragDrop()
        .doneEditing()
        .assertReviewAndScheduleSubPage()
        .clickSendButton();

    // DEBUG: Dump page after finishing campaign creation
    DebugUtils.debugPage(driver, "AfterSendButton", false);

    // Wait for the email to arrive in Mailinator (up to 60 seconds)
    EmailAssertions.assertEmailReceived(TEXT_SENDER_LASTNAME, TEXT_CAMPAIGN_SUBJECT, 60);
  }
}
