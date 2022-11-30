package com.covenant.common;

import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Page;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class Attachments {

    public static String timestampImageName(String baseName) {
        return timestampFileName(baseName, "png");
    }

    private static String timestampFileName(String baseName, String ext) {
        String timePart = java.time.LocalDateTime.now().toString().replace("T","_").replace(":","-");
        return String.format("%s_%s.%s", timePart, baseName, ext);
    }

    public static void takeScreenshot(String name) {
        SessionSetup.getPage().waitForTimeout(500);
        SessionSetup.getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(Properties.getScreenshotsFolder(), timestampImageName(name))));
    }

    public static void printTextToFile(String text, String filepath) {
        try(PrintWriter out = new PrintWriter(filepath)) {
            out.print(text);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
