package net.loncarevic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

  public SubscribersPage selectUnsubscribedOption() throws InterruptedException {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.py-1")));
    WebElement unsubscribedOption =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//button[@data-test-id='dropdown-list-item'][.//span[normalize-space()='Unsubscribed']]")));
    unsubscribedOption.click();
    return this;
  }
}
