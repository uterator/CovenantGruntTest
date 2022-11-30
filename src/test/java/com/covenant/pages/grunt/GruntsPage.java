package com.covenant.pages.grunt;

import com.covenant.common.Attachments;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

public class GruntsPage {
    private final Page page;

    public GruntsPage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    public GruntTableRow findGrunt(GruntData gruntData) {
        Locator grantRowLocator = page.getByRole(AriaRole.MAIN)
                .locator("div[class='table-responsive'] tbody tr")
                .filter(new Locator.FilterOptions().setHasText(gruntData.getStatus()))
                .filter(new Locator.FilterOptions().setHasText(gruntData.getUser()))
                .filter(new Locator.FilterOptions().setHasText(gruntData.getHostname()))
                .filter(new Locator.FilterOptions().setHasText(gruntData.getTemplate()));

        grantRowLocator.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(Properties.getScreenshotsFolder(), Attachments.timestampImageName("grantRow"))));

        return new GruntTableRow(grantRowLocator);
    }
}
