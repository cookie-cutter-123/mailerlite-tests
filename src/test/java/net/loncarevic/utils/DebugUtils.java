package net.loncarevic.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

public class DebugUtils {

  /**
   * Dumps the current page source to console and optionally saves a screenshot.
   *
   * @param driver The WebDriver instance
   * @param label A label to identify your debug output
   * @param screenshot If true, also takes a screenshot
   */
  public static void debugPage(WebDriver driver, String label, boolean screenshot) {
    System.out.println("========== DEBUG START: " + label + " ==========");
    System.out.println("URL: " + driver.getCurrentUrl());
    System.out.println("Title: " + driver.getTitle());
    System.out.println("Page Source:");
    System.out.println(driver.getPageSource());
    System.out.println("========== DEBUG END: " + label + " ==========");

    if (screenshot && driver instanceof TakesScreenshot) {
      File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      // Just an example location; adjust as needed
      File destFile = new File("debug-" + label + ".png");
      try {
        FileUtils.copyFile(tempFile, destFile);
        System.out.println("Saved screenshot to: " + destFile.getAbsolutePath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
