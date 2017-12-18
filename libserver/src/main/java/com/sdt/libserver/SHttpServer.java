package com.sdt.libserver;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SHttpServer {

    final static String CRLF = "\r\n";         //http规定的分割字符串格式
    final static String SPACE_STRING = " ";           //空格字符串
    final static String QUESTION_MARK = "\\?";  //为什么要加两个反斜杠，我还真没搞懂，不加就有红色下划线，好像也没报错
    final static String AND_MARK = "&";
    final static String EQUAL_MARK = "=";
    final static String ADD_MARK = "+";

    private static final String TAG = "SHttpServer";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";

    ExecutorService threadPool;
    private WebConfig webConfig;

    private ArrayMap<String, BaseUriHandler> uriHandlerArray;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SHttpServer(WebConfig webConfig) {
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool(); //线程会被cache，不会被立即销毁
        uriHandlerArray = new ArrayMap<>();
    }

    public void asyncStart() {
        new Thread() {
            @Override
            public void run() {
                startServer();
            }
        }.start();
    }


    public void registerHandler(BaseUriHandler handler) {
        uriHandlerArray.put(handler.getHandlerKey(), handler);
    }


    private boolean isRunning;

    private void startServer() {
        InetSocketAddress socketAddress = new InetSocketAddress(webConfig.getPort());
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(socketAddress);
            while (isRunning) {
                final Socket client = serverSocket.accept();
                threadPool.submit(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        handle(client);
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handle(Socket client) {
        try {
//            client.getOutputStream().write("hello".getBytes());
            InputStream is = client.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            String headerLine = null;
            HttpContext httpContext = new HttpContext(client);

            headerLine = StreamReadUtil.readLine(reader);
            if (!TextUtils.isEmpty(headerLine)) {
                String[] httpInfo = headerLine.split(SPACE_STRING);
                if (httpInfo.length == 3) {
                    httpContext.setMethod(httpInfo[0]);
                    httpContext.setUrl(httpInfo[1]);
                    httpContext.setVersion(httpInfo[2]);
                    while ((headerLine = StreamReadUtil.readLine(reader)) != null) {
                        if (headerLine == CRLF) {
                            break;
                        }
                        Log.d(TAG, "head line:" + headerLine);  //请求header信息
                        String[] headers = headerLine.split(":");
                        httpContext.addHeader(headers[0], headers[1]);
                    }
                    //处理url后面的参数
                    if (httpInfo[1].contains("?")) {
                        String[] urls = httpInfo[1].split(QUESTION_MARK);
                        if (urls != null && urls.length == 2) {
                            ArrayMap<String, String> getParmsMap = parseParam(urls[1], false);
                            httpContext.getRequestParams().putAll((SimpleArrayMap<? extends String, ? extends String>) getParmsMap);
                        }
                    }

                    if (httpInfo[0].toUpperCase().equals(METHOD_POST)) {
                        StringBuffer bodySb = new StringBuffer();
                        char[] bodyChars = new char[1024];
                        int len = 0;
                        while (reader.ready() && (len = reader.read(bodyChars)) > 0) {
                            bodySb.append(bodyChars, 0, len);
                        }
                        ArrayMap<String, String> postParmsMap = parseParam(bodySb.toString(), true);
                        httpContext.getRequestParams().putAll((SimpleArrayMap<? extends String, ? extends String>) postParmsMap);
                    }

                    response(httpContext);

                    // close socket, this indicate the client that the response is finished,
                    client.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * response
     * @param httpContext
     */
    private void response(HttpContext httpContext) {
        //TODO  怎么处理等待实现
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayMap<String, String> parseParam(String paramStr, boolean isBody) {
        ArrayMap<String, String> paramMap = new ArrayMap<>();
        String[] paramPairs = paramStr.trim().split(AND_MARK);
        int length = paramPairs.length;
        String[] paramItem;
        for (int i = 0; i < length; i++) {
            if (paramPairs[i].contains(EQUAL_MARK)) {
                paramItem = paramPairs[i].split(EQUAL_MARK);
                if (paramItem.length == 2) {
                    if (isBody) {
                        paramItem[1] = paramItem[1].replace(ADD_MARK, SPACE_STRING);
                    }
                    paramMap.put(paramItem[0], paramItem[1]);
                }
            }
        }
        return paramMap;
    }
}
