package net.loncarevic.utils;

import org.openqa.selenium.By;

public class LocatorUtils {
  public static By byDataTestId(String testId) {
    return By.cssSelector("[data-test-id='" + testId + "']");
  }
}
