package net.loncarevic.pages;

import static net.loncarevic.utils.Constants.*;
import static net.loncarevic.utils.PopUpUtils.dismissCookiePopupIfPresent;
import static net.loncarevic.utils.PopUpUtils.dismissGlowUpPopupIfPresent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardPage {

  private final WebDriver driver;
  private final WebDriverWait wait;

  public DashboardPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /** Navigates to the dashboard explicitly. */
  public DashboardPage openDashboard() {
    driver.get(URL_DASHBOARD);
    return this;
  }

  /** Close cookie popup if present. Close the "A glow up for your pop-ups!" modal if present. */
  public DashboardPage dismissPopups() {
    // Close cookie popup if present
    dismissCookiePopupIfPresent(wait);

    // Close the "A glow up for your pop-ups!" modal
    dismissGlowUpPopupIfPresent(wait);
    return this;
  }

  /** Verify successful login by checking the Dashboard title. */
  public DashboardPage assertDashboardTitle() {
    // Wait for h1
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_H1_DASHBOARD)));

    // Verify successful login by checking the Dashboard title
    String dashboardTitle = driver.getTitle();
    Assert.assertNotNull(dashboardTitle, MSG_DASHBOARD_TITLE_NULL);
    Assert.assertTrue(
        dashboardTitle.contains(TEXT_DASHBOARD), MSG_UNEXPECTED_DASHBOARD_TITLE + dashboardTitle);
    return this;
  }
}
