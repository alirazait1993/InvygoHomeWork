package org.automation.common;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.automation.utils.Wait;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;

import java.time.Duration;

public class Login {
    protected final AndroidDriver driver;
    protected static String messageApp = "com.google.android.apps.messaging";
    protected static String invygoApp = "com.invygo.customer";

    private final By loginButton = By.xpath("//android.widget.TextView[@text=\"Login/Sign up\"]");
    private final By enterMobileNumber = By.xpath("//android.widget.EditText[@resource-id=\"phoneNumberInput\"]");
    private final By mobileNumberScreenBanner = By.xpath("//android.widget.TextView[@text=\"Login/Sign up\"]");
    private final By continueButton = By.xpath("//android.view.ViewGroup[@resource-id=\"buttonContinue\"]");
    private final By resendOTP = By.xpath("//android.widget.TextView[@text=\"I didn’t get a code\"]");
    private final By firstSMS = By.xpath("//android.view.ViewGroup[@resource-id=\"com.google.android.apps.messaging:id/swipeableContainer\"]");
    private final By smsBody = By.xpath("//android.view.View[@resource-id=\"message_list\"]/android.view.View/android.view.View[2]");
    private final By incorrectOTPmessage = By.xpath("//android.widget.TextView[@text=\"Gone[410]: OTP is incorrect\"]");
    private final By dashboard = By.xpath("//android.view.ViewGroup[@resource-id=\"Home\"]");
    private final By myProfile = By.xpath("//android.view.ViewGroup[@resource-id=\"My profile\"]");
    private final By logOutButton = By.xpath("//android.widget.TextView[@text=\"Log Out\"]");
    private final By logOutConfirm = By.xpath("(//android.widget.TextView[@text=\"Log Out\"])[3]");

    public Login(AndroidDriver driver) {
        this.driver = driver;
    }

    public void enterMobile(String mobileNumber) {
        Wait.waitForPageToLoad(driver, loginButton);
        driver.findElement(loginButton).click();
        Wait.waitForPageToLoad(driver, mobileNumberScreenBanner);
        driver.findElement(enterMobileNumber).sendKeys(mobileNumber);
        if (mobileNumber.length()>=7){
            requestOTP();
        }
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void enterOPT(String OTP1, String OTP2, String OTP3, String OTP4) {
        getSMSOTP();
        switchApp(invygoApp);
        Wait.waitForPageToLoad(driver, resendOTP);
        KeyEvent key = new KeyEvent();
        driver.pressKey(key.withKey(AndroidKey.valueOf("DIGIT_"+OTP1)));
        driver.pressKey(key.withKey(AndroidKey.valueOf("DIGIT_"+OTP2)));
        driver.pressKey(key.withKey(AndroidKey.valueOf("DIGIT_"+OTP3)));
        driver.pressKey(key.withKey(AndroidKey.valueOf("DIGIT_"+OTP4)));
        Wait.waitFor10secs(driver);
    }

    public void switchApp(String appPackage) {
        driver.activateApp(appPackage);
    }

    public String getSMSOTP() {
        switchApp(messageApp);
        Wait.waitForPageToLoad(driver, firstSMS);
        driver.findElement(firstSMS).click();
        Wait.waitForPageToLoad(driver, smsBody);
        String smsMessage = driver.findElement(smsBody).getText();
        killApp(messageApp);
        return smsMessage;
    }

//TODO once found a way to retrieve the textMessageBody from text messages then use extractionOTP to get OTP
    public String extractOTP() {
        String smsText = getSMSOTP();
        return smsText.substring(0, Math.min(smsText.length(), 4));
    }

    public void killApp(String appPackage) {
        driver.terminateApp(appPackage);
    }

    public void requestOTP() {
        Wait.waitForPageToLoad(driver, mobileNumberScreenBanner);
        driver.findElement(continueButton).click();
    }

    public String verifyIncorrectOTPmessage() {
        Wait.waitForPageToLoad(driver, incorrectOTPmessage);
        return driver.findElement(incorrectOTPmessage).getText();
    }

    public boolean verifyMobileNumberNotValid() {
        Wait.waitForPageToLoad(driver, enterMobileNumber);
        driver.findElement(mobileNumberScreenBanner);
        return true;
    }

    public boolean loginSuccess() {
        Wait.waitForPageToLoad(driver, dashboard);
        return driver.findElement(dashboard).isDisplayed();
    }

    public void logout() {
        Wait.waitForPageToLoad(driver, myProfile);
        driver.findElement(myProfile).click();
        try {
            scrollDownUntilElementFound(logOutButton, 10);  // Max 10 scrolls
        } catch (UnsupportedCommandException e) {
            System.err.println("UnsupportedCommandException caught: " + e.getMessage());
            // Handle the exception as needed
        }
        driver.findElement(logOutButton).click();
        Wait.waitForPageToLoad(driver, logOutConfirm);
        driver.findElement(logOutConfirm).click();
        Wait.waitForPageToLoad(driver, loginButton);
        driver.findElement(loginButton);
    }

    public boolean logoutSuccess() {
        Wait.waitForPageToLoad(driver, loginButton);
        driver.findElement(loginButton);
        return true;
    }

//TODO Create this as a Util for reusability
    public void scrollDownUntilElementFound(By locator, int maxScrolls) throws UnsupportedCommandException {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.getWidth() / 2;
            int startY = (int) (size.getHeight() * 0.8);
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
