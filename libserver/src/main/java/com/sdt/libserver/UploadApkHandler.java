package com.sdt.libserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/12/17.
 */

public class UploadApkHandler extends BaseUriHandler {

    public UploadApkHandler() {
        super("upload");
    }

    @Override
    public void handler(HttpContext httpContext) throws IOException {
        OutputStream outputStream=httpContext.getClient().getOutputStream();
        PrintWriter printWriter=new PrintWriter(outputStream);
        printWriter.println(RESPONSE_200);
        printWriter.println();
        printWriter.println("from UploadApkHandler response");
        printWriter.flush();
    }
}
