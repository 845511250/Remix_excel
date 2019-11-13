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

import static com.example.zuoyun.remix_excel.activity.MainActivity.getLastNewCode;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDU extends BaseFragment {
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
                    remixxKid();
                    strPlus += "+";
                }
            }
        }.start();

    }

    public void remixxKid(){
        //main:1284*985
        //side:1688*470(470*844)
        Bitmap bitmapDB_main = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dff31_main);
        Bitmap bitmapDB_side = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dff31_side);

        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 1410, 1785, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 1410, 1785, true);
        //left_main
        Bitmap bitmapLeftMain = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 65, 789, 1284, 985);
        Canvas canvasLeftMain = new Canvas(bitmapLeftMain);
        canvasLeftMain.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeftMain.drawBitmap(bitmapDB_main, 0, 0, null);
        //left_side
        Bitmap bitmapLeftSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 7, 58, 470, 844);
        Bitmap bitmapLeftSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 931, 58, 470, 844);
        Bitmap bitmapLeftSide = Bitmap.createBitmap(1688, 470, Bitmap.Config.ARGB_8888);
        Canvas canvasLeftSide = new Canvas(bitmapLeftSide);
        canvasLeftSide.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Matrix matrixSide = new Matrix();
        matrixSide.postRotate(90);
        matrixSide.postTranslate(844, 0);
        canvasLeftSide.drawBitmap(bitmapLeftSide1, matrixSide, null);
        matrixSide.reset();
        matrixSide.postRotate(-90);
        matrixSide.postTranslate(844, 470);
        canvasLeftSide.drawBitmap(bitmapLeftSide2, matrixSide, null);
        canvasLeftSide.drawBitmap(bitmapDB_side, 0, 0, null);

        //right_main
        Bitmap bitmapRightMain = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 65, 789, 1284, 985);
        Canvas canvasRightMain = new Canvas(bitmapRightMain);
        canvasRightMain.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRightMain.drawBitmap(bitmapDB_main, 0, 0, null);
        //right_side
        Bitmap bitmapRightSide1 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 7, 58, 470, 844);
        Bitmap bitmapRightSide2 = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 931, 58, 470, 844);
        Bitmap bitmapRightSide = Bitmap.createBitmap(1688, 470, Bitmap.Config.ARGB_8888);
        Canvas canvasRightSide = new Canvas(bitmapRightSide);
        canvasRightSide.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrixSide.reset();
        matrixSide.postRotate(90);
        matrixSide.postTranslate(844, 0);
        canvasRightSide.drawBitmap(bitmapRightSide1, matrixSide, null);
        matrixSide.reset();
        matrixSide.postRotate(-90);
        matrixSide.postTranslate(844, 470);
        canvasRightSide.drawBitmap(bitmapRightSide2, matrixSide, null);
        canvasRightSide.drawBitmap(bitmapDB_side, 0, 0, null);

        //paint
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(42);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(42);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(42);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);
        String time = MainActivity.instance.orderDate_Print;
        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 500, 50);

        canvasLeftSide.drawRect(1264, 21, 1414, 61, rectPaint);
        canvasLeftSide.drawText(getLastNewCode(orderItems.get(currentID).newCode) + "左", 1265, 57, paintRed);
        canvasLeftMain.drawRect(460, 903, 830, 943, rectPaint);
        canvasLeftMain.drawText("    左", 460, 939, paintRed);
        canvasLeftMain.drawText(orderItems.get(currentID).size + "码" + orderItems.get(currentID).color, 640, 939, paint);
        canvasLeftMain.save();
        canvasLeftMain.rotate(-73.1f, 1121, 543);
        canvasLeftMain.drawRect(1121, 503, 1621, 543, rectPaint);
        canvasLeftMain.drawText(orderItems.get(currentID).order_number + "     " + time, 1121, 539, paint);
        canvasLeftMain.restore();

        canvasLeftMain.save();
        canvasLeftMain.rotate(-107.1f, 200, 530);
        canvasLeftMain.drawRect(200, 490, 700, 530, rectPaint);
        //canvasLeftMain.drawBitmap(bitmapBarCode, 200, 480, null);
        canvasLeftMain.drawText(orderItems.get(currentID).newCode, 200, 526, paintRed);
        canvasLeftMain.restore();

        canvasRightSide.drawRect(1264, 21, 1414, 61, rectPaint);
        canvasRightSide.drawText(getLastNewCode(orderItems.get(currentID).newCode) + "右", 1265, 57, paintRed);
        canvasRightMain.drawRect(460, 903, 830, 943, rectPaint);
        canvasRightMain.drawText(orderItems.get(currentID).size + "码" + orderItems.get(currentID).color, 460, 939, paint);
        canvasRightMain.drawText("   右", 640, 939, paintRed);
        canvasRightMain.save();
        canvasRightMain.rotate(-107.1f, 200, 530);
        canvasRightMain.drawRect(200, 490, 700, 530, rectPaint);
        canvasRightMain.drawText(orderItems.get(currentID).order_number + "     " + time, 200, 526, paint);
        canvasRightMain.restore();

        canvasRightMain.save();
        canvasRightMain.rotate(-73.1f, 1121, 543);
        canvasRightMain.drawRect(1121, 503, 1621, 543, rectPaint);
        //canvasRightMain.drawBitmap(bitmapBarCode, 1121, 503, null);
        canvasRightMain.drawText(orderItems.get(currentID).newCode, 1121, 540, paintRed);
        canvasRightMain.restore();

        try {
            setScale(orderItems.get(currentID).size);

            Bitmap bitmapCombine = Bitmap.createBitmap(3902, 1344, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);
            //left_side
            canvasCombine.drawBitmap(bitmapLeftSide, 0, 0, null);
            //right_side
            Matrix matrixCombine = new Matrix();
            matrixCombine.postTranslate(0, 704);
            canvasCombine.drawBitmap(bitmapRightSide, matrixCombine, null);
            //left_main
            matrixCombine.reset();
            matrixCombine.postRotate(-90);
            matrixCombine.postTranslate(1810, 1284);
            canvasCombine.drawBitmap(bitmapLeftMain, matrixCombine, null);
            //right_main
            matrixCombine.reset();
            matrixCombine.postRotate(-90);
            matrixCombine.postTranslate(2917, 1284);
            canvasCombine.drawBitmap(bitmapRightMain, matrixCombine, null);

            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) (3902 * scaleY), (int) (1344 * scaleX), true);
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
            case 28:
                scaleX = 0.943f;
                scaleY = 0.912f;
                break;
            case 29:
                scaleX = 0.962f;
                scaleY = 0.942f;
                break;
            case 30:
                scaleX = 0.98f;
                scaleY = 0.97f;
                break;
            case 31:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
            case 32:
                scaleX = 1.019f;
                scaleY = 1.03f;
                break;
            case 33:
                scaleX = 1.038f;
                scaleY = 1.058f;
                break;
            case 34:
                scaleX = 1.058f;
                scaleY = 1.089f;
                break;
        }
    }

}
