package com.covenant.config;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.nio.file.Path;

public class SessionSetup {

    private static Playwright playwright = null;
    private static BrowserContext browserContext = null;

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        BrowserType chromium = playwright.chromium();
        BrowserType.LaunchPersistentContextOptions psOptions = new BrowserType.LaunchPersistentContextOptions();
        psOptions.setIgnoreHTTPSErrors(true);
        psOptions.setHeadless(true);
        psOptions.setTimeout(5000);
        psOptions.setSlowMo(250);
        browserContext = chromium.launchPersistentContext(Path.of(""), psOptions);
    }

    @AfterAll
    static void tearDown() {
        if (browserContext != null) {
            browserContext.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    public static Page getPage() {
        return browserContext.pages().get(0);
    }
}
