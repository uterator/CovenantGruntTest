package com.covenant.pages.grunt;

import javax.annotation.Nullable;

public class GruntData {

    public enum Status {
        ACTIVE("Active"),
        LOST("Lost");
        private final String value;
        Status(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

    public enum Type {
        GRUNT_TYPE("GruntHTTP");
        private final String value;
        Type(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

    private String Sign = null;
    private String Name = null;
    private String Hostname = null;
    private String User = null;
    private String Integrity = null;
    private String LastCheckIn = null;
    private String Status = null;
    private String Note = null;
    private String Template = null;

    @Nullable
    public String getSign() {
        return Sign;
    }

    public GruntData setSign(String sign) {
        Sign = sign;
        return this;
    }

    @Nullable
    public String getName() {
        return Name;
    }

    public GruntData setName(String name) {
        Name = name;
        return this;
    }

    @Nullable
    public String getHostname() {
        return Hostname;
    }

    public GruntData setHostname(String hostname) {
        Hostname = hostname;
        return this;
    }

    @Nullable
    public String getUser() {
        return User;
    }

    public GruntData setUser(String user) {
        User = user;
        return this;
    }

    @Nullable
    public String getIntegrity() {
        return Integrity;
    }

    public GruntData setIntegrity(String integrity) {
        Integrity = integrity;
        return this;
    }

    @Nullable
    public String getLastCheckIn() {
        return LastCheckIn;
    }

    public GruntData setLastCheckIn(String lastCheckIn) {
        LastCheckIn = lastCheckIn;
        return this;
    }

    @Nullable
    public String getStatus() {
        return Status;
    }

    public GruntData setStatus(Status status) {
        setStatus(status.getValue());
        return this;
    }

    public GruntData setStatus(String status) {
        Status = status;
        return this;
    }


    @Nullable
    public String getNote() {
        return Note;
    }

    public GruntData setNote(String note) {
        Note = note;
        return this;
    }

    @Nullable
    public String getTemplate() {
        return Template;
    }

    public GruntData setTemplate(String template) {
        Template = template;
        return this;
    }

    public GruntData setTemplate(GruntData.Type template) {
        setTemplate(template.getValue());
        return this;
    }

    public void report() {
        StringBuilder builder = new StringBuilder();
        reportBuilder(builder, "Name", Name);
        reportBuilder(builder, "Hostname", Hostname);
        reportBuilder(builder, "User", User);
        reportBuilder(builder, "Integrity", Integrity);
        reportBuilder(builder, "LastCheckIn", LastCheckIn);
        reportBuilder(builder, "Status", Status);
        reportBuilder(builder, "Note", Note);
        reportBuilder(builder, "Template", Template);
        System.out.println("Grunt line details:\n" + builder);
    }

    public void reportBuilder(StringBuilder builder, String name, String value) {
        if (value != null) { builder.append("  ").append(name).append(" = ").append(value.trim()).append("\n"); }
    }
}