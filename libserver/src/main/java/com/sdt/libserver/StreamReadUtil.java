package com.sdt.libserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/12/16.
 */

public class StreamReadUtil {
    private final static int CHAR_R = '\r';
    private final static int CHAR_N = '\n';

    /**
     * 从流中读一行
     * @param inputStreamReader
     * @return
     */
    public static String readLine(InputStreamReader inputStreamReader) {
        StringBuilder stringBuilder = new StringBuilder();
        int index1 = 0, index2 = 0;
        char data;
        try {
            while (index2 != -1 && !(index1 == CHAR_R && index2 == CHAR_N)) {
                index1 = index2;
                index2 = inputStreamReader.read();
                data = (char) index2;
                stringBuilder.append(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (stringBuilder.length() != 0) ? stringBuilder.toString() : "";
    }
}
