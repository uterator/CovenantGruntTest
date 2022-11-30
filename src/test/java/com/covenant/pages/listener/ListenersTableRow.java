package com.covenant.pages.listener;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.covenant.common.Attachments;
import com.covenant.config.SessionSetup;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;

public class ListenersTableRow {

    enum ListenerAttribute {
        Name(0),
        ListenerType(1),
        Status(2),
        StartTime(3),
        ConnectAddresses(4),
        ConnectPort(5);

        private final int pos;
        ListenerAttribute(int pos) {
            this.pos = pos;
        }
    }

    private final Locator td;

    ListenersTableRow(Locator listenerRow) {
        td = listenerRow.locator("td");
    }

    private String getAttr(ListenerAttribute attribute) {
        return td.nth(attribute.pos).textContent();
    }

    public String getName() {
        return getAttr(ListenerAttribute.Name).trim();
    }

    public String getType() {
        return getAttr(ListenerAttribute.ListenerType);
    }

    public String getStatus() {
        return getAttr(ListenerAttribute.Status);
    }

    public String getStartTime() {
        return getAttr(ListenerAttribute.StartTime);
    }

    public String getConnectionAddress() {
        return getAttr(ListenerAttribute.ConnectAddresses);
    }

    public String getConnectionPort() {
        return getAttr(ListenerAttribute.ConnectPort);
    }

    public void waitForStatusActive() {
        assertThat(td.nth(ListenerAttribute.Status.pos))
                .hasText("Active", new LocatorAssertions.HasTextOptions()
                        .setTimeout(10000));
    }

    public void openListenerDetails() {
        td.nth(ListenerAttribute.Name.pos).click();
        SessionSetup.getPage().waitForTimeout(1000);
        Attachments.takeScreenshot("ListenerDetails");
    }
}
