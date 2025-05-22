package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtility.class);

    public static String takeScreenshot(WebDriver driver, String subDir, String screenshotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String sanitizedFileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String relativeDir = "screenshots" + File.separator + subDir;
        String relativePath = relativeDir + File.separator + sanitizedFileName + "_" + timestamp + ".png";
        String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;
        try {
            File dir = new File(System.getProperty("user.dir") + File.separator + relativeDir);
            if (!dir.exists()) dir.mkdirs();
            FileUtils.copyFile(src, new File(absolutePath));
            logger.info("Screenshot saved successfully: " + absolutePath);
        } catch (IOException e) {
            logger.error("Failed to save screenshot to path: " + absolutePath, e);
            throw e;
        }
        return "../" + relativePath.replace("\\", "/");
    }

    public static String takeScreenshotAsBase64(WebDriver driver) throws WebDriverException {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String base64 = ts.getScreenshotAs(OutputType.BASE64);
            logger.info("Screenshot captured successfully as Base64");
            return base64;
        } catch (WebDriverException e) {
            logger.error("Failed to take screenshot as Base64", e);
            throw e;
        }
    }

    public static String takeElementScreenshot(WebElement element, String subDir, String screenshotName) throws IOException {
        File src = element.getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String sanitizedFileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String relativeDir = "screenshots" + File.separator + subDir;
        String relativePath = relativeDir + File.separator + sanitizedFileName + "_" + timestamp + ".png";
        String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;
        try {
            File dir = new File(System.getProperty("user.dir") + File.separator + relativeDir);
            if (!dir.exists()) dir.mkdirs();
            FileUtils.copyFile(src, new File(absolutePath));
            logger.info("Element screenshot saved successfully: " + absolutePath);
        } catch (IOException e) {
            logger.error("Failed to save element screenshot to path: " + absolutePath, e);
            throw e;
        }
        return "../" + relativePath.replace("\\", "/");
    }

    public static String takeElementScreenshotAsBase64(WebElement element) throws WebDriverException {
        try {
            String base64 = element.getScreenshotAs(OutputType.BASE64);
            logger.info("Element screenshot captured successfully as Base64");
            return base64;
        } catch (WebDriverException e) {
            logger.error("Failed to take element screenshot as Base64", e);
            throw e;
        }
    }
}