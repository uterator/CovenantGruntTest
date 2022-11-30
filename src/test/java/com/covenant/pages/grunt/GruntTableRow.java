package com.covenant.pages.grunt;

import com.microsoft.playwright.Locator;

public class GruntTableRow {

    private final GruntData gruntData = new GruntData();

    enum GruntRowAttribute {
        Sign(0),
        Name(1),
        Hostname(2),
        User(3),
        Integrity(4),
        LastCheckIn(5),
        Status(6),
        Note(7),
        Template(8);

        private final int pos;
        GruntRowAttribute(int pos) {
            this.pos = pos;
        }
    }

    private final Locator td;

    GruntTableRow(Locator listenerRow) {
        td = listenerRow.locator("td");
        readRow();
    }

    private String getAttr(GruntRowAttribute attribute) {
        return td.nth(attribute.pos).textContent();
    }

    public String getName() {
        return getAttr(GruntRowAttribute.Name);
    }

    public String getHostname() {
        return getAttr(GruntRowAttribute.Hostname);
    }

    public String getUser() {
        return getAttr(GruntRowAttribute.User);
    }

    public String getIntegrity() {
        return getAttr(GruntRowAttribute.Integrity);
    }

    public String getLastCheckIn() {
        return getAttr(GruntRowAttribute.LastCheckIn);
    }

    public String getStatus() {
        return getAttr(GruntRowAttribute.Status);
    }

    public String getNote() {
        return getAttr(GruntRowAttribute.Note);
    }

    public String getTemplate() {
        return getAttr(GruntRowAttribute.Template);
    }

    public void readRow() {
        gruntData.setName(getName())
                .setHostname(getHostname())
                .setUser(getUser())
                .setIntegrity(getIntegrity())
                .setLastCheckIn(getLastCheckIn())
                .setStatus(getStatus())
                .setTemplate(getTemplate());

    }

    public void reportRow() {
        gruntData.report();
    }

    public GruntData getRowData() {
        return gruntData;
    }

}