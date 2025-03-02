package net.loncarevic.utils;

public class Constants {
  public static final String URL_DASHBOARD = "https://dashboard.mailerlite.com";
  public static final String TITLE_LOGIN = "Login | MailerLite";
  public static final String MSG_LOGIN_TITLE_NULL = "Login page title is null.";
  public static final String MSG_UNEXPECTED_LOGIN_TITLE = "Unexpected login page title: ";
  public static final String MSG_LOGIN_PAGE_SOURCE_NULL = "Login page source is null.";
  public static final String TEXT_WELCOME_BACK = "Welcome back";
  public static final String MSG_WELCOME_BACK_MISSING = "Welcome back text is missing";
  public static final String TEXT_DONT_HAVE_ACCOUNT = "Don’t have an account?";
  public static final String MSG_DONT_HAVE_ACCOUNT_MISSING =
      "Don't have an account text is missing";
  public static final String TEXT_SIGN_UP = "Sign up";
  public static final String MSG_SIGN_UP_MISSING = "Sign up text is missing";
  public static final String TEXT_REMEMBER_ME = "Remember me for 7 days";
  public static final String MSG_REMEMBER_ME_MISSING = "Remember me checkbox text is missing";
  public static final String TEXT_FORGOT_PASSWORD = "Forgot your password?";
  public static final String MSG_FORGOT_PASSWORD_MISSING = "Forgot password link is missing";
  public static final String TEXT_LOGIN_BUTTON = "Login";
  public static final String MSG_LOGIN_BUTTON_MISSING = "Login button text is missing";
  public static final String TEXT_LOGIN_ISSUE = "I can't login to my account";
  public static final String MSG_LOGIN_ISSUE_MISSING = "Login issue text is missing";
  public static final String CSS_EMAIL_FIELD = "input#email[type='email']";
  public static final String MSG_EMAIL_FIELD_NOT_VISIBLE = "Email field is not visible";
  public static final String CSS_PASSWORD_FIELD = "input#password[type='password']";
  public static final String MSG_PASSWORD_FIELD_NOT_VISIBLE = "Password field is not visible";
  public static final String ENV_MAILERLITE_COOKIE = "MAILERLITE_COOKIE";
  public static final String MSG_ENV_MAILERLITE_COOKIE_NOT_SET =
      "MAILERLITE_COOKIE environment variable not set";
  public static final String COOKIE_NAME = "mailerlite_session";
  public static final String COOKIE_DOMAIN = ".mailerlite.com";
  public static final String MSG_PAGE_TITLE_NULL_AFTER_COOKIE =
      "Page title is null after injecting session cookie.";
  public static final String WARN_SESSION_EXPIRED =
      "Session expired! You need to log in and update the cookie.";
  public static final String INFO_SESSION_VALID = "Session is valid.";
  public static final String XPATH_REJECT_ALL_BUTTON = "//button[contains(text(), 'Reject all')]";
  public static final String TEXT_NO_COOKIE_POPUP = "No cookie popup found, continuing...";
  public static final String XPATH_GLOW_UP_POPUP =
      "//h2[contains(text(), 'A glow up for your pop-ups!')]";
  public static final String XPATH_NO_THANKS_BUTTON = "//button[contains(., 'No thanks')]";
  public static final String TEXT_NO_GLOW_UP_POPUP = "No 'glow-up' popup found, continuing...";
  public static final String TEXT_DASHBOARD = "Dashboard";
  public static final String XPATH_H1_DASHBOARD = "//h1[contains(text(), 'Dashboard')]";
  public static final String MSG_DASHBOARD_TITLE_NULL = "Dashboard page title is null.";
  public static final String MSG_UNEXPECTED_DASHBOARD_TITLE = "Unexpected dashboard title: ";
  public static final String TITLE_CAMPAIGNS = "Campaigns | MailerLite";
  public static final String MSG_CAMPAIGN_PAGE_TITLE_NULL = "Campaign page title is null.";
  public static final String MSG_UNEXPECTED_CAMPAIGN_TITLE = "Unexpected Campaign page title: ";
  public static final String CSS_CREATE_CAMPAIGN_BUTTON = "[data-test-id='create-campaign-button']";
  public static final String XPATH_CAMPAIGNS_LINK =
      "//a[contains(@href, '/campaigns/status') and .//span[text()='Campaigns']]";
  public static final String XPATH_REGULAR_CAMPAIGN_OPTION =
      "//h3[text()='Regular campaign']/ancestor::button";
  public static final String CSS_CAMPAIGN_NAME_INPUT = "[data-test-id='campaign-name-input']";
  public static final String TEXT_CAMPAIGN_NAME = "Automated Campaign";
  public static final String CSS_SUBJECT_INPUT = "[data-test-id='subject-input']";
  public static final String TEXT_CAMPAIGN_SUBJECT = "Test Campaign Subject";
  public static final String CSS_RECIPIENTS_SELECTION_BOX =
      "[data-test-id='recipients-selection-box']";
  public static final String CSS_SUBSCRIBER_GROUP_ROW =
      "[data-test-id='subscriber-group-row'] input[type='checkbox']";
  public static final String CSS_SAVE_RECIPIENTS_BUTTON = "[data-test-id='save-recipients-button']";
  public static final String CSS_CREATE_CAMPAIGN_NEXT_BUTTON =
      "[data-test-id='create-campaign-next-button']";
  public static final String CSS_START_FROM_SCRATCH_TAB = "[data-test-id='start-from-scratch-tab']";
  public static final String CSS_DRAG_DROP_EDITOR = "[data-test-id='drag-drop-editor']";
  public static final String XPATH_DONE_EDITING_BUTTON = "//button[contains(@class, 'btn-green')]";
  public static final String TEXT_DONE_EDITING = "Done editing";
  public static final String CSS_SUBJECT_ELEMENT = "[data-test-id='subject'] p:last-child";
  public static final String MSG_SUBJECT_INCORRECT = "Subject text is incorrect.";
  public static final String CSS_SENDER_ELEMENT = "[data-test-id='sender'] p:last-child";
  public static final String MSG_SENDER_INCORRECT = "Sender text is incorrect.";
  public static final String CSS_REPLY_TO_ELEMENT = "[data-test-id='reply-to'] p:last-child";
  public static final String MSG_REPLY_TO_INCORRECT = "Reply-to text is incorrect.";
  public static final String CSS_CAMPAIGN_LANGUAGE_ELEMENT =
      "[data-test-id='campaign-language'] p:last-child";
  public static final String MSG_CAMPAIGN_LANGUAGE_INCORRECT =
      "Campaign language text is incorrect.";
  public static final String CSS_TRACKING_OPTIONS = "[data-test-id='tracking-options']";
  public static final String MSG_TRACKING_OPTIONS_INCORRECT = "Tracking options text is incorrect.";
  public static final String TEXT_REVIEW_AND_SCHEDULE = "Review and schedule";
  public static final String CSS_RECIPIENTS_SECTION = "[data-test-id='recipents-section']";
  public static final String XPATH_GROUPS_LABEL = ".//span[contains(text(), 'Groups:')]";
  public static final String XPATH_GROUP_NAME = ".//span[contains(text(), 'subs')]";
  public static final String TEXT_RECIPIENTS = "Groups: subs";
  public static final String MSG_RECIPIENTS_SECTION_INCORRECT =
      "Recipients section text is incorrect.";
  public static final String TEXT_TRACK_OPENS_ENABLED = "Track opens: Enabled";
  public static final String[] CLICKABLE_BUTTONS = {
    "subject-generator-button",
    "select-recipients-button",
    "send-test-email-button",
    "edit-content-button",
    "preview-email-button",
    "preview-plain-text",
    "edit-plain-text",
    "send-now-button",
    "send-later-button",
    "send-timezone-button",
    "smart-sending-button",
    "button-back"
  };
  public static final String CSS_BUTTON_SEND = "[data-test-id='button-send']";
  public static final String TEXT_SENDER = "Lončarević (igor@loncarevic.net)";
  public static final String TEXT_SENDER_LASTNAME = "Lončarević";
  public static final String TEXT_REPLY_TO = "igor@loncarevic.net";
  public static final String TEXT_CAMPAIGN_LANGUAGE = "English";
  public static final String MSG_TEST_EXECUTION_FAILED = "Test execution failed";
  public static final String MSG_RECIPIENTS_EXTRACTED = "Recipients section extracted: {} {}";
  public static final String MSG_BUTTON_PREFIX = "Button ";
  public static final String MSG_BUTTON_SUFFIX = " is not displayed";

  // New constants for EmailAssertions magic strings
  public static final String ASSERT_FAIL_EMAIL_NOT_RECEIVED =
      "Email with subject [%s] and sender [%s] was not received in Mailinator.";
  public static final String ASSERT_SENDER_MISMATCH = "Sender does not match!";
  public static final String ASSERT_SUBJECT_MISMATCH = "Subject does not match!";
  public static final String ASSERT_NO_NEWSLETTER_URL =
      "No newsletter URL found in the email body!";
  public static final String ASSERT_NEWSLETTER_CONTENT_NOT_FOUND =
      "Expected newsletter content not found on the webpage!";
}
