package org.automation.login;

import org.automation.Setup;
import org.automation.common.Login;
import org.automation.utils.Wait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends Setup {

    protected static String mobile = "585722789";
    //hardcode OPT
    protected static String opt1 = "3";
    protected static String opt2 = "1";
    protected static String opt3 = "7";
    protected static String opt4 = "7";

    @Test(priority = 0)
    public void LoginWithIncorrectMobile() {
        Login login = new Login(driver);
        login.enterMobile("123456");
        Assert.assertTrue(login.verifyMobileNumberNotValid(), "MobileNumber is not valid.");
    }

    @Test (priority = 1)
    public void LoginWithIncorrectOPT() {
        Login login = new Login(driver);
        login.enterMobile("123456789");
        login.enterOPT("1", "2", opt3, opt4);
        Assert.assertEquals(login.verifyIncorrectOTPmessage(),"Gone[410]: OTP is incorrect");
    }

    @Test (priority = 2)
    public void LoginWithCorrectOTP() {
        Login login = new Login(driver);
        login.enterMobile(mobile);
        login.enterOPT(opt1, opt2, opt3, opt4);
        Assert.assertTrue(login.loginSuccess(), "Login successfully.");
    }
    @Test (dependsOnMethods = "LoginWithCorrectOTP()")
    public void LogOut() {
        Login login = new Login(driver);
        login.logout();
        Assert.assertTrue(login.logoutSuccess(), "Logout successfully.");
    }
}
