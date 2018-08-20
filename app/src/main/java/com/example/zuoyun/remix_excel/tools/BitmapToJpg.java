package com.example.zuoyun.remix_excel.tools;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.zuoyun.remix_excel.activity.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zuoyun on 2017/4/17.
 */

public class BitmapToJpg {
    public static void save(Bitmap bitmap, File file, int dpi) {
        try {
            ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
            byte[] imageData = imageByteArray.toByteArray();
            imageByteArray.close();
            setDpi(imageData, dpi);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(imageData);
            fileOutputStream.close();
            imageData = null;
            MainActivity.instance.doRequestFinish();
        }catch (Exception e){
            Log.e("aaa", "Wrong in Class 'BitmapToJpg'");
            Log.e("aaa", e.getMessage());
        }
    }

    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
    }
}
