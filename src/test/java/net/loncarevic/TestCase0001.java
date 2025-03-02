package net.loncarevic;

import static net.loncarevic.utils.Constants.*;
import static net.loncarevic.utils.CookieUtils.injectSessionIntoSelenium;

import net.loncarevic.base.BaseTest;
import net.loncarevic.pages.CampaignsPage;
import net.loncarevic.pages.DashboardPage;
import net.loncarevic.pages.LoginPage;
import net.loncarevic.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TestCase0001 extends BaseTest {

  private static final Logger logger = LoggerFactory.getLogger(TestCase0001.class);

  @Test
  public void testDashboardUI() {
    try {
      // Test login page
      new LoginPage(driver, wait)
          .openLoginPage()
          .assertLoginTitle()
          .assertLoginPageUiElements()
          .assertInputFields();

      // Inject session cookie to bypass login because of the reCAPTCHA
      injectSessionIntoSelenium(driver);

      // Navigate to the dashboard explicitly
      new DashboardPage(driver, wait).openDashboard().dismissPopups().assertDashboardTitle();

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

      // Wait for the email to arrive in Mailinator (up to 60 seconds)
      EmailUtils.assertEmailReceived(TEXT_CAMPAIGN_SUBJECT, 60);

    } catch (Exception e) {
      logger.error(MSG_TEST_EXECUTION_FAILED, e);
    }
  }
}
