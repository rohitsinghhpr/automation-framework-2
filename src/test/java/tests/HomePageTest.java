package tests;

import base_test.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.ExtentReportListener;
import utils.driver.DriverManager;

public class HomePageTest extends BaseTest {
    @BeforeMethod
    public void setUpBrowser(){
        super.setUpBrowser();
    }
    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser(){
        super.tearDownBrowser();
    }
    @Test
    public void TC001() {
        try {
            HomePage homePage = new HomePage(DriverManager.getDriver());
            String cardName = "Elements";
            ExtentReportListener.getExtentTest().log(Status.PASS,"going to take "+cardName+" screenshot");
            ExtentReportListener.logPassWithElementScreenshot("taking screenshot of "+cardName, homePage.getCard(cardName));
            homePage.clickOnFormsCard();
        } catch (Exception e) {
            logger.error("Test TC001 failed due to exception: ", e);
            throw new RuntimeException(e);
        }
    }
}