package net.loncarevic.pageobjects;

import static net.loncarevic.utils.Constants.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class LoginPage {

  private WebDriver driver;
  private WebDriverWait wait;

  public LoginPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /** Open login page. */
  public LoginPage openLoginPage() {
    driver.get(URL_DASHBOARD);
    return this;
  }

  /** Asserts the login page title. */
  public LoginPage assertLoginTitle() {
    String loginTitle = driver.getTitle();
    Assert.assertNotNull(loginTitle, MSG_LOGIN_TITLE_NULL);
    Assert.assertTrue(loginTitle.contains(TITLE_LOGIN), MSG_UNEXPECTED_LOGIN_TITLE + loginTitle);
    return this;
  }

  /** Asserts the login page UI elements. */
  public LoginPage assertLoginPageUiElements() {
    String loginPage = driver.getPageSource();
    Assert.assertNotNull(loginPage, MSG_LOGIN_PAGE_SOURCE_NULL);
    Assert.assertTrue(loginPage.contains(TEXT_WELCOME_BACK), MSG_WELCOME_BACK_MISSING);
    Assert.assertTrue(loginPage.contains(TEXT_DONT_HAVE_ACCOUNT), MSG_DONT_HAVE_ACCOUNT_MISSING);
    Assert.assertTrue(loginPage.contains(TEXT_SIGN_UP), MSG_SIGN_UP_MISSING);
    Assert.assertTrue(loginPage.contains(TEXT_REMEMBER_ME), MSG_REMEMBER_ME_MISSING);
    Assert.assertTrue(loginPage.contains(TEXT_FORGOT_PASSWORD), MSG_FORGOT_PASSWORD_MISSING);
    Assert.assertTrue(loginPage.contains(TEXT_LOGIN_BUTTON), MSG_LOGIN_BUTTON_MISSING);
    Assert.assertTrue(loginPage.contains(TEXT_LOGIN_ISSUE), MSG_LOGIN_ISSUE_MISSING);
    return this;
  }

  /** Asserts the presence of the email/password fields. */
  public LoginPage assertInputFields() {
    WebElement emailField =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(CSS_EMAIL_FIELD)));
    Assert.assertTrue(emailField.isDisplayed(), MSG_EMAIL_FIELD_NOT_VISIBLE);

    WebElement passwordField =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(CSS_PASSWORD_FIELD)));
    Assert.assertTrue(passwordField.isDisplayed(), MSG_PASSWORD_FIELD_NOT_VISIBLE);
    return this;
  }
}
