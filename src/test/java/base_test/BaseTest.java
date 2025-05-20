package base_test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import utils.*;
import utils.driver.DriverFactory;
import utils.driver.DriverManager;

@Listeners({ExtentReportListener.class, LogListener.class, RetryListener.class})
public class BaseTest {
    protected final Logger logger = LogManager.getLogger(this.getClass());

    public void setUpBrowser() {
        String browserValue = TestConstants.BROWSER;
        String headlessValue = TestConstants.HEADLESS;
        String browser = (browserValue != null && !browserValue.trim().isEmpty()) ? browserValue.trim() : "chrome";
        boolean headless = Boolean.parseBoolean(headlessValue != null ? headlessValue.trim() : "false");
        String url = TestConstants.BASE_URL;
        WebDriver driver = DriverFactory.createInstance(browser, headless);
        DriverManager.setDriver(driver);
        driver.manage().window().maximize();
        driver.get(url);
        logger.info("Browser launched and navigated to: " + url);
    }
    public void tearDownBrowser() {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (driver != null) {
                logger.info("Quitting browser...");
                driver.quit();
            }
        } catch (Exception e) {
            logger.error("Error while quitting browser", e);
        } finally {
            DriverManager.unload();
            logger.info("Driver unloaded from thread-local storage.");
        }
    }
    @BeforeSuite
    public void makeSureFoldersExits() {
        DirectoryUtility.createFolderIfNotExists(DirectoryUtility.getPath("reports"));
        DirectoryUtility.createFolderIfNotExists(DirectoryUtility.getPath("logs"));
        DirectoryUtility.createFolderIfNotExists(DirectoryUtility.getPath("screenshots"));
        DirectoryUtility.createFolderIfNotExists(DirectoryUtility.getPath("screenshots","passed"));
        DirectoryUtility.createFolderIfNotExists(DirectoryUtility.getPath("screenshots","failed"));
        logger.info("Required folders verified or created.");
    }
}
