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
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDT extends BaseFragment {
    Context context;
//    String sdCardPath = "/mnt/asec/share";
String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    int currentID;
    String childPath;

    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.bt_remix)
    Button bt_remix;

    float scaleX=1.0f, scaleY = 1.0f;
    int num;
    String strPlus = "";

    @Override
    public int getLayout() {
        return R.layout.fragment_dff;
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
                    iv_left.setImageDrawable(null);
                    iv_right.setImageDrawable(null);
                    Log.e("fragment2", "message0");
                } else if (message == 1) {
                    Log.e("fragment2", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_left.setImageBitmap(MainActivity.instance.bitmapLeft);
                    checkremix();
                } else if (message == 2) {
                    Log.e("fragment2", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_right.setImageBitmap(MainActivity.instance.bitmapRight);
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
        //main:1612*1262
        //side:2172*578(578*1086)
        Bitmap bitmapDB_main = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dff41_main);
        Bitmap bitmapDB_side = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dff41_side);

        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 1768, 2288, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 1768, 2288, true);
        //left_main
        Bitmap bitmapLeftMain = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 79, 1001, 1612, 1262);
        Canvas canvasLeftMain = new Canvas(bitmapLeftMain);
        canvasLeftMain.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeftMain.drawBitmap(bitmapDB_main, 0, 0, null);
        //left_side
        Bitmap bitmapLeftSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 9, 75, 578, 1086);
        Bitmap bitmapLeftSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1180, 75, 578, 1086);
        Bitmap bitmapLeftSide = Bitmap.createBitmap(2172, 578, Bitmap.Config.ARGB_8888);
        Canvas canvasLeftSide = new Canvas(bitmapLeftSide);
        canvasLeftSide.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Matrix matrixSide = new Matrix();
        matrixSide.postRotate(90);
        matrixSide.postTranslate(1086, 0);
        canvasLeftSide.drawBitmap(bitmapLeftSide1, matrixSide, null);
        matrixSide.reset();
        matrixSide.postRotate(-90);
        matrixSide.postTranslate(1086, 578);
        canvasLeftSide.drawBitmap(bitmapLeftSide2, matrixSide, null);
        canvasLeftSide.drawBitmap(bitmapDB_side, 0, 0, null);

        //right_main
        Bitmap bitmapRightMain = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 79, 1001, 1612, 1262);
        Canvas canvasRightMain = new Canvas(bitmapRightMain);
        canvasRightMain.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRightMain.drawBitmap(bitmapDB_main, 0, 0, null);
        //right_side
        Bitmap bitmapRightSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 9, 75, 578, 1086);
        Bitmap bitmapRightSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1180, 75, 578, 1086);
        Bitmap bitmapRightSide = Bitmap.createBitmap(2172, 578, Bitmap.Config.ARGB_8888);
        Canvas canvasRightSide = new Canvas(bitmapRightSide);
        canvasRightSide.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrixSide.reset();
        matrixSide.postRotate(90);
        matrixSide.postTranslate(1086, 0);
        canvasRightSide.drawBitmap(bitmapRightSide1, matrixSide, null);
        matrixSide.reset();
        matrixSide.postRotate(-90);
        matrixSide.postTranslate(1086, 578);
        canvasRightSide.drawBitmap(bitmapRightSide2, matrixSide, null);
        canvasRightSide.drawBitmap(bitmapDB_side, 0, 0, null);

        //paint
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(23);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(23);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);
        String time = MainActivity.instance.orderDate_Print;
        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 500, 50);

        canvasLeftSide.drawRect(1655, 48, 1805, 48 + 23, rectPaint);
        canvasLeftSide.drawText(orderItems.get(currentID).size + "码 左", 1656, 48 + 21, paintRed);
        canvasLeftSide.drawRect(370, 48, 512, 48 + 23, rectPaint);
        canvasLeftSide.drawText(orderItems.get(currentID).order_number, 370, 48 + 21, paintRed);
        canvasLeftMain.drawRect(615, 1160, 1011, 1212, rectPaint);
        canvasLeftMain.drawText((orderItems.get(currentID).size) + "码" + orderItems.get(currentID).color + "  左", 612, 1206, paintRed);
        canvasLeftMain.save();
        canvasLeftMain.rotate(-68.5f, 1296, 882);
        canvasLeftMain.drawRect(1298, 832, 1800, 882, rectPaint);
        canvasLeftMain.drawText(orderItems.get(currentID).order_number + "     " + time, 1300, 876, paint);
        canvasLeftMain.restore();

        canvasLeftMain.save();
        canvasLeftMain.rotate(-111.5f, 373, 862);
        canvasLeftMain.drawRect(373, 800, 873, 850, rectPaint);
        //canvasLeftMain.drawBitmap(bitmapBarCode, 373, 800, null);
        canvasLeftMain.drawText(orderItems.get(currentID).newCodeStr, 373, 845, paint);
        canvasLeftMain.restore();

        canvasRightSide.drawRect(1655, 48, 1805, 48 + 23, rectPaint);
        canvasRightSide.drawText((orderItems.get(currentID).size + "码 右"), 1656, 48 + 21, paintRed);
        canvasRightSide.drawRect(370, 48, 512, 48 + 23, rectPaint);
        canvasRightSide.drawText(orderItems.get(currentID).order_number, 370, 48 + 21, paintRed);
        canvasRightMain.drawRect(615, 1160, 1011, 1212, rectPaint);
        canvasRightMain.drawText((orderItems.get(currentID).size) + "码" + orderItems.get(currentID).color + "  右", 612, 1206, paintRed);
        canvasRightMain.save();
        canvasRightMain.rotate(-111.5f, 373, 862);
        canvasRightMain.drawRect(373, 800, 873, 850, rectPaint);
        canvasRightMain.drawText(orderItems.get(currentID).order_number + "     " + time, 375, 844, paint);
        canvasRightMain.restore();

        canvasRightMain.save();
        canvasRightMain.rotate(-68.5f, 1296, 882);
        canvasRightMain.drawRect(1298, 832, 1800, 882, rectPaint);
        //canvasRightMain.drawBitmap(bitmapBarCode, 1298, 832, null);
        canvasRightMain.drawText(orderItems.get(currentID).newCodeStr, 1298, 876, paint);
        canvasRightMain.restore();

        try {
            setScale(orderItems.get(currentID).size);

            Bitmap bitmapCombine = Bitmap.createBitmap(4696, 1612+59, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);
            //left_side
            canvasCombine.drawBitmap(bitmapLeftSide, 0, 0, null);
            //right_side
            Matrix matrixCombine = new Matrix();
            matrixCombine.postTranslate(0, 726);
            canvasCombine.drawBitmap(bitmapRightSide, matrixCombine, null);
            //left_main
            matrixCombine.reset();
            matrixCombine.postRotate(-90);
            matrixCombine.postTranslate(2172, 1612);
            canvasCombine.drawBitmap(bitmapLeftMain, matrixCombine, null);
            //right_main
            matrixCombine.reset();
            matrixCombine.postRotate(-90);
            matrixCombine.postTranslate(2172+1262, 1612);
            canvasCombine.drawBitmap(bitmapRightMain, matrixCombine, null);

            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) (4696 * scaleY), (int) ((1612 + 59) * scaleX), true);
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
            BitmapToJpg.save(bitmapPrint, fileSave, 150);

            //释放bitmap
            bitmapDB_main.recycle();
            bitmapDB_side.recycle();
            bitmapLeftMain.recycle();
            bitmapLeftSide1.recycle();
            bitmapLeftSide2.recycle();
            bitmapLeftSide.recycle();
            bitmapRightMain.recycle();
            bitmapRightSide1.recycle();
            bitmapRightSide2.recycle();
            bitmapRightSide.recycle();
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
                scaleX = 0.857f;
                scaleY = 0.872f;
                break;
            case 36:
                scaleX = 0.923f;
                scaleY = 0.888f;
                break;
            case 37:
                scaleX = 0.937f;
                scaleY = 0.912f;
                break;
            case 38:
                scaleX = 0.954f;
                scaleY = 0.933f;
                break;
            case 39:
                scaleX = 0.97f;
                scaleY = 0.957f;
                break;
            case 40:
                scaleX = 0.986f;
                scaleY = 0.983f;
                break;
            case 41:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
            case 42:
                scaleX = 1.016f;
                scaleY = 1.019f;
                break;
            case 43:
                scaleX = 1.032f;
                scaleY = 1.046f;
                break;
            case 44:
                scaleX = 1.047f;
                scaleY = 1.07f;
                break;
            case 45:
                scaleX = 1.061f;
                scaleY = 1.089f;
                break;
            case 46:
                scaleX = 1.079f;
                scaleY = 1.117f;
                break;
        }
    }

}
