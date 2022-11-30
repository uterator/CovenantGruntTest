package com.covenant.pages.launcher;

import com.covenant.common.Attachments;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

abstract class LauncherBasePage {

    private final Page page;

    LauncherBasePage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    protected void fillDataBase(LauncherData launcherData) {
        page.locator("#generate-tab").click();

        if (launcherData.getListenerName() != null) {
            reportAction("Listener name", launcherData.getListenerName());
            page.locator("select#ListenerId")
                    .selectOption(new SelectOption().setLabel(launcherData.getListenerName()));
            page.waitForTimeout(1000);
        }

        if (launcherData.getUseCertPinning() != null) {
            reportAction("Use Cert Pinning", String.valueOf(launcherData.getUseCertPinning()));
            page.locator("select#UseCertPinning")
                    .selectOption(launcherData.getUseCertPinning() ? "True" : "False");
            page.waitForTimeout(1000);
        }

        if (launcherData.getValidateCert() != null) {
            reportAction("Validate Cart", String.valueOf(launcherData.getValidateCert()));
            page.locator("select#ValidateCert")
                    .selectOption(launcherData.getValidateCert() ? "True" : "False");
            page.waitForTimeout(1000);
        }

        if (launcherData.getDotNetVersion() != null) {
            reportAction("Dot Net Version", launcherData.getDotNetVersion());
            page.locator("select#DotNetVersion")
                    .selectOption(new SelectOption().setLabel(launcherData.getDotNetVersion()));
            page.waitForTimeout(1000);
        }
    }

    private void reportAction(String field, String value) {
        System.out.printf("[Launcher] '%s' = '%s'\n", field, value);
    }

}
