package com.covenant.pages.launcher;

import com.covenant.common.Attachments;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class BinaryLauncherPage extends LauncherBasePage {

    private final Page page;

    public BinaryLauncherPage() {
        page = SessionSetup.getPage();
        Attachments.takeScreenshot(this.getClass().getSimpleName());
    }

    public void fillData(LauncherData launcherData) {
        fillDataBase(launcherData);
    }

    public Path generateAndDownload() {
        page.locator("button#generate").click();
        page.waitForTimeout(1000);

        // wait for download to start
        Download download = page.waitForDownload(() -> {
            page.locator("button#download").click();
        });

        String GruntExeName = ("GruntHTTP-" + new Random().nextInt(100000) + ".exe");
        Path path = Paths.get(Properties.getLaunchersFolder(), GruntExeName);
        download.saveAs(path);

        Attachments.takeScreenshot("BinaryLauncher_AfterDownload");

        return path;
    }
}
