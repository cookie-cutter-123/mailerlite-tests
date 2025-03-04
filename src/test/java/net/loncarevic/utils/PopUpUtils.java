package net.loncarevic.utils;

import static net.loncarevic.utils.Constants.*;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Provides methods for dismissing pop-ups on the dashboard. */
public class PopUpUtils {
  private static final Logger logger = LoggerFactory.getLogger(PopUpUtils.class);

  /** Closes the cookie consent pop-up if present by clicking "Reject all." */
  public static void dismissCookiePopupIfPresent(WebDriver driver, WebDriverWait wait) {
    try {
      // Wait for the "Reject all" button and click it
      WebElement rejectAllButton =
          wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_REJECT_ALL_BUTTON)));
      rejectAllButton.click();

      // Wait for the button to disappear, confirming the popup is dismissed
      wait.until(ExpectedConditions.invisibilityOf(rejectAllButton));
    } catch (TimeoutException e) {
      // If the pop-up is not found, log debug info and continue
      logger.debug(TEXT_NO_COOKIE_POPUP);
    }
    // Force removal of cookie banner if still present
    try {
      WebElement cookieBanner = driver.findElement(By.id("CookieBannerNotice"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].remove();", cookieBanner);
      logger.debug("CookieBannerNotice removed via JavaScript.");
    } catch (Exception ex) {
      logger.debug("Cookie banner not present or already removed.");
    }
  }

  /** Closes the "A glow up for your pop-ups!" modal if present. */
  public static void dismissGlowUpPopupIfPresent(WebDriver driver, WebDriverWait wait) {
    try {
      // Wait for the modal that contains the pop-up text
      WebElement popupModal =
          wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_GLOW_UP_POPUP)));

      // Find and click the "No thanks" button
      WebElement noThanksButton =
          wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_NO_THANKS_BUTTON)));
      try {
        noThanksButton.click();
      } catch (ElementClickInterceptedException e) {
        logger.debug("No thanks button click intercepted, dismissing cookie popup first");
        dismissCookiePopupIfPresent(driver, wait);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("CookieBannerNotice")));
        noThanksButton.click();
      }
      wait.until(ExpectedConditions.invisibilityOf(popupModal));

    } catch (TimeoutException e) {
      // If no modal is found, log debug info and continue
      logger.debug(TEXT_NO_GLOW_UP_POPUP);
    }
  }
}
