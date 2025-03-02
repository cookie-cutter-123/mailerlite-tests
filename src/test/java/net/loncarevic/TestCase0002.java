package net.loncarevic;

import static net.loncarevic.utils.Constants.TEXT_CAMPAIGN_SUBJECT;
import static net.loncarevic.utils.Constants.TEXT_SENDER_LASTNAME;

import net.loncarevic.assertions.EmailAssertions;
import net.loncarevic.base.BaseTest;
import net.loncarevic.utils.EmailUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCase0002 extends BaseTest {

  @Test
  public void testUnsubscribeFlow() throws Exception {
    // Fetch the latest email and extract the unsubscribe link
    String emailBody =
        EmailAssertions.getEmailBody(TEXT_SENDER_LASTNAME, TEXT_CAMPAIGN_SUBJECT, 60);
    String unsubscribeLink = EmailUtils.extractUrlFromText(emailBody);

    // Open the unsubscribe link
    driver.get(unsubscribeLink);

    // Click the "Unsubscribe" button
    wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Unsubscribe')]")))
        .click();

    // Check if the "Yes" button exists before attempting to click it
    if (isElementPresent(By.id("optout_link"))) {
      wait.until(ExpectedConditions.elementToBeClickable(By.id("optout_link"))).click();
    } else {
      System.out.println("No 'Yes' button found. Proceeding to the next step.");
    }

    // Select the first radio button (reason: "I no longer want to receive these emails")
    WebElement radioButton =
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("customRadio1")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radioButton);

    // Click the "Submit" button
    WebElement submitButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(".unsubscribe-reason-save")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

    // Assert the confirmation message
    WebElement confirmationMessage =
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmedReasonMessage")));
    String actualMessage = confirmationMessage.getText().trim();
    Assert.assertEquals(
        actualMessage,
        "You have successfully unsubscribed",
        "Unsubscribe confirmation message mismatch.");

    Thread.sleep(2000); // TODO remove
    // TODO subs assertions
    // TODO restructure this
    // TODO restructure EmailAssertions
    // TODO Prepare TC1 with adding an email to subs!
  }

  /**
   * Utility method to check if an element exists in the DOM.
   *
   * @param by The locator of the element.
   * @return true if element exists, false otherwise.
   */
  private boolean isElementPresent(By by) {
    try {
      WebElement element = driver.findElement(by);
      return element.isDisplayed();
    } catch (NoSuchElementException e) {
      return false;
    }
  }
}
