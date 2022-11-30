package com.covenant.pages.listener;

import com.covenant.common.Attachments;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HTTPListenerPage {

    private final Page page;

    public HTTPListenerPage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    public void registerHTTPListener(HTTPListener httpListener) {
        httpListener.setName("Auto-" + page.locator("#http #Name").inputValue());

        page.locator("#http #Name")
                .fill(httpListener.getName());

        page.locator("#http #ConnectAddresses_0_")
                .fill(httpListener.getConnectionAddress());

        page.locator("#http button[type='submit']").click();
    }

    // Could go to the base abstract Listener base page
    public void stopHTTPListener() {
        System.out.println("Stopping the listener!");
        page.locator("button", new Page.LocatorOptions().setHasText("Stop")).click();
        page.waitForTimeout(1000);
    }

    // Could go to the base abstract Listener base page
    public void deleteHTTPListener() {
        System.out.println("Deleting the listener!");
        page.locator("button[class*='btn-danger']").click();
        page.waitForTimeout(1000);
    }

}

