package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
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
 * Created by zuoyun on 2016/10/6.
 */

public class FragmentDQOld extends BaseFragment {
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

    float scaleX=1.0f, scaleY = 1.0f;
    int num;
    String strPlus = "";

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

    public void remixx(){
        Bitmap bitmapDB_main = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dq40_main);
        Bitmap bitmapDB_tongue = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dq40_tongue);

        //left
        Bitmap bitmapLeft_main = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 34, 48, 885, 1099);
        Bitmap bitmapLeft_tongue = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 282, 331, 390, 468);

        Canvas canvasLeft_main = new Canvas(bitmapLeft_main);
        canvasLeft_main.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft_main.drawBitmap(bitmapDB_main, 0, 0, null);

        Canvas canvasLeft_tongue = new Canvas(bitmapLeft_tongue);
        canvasLeft_tongue.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft_tongue.drawBitmap(bitmapDB_tongue, 0, 0, null);

        //right
        Bitmap bitmapRight_main = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 34, 48, 885, 1099);
        Bitmap bitmapRight_tongue = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 282, 331, 390, 468);

        Canvas canvasRight_main = new Canvas(bitmapRight_main);
        canvasRight_main.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight_main.drawBitmap(bitmapDB_main, 0, 0, null);

        Canvas canvasRight_tongue = new Canvas(bitmapRight_tongue);
        canvasRight_tongue.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight_tongue.drawBitmap(bitmapDB_tongue, 0, 0, null);

        //paint
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(30);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff000000);
        paintBlue.setTextSize(30);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 400, 30);
        String time = MainActivity.instance.orderDate_Print;

        //left_main
        canvasLeft_main.drawRect(390, 1040, 510, 1070, rectPaint);
        canvasLeft_main.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "左", 390, 1067, paint);
        Path pathLeft_date = new Path();
        pathLeft_date.moveTo(124, 772);
        pathLeft_date.lineTo(179, 915);
        pathLeft_date.lineTo(208, 902);
        pathLeft_date.lineTo(154, 759);
        pathLeft_date.close();
        canvasLeft_main.drawPath(pathLeft_date, rectPaint);
        canvasLeft_main.save();
        canvasLeft_main.rotate(71, 124, 772);
        canvasLeft_main.drawText(time, 126, 769, paint);
        canvasLeft_main.restore();
        Path pathLeft_number = new Path();
        pathLeft_number.moveTo(647, 1011);
        pathLeft_number.lineTo(713, 910);
        pathLeft_number.lineTo(690, 894);
        pathLeft_number.lineTo(622, 996);
        pathLeft_number.close();
        canvasLeft_main.drawPath(pathLeft_number, rectPaint);
        canvasLeft_main.save();
        canvasLeft_main.rotate(-56.8f, 647, 1011);
        canvasLeft_main.drawText(orderItems.get(currentID).order_number, 649, 1007, paint);
        canvasLeft_main.restore();
        Path pathLeft_ordernumber = new Path();
        pathLeft_ordernumber.moveTo(717, 900);
        pathLeft_ordernumber.lineTo(774, 737);
        pathLeft_ordernumber.lineTo(747, 728);
        pathLeft_ordernumber.lineTo(690, 888);
        pathLeft_ordernumber.close();
        canvasLeft_main.drawPath(pathLeft_ordernumber, rectPaint);
        canvasLeft_main.save();
        canvasLeft_main.rotate(-70.8f, 717, 900);
        canvasLeft_main.drawText(orderItems.get(currentID).newCode,718,897,paintRed);
        canvasLeft_main.restore();

        canvasLeft_main.save();
        canvasLeft_main.rotate(77, 72, 378);
        canvasLeft_main.drawRect(22, 354, 372, 380, rectPaint);
        //canvasLeft_main.drawBitmap(bitmapBarCode, -3, 356, null);
        canvasLeft_main.drawText("      " + orderItems.get(currentID).printCode, 22, 377, paintBlue);
        canvasLeft_main.restore();

        //right_main
        canvasRight_main.drawRect(390, 1040, 510, 1070, rectPaint);
        canvasRight_main.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "右", 390, 1067, paint);
        Path pathRight_date = new Path();
        pathRight_date.moveTo(721, 882);
        pathRight_date.lineTo(774, 741);
        pathRight_date.lineTo(745, 729);
        pathRight_date.lineTo(693, 870);
        pathRight_date.close();
        canvasRight_main.drawPath(pathRight_date, rectPaint);
        canvasRight_main.save();
        canvasRight_main.rotate(-70.7f, 721, 882);
        canvasRight_main.drawText(time, 722, 879, paint);
        canvasRight_main.restore();
        Path pathRight_number = new Path();
        pathRight_number.moveTo(175, 910);
        pathRight_number.lineTo(243, 1010);
        pathRight_number.lineTo(265, 994);
        pathRight_number.lineTo(199, 894);
        pathRight_number.close();
        canvasRight_main.drawPath(pathRight_number, rectPaint);
        canvasRight_main.save();
        canvasRight_main.rotate(57, 175, 910);
        canvasRight_main.drawText(orderItems.get(currentID).order_number, 176, 907, paint);
        canvasRight_main.restore();
        Path pathRight_ordernumber = new Path();
        pathRight_ordernumber.moveTo(113, 736);
        pathRight_ordernumber.lineTo(170, 899);
        pathRight_ordernumber.lineTo(196, 886);
        pathRight_ordernumber.lineTo(139, 726);
        pathRight_ordernumber.close();
        canvasRight_main.drawPath(pathRight_ordernumber, rectPaint);
        canvasRight_main.save();
        canvasRight_main.rotate(70, 113, 736);
        canvasRight_main.drawText(orderItems.get(currentID).newCode, 114, 733, paintRed);
        canvasRight_main.restore();

        canvasRight_main.save();
        canvasRight_main.rotate(77, 72, 378);
        canvasRight_main.drawRect(22, 354, 372, 380, rectPaint);
        //canvasRight_main.drawBitmap(bitmapBarCode, -3, 356, null);
        canvasRight_main.drawText("      "+orderItems.get(currentID).printCode, 22, 377, paintBlue);
        canvasRight_main.restore();

        paint.setTextSize(25);
        paintRed.setTextSize(26);
        //left_tongue
        canvasLeft_tongue.drawRect(130, 440, 270, 460, rectPaint);
        canvasLeft_tongue.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "左", 130, 460, paint);
        canvasLeft_tongue.drawText(getLastNewCode(orderItems.get(currentID).newCode) + "", 210, 460, paintRed);
        canvasLeft_tongue.drawRect(86, 419, 316, 439, rectPaint);
        canvasLeft_tongue.drawText(time + "  " + orderItems.get(currentID).order_number, 86, 439, paint);
        //right_tongue
        canvasRight_tongue.drawRect(130, 440, 270, 460, rectPaint);
        canvasRight_tongue.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "右", 130, 460, paint);
        canvasRight_tongue.drawText(getLastNewCode(orderItems.get(currentID).newCode) + "", 210, 460, paintRed);
        canvasRight_tongue.drawRect(86, 419, 316, 439, rectPaint);
        canvasRight_tongue.drawText(time + "  " + orderItems.get(currentID).order_number, 86, 439, paint);

        try {
            setScale(orderItems.get(currentID).size);

            Bitmap bitmapCombine = Bitmap.createBitmap(2716, 885+35, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.drawColor(0xffffffff);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

            //left_main
            Matrix matrixCombine = new Matrix();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(1099, 0);
            canvasCombine.drawBitmap(bitmapLeft_main, matrixCombine, null);
            //right_main
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2208, 0);
            canvasCombine.drawBitmap(bitmapRight_main, matrixCombine, null);
            //left_tongue
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2716, 38);
            canvasCombine.drawBitmap(bitmapLeft_tongue, matrixCombine, null);
            //right_tongue
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2716, 470);
            canvasCombine.drawBitmap(bitmapRight_tongue, matrixCombine, null);

            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) (2716 * scaleY), (int) ((885 + 35) * scaleX), true);
            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String nameCombine = orderItems.get(currentID).newCode + orderItems.get(currentID).color + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapPrint, fileSave, 88);

            //释放bitmap
            bitmapDB_main.recycle();
            bitmapDB_tongue.recycle();
            bitmapLeft_main.recycle();
            bitmapLeft_tongue.recycle();
            bitmapRight_main.recycle();
            bitmapRight_tongue.recycle();
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
            Log.e("aaa", e.toString());
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
                scaleX = 0.9055f;
                scaleY = 0.886f;
                break;
            case 36:
                scaleX = 0.9215f;
                scaleY = 0.91f;
                break;
            case 37:
                scaleX = 0.9395f;
                scaleY = 0.931f;
                break;
            case 38:
                scaleX = 0.9614f;
                scaleY = 0.955f;
                break;
            case 39:
                scaleX = 0.983f;
                scaleY = 0.978f;
                break;
            case 40:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
            case 41:
                scaleX = 1.016f;
                scaleY = 1.021f;
                break;
            case 42:
                scaleX = 1.038f;
                scaleY = 1.045f;
                break;
            case 43:
                scaleX = 1.059f;
                scaleY = 1.067f;
                break;
            case 44:
                scaleX = 1.075f;
                scaleY = 1.093f;
                break;
            case 45:
                scaleX = 1.094f;
                scaleY = 1.116f;
                break;
            case 46:
                scaleX = 1.118f;
                scaleY = 1.141f;
                break;
        }
    }

}
