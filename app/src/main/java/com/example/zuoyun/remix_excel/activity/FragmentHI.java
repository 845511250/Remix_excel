package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentHI extends BaseFragment {
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
    int sideMarginTop;
    int num;
    String strPlus = "";
    int intPlus = 1;

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
                            intPlus += 1;
                        }
                    }
                    strPlus = intPlus == 1 ? "" : "(" + intPlus + ")";
                    remixx();
                    intPlus += 1;
                }
            }
        }.start();

    }

    void drawTextSideR(Canvas canvas, int degree, int left, int bottom, String LR) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 26, left + 500, bottom, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode, left + 20, bottom - 2, paintRed);
        canvas.drawText(time + " " + orderItems.get(currentID).sku + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + " " + LR, left + 250, bottom - 2, paint);

        canvas.drawRect(left + 250, bottom + 1, left + 500, bottom + 33, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, left + 250, bottom + 31, paintBlue);

        canvas.restore();
    }
    void drawTextSideL(Canvas canvas, int degree, int left, int bottom, String LR) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 26, left + 500, bottom, rectPaint);
        canvas.drawText(time + " " + orderItems.get(currentID).sku + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + " " + LR, left + 20, bottom - 2, paint);
        canvas.drawText(orderItems.get(currentID).newCode + "", left + 250, bottom - 2, paintRed);

        canvas.drawRect(left, bottom + 1, left + 250, bottom + 33, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, left + 20, bottom + 31, paintBlue);

        canvas.restore();
    }
    void drawTextMain(Canvas canvasPart2L, String LR){
        canvasPart2L.save();
        canvasPart2L.rotate(75, 22, 56);
        canvasPart2L.drawRect(22, 56 - 26, 22 + 500, 56, rectPaint);
        canvasPart2L.drawText("      " + orderItems.get(currentID).newCode, 22, 56 - 2, paintRed);
        canvasPart2L.restore();
        canvasPart2L.save();
        canvasPart2L.rotate(-75, 1348, 534);
        canvasPart2L.drawRect(1348, 534 - 26, 1348 + 500, 534, rectPaint);
        canvasPart2L.drawText(time + " " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).sku + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + " " + LR, 1348, 534 - 2, paint);
        canvasPart2L.restore();
    }
    void drawTextTongue(Canvas canvasPart4L, String LR) {
        canvasPart4L.save();
        canvasPart4L.rotate(-9.7f, 144, 1050);
        canvasPart4L.drawRect(144, 1050 - 26, 144 + 210, 1050, rectPaint);
        canvasPart4L.drawText(time + " " + LR + orderItems.get(currentID).size, 144, 1050 - 2, paint);
        canvasPart4L.restore();
        canvasPart4L.save();
        canvasPart4L.rotate(9.6f, 364, 1017);
        canvasPart4L.drawRect(364, 1017 - 26, 364 + 190, 1017, rectPaint);
        canvasPart4L.drawText(orderItems.get(currentID).order_number, 370, 1017 - 2, paintBlue);
        canvasPart4L.restore();
    }

    public void remixx(){
        setSize(orderItems.get(currentID).size);

        Bitmap bitmapCombine = Bitmap.createBitmap(5048, 2748, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine = new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        //LR
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hi_r);
        Bitmap bitmapTemp = Bitmap.createBitmap(1091, 1183, Bitmap.Config.ARGB_8888);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        Matrix matrix = new Matrix();
        matrix.postRotate(-8.9f);
        matrix.postTranslate(-174, 91);
        canvasTemp.drawBitmap(MainActivity.instance.bitmapLeft, matrix, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextSideR(canvasTemp, 4, 45, 1105, "左");

        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 4, 4 + sideMarginTop, null);

        //RR
        bitmapTemp = Bitmap.createBitmap(1091, 1183, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix = new Matrix();
        matrix.postRotate(-8.9f);
        matrix.postTranslate(-174, 91);
        canvasTemp.drawBitmap(MainActivity.instance.bitmapRight, matrix, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextSideR(canvasTemp, 4, 45, 1105, "右");

        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 4, 1376 + sideMarginTop, null);

        //LMain
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hi_main);
        bitmapTemp = Bitmap.createBitmap(1496, 1188, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawBitmap(MainActivity.instance.bitmapLeft, -506, -1212, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextMain(canvasTemp, "左");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, mainWidth, mainHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 4244 - mainWidth / 2, 4, null);

        //RMain
        bitmapTemp = Bitmap.createBitmap(1496, 1188, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawBitmap(MainActivity.instance.bitmapRight, -506, -1212, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextMain(canvasTemp, "右");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, mainWidth, mainHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 4244 - mainWidth / 2, 1376, null);

        //LL
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hi_l);
        bitmapTemp = Bitmap.createBitmap(1091, 1183, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix = new Matrix();
        matrix.postTranslate(-1235, 0);
        matrix.postRotate(8.9f);
        matrix.postTranslate(13, -106);
        canvasTemp.drawBitmap(MainActivity.instance.bitmapLeft, matrix, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextSideL(canvasTemp, -4, 538, 1140, "左");

        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 3405 - sideWidth, 4 + sideMarginTop, null);

        //RL
        bitmapTemp = Bitmap.createBitmap(1091, 1183, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix = new Matrix();
        matrix.postTranslate(-1235, 0);
        matrix.postRotate(8.9f);
        matrix.postTranslate(13, -106);
        canvasTemp.drawBitmap(MainActivity.instance.bitmapRight, matrix, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextSideL(canvasTemp, -4, 538, 1140, "右");

        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, sideWidth, sideHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 3405 - sideWidth, 1376 + sideMarginTop, null);

        //LTongue

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hi_tongue);
        bitmapTemp = Bitmap.createBitmap(691, 1057, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawBitmap(MainActivity.instance.bitmapLeft, -906, -5, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextTongue(canvasTemp, "左");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, tongueWidth, tongueHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 1704 - tongueWidth / 2, 4, null);

        //RTongue
        bitmapTemp = Bitmap.createBitmap(691, 1057, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawBitmap(MainActivity.instance.bitmapRight, -906, -5, null);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextTongue(canvasTemp, "右");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, tongueWidth, tongueHeight, true);
        canvasCombine.drawBitmap(bitmapTemp, 1704 - tongueWidth / 2, 1376, null);

        //
        bitmapTemp.recycle();
        bitmapDB.recycle();

        try {
            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String nameCombine = orderItems.get(currentID).sku + orderItems.get(currentID).color + orderItems.get(currentID).size + orderItems.get(currentID).order_number + strPlus + ".jpg";

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

    void setSize(int size){
        switch (size) {
            case 35:
                sideWidth = 1011;
                sideHeight = 1086;
                mainWidth = 1379;
                mainHeight = 1060;
                tongueWidth = 632;
                tongueHeight = 948;
                sideMarginTop = 20;
                break;
            case 36:
                sideWidth = 1039;
                sideHeight = 1104;
                mainWidth = 1399;
                mainHeight = 1090;
                tongueWidth = 632;
                tongueHeight = 948;
                sideMarginTop = 22;
                break;
            case 37:
                sideWidth = 1068;
                sideHeight = 1121;
                mainWidth = 1419;
                mainHeight = 1116;
                tongueWidth = 654;
                tongueHeight = 999;
                sideMarginTop = 20;
                break;
            case 38:
                sideWidth = 1096;
                sideHeight = 1138;
                mainWidth = 1440;
                mainHeight = 1145;
                tongueWidth = 654;
                tongueHeight = 999;
                sideMarginTop = 15;
                break;
            case 39:
                sideWidth = 1124;
                sideHeight = 1156;
                mainWidth = 1461;
                mainHeight = 1175;
                tongueWidth = 676;
                tongueHeight = 1050;
                sideMarginTop = 14;
                break;
            case 40:
                sideWidth = 1153;
                sideHeight = 1173;
                mainWidth = 1482;
                mainHeight = 1205;
                tongueWidth = 676;
                tongueHeight = 1050;
                sideMarginTop = 12;
                break;
            case 41:
                sideWidth = 1181;
                sideHeight = 1190;
                mainWidth = 1503;
                mainHeight = 1235;
                tongueWidth = 698;
                tongueHeight = 1102;
                sideMarginTop = 10;
                break;
            case 42:
                sideWidth = 1209;
                sideHeight = 1208;
                mainWidth = 1524;
                mainHeight = 1264;
                tongueWidth = 698;
                tongueHeight = 1102;
                sideMarginTop = 8;
                break;
            case 43:
                sideWidth = 1238;
                sideHeight = 1225;
                mainWidth = 1545;
                mainHeight = 1291;
                tongueWidth = 720;
                tongueHeight = 1153;
                sideMarginTop = 7;
                break;
            case 44:
                sideWidth = 1267;
                sideHeight = 1243;
                mainWidth = 1566;
                mainHeight = 1318;
                tongueWidth = 720;
                tongueHeight = 1153;
                sideMarginTop = 5;
                break;
            case 45:
                sideWidth = 1295;
                sideHeight = 1260;
                mainWidth = 1587;
                mainHeight = 1345;
                tongueWidth = 742;
                tongueHeight = 1204;
                sideMarginTop = 2;
                break;
            case 46:
                sideWidth = 1324;
                sideHeight = 1277;
                mainWidth = 1608;
                mainHeight = 1372;
                tongueWidth = 742;
                tongueHeight = 1204;
                sideMarginTop = 0;
                break;
        }
    }
}
