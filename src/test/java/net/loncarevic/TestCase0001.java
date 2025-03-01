package net.loncarevic;

import static net.loncarevic.Constants.*;
import static net.loncarevic.utils.LocatorUtils.byDataTestId;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCase0001 {
  private static final Logger logger = LoggerFactory.getLogger(TestCase0001.class);

  @Test
  public void testDashboardUI() {
    ChromeOptions options = new ChromeOptions();
    boolean isCI = System.getenv("CI") != null; // Detect if running in GitHub Actions
    try {
      if (isCI) {
        // Create a unique temporary directory to prevent session conflicts in CI
        Path tempDir = Files.createTempDirectory("chrome-user-data");
        options.addArguments("--user-data-dir=" + tempDir.toAbsolutePath());

        // Run headless in CI to avoid UI dependency
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
      }

      WebDriver driver = new ChromeDriver(options);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

      try {
        // Open login page
        driver.get(URL_DASHBOARD);
        String loginTitle = driver.getTitle();
        Assert.assertNotNull(loginTitle, MSG_LOGIN_TITLE_NULL);
        Assert.assertTrue(
            loginTitle.contains(TITLE_LOGIN), MSG_UNEXPECTED_LOGIN_TITLE + loginTitle);

        // Assert login page UI elements
        String loginPage = driver.getPageSource();
        Assert.assertNotNull(loginPage, MSG_LOGIN_PAGE_SOURCE_NULL);
        Assert.assertTrue(loginPage.contains(TEXT_WELCOME_BACK), MSG_WELCOME_BACK_MISSING);
        Assert.assertTrue(
            loginPage.contains(TEXT_DONT_HAVE_ACCOUNT), MSG_DONT_HAVE_ACCOUNT_MISSING);
        Assert.assertTrue(loginPage.contains(TEXT_SIGN_UP), MSG_SIGN_UP_MISSING);
        Assert.assertTrue(loginPage.contains(TEXT_REMEMBER_ME), MSG_REMEMBER_ME_MISSING);
        Assert.assertTrue(loginPage.contains(TEXT_FORGOT_PASSWORD), MSG_FORGOT_PASSWORD_MISSING);
        Assert.assertTrue(loginPage.contains(TEXT_LOGIN_BUTTON), MSG_LOGIN_BUTTON_MISSING);
        Assert.assertTrue(loginPage.contains(TEXT_LOGIN_ISSUE), MSG_LOGIN_ISSUE_MISSING);

        // Assert input fields exist
        WebElement emailField =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(CSS_EMAIL_FIELD)));
        Assert.assertTrue(emailField.isDisplayed(), MSG_EMAIL_FIELD_NOT_VISIBLE);

        WebElement passwordField =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(CSS_PASSWORD_FIELD)));
        Assert.assertTrue(passwordField.isDisplayed(), MSG_PASSWORD_FIELD_NOT_VISIBLE);

        // Inject session cookie to bypass login because of the reCAPTCHA
        injectSessionIntoSelenium(driver);

        // Navigate to the dashboard explicitly
        driver.get(URL_DASHBOARD);

        // Close cookie popup if present
        dismissCookiePopupIfPresent(wait);

        // Close the "A glow up for your pop-ups!" modal
        dismissGlowUpPopupIfPresent(wait);

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath(XPATH_H1_DASHBOARD)));

        // Verify successful login by checking the Dashboard title
        String dashboardTitle = driver.getTitle();
        Assert.assertNotNull(dashboardTitle, MSG_DASHBOARD_TITLE_NULL);
        Assert.assertTrue(
            dashboardTitle.contains(TEXT_DASHBOARD),
            MSG_UNEXPECTED_DASHBOARD_TITLE + dashboardTitle);

        // Click on the "Campaigns" link
        WebElement campaignsLink =
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_CAMPAIGNS_LINK)));
        campaignsLink.click();

        String campaignTitle = driver.getTitle();
        Assert.assertNotNull(campaignTitle, MSG_CAMPAIGN_PAGE_TITLE_NULL);
        Assert.assertTrue(
            campaignTitle.contains(TITLE_CAMPAIGNS), MSG_UNEXPECTED_CAMPAIGN_TITLE + campaignTitle);

        // Click on "Create Campaign"
        WebElement createCampaignButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(CSS_CREATE_CAMPAIGN_BUTTON)));
        createCampaignButton.click();

        // Select "Regular Campaign"
        WebElement regularCampaignOption =
            wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(XPATH_REGULAR_CAMPAIGN_OPTION)));
        regularCampaignOption.click();

        // Fill in the Campaign name
        WebElement campaignNameParent = driver.findElement(By.cssSelector(CSS_CAMPAIGN_NAME_INPUT));
        campaignNameParent.click();
        WebElement campaignNameField = campaignNameParent.findElement(By.tagName("input"));
        campaignNameField.sendKeys(TEXT_CAMPAIGN_NAME);

        // Fill in the Campaign subject
        WebElement subjectParent = driver.findElement(By.cssSelector(CSS_SUBJECT_INPUT));
        subjectParent.click();
        WebElement subjectField = subjectParent.findElement(By.tagName("input"));
        subjectField.sendKeys(TEXT_CAMPAIGN_SUBJECT);

        // Select the group of subscribers
        WebElement recipientsButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(CSS_RECIPIENTS_SELECTION_BOX)));

        // Open recipients
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.PAGE_DOWN).perform();
        actions.moveToElement(recipientsButton).perform();
        recipientsButton.click();

        // Tick a subscribers group
        WebElement checkbox =
            wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_SUBSCRIBER_GROUP_ROW)));
        checkbox.click();

        // Save recipients
        WebElement saveRecipientsButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(CSS_SAVE_RECIPIENTS_BUTTON)));
        saveRecipientsButton.click();

        // Click Continue
        WebElement nextButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(CSS_CREATE_CAMPAIGN_NEXT_BUTTON)));
        nextButton.click();

        // Click on the Start from scratch tab
        WebElement startFromScratchTab =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(CSS_START_FROM_SCRATCH_TAB)));
        startFromScratchTab.click();

        // Click on Drag & drop editor
        WebElement dragAndDropEditor =
            wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_DRAG_DROP_EDITOR)));
        dragAndDropEditor.click();

        // Locate the green button
        WebElement doneEditingButton =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_DONE_EDITING_BUTTON)));

        // Wait until "Done editing" text is visible inside the button
        wait.until(
            ExpectedConditions.textToBePresentInElement(doneEditingButton, TEXT_DONE_EDITING));
        // Wait until the button is not disabled
        wait.until(
            ExpectedConditions.not(
                ExpectedConditions.attributeToBe(doneEditingButton, "disabled", "true")));

        // Ensure the button is visible and clickable
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", doneEditingButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", doneEditingButton);

        // ### Assert the Review and schedule page ###
        // Wait for the "Review and schedule" page to load
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), '" + TEXT_REVIEW_AND_SCHEDULE + "')]")));

        // Assert text content
        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(CSS_SUBJECT_ELEMENT)))
                .getText()
                .trim(),
            TEXT_CAMPAIGN_SUBJECT,
            MSG_SUBJECT_INCORRECT);

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(CSS_SENDER_ELEMENT)))
                .getText()
                .trim(),
            TEXT_SENDER,
            MSG_SENDER_INCORRECT);

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(CSS_REPLY_TO_ELEMENT)))
                .getText()
                .trim(),
            TEXT_REPLY_TO,
            MSG_REPLY_TO_INCORRECT);

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(CSS_CAMPAIGN_LANGUAGE_ELEMENT)))
                .getText()
                .trim(),
            TEXT_CAMPAIGN_LANGUAGE,
            MSG_CAMPAIGN_LANGUAGE_INCORRECT);

        Assert.assertTrue(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(CSS_TRACKING_OPTIONS)))
                .getText()
                .contains(TEXT_TRACK_OPENS_ENABLED),
            MSG_TRACKING_OPTIONS_INCORRECT);

        // Locate recipients
        WebElement recipientsElement =
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(CSS_RECIPIENTS_SECTION)));

        // Extract individual spans
        WebElement groupsLabel = recipientsElement.findElement(By.xpath(XPATH_GROUPS_LABEL));
        WebElement groupName = recipientsElement.findElement(By.xpath(XPATH_GROUP_NAME));

        // Get text and trim any extra spaces
        String groupsText = groupsLabel.getText().trim();
        String groupValue = groupName.getText().trim();

        // Log extracted values for debugging
        logger.debug(MSG_RECIPIENTS_EXTRACTED, groupsText, groupValue);
        Assert.assertEquals(
            groupsText + " " + groupValue, TEXT_RECIPIENTS, MSG_RECIPIENTS_SECTION_INCORRECT);

        // Assert buttons are clickable (except the last one)
        for (String buttonId : CLICKABLE_BUTTONS) {
          WebElement button =
              wait.until(ExpectedConditions.elementToBeClickable(byDataTestId(buttonId)));
          Assert.assertTrue(button.isDisplayed(), MSG_BUTTON_PREFIX + buttonId + MSG_BUTTON_SUFFIX);
        }

        // Click the "Send" button
        WebElement sendButton =
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_BUTTON_SEND)));
        // sendButton.click(); // TODO uncomment when ready

      } finally {
        driver.quit();
      }
    } catch (Exception e) {
      logger.error(MSG_TEST_EXECUTION_FAILED, e);
    }
  }

  private void injectSessionIntoSelenium(WebDriver driver) {
    // Read the session cookie from an environment variable
    String cookieValue = System.getenv(ENV_MAILERLITE_COOKIE);
    if (cookieValue == null) {
      throw new RuntimeException(MSG_ENV_MAILERLITE_COOKIE_NOT_SET);
    }

    // Add the session cookie
    Cookie sessionCookie = new Cookie(COOKIE_NAME, cookieValue, COOKIE_DOMAIN, "/", null);
    driver.manage().addCookie(sessionCookie);

    // Verify if the cookie is still valid
    driver.get(URL_DASHBOARD);
    String pageTitle = driver.getTitle();
    Assert.assertNotNull(pageTitle, MSG_PAGE_TITLE_NULL_AFTER_COOKIE);
    if (pageTitle.contains(TITLE_LOGIN)) {
      logger.warn(WARN_SESSION_EXPIRED);
    } else {
      logger.info(INFO_SESSION_VALID);
    }
  }

  private void dismissCookiePopupIfPresent(WebDriverWait wait) {
    try {
      WebElement rejectAllButton =
          wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_REJECT_ALL_BUTTON)));
      rejectAllButton.click();
      wait.until(ExpectedConditions.invisibilityOf(rejectAllButton));
    } catch (TimeoutException e) {
      logger.debug(TEXT_NO_COOKIE_POPUP);
    }
  }

  private void dismissGlowUpPopupIfPresent(WebDriverWait wait) {
    try {
      // Wait for the modal that contains the pop-up text
      WebElement popupModal =
          wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_GLOW_UP_POPUP)));

      // Find and click the "No thanks" button
      WebElement noThanksButton =
          wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_NO_THANKS_BUTTON)));
      noThanksButton.click();

      // Wait for the modal to disappear
      wait.until(ExpectedConditions.invisibilityOf(popupModal));

    } catch (TimeoutException e) {
      logger.debug(TEXT_NO_GLOW_UP_POPUP);
    }
  }
}
