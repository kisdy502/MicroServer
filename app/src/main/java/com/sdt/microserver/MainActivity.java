package com.sdt.microserver;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sdt.libserver.LoginHandler;
import com.sdt.libserver.SHttpServer;
import com.sdt.libserver.UploadApkHandler;
import com.sdt.libserver.WebConfig;

/**
 * 手机微架构
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initServer(){
        WebConfig webConfig=new WebConfig(9988,16);
        SHttpServer httpServer=new SHttpServer(webConfig);
        httpServer.registerHandler(new LoginHandler());
        httpServer.registerHandler(new UploadApkHandler());
        httpServer.asyncStart();

    }

}
