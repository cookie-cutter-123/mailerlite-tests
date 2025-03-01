package net.loncarevic;

import static net.loncarevic.utils.Constants.*;
import static net.loncarevic.utils.CookieUtils.injectSessionIntoSelenium;

import net.loncarevic.base.BaseTest;
import net.loncarevic.pageobjects.CampaignsPage;
import net.loncarevic.pageobjects.DashboardPage;
import net.loncarevic.pageobjects.LoginPage;
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

      // Click on the "Campaigns" link
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

    } catch (Exception e) {
      // exact catch block from your code
      logger.error(MSG_TEST_EXECUTION_FAILED, e);
    }
  }
}
