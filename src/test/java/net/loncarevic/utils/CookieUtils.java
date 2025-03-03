package net.loncarevic.utils;

import static net.loncarevic.utils.Constants.*;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/** Provides utility methods for handling cookies in Selenium tests. */
public class CookieUtils {
  private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

  /**
   * Injects the session cookie into the browser to bypass login. Reads the cookie from the
   * environment and adds it to the current session.
   *
   * @param driver the WebDriver instance.
   */
  public static void injectSessionIntoSelenium(WebDriver driver) {
    // Read the session cookie from an environment variable
    String cookieValue = System.getenv(ENV_MAILERLITE_COOKIE);
    if (cookieValue == null) {
      throw new RuntimeException(MSG_ENV_MAILERLITE_COOKIE_NOT_SET);
    }

    // Navigate to the dashboard
    driver.get(URL_DASHBOARD);

    // Add the session cookie
    Cookie sessionCookie = new Cookie(COOKIE_NAME, cookieValue);
    driver.manage().addCookie(sessionCookie);

    // Refresh to apply the session
    driver.navigate().refresh();

    // Verify if the cookie is still valid
    String pageTitle = driver.getTitle();
    Assert.assertNotNull(pageTitle, MSG_PAGE_TITLE_NULL_AFTER_COOKIE);
    if (pageTitle.contains(TITLE_LOGIN)) {
      logger.warn(WARN_SESSION_EXPIRED);
    } else {
      logger.info(INFO_SESSION_VALID);
    }
  }
}
