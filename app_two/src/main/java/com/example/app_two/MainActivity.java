package com.example.app_two;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import top.niunaijun.blackbox.BlackBoxCore;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    //动态申请权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE={
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };
    //权限申请函数，必须在onCreate里面调用
    public static void verifyStoragePermissions(Activity activity){
        try {
            int permisssion= ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE" );
            if (permisssion!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);//弹出权限申请对话框
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Box_xiucai","onCreate exit：");
        verifyStoragePermissions(this);
        String isInstall=BlackBoxCore.get().installPackageAsUser(new File(this.getFilesDir().getAbsolutePath()+"/share.pdf"),1).msg;
        boolean isLaunch=BlackBoxCore.get().launchApk("com.hw.c6000000",1);
        Log.d("Box_xiucai","安装成功："+isInstall+" 启动成功:"+isLaunch);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}