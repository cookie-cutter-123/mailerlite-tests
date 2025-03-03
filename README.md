# MailerLite Automation

1. **Setup:**
    - Clone repo, install Java 17+ and Maven.
    - Export `MAILERLITE_COOKIE` (session cookie), `MAILINATOR_DOMAIN`, `MAILINATOR_API_TOKEN`.

2. **Run Tests Locally:**
    - `mvn clean test`

3. **Structure:**
    - `TestCase0001` creates/sends a campaign and verifies the email.
    - `TestCase0002` unsubscribes and checks the subscriber is marked as "Unsubscribed."

4. **CI:**
    - GitHub Actions (`.github/workflows/run-tests.yml`) automatically checks out code, sets Java 17, installs dependencies, and runs tests headless.
