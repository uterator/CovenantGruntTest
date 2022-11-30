package com.covenant.tests;

import com.covenant.common.Attachments;
import com.covenant.common.NavigateTo;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.covenant.pages.LoginPage;
import com.covenant.pages.launcher.BinaryLauncherPage;
import com.covenant.pages.launcher.LauncherData;
import com.covenant.pages.launcher.LaunchersPage;
import com.covenant.pages.listener.HTTPListener;
import com.covenant.pages.listener.HTTPListenerPage;
import com.covenant.pages.listener.ListenersPage;
import com.covenant.pages.listener.ListenersTableRow;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateListenerLauncherTest extends SessionSetup {

    @Test
    @DisplayName("Create Listener and Launcher")
    public void CreateListenerLauncher() {

        NavigateTo.loginPage();
        new LoginPage().loginRegular();

        // Add a listener

        NavigateTo.listenersPage();
        new ListenersPage().clickAddNewListener();

        HTTPListener httpListener = new HTTPListener()
                .setConnectionAddress(Properties.getHostIP());

        new HTTPListenerPage().registerHTTPListener(httpListener);

        ListenersTableRow listenersTableRow = new ListenersPage().getListener(httpListener.getName());

        listenersTableRow.waitForStatusActive();

        String listenerName = listenersTableRow.getName();
        System.out.println("Listener name: " + listenerName);
        System.out.println("Listener status: " + listenersTableRow.getStatus());

        Attachments.printTextToFile(listenerName, Properties.getListenerNameFile());


        // Generate Launcher

        NavigateTo.launchersPage();
        LaunchersPage launchersPage = new LaunchersPage();

        launchersPage.clickBinaryLauncher();

        BinaryLauncherPage binaryLauncherPage = new BinaryLauncherPage();
        LauncherData launcherData = new LauncherData()
                .setDotNetVersion("Net40")
                .setUseCertPinning(false)
                .setValidateCert(false)
                .setListenerName(httpListener.getName());

        binaryLauncherPage.fillData(launcherData);
        Path binaryLauncherPath = binaryLauncherPage.generateAndDownload();

        String fileName = binaryLauncherPath.getFileName().toString();
        System.out.println(fileName);
    }
}