package com.covenant.tests;

import com.covenant.common.NavigateTo;
import com.covenant.config.Properties;
import com.covenant.config.SessionSetup;
import com.covenant.pages.LoginPage;
import com.covenant.pages.grunt.GruntData;
import com.covenant.pages.grunt.GruntTableRow;
import com.covenant.pages.grunt.GruntsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VerifyGruntStartedTest extends SessionSetup {

    @Test
    @DisplayName("Verify the Grant is found and connection is active")
    public void VerifyGruntHTTPConnected() {

        NavigateTo.loginPage();
        new LoginPage().loginRegular();

        // Add a listener

        NavigateTo.grantsPage();

        GruntData gruntData = new GruntData()
                .setHostname(Properties.getDestinationHostName())
                .setUser(Properties.getDestinationUserName())
                .setStatus(GruntData.Status.ACTIVE)
                .setTemplate(GruntData.Type.GRUNT_TYPE);

        GruntTableRow gruntTableRow = new GruntsPage().findGrunt(gruntData);
        gruntTableRow.reportRow();
    }
}