package com.example.app_two

import android.content.Context
import java.io.*
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.xor

object GameFileUtils {
     @JvmStatic
    fun loadDrawable(): IntArray{
        val drawableInt= intArrayOf(
            Integer.valueOf(R.drawable.a as Int),
            Integer.valueOf(R.drawable.b as Int),
            Integer.valueOf(R.drawable.c as Int),
            Integer.valueOf(R.drawable.d as Int),
            Integer.valueOf(R.drawable.e as Int),
            Integer.valueOf(R.drawable.f as Int),
            Integer.valueOf(R.drawable.g as Int),
            Integer.valueOf(R.drawable.h as Int),
            Integer.valueOf(R.drawable.i as Int),
            Integer.valueOf(R.drawable.j as Int),
            Integer.valueOf(R.drawable.k as Int),
            Integer.valueOf(R.drawable.l as Int),
            Integer.valueOf(R.drawable.m as Int),
            Integer.valueOf(R.drawable.n as Int),
            Integer.valueOf(R.drawable.o as Int)
        )
        return drawableInt
    }
     @JvmStatic
     open fun MergeFiles(mContext: Context,intArray: IntArray,string: String):File{
        var file=File(mContext.filesDir.absolutePath+ "/$string")
        if (file.exists())return file

        var v = Vector<InputStream>();
        for (i in intArray){
            var ins=mContext.resources.openRawResource(i)
            v.add(ins)
        }
        var e = v.elements()
        //创建合并流
        var sis = SequenceInputStream(e);
        //创建目的源
        var bos = BufferedOutputStream( FileOutputStream(mContext.filesDir.absolutePath+ "/$string"));
        //写入文件
        var bys=ByteArray(2048);
        var len=0;
        while (sis.read(bys).also({ len = it }) > 0) {
            bos.write(bys, 0, len);
        }
        //释放资源
        sis.close();
        bos.close();
        return file
    }
     /*这里接下来的操作是将本地游戏apk以字节形式读取出来，然后再写回到应用私有目录文件夹下面**/
     @JvmStatic
     fun readFile(context: Context,targetAddress: String, sourceAddress: String):File {
         var files=File(targetAddress)
         if (files.exists())return files
         //得到这个文件的输入流
         var ins=context.assets.open("resources/$sourceAddress")
         var byyy=ByteArray(ins.available())
         ins.read(byyy)
         ins.close()
         writeFile(context,targetAddress, byyy)
         return files
     }
     fun writeFile(context: Context,string: String, byteArray: ByteArray){
         var fileOutputStream= FileOutputStream(string)
         var channel=fileOutputStream.channel
      //   var decodeByteArray=encrypt(byteArray,context.resources.getString(R.string.black_key).toByteArray())
         channel.write(ByteBuffer.wrap(byteArray))
         channel.close()
     }

    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        val result = ByteArray(data.size)
        // 使用密钥字节数组循环加密或解密
        for (i in data.indices) {
            // 数据与密钥异或, 再与循环变量的低8位异或（增加复杂度）
            result[i] = (data[i] xor key[i % key.size] xor ((i and 0xFF).toByte())).toByte()
        }
        return result
    }



}