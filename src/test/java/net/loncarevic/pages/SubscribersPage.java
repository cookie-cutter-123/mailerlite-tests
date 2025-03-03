package net.loncarevic.pages;

import static net.loncarevic.utils.LocatorUtils.byDataTestId;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SubscribersPage {

  private final WebDriver driver;
  private final WebDriverWait wait;
  private static final String SUBSCRIBERS_URL = "https://dashboard.mailerlite.com/subscribers";
  private static final String DROPDOWN_BUTTON = "dropdown-button";

  public SubscribersPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /** Navigates to the Subscribers page. */
  public SubscribersPage openSubscribersPage() {
    driver.get(SUBSCRIBERS_URL);
    return this;
  }

  /** Clicks the dropdown button on the Subscribers page. */
  public SubscribersPage clickDropdownButton() {
    WebElement activeElement =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='Active']")));
    activeElement.click();
    return this;
  }

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

  public SubscribersPage selectAllOption() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.py-1")));
    WebElement unsubscribedOption =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@data-test-id='dropdown-list-item'][.//span[normalize-space()='All']]")));
    unsubscribedOption.click();
    return this;
  }

  /** Checks if a specific email is present in the list */
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

  /** Verifies if a specific email is present in the list */
  public SubscribersPage assertEmailPresent(String email) {
    assert isEmailPresent(email) : "Email not found: " + email;
    return this;
  }

  public SubscribersPage assertUnsubscribeReason(String email, String expectedReason)
      throws InterruptedException {
    WebElement reasonElement =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tr[td//a[contains(text(), '" + email + "')]]/td[last()]")));
    assert reasonElement.getText().trim().equals(expectedReason)
        : "Expected unsubscribe reason '"
            + expectedReason
            + "' but found '"
            + reasonElement.getText().trim()
            + "'";
    Thread.sleep(3000);
    return this;
  }

  public SubscribersPage selectSubscriberCheckbox(String email) {
    WebElement checkbox =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//tr[td//a[contains(text(), '" + email + "')]]//input[@type='checkbox']")));

    if (!checkbox.isSelected()) {
      checkbox.click();
    }
    return this;
  }

  public SubscribersPage clickActionsButton() {
    WebElement actionsButton =
        wait.until(ExpectedConditions.elementToBeClickable(byDataTestId("actions-button")));
    actionsButton.click();
    return this;
  }

  public SubscribersPage clickAddSubscriberToGroupDropdownItem() {
    WebElement actionsButton =
        wait.until(ExpectedConditions.elementToBeClickable(byDataTestId("add-to-group-action")));
    actionsButton.click();
    return this;
  }

  public SubscribersPage selectSubsCheckbox() {
    WebElement subsCheckbox =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[id^='group-checkbox'][value='1']")));
    if (!subsCheckbox.isSelected()) {
      subsCheckbox.click();
    }

    // Click away
    Actions actions = new Actions(driver);
    actions.moveByOffset(10, 10).click().perform();
    return this;
  }

  public SubscribersPage clickSaveButton() {
    WebElement saveButton =
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.bg-green-500")));
    saveButton.click();
    return this;
  }

  public SubscribersPage clickOnSubscriberEmail(String email) {
    By emailLocator =
        By.xpath("//td[contains(@class, 'subscriber')]//a[contains(text(), '" + email + "')]");
    WebElement emailElement = wait.until(ExpectedConditions.elementToBeClickable(emailLocator));
    emailElement.click();
    return this;
  }

  public SubscribersPage clickActionsButtonForSubscriber() {
    By actionsButtonLocator = By.xpath("//button[span[text()='Actions']]");
    WebElement actionsButton =
        wait.until(ExpectedConditions.elementToBeClickable(actionsButtonLocator));
    actionsButton.click();
    return this;
  }

  public SubscribersPage clickSubscribe() {
    By subscribeOption =
        By.xpath("//li[contains(@id, 'dropdown-item')]/button[.//span[text()='Subscribe']]");

    // Ensure the "Subscribe" option is visible before clicking
    WebElement subscribe =
        wait.until(ExpectedConditions.visibilityOfElementLocated(subscribeOption));
    wait.until(ExpectedConditions.elementToBeClickable(subscribe)).click();

    return this;
  }

  public SubscribersPage clickConfirmActionButton() {
    // Wait for the button to be visible and clickable, then click it
    WebElement button =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(byDataTestId("confirm-action-button")));
    wait.until(ExpectedConditions.elementToBeClickable(button)).click();

    return this;
  }

  public SubscribersPage clickAddSubscribersButton() {
    WebElement addSubscribersButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/subscribers/import')]")));
    addSubscribersButton.click();
    return this;
  }

  public SubscribersPage clickAddSingleSubscriber() {
    WebElement addSingleSubscriberLink =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/subscribers/create')]")));
    addSingleSubscriberLink.click();
    return this;
  }

  public SubscribersPage enterEmail(String email) {
    WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
    emailInput.sendKeys(email);
    return this;
  }

  public SubscribersPage selectGroupSubs() throws InterruptedException {
    // Click the input field to activate the dropdown
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
    Thread.sleep(3000);

    return this;
  }

  public SubscribersPage clickAddSubscribersButton1() {
    WebElement addSubscribersButton =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href, '/subscribers/create')]")));

    // Scroll into view before clicking
    ((JavascriptExecutor) driver)
        .executeScript("arguments[0].scrollIntoView(true);", addSubscribersButton);

    wait.until(ExpectedConditions.elementToBeClickable(addSubscribersButton)).click();
    return this;
  }
}
