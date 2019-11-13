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
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDJ extends BaseFragment {
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
                    Log.e("fragment2", "message0");
                } else if (message == 1) {
                    Log.e("fragment2", "message1");
                    bt_remix.setClickable(true);
                    if (!MainActivity.instance.cb_fastmode.isChecked()) {
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapLeft);
                    }
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

    public void remixx(){
        //main:1288*1086
        //side:1682*631(631*841)
        Bitmap bitmapDB_main = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dj41_main);
        Bitmap bitmapDB_side = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dj41_side);

        //left_main
        Bitmap bitmapLeftMain = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 331, 1015, 1288, 1086);
        Canvas canvasLeftMain = new Canvas(bitmapLeftMain);
        canvasLeftMain.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeftMain.drawBitmap(bitmapDB_main, 0, 0, null);
        //left_side
        Bitmap bitmapLeftSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 297, 124, 698, 890);
        bitmapLeftSide1 = Bitmap.createScaledBitmap(bitmapLeftSide1, 631, 841, true);
        Bitmap bitmapLeftSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1062, 124, 698, 890);
        bitmapLeftSide2 = Bitmap.createScaledBitmap(bitmapLeftSide2, 631, 841, true);

//        Bitmap bitmapLeftSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 295, 124, 698, 890);
//        bitmapLeftSide1 = Bitmap.createScaledBitmap(bitmapLeftSide1, 631, 841, true);
//        Bitmap bitmapLeftSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1062, 124, 698, 890);
//        bitmapLeftSide2 = Bitmap.createScaledBitmap(bitmapLeftSide2, 631, 841, true);

        Bitmap bitmapLeftSide = Bitmap.createBitmap(1682, 631, Bitmap.Config.ARGB_8888);
        Canvas canvasLeftSide = new Canvas(bitmapLeftSide);
        canvasLeftSide.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Matrix matrixSide = new Matrix();
        matrixSide.postRotate(90);
        matrixSide.postTranslate(841, 0);
        canvasLeftSide.drawBitmap(bitmapLeftSide1, matrixSide, null);
        matrixSide.reset();
        matrixSide.postRotate(-90);
        matrixSide.postTranslate(841, 631);
        canvasLeftSide.drawBitmap(bitmapLeftSide2, matrixSide, null);
        canvasLeftSide.drawBitmap(bitmapDB_side, 0, 0, null);

        //right_main
        Bitmap bitmapRightMain = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 331, 1015, 1288, 1086);
        Canvas canvasRightMain = new Canvas(bitmapRightMain);
        canvasRightMain.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRightMain.drawBitmap(bitmapDB_main, 0, 0, null);
        //right_side
        Bitmap bitmapRightSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 297, 124, 698, 890);
        bitmapRightSide1 = Bitmap.createScaledBitmap(bitmapRightSide1, 631, 841, true);
        Bitmap bitmapRightSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1062, 124, 698, 890);
        bitmapRightSide2 = Bitmap.createScaledBitmap(bitmapRightSide2, 631, 841, true);

//        Bitmap bitmapRightSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 295, 124, 698, 890);
//        bitmapRightSide1 = Bitmap.createScaledBitmap(bitmapRightSide1, 631, 841, true);
//        Bitmap bitmapRightSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1062, 124, 698, 890);
//        bitmapRightSide2 = Bitmap.createScaledBitmap(bitmapRightSide2, 631, 841, true);

        Bitmap bitmapRightSide = Bitmap.createBitmap(1682, 631, Bitmap.Config.ARGB_8888);
        Canvas canvasRightSide = new Canvas(bitmapRightSide);
        canvasRightSide.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrixSide.reset();
        matrixSide.postRotate(90);
        matrixSide.postTranslate(841, 0);
        canvasRightSide.drawBitmap(bitmapRightSide1, matrixSide, null);
        matrixSide.reset();
        matrixSide.postRotate(-90);
        matrixSide.postTranslate(841, 631);
        canvasRightSide.drawBitmap(bitmapRightSide2, matrixSide, null);
        canvasRightSide.drawBitmap(bitmapDB_side, 0, 0, null);

        //paint
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(35);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(35);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(35);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);
        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 460, 36);
        String time = MainActivity.instance.orderDate_Print;

        canvasLeftMain.save();
        canvasLeftMain.rotate(-6.8f, 900, 80);
        canvasLeftMain.drawRect(900, 44, 1250, 80, rectPaint);
        canvasLeftMain.drawText(orderItems.get(currentID).order_number + "  " + time, 900, 77, paint);
        canvasLeftMain.restore();
        canvasLeftMain.save();
        canvasLeftMain.rotate(6.8f, 92, 44);
        canvasLeftMain.drawRect(42, 8, 500, 44, rectPaint);
        //canvasLeftMain.drawBitmap(bitmapBarCode, 46, 8, null);
        canvasLeftMain.drawText(" 左" + orderItems.get(currentID).size + "码", 62, 41, paintRed);
        canvasLeftMain.restore();

        canvasRightMain.save();
        canvasRightMain.rotate(-6.8f, 900, 80);
        canvasRightMain.drawRect(900, 44, 1250, 80, rectPaint);
        canvasRightMain.drawText(orderItems.get(currentID).order_number + "  " + time, 900, 77, paint);
        canvasRightMain.restore();
        canvasRightMain.save();
        canvasRightMain.rotate(6.8f, 92, 44);
        canvasRightMain.drawRect(42, 8, 500, 44, rectPaint);
//        canvasRightMain.drawBitmap(bitmapBarCode, 46, 8, null);
        canvasRightMain.drawText(" 右" + orderItems.get(currentID).size + "码", 62, 41, paintRed);
        canvasRightMain.restore();

//        paintRed.setTextSize(45);
//        canvasLeftMain.drawRect(540, 1018, 780, 1060, rectPaint);
//        canvasLeftMain.drawText("  左 " + orderItems.get(currentID).size + "码", 540, 1057, paintRed);
//        canvasRightMain.drawRect(540, 1018, 780, 1060, rectPaint);
//        canvasRightMain.drawText(" 右 " + orderItems.get(currentID).size + "码", 540, 1057, paintRed);

        paintRed.setTextSize(50);
        paint.setTextSize(50);

        canvasLeftSide.save();
        canvasLeftSide.rotate(-165.9f, 1238, 81);
        canvasLeftSide.drawRect(1100, 29, 1560, 81, rectPaint);
        canvasLeftSide.drawText(" 左 " + orderItems.get(currentID).newCode, 1100, 77, paintRed);
        canvasLeftSide.restore();
        canvasLeftSide.save();
        canvasLeftSide.rotate(167.2f, 752, 6);
        canvasLeftSide.drawRect(752, -46, 1200, 6, rectPaint);
        canvasLeftSide.drawText("  " + time + "  " + orderItems.get(currentID).size + "码", 752, 2, paintRed);
        canvasLeftSide.restore();

        canvasRightSide.save();
        canvasRightSide.rotate(-165.9f, 1238, 81);
        canvasRightSide.drawRect(1100, 29, 1560, 81, rectPaint);
        canvasRightSide.drawText(" 右 " + orderItems.get(currentID).newCode, 1100, 77, paintRed);
        canvasRightSide.restore();
        canvasRightSide.save();
        canvasRightSide.rotate(167.2f, 752, 6);
        canvasRightSide.drawRect(752, -46, 1200, 6, rectPaint);
        canvasRightSide.drawText("  " + time + "  " + orderItems.get(currentID).size + "码", 752, 2, paintRed);
        canvasRightSide.restore();

        try {
            setScale(orderItems.get(currentID).size);

            Bitmap bitmapCombine = Bitmap.createBitmap(4094, 1383, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);
            //left_side
            Matrix matrixCombine = new Matrix();
            matrixCombine.postTranslate(0, 121);
            canvasCombine.drawBitmap(bitmapLeftSide, matrixCombine, null);
            //right_side
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 752);
            canvasCombine.drawBitmap(bitmapRightSide, matrixCombine, null);
            //left_main
            matrixCombine.reset();
            matrixCombine.postRotate(-90);
            matrixCombine.postTranslate(1807, 1383);
            canvasCombine.drawBitmap(bitmapLeftMain, matrixCombine, null);
            //right_main
            matrixCombine.reset();
            matrixCombine.postRotate(-90);
            matrixCombine.postTranslate(3008, 1383);
            canvasCombine.drawBitmap(bitmapRightMain, matrixCombine, null);

            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) (4094 * scaleY), (int) (1383 * scaleX), true);
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku + orderItems.get(currentID).size : "";
            String nameCombine = noNewCode + orderItems.get(currentID).sku + orderItems.get(currentID).newCode + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapPrint, fileSave, 141);

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
            Label label0 = new Label(0, currentID + 1, orderItems.get(currentID).order_number + orderItems.get(currentID).sku + orderItems.get(currentID).size);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID + 1, orderItems.get(currentID).sku + orderItems.get(currentID).size);
            sheet.addCell(label1);
            Number number2 = new Number(2, currentID + 1, orderItems.get(currentID).num);
            sheet.addCell(number2);
            Label label3 = new Label(3, currentID + 1, orderItems.get(currentID).customer);
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
            case 36:
                scaleX = 0.909f;
                scaleY = 0.885f;
                break;
            case 37:
                scaleX = 0.927f;
                scaleY = 0.907f;
                break;
            case 38:
                scaleX = 0.944f;
                scaleY = 0.931f;
                break;
            case 39:
                scaleX = 0.963f;
                scaleY = 0.954f;
                break;
            case 40:
                scaleX = 0.981f;
                scaleY = 0.978f;
                break;
            case 41:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
            case 42:
                scaleX = 1.018f;
                scaleY = 1.023f;
                break;
            case 43:
                scaleX = 1.036f;
                scaleY = 1.046f;
                break;
            case 44:
                scaleX = 1.054f;
                scaleY = 1.068f;
                break;
            case 45:
                scaleX = 1.072f;
                scaleY = 1.091f;
                break;
            case 46:
                scaleX = 1.091f;
                scaleY = 1.115f;
                break;
        }
    }

}
