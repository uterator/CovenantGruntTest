package com.covenant.config;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Properties {

    public static String getCovenantHost() {
        return getPropertyStrict("CovenantHost");
    }

    public static String getCovenantUname() {
        return getPropertyStrict("CovenantUname");
    }

    public static String getCovenantPword() {
        return getPropertyStrict("CovenantPword");
    }

    public static String getScreenshotsFolder() {
        return getPropertyStrict("ScreenshotsFolder");
    }

    public static String getLaunchersFolder() {
        return getPropertyStrict("LauncherFolder");
    }

    public static String getListenerFolder() {
        return getPropertyStrict("ListenerFolder");
    }

    // ListenerNameFile is for outputting the listener name from Java to out environment
    public static String getListenerNameFile() {
        return getPropertyStrict("ListenerNameFile");
    }

    // ListenerName is for receiving the listener name from out environment
    public static String getListenerName() {
        return getPropertyStrict("ListenerName");
    }

    public static String getHostIP() {
        return getPropertyStrict("HostIP");
    }
    public static String getDestinationHostName() {
        return getPropertyStrict("WinHostName");
    }
    public static String getDestinationUserName() {
        return getPropertyStrict("WinUname");
    }

    private static String getPropertyStrict(String name) {
        String value = System.getProperty(name, System.getenv(name));
        assertNotNull(value, "Property value was null: '" + name + "'");
        return value;
    }
}
