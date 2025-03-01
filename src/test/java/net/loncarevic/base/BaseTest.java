package net.loncarevic.base;

import static net.loncarevic.utils.Constants.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

  private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

  protected WebDriver driver;
  protected WebDriverWait wait;

  @BeforeMethod
  public void setUp() throws Exception {
    ChromeOptions options = new ChromeOptions();
    boolean isCI = System.getenv("CI") != null; // Detect if running in GitHub Actions

    if (isCI) {
      // Create a unique temporary directory to prevent session conflicts in CI
      Path tempDir = Files.createTempDirectory("chrome-user-data");
      options.addArguments("--user-data-dir=" + tempDir.toAbsolutePath());

      // Run headless in CI to avoid UI dependency
      options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
    }

    driver = new ChromeDriver(options);
    wait = new WebDriverWait(driver, Duration.ofSeconds(20));
  }

  @AfterMethod
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }

  /**
   * Inject session cookie to bypass login because of the reCAPTCHA.
   *
   * @param driver WebDriver instance
   */
  public void injectSessionIntoSelenium(WebDriver driver) {
    // Read the session cookie from an environment variable
    String cookieValue = System.getenv(ENV_MAILERLITE_COOKIE);
    if (cookieValue == null) {
      throw new RuntimeException(MSG_ENV_MAILERLITE_COOKIE_NOT_SET);
    }

    // Add the session cookie
    Cookie sessionCookie = new Cookie(COOKIE_NAME, cookieValue, COOKIE_DOMAIN, "/", null);
    driver.manage().addCookie(sessionCookie);

    // Verify if the cookie is still valid
    driver.get(URL_DASHBOARD);
    String pageTitle = driver.getTitle();
    Assert.assertNotNull(pageTitle, MSG_PAGE_TITLE_NULL_AFTER_COOKIE);
    if (pageTitle.contains(TITLE_LOGIN)) {
      logger.warn(WARN_SESSION_EXPIRED);
    } else {
      logger.info(INFO_SESSION_VALID);
    }
  }
}
