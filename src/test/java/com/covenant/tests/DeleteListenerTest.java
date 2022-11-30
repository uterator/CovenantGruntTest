package com.covenant.tests;

import com.covenant.common.NavigateTo;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.covenant.pages.LoginPage;
import com.covenant.pages.listener.HTTPListenerPage;
import com.covenant.pages.listener.ListenersPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteListenerTest extends SessionSetup {

    @Test
    @DisplayName("Delete Listener")
    public void DeleteListener() {

        NavigateTo.loginPage();
        new LoginPage().loginRegular();

        // Add a listener

        NavigateTo.listenersPage();
        ListenersPage listenersPage = new ListenersPage();
        listenersPage.getListener(Properties.getListenerName()).openListenerDetails();

        HTTPListenerPage httpListenerPage = new HTTPListenerPage();
        httpListenerPage.deleteHTTPListener();
    }
}