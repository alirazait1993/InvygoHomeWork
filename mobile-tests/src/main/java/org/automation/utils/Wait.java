package org.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility class to manage waiting time foe the screen/pages date to load
 */
public class Wait {

    /**
     * Waits for an element on a page to render in the DOM.
     */
    public static void waitForPageToLoad(WebDriver driver, By pageElement){
        new WebDriverWait(driver, Duration.ofSeconds(30)).until((ExpectedConditions.presenceOfElementLocated(pageElement)));
    }

    /**
     * Waits for 10sec
     */
    public static void waitFor10secs(WebDriver driver) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Waits for spinner to disappear
     */
    public static void waitForSpinner(WebDriver driver, By spinner) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until((ExpectedConditions.invisibilityOf(driver.findElement(spinner))));
    }

    /**
     * Drawer elements already exist in the DOM but are not immediately interactable once button is clicked, so we can't use waitForPageToLoad()
     */
    public static void waitForDrawer(WebDriver driver, By drawerElement) {
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(ElementNotInteractableException.class)
                .until((ExpectedConditions.visibilityOf(driver.findElement(drawerElement))));
    }
}
