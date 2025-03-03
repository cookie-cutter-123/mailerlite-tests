package net.loncarevic;

import static net.loncarevic.utils.Constants.TEXT_CAMPAIGN_SUBJECT;
import static net.loncarevic.utils.Constants.TEXT_SENDER_LASTNAME;

import net.loncarevic.assertions.EmailAssertions;
import net.loncarevic.base.BaseTest;
import net.loncarevic.pages.UnsubscribePage;
import net.loncarevic.utils.EmailUtils;
import org.testng.annotations.Test;

public class TestCase0002 extends BaseTest {

  @Test
  public void testUnsubscribeFlow() throws Exception {
    // Fetch the latest email and extract the unsubscribe link
    String emailBody =
        EmailAssertions.getEmailBody(TEXT_SENDER_LASTNAME, TEXT_CAMPAIGN_SUBJECT, 60);
    String unsubscribeLink = EmailUtils.extractUrlFromText(emailBody);

    // Open the unsubscribe link
    assert unsubscribeLink != null;
    driver.get(unsubscribeLink);

    // Perform the unsubscribe flow using the UnsubscribePage
    new UnsubscribePage(driver, wait)
        .clickUnsubscribeButton()
        .clickYesButtonIfPresent()
        .selectUnsubscribeReason()
        .clickSubmitButton()
        .assertUnsubscriptionSuccess();
  }
}

// TODO subs assertions
// TODO restructure this
// TODO restructure EmailAssertions
// TODO Prepare TC1 with adding an email to subs!
