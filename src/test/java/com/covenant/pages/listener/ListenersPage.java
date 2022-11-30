package com.covenant.pages.listener;

import com.covenant.common.Attachments;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

public class ListenersPage {

    private final Page page;

    public ListenersPage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    public void clickAddNewListener() {
        page.getByRole(AriaRole.MAIN).locator("a[href='/listener/create']").click();
    }

    public ListenersTableRow getListener(String listenerName) {
        Locator listenerRowLocator = page.getByRole(AriaRole.MAIN)
                .locator("div#listeners tr")
                .filter(new Locator.FilterOptions().setHasText(listenerName));

        listenerRowLocator.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(Properties.getScreenshotsFolder(), Attachments.timestampImageName("listenerRow"))));
        System.out.println("Found listener " + listenerName);
        return new ListenersTableRow(listenerRowLocator);
    }

}
