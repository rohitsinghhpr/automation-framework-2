package pages;

import base_page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.BrowserUtility;
import utils.WaitUtility;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//img[@src='/images/Toolsqa.jpg']")
    WebElement logo;

    @FindBy(xpath = "//div[@class='card-body']//h5[text()='Forms']")
    WebElement formsCard;

    public void clickOnFormsCard(){
        BrowserUtility.click(driver,formsCard,"Froms Card");
    }

    public WebElement getLogo(){
        return logo;
    }

    public WebElement getCard(String cardText){
        String xpath = "//div[@class='card-body']//h5[text()='"+cardText+"']//parent::div//parent::div//div[contains(@class,'avatar')]";
        WaitUtility.waitForVisibility(driver,By.xpath(xpath), 20, cardText);
        BrowserUtility.scrollToElement(driver,driver.findElement(By.xpath(xpath)), cardText);
        return driver.findElement(By.xpath(xpath));
    }

    public void clickOnCard(String cardText){
        String xpath = "//div[@class='card-body']//h5[text()='"+cardText+"']";
        BrowserUtility.click(driver,By.xpath(xpath),cardText);
    }
}
