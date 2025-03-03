package net.loncarevic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class UnsubscribePage {

  private final WebDriver driver;
  private final WebDriverWait wait;

  public UnsubscribePage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /** Clicks the 'Unsubscribe' button. */
  public UnsubscribePage clickUnsubscribeButton() {
    wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Unsubscribe')]")))
        .click();
    return this;
  }

  /** Clicks the 'Yes' button if present, otherwise continues. */
  public UnsubscribePage clickYesButtonIfPresent() {
    if (isElementPresent(By.id("optout_link"))) {
      wait.until(ExpectedConditions.elementToBeClickable(By.id("optout_link"))).click();
    } else {
      System.out.println("No 'Yes' button found. Proceeding to the next step.");
    }
    return this;
  }

  /** Selects the first radio button for unsubscribe reason. */
  public UnsubscribePage selectUnsubscribeReason() {
    WebElement radioButton =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("customRadio1")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radioButton);
    return this;
  }

  /** Clicks the 'Submit' button. */
  public UnsubscribePage clickSubmitButton() {
    WebElement submitButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(".unsubscribe-reason-save")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
    return this;
  }

  /** Asserts the confirmation message for successful unsubscription. */
  public UnsubscribePage assertUnsubscriptionSuccess() {
    WebElement confirmationMessage =
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmedReasonMessage")));
    String actualMessage = confirmationMessage.getText().trim();
    Assert.assertEquals(
        actualMessage,
        "You have successfully unsubscribed",
        "Unsubscribe confirmation message mismatch.");
    return this;
  }

  /**
   * Utility method to check if an element exists in the DOM.
   *
   * @param by The locator of the element.
   * @return true if element exists, false otherwise.
   */
  private boolean isElementPresent(By by) {
    try {
      return driver.findElement(by).isDisplayed();
    } catch (Exception e) {
      return false;
    }
  }
}
