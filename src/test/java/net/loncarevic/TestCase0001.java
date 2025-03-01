package net.loncarevic;

import static net.loncarevic.utils.Constants.*;

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
      // Open login page
      LoginPage loginPage = new LoginPage(driver, wait).openLoginPage();

      // Check login page title
      loginPage.assertLoginTitle();

      // Assert login page UI elements
      loginPage.assertLoginPageUiElements();

      // Assert input fields exist
      loginPage.assertInputFields();

      // Inject session cookie to bypass login because of the reCAPTCHA
      injectSessionIntoSelenium(driver);

      // Navigate to the dashboard explicitly
      DashboardPage dashboardPage = new DashboardPage(driver, wait).openDashboard().dismissPopups();

      // Verify successful login by checking the Dashboard title
      dashboardPage.assertDashboardTitle();

      // Click on the "Campaigns" link
      CampaignsPage campaignsPage = new CampaignsPage(driver, wait).clickCampaignsLink();

      // Check campaign title
      campaignsPage.assertCampaignsTitle();

      // Create "Regular Campaign"
      campaignsPage.createRegularCampaign();

      // Fill in the Campaign name and subject
      campaignsPage.fillCampaignNameAndSubject();

      // Select the group of subscribers
      campaignsPage.selectSubscribersGroup();

      // Click Continue
      campaignsPage.clickContinue();

      // Click on the Start from scratch tab
      // Click on Drag & drop editor
      campaignsPage.startFromScratchAndDragDrop();

      // Locate the green button
      // Wait until "Done editing" text is visible inside the button
      campaignsPage.doneEditing();

      // ### Assert the Review and schedule page ###
      // Wait for the "Review and schedule" page to load
      // Assert text content, recipients, and buttons
      campaignsPage.assertReviewAndSchedulePage();

      // Click the "Send" button
      // sendButton.click(); // TODO uncomment when ready

    } catch (Exception e) {
      // exact catch block from your code
      logger.error(MSG_TEST_EXECUTION_FAILED, e);
    }
  }
}
