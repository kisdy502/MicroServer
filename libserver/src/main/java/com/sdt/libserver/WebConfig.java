package com.sdt.libserver;

/**
 * Created by Administrator on 2017/12/16.
 */

public class WebConfig {
    private int port;
    private int maxParalles;

    public WebConfig(){

    }

    public WebConfig(int port, int maxParalles) {
        this.port = port;
        this.maxParalles = maxParalles;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParalles() {
        return maxParalles;
    }

    public void setMaxParalles(int maxParalles) {
        this.maxParalles = maxParalles;
    }
}
