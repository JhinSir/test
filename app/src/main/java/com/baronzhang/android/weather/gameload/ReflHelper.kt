package com.baronzhang.android.weather.gameload

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Method

object ReflHelper {
    @JvmStatic
    lateinit var dexClsLoad:DexClassLoader
    @JvmStatic
    fun getClassLoad(file: File, context: Context):DexClassLoader{
        var dexClassLoader=DexClassLoader(file.absolutePath, context.filesDir.absolutePath, context.filesDir.absolutePath, context.classLoader)
        dexClsLoad=dexClassLoader
        //getMethod_LoadApk(dexClsLoad)
        return dexClassLoader
    }
    @JvmStatic
    fun retrySoPathVm(string: String){
        var classes= dexClsLoad.loadClass("top.niunaijun.blackbox.client.VMCore")
        var field=classes.getField("soPath")
        field.set(null, string)
    }
    @JvmStatic
    fun retrySoPathHk(string: String){
        var classes= dexClsLoad.loadClass("com.swift.sandhook.SandHookConfig")
        var filed=classes.getField("libSandHookPath")
        filed.set(null, string)
    }


    @JvmStatic
    fun getMethod_Get():Any{
        var classes=dexClsLoad.loadClass("top.niunaijun.blackbox.BlackBoxCore")
        var instance=classes.getConstructor().newInstance()
        //BlackBoxCore.get().doAttachBaseContext(base,base.getPackageName(),false,false);
        //BlackBoxCore.get().doAttachBaseContext(base,base.getPackageName(),false,false);
        val test3 = classes.getDeclaredMethod("get")
        val result = test3.invoke(instance)
        return result

    }
    @JvmStatic
    fun getMethod_doAttchCtx(context: Context, string: String, boolean: Boolean, boolean2: Boolean){
          var clazz= getMethod_Get().javaClass
          var instance=  clazz.newInstance()
          var method=  clazz.getDeclaredMethod("doAttachBaseContext", Context::class.java, String::class.java, Boolean::class.java, Boolean::class.java)
          method.invoke(instance, context, string, boolean, boolean2)
    }
    @JvmStatic
    fun getMethod_doOncreate(){
        var clazz= getMethod_Get().javaClass
        var instance=clazz.newInstance()
        var method=clazz.getDeclaredMethod("doCreate")
        method.invoke(instance)
    }
    @JvmStatic
    fun getMethod_installPkg(file: File, int: Int):Boolean{
        var clazz= getMethod_Get().javaClass
        var instance=clazz.newInstance()
        var ObjectAny=clazz.getDeclaredMethod("installPackageAsUser", File::class.java, Int::class.java).invoke(instance, file, int)
         return ObjectAny.javaClass.getDeclaredField("success").getBoolean(ObjectAny)
    }
    @JvmStatic
    fun getMethod_launchGame(string: String, int: Int):Boolean{
        var clazz= getMethod_Get().javaClass
        var instance=clazz.newInstance()
        var method=clazz.getDeclaredMethod("launchApk", String::class.java, Int::class.java)
         return method.invoke(instance, string, int) as Boolean
    }
//    fun getMethod_LoadApk(dexClassLoader: DexClassLoader?) {
//        try {
//            val cls = Class.forName("android.app.ActivityThread")
//            Method currentActivityThread = cls.getMethod("currentActivityThread",new Class[]{});
//            val currentActivityThread = cls.getMethod("currentActivityThread",arrayOf<Class<*>>())
//            val method = cls.getMethod("currentActivityThread", arrayOf<Class<*>>())
//            method.setAccessible(true)
//            val invoke: Any = method.invoke(null, arrayOfNulls<Any>(0))
//            val declaredField = cls.getDeclaredField("mBoundApplication")
//            declaredField.setAccessible(true)
//            val obj: Any = declaredField.get(invoke)
//            val declaredField2 = Class.forName("android.app.ActivityThread$AppBindData").getDeclaredField("info")
//            declaredField2.setAccessible(true)
//            val obj2: Any = declaredField2.get(obj)
//            val declaredField3 = Class.forName("android.app.LoadedApk").getDeclaredField("mClassLoader")
//            declaredField3.setAccessible(true)
//            declaredField3.set(obj2, dexClassLoader)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


}