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
 * Created by zuoyun on 2016/10/6.
 */

public class FragmentFF extends BaseFragment {
    Context context;
    //    String sdCardPath = "/mnt/asec/share";
    String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    int currentID;
    String childPath;

    @BindView(R.id.iv_fg2_leftup)
    ImageView iv_leftup;
    @BindView(R.id.iv_fg2_rightup)
    ImageView iv_rightup;
    @BindView(R.id.bt_remix)
    Button bt_remix;

    int part1Width, part1Height, part2Width, part2Height, part3Width, part3Height;
    int part1LX,part1LY,part2LX,part2LY,part3LX,part3LY;
    int part1RX,part1RY,part2RX,part2RY,part3RX,part3RY;
    int num;
    String strPlus = "";
    int intPlus = 1;
    Paint paint,paintRed,paintBlue, rectPaint;
    String time = MainActivity.instance.orderDate_Print;

    @Override
    public int getLayout() {
        return R.layout.fragment_dq;
    }

    @Override
    public void initData(View view) {
        context = getContext();
        ButterKnife.bind(this, view);
        orderItems=MainActivity.instance.orderItems;
        currentID = MainActivity.instance.currentID;
        childPath = MainActivity.instance.childPath;

        //paint
        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(26);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(26);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff000000);
        paintBlue.setTextSize(26);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message,String sampleurl) {
                if(message==0){
                    iv_leftup.setImageDrawable(null);
                    iv_rightup.setImageDrawable(null);
                    Log.e("fragment_dq", "message0");
                }
                else if (message == 1) {
                    Log.e("fragment_dq", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_leftup.setImageBitmap(MainActivity.instance.bitmapLeft);
//                    Glide.with(context).load(sampleurl).into(iv_sample1);
                    checkremix();
                }
                else if(message==2){
                    Log.e("fragment_dq", "message2");
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

    void drawText1(Canvas canvas, String LR) {
        canvas.save();
        canvas.rotate(45.3f, 195, 830);
        canvas.drawRect(195, 830-26, 195+140, 830, rectPaint);
        canvas.drawText(time, 195, 830-2, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(35.2f, 305, 941);
        canvas.drawRect(305, 941-26, 305+205, 941, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, 305, 941-2, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(-17f, 512, 1055);
        canvas.drawRect(512, 1055-26, 512+250, 1055, rectPaint);
        canvas.drawText(orderItems.get(currentID).size + "码 " + LR + "   " + orderItems.get(currentID).newCode, 512, 1055 - 2, paintRed);
        canvas.restore();
    }

    void drawText2(Canvas canvas, String LR) {
        canvas.save();
        canvas.rotate(-2.9f, 80, 498);
        canvas.drawRect(80, 498 - 26, 80+300, 498, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number + "   " + time, 80, 498-2, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(3.5f, 1173, 488);
        canvas.drawRect(1173, 488 - 26, 1173+260, 488, rectPaint);
        canvas.drawText(orderItems.get(currentID).size + "码 " + LR + "  " + orderItems.get(currentID).newCode, 1173, 488 - 2, paintRed);
        canvas.restore();
    }

    void drawText3(Canvas canvas, String LR) {
        canvas.save();
        canvas.rotate(68f, 12, 408);
        canvas.drawRect(12, 408 - 26, 12 + 500, 408, rectPaint);
        canvas.drawText(time + "   " + orderItems.get(currentID).order_number + "     " + orderItems.get(currentID).size + "码 " + LR, 12, 408 - 2, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(63.3f, 214, 903);
        canvas.drawRect(214, 903-26, 214+160, 903, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode, 214, 903 - 2, paintRed);
        canvas.restore();
    }
    public void remixx(){
        //
        setScale(orderItems.get(currentID).size);

        Bitmap bitmapDB1 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ff_part1);
        Bitmap bitmapDB2 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ff_part2);
        Bitmap bitmapDB3 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ff_part3);

        //left
        Bitmap bitmapLeft1 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 104, 70, 2096, 1069);
        Bitmap bitmapLeft2 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 420, 1204, 1461, 511);
        Bitmap bitmapLeft3 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 389, 1756, 1528, 1398);

        Canvas canvasLeft1 = new Canvas(bitmapLeft1);
        canvasLeft1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft1.drawBitmap(bitmapDB1, 0, 0, null);

        Canvas canvasLeft2 = new Canvas(bitmapLeft2);
        canvasLeft2.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft2.drawBitmap(bitmapDB2, 0, 0, null);

        Canvas canvasLeft3 = new Canvas(bitmapLeft3);
        canvasLeft3.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft3.drawBitmap(bitmapDB3, 0, 0, null);

        //right
        Bitmap bitmapRight1 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 104, 70, 2096, 1069);
        Bitmap bitmapRight2 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 420, 1204, 1461, 511);
        Bitmap bitmapRight3 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 389, 1756, 1528, 1398);

        Canvas canvasRight1 = new Canvas(bitmapRight1);
        canvasRight1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight1.drawBitmap(bitmapDB1, 0, 0, null);

        Canvas canvasRight2 = new Canvas(bitmapRight2);
        canvasRight2.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight2.drawBitmap(bitmapDB2, 0, 0, null);

        Canvas canvasRight3 = new Canvas(bitmapRight3);
        canvasRight3.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight3.drawBitmap(bitmapDB3, 0, 0, null);

        //drawText
        drawText1(canvasLeft1, "左");
        drawText1(canvasRight1, "右");
        drawText2(canvasLeft2, "左");
        drawText2(canvasRight2, "右");
        drawText3(canvasLeft3, "左");
        drawText3(canvasRight3, "右");

        bitmapLeft1 = Bitmap.createScaledBitmap(bitmapLeft1, part1Width, part1Height, true);
        bitmapLeft2 = Bitmap.createScaledBitmap(bitmapLeft2, part2Width, part2Height, true);
        bitmapLeft3 = Bitmap.createScaledBitmap(bitmapLeft3, part3Width, part3Height, true);
        bitmapRight1 = Bitmap.createScaledBitmap(bitmapRight1, part1Width, part1Height, true);
        bitmapRight2 = Bitmap.createScaledBitmap(bitmapRight2, part2Width, part2Height, true);
        bitmapRight3 = Bitmap.createScaledBitmap(bitmapRight3, part3Width, part3Height, true);

        try {
            //旋转90度matrix
            Matrix matrix90 = new Matrix();
            matrix90.postRotate(90);
            matrix90.postTranslate(part1Height, 0);
            bitmapLeft1 = Bitmap.createBitmap(bitmapLeft1, 0, 0, part1Width, part1Height, matrix90, true);
            bitmapRight1 = Bitmap.createBitmap(bitmapRight1, 0, 0, part1Width, part1Height, matrix90, true);

            Bitmap bitmapCombine;
            bitmapCombine = Bitmap.createBitmap(5762, 2515, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.drawColor(0xffffffff);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

            canvasCombine.drawBitmap(bitmapLeft1, part1LX, part1LY, null);
            canvasCombine.drawBitmap(bitmapRight1, part1RX, part1RY, null);
            canvasCombine.drawBitmap(bitmapLeft2, part2LX, part2LY, null);
            canvasCombine.drawBitmap(bitmapRight2, part2RX, part2RY, null);
            canvasCombine.drawBitmap(bitmapLeft3, part3LX, part3LY, null);
            canvasCombine.drawBitmap(bitmapRight3, part3RX, part3RY, null);

            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).size + "" : "";
            String nameCombine = orderItems.get(currentID).sku + orderItems.get(currentID).newCode + orderItems.get(currentID).color + orderItems.get(currentID).order_number + strPlus + ".jpg";

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
            bitmapDB1.recycle();
            bitmapDB2.recycle();
            bitmapDB3.recycle();
            bitmapLeft1.recycle();
            bitmapLeft2.recycle();
            bitmapLeft3.recycle();
            bitmapRight1.recycle();
            bitmapRight2.recycle();
            bitmapRight3.recycle();
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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku+orderItems.get(currentID).size+printColor);
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
            Log.e("aaa", e.toString());
        }
        if (num == 1) {
            MainActivity.instance.bitmapLeft.recycle();
            MainActivity.instance.bitmapRight.recycle();
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
                part1Width = 2042;
                part1Height = 988;
                part2Width = 1365;
                part2Height = 543;
                part3Width = 1511;
                part3Height = 1245;

                part1LX = 104;
                part1LY = 207;
                part2LX = 2495;
                part2LY = 52;
                part3LX = 2389;
                part3LY = 1069;

                part1RX = 1267;
                part1RY = 207;
                part2RX = 4227;
                part2RY = 52;
                part3RX = 4154;
                part3RY = 1069;
                break;
            case 37:
                part1Width = 2094;
                part1Height = 1004;
                part2Width = 1399;
                part2Height = 551;
                part3Width = 1536;
                part3Height = 1277;

                part1LX = 92;
                part1LY = 180;
                part2LX = 2479;
                part2LY = 47;
                part3LX = 2378;
                part3LY = 1037;

                part1RX = 1255;
                part1RY = 181;
                part2RX = 4210;
                part2RY = 47;
                part3RX = 4141;
                part3RY = 1037;
                break;
            case 38:
                part1Width = 2144;
                part1Height = 1024;
                part2Width = 1435;
                part2Height = 558;
                part3Width = 1561;
                part3Height = 1309;

                part1LX = 79;
                part1LY = 156;
                part2LX = 2460;
                part2LY = 40;
                part3LX = 2365;
                part3LY = 1005;

                part1RX = 1242;
                part1RY = 156;
                part2RX = 4192;
                part2RY = 40;
                part3RX = 4128;
                part3RY = 1005;
                break;
            case 39:
                part1Width = 2197;
                part1Height = 1038;
                part2Width = 1469;
                part2Height = 566;
                part3Width = 1584;
                part3Height = 1341;

                part1LX = 65;
                part1LY = 129;
                part2LX = 2443;
                part2LY = 34;
                part3LX = 2353;
                part3LY = 973;

                part1RX = 1228;
                part1RY = 129;
                part2RX = 4175;
                part2RY = 34;
                part3RX = 4116;
                part3RY = 973;
                break;
            case 40:
                part1Width = 2247;
                part1Height = 1059;
                part2Width = 1504;
                part2Height = 574;
                part3Width = 1609;
                part3Height = 1371;

                part1LX = 51;
                part1LY = 104;
                part2LX = 2425;
                part2LY = 27;
                part3LX = 2341;
                part3LY = 943;

                part1RX = 1214;
                part1RY = 104;
                part2RX = 4157;
                part2RY = 26;
                part3RX = 4104;
                part3RY = 943;
                break;
            case 41:
                part1Width = 2299;
                part1Height = 1079;
                part2Width = 1540;
                part2Height = 580;
                part3Width = 1634;
                part3Height = 1404;

                part1LX = 37;
                part1LY = 78;
                part2LX = 2408;
                part2LY = 24;
                part3LX = 2329;
                part3LY = 910;

                part1RX = 1201;
                part1RY = 78;
                part2RX = 4139;
                part2RY = 21;
                part3RX = 4091;
                part3RY = 910;
                break;
            case 42:
                part1Width = 2351;
                part1Height = 1099;
                part2Width = 1575;
                part2Height = 588;
                part3Width = 1658;
                part3Height = 1436;

                part1LX = 25;
                part1LY = 52;
                part2LX = 2389;
                part2LY = 16;
                part3LX = 2316;
                part3LY = 878;

                part1RX = 1186;
                part1RY = 52;
                part2RX = 4121;
                part2RY = 13;
                part3RX = 4079;
                part3RY = 878;
                break;
            case 43:
                part1Width = 2403;
                part1Height = 1112;
                part2Width = 1609;
                part2Height = 595;
                part3Width = 1683;
                part3Height = 1468;

                part1LX = 12;
                part1LY = 26;
                part2LX = 2373;
                part2LY = 9;
                part3LX = 2304;
                part3LY = 846;

                part1RX = 1173;
                part1RY = 26;
                part2RX = 4104;
                part2RY = 9;
                part3RX = 4067;
                part3RY = 846;
                break;
            case 44:
                part1Width = 2455;
                part1Height = 1129;
                part2Width = 1644;
                part2Height = 604;
                part3Width = 1707;
                part3Height = 1499;

                part1LX = 0;
                part1LY = 0;
                part2LX = 2355;
                part2LY = 0;
                part3LX = 2292;
                part3LY = 815;

                part1RX = 1161;
                part1RY = 0;
                part2RX = 4087;
                part2RY = 0;
                part3RX = 4055;
                part3RY = 815;
                break;
        }
    }

}
