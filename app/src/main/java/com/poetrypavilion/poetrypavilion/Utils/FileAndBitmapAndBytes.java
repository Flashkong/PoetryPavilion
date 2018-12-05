package com.poetrypavilion.poetrypavilion.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;

import okhttp3.ResponseBody;

public class FileAndBitmapAndBytes {
    public static byte[] fileToBytes(File file){
        ByteArrayOutputStream out = null;
        try {
            FileInputStream in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) != -1) {

                out.write(b, 0, b.length);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] s = Objects.requireNonNull(out).toByteArray();
        return s;
    }

    public static byte[] BitmapToBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static File ResponseBodyToFile(ResponseBody body,String filePath){
        InputStream is = body.byteStream();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File StringToFile(String path,String text){
        File file = new File(path);
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(file));
            ps.print(text);// 往文件里写入字符串
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Bitmap FileToBitMap(File file){
        byte[] filebyte = fileToBytes(file);
        Bitmap bitmap = BitmapFactory.decodeByteArray(filebyte, 0,
                filebyte.length);
        return bitmap;
    }
}
