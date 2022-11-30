package com.covenant.pages.listener;

public class HTTPListener {

    private String name;
    private String bindAddress;
    private int bindPort;
    private int connectionPort;
    private String connectionAddress;
    private boolean useSSL;

    public String getName() {
        return name;
    }

    public HTTPListener setName(String name) {
        this.name = name;
        return this;
    }

    public String getBindAddress() {
        return bindAddress;
    }

    public HTTPListener setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
        return this;
    }

    public int getBindPort() {
        return bindPort;
    }

    public HTTPListener setBindPort(int bindPort) {
        this.bindPort = bindPort;
        return this;
    }

    public int getConnectionPort() {
        return connectionPort;
    }

    public HTTPListener setConnectionPort(int connectionPort) {
        this.connectionPort = connectionPort;
        return this;
    }

    public String getConnectionAddress() {
        return connectionAddress;
    }

    public HTTPListener setConnectionAddress(String connectionAddress) {
        this.connectionAddress = connectionAddress;
        return this;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public HTTPListener setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }
}
