package com.covenant.pages.launcher;

import javax.annotation.Nullable;

public class LauncherData {
    private String ListenerName = null;
    private String ImplantTemplate = null;
    private String DotNetVersion = null;
    private Boolean ValidateCert = null;
    private Boolean UseCertPinning = null;
    private int Delay = 5;
    private int JitterPercent = 10;
    private int ConnectAttempts = 5000;
    private String KillDate = null;


    @Nullable
    public String getListenerName() {
        return ListenerName;
    }

    public LauncherData setListenerName(String listenerName) {
        ListenerName = listenerName;
        return this;
    }

    @Nullable
    public String getImplantTemplate() {
        return ImplantTemplate;
    }

    public LauncherData setImplantTemplate(String implantTemplate) {
        ImplantTemplate = implantTemplate;
        return this;
    }

    @Nullable
    public String getDotNetVersion() {
        return DotNetVersion;
    }

    public LauncherData setDotNetVersion(String dotNetVersion) {
        DotNetVersion = dotNetVersion;
        return this;
    }

    @Nullable
    public Boolean getValidateCert() {
        return ValidateCert;
    }

    public LauncherData setValidateCert(boolean validateCert) {
        ValidateCert = validateCert;
        return this;
    }

    @Nullable
    public Boolean getUseCertPinning() {
        return UseCertPinning;
    }

    public LauncherData setUseCertPinning(boolean useCertPinning) {
        UseCertPinning = useCertPinning;
        return this;
    }

    public int getDelay() {
        return Delay;
    }

    public LauncherData setDelay(int delay) {
        Delay = delay;
        return this;
    }

    public int getJitterPercent() {
        return JitterPercent;
    }

    public LauncherData setJitterPercent(int jitterPercent) {
        JitterPercent = jitterPercent;
        return this;
    }

    public int getConnectAttempts() {
        return ConnectAttempts;
    }

    public LauncherData setConnectAttempts(int connectAttempts) {
        ConnectAttempts = connectAttempts;
        return this;
    }

    @Nullable
    public String getKillDate() {
        return KillDate;
    }

    public LauncherData setKillDate(String killDate) {
        KillDate = killDate;
        return this;
    }
}
