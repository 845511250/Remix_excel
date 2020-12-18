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

/**
 * Created by zuoyun on 2016/10/6.
 */

public class FragmentDD extends BaseFragment {
    Context context;
    //    String sdCardPath = "/mnt/asec/share";
    String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    int currentID;
    String childPath;

    @BindView(R.id.iv_fg2_leftup)
    ImageView iv_leftup;
    @BindView(R.id.iv_fg2_leftdown)
    ImageView iv_leftdown;
    @BindView(R.id.iv_fg2_rightup)
    ImageView iv_rightup;
    @BindView(R.id.iv_fg2_rightdown)
    ImageView iv_rightdown;
    @BindView(R.id.bt_remix)
    Button bt_remix;
    @BindView(R.id.iv_sample1)
    ImageView iv_sample1;
    @BindView(R.id.iv_sample2)
    ImageView iv_sample2;

    int width, height;
    int num;
    String strPlus = "";
    int intPlus = 1;
    Paint paint,paintRed,paintBlue, rectPaint;
    String time = MainActivity.instance.orderDate_Print;


    @Override
    public int getLayout() {
        return R.layout.fragment_dd;
    }

    @Override
    public void initData(View view) {
        context = getContext();
        ButterKnife.bind(this, view);
        orderItems=MainActivity.instance.orderItems;
        currentID = MainActivity.instance.currentID;
        childPath = MainActivity.instance.childPath;

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(38);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(38);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message,String sampleurl) {
                if(message==0){
                    iv_leftup.setImageDrawable(null);
                    iv_rightup.setImageDrawable(null);
                    Log.e("fragment_dd_old", "message0");
                } else if (message == 1) {
                    Log.e("fragment_dd_old", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_leftup.setImageBitmap(MainActivity.instance.bitmapLeft);
//                    Glide.with(context).load(sampleurl).into(iv_sample1);
                    checkremix();
                } else if(message==2){
                    Log.e("fragment_dd_old", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_rightup.setImageBitmap(MainActivity.instance.bitmapRight);
//                    Glide.with(context).load(sampleurl).into(iv_sample2);
                    checkremix();
                } else if (message==3){
                    bt_remix.setClickable(false);
                } else if (message == 10) {
                    remix();
                }
            }
        });

        //******************************************************************************************
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

    void drawTextRight(Canvas canvas, String LR) {
        String isKK = orderItems.get(currentID).sku.equals("KK") ? "KK(MD鞋底) " : "";

        canvas.drawRect(120, 720 - 40, 1520, 720, rectPaint);
        canvas.drawText(time, 120, 716, paint);
        canvas.drawText(isKK + orderItems.get(currentID).order_number, 320, 716, paint);
        canvas.drawText(orderItems.get(currentID).newCodeStr, 700, 716, paint);
        canvas.drawText("生产尺寸:" + (orderItems.get(currentID).size - 1) + "码" + orderItems.get(currentID).color + " " + LR, 1120, 716, paintRed);
    }
    void drawTextLeft(Canvas canvas, String LR) {
        String isKK = orderItems.get(currentID).sku.equals("KK") ? "KK(MD鞋底) " : "";

        canvas.drawRect(70, 720 - 40, 1500, 720, rectPaint);
        canvas.drawText("生产尺寸:" + (orderItems.get(currentID).size - 1) + "码" + orderItems.get(currentID).color + " " + LR, 70, 716, paintRed);
        canvas.drawText(orderItems.get(currentID).newCodeStr, 500, 716, paint);
        canvas.drawText(time, 900, 716, paint);
        canvas.drawText(isKK + orderItems.get(currentID).order_number, 1100, 716, paint);
    }

    public void remixx(){
        setScale(orderItems.get(currentID).size);
        Bitmap bitmapDBRight = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dd41right);
        Bitmap bitmapDBLeft = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dd41left);

        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 1463, 692, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 1463, 692, true);

        Bitmap bitmapRR = Bitmap.createBitmap(1567, 804, Bitmap.Config.ARGB_8888);
        Canvas canvasRR = new Canvas(bitmapRR);
        canvasRR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRR.drawBitmap(MainActivity.instance.bitmapRight, 52, 6, null);
        canvasRR.drawBitmap(bitmapDBRight,0,0,null);

        Bitmap bitmapRL = Bitmap.createBitmap(1567, 804, Bitmap.Config.ARGB_8888);
        Canvas canvasRL = new Canvas(bitmapRL);
        canvasRL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRL.drawBitmap(MainActivity.instance.bitmapLeft, 52, 6, null);
        canvasRL.drawBitmap(bitmapDBLeft,0,0,null);

        Bitmap bitmapLR = Bitmap.createBitmap(1567, 804, Bitmap.Config.ARGB_8888);
        Canvas canvasLR = new Canvas(bitmapLR);
        canvasLR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLR.drawBitmap(MainActivity.instance.bitmapRight, 52, 6, null);
        canvasLR.drawBitmap(bitmapDBRight,0,0,null);

        Bitmap bitmapLL = Bitmap.createBitmap(1567, 804, Bitmap.Config.ARGB_8888);
        Canvas canvasLL = new Canvas(bitmapLL);
        canvasLL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLL.drawBitmap(MainActivity.instance.bitmapLeft, 52, 6, null);
        canvasLL.drawBitmap(bitmapDBLeft,0,0,null);

        bitmapDBLeft.recycle();
        bitmapDBRight.recycle();

        //drawText
        drawTextRight(canvasRR, "右外");
        drawTextRight(canvasLR, "左内");
        drawTextLeft(canvasRL, "右内");
        drawTextLeft(canvasLL, "左外");
        bitmapRR = Bitmap.createScaledBitmap(bitmapRR, width, height, true);
        bitmapRL = Bitmap.createScaledBitmap(bitmapRL, width, height, true);
        bitmapLR = Bitmap.createScaledBitmap(bitmapLR, width, height, true);
        bitmapLL = Bitmap.createScaledBitmap(bitmapLL, width, height, true);


        //bitmapCombine
        Bitmap bitmapCombine = Bitmap.createBitmap(width + 59, height * 4 + 200, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine = new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        Matrix matrixCombine = new Matrix();

        canvasCombine.drawBitmap(bitmapRR, 0, 0, null);
        bitmapRR.recycle();

        matrixCombine.reset();
        matrixCombine.postRotate(180);
        matrixCombine.postTranslate(width, height * 2 + 80);
        canvasCombine.drawBitmap(bitmapRL, matrixCombine, null);
        bitmapRL.recycle();

        matrixCombine.reset();
        matrixCombine.postTranslate(0, height * 2 + 120);
        canvasCombine.drawBitmap(bitmapLR, matrixCombine, null);
        bitmapLR.recycle();

        matrixCombine.reset();
        matrixCombine.postRotate(180);
        matrixCombine.postTranslate(width, height * 4 + 200);
        canvasCombine.drawBitmap(bitmapLL, matrixCombine, null);
        bitmapLL.recycle();

        try {
            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku + orderItems.get(currentID).size : "";
            String nameCombine = noNewCode + orderItems.get(currentID).sku + orderItems.get(currentID).newCode + orderItems.get(currentID).color + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 150);

            //释放bitmap
            bitmapCombine.recycle();

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
            Label label0 = new Label(0, currentID + 1, orderItems.get(currentID).order_number + orderItems.get(currentID).sku + orderItems.get(currentID).size + printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku+orderItems.get(currentID).size+printColor);
            sheet.addCell(label1);
            Number number2 = new Number(2, currentID+1, orderItems.get(currentID).num);
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
            if (MainActivity.instance.bitmapPillow != null) {
                MainActivity.instance.bitmapPillow.recycle();
            }
            if (MainActivity.instance.bitmapLeft != null) {
                MainActivity.instance.bitmapLeft.recycle();
                MainActivity.instance.bitmapRight.recycle();
            }

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

    void setScale(int size){
        switch (size + 1 - 1) {
            case 36:
                width = 1376;
                height = 763;
                break;
            case 37:
                width = 1418;
                height = 749;
                break;
            case 38:
                width = 1454;
                height = 765;
                break;
            case 39:
                width = 1491;
                height = 779;
                break;
            case 40:
                width = 1529;
                height = 791;
                break;
            case 41:
                width = 1567;
                height = 804;
                break;
            case 42:
                width = 1601;
                height = 819;
                break;
            case 43:
                width = 1639;
                height = 832;
                break;
            case 44:
                width = 1678;
                height = 844;
                break;
            case 45:
                width = 1709;
                height = 861;
                break;
            case 46:
                width = 1748;
                height = 873;
                break;
            case 47:
                width = 1788;
                height = 886;
                break;
            case 48:
                width = 1828;
                height = 912;
                break;
            case 49:
                width = 1865;
                height = 925;
                break;
        }
    }

}
