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
import android.widget.SeekBar;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;
import com.example.zuoyun.remix_excel.tools.BitmapToJpg;

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
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDE extends BaseFragment {
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
    @BindView(R.id.sb1)
    SeekBar sb1;
    @BindView(R.id.sbrotate1)
    SeekBar sbrotate1;
    @BindView(R.id.sb2)
    SeekBar sb2;
    @BindView(R.id.sbrotate2)
    SeekBar sbrotate2;

    float scaleX=1.0f, scaleY = 1.0f;
    int num;
    String strPlus = "";

    @Override
    public int getLayout() {
        return R.layout.fragment_de;
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
            public void listen(int message,String sampleurl) {
                if(message==0){
                    iv_leftup.setImageDrawable(null);
                    iv_leftdown.setImageDrawable(null);
                    iv_rightup.setImageDrawable(null);
                    iv_rightdown.setImageDrawable(null);
                    Log.e("fragment_de_old", "message0");
                }
                else if (message == 1) {
                    Log.e("fragment_de_old", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_leftup.setImageBitmap(MainActivity.instance.bitmapLeft);
//                    Glide.with(context).load(sampleurl).into(iv_sample1);
                    checkremix();
                }
                else if(message==2){
                    Log.e("fragment_de_old", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_rightup.setImageBitmap(MainActivity.instance.bitmapRight);
//                    Glide.with(context).load(sampleurl).into(iv_sample2);
                    checkremix();
                }
                else if (message==3){
                    bt_remix.setClickable(false);
                }
                else if (message == 10) {
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

    public void remixx(){
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(34);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(40);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 500, 50);
        String time = MainActivity.instance.orderDate_Print;

        //
        Bitmap bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 1481, 510, true);//1481x516
        Bitmap bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 1481, 506, true);//1481x516

        Bitmap bitmapCombine = Bitmap.createBitmap(1525+59, 2828+59, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine = new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        Matrix matrixCombine = new Matrix();

        //RR
        Bitmap bitmapDBRight = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.de41right);
        Bitmap bitmapRR = Bitmap.createBitmap(1525, 652, Bitmap.Config.ARGB_8888);
        Canvas canvasRR = new Canvas(bitmapRR);
        canvasRR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRR.drawBitmap(bitmapRight, 44, 0, null);
        canvasRR.drawBitmap(bitmapDBRight,0,0,null);

        canvasRR.drawRect(1020, 520, 1450, 570, rectPaint);
        canvasRR.drawText(orderItems.get(currentID).newCode + " 右外", 1020, 565, paintRed);
        canvasRR.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + (orderItems.get(currentID).sku.equals("FX") ? "(新)" : ""), 1360, 565, paint);
        canvasRR.drawText(orderItems.get(currentID).order_number, 1200, 610, paint);
        canvasRR.drawRect(100, 520, 300, 570, rectPaint);
        canvasRR.drawText(time, 100, 565, paint);

        canvasCombine.drawBitmap(bitmapRR, 0, 0, null);
        bitmapRR.recycle();

        //RL
        Bitmap bitmapDBLeft = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.de41left);
        Bitmap bitmapRL = Bitmap.createBitmap(1525, 652, Bitmap.Config.ARGB_8888);
        Canvas canvasRL = new Canvas(bitmapRL);
        canvasRL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRL.drawBitmap(bitmapLeft, 0, 0, null);
        canvasRL.drawBitmap(bitmapDBLeft,0,0,null);

        canvasRL.drawRect(70,520,450,570,rectPaint);
        canvasRL.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color + (orderItems.get(currentID).sku.equals("FX") ? "(新)" : ""),70,565,paint);
        canvasRL.drawText("右内 " + orderItems.get(currentID).newCode, 230, 565, paintRed);
        canvasRL.drawText(orderItems.get(currentID).order_number,110,610,paint);
        canvasRL.drawRect(1100, 520, 1300, 570, rectPaint);
        canvasRL.drawText(time, 1100, 565, paint);

        matrixCombine.reset();
        matrixCombine.postRotate(180);
        matrixCombine.postTranslate(1525, 652*2 + 80);
        canvasCombine.drawBitmap(bitmapRL, matrixCombine, null);
        bitmapRL.recycle();

        //LR
        Bitmap bitmapLR = Bitmap.createBitmap(1525, 652, Bitmap.Config.ARGB_8888);
        Canvas canvasLR = new Canvas(bitmapLR);
        canvasLR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLR.drawBitmap(bitmapRight, 44, 0, null);
        canvasLR.drawBitmap(bitmapDBRight,0,0,null);

        canvasLR.drawRect(1020, 520, 1450, 570, rectPaint);
        canvasLR.drawText(orderItems.get(currentID).newCode + " 左内", 1020, 565, paintRed);
        canvasLR.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + (orderItems.get(currentID).sku.equals("FX") ? "(新)" : ""), 1360, 565, paint);
        canvasLR.drawText(orderItems.get(currentID).order_number, 1200, 610, paint);
        canvasLR.drawRect(100, 520, 300, 570, rectPaint);
        canvasLR.drawText(time, 100, 565, paint);

        matrixCombine.reset();
        matrixCombine.postTranslate(0, 652 * 2 + 140);
        canvasCombine.drawBitmap(bitmapLR, matrixCombine, null);
        bitmapLR.recycle();
        bitmapDBRight.recycle();

        //LL
        Bitmap bitmapLL = Bitmap.createBitmap(1525, 652, Bitmap.Config.ARGB_8888);
        Canvas canvasLL = new Canvas(bitmapLL);
        canvasLL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLL.drawBitmap(bitmapLeft, 0, 0, null);
        canvasLL.drawBitmap(bitmapDBLeft,0,0,null);

        canvasLL.drawRect(70, 520, 450, 570, rectPaint);
        canvasLL.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + (orderItems.get(currentID).sku.equals("FX") ? "(新)" : ""), 70, 565, paint);
        canvasLL.drawText("左外 " + orderItems.get(currentID).newCode, 230, 565, paintRed);
        canvasLL.drawText(orderItems.get(currentID).order_number, 110, 610, paint);
        canvasLL.drawRect(1100, 520, 1300, 570, rectPaint);
        canvasLL.drawText(time, 1100, 565, paint);

        matrixCombine.reset();
        matrixCombine.postRotate(180);
        matrixCombine.postTranslate(1525, 652 * 4 + 220);
        canvasCombine.drawBitmap(bitmapLL, matrixCombine, null);
        bitmapLL.recycle();
        bitmapDBLeft.recycle();

        try {
            setScale(orderItems.get(currentID).size);
            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) ((1525 + 59) * scaleX + 20), (int) ((2828 + 59) * scaleY - 45), true);
            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku + orderItems.get(currentID).size : "";
            String nameCombine = noNewCode + orderItems.get(currentID).sku + orderItems.get(currentID).newCode + orderItems.get(currentID).color + (orderItems.get(currentID).sku.equals("FX") ? "(新)" : "") + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapPrint, fileSave, 150);

            //释放bitmap
            bitmapCombine.recycle();
            bitmapPrint.recycle();

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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku+orderItems.get(currentID).size+printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku+orderItems.get(currentID).size+printColor);
            sheet.addCell(label1);
            Number number2 = new Number(2, currentID+1, orderItems.get(currentID).num);
            sheet.addCell(number2);
            Label label3 = new Label(3, currentID+1, "小左");
            sheet.addCell(label3);
            Label label4 = new Label(4, currentID + 1, MainActivity.instance.orderDate_Excel);
            sheet.addCell(label4);
            Label label6 = new Label(6, currentID+1, "平台大货");
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
    void setScale(int size){
        switch (size) {
            case 36:
                scaleX = 0.881f;
                scaleY = 0.917f;
                break;
            case 37:
                scaleX = 0.904f;
                scaleY = 0.933f;
                break;
            case 38:
                scaleX = 0.929f;
                scaleY = 0.95f;
                break;
            case 39:
                scaleX = 0.953f;
                scaleY = 0.964f;
                break;
            case 40:
                scaleX = 0.976f;
                scaleY = 0.983f;
                break;
            case 41:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
            case 42:
                scaleX = 1.023f;
                scaleY = 1.019f;
                break;
            case 43:
                scaleX = 1.048f;
                scaleY = 1.03f;
                break;
            case 44:
                scaleX = 1.072f;
                scaleY = 1.045f;
                break;
            case 45:
                scaleX = 1.095f;
                scaleY = 1.064f;
                break;
            case 46:
                scaleX = 1.112f;
                scaleY = 1.078f;
                break;
        }
    }

}
