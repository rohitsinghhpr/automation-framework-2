package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.driver.DriverManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportListener implements ITestListener {

    public void configureReport() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportFileName = "TestReport_" + timeStamp + ".html";
        String reportsDirPath = System.getProperty("user.dir") + "/reports";

        File reportsDir = new File(reportsDirPath);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportsDirPath + "/" + reportFileName);
        sparkReporter.config().setDocumentTitle(TestConstants.REPORT_TITLE);
        sparkReporter.config().setReportName(TestConstants.REPORT_NAME);

        if ("DARK".equalsIgnoreCase(TestConstants.REPORT_THEME)) {
            sparkReporter.config().setTheme(Theme.DARK);
        } else if ("STANDARD".equalsIgnoreCase(TestConstants.REPORT_THEME)) {
            sparkReporter.config().setTheme(Theme.STANDARD);
        } else {
            // Default theme if nothing is configured
            sparkReporter.config().setTheme(Theme.STANDARD);
        }


        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Host Name",TestConstants.HOST_NAME);
        extent.setSystemInfo("Environment", TestConstants.ENVIRONMENT);
        extent.setSystemInfo("User", TestConstants.USER);
    }

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getExtentTest() {
        return test.get();
    }

    public static void logPassWithScreenshot(String stepName) {
        try {
            String screenshotFormat = TestConstants.SCREENSHOT_FORMAT;
            if ("base64".equalsIgnoreCase(screenshotFormat)) {
                String base64Screenshot = ScreenshotUtility.takeScreenshotAsBase64(DriverManager.getDriver());
                getExtentTest().pass(stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else if ("png".equalsIgnoreCase(screenshotFormat)) {
                String screenshotPath = ScreenshotUtility.takeScreenshot(DriverManager.getDriver(), "passed", stepName);
                getExtentTest().pass(stepName, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                getExtentTest().pass(stepName);
                getExtentTest().log(Status.WARNING, "Invalid screenshot format in config: " + screenshotFormat);
            }
        } catch (Exception e) {
            getExtentTest().pass(stepName);
            e.printStackTrace();
            getExtentTest().log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
        }
    }

    public static void logPassWithElementScreenshot(String stepName, WebElement element) {
        try {
            String screenshotFormat = TestConstants.SCREENSHOT_FORMAT;
            if ("base64".equalsIgnoreCase(screenshotFormat)) {
                String base64Screenshot = ScreenshotUtility.takeElementScreenshotAsBase64(element);
                getExtentTest().pass(stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else if ("png".equalsIgnoreCase(screenshotFormat)) {
                String screenshotPath = ScreenshotUtility.takeElementScreenshot(element, "passed", stepName);
                getExtentTest().pass(stepName, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else {
                getExtentTest().pass(stepName);
                getExtentTest().log(Status.WARNING, "Invalid screenshot format in config: " + screenshotFormat);
            }
        } catch (Exception e) {
            getExtentTest().pass(stepName);
            e.printStackTrace();
            getExtentTest().log(Status.WARNING, "Could not capture element screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        configureReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        extentTest.log(Status.INFO, "Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
        test.get().log(Status.FAIL, result.getThrowable());
        if (DriverManager.getDriver() != null) {
            try {
                String screenshotFormat = TestConstants.SCREENSHOT_FORMAT;
                if ("base64".equalsIgnoreCase(screenshotFormat)) {
                    String base64Screenshot = ScreenshotUtility.takeScreenshotAsBase64(DriverManager.getDriver());
                    test.get().addScreenCaptureFromBase64String(base64Screenshot, "Failed Screenshot");

                } else if ("png".equalsIgnoreCase(screenshotFormat)) {
                    String screenshotPath = ScreenshotUtility.takeScreenshot(
                            DriverManager.getDriver(), "failed", result.getMethod().getMethodName()
                    );
                    test.get().addScreenCaptureFromPath(screenshotPath);

                } else {
                    test.get().log(Status.WARNING, "Invalid screenshot format in config: " + screenshotFormat);
                }
            } catch (Exception e) {
                e.printStackTrace();
                test.get().log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
