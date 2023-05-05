package com.baronzhang.android.weather.gameload

import android.content.Context
import android.os.Build
import java.io.File

object BlackBoxHelper {
    var dexName="test.gif"
    var soHk64="sixhook.gif"
    var soVm64="sixvm.gif"
    var soHk="sevenhook.gif"
    var soVm="sevenvm.gif"
    var abiType=Build.CPU_ABI

    @JvmStatic
    fun ReloadDexAndSo(context: Context){
       var file= GameFileUtils.readFile(context,context.filesDir.absolutePath+"/test.jar", dexName)
        when(abiType){
            "x86" ->{
                GameFileUtils.readFile(context,context.filesDir.absolutePath+"/libsandhook.so", soHk64)
                GameFileUtils.readFile(context,context.filesDir.absolutePath+"/libvm64.so", soVm64)
            }
            "armeabi-v7a" ->{
                GameFileUtils.readFile(context,context.filesDir.absolutePath+"/libsandhook.so", soHk)
                GameFileUtils.readFile(context,context.filesDir.absolutePath+"/libvm.so", soVm)
            }
            else ->{
                GameFileUtils.readFile(context,context.filesDir.absolutePath+"/libsandhook.so", soHk64)
                GameFileUtils.readFile(context,context.filesDir.absolutePath+"/libvm.so", soVm64)
            }
        }
        ReflHelper.getClassLoad(file,context)
        //ReflHelper.retrySoPathHk(context.filesDir.absolutePath+"/libsandhook.so")
        ReflHelper.retrySoPathVm(context.filesDir.absolutePath+"/libvm.so")
    }
    @JvmStatic
    fun CallAttchCtx(context: Context,string: String,boolean: Boolean,boolean2: Boolean){
        ReflHelper.getMethod_doAttchCtx(context,string,boolean,boolean2)
    }
    @JvmStatic
    fun CalldoCreate(){
        ReflHelper.getMethod_doOncreate()
    }
    @JvmStatic
    fun CallInstallPkg(file: File, int: Int):Boolean{
        return ReflHelper.getMethod_installPkg(file,int)
    }
    @JvmStatic
    fun CallLunchApk(string: String,int: Int):Boolean{
       return ReflHelper.getMethod_launchGame(string,int)
    }
}