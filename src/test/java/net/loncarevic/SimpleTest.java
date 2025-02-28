package net.loncarevic;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest {

  @Test
  public void testDashboardUI() {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    try {
      // Open login page
      driver.get("https://dashboard.mailerlite.com");
      Assert.assertTrue(driver.getTitle().contains("Login | MailerLite"));

      // Assert login page UI elements
      Assert.assertTrue(
          driver.getPageSource().contains("Welcome back"), "Welcome back text is missing");
      Assert.assertTrue(
          driver.getPageSource().contains("Don’t have an account?"),
          "Don't have an account text is missing");
      Assert.assertTrue(driver.getPageSource().contains("Sign up"), "Sign up text is missing");
      Assert.assertTrue(
          driver.getPageSource().contains("Remember me for 7 days"),
          "Remember me checkbox text is missing");
      Assert.assertTrue(
          driver.getPageSource().contains("Forgot your password?"),
          "Forgot password link is missing");
      Assert.assertTrue(driver.getPageSource().contains("Login"), "Login button text is missing");
      Assert.assertTrue(
          driver.getPageSource().contains("I can't login to my account"),
          "Login issue text is missing");

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
      dismissCookiePopupIfPresent(driver, wait);

      // Close the "A glow up for your pop-ups!" modal
      dismissGlowUpPopupIfPresent(driver, wait);

      // Wait for UI to load after login
      wait.until(
          ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//h1[contains(text(), 'Dashboard')]")));

      // Verify successful login by checking the page title or dashboard-specific elements
      Assert.assertTrue(
          driver.getTitle().contains("Dashboard"), "Dashboard page did not load after login.");

    } finally {
      driver.quit();
    }
  }

  private void injectSessionIntoSelenium(WebDriver driver) {
    // Read the session cookie from an environment variable
    String cookieValue = System.getenv("MAILERLITE_COOKIE");
    if (cookieValue == null) {
      throw new RuntimeException("MAILERLITE_COOKIE environment variable not set");
    }
    Cookie sessionCookie =
        new Cookie("mailerlite_session", cookieValue, ".mailerlite.com", "/", null);
    driver.manage().addCookie(sessionCookie);
  }

  private void dismissCookiePopupIfPresent(WebDriver driver, WebDriverWait wait) {
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

  private void dismissGlowUpPopupIfPresent(WebDriver driver, WebDriverWait wait) {
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
