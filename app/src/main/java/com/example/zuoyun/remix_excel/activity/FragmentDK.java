package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.tools.BitmapToJpg;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static com.example.zuoyun.remix_excel.activity.MainActivity.getLast2NewCode;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDK extends BaseFragment {
    Context context;
//    String sdCardPath = "/mnt/asec/share";
String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    int currentID;
    String childPath;
    
    @BindView(R.id.bt_remix)
    Button bt_remix;
    @BindView(R.id.iv_pillow)
    ImageView iv_pillow;

    int num;
    String strPlus = "";
    int intPlus = 1;
    @Override
    public int getLayout() {
        return R.layout.fragment_dg;
    }

    @Override
    public void initData(View view) {
        context = getContext();
        ButterKnife.bind(this, view);
        orderItems=MainActivity.instance.orderItems;
        currentID = MainActivity.instance.currentID;
        childPath = MainActivity.instance.childPath;

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_pillow.setImageDrawable(null);
                } else if (message == 1) {
                    Log.e("fragmentSock", "message1");
                    bt_remix.setClickable(true);
                    checkremix();
                } else if(message==2){
                    Log.e("fragmentSock", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapRight);
                    checkremix();
                } else if (message == 3) {
                    bt_remix.setClickable(false);
                } else if (message == 10) {
                    remix();
                }
            }
        });

        bt_remix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remix();
            }
        });
    }

    public void remix(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                for(num=orderItems.get(currentID).num;num>=1;num--) {
                    for(int i=0;i<currentID;i++) {
                        if (orderItems.get(currentID).order_number.equals(orderItems.get(i).order_number)) {
                            strPlus += "+";
                        }
                    }
                    remixx();
                    strPlus += "+";
                }
            }
        }.start();

    }

    public void remixx(){
        //
        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setStrokeWidth(2f);
        paint.setTextSize(38);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintWhite = new Paint();
        paintWhite.setColor(0xffffffff);
        paintWhite.setStrokeWidth(2f);
        paintWhite.setTypeface(Typeface.DEFAULT_BOLD);
        paintWhite.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(38);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Bitmap bitmapRFront = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 0, 0, 609, 2220);
        Bitmap bitmapRBack = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 21, 0, 609, 2220);
        Bitmap bitmapLFront = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 0, 0, 609, 2220);
        Bitmap bitmapLBack = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 21, 0, 609, 2220);

        String time = MainActivity.instance.orderDate_Print;
        try {
            Bitmap bitmapremix;
            Canvas canvasremix;

            if (orderItems.get(currentID).size == 0) {
                bitmapremix = Bitmap.createBitmap(2616+60, 2330+60, Bitmap.Config.ARGB_8888);
                canvasremix = new Canvas(bitmapremix);
                canvasremix.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                canvasremix.drawColor(0xffffffff);

                Matrix matrix = new Matrix();
                matrix.postScale(1, 1.05f);
                canvasremix.drawBitmap(bitmapRFront, matrix, null);
                matrix.reset();
                matrix.postScale(1, 1.05f);
                matrix.postTranslate(609, 0);
                canvasremix.drawBitmap(bitmapRBack, matrix, null);
                canvasremix.drawLine(609, 0, 609, 50, paint);
                canvasremix.drawLine(609, 2170, 609, 2220, paint);
                canvasremix.drawLine(611, 0, 611, 50, paintWhite);
                canvasremix.drawLine(611, 2170, 611, 2220, paintWhite);
                canvasremix.drawRect(750, 1900, 1000, 2150, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 751, 1945, paintRed);
                canvasremix.drawText(orderItems.get(currentID).order_number, 751, 2000, paint);
                canvasremix.drawText("中号袜子",751,2080,paint);
                canvasremix.drawText(time, 751, 2145, paint);

                matrix.reset();
                matrix.postScale(1, 1.05f);
                matrix.postTranslate(1398, 0);
                canvasremix.drawBitmap(bitmapLFront, matrix, null);
                matrix.reset();
                matrix.postScale(1, 1.05f);
                matrix.postTranslate(2007, 0);
                canvasremix.drawBitmap(bitmapLBack, matrix, null);
                canvasremix.drawLine(2007, 0, 2007, 50, paint);
                canvasremix.drawLine(2007, 2170, 2007, 2220, paint);
                canvasremix.drawLine(2009, 0, 2009, 50, paintWhite);
                canvasremix.drawLine(2009, 2170, 2009, 2220, paintWhite);
                canvasremix.drawRect(2150, 1900, 2400, 2150, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 2151, 1945, paintRed);
                canvasremix.drawText(orderItems.get(currentID).order_number, 2151, 2000, paint);
                canvasremix.drawText("中号袜子",2151,2080,paint);
                canvasremix.drawText(time, 2151, 2145, paint);
                canvasremix.drawBitmap(BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.border_dk), 0, 0, null);
            } else {
                bitmapremix = Bitmap.createBitmap(2616+60, 2460+60, Bitmap.Config.ARGB_8888);
                canvasremix = new Canvas(bitmapremix);
                canvasremix.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                canvasremix.drawColor(0xffffffff);

                Matrix matrix = new Matrix();
                matrix.postScale(1, 1.1081f);
                canvasremix.drawBitmap(bitmapRFront, matrix, null);
                matrix.reset();
                matrix.postScale(1, 1.1081f);
                matrix.postTranslate(609, 0);
                canvasremix.drawBitmap(bitmapRBack, matrix, null);
                canvasremix.drawLine(609, 0, 609, 50, paint);
                canvasremix.drawLine(609, 2290, 609, 2340, paint);
                canvasremix.drawLine(611, 0, 611, 50, paintWhite);
                canvasremix.drawLine(611, 2290, 611, 2340, paintWhite);
                canvasremix.drawRect(750, 1900, 1000, 2150, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 751, 1945, paintRed);
                canvasremix.drawText(orderItems.get(currentID).order_number, 751, 2000, paint);
                canvasremix.drawText("大号袜子",751,2080,paint);
                canvasremix.drawText(time, 751, 2145, paint);

                matrix.reset();
                matrix.postScale(1, 1.1081f);
                matrix.postTranslate(1398, 0);
                canvasremix.drawBitmap(bitmapLFront, matrix, null);
                matrix.reset();
                matrix.postScale(1, 1.1081f);
                matrix.postTranslate(2007, 0);
                canvasremix.drawBitmap(bitmapLBack, matrix, null);
                canvasremix.drawLine(2007, 0, 2007, 50, paint);
                canvasremix.drawLine(2007, 2290, 2007, 2340, paint);
                canvasremix.drawLine(2009, 0, 2009, 50, paintWhite);
                canvasremix.drawLine(2009, 2290, 2009, 2340, paintWhite);
                canvasremix.drawRect(2150, 1900, 2400, 2150, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 2151, 1945, paintRed);
                canvasremix.drawText(orderItems.get(currentID).order_number, 2151, 2000, paint);
                canvasremix.drawText("大号袜子",2151,2080,paint);
                canvasremix.drawText(time, 2151, 2145, paint);
                canvasremix.drawBitmap(BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.border_dk), 0, 0, null);
            }

            String sockSize = orderItems.get(currentID).size == 0 ? "M" : "L";
            String nameCombine = orderItems.get(currentID).sku + sockSize + getLast2NewCode(orderItems.get(currentID).newCode) + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapremix, fileSave, 153);

            //释放bitmap
            bitmapRFront.recycle();
            bitmapRBack.recycle();
            bitmapLFront.recycle();
            bitmapLBack.recycle();
            bitmapremix.recycle();

            //写入excel
            String writePath = sdCardPath + "/生产图/" + childPath + "/生产单.xls";
            File fileWrite = new File(writePath);
            if(!fileWrite.exists()){
                WritableWorkbook book = Workbook.createWorkbook(fileWrite);
                WritableSheet sheet = book.createSheet("sheet1", 0);
                Label label0 = new Label(0, 0, "货号");
                sheet.addCell(label0);
                Label label1 = new Label(1, 0, "结构");
                sheet.addCell(label1);
                Label label2 = new Label(2, 0, "数量");
                sheet.addCell(label2);
                Label label3 = new Label(3, 0, "业务员");
                sheet.addCell(label3);
                Label label4 = new Label(4, 0, "销售日期");
                sheet.addCell(label4);
                Label label5 = new Label(5, 0, "备注");
                sheet.addCell(label5);
                Label label6 = new Label(6, 0, "部门");
                sheet.addCell(label6);
                book.write();
                book.close();
            }

            Workbook book = Workbook.getWorkbook(fileWrite);
            WritableWorkbook workbook = Workbook.createWorkbook(fileWrite,book);
            WritableSheet sheet = workbook.getSheet(0);
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku+sockSize);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku+sockSize);
            sheet.addCell(label1);
            int num = orderItems.get(currentID).num;
            Number number2 = new Number(2, currentID+1, num);
            sheet.addCell(number2);
            Label label3 = new Label(3, currentID+1, orderItems.get(currentID).customer);
            sheet.addCell(label3);
            Label label4 = new Label(4, currentID + 1, MainActivity.instance.orderDate_Excel);
            sheet.addCell(label4);
            Label label6 = new Label(6, currentID + 1, "平台大货");
            sheet.addCell(label6);

            workbook.write();
            workbook.close();

        }catch (Exception e){
        }
        if (num == 1) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.instance.tv_finishRemixx.setText("完成");
                    if (MainActivity.instance.tb_auto.isChecked()) {
                        MainActivity.instance.setnext();
                    }
                }
            });
        }
    }
    public void checkremix(){
        if (MainActivity.instance.tb_auto.isChecked()){
            if(MainActivity.instance.leftsucceed&&MainActivity.instance.rightsucceed)
                remix();
        }
    }

}
