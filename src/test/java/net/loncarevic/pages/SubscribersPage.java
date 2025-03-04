package net.loncarevic.pages;

import static net.loncarevic.utils.Constants.SUBSCRIBERS_URL;
import static net.loncarevic.utils.LocatorUtils.byDataTestId;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Represents the Subscribers page and provides methods to interact with its elements. */
public class SubscribersPage {

  private final WebDriver driver;
  private final WebDriverWait wait;

  /**
   * Constructs a SubscribersPage object.
   *
   * @param driver The WebDriver instance.
   * @param wait The WebDriverWait instance.
   */
  public SubscribersPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /**
   * Navigates to the Subscribers page.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage openSubscribersPage() {
    driver.get(SUBSCRIBERS_URL);
    return this;
  }

  /**
   * Clicks the dropdown button on the Subscribers page.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickDropdownButton() {
    WebElement activeElement =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='Active']")));
    activeElement.click();
    return this;
  }

  /**
   * Selects the "Unsubscribed" option from the dropdown.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage selectUnsubscribedOption() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.py-1")));
    WebElement unsubscribedOption =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@data-test-id='dropdown-list-item'][.//span[normalize-space()='Unsubscribed']]")));
    unsubscribedOption.click();
    return this;
  }

  /**
   * Selects the "All" option from the dropdown.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage selectAllOption() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.py-1")));
    WebElement allOption =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@data-test-id='dropdown-list-item'][.//span[normalize-space()='All']]")));
    allOption.click();
    return this;
  }

  /**
   * Checks if a specific email is present in the subscribers list.
   *
   * @param email The email to search for.
   * @return True if the email is present; false otherwise.
   */
  public boolean isEmailPresent(String email) {
    try {
      return wait.until(
              ExpectedConditions.presenceOfElementLocated(
                  By.xpath(
                      "//td[contains(@class, 'subscriber')]//a[contains(text(), '"
                          + email
                          + "')]")))
          .isDisplayed();
    } catch (TimeoutException e) {
      return false; // Email not found
    }
  }

  /**
   * Asserts that a specific email is present in the list.
   *
   * @param email The email to assert.
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage assertEmailPresent(String email) {
    if (!isEmailPresent(email)) {
      throw new AssertionError("Email not found: " + email);
    }
    return this;
  }

  /**
   * Checks if the given email is marked as unsubscribed.
   *
   * @param email The email to check.
   * @return True if unsubscribed; false otherwise.
   */
  public boolean isUnsubscribed(String email) {
    try {
      WebElement statusElement =
          wait.until(
              ExpectedConditions.presenceOfElementLocated(
                  By.xpath(
                      "//tr[td//a[contains(text(), '"
                          + email
                          + "')]]/td[contains(text(), 'Unsubscribed')]")));
      return statusElement.isDisplayed();
    } catch (TimeoutException e) {
      return false; // Email is not unsubscribed
    }
  }

  /**
   * Verifies that the unsubscribe reason for the given email matches the expected reason.
   *
   * @param email The email to check.
   * @param expectedReason The expected unsubscribe reason.
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage assertUnsubscribeReason(String email, String expectedReason) {
    WebElement reasonElement =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tr[td//a[contains(text(), '" + email + "')]]/td[last()]")));
    new WebDriverWait(driver, Duration.ofSeconds(10))
        .until(ExpectedConditions.textToBePresentInElement(reasonElement, expectedReason));
    if (!reasonElement.getText().trim().equals(expectedReason)) {
      throw new AssertionError(
          "Expected unsubscribe reason '"
              + expectedReason
              + "' but found '"
              + reasonElement.getText().trim()
              + "'");
    }
    return this;
  }

  /**
   * Clicks the "Save" button.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickSaveButton() {
    WebElement saveButton =
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.bg-green-500")));
    saveButton.click();
    return this;
  }

  /**
   * Clicks on the subscriber's email link.
   *
   * @param email The email to click.
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickOnSubscriberEmail(String email) {
    By emailLocator =
        By.xpath("//td[contains(@class, 'subscriber')]//a[contains(text(), '" + email + "')]");
    WebElement emailElement = wait.until(ExpectedConditions.elementToBeClickable(emailLocator));
    emailElement.click();
    return this;
  }

  /**
   * Clicks the "Actions" button for the subscriber.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickActionsButtonForSubscriber() {
    By actionsButtonLocator = By.xpath("//button[span[text()='Actions']]");
    WebElement actionsButton =
        wait.until(ExpectedConditions.elementToBeClickable(actionsButtonLocator));
    actionsButton.click();
    return this;
  }

  /**
   * Clicks the "Subscribe" option from the actions dropdown.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickSubscribe() {
    By subscribeOption =
        By.xpath("//li[contains(@id, 'dropdown-item')]/button[.//span[text()='Subscribe']]");
    WebElement subscribe =
        wait.until(ExpectedConditions.visibilityOfElementLocated(subscribeOption));
    wait.until(ExpectedConditions.elementToBeClickable(subscribe)).click();
    return this;
  }

  /**
   * Clicks the "Confirm Action" button.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickConfirmActionButton() {
    WebElement button =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(byDataTestId("confirm-action-button")));
    wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    return this;
  }

  /**
   * Clicks the button to add subscribers.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickAddSubscribersButton() {
    WebElement addSubscribersButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/subscribers/import')]")));
    addSubscribersButton.click();
    return this;
  }

  /**
   * Clicks the link to add a single subscriber.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage clickAddSingleSubscriber() {
    // Force removal of any modal overlay that may block the click
    try {
      for (WebElement overlay : driver.findElements(By.cssSelector("div.fixed.z-100"))) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].remove();", overlay);
      }
    } catch (Exception e) {
      // Do nothing if removal fails
    }
    // Additionally, force removal of the cookie banner overlay if present
    try {
      WebElement cookieBanner = driver.findElement(By.id("CookieBannerNotice"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].remove();", cookieBanner);
    } catch (Exception e) {
      // Do nothing if not present
    }
    WebElement addSingleSubscriberLink =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/subscribers/create')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addSingleSubscriberLink);
    return this;
  }

  /**
   * Enters the email into the subscriber email input field.
   *
   * @param email The email to enter.
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage enterEmail(String email) {
    WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
    emailInput.sendKeys(email);
    return this;
  }

  /**
   * Selects the subscriber group by clicking the input field, checking the desired checkbox.
   *
   * @return The current instance of SubscribersPage.
   */
  public SubscribersPage selectGroupSubs() {
    WebElement groupInput =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@name='group_autocomplete']")));
    groupInput.click();

    // Wait for the "subs" checkbox and select it
    WebElement subsCheckbox =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='checkbox' and @class='custom-control-input']")));
    if (!subsCheckbox.isSelected()) {
      subsCheckbox.click();
    }

    Actions actions = new Actions(driver);
    actions.moveByOffset(10, 10).click().perform();
    return this;
  }
}
