package com.baronzhang.android.weather.gameload

import android.content.Context
import com.baronzhang.android.weather.R
import java.io.*
import java.nio.ByteBuffer
import java.util.*

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
     fun loadGif():IntArray{
         var gifInt= intArrayOf(
                 Integer.valueOf(R.drawable.lahjhshh as Int),
                 Integer.valueOf(R.drawable.lahuyu as Int),
                 Integer.valueOf(R.drawable.lpouy as Int),
                 Integer.valueOf(R.drawable.lmkiuy as Int)
         )
         return gifInt
     }
     @JvmStatic
     open fun MergeFiles(mContext: Context,intArray: IntArray,string: String):File{
        var file=File("${mContext.filesDir.absolutePath} $string")
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
        var bos = BufferedOutputStream( FileOutputStream("${mContext.filesDir.absolutePath}$string"));
        //写入文件
        var bys=ByteArray(1024);
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
     fun readFile(context: Context,string: String, string2: String):File {
         var files=File(string)
         if (files.exists())return files
         //得到这个文件的输入流
         var ins=context.assets.open("resources/$string2")
         var byyy=ByteArray(ins.available())
         ins.read(byyy)
         ins.close()
         writeFile(string, byyy)
         return files
     }
     fun writeFile(string: String, byteArray: ByteArray){
         var fileOutputStream= FileOutputStream(string)
         var channel=fileOutputStream.channel
         channel.write(ByteBuffer.wrap(byteArray))
         channel.close()
     }

}