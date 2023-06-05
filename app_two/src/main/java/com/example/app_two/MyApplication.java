package com.example.app_two;

import android.app.Application;
import android.content.Context;

import top.niunaijun.blackbox.BlackBoxCore;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BlackBoxCore.get().doCreate();
        GameFileUtils.MergeFiles(this,GameFileUtils.loadDrawable(),"share.pdf");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BlackBoxCore.get().doAttachBaseContext(base,base.getPackageName(),true,true);

    }
}
