package com.covenant.pages.launcher;

import com.covenant.common.Attachments;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LaunchersPage {

    private final Page page;

    public LaunchersPage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    public void clickBinaryLauncher() {
        page.getByRole(AriaRole.MAIN)
                .locator("tr a")
                .filter(new Locator.FilterOptions().setHasText("Binary"))
                .click();
    }
}
