package net.loncarevic;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

public class SimpleTest {

    @Test
    public void testGoogleHomePageTitle() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://dashboard.mailerlite.com");
            Assert.assertTrue(driver.getTitle().contains("Login | MailerLite"));

            // Wait for and click the decline button if present
            try {
                WebElement declineButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinDeclineAll")));
                declineButton.click();
            } catch (Exception e) {
                System.out.println("Cookie decline button not found or already dismissed.");
            }

            // Assert visible text
            Assert.assertTrue(driver.getPageSource().contains("Welcome back"), "Welcome back text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Don’t have an account?"), "Don't have an account text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Sign up"), "Sign up text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Remember me for 7 days"), "Remember me checkbox text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Forgot your password?"), "Forgot password link is missing");
            Assert.assertTrue(driver.getPageSource().contains("Login"), "Login button text is missing");
            Assert.assertTrue(driver.getPageSource().contains("I can't login to my account"), "Login issue text is missing");

            // Enter email address
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#email[type='email']")));
            emailField.sendKeys("igor@loncarevic.net");

            // Enter password
            WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input#password[type='password']")));
            passwordField.sendKeys("bxhM%tS2V$bq*3he^8Fc");

            // Click on the Login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Login')]")));
            loginButton.click();

            Thread.sleep(5000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }
}
