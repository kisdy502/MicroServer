package com.sdt.libserver;

import android.support.v4.util.ArrayMap;
import java.net.Socket;

/**
 * Created by Administrator on 2017/12/16.
 */

public class HttpContext {

    private Socket client;

    private String method;
    private String url;
    private String version;
    private ArrayMap<String, String> headers;
    private ArrayMap<String, String> requestParams;

    public HttpContext(Socket client) {
        this.client = client;
        headers = new ArrayMap<>();
        requestParams = new ArrayMap<>();
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeadersValue(String name) {
        return headers.get(name);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayMap<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(ArrayMap<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    public void addRequestParams(String key, String value) {
        requestParams.put(key, value);
    }


}
