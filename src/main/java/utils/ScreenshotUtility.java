package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility {

    public static String takeScreenshot(WebDriver driver, String subDir, String screenshotName) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "../" + relativePath.replace("\\", "/");
    }

    public static String takeElementScreenshot(WebElement element, String subDir, String screenshotName) {
        File src = element.getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String sanitizedFileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String relativeDir = "screenshots" + File.separator + subDir;
        String relativePath = relativeDir + File.separator + sanitizedFileName + "_" + timestamp + ".png";
        String absolutePath = System.getProperty("user.dir") + File.separator + relativePath;
        try {
            File dir = new File(System.getProperty("user.dir") + File.separator + relativeDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.copyFile(src, new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "../" + relativePath.replace("\\", "/");
    }

}
