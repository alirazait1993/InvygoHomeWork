package org.automation;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Setup {

    protected AndroidDriver driver;
    protected static String invygoApp = "com.invygo.customer";
    protected static String apkPath = System.getProperty("user.dir")+"/src/test/resources/apkFile/invygo.apk";
    protected static String appiumLogPath = System.getProperty("user.dir")+"/src/test/resources/appiumLogs/appium.log";

    private static AppiumDriverLocalService service;


    @BeforeSuite
    public void globalSetup() {
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .withArgument(GeneralServerFlag.BASEPATH, "/wd.hub")
                .usingPort(4723)
                .withLogFile(new File(appiumLogPath))
                .build();
        service.start();
    }

    @BeforeClass
    public void setUp() throws IOException {

        DesiredCapabilities caps = new DesiredCapabilities();
        // Platform details
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME,"R5CT138XWEA");  // Set your android device name or ID here

        // App details
        caps.setCapability(MobileCapabilityType.APP,apkPath); // APK file path
        caps.setCapability("appPackage", invygoApp);
        caps.setCapability("appActivity", "com.invygo.customer.SplashActivity");
        caps.setCapability("autoGrantPermissions", true);  // Automatically grant app permissions
        caps.setCapability(MobileCapabilityType.NO_RESET, true);  // Prevents resetting app state between sessions
        caps.setCapability(MobileCapabilityType.FULL_RESET, false);  // Prevents reinstalling the app
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);  // Timeout for new commands
        driver = new AndroidDriver(getServiceUrl(), caps);
    }
    @BeforeMethod
    public void MethodSetup(){
        driver.activateApp(invygoApp);
    }

    @AfterMethod
    public void MethodTearDown(){
        driver.terminateApp(invygoApp);
    }

    @AfterClass
    public void TearDown() {
//        driver.terminateApp(invygoApp);
//        driver.removeApp(invygoApp);
        if (driver != null){
            driver.quit();
        }
    }

    @AfterSuite
    public void globalTearDown() {
        if (service != null){
            service.stop();
        }
    }

    public URL getServiceUrl(){
        return service.getUrl();
    }
}