package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.wapper.IWaitUtility;

import java.time.Duration;
import java.util.Set;

public final class WaitUtility implements IWaitUtility {

    private static final Logger logger = LogManager.getLogger(WaitUtility.class);

    private static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public static void waitForVisibility(WebDriver driver, WebElement element, int timeoutSeconds, String elementName) {
        logger.info("Waiting for visibility of element: {}", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
            logger.info("Element '{}' is visible.", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for visibility of element '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForVisibility(WebDriver driver, By locator, int timeoutSeconds, String elementName) {
        logger.info("Waiting for visibility of element: {}", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element '{}' is visible.", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for visibility of element '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForClickability(WebDriver driver, WebElement element, int timeoutSeconds, String elementName) {
        logger.info("Waiting for element to be clickable: {}", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeClickable(element));
            logger.info("Element '{}' is clickable.", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for clickability of element '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForClickability(WebDriver driver, By locator, int timeoutSeconds, String elementName) {
        logger.info("Waiting for element to be clickable: {}", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element '{}' is clickable.", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for clickability of element '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForPresence(WebDriver driver, By locator, int timeoutSeconds, String elementName) {
        logger.info("Waiting for presence of element: {}", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Element '{}' is present.", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for presence of element '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForInvisibility(WebDriver driver, WebElement element, int timeoutSeconds, String elementName) {
        logger.info("Waiting for invisibility of element: {}", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.invisibilityOf(element));
            logger.info("Element '{}' is invisible.", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for invisibility of element '{}': {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForTextToBePresent(WebDriver driver, WebElement element, String text, int timeoutSeconds, String elementName) {
        logger.info("Waiting for text '{}' to be present in element: {}", text, elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.textToBePresentInElement(element, text));
            logger.info("Text '{}' is present in element '{}'.", text, elementName);
        } catch (Exception e) {
            logger.error("Error waiting for text '{}' in element '{}': {}", text, elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForTextToBeAbsent(WebDriver driver, WebElement element, String text, int timeoutSeconds, String elementName) {
        logger.info("Waiting for text '{}' to be absent in element: {}", text, elementName);
        try {
            getWait(driver, timeoutSeconds).until(driver1 -> !element.getText().contains(text));
            logger.info("Text '{}' is absent in element '{}'.", text, elementName);
        } catch (Exception e) {
            logger.error("Error waiting for absence of text '{}' in element '{}': {}", text, elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForAttributeToBe(WebDriver driver, WebElement element, String attribute, String value, int timeoutSeconds, String elementName) {
        logger.info("Waiting for element '{}' attribute '{}' to be '{}'", elementName, attribute, value);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.attributeToBe(element, attribute, value));
            logger.info("Element '{}' attribute '{}' is '{}'.", elementName, attribute, value);
        } catch (Exception e) {
            logger.error("Error waiting for attribute '{}' to be '{}' in element '{}': {}", attribute, value, elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForAttributeContains(WebDriver driver, WebElement element, String attribute, String value, int timeoutSeconds, String elementName) {
        logger.info("Waiting for element '{}' attribute '{}' to contain '{}'", elementName, attribute, value);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.attributeContains(element, attribute, value));
            logger.info("Element '{}' attribute '{}' contains '{}'.", elementName, attribute, value);
        } catch (Exception e) {
            logger.error("Error waiting for attribute '{}' to contain '{}' in element '{}': {}", attribute, value, elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForFrameToBeAvailableAndSwitchToIt(WebDriver driver, By frameLocator, int timeoutSeconds, String frameName) {
        logger.info("Waiting for frame '{}' to be available and switching to it", frameName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
            logger.info("Switched to frame '{}'.", frameName);
        } catch (Exception e) {
            logger.error("Error waiting for frame '{}': {}", frameName, e.getMessage());
            throw e;
        }
    }

    public static void waitForNumberOfElementsToBe(WebDriver driver, By locator, int number, int timeoutSeconds, String elementName) {
        logger.info("Waiting for number of elements '{}' to be {}", elementName, number);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.numberOfElementsToBe(locator, number));
            logger.info("Number of elements '{}' is {}", elementName, number);
        } catch (Exception e) {
            logger.error("Error waiting for number of elements '{}' to be {}: {}", elementName, number, e.getMessage());
            throw e;
        }
    }

    public static void waitForNumberOfElementsToBeMoreThan(WebDriver driver, By locator, int number, int timeoutSeconds, String elementName) {
        logger.info("Waiting for number of elements '{}' to be more than {}", elementName, number);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
            logger.info("Number of elements '{}' is more than {}", elementName, number);
        } catch (Exception e) {
            logger.error("Error waiting for number of elements '{}' to be more than {}: {}", elementName, number, e.getMessage());
            throw e;
        }
    }

    public static void waitForNumberOfElementsToBeLessThan(WebDriver driver, By locator, int number, int timeoutSeconds, String elementName) {
        logger.info("Waiting for number of elements '{}' to be less than {}", elementName, number);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.numberOfElementsToBeLessThan(locator, number));
            logger.info("Number of elements '{}' is less than {}", elementName, number);
        } catch (Exception e) {
            logger.error("Error waiting for number of elements '{}' to be less than {}: {}", elementName, number, e.getMessage());
            throw e;
        }
    }

    public static void waitForUrlToBe(WebDriver driver, String url, int timeoutSeconds) {
        logger.info("Waiting for URL to be '{}'", url);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.urlToBe(url));
            logger.info("URL is '{}'", url);
        } catch (Exception e) {
            logger.error("Error waiting for URL to be '{}': {}", url, e.getMessage());
            throw e;
        }
    }

    public static void waitForUrlContains(WebDriver driver, String fraction, int timeoutSeconds) {
        logger.info("Waiting for URL to contain '{}'", fraction);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.urlContains(fraction));
            logger.info("URL contains '{}'", fraction);
        } catch (Exception e) {
            logger.error("Error waiting for URL to contain '{}': {}", fraction, e.getMessage());
            throw e;
        }
    }

    public static void waitForTitleToBe(WebDriver driver, String title, int timeoutSeconds) {
        logger.info("Waiting for page title to be '{}'", title);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.titleIs(title));
            logger.info("Page title is '{}'", title);
        } catch (Exception e) {
            logger.error("Error waiting for page title to be '{}': {}", title, e.getMessage());
            throw e;
        }
    }

    public static void waitForTitleContains(WebDriver driver, String titleFragment, int timeoutSeconds) {
        logger.info("Waiting for page title to contain '{}'", titleFragment);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.titleContains(titleFragment));
            logger.info("Page title contains '{}'", titleFragment);
        } catch (Exception e) {
            logger.error("Error waiting for page title to contain '{}': {}", titleFragment, e.getMessage());
            throw e;
        }
    }

    public static void waitForAlertPresence(WebDriver driver, int timeoutSeconds) {
        logger.info("Waiting for alert to be present");
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.alertIsPresent());
            logger.info("Alert is present");
        } catch (Exception e) {
            logger.error("Error waiting for alert presence: {}", e.getMessage());
            throw e;
        }
    }

    public static void waitForElementToBeSelected(WebDriver driver, WebElement element, int timeoutSeconds, String elementName) {
        logger.info("Waiting for element '{}' to be selected", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeSelected(element));
            logger.info("Element '{}' is selected", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for element '{}' to be selected: {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForElementToBeDeselected(WebDriver driver, WebElement element, int timeoutSeconds, String elementName) {
        logger.info("Waiting for element '{}' to be deselected", elementName);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.elementSelectionStateToBe(element, false));
            logger.info("Element '{}' is deselected", elementName);
        } catch (Exception e) {
            logger.error("Error waiting for element '{}' to be deselected: {}", elementName, e.getMessage());
            throw e;
        }
    }

    public static void waitForNumberOfWindowsToBe(WebDriver driver, int expectedNumberOfWindows, int timeoutSeconds) {
        logger.info("Waiting for number of windows to be {}", expectedNumberOfWindows);
        try {
            getWait(driver, timeoutSeconds).until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
            logger.info("Number of windows is {}", expectedNumberOfWindows);
        } catch (Exception e) {
            logger.error("Error waiting for number of windows to be {}: {}", expectedNumberOfWindows, e.getMessage());
            throw e;
        }
    }

    public static void waitForWindowToBeAvailableAndSwitchToIt(WebDriver driver, String windowHandleOrTitle, int timeoutSeconds) {
        logger.info("Waiting for window '{}' to be available and switching to it", windowHandleOrTitle);
        try {
            WebDriverWait wait = getWait(driver, timeoutSeconds);

            boolean switched = false;
            try {
                wait.until(driver1 -> {
                    Set<String> handles = driver1.getWindowHandles();
                    if (handles.contains(windowHandleOrTitle)) {
                        driver1.switchTo().window(windowHandleOrTitle);
                        return true;
                    }
                    return false;
                });
                switched = true;
            } catch (Exception ignored) {
            }

            if (!switched) {
                wait.until(driver1 -> {
                    for (String handle : driver1.getWindowHandles()) {
                        driver1.switchTo().window(handle);
                        if (driver1.getTitle().equals(windowHandleOrTitle)) {
                            return true;
                        }
                    }
                    return false;
                });
            }

            logger.info("Switched to window '{}'", windowHandleOrTitle);
        } catch (Exception e) {
            logger.error("Error waiting for window '{}': {}", windowHandleOrTitle, e.getMessage());
            throw e;
        }
    }

    public static void waitForWindowToClose(WebDriver driver, String windowHandle, int timeoutSeconds) {
        logger.info("Waiting for window '{}' to close", windowHandle);
        try {
            getWait(driver, timeoutSeconds).until(driver1 -> !driver1.getWindowHandles().contains(windowHandle));
            logger.info("Window '{}' is closed", windowHandle);
        } catch (Exception e) {
            logger.error("Error waiting for window '{}' to close: {}", windowHandle, e.getMessage());
            throw e;
        }
    }
}
