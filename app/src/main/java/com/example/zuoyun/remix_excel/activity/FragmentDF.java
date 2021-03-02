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

public class FragmentDF extends BaseFragment {
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
    int intPlus = 1;
    @Override
    public int getLayout() {
        return R.layout.fragment_df;
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
                    Log.e("fragment_de_old", "message0");
                } else if (message == 1) {
                    Log.e("fragment_de_old", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_left.setImageBitmap(MainActivity.instance.bitmapLeft);
                    checkremix();
                } else if (message == 2) {
                    Log.e("fragment_de_old", "message2");
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

        Bitmap bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 688, 540, true);
        Bitmap bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 688, 540, true);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.lazy46);


        //1044*800--->688*540
        //left
        Bitmap bitmapL = Bitmap.createBitmap(836, 678, Bitmap.Config.ARGB_8888);
        Canvas canvasLeft = new Canvas(bitmapL);
        canvasLeft.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft.drawBitmap(bitmapLeft, 74, 55, null);
        canvasLeft.drawBitmap(bitmapDB, 0, 0, null);

        //right
        Bitmap bitmapR = Bitmap.createBitmap(836, 678, Bitmap.Config.ARGB_8888);
        Canvas canvasRight = new Canvas(bitmapR);
        canvasRight.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight.drawBitmap(bitmapRight, 74, 55, null);
        canvasRight.drawBitmap(bitmapDB, 0, 0, null);

        //
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(38);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(38);
        paintRed.setAntiAlias(true);

        Paint paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(38);
        paintBlue.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);
        String time = MainActivity.instance.orderDate_Print;
        //Bitmap bitmapBarCode = BarCodeUtil.creatBarcode(orderItems.get(currentID).order_number.replace("_", ""), 350, 35);

        canvasLeft.drawText("   左",290,640,paintRed);
        canvasLeft.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color,455,640,paint);
        canvasLeft.save();
        canvasLeft.rotate(-70, 678, 446);
        canvasLeft.drawRect(678, 415, 1082, 450, rectPaint);
        canvasLeft.drawText(orderItems.get(currentID).order_number + "     " + time, 678, 446, paint);
        canvasLeft.restore();
        canvasLeft.save();
        canvasLeft.rotate(-110, 178, 430);
        canvasLeft.drawRect(178, 399, 578, 434, rectPaint);
        //canvasLeft.drawBitmap(bitmapBarCode, 178, 399, null);
        canvasLeft.drawText(orderItems.get(currentID).newCode, 178, 430, paintRed);
        canvasLeft.restore();


        canvasRight.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color,290,640,paint);
        canvasRight.drawText("   右", 410,640, paintRed);
        canvasRight.save();
        canvasRight.rotate(-110, 178, 430);
        canvasRight.drawRect(178, 399, 578, 434, rectPaint);
        canvasRight.drawText(orderItems.get(currentID).order_number + "     " + time, 178, 430, paint);
        canvasRight.restore();
        canvasRight.save();
        canvasRight.rotate(-70, 678, 446);
        canvasRight.drawRect(678, 415, 1082, 450, rectPaint);
        //canvasRight.drawBitmap(bitmapBarCode, 678, 415, null);
        canvasRight.drawText(orderItems.get(currentID).newCode, 678, 447, paintRed);
        canvasRight.restore();

        try {
            setScale(orderItems.get(currentID).size);

            Bitmap bitmapCombine = Bitmap.createBitmap(836+30, 1356, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);

            canvasCombine.drawBitmap(bitmapL, 0, 0, null);
            canvasCombine.drawBitmap(bitmapR, 0, 678, null);

            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) ((836 + 36) * scaleX), (int) (1356 * scaleY), true);
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
            BitmapToJpg.save(bitmapPrint, fileSave, 72);

            //释放bitmap
            bitmapDB.recycle();
            bitmapLeft.recycle();
            bitmapRight.recycle();
            bitmapL.recycle();
            bitmapR.recycle();
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
            case 36:
                scaleX = 0.856f;
                scaleY = 0.796f;
                break;
            case 37:
                scaleX = 0.87f;
                scaleY = 0.815f;
                break;
            case 38:
                scaleX = 0.885f;
                scaleY = 0.834f;
                break;
            case 39:
                scaleX = 0.899f;
                scaleY = 0.855f;
                break;
            case 40:
                scaleX = 0.914f;
                scaleY = 0.876f;
                break;
            case 41:
                scaleX = 0.933f;
                scaleY = 0.902f;
                break;
            case 42:
                scaleX = 0.942f;
                scaleY = 0.917f;
                break;
            case 43:
                scaleX = 0.957f;
                scaleY = 0.939f;
                break;
            case 44:
                scaleX = 0.971f;
                scaleY = 0.96f;
                break;
            case 45:
                scaleX = 0.986f;
                scaleY = 0.981f;
                break;
            case 46:
                scaleX = 1.0f;
                scaleY = 1.0f;
                break;
        }
    }

}
