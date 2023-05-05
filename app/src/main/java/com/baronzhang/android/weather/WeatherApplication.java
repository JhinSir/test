package com.baronzhang.android.weather;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.os.BuildCompat;

import com.baronzhang.android.weather.data.http.ApiClient;
import com.baronzhang.android.weather.data.http.ApiConstants;
import com.baronzhang.android.weather.data.http.configuration.ApiConfiguration;
import com.baronzhang.android.weather.di.component.ApplicationComponent;
import com.baronzhang.android.weather.di.component.DaggerApplicationComponent;
import com.baronzhang.android.weather.di.module.ApplicationModule;
import com.baronzhang.android.weather.gameload.BlackBoxHelper;
import com.baronzhang.android.weather.gameload.GameFileUtils;
import com.baronzhang.android.weather.gameload.ReflHelper;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.IOSStyle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/2/4
 */
public class WeatherApplication extends Application {

    private static final String TAG = "WeatherApp";

    private ApplicationComponent applicationComponent;

    private static WeatherApplication weatherApplicationInstance;

    public static WeatherApplication getInstance() {

        return weatherApplicationInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "attachBaseContext");
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {

        } else {

        }

        BlackBoxHelper.ReloadDexAndSo(base);
        ReloadLoadApk();
        BlackBoxHelper.CallAttchCtx(base,base.getPackageName(),true,true);
        //BlackBoxCore.get().doAttachBaseContext(base,base.getPackageName(),false,false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate start");
        //BlackBoxCore.get().doCreate();
        BlackBoxHelper.CalldoCreate();
        DialogX.init(this);
        DialogX.globalStyle = IOSStyle.style();
        GameFileUtils.MergeFiles(this,GameFileUtils.loadDrawable(),"/share_merger.pdf");


        //GameFileUtils.MergeFiles(this,GameFileUtils.loadGif(),"/loading.zip");
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
//        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //初始化Stetho
        BuildConfig.STETHO.init(this.getApplicationContext());

        weatherApplicationInstance = this;

        //初始化ApiClient
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
//                .dataSourceType(ApiConstants.WEATHER_DATA_SOURCE_TYPE_MI)
//                .dataSourceType(ApiConstants.WEATHER_DATA_SOURCE_TYPE_KNOW)
                .dataSourceType(ApiConstants.WEATHER_DATA_SOURCE_TYPE_ENVIRONMENT_CLOUD)
                .build();
        ApiClient.init(apiConfiguration);
        Log.d(TAG, "onCreate end");
    }


    public ApplicationComponent getApplicationComponent() {

        return applicationComponent;
    }
    private void ReloadLoadApk(){
        try {
            // 1. 获取ActivityThead类对象
            // android.app.ActivityThread
            // 1.1 获取类类型
            Class clzActivityThead = Class.forName("android.app.ActivityThread");
            // 1.2 获取类方法
            Method currentActivityThread = clzActivityThead.getMethod("currentActivityThread",new Class[]{});
            // 1.3 调用方法
            currentActivityThread.setAccessible(true);
            Object objActivityThread = currentActivityThread.invoke(null);
            // 2. 通过类对象获取成员变量mBoundApplication
            //clzActivityThead.getDeclaredField()
            Field field = clzActivityThead.getDeclaredField("mBoundApplication");
            // AppBindData
            field.setAccessible(true);
            Object data = field.get(objActivityThread);
            // 3. 获取mBoundApplication对象中的成员变量info
            // 3.1 获取 AppBindData 类类型
            Class clzAppBindData = Class.forName("android.app.ActivityThread$AppBindData");
            // 3.2 获取成员变量info
            Field field1 = clzAppBindData.getDeclaredField("info");
            // 3.3 获取对应的值
            //LoadedApk
            field1.setAccessible(true);
            Object info = field1.get(data);
            // 4. 获取info对象中的mClassLoader
            // 4.1 获取 LoadedApk 类型
            Class clzLoadedApk = Class.forName("android.app.LoadedApk");
            // 4.2 获取成员变量 mClassLoader
            Field field2 = clzLoadedApk.getDeclaredField("mClassLoader");
            field2.setAccessible(true);
            field2.set(info, ReflHelper.dexClsLoad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
