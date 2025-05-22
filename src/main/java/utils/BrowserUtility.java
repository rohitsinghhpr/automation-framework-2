package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import utils.wapper.IBrowserUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BrowserUtility implements IBrowserUtility {

    private static final Logger logger = LogManager.getLogger(BrowserUtility.class);

    public static void clickUsingJS(WebDriver driver, WebElement element, String elementName) {
        try {
            logger.info("Clicking on element using JS: " + elementName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            logger.info("Clicked on element: " + elementName);
        } catch (Exception e) {
            logger.error("Failed to click on element using JS: " + elementName + " - " + e.getMessage());
            throw new RuntimeException("Failed to click on element using JS: " + elementName, e);
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            logger.info("Scrolling to element: " + elementName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: " + elementName);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + elementName + " - " + e.getMessage());
            throw new RuntimeException("Failed to scroll to element: " + elementName, e);
        }
    }

    public static void scrollToElement(WebDriver driver, By locator, String elementName) {
        try {
            WebElement element = driver.findElement(locator);
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            logger.info("Scrolling to element: {}", elementName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {} - {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to scroll to element: " + elementName, e);
        }
    }

    public static void scrollPage(WebDriver driver, int scrollAmount) {
        try {
            logger.info("Scrolling vertically by " + scrollAmount);
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", scrollAmount);
            logger.info("Scrolled vertically by " + scrollAmount);
        } catch (Exception e) {
            logger.error("Failed to scroll vertically: " + e.getMessage());
            throw new RuntimeException("Failed to scroll vertically by " + scrollAmount, e);
        }
    }

    public static void scrollPage(WebDriver driver, int scrollAmountX, int scrollAmountY) {
        try {
            logger.info("Scrolling horizontally by " + scrollAmountX + " and vertically by " + scrollAmountY);
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", scrollAmountX, scrollAmountY);
            logger.info("Scrolled horizontally by " + scrollAmountX + " and vertically by " + scrollAmountY);
        } catch (Exception e) {
            logger.error("Failed to scroll: " + e.getMessage());
            throw new RuntimeException("Failed to scroll by (" + scrollAmountX + ", " + scrollAmountY + ")", e);
        }
    }

    public static void scrollToBottomOfPage(WebDriver driver) {
        try {
            logger.info("Scrolling to bottom of page");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            logger.info("Scrolled to bottom of page");
        } catch (Exception e) {
            logger.error("Failed to scroll to bottom of page - " + e.getMessage());
            throw new RuntimeException("Failed to scroll to bottom of page", e);
        }
    }

    public static void scrollToTopOfPage(WebDriver driver) {
        try {
            logger.info("Scrolling to top of page");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            logger.info("Scrolled to top of page");
        } catch (Exception e) {
            logger.error("Failed to scroll to top of page - " + e.getMessage());
            throw new RuntimeException("Failed to scroll to top of page", e);
        }
    }

    public static void refreshPageUsingJS(WebDriver driver) {
        try {
            logger.info("Refreshing page using JS");
            ((JavascriptExecutor) driver).executeScript("history.go(0)");
            logger.info("Page refreshed");
        } catch (Exception e) {
            logger.error("Failed to refresh page using JS - " + e.getMessage());
            throw new RuntimeException("Failed to refresh page using JS", e);
        }
    }

    public static void focusElementUsingJS(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            logger.info("Focusing on element: " + elementName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", element);
            logger.info("Focused on element: " + elementName);
        } catch (Exception e) {
            logger.error("Failed to focus on element: " + elementName + " - " + e.getMessage());
            throw new RuntimeException("Failed to focus on element: " + elementName, e);
        }
    }

    public static void blurElementUsingJS(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            logger.info("Blurring element: " + elementName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", element);
            logger.info("Blurred element: " + elementName);
        } catch (Exception e) {
            logger.error("Failed to blur element: " + elementName + " - " + e.getMessage());
            throw new RuntimeException("Failed to blur element: " + elementName, e);
        }
    }

    public static boolean isElementInViewport(WebDriver driver, WebElement element, String elementName) {
        try {
            logger.info("Checking if element is in viewport: " + elementName);
            Boolean isInView = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "var elem = arguments[0],                 " +
                            "  box = elem.getBoundingClientRect(),    " +
                            "  cx = box.left + box.width / 2,         " +
                            "  cy = box.top + box.height / 2,         " +
                            "  e = document.elementFromPoint(cx, cy); " +
                            "for (; e; e = e.parentElement) {          " +
                            "  if (e === elem)                         " +
                            "    return true;                          " +
                            "}                                        " +
                            "return false;                             "
                    , element);
            logger.info("Element '" + elementName + "' in viewport: " + isInView);
            return isInView != null && isInView;
        } catch (Exception e) {
            logger.error("Failed to check if element is in viewport: " + elementName + " - " + e.getMessage());
            throw new RuntimeException("Failed to check if element is in viewport: " + elementName, e);
        }
    }

    public static void clearInputUsingJS(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            logger.info("Clearing input value for element: " + elementName);
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", element);
            logger.info("Cleared input value for element: " + elementName);
        } catch (Exception e) {
            logger.error("Failed to clear input value for element: " + elementName + " - " + e.getMessage());
            throw new RuntimeException("Failed to clear input value for element: " + elementName, e);
        }
    }

    public static boolean isPageLoaded(WebDriver driver) {
        try {
            logger.info("Checking if page is fully loaded");
            String readyState = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState;");
            boolean loaded = "complete".equals(readyState);
            logger.info("Page loaded status: " + loaded);
            return loaded;
        } catch (Exception e) {
            logger.error("Failed to check if page is loaded - " + e.getMessage());
            throw new RuntimeException("Failed to check if page is loaded", e);
        }
    }

    public static void executeCustomJS(WebDriver driver, String script, Object... args) {
        try {
            logger.info("Executing custom JS script");
            ((JavascriptExecutor) driver).executeScript(script, args);
            logger.info("Executed custom JS script");
        } catch (Exception e) {
            logger.error("Failed to execute custom JS script - " + e.getMessage());
            throw new RuntimeException("Failed to execute custom JS script", e);
        }
    }

    public static void click(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            element.click();
            logger.info("Clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to click on element: {}. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to click on element: " + elementName, e);
        }
    }

    public static void click(WebDriver driver, By locator, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, locator, 20, elementName);
            WaitUtility.waitForClickability(driver, locator, 20, elementName);
            WebElement element = driver.findElement(locator);
            BrowserUtility.scrollToElement(driver, locator, elementName);
            element.click();
            logger.info("Clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to click on element: {}. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to click on element: " + elementName, e);
        }
    }

    public static void doubleClick(WebDriver driver, WebElement element, String elementName) {
        try {
            Actions actions = new Actions(driver);
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            WaitUtility.waitForClickability(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            actions.doubleClick(element).perform();
            logger.info("Double clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to double click on element: {}. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to double click on element: " + elementName, e);
        }
    }

    public static void rightClick(WebDriver driver, WebElement element, String elementName) {
        try {
            Actions actions = new Actions(driver);
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            WaitUtility.waitForClickability(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            actions.contextClick(element).perform();
            logger.info("Right clicked on element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to right click on element: {}. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to right click on element: " + elementName, e);
        }
    }

    public static void sendKeys(WebDriver driver, WebElement element, String value, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            element.sendKeys(value);
            logger.info("Sent keys '{}' to element: {}", value, elementName);
        } catch (Exception e) {
            logger.error("Failed to send keys '{}' to element: {}. Exception: {}", value, elementName, e.getMessage());
            throw new RuntimeException("Failed to send keys to element: " + elementName, e);
        }
    }

    public static void sendKeys(WebDriver driver, By locator, String value, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, locator, 20, elementName);
            WebElement element = driver.findElement(locator);
            BrowserUtility.scrollToElement(driver, locator, elementName);
            element.sendKeys(value);
            logger.info("Sent keys '{}' to element: {}", value, elementName);
        } catch (Exception e) {
            logger.error("Failed to send keys '{}' to element: {}. Exception: {}", value, elementName, e.getMessage());
            throw new RuntimeException("Failed to send keys to element: " + elementName, e);
        }
    }

    public static String getText(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            String text = element.getText();
            logger.info("Got text '{}' from element: {}", text, elementName);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to get text from element: " + elementName, e);
        }
    }

    public static void clear(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            element.clear();
            logger.info("Cleared element: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to clear element: {}. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to clear element: " + elementName, e);
        }
    }

    public static boolean isDisplayed(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            boolean displayed = element.isDisplayed();
            logger.info("Element '{}' displayed: {}", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.error("Failed to check if element '{}' is displayed. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check if element is displayed: " + elementName, e);
        }
    }

    public static boolean isEnabled(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            boolean enabled = element.isEnabled();
            logger.info("Element '{}' enabled: {}", elementName, enabled);
            return enabled;
        } catch (Exception e) {
            logger.error("Failed to check if element '{}' is enabled. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check if element is enabled: " + elementName, e);
        }
    }

    public static boolean isDisabled(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            boolean disabled = !element.isEnabled();
            logger.info("Element '{}' disabled: {}", elementName, disabled);
            return disabled;
        } catch (Exception e) {
            logger.error("Failed to check if element '{}' is disabled. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check if element is disabled: " + elementName, e);
        }
    }

    public static void checkIfNotSelected(WebDriver driver, WebElement element, String elementName) {
        try {
            if (!element.isSelected()) {
                WaitUtility.waitForVisibility(driver, element, 20, elementName);
                BrowserUtility.scrollToElement(driver, element, elementName);
                click(driver, element, elementName);
                logger.info("Checked the element (if not already selected): {}", elementName);
            } else {
                logger.info("Element '{}' was already selected", elementName);
            }
        } catch (Exception e) {
            logger.error("Failed to check the element '{}'. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to check (select) element: " + elementName, e);
        }
    }

    public static void uncheckIfSelected(WebDriver driver, WebElement element, String elementName) {
        try {
            if (element.isSelected()) {
                WaitUtility.waitForVisibility(driver, element, 20, elementName);
                BrowserUtility.scrollToElement(driver, element, elementName);
                click(driver, element, elementName);
                logger.info("Unchecked the element (if it was selected): {}", elementName);
            } else {
                logger.info("Element '{}' was already unchecked", elementName);
            }
        } catch (Exception e) {
            logger.error("Failed to uncheck the element '{}'. Exception: {}", elementName, e.getMessage());
            throw new RuntimeException("Failed to uncheck element: " + elementName, e);
        }
    }

    public static boolean isChecked(WebDriver driver, WebElement element, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, element, 20, elementName);
            BrowserUtility.scrollToElement(driver, element, elementName);
            boolean selected = element.isSelected();
            logger.info("Element '{}' selected state is '{}'", elementName, selected);
            return selected;
        } catch (Exception e) {
            logger.error("Failed to determine selected state for element '{}'", elementName, e);
            throw new RuntimeException("Failed to determine if element is checked: " + elementName, e);
        }
    }


    public static void selectByVisibleText(WebDriver driver, WebElement dropdownElement, String visibleText, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            select.selectByVisibleText(visibleText);
            logger.info("Selected option '{}' by visible text in '{}'", visibleText, elementName);
        } catch (NoSuchElementException e) {
            logger.error("Option '{}' not found in dropdown '{}'", visibleText, elementName, e);
            throw new RuntimeException("Option not found: " + visibleText, e);
        } catch (Exception e) {
            logger.error("Failed to select option '{}' by visible text in '{}'", visibleText, elementName, e);
            throw new RuntimeException("Error selecting by visible text: " + visibleText, e);
        }
    }

    public static void selectByValue(WebDriver driver, WebElement dropdownElement, String value, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            select.selectByValue(value);
            logger.info("Selected option with value '{}' in '{}'", value, elementName);
        } catch (NoSuchElementException e) {
            logger.error("Option with value '{}' not found in dropdown '{}'", value, elementName, e);
            throw new RuntimeException("Option with value not found: " + value, e);
        } catch (Exception e) {
            logger.error("Failed to select option with value '{}' in '{}'", value, elementName, e);
            throw new RuntimeException("Error selecting by value: " + value, e);
        }
    }

    public static void selectByIndex(WebDriver driver, WebElement dropdownElement, int index, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            select.selectByIndex(index);
            logger.info("Selected option at index '{}' in '{}'", index, elementName);
        } catch (NoSuchElementException e) {
            logger.error("Option at index '{}' not found in dropdown '{}'", index, elementName, e);
            throw new RuntimeException("Option at index not found: " + index, e);
        } catch (Exception e) {
            logger.error("Failed to select option at index '{}' in '{}'", index, elementName, e);
            throw new RuntimeException("Error selecting by index: " + index, e);
        }
    }

    public static void selectRandomOption(WebDriver driver, WebElement dropdownElement, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            List<WebElement> options = select.getOptions();
            if (options.isEmpty()) {
                logger.warn("Dropdown '{}' has no options to select", elementName);
                throw new RuntimeException("Dropdown has no options to select");
            }
            int startIndex = 0;
            if (options.size() <= startIndex) {
                logger.warn("Dropdown '{}' has no options to select after excluding the first option", elementName);
                throw new RuntimeException("Dropdown has no selectable options after exclusion");
            }
            Random rand = new Random();
            int randomIndex = rand.nextInt(options.size() - startIndex) + startIndex;
            select.selectByIndex(randomIndex);
            logger.info("Randomly selected option '{}' at index '{}' in '{}'", options.get(randomIndex).getText(), randomIndex, elementName);
        } catch (Exception e) {
            logger.error("Failed to select random option from dropdown '{}'", elementName, e);
            throw new RuntimeException("Error selecting random option", e);
        }
    }

    public static boolean isOptionSelected(WebDriver driver, WebElement dropdownElement, String expectedOption, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            List<WebElement> selectedOptions = select.getAllSelectedOptions();
            for (WebElement option : selectedOptions) {
                if (option.getText().trim().equals(expectedOption.trim())) {
                    logger.info("Option '{}' is selected in dropdown '{}'", expectedOption, elementName);
                    return true;
                }
            }
            logger.info("Option '{}' is NOT selected in dropdown '{}'", expectedOption, elementName);
            return false;
        } catch (Exception e) {
            logger.error("Failed to check selected option '{}' in dropdown '{}'", expectedOption, elementName, e);
            throw new RuntimeException("Error checking selected option: " + expectedOption, e);
        }
    }

    public static String getSelectedOptionText(WebDriver driver, WebElement dropdownElement, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            String selectedText = select.getFirstSelectedOption().getText();
            logger.info("Selected option in '{}' is '{}'", elementName, selectedText);
            return selectedText;
        } catch (Exception e) {
            logger.error("Failed to get selected option text from '{}'", elementName, e);
            throw new RuntimeException("Error getting selected option text from: " + elementName, e);
        }
    }

    public static List<String> getAllDropdownOptions(WebDriver driver, WebElement dropdownElement, String elementName) {
        List<String> optionsText = new ArrayList<>();
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            List<WebElement> options = select.getOptions();
            for (WebElement option : options) {
                optionsText.add(option.getText());
            }
            logger.info("Retrieved all options from dropdown '{}': {}", elementName, optionsText);
        } catch (Exception e) {
            logger.error("Failed to get all options from dropdown '{}'", elementName, e);
            throw new RuntimeException("Error retrieving options from dropdown: " + elementName, e);
        }
        return optionsText;
    }

    public static boolean isOptionPresent(WebDriver driver, WebElement dropdownElement, String expectedOption, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            List<WebElement> options = select.getOptions();
            for (WebElement option : options) {
                if (option.getText().trim().equals(expectedOption.trim())) {
                    logger.info("Option '{}' is present in dropdown '{}'", expectedOption, elementName);
                    return true;
                }
            }
            logger.warn("Option '{}' is NOT present in dropdown '{}'", expectedOption, elementName);
            return false;
        } catch (Exception e) {
            logger.error("Failed to check option presence '{}' in dropdown '{}'", expectedOption, elementName, e);
            throw new RuntimeException("Error checking presence of option: " + expectedOption, e);
        }
    }

    public static int getDropdownSize(WebDriver driver, WebElement dropdownElement, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            int size = select.getOptions().size();
            logger.info("Dropdown '{}' has '{}' options", elementName, size);
            return size;
        } catch (Exception e) {
            logger.error("Failed to get dropdown size for '{}'", elementName, e);
            throw new RuntimeException("Error getting dropdown size for: " + elementName, e);
        }
    }

    public static boolean isMultiple(WebDriver driver, WebElement dropdownElement, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            boolean multiple = select.isMultiple();
            logger.info("Dropdown '{}' multiple select: {}", elementName, multiple);
            return multiple;
        } catch (Exception e) {
            logger.error("Failed to determine if dropdown '{}' supports multiple selection", elementName, e);
            throw new RuntimeException("Error checking if dropdown supports multiple: " + elementName, e);
        }
    }

    public static void deselectByVisibleText(WebDriver driver, WebElement dropdownElement, String visibleText, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            if (select.isMultiple()) {
                select.deselectByVisibleText(visibleText);
                logger.info("Deselected option '{}' by visible text in '{}'", visibleText, elementName);
            } else {
                logger.warn("Dropdown '{}' does not support multiple selection. Cannot deselect.", elementName);
                throw new RuntimeException("Dropdown does not support multiple selection: " + elementName);
            }
        } catch (NoSuchElementException e) {
            logger.error("Option '{}' not found for deselect in dropdown '{}'", visibleText, elementName, e);
            throw new RuntimeException("Option not found for deselect: " + visibleText, e);
        } catch (Exception e) {
            logger.error("Failed to deselect option '{}' by visible text in '{}'", visibleText, elementName, e);
            throw new RuntimeException("Error deselecting by visible text: " + visibleText, e);
        }
    }

    public static void deselectByValue(WebDriver driver, WebElement dropdownElement, String value, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            if (select.isMultiple()) {
                select.deselectByValue(value);
                logger.info("Deselected option with value '{}' in '{}'", value, elementName);
            } else {
                logger.warn("Dropdown '{}' does not support multiple selection. Cannot deselect.", elementName);
                throw new RuntimeException("Dropdown does not support multiple selection: " + elementName);
            }
        } catch (NoSuchElementException e) {
            logger.error("Option with value '{}' not found for deselect in dropdown '{}'", value, elementName, e);
            throw new RuntimeException("Option with value not found for deselect: " + value, e);
        } catch (Exception e) {
            logger.error("Failed to deselect option with value '{}' in '{}'", value, elementName, e);
            throw new RuntimeException("Error deselecting by value: " + value, e);
        }
    }

    public static void deselectByIndex(WebDriver driver, WebElement dropdownElement, int index, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            if (select.isMultiple()) {
                select.deselectByIndex(index);
                logger.info("Deselected option at index '{}' in '{}'", index, elementName);
            } else {
                logger.warn("Dropdown '{}' does not support multiple selection. Cannot deselect.", elementName);
                throw new RuntimeException("Dropdown does not support multiple selection: " + elementName);
            }
        } catch (NoSuchElementException e) {
            logger.error("Option at index '{}' not found for deselect in dropdown '{}'", index, elementName, e);
            throw new RuntimeException("Option at index not found for deselect: " + index, e);
        } catch (Exception e) {
            logger.error("Failed to deselect option at index '{}' in '{}'", index, elementName, e);
            throw new RuntimeException("Error deselecting by index: " + index, e);
        }
    }

    public static void deselectAll(WebDriver driver, WebElement dropdownElement, String elementName) {
        try {
            WaitUtility.waitForVisibility(driver, dropdownElement, 20, elementName);
            BrowserUtility.scrollToElement(driver, dropdownElement, elementName);
            Select select = new Select(dropdownElement);
            if (select.isMultiple()) {
                select.deselectAll();
                logger.info("Deselected all options in '{}'", elementName);
            } else {
                logger.warn("Dropdown '{}' does not support multiple selection. Cannot deselect all.", elementName);
                throw new RuntimeException("Dropdown does not support multiple selection: " + elementName);
            }
        } catch (Exception e) {
            logger.error("Failed to deselect all options in '{}'", elementName, e);
            throw new RuntimeException("Error deselecting all options in: " + elementName, e);
        }
    }

}
