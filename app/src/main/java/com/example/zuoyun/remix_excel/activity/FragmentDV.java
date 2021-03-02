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
 * Created by zuoyun on 2016/10/6.
 */

public class FragmentDV extends BaseFragment {
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
    int intPlus = 1;

    @Override
    public int getLayout() {
        return R.layout.fragment_dv;
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

        //main:1000*1058
        //tongue:370*484
        Bitmap bitmapDB_main = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dq31_main);
        Bitmap bitmapDB_tongue = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dq31_tongue);
        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 1060, 1042, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 1060, 1042, true);

        //left
        Bitmap bitmapLeft_main = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 29, 0, 1000, 1042);
        Bitmap bitmapLeft_tongue = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 345, 258, 370, 484);

        Canvas canvasLeft_main = new Canvas(bitmapLeft_main);
        canvasLeft_main.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft_main.drawBitmap(bitmapDB_main, 0, 0, null);

        Canvas canvasLeft_tongue = new Canvas(bitmapLeft_tongue);
        canvasLeft_tongue.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft_tongue.drawBitmap(bitmapDB_tongue, 0, 0, null);

        //right
        Bitmap bitmapRight_main = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 29, 0, 1000, 1042);
        Bitmap bitmapRight_tongue = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 345, 258, 370, 484);

        Canvas canvasRight_main = new Canvas(bitmapRight_main);
        canvasRight_main.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight_main.drawBitmap(bitmapDB_main, 0, 0, null);

        Canvas canvasRight_tongue = new Canvas(bitmapRight_tongue);
        canvasRight_tongue.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight_tongue.drawBitmap(bitmapDB_tongue, 0, 0, null);

        //paint
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(32);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(32);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(32);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        String time = MainActivity.instance.orderDate_Print;
        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 420, 40);

        //left_main
        canvasLeft_main.drawRect(463, 1007, 563, 1037, rectPaint);
        canvasLeft_main.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "左", 463, 1034, paint);
        canvasLeft_main.save();
        canvasLeft_main.rotate(72.5f, 40, 378);
        canvasLeft_main.drawRect(40, 348, 440, 378, rectPaint);
        canvasLeft_main.drawText(time+"   "+orderItems.get(currentID).order_number, 40, 375, paint);
        canvasLeft_main.drawText("", 300, 375, paintRed);
        canvasLeft_main.restore();

        canvasLeft_main.save();
        canvasLeft_main.rotate(-71.8f, 842, 756);
        canvasLeft_main.drawRect(842, 718, 1242, 758, rectPaint);
        //canvasLeft_main.drawBitmap(bitmapBarCode, 832, 718, null);
        canvasLeft_main.drawText(orderItems.get(currentID).newCode, 842, 755, paintRed);
        canvasLeft_main.restore();

        //right_main
        canvasRight_main.drawRect(463, 1007, 563, 1037, rectPaint);
        canvasRight_main.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "右", 463, 1034, paint);
        canvasRight_main.save();
        canvasRight_main.rotate(72.5f, 40, 378);
        canvasRight_main.drawRect(40, 348, 440, 378, rectPaint);
        canvasRight_main.drawText(time+"   "+orderItems.get(currentID).order_number, 40, 375, paint);
        canvasRight_main.drawText("", 300, 375, paintRed);
        canvasRight_main.restore();

        canvasRight_main.save();
        canvasRight_main.rotate(-71.8f, 842, 756);
        canvasRight_main.drawRect(842, 718, 1242, 758, rectPaint);
        //canvasRight_main.drawBitmap(bitmapBarCode, 832, 718, null);
        canvasRight_main.drawText(orderItems.get(currentID).newCode, 842, 755, paintRed);
        canvasRight_main.restore();

        paint.setTextSize(24);
        paintRed.setTextSize(24);
        //left_tongue
        canvasLeft_tongue.drawRect(107, 450, 277, 472, rectPaint);
        canvasLeft_tongue.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "左", 107, 470, paint);
        canvasLeft_tongue.drawText(getLastNewCode(orderItems.get(currentID).newCode) + "", 207, 470, paintRed);
        //right_tongue
        canvasRight_tongue.drawRect(107, 450, 277, 472, rectPaint);
        canvasRight_tongue.drawText(orderItems.get(currentID).size + orderItems.get(currentID).color + "右", 107, 470, paint);
        canvasRight_tongue.drawText(getLastNewCode(orderItems.get(currentID).newCode) + "", 207, 470, paintRed);

        try {
            setScale(orderItems.get(currentID).size);

            Bitmap bitmapCombine = Bitmap.createBitmap(2848, 1100, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.drawColor(0xffffffff);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

            //left_main
            Matrix matrixCombine = new Matrix();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(1058, 0);
            canvasCombine.drawBitmap(bitmapLeft_main, matrixCombine, null);
            //right_main
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2234, 0);
            canvasCombine.drawBitmap(bitmapRight_main, matrixCombine, null);
            //left_tongue
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2848, 0);
            canvasCombine.drawBitmap(bitmapLeft_tongue, matrixCombine, null);
            //right_tongue
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2848, 500);
            canvasCombine.drawBitmap(bitmapRight_tongue, matrixCombine, null);

            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) (2848 * scaleY), (int) (1100 * scaleX), true);
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
            BitmapToJpg.save(bitmapPrint, fileSave, 112);

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
            Label label0 = new Label(0, currentID + 1, orderItems.get(currentID).order_number + orderItems.get(currentID).sku + orderItems.get(currentID).size + printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID + 1, orderItems.get(currentID).sku + orderItems.get(currentID).size + printColor);
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
                scaleX = 0.964f;
                scaleY = 0.924f;
                break;
            case 29:
                scaleX = 0.964f;
                scaleY = 0.941f;
                break;
            case 30:
                scaleX = 0.979f;
                scaleY = 0.969f;
                break;
            case 31:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
            case 32:
                scaleX = 1.045f;
                scaleY = 1.058f;
                break;
            case 33:
                scaleX = 1.045f;
                scaleY = 1.058f;
                break;
            case 34:
                scaleX = 1.07f;
                scaleY = 1.09f;
                break;
        }
    }

}
