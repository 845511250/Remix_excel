package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
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

import static com.example.zuoyun.remix_excel.activity.MainActivity.getLastNewCode;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDY extends BaseFragment {
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

    int sideWidth, sideHeight, mainWidth, mainHeight, tongueWidth, tongueHeight;
    int part1LX,part1LY,part2LX,part2LY,part3LX,part3LY,part4LX, part4LY;
    int part1RX,part1RY,part2RX,part2RY,part3RX,part3RY,part4RX, part4RY;
    int num;
    String strPlus = "";

    Paint rectPaint, paint, paintRed, paintBlue,paintRectBlack;
    String time;

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

        rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        paintRectBlack = new Paint();
        paintRectBlack.setColor(0xff000000);
        paintRectBlack.setStyle(Paint.Style.FILL);

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(26);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(26);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(26);
        paintBlue.setAntiAlias(true);

        time = MainActivity.instance.orderDate_Print;


        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_pillow.setImageDrawable(null);
                    Log.e("fragment2", "message0");
                } else if (message == 1) {
                    Log.e("fragment2", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapLeft);
                    checkremix();
                } else if (message == 2) {
                    Log.e("fragment2", "message2");
                    bt_remix.setClickable(true);
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


    void drawTextRotate1(Canvas canvas, int degree, int left, int bottom, String LR) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 32, left + 560, bottom, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode, left + 20, bottom - 2, paintRed);
        canvas.drawText(time + " " + LR + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + orderItems.get(currentID).sku, left + 250, bottom - 2, paint);

        canvas.drawRect(left + 280, bottom + 1, left + 560, bottom + 33, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, left + 280, bottom + 31, paintBlue);

        canvas.restore();
    }
    void drawTextRotate3(Canvas canvas, int degree, int left, int bottom, String LR) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 32, left + 560, bottom, rectPaint);
        canvas.drawText(LR + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + orderItems.get(currentID).sku + " " + time, left + 20, bottom - 2, paint);
        canvas.drawText(orderItems.get(currentID).newCode + "", left + 350, bottom - 2, paintRed);

        canvas.drawRect(left, bottom + 1, left + 270, bottom + 33, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, left + 20, bottom + 31, paintBlue);

        canvas.restore();
    }

    public void remixx(){
        setScale(orderItems.get(currentID).size);
        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 500, 40);

        Bitmap bitmapCombine = Bitmap.createBitmap(5146, 2779, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine = new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 2520, 2450, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 2520, 2450, true);
        //part1
        Bitmap bitmapDBPart1 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part1_dy);

        Bitmap bitmapPart1L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 11, 0, 1242, 1231);
        Canvas canvasPart1L = new Canvas(bitmapPart1L);
        canvasPart1L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart1L.drawBitmap(bitmapDBPart1, 0, 0, null);
        drawTextRotate1(canvasPart1L, 14, 10, 1030, "左");

        bitmapPart1L = Bitmap.createScaledBitmap(bitmapPart1L, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapPart1L, part1LX, part1LY, null);
        bitmapPart1L.recycle();

        Bitmap bitmapPart1R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 11, 0, 1242, 1231);
        Canvas canvasPart1R = new Canvas(bitmapPart1R);
        canvasPart1R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart1R.drawBitmap(bitmapDBPart1, 0, 0, null);
        drawTextRotate1(canvasPart1R, 14, 10, 1030, "右");

        bitmapPart1R = Bitmap.createScaledBitmap(bitmapPart1R, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapPart1R, part1RX, part1RY, null);
        bitmapPart1R.recycle();
        bitmapDBPart1.recycle();

        //part2
        Bitmap bitmapDBPart2 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part2_dy);
        Bitmap bitmapPart2L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 506, 1240, 1499, 1210);

        Bitmap bitmapPart2LFUCK = Bitmap.createBitmap(1499, 1221, Bitmap.Config.ARGB_8888);
        Canvas canvasPart2L = new Canvas(bitmapPart2LFUCK);
        canvasPart2L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart2L.drawBitmap(bitmapPart2L, 0, 0, null);
        bitmapPart2L.recycle();
        canvasPart2L.drawBitmap(bitmapDBPart2, 0, 0, null);
        canvasPart2L.save();
        canvasPart2L.rotate(74.8f, 15, 73);
        canvasPart2L.drawRect(15, 33, 515, 73, rectPaint);
        //canvasPart2L.drawBitmap(bitmapBarCode, 15, 33, null);
        canvasPart2L.drawText("      " + orderItems.get(currentID).newCode + "      验片码" + orderItems.get(currentID).codeE, 20, 70, paintRed);
        canvasPart2L.restore();
        canvasPart2L.save();
        canvasPart2L.rotate(-75.2f, 1331, 631);
        canvasPart2L.drawRect(1331, 596, 1951, 631, rectPaint);
        canvasPart2L.drawText(time + " " + orderItems.get(currentID).order_number + " 左" + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + orderItems.get(currentID).sku, 1331, 627, paint);
        canvasPart2L.restore();

        bitmapPart2LFUCK = Bitmap.createScaledBitmap(bitmapPart2LFUCK, mainWidth, mainHeight, true);
        canvasCombine.drawBitmap(bitmapPart2LFUCK, part2LX, part2LY, null);
        bitmapPart2LFUCK.recycle();

        Bitmap bitmapPart2R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 506, 1240, 1499, 1210);
        Bitmap bitmapPart2RFUCK = Bitmap.createBitmap(1499, 1221, Bitmap.Config.ARGB_8888);
        Canvas canvasPart2R = new Canvas(bitmapPart2RFUCK);
        canvasPart2R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart2R.drawBitmap(bitmapPart2R, 0, 0, null);
        bitmapPart2R.recycle();
        canvasPart2R.drawBitmap(bitmapDBPart2, 0, 0, null);
        canvasPart2R.save();
        canvasPart2R.rotate(74.8f, 15, 73);
        canvasPart2R.drawRect(15, 33, 515, 73, rectPaint);
        //canvasPart2R.drawBitmap(bitmapBarCode, 15, 33, null);
        canvasPart2R.drawText("      "+orderItems.get(currentID).newCode + "      验片码" + orderItems.get(currentID).codeE, 20, 70, paintRed);
        canvasPart2R.restore();
        canvasPart2R.save();
        canvasPart2R.rotate(-75.2f, 1331, 631);
        canvasPart2R.drawRect(1331, 596, 1951, 631, rectPaint);
        canvasPart2R.drawText(time + " " + orderItems.get(currentID).order_number + " 右" + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + orderItems.get(currentID).sku, 1331, 627, paint);
        canvasPart2R.restore();

        bitmapPart2RFUCK = Bitmap.createScaledBitmap(bitmapPart2RFUCK, mainWidth, mainHeight, true);
        canvasCombine.drawBitmap(bitmapPart2RFUCK, part2RX, part2RY, null);
        bitmapPart2RFUCK.recycle();
        bitmapDBPart2.recycle();

        //part3
        Bitmap bitmapDBPart3 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part3_dy);

        Bitmap bitmapPart3L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1260, 0, 1242, 1231);
        Canvas canvasPart3L = new Canvas(bitmapPart3L);
        canvasPart3L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart3L.drawBitmap(bitmapDBPart3, 0, 0, null);
        drawTextRotate3(canvasPart3L, -14, 655, 1170, "左");

        bitmapPart3L = Bitmap.createScaledBitmap(bitmapPart3L, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapPart3L, part3LX, part3LY, null);
        bitmapPart3L.recycle();

        Bitmap bitmapPart3R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1260, 0, 1242, 1231);
        Canvas canvasPart3R = new Canvas(bitmapPart3R);
        canvasPart3R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart3R.drawBitmap(bitmapDBPart3, 0, 0, null);
        drawTextRotate3(canvasPart3R, -14, 655, 1170, "右");

        bitmapPart3R = Bitmap.createScaledBitmap(bitmapPart3R, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapPart3R, part3RX, part3RY, null);
        bitmapPart3R.recycle();
        bitmapDBPart3.recycle();

        //part4
        paint.setTextSize(26);
        paintRed.setTextSize(26);
        Bitmap bitmapDBPart4 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part4);

        Bitmap bitmapPart4L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 911, 0, 699, 1092);
        Canvas canvasPart4L = new Canvas(bitmapPart4L);
        canvasPart4L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart4L.drawBitmap(bitmapDBPart4, 0, 0, null);
        canvasPart4L.save();
        canvasPart4L.rotate(-10.7f, 144, 1080);
        canvasPart4L.drawRect(144, 1054, 384, 1080, rectPaint);
        canvasPart4L.drawText(time + " " + orderItems.get(currentID).order_number, 144, 1078, paint);
        canvasPart4L.restore();
        canvasPart4L.save();
        canvasPart4L.rotate(10, 366, 1045);
        canvasPart4L.drawRect(376, 1019, 566, 1045, rectPaint);
        canvasPart4L.drawText("左" + orderItems.get(currentID).size + "码", 376, 1043, paint);
        canvasPart4L.drawText("流:" + getLastNewCode(orderItems.get(currentID).newCode), 476, 1043, paintRed);
        canvasPart4L.restore();

        bitmapPart4L = Bitmap.createScaledBitmap(bitmapPart4L, tongueWidth, tongueHeight, true);
        canvasCombine.drawBitmap(bitmapPart4L, part4LX, part4LY, null);
        bitmapPart4L.recycle();

        Bitmap bitmapPart4R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 911, 0, 699, 1092);
        Canvas canvasPart4R = new Canvas(bitmapPart4R);
        canvasPart4R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart4R.drawBitmap(bitmapDBPart4, 0, 0, null);
        canvasPart4R.save();
        canvasPart4R.rotate(-10.7f, 144, 1080);
        canvasPart4R.drawRect(144, 1054, 384, 1080, rectPaint);
        canvasPart4R.drawText(time + " " + orderItems.get(currentID).order_number, 144, 1078, paint);
        canvasPart4R.restore();
        canvasPart4R.save();
        canvasPart4R.rotate(10, 366, 1045);
        canvasPart4R.drawRect(376, 1019, 566, 1045, rectPaint);
        canvasPart4R.drawText("右" + orderItems.get(currentID).size + "码", 376, 1043, paint);
        canvasPart4R.drawText("流:" + getLastNewCode(orderItems.get(currentID).newCode), 476, 1043, paintRed);
        canvasPart4R.restore();

        bitmapPart4R = Bitmap.createScaledBitmap(bitmapPart4R, tongueWidth, tongueHeight, true);
        canvasCombine.drawBitmap(bitmapPart4R, part4RX, part4RY, null);
        bitmapPart4R.recycle();
        bitmapDBPart4.recycle();

        //
        canvasCombine.drawRect(2200, 0, 2300, 1, paintRectBlack);
        canvasCombine.drawRect(2200, 2778, 2300, 2779, paintRectBlack);

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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku+ orderItems.get(currentID).size + printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku+ orderItems.get(currentID).size + printColor);
            sheet.addCell(label1);
            int num=orderItems.get(currentID).num;
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

    void setScale(int size){
        switch (size) {
            case 35:
                sideWidth = 1057;
                sideHeight = 1109;
                mainWidth = 1371;
                mainHeight = 1062;
                tongueWidth = 636;
                tongueHeight = 943;

                part1LX = 171;
                part1LY = 114;
                part2LX = 1515;
                part2LY = 148;
                part3LX = 3173;
                part3LY = 113;
                part4LX = 4455;
                part4LY = 124;

                part1RX = 171;
                part1RY = 1510;
                part2RX = 1515;
                part2RY = 1544;
                part3RX = 3173;
                part3RY = 1509;
                part4RX = 4455;
                part4RY = 1520;
                break;
            case 36:
                sideWidth = 1086;
                sideHeight = 1128;
                mainWidth = 1392;
                mainHeight = 1092;
                tongueWidth = 636;
                tongueHeight = 943;

                part1LX = 156;
                part1LY = 101;
                part2LX = 1504;
                part2LY = 135;
                part3LX = 3159;
                part3LY = 102;
                part4LX = 4456;
                part4LY = 125;

                part1RX = 156;
                part1RY = 1496;
                part2RX = 1504;
                part2RY = 1530;
                part3RX = 3159;
                part3RY = 1497;
                part4RX = 4456;
                part4RY = 1520;
                break;
            case 37:
                sideWidth = 1128;
                sideHeight = 1147;
                mainWidth = 1414;
                mainHeight = 1122;
                tongueWidth = 656;
                tongueHeight = 991;

                part1LX = 136;
                part1LY = 95;
                part2LX = 1494;
                part2LY = 119;
                part3LX = 3138;
                part3LY = 95;
                part4LX = 4445;
                part4LY = 101;

                part1RX = 136;
                part1RY = 1489;
                part2RX = 1494;
                part2RY = 1513;
                part3RX = 3138;
                part3RY = 1496;
                part4RX = 4445;
                part4RY = 1495;
                break;
            case 38:
                sideWidth = 1156;
                sideHeight = 1166;
                mainWidth = 1434;
                mainHeight = 1146;
                tongueWidth = 656;
                tongueHeight = 991;

                part1LX = 121;
                part1LY = 84;
                part2LX = 1484;
                part2LY = 107;
                part3LX = 3124;
                part3LY = 85;
                part4LX = 4446;
                part4LY = 101;

                part1RX = 121;
                part1RY = 1478;
                part2RX = 1484;
                part2RY = 1501;
                part3RX = 3124;
                part3RY = 1479;
                part4RX = 4446;
                part4RY = 1495;
                break;
            case 39:
                sideWidth = 1178;
                sideHeight = 1191;
                mainWidth = 1455;
                mainHeight = 1177;
                tongueWidth = 676;
                tongueHeight = 1041;

                part1LX = 110;
                part1LY = 71;
                part2LX = 1473;
                part2LY = 94;
                part3LX = 3111;
                part3LY = 73;
                part4LX = 4436;
                part4LY = 73;

                part1RX = 110;
                part1RY = 1465;
                part2RX = 1473;
                part2RY = 1488;
                part3RX = 3111;
                part3RY = 1467;
                part4RX = 4436;
                part4RY = 1467;
                break;
            case 40:
                sideWidth = 1211;
                sideHeight = 1213;
                mainWidth = 1477;
                mainHeight = 1205;
                tongueWidth = 676;
                tongueHeight = 1041;

                part1LX = 94;
                part1LY = 62;
                part2LX = 1462;
                part2LY = 80;
                part3LX = 3096;
                part3LY = 62;
                part4LX = 4436;
                part4LY = 76;

                part1RX = 94;
                part1RY = 1456;
                part2RX = 1462;
                part2RY = 1474;
                part3RX = 3096;
                part3RY = 1456;
                part4RX = 4436;
                part4RY = 1467;
                break;
            case 41:
                sideWidth = 1242;
                sideHeight = 1231;
                mainWidth = 1499;
                mainHeight = 1221;
                tongueWidth = 699;
                tongueHeight = 1093;

                part1LX = 79;
                part1LY = 54;
                part2LX = 1451;
                part2LY = 71;
                part3LX = 3081;
                part3LY = 53;
                part4LX = 4425;
                part4LY = 49;

                part1RX = 79;
                part1RY = 1449;
                part2RX = 1451;
                part2RY = 1466;
                part3RX = 3081;
                part3RY = 1448;
                part4RX = 4425;
                part4RY = 1444;
                break;
            case 42:
                sideWidth = 1268;
                sideHeight = 1256;
                mainWidth = 1520;
                mainHeight = 1249;
                tongueWidth = 699;
                tongueHeight = 1093;

                part1LX = 66;
                part1LY = 41;
                part2LX = 1441;
                part2LY = 56;
                part3LX = 3068;
                part3LY = 41;
                part4LX = 4425;
                part4LY = 49;

                part1RX = 66;
                part1RY = 1436;
                part2RX = 1441;
                part2RY = 1451;
                part3RX = 3068;
                part3RY = 1436;
                part4RX = 4425;
                part4RY = 1444;
                break;
            case 43:
                sideWidth = 1297;
                sideHeight = 1282;
                mainWidth = 1540;
                mainHeight = 1277;
                tongueWidth = 721;
                tongueHeight = 1142;

                part1LX = 51;
                part1LY = 28;
                part2LX = 1431;
                part2LY = 42;
                part3LX = 3053;
                part3LY = 28;
                part4LX = 4414;
                part4LY = 25;

                part1RX = 51;
                part1RY = 1421;
                part2RX = 1431;
                part2RY = 1435;
                part3RX = 3053;
                part3RY = 1421;
                part4RX = 4414;
                part4RY = 1418;
                break;
            case 44:
                sideWidth = 1323;
                sideHeight = 1303;
                mainWidth = 1562;
                mainHeight = 1303;
                tongueWidth = 721;
                tongueHeight = 1142;

                part1LX = 38;
                part1LY = 17;
                part2LX = 1420;
                part2LY = 28;
                part3LX = 3039;
                part3LY = 17;
                part4LX = 4413;
                part4LY = 25;

                part1RX = 38;
                part1RY = 1410;
                part2RX = 1420;
                part2RY = 1421;
                part3RX = 3039;
                part3RY = 1410;
                part4RX = 4413;
                part4RY = 1418;
                break;
            case 45:
                sideWidth = 1373;
                sideHeight = 1324;
                mainWidth = 1583;
                mainHeight = 1337;
                tongueWidth = 744;
                tongueHeight = 1193;

                part1LX = 13;
                part1LY = 7;
                part2LX = 1409;
                part2LY = 11;
                part3LX = 3015;
                part3LY = 7;
                part4LX = 4402;
                part4LY = 0;

                part1RX = 13;
                part1RY = 1398;
                part2RX = 1409;
                part2RY = 1402;
                part3RX = 3015;
                part3RY = 1398;
                part4RX = 4402;
                part4RY = 1391;
                break;
            case 46:
                sideWidth = 1400;
                sideHeight = 1338;
                mainWidth = 1602;
                mainHeight = 1360;
                tongueWidth = 744;
                tongueHeight = 1193;

                part1LX = 0;
                part1LY = 0;
                part2LX = 1400;
                part2LY = 0;
                part3LX = 3002;
                part3LY = 0;
                part4LX = 4402;
                part4LY = 0;

                part1RX = 0;
                part1RY = 1391;
                part2RX = 1400;
                part2RY = 1390;
                part3RX = 3002;
                part3RY = 1390;
                part4RX = 4402;
                part4RY = 1390;
                break;
        }
    }
}
