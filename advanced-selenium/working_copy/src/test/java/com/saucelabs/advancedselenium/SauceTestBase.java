package test.java.com.saucelabs.advancedselenium;

import com.saucelabs.saucebindings.JobVisibility;
import com.saucelabs.saucebindings.SauceOptions;
import com.saucelabs.saucebindings.SauceSession; 
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo; //a
import org.junit.jupiter.api.extension.ExtendWith; //a
import org.junit.jupiter.api.extension.ExtensionContext; //a
import org.junit.jupiter.api.extension.TestWatcher; //a
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Collections;

@ExtendWith(SauceTestBase.SauceTestWatcher.class) // added

public class SauceTestBase {

    RemoteWebDriver driver = null;
    SauceSession session = null;

    @BeforeEach
    public void setUp(TestInfo testinfo) {  // change method name, added testinfo parameters
        // Create TestWatcher in JUnit5
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("excludeSwitches",
                Collections.singletonList("disable-popup-blocking"));
        if (System.getenv("SELENIUM_PLATFORM") == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(chromeOptions);
        } else if (System.getenv("SELENIUM_PLATFORM").equals("SAUCE")) {
            SauceOptions sauceOptions = new SauceOptions(chromeOptions);
            sauceOptions.setJobVisibility(JobVisibility.PUBLIC);
            sauceOptions.setName(testinfo.getDisplayName()); // added
            SauceSession sauceSession = new SauceSession(sauceOptions);
            driver = sauceSession.start();
        }
        else {
            throw new RuntimeException("You have no environment variable set that specifies the local or remote host");
        }
    }


    @AfterEach
    public void endSession() {
        if (session != null) { // this will be null if you run locally
            session.stop(true);
        } else if (driver != null) { // this is null if there was a problem initializing the driver
            driver.quit();
        }
    }

    public class SauceTestWatcher implements TestWatcher { // entire class added
        private SauceSession session;

        public void setSession(SauceSession session) {
            this.session = session;
        }

        @Override
        protected void testFailed(ExtensionContext context, Throwable cause) {
            session.stop(false);
        }

        @Override
        protected void testSuccessful(ExtensionContext context) {
            session.stop(true);
        }

    }
}