// filename: tests/BaseTest.java
package tests;

import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static tests.Config.*;

public class BaseTest {

    protected WebDriver driver;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Exception {
            if (browserName.equals("chrome")) {
                System.setProperty("webdriver.chrome.driver", "src/test/java/drivers/chromedriver");
                ChromeOptions browserOptions = new ChromeOptions();
                browserOptions.setCapability("browserVersion", "86.0");
                driver = new ChromeDriver(browserOptions);
            } else if (browserName.equals("firefox")) {
                System.setProperty("webdriver.gecko.driver",
                        System.getProperty("webdriver.gecko.driver", "src/test/java/drivers/geckodriver"));
                driver = new FirefoxDriver();
            }
        }

        @Override
        protected void after() {
            driver.quit();
        }

    };
}
