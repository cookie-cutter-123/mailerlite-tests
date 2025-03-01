package net.loncarevic.pageobjects;

import static net.loncarevic.utils.Constants.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class CampaignsPage {

  private static final Logger logger = LoggerFactory.getLogger(CampaignsPage.class);

  private final WebDriver driver;
  private final WebDriverWait wait;

  public CampaignsPage(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /** Click on the "Campaigns" link. */
  public CampaignsPage clickCampaignsLink() {
    WebElement campaignsLink =
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_CAMPAIGNS_LINK)));
    campaignsLink.click();
    return this;
  }

  /** Assert the Campaigns page title. */
  public CampaignsPage assertCampaignsTitle() {
    String campaignTitle = driver.getTitle();
    Assert.assertNotNull(campaignTitle, MSG_CAMPAIGN_PAGE_TITLE_NULL);
    Assert.assertTrue(
        campaignTitle.contains(TITLE_CAMPAIGNS), MSG_UNEXPECTED_CAMPAIGN_TITLE + campaignTitle);
    return this;
  }

  /** Click on "Create Campaign" and select "Regular Campaign". */
  public CampaignsPage createRegularCampaign() {
    // Click on "Create Campaign"
    WebElement createCampaignButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_CREATE_CAMPAIGN_BUTTON)));
    createCampaignButton.click();

    // Select "Regular Campaign"
    WebElement regularCampaignOption =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath(XPATH_REGULAR_CAMPAIGN_OPTION)));
    regularCampaignOption.click();

    return this;
  }

  /** Fill in the Campaign name and subject. */
  public CampaignsPage fillCampaignNameAndSubject() {
    // Fill in the Campaign name
    WebElement campaignNameParent = driver.findElement(By.cssSelector(CSS_CAMPAIGN_NAME_INPUT));
    campaignNameParent.click();
    WebElement campaignNameField = campaignNameParent.findElement(By.tagName("input"));
    campaignNameField.sendKeys(TEXT_CAMPAIGN_NAME);

    // Fill in the Campaign subject
    WebElement subjectParent = driver.findElement(By.cssSelector(CSS_SUBJECT_INPUT));
    subjectParent.click();
    WebElement subjectField = subjectParent.findElement(By.tagName("input"));
    subjectField.sendKeys(TEXT_CAMPAIGN_SUBJECT);

    return this;
  }

  /** Select the group of subscribers. */
  public CampaignsPage selectSubscribersGroup() {
    // Select the group of subscribers
    WebElement recipientsButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_RECIPIENTS_SELECTION_BOX)));

    // Open recipients
    Actions actions = new Actions(driver);
    actions.sendKeys(Keys.PAGE_DOWN).perform();
    actions.moveToElement(recipientsButton).perform();
    recipientsButton.click();

    // Tick a subscribers group
    WebElement checkbox =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_SUBSCRIBER_GROUP_ROW)));
    checkbox.click();

    // Save recipients
    WebElement saveRecipientsButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_SAVE_RECIPIENTS_BUTTON)));
    saveRecipientsButton.click();

    return this;
  }

  /** Click Continue. */
  public CampaignsPage clickContinue() {
    WebElement nextButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector(CSS_CREATE_CAMPAIGN_NEXT_BUTTON)));
    nextButton.click();

    return this;
  }

  /** Click on the Start from scratch tab, then Drag & drop editor. */
  public CampaignsPage startFromScratchAndDragDrop() {
    // Click on the Start from scratch tab
    WebElement startFromScratchTab =
        wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_START_FROM_SCRATCH_TAB)));
    startFromScratchTab.click();

    // Click on Drag & drop editor
    WebElement dragAndDropEditor =
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_DRAG_DROP_EDITOR)));
    dragAndDropEditor.click();

    return this;
  }

  /**
   * Locate the green button, wait until "Done editing", ensure the button is visible/clickable,
   * etc.
   */
  public CampaignsPage doneEditing() {
    // Locate the green button
    WebElement doneEditingButton =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_DONE_EDITING_BUTTON)));

    // Wait until "Done editing" text is visible inside the button
    wait.until(ExpectedConditions.textToBePresentInElement(doneEditingButton, TEXT_DONE_EDITING));
    // Wait until the button is not disabled
    wait.until(
        ExpectedConditions.not(
            ExpectedConditions.attributeToBe(doneEditingButton, "disabled", "true")));

    // Ensure the button is visible and clickable
    ((JavascriptExecutor) driver)
        .executeScript("arguments[0].scrollIntoView({block: 'center'});", doneEditingButton);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", doneEditingButton);

    return this;
  }

  /** Asserts the final Review and schedule page content. */
  public CampaignsPage assertReviewAndScheduleSubPage() { // TODO split into more methods
    // Wait for the "Review and schedule" page to load
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h1[contains(text(), '" + TEXT_REVIEW_AND_SCHEDULE + "')]"))); // TODO

    // Assert text content
    Assert.assertEquals(
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(CSS_SUBJECT_ELEMENT)))
            .getText()
            .trim(),
        TEXT_CAMPAIGN_SUBJECT,
        MSG_SUBJECT_INCORRECT);

    Assert.assertEquals(
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(CSS_SENDER_ELEMENT)))
            .getText()
            .trim(),
        TEXT_SENDER,
        MSG_SENDER_INCORRECT);

    Assert.assertEquals(
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(CSS_REPLY_TO_ELEMENT)))
            .getText()
            .trim(),
        TEXT_REPLY_TO,
        MSG_REPLY_TO_INCORRECT);

    Assert.assertEquals(
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(CSS_CAMPAIGN_LANGUAGE_ELEMENT)))
            .getText()
            .trim(),
        TEXT_CAMPAIGN_LANGUAGE,
        MSG_CAMPAIGN_LANGUAGE_INCORRECT);

    Assert.assertTrue(
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(CSS_TRACKING_OPTIONS)))
            .getText()
            .contains(TEXT_TRACK_OPENS_ENABLED),
        MSG_TRACKING_OPTIONS_INCORRECT);

    // Locate recipients
    WebElement recipientsElement =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(CSS_RECIPIENTS_SECTION)));

    // Extract individual spans
    WebElement groupsLabel = recipientsElement.findElement(By.xpath(XPATH_GROUPS_LABEL));
    WebElement groupName = recipientsElement.findElement(By.xpath(XPATH_GROUP_NAME));

    // Get text and trim any extra spaces
    String groupsText = groupsLabel.getText().trim();
    String groupValue = groupName.getText().trim();

    // Log extracted values for debugging
    logger.debug(MSG_RECIPIENTS_EXTRACTED, groupsText, groupValue);
    Assert.assertEquals(
        groupsText + " " + groupValue, TEXT_RECIPIENTS, MSG_RECIPIENTS_SECTION_INCORRECT);

    // Assert buttons are clickable (except the last one)
    for (String buttonId : CLICKABLE_BUTTONS) {
      WebElement button =
          wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.cssSelector("[data-test-id='" + buttonId + "']")));
      Assert.assertTrue(button.isDisplayed(), MSG_BUTTON_PREFIX + buttonId + MSG_BUTTON_SUFFIX);
    }
    return this;
  }

  /** Click the "Send" button. */
  public CampaignsPage clickSendButton() {
    WebElement sendButton =
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(CSS_BUTTON_SEND)));
    sendButton.click(); // TODO uncomment
    return this;
  }
}
