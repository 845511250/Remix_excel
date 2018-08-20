package com.example.zuoyun.remix_excel.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by zuoyun on 2017/4/27.
 */

public class BarCodeUtil {
    public static Bitmap creatBarcode(String content, int barWidth, int barHeight){
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        try{
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, barWidth, barHeight);

            //矩阵的宽度
            int width = matrix.getWidth();
            //矩阵的高度
            int height = matrix.getHeight();
            //矩阵像素数组
            int[] pixels = new int[width * height];
            //双重循环遍历每一个矩阵点
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        //设置矩阵像素点的值
                        pixels[y * width + x] = 0xff000000;
                    }
                }
            }
            //根据颜色数组来创建位图
            /**
             * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
             * 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
             * config是一个枚举，可以用它来指定位图“质量”。
             */
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap,具体参考api

            bm.setPixels(pixels, 0, width, 0, 0, width, height);
            //将生成的条形码返回给调用者
            return bm;
        }catch (WriterException e){
            Log.e("aaa", e.getMessage());
            return null;
        }
    }
}
