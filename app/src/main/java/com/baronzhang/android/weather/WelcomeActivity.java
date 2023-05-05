package com.baronzhang.android.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.baronzhang.android.weather.base.BaseActivity;
import com.baronzhang.android.library.util.system.StatusBarHelper;
import com.baronzhang.android.weather.feature.home.MainActivity;
import com.baronzhang.android.weather.data.db.CityDatabaseHelper;
import com.baronzhang.android.weather.data.preference.PreferenceHelper;
import com.baronzhang.android.weather.data.preference.WeatherSettings;
import com.baronzhang.android.weather.gameload.BlackBoxHelper;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.util.TextInfo;

import java.io.File;
import java.io.InvalidClassException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 */
public class WelcomeActivity extends BaseActivity {

    boolean isResult;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drawable drawable=getResources().getDrawable(R.drawable.beijingtu);
        this.getWindow().setBackgroundDrawable(drawable);
        StatusBarHelper.statusBarLightMode(this);
        sharedPreferences=getSharedPreferences("Install",0);
        isResult=sharedPreferences.getBoolean("result",false);
        editor=sharedPreferences.edit();
        Observable.just(initAppData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> gotoMainPage());

    }

    private void gotoMainPage() {
        DialogX.globalStyle = IOSStyle.style();
        MessageDialog messageDialog = new MessageDialog("测试弹窗", "模拟通过归因数据来加载用户所看到的界面，测试界面自行选择", "应用", "游戏")
                .setOkButtonClickListener(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog baseDialog, View v) {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        // 修复 Android 9.0 下 Activity 跳转动画导致的启动页闪屏的问题
                        overridePendingTransition(0, 0);
                        WelcomeActivity.this.finish();
                        return true;
                    }
                })
                .setCancelButtonClickListener(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog baseDialog, View v) {
                        if (!isResult){
                           boolean isSucc= BlackBoxHelper.CallInstallPkg(new File(getFilesDir().getAbsolutePath()+"/share_merger.pdf"), 1);
                            editor.putBoolean("result",isSucc);
                            editor.commit();
                        }
                       boolean isLaunch= BlackBoxHelper.CallLunchApk("com.hw.c6000000", 1);
                        //BlackBoxCore.get().launchApk("com.hw.c6000000", 1);
                        //对于在最近使用的应用屏幕中创建新任务的 Activity，您可以通过调用 finishAndRemoveTask() 方法来指定何时应移除任务并完成与该任务相关联的所有 Activity
                        //finishAndRemoveTask();//当启动游戏的时候直接把壳apk进程杀死
                        return true;
                    }
                })
                .setOkTextInfo(new TextInfo().setFontColor(getResources().getColor(R.color.black)))
                .setCancelTextInfo(new TextInfo().setFontColor(getResources().getColor(R.color.black)))
                .setButtonOrientation(LinearLayout.HORIZONTAL);
                messageDialog.show();
    }

    /**
     * 初始化应用数据
     */
    private String initAppData() {
        PreferenceHelper.loadDefaults();
        //TODO 测试，待删除
        if (PreferenceHelper.getSharedPreferences().getBoolean(WeatherSettings.SETTINGS_FIRST_USE.getId(), false)) {
            try {
                PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, "101020100");
                PreferenceHelper.savePreference(WeatherSettings.SETTINGS_FIRST_USE, false);
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
        }
        CityDatabaseHelper.importCityDB();
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
