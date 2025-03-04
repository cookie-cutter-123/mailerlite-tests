package net.loncarevic.base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

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

//      // Run headless in CI to avoid UI dependency
//      options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
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
}
