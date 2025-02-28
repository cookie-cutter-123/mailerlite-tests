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
    public void testDashboardUI() {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Open login page
            driver.get("https://dashboard.mailerlite.com");
            Assert.assertTrue(driver.getTitle().contains("Login | MailerLite"));

            // Assert login page UI elements
            Assert.assertTrue(driver.getPageSource().contains("Welcome back"), "Welcome back text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Don’t have an account?"), "Don't have an account text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Sign up"), "Sign up text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Remember me for 7 days"), "Remember me checkbox text is missing");
            Assert.assertTrue(driver.getPageSource().contains("Forgot your password?"), "Forgot password link is missing");
            Assert.assertTrue(driver.getPageSource().contains("Login"), "Login button text is missing");
            Assert.assertTrue(driver.getPageSource().contains("I can't login to my account"), "Login issue text is missing");

            // Assert and interact with input fields (without submitting)
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#email[type='email']")));
            Assert.assertTrue(emailField.isDisplayed(), "Email field is not visible");
            emailField.sendKeys("lorem@ipsum.net");

            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#password[type='password']")));
            Assert.assertTrue(passwordField.isDisplayed(), "Password field is not visible");
            passwordField.sendKeys("lorem ipsum");

            // Inject session cookie to bypass reCAPTCHA
            injectSessionIntoSelenium(driver);

            // Refresh to apply session
            driver.navigate().refresh();

            // Wait for UI to load after login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Welcome back')]")));

            // Verify dashboard loaded successfully
            Assert.assertTrue(driver.getPageSource().contains("Welcome back"), "Dashboard did not load properly.");

        } finally {
            driver.quit();
        }
    }

    private void injectSessionIntoSelenium(WebDriver driver) {
        Cookie sessionCookie = new Cookie("mailerlite_session",
                "eyJpdiI6Ijc4b1YrK3B5RDZlazcwTnp4alNaRkE9PSIsInZhbHVlIjoibFNzam1sRWJPaGJaRHphWlhxSERsTWUycE9zcjlWM3JuazB2bzJvU3ZWYUJvbDNUdlhYcHplM2JNenRsMG9Qc2dkaFJBUng5U3Y5RVBXY3ZJRGlhQm0xenh4dDdsSURhUHprZWJWMWx4dGVZTGxWbnZHSFc0RytrUkpvTGQ3TkkiLCJtYWMiOiJkOWQyYzU5ZmU0OGU3YThmYmViNjY5ODQ2ZjE0ZmJlNTNkMTVkNjM2MmU2N2M5NzUzMzg0ZjBkNDFhODZiYTU5IiwidGFnIjoiIn0%3D",
                ".mailerlite.com", "/", null);
        driver.manage().addCookie(sessionCookie);
    }
}
