package net.loncarevic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest {

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
        driver.get("https://dashboard.mailerlite.com");
        String loginTitle = driver.getTitle();
        Assert.assertNotNull(loginTitle, "Login page title is null.");
        Assert.assertTrue(
            loginTitle.contains("Login | MailerLite"),
            "Unexpected login page title: " + loginTitle);

        // Assert login page UI elements
        String loginPage = driver.getPageSource();
        Assert.assertNotNull(loginPage, "Login page source is null.");
        Assert.assertTrue(loginPage.contains("Welcome back"), "Welcome back text is missing");
        Assert.assertTrue(
            loginPage.contains("Don’t have an account?"), "Don't have an account text is missing");
        Assert.assertTrue(loginPage.contains("Sign up"), "Sign up text is missing");
        Assert.assertTrue(
            loginPage.contains("Remember me for 7 days"), "Remember me checkbox text is missing");
        Assert.assertTrue(
            loginPage.contains("Forgot your password?"), "Forgot password link is missing");
        Assert.assertTrue(loginPage.contains("Login"), "Login button text is missing");
        Assert.assertTrue(
            loginPage.contains("I can't login to my account"), "Login issue text is missing");

        // Assert input fields exist
        WebElement emailField =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input#email[type='email']")));
        Assert.assertTrue(emailField.isDisplayed(), "Email field is not visible");

        WebElement passwordField =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("input#password[type='password']")));
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not visible");

        // Inject session cookie to bypass login because of the reCAPTCHA
        injectSessionIntoSelenium(driver);

        // Navigate to the dashboard explicitly
        driver.get("https://dashboard.mailerlite.com/");

        // Close cookie popup if present
        dismissCookiePopupIfPresent(wait);

        // Close the "A glow up for your pop-ups!" modal
        dismissGlowUpPopupIfPresent(wait);

        // Wait for UI to load after login
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Dashboard')]")));

        // Verify successful login by checking the Dashboard title
        String dashboardTitle = driver.getTitle();
        Assert.assertNotNull(dashboardTitle, "Dashboard page title is null.");
        Assert.assertTrue(
            dashboardTitle.contains("Dashboard"), "Unexpected dashboard title: " + dashboardTitle);

        // Click on the "Campaigns" link
        WebElement campaignsLink =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//a[contains(@href, '/campaigns/status') and .//span[text()='Campaigns']]")));
        campaignsLink.click();

        String campaignTitle = driver.getTitle();
        Assert.assertNotNull(campaignTitle, "Campaign page title is null.");
        Assert.assertTrue(
            campaignTitle.contains("Campaigns | MailerLite"),
            "Unexpected Campaign page title: " + campaignTitle);

        // Click on "Create Campaign" (selector is a placeholder; update as needed)
        WebElement createCampaignButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='create-campaign-button']")));
        createCampaignButton.click();

        // Select "Regular Campaign"
        WebElement regularCampaignOption =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//h3[text()='Regular campaign']/ancestor::button")));
        regularCampaignOption.click();

        // Fill in the Campaign name
        WebElement campaignNameParent =
            driver.findElement(By.cssSelector("[data-test-id='campaign-name-input']"));
        campaignNameParent.click();
        WebElement campaignNameField = campaignNameParent.findElement(By.tagName("input"));
        campaignNameField.sendKeys("Automated Campaign");

        // Fill in the Campaign subject
        WebElement subjectParent =
            driver.findElement(By.cssSelector("[data-test-id='subject-input']"));
        subjectParent.click();
        WebElement subjectField = subjectParent.findElement(By.tagName("input"));
        subjectField.sendKeys("Test Campaign Subject");

        // Select the group of subscribers
        WebElement recipientsButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='recipients-selection-box']")));

        // Open recipients
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.PAGE_DOWN).perform();
        actions.moveToElement(recipientsButton).perform();
        recipientsButton.click();

        // Tick a subscribers group
        WebElement checkbox =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(
                        "[data-test-id='subscriber-group-row'] input[type='checkbox']")));
        checkbox.click();

        // Save recipients
        WebElement saveRecipientsButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='save-recipients-button']")));
        saveRecipientsButton.click();

        // Click Continue
        WebElement nextButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='create-campaign-next-button']")));
        nextButton.click();

        // Click on the Start from scratch tab
        WebElement startFromScratchTab =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='start-from-scratch-tab']")));
        startFromScratchTab.click();

        // Click on Drag & drop editor
        WebElement dragAndDropEditor =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='drag-drop-editor']")));
        dragAndDropEditor.click();

        // Locate the green button
        WebElement doneEditingButton =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(@class, 'btn-green')]")));

        // Wait until "Done editing" text is visible inside the button
        wait.until(ExpectedConditions.textToBePresentInElement(doneEditingButton, "Done editing"));

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
                By.xpath("//h1[contains(text(), 'Review and schedule')]")));

        // Assert text content
        WebElement subjectElement =
            wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("[data-test-id='subject'] p:last-child")));
        Assert.assertEquals(
            subjectElement.getText().trim(), "Test Campaign Subject", "Subject text is incorrect.");

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                            "[data-test-id='subject'] p:last-child"))) // Selects last <p> which
                // contains actual text
                .getText()
                .trim(),
            "Test Campaign Subject",
            "Subject text is incorrect.");

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                            "[data-test-id='sender'] p:last-child"))) // Adjusted to select the
                // correct <p>
                .getText()
                .trim(),
            "Lončarević (igor@loncarevic.net)",
            "Sender text is incorrect.");

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                            "[data-test-id='reply-to'] p:last-child"))) // Adjusted to select the
                // correct <p>
                .getText()
                .trim(),
            "igor@loncarevic.net",
            "Reply-to text is incorrect.");

        Assert.assertEquals(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(
                            "[data-test-id='campaign-language'] p:last-child"))) // Adjusted to
                // select the
                // correct <p>
                .getText()
                .trim(),
            "English",
            "Campaign language text is incorrect.");

        Assert.assertTrue(
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test-id='tracking-options']")))
                .getText()
                .contains("Track opens: Enabled"),
            "Tracking options text is incorrect.");

        // Locate recipients
        WebElement recipientsElement =
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("[data-test-id='recipents-section']")));

        // Extract individual spans
        WebElement groupsLabel =
            recipientsElement.findElement(By.xpath(".//span[contains(text(), 'Groups:')]"));
        WebElement groupName =
            recipientsElement.findElement(By.xpath(".//span[contains(text(), 'subs')]"));

        // Get text and trim any extra spaces
        String groupsText = groupsLabel.getText().trim();
        String groupValue = groupName.getText().trim();

        // Print extracted values for debugging
        System.out.println("Recipients section extracted: " + groupsText + " " + groupValue);

        Assert.assertEquals(
            groupsText + " " + groupValue, "Groups: subs", "Recipients section text is incorrect.");

        // Assert buttons are clickable (except the last one)
        String[] clickableButtons = {
          "subject-generator-button",
          "select-recipients-button",
          "send-test-email-button",
          "edit-content-button",
          "preview-email-button",
          "preview-plain-text",
          "edit-plain-text",
          "send-now-button",
          "send-later-button",
          "send-timezone-button",
          "smart-sending-button",
          "button-back"
        };

        for (String buttonId : clickableButtons) {
          WebElement button =
              wait.until(
                  ExpectedConditions.elementToBeClickable(
                      By.cssSelector("[data-test-id='" + buttonId + "']")));
          Assert.assertTrue(button.isDisplayed(), "Button " + buttonId + " is not displayed");
        }

        // Click the "Send" button
        WebElement sendButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("[data-test-id='button-send']")));
        sendButton.click();

      } finally {
        driver.quit();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void injectSessionIntoSelenium(WebDriver driver) {
    // Read the session cookie from an environment variable
    String cookieValue = System.getenv("MAILERLITE_COOKIE");
    if (cookieValue == null) {
      throw new RuntimeException("MAILERLITE_COOKIE environment variable not set");
    }

    // Add the session cookie
    Cookie sessionCookie =
        new Cookie("mailerlite_session", cookieValue, ".mailerlite.com", "/", null);
    driver.manage().addCookie(sessionCookie);

    // Verify if the cookie is still valid
    driver.get("https://dashboard.mailerlite.com/");
    String pageTitle = driver.getTitle();
    Assert.assertNotNull(pageTitle, "Page title is null after injecting session cookie.");
    if (pageTitle.contains("Login | MailerLite")) {
      System.out.println("Session expired! You need to log in and update the cookie.");
    } else {
      System.out.println("Session is valid.");
    }
  }

  private void dismissCookiePopupIfPresent(WebDriverWait wait) {
    try {
      WebElement rejectAllButton =
          wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.xpath("//button[contains(text(), 'Reject all')]")));
      rejectAllButton.click();
      wait.until(ExpectedConditions.invisibilityOf(rejectAllButton));
    } catch (TimeoutException e) {
      System.out.println("No cookie popup found, continuing...");
    }
  }

  private void dismissGlowUpPopupIfPresent(WebDriverWait wait) {
    try {
      // Wait for the modal that contains the pop-up text
      WebElement popupModal =
          wait.until(
              ExpectedConditions.presenceOfElementLocated(
                  By.xpath("//h2[contains(text(), 'A glow up for your pop-ups!')]")));

      // Find and click the "No thanks" button
      WebElement noThanksButton =
          wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.xpath("//button[contains(., 'No thanks')]")));
      noThanksButton.click();

      // Wait for the modal to disappear
      wait.until(ExpectedConditions.invisibilityOf(popupModal));

    } catch (TimeoutException e) {
      System.out.println("No 'glow-up' popup found, continuing...");
    }
  }
}
