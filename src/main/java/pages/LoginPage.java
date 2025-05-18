package pages;

import base_page.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath="//textarea[@aria-label=\"Search\"]")
    WebElement searchInput;
    public void search(String text) {
        logger.info("Entering search text: " + text);
        searchInput.sendKeys(text);
    }
    public void enter(){
        searchInput.sendKeys(Keys.ENTER);
        logger.info("Search submitted");
    }
}
