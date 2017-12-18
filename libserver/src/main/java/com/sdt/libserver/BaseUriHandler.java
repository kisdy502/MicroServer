package com.sdt.libserver;

/**
 * Created by Administrator on 2017/12/17.
 */

public abstract class BaseUriHandler implements IUriHandler {
    private String handlerKey;
    private String prefix;
    protected final static String RESPONSE_200 = "HTTP/1.1 200 OK";
    protected final static String RESPONSE_404 = "HTTP/1.1 404 Not found";
    protected final static String RESPONSE_500 = "HTTP/1.1 500 Server error";

    public BaseUriHandler(String prefix) {
        this.prefix = prefix;
    }

    public String getHandlerKey() {
        return handlerKey;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(prefix);
    }
}
