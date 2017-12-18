package com.sdt.libserver;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/17.
 */

public interface IUriHandler {
    boolean accept(String uri);
    void handler(HttpContext httpContext) throws IOException;
}
