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

import static com.example.zuoyun.remix_excel.activity.MainActivity.getLastNewCode;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDYold extends BaseFragment {
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

    float scaleX=1.0f, scaleY = 1.0f;
    int num;
    String strPlus = "";

    Paint rectPaint, paint, paintRed, paintBlue;
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

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(34);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(34);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(34);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
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

    void drawText(Canvas canvas, int left, int bottom) {
        canvas.drawRect(left, bottom - 28, left + 420, bottom, rectPaint);
        canvas.drawText(time + "   " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).sizeStr, left, bottom - 2, paint);
        canvas.drawText((currentID + 1) + "", left + 330, bottom - 2, paintRed);
    }

    void drawTextRotate1(Canvas canvas, int degree, int left, int bottom, String LR) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 32, left + 560, bottom, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode, left + 20, bottom - 2, paintRed);
        canvas.drawText(time + " " + LR + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color, left + 250, bottom - 2, paint);

        canvas.drawRect(left + 290, bottom + 1, left + 560, bottom + 33, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, left + 290, bottom + 31, paintBlue);

        canvas.restore();
    }
    void drawTextRotate3(Canvas canvas, int degree, int left, int bottom, String LR) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 32, left + 560, bottom, rectPaint);
        canvas.drawText(LR + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color + " " + time, left + 20, bottom - 2, paint);
        canvas.drawText(orderItems.get(currentID).newCode + "", left + 330, bottom - 2, paintRed);

        canvas.drawRect(left, bottom + 1, left + 270, bottom + 33, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, left + 20, bottom + 31, paintBlue);

        canvas.restore();
    }

    public void remixx(){

        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 500, 40);

        Bitmap bitmapCombine = Bitmap.createBitmap(4862 + 120, 2724 + 240, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine = new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        Matrix matrixCombine = new Matrix();

        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 2520, 2450, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 2520, 2450, true);
        //part1
        Bitmap bitmapDBPart1 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part1);

        Bitmap bitmapPart1L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 11, 0, 1242, 1231);
        Canvas canvasPart1L = new Canvas(bitmapPart1L);
        canvasPart1L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart1L.drawBitmap(bitmapDBPart1, 0, 0, null);
        drawTextRotate1(canvasPart1L, 14, 10, 1025, "左");
        bitmapPart1L = Bitmap.createScaledBitmap(bitmapPart1L, 1242 + 60, 1231 + 60, true);

        matrixCombine.reset();
        matrixCombine.postRotate(-90);
        matrixCombine.postTranslate(0, 1242 + 60);
        canvasCombine.drawBitmap(bitmapPart1L, matrixCombine, null);
        bitmapPart1L.recycle();

        Bitmap bitmapPart1R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 11, 0, 1242, 1231);
        Canvas canvasPart1R = new Canvas(bitmapPart1R);
        canvasPart1R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart1R.drawBitmap(bitmapDBPart1, 0, 0, null);
        drawTextRotate1(canvasPart1R, 14, 10, 1025, "右");
        bitmapPart1R = Bitmap.createScaledBitmap(bitmapPart1R, 1242 + 60, 1231 + 60, true);

        matrixCombine.reset();
        matrixCombine.postRotate(-90);
        matrixCombine.postTranslate(0, 2724 + 60);
        canvasCombine.drawBitmap(bitmapPart1R, matrixCombine, null);
        bitmapPart1R.recycle();
        bitmapDBPart1.recycle();

        //part2
        Bitmap bitmapDBPart2 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part2);
        Bitmap bitmapPart2L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 506, 1240, 1499, 1210);

        Bitmap bitmapPart2LFUCK = Bitmap.createBitmap(1499, 1221, Bitmap.Config.ARGB_8888);
        Canvas canvasPart2L = new Canvas(bitmapPart2LFUCK);
        canvasPart2L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart2L.drawBitmap(bitmapPart2L, 0, 0, null);
        canvasPart2L.drawBitmap(bitmapDBPart2, 0, 0, null);
        canvasPart2L.save();
        canvasPart2L.rotate(74.8f, 15, 73);
        canvasPart2L.drawRect(15, 33, 515, 73, rectPaint);
        //canvasPart2L.drawBitmap(bitmapBarCode, 15, 33, null);
        canvasPart2L.drawText("      流：" + orderItems.get(currentID).newCode, 20, 70, paintRed);
        canvasPart2L.restore();
        canvasPart2L.save();
        canvasPart2L.rotate(-75.2f, 1331, 631);
        canvasPart2L.drawRect(1331, 596, 1951, 631, rectPaint);
        canvasPart2L.drawText(time + " " + orderItems.get(currentID).order_number + " 左" + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color, 1331, 627, paint);
        canvasPart2L.restore();

        matrixCombine.reset();
        matrixCombine.postTranslate(1231 + 60, 0);
        canvasCombine.drawBitmap(bitmapPart2LFUCK, matrixCombine, null);
        bitmapPart2L.recycle();
        bitmapPart2LFUCK.recycle();

        Bitmap bitmapPart2R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 506, 1240, 1499, 1210);

        Bitmap bitmapPart2RFUCK = Bitmap.createBitmap(1499, 1221, Bitmap.Config.ARGB_8888);
        Canvas canvasPart2R = new Canvas(bitmapPart2RFUCK);
        canvasPart2R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart2R.drawBitmap(bitmapPart2R, 0, 0, null);
        canvasPart2R.drawBitmap(bitmapDBPart2, 0, 0, null);
        canvasPart2R.save();
        canvasPart2R.rotate(74.8f, 15, 73);
        canvasPart2R.drawRect(15, 33, 515, 73, rectPaint);
        //canvasPart2R.drawBitmap(bitmapBarCode, 15, 33, null);
        canvasPart2R.drawText("      流："+orderItems.get(currentID).newCode, 20, 70, paintRed);
        canvasPart2R.restore();
        canvasPart2R.save();
        canvasPart2R.rotate(-75.2f, 1331, 631);
        canvasPart2R.drawRect(1331, 596, 1951, 631, rectPaint);
        canvasPart2R.drawText(time + " " + orderItems.get(currentID).order_number + " 右" + orderItems.get(currentID).size + "码" + orderItems.get(currentID).color, 1331, 627, paint);
        canvasPart2R.restore();

        matrixCombine.reset();
        matrixCombine.postTranslate(1231 + 60, 1482);
        canvasCombine.drawBitmap(bitmapPart2RFUCK, matrixCombine, null);
        bitmapPart2R.recycle();
        bitmapPart2RFUCK.recycle();
        bitmapDBPart2.recycle();

        //part3
        Bitmap bitmapDBPart3 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.boot41_part3);

        Bitmap bitmapPart3L = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1260, 0, 1242, 1231);
        Canvas canvasPart3L = new Canvas(bitmapPart3L);
        canvasPart3L.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart3L.drawBitmap(bitmapDBPart3, 0, 0, null);
        drawTextRotate3(canvasPart3L, -14, 655, 1165, "左");
        bitmapPart3L = Bitmap.createScaledBitmap(bitmapPart3L, 1242 + 60, 1231 + 60, true);

        matrixCombine.reset();
        matrixCombine.postRotate(90);
        matrixCombine.postTranslate(3961 + 120, 0);
        canvasCombine.drawBitmap(bitmapPart3L, matrixCombine, null);
        bitmapPart3L.recycle();

        Bitmap bitmapPart3R = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1260, 0, 1242, 1231);
        Canvas canvasPart3R = new Canvas(bitmapPart3R);
        canvasPart3R.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart3R.drawBitmap(bitmapDBPart3, 0, 0, null);
        drawTextRotate3(canvasPart3R, -14, 655, 1165, "右");
        bitmapPart3R = Bitmap.createScaledBitmap(bitmapPart3R, 1242 + 60, 1231 + 60, true);

        matrixCombine.reset();
        matrixCombine.postRotate(90);
        matrixCombine.postTranslate(3961 + 120, 1482);
        canvasCombine.drawBitmap(bitmapPart3R, matrixCombine, null);
        bitmapPart3L.recycle();
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

        matrixCombine.reset();
        matrixCombine.postTranslate(4092 + 120, 0);
        canvasCombine.drawBitmap(bitmapPart4L, matrixCombine, null);
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

        matrixCombine.reset();
        matrixCombine.postTranslate(4092 + 120, 1482);
        canvasCombine.drawBitmap(bitmapPart4R, matrixCombine, null);
        bitmapPart4R.recycle();
        bitmapDBPart4.recycle();

        try {
            setScale(orderItems.get(currentID).size);
            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) ((4862 + 120) * scaleX), (int) ((2724 + 240) * scaleY), true);
            bitmapCombine.recycle();

            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku + orderItems.get(currentID).size : "";
            String nameCombine = noNewCode + orderItems.get(currentID).newCode + orderItems.get(currentID).color + orderItems.get(currentID).order_number + strPlus + ".jpg";

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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku+ orderItems.get(currentID).size + printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku+ orderItems.get(currentID).size + printColor);
            sheet.addCell(label1);
            int num=orderItems.get(currentID).num;
            Number number2 = new Number(2, currentID+1, num);
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
            case 35:
                scaleX = 0.913f;
                scaleY = 0.859f;
                break;
            case 36:
                scaleX = 0.927f;
                scaleY = 0.881f;
                break;
            case 37:
                scaleX = 0.944f;
                scaleY = 0.907f;
                break;
            case 38:
                scaleX = 0.957f;
                scaleY = 0.928f;
                break;
            case 39:
                scaleX = 0.972f * 1.01f;
                scaleY = 0.952f * 1.01f;
                break;
            case 40:
                scaleX = 0.985f * 1.01f;
                scaleY = 0.975f * 1.01f;
                break;
            case 41:
                scaleX = 1.0f * 1.01f;
                scaleY = 1.0f * 1.01f;
                break;
            case 42:
                scaleX = 1.012f * 1.01f;
                scaleY = 1.022f * 1.01f;
                break;
            case 43:
                scaleX = 1.027f * 1.01f;
                scaleY = 1.045f * 1.01f;
                break;
            case 44:
                scaleX = 1.041f * 1.01f;
                scaleY = 1.069f * 1.01f;
                break;
            case 45:
                scaleX = 1.057f * 1.01f;
                scaleY = 1.09f * 1.01f;
                break;
            case 46:
                scaleX = 1.071f * 1.01f;
                scaleY = 1.112f * 1.01f;
                break;
        }
    }

}
