package com.covenant.pages;

import com.covenant.common.Attachments;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;

    public LoginPage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    public void loginRegular() {
        login(Properties.getCovenantUname(), Properties.getCovenantPword());
    }

    public void login(String username, String pword) {
        page.locator("#CovenantUserRegister_UserName").fill(username);
        page.locator("#CovenantUserRegister_Password").fill(pword);
        page.getByText("Login").click();
        Attachments.takeScreenshot("AfterLogin");
    }
}
