package com.covenant.common;

import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;

import java.nio.file.Paths;

public class NavigateTo {

    private static final String BASE_URL = Properties.getCovenantHost();

    private static final String LOGIN_URL = "covenantuser/login";
    private static final String LISTENERS_URL = "listener";
    private static final String LAUNCHERS_URL = "launcher";
    private static final String GRUNTS_URL = "grunt";


    public static void loginPage() {
        SessionSetup.getPage().navigate(getUrl(LOGIN_URL));
    }

    public static void listenersPage() {
        SessionSetup.getPage().navigate(getUrl(LISTENERS_URL));
    }

    public static void launchersPage() {
        SessionSetup.getPage().navigate(getUrl(LAUNCHERS_URL));
    }

    public static void grantsPage() {
        SessionSetup.getPage().navigate(getUrl(GRUNTS_URL));
    }

    private static String getUrl(String endpoint) {
        String path = Paths.get(BASE_URL, endpoint).toString();
        System.out.println("Navigating to " + path);
        return path;
    }

}
