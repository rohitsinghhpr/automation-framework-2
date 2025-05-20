package tests;

import base_test.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ExtentReportListener;
import utils.driver.DriverManager;

public class LoginPageTest2 extends BaseTest {

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
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            ExtentReportListener.getExtentTest().log(Status.PASS,"navigated to login page");
            ExtentReportListener.logPassWithElementScreenshot("navigated to login page",loginPage.getCodeBlock());

            System.out.println("LoginPageTest2");

        } catch (Exception e) {
            logger.error("Test TC001 failed due to exception: ", e);
            throw new RuntimeException(e);
        }
    }

}