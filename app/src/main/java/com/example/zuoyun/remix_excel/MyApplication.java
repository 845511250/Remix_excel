package com.example.zuoyun.remix_excel;

import android.app.Application;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import java.util.Date;

/**
 * Created by zuoyun on 2016/11/3.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NoHttp.initialize(this);
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpLogger:");// 设置NoHttp打印Log的tag。
    }

    public static int dd = Integer.parseInt( (new Date().getTime()+"").substring(1,2) );

}
