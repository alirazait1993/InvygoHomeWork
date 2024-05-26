package org.automation.views;

import io.appium.java_client.android.AndroidDriver;
import org.automation.utils.Wait;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;

import java.time.Duration;

public class ProductSearch {

    protected final AndroidDriver driver;

    private final By viewAll = By.xpath("//android.widget.TextView[@text=\"View all cars\"]");
    private final By findCar = By.xpath("//android.widget.TextView[@text=\"Find car\"]");
    private final By weeklySub = By.xpath("//android.widget.TextView[@text=\"Weekly\"]");
    private final By monthlySub = By.xpath("//android.widget.TextView[@text=\"Monthly\"]");
    private final By weeklyCars = By.xpath("//android.widget.TextView[@text=\"Why subscribe weekly with invygo?\"]");
    private final By monthlyCars = By.xpath("//android.widget.TextView[@text=\"Why subscribe monthly with invygo?\"]");
    private final By carSearchOption = By.xpath("//android.widget.TextView[@text=\"What car are you looking for?\"]");
    private final By carSearchFeild = By.xpath("//android.view.ViewGroup[@resource-id=\"Search cars_inputContainer\"]");
    private final By carSearchInput = By.xpath("//android.widget.EditText[@resource-id=\"SearchCarsField\"]");
    private final By brandShowMore = By.xpath("//android.widget.TextView[@text=\"Show more\"]");
    private final By filtersOption = By.xpath("//android.widget.ScrollView[@resource-id=\"CarList\"]/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[3]/android.view.ViewGroup/com.horcrux.svg.SvgView/com.horcrux.svg.GroupView/com.horcrux.svg.GroupView/com.horcrux.svg.PathView");
    private final By showCars = By.xpath("//android.widget.TextView[@text=\"Show cars\"]");

    public ProductSearch(AndroidDriver driver) {
        this.driver = driver;
    }

    public void navigateToFindCar(){
        Wait.waitForPageToLoad(driver, viewAll);
        driver.findElement(findCar).click();
    }

    public boolean viewAllMonthlyCars(){
        navigateToFindCar();
        Wait.waitForPageToLoad(driver, monthlySub);
        driver.findElement(monthlySub).click();
        Wait.waitForPageToLoad(driver, monthlyCars);
        return driver.findElement(monthlyCars).isDisplayed();
    }

    public boolean viewAllWeeklyCars(){
        navigateToFindCar();
        Wait.waitForPageToLoad(driver, weeklySub);
        driver.findElement(weeklySub).click();
        Wait.waitForPageToLoad(driver, weeklyCars);
        return driver.findElement(weeklyCars).isDisplayed();
    }

    public void carSearchByName(String carMake, String carModel){
        navigateToFindCar();
        Wait.waitForPageToLoad(driver, carSearchOption);
        driver.findElement(carSearchOption).click();
        Wait.waitForPageToLoad(driver, carSearchInput);
        driver.findElement(carSearchFeild).click();
        driver.findElement(carSearchInput).sendKeys(carMake," ",carModel);
    }

    public void carSearchByFilter(String brand, String carType){
        navigateToFindCar();
        Wait.waitForPageToLoad(driver, filtersOption);
        driver.findElement(filtersOption).click();
        Wait.waitForPageToLoad(driver, brandShowMore);
        driver.findElement(brandShowMore).click();
        selectCarBrand(brand);
        selectCarType(carType);
        driver.findElement(showCars).click();
    }

    public void selectCarBrand(String brand){
        String xpath = constructXPath("//android.widget.TextView[@text=\"",brand+"\"]");
        try {
            scrollDownUntilElementFound(By.xpath(xpath), 10);  // Max 10 scrolls
        } catch (UnsupportedCommandException e) {
            System.err.println("UnsupportedCommandException caught: " + e.getMessage());
            // Handle the exception as needed
        }
        driver.findElement(By.xpath(xpath)).click();
        driver.navigate().back();
    }

    public void selectCarType(String carType){
        String xpath = constructXPath("//android.widget.TextView[@text=\"",carType+"\"]");
        try {
            scrollDownUntilElementFound(By.xpath(xpath), 10);  // Max 10 scrolls
        } catch (UnsupportedCommandException e) {
            System.err.println("UnsupportedCommandException caught: " + e.getMessage());
            // Handle the exception as needed
        }
        driver.findElement(By.xpath(xpath)).click();
    }

    public String carSearchVerify(String carMake, String carModel){
        String xpath = constructXPath("//android.widget.TextView[@text=\"",carMake+" "+carModel+"\"]");
        Wait.waitForPageToLoad(driver, By.xpath(xpath));
        return driver.findElement(By.xpath(xpath)).getText();
    }

    public String constructXPath(String baseXPath, String dynamicPart) {
        return baseXPath + dynamicPart;
    }

    //TODO Create this as a Util for reusability
    public void scrollDownUntilElementFound(By locator, int maxScrolls) throws UnsupportedCommandException {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.getWidth() / 2;
            int startY = (int) (size.getHeight() * 0.7);
            int endY = (int) (size.getHeight() * 0.2);

            for (int i = 0; i < maxScrolls; i++) {
                if (isElementPresent(locator)) {
                    return;
                }

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Actions actions = new Actions(driver);
                actions
                        .tick(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY))
                        .tick(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                        .tick(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY))
                        .tick(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))
                        .perform();
            }
        } catch (WebDriverException e) {
            throw new UnsupportedCommandException("Scroll down command is not supported by the driver.", e);
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
