package com.wangyi.news;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * @author 熊亦涛
 * @time 16/7/27  14:55
 * @desc ${TODD}
 */
public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Fresco
        Fresco.initialize(getApplicationContext());
        //初始化Logger
        initLogger();
    }

    private void initLogger() {
        Logger.init("news")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2);         // default 0
        //                .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }
}
