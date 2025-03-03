package net.loncarevic.utils;

import static net.loncarevic.utils.Constants.*;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Provides utility methods for dismissing pop-ups on the dashboard. */
public class PopUpUtils {
  private static final Logger logger = LoggerFactory.getLogger(PopUpUtils.class);

  /**
   * Dismisses the cookie consent pop-up if it is present.
   *
   * @param wait The WebDriverWait instance to wait for elements.
   */
  public static void dismissCookiePopupIfPresent(WebDriverWait wait) {
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
  }

  /**
   * Dismisses the "Glow up" pop-up if it is present. If clicking the "No thanks" button is
   * intercepted by another overlay (such as the cookie banner), it dismisses the cookie banner
   * first and retries.
   *
   * @param wait The WebDriverWait instance to wait for elements.
   */
  public static void dismissGlowUpPopupIfPresent(WebDriverWait wait) {
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
        dismissCookiePopupIfPresent(wait);
        noThanksButton.click();
      }
      wait.until(ExpectedConditions.invisibilityOf(popupModal));

    } catch (TimeoutException e) {
      // If no modal is found, log debug info and continue
      logger.debug(TEXT_NO_GLOW_UP_POPUP);
    }
  }
}
