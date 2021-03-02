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

public class FragmentFH extends BaseFragment {
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

    int mianWidth, mainHeight, sideWidth, sideHeight;
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
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(30);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff000000);
        paintBlue.setTextSize(30);
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

    void drawTextMain(Canvas canvas, String LR) {
        canvas.save();
        canvas.rotate(74.6f, 20, 577);
        canvas.drawRect(30, 545, 400, 575, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).order_number, 30, 572, paint);
        canvas.restore();

        canvas.drawRect(554, 1300, 694, 1330, rectPaint);
        canvas.drawText(orderItems.get(currentID).size + "码 " + LR + " " + orderItems.get(currentID).color, 554, 1327, paint);

        canvas.save();
        canvas.rotate(-75f, 1105, 950);
        canvas.drawRect(1105, 920, 1485, 950, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode, 1105, 947, paintRed);
        canvas.restore();
    }

    void drawTextL(Canvas canvas, String LR) {
        canvas.drawRect(720, 565, 1200, 595, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).size + "码 " + LR + " " + orderItems.get(currentID).color, 720, 592, paint);

        canvas.drawRect(270, 630, 640, 660, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode , 270, 657, paintRed);
    }

    void drawTextR(Canvas canvas, String LR) {
        canvas.drawRect(80, 565, 560, 595, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).size + "码 " + LR + " " + orderItems.get(currentID).color, 80, 592, paint);

        canvas.drawRect(645, 630, 1020, 660, rectPaint);
        canvas.drawText(orderItems.get(currentID).newCode, 645, 657, paintRed);
    }
    public void remixx(){
        //
        setScale(orderItems.get(currentID).size);

        Bitmap bitmapDB_main = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fh_main);
        Bitmap bitmapDB_l = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fh_side_l);
        Bitmap bitmapDB_r = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fh_side_r);

        //left
        Bitmap bitmapLeft_main = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 582, 753, 1235, 1401);
        Bitmap bitmapLL_cut = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1535, 50, 847, 1188);
        Bitmap bitmapLR_cut = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 18, 48, 847, 1188);

        Canvas canvasLeft_main = new Canvas(bitmapLeft_main);
        canvasLeft_main.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft_main.drawBitmap(bitmapDB_main, 0, 0, null);

        Bitmap bitmapLL = Bitmap.createBitmap(1289, 690, Bitmap.Config.ARGB_8888);
        Canvas canvasLL = new Canvas(bitmapLL);
        canvasLL.drawColor(0xffffffff);
        canvasLL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Matrix matrix = new Matrix();
        matrix.postRotate(68.2f);
        matrix.postTranslate(1080, -250);
        canvasLL.drawBitmap(bitmapLL_cut, matrix, null);
        canvasLL.drawBitmap(bitmapDB_l, 0, 0, null);
        bitmapLL_cut.recycle();

        Bitmap bitmapLR = Bitmap.createBitmap(1289, 690, Bitmap.Config.ARGB_8888);
        Canvas canvasLR = new Canvas(bitmapLR);
        canvasLR.drawColor(0xffffffff);
        canvasLR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();
        matrix.postRotate(-68.2f);
        matrix.postTranslate(-107, 538);
        canvasLR.drawBitmap(bitmapLR_cut, matrix, null);
        canvasLR.drawBitmap(bitmapDB_r, 0, 0, null);
        bitmapLR_cut.recycle();

        //right
        Bitmap bitmapRight_main = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 582, 753, 1235, 1401);
        Bitmap bitmapRL_cut = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1535, 50, 847, 1188);
        Bitmap bitmapRR_cut = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 18, 48, 847, 1188);

        Canvas canvasRight_main = new Canvas(bitmapRight_main);
        canvasRight_main.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight_main.drawBitmap(bitmapDB_main, 0, 0, null);

        Bitmap bitmapRL = Bitmap.createBitmap(1289, 690, Bitmap.Config.ARGB_8888);
        Canvas canvasRL = new Canvas(bitmapRL);
        canvasRL.drawColor(0xffffffff);
        canvasRL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();
        matrix.postRotate(68.2f);
        matrix.postTranslate(1080, -250);
        canvasRL.drawBitmap(bitmapRL_cut, matrix, null);
        canvasRL.drawBitmap(bitmapDB_l, 0, 0, null);
        bitmapRL_cut.recycle();

        Bitmap bitmapRR = Bitmap.createBitmap(1289, 690, Bitmap.Config.ARGB_8888);
        Canvas canvasRR = new Canvas(bitmapRR);
        canvasRR.drawColor(0xffffffff);
        canvasRR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();
        matrix.postRotate(-68.2f);
        matrix.postTranslate(-107, 538);
        canvasRR.drawBitmap(bitmapRR_cut, matrix, null);
        canvasRR.drawBitmap(bitmapDB_r, 0, 0, null);
        bitmapRR_cut.recycle();

        bitmapDB_main.recycle();
        bitmapDB_l.recycle();
        bitmapDB_r.recycle();

        //drawText
        drawTextMain(canvasLeft_main, "左");
        drawTextMain(canvasRight_main, "右");
        drawTextL(canvasLL, "左");
        drawTextL(canvasRL, "右");
        drawTextR(canvasLR, "左");
        drawTextR(canvasRR, "右");

        bitmapLeft_main = Bitmap.createScaledBitmap(bitmapLeft_main, mianWidth, mainHeight, true);
        bitmapLL = Bitmap.createScaledBitmap(bitmapLL, sideWidth, sideHeight, true);
        bitmapLR = Bitmap.createScaledBitmap(bitmapLR, sideWidth, sideHeight, true);
        bitmapRight_main = Bitmap.createScaledBitmap(bitmapRight_main, mianWidth, mainHeight, true);
        bitmapRL = Bitmap.createScaledBitmap(bitmapRL, sideWidth, sideHeight, true);
        bitmapRR = Bitmap.createScaledBitmap(bitmapRR, sideWidth, sideHeight, true);

        try {
            Bitmap bitmapCombine;
            bitmapCombine = Bitmap.createBitmap(mianWidth * 2 + sideWidth * 2 + 180, mainHeight + 60, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.drawColor(0xffffffff);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

            canvasCombine.drawBitmap(bitmapLeft_main, 0, 0, null);
            canvasCombine.drawBitmap(bitmapLL, mianWidth + 60, 0, null);
            canvasCombine.drawBitmap(bitmapLR, mianWidth + 60, sideHeight, null);
            canvasCombine.drawBitmap(bitmapRight_main, mianWidth + sideWidth + 120, 0, null);
            canvasCombine.drawBitmap(bitmapRL, mianWidth * 2 + sideWidth + 180, 0, null);
            canvasCombine.drawBitmap(bitmapRR, mianWidth * 2 + sideWidth + 180, sideHeight, null);

            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku + orderItems.get(currentID).size + "_" : "";
            String nameCombine = noNewCode + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).newCode + orderItems.get(currentID).color + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 149);

            //释放bitmap
            bitmapLeft_main.recycle();
            bitmapRight_main.recycle();
            bitmapLL.recycle();
            bitmapLR.recycle();
            bitmapRL.recycle();
            bitmapRR.recycle();
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
            case 35:
                mianWidth = 1165;
                mainHeight = 1263;
                sideWidth = 1120;
                sideHeight = 626;
                break;
            case 36:
                mianWidth = 1184;
                mainHeight = 1290;
                sideWidth = 1148;
                sideHeight = 637;
                break;
            case 37:
                mianWidth = 1203;
                mainHeight = 1320;
                sideWidth = 1179;
                sideHeight = 646;
                break;
            case 38:
                mianWidth = 1223;
                mainHeight = 1350;
                sideWidth = 1207;
                sideHeight = 657;
                break;
            case 39:
                mianWidth = 1241;
                mainHeight = 1376;
                sideWidth = 1241;
                sideHeight = 670;
                break;
            case 40:
                mianWidth = 1262;
                mainHeight = 1408;
                sideWidth = 1269;
                sideHeight = 682;
                break;
            case 41:
                mianWidth = 1280;
                mainHeight = 1440;
                sideWidth = 1300;
                sideHeight = 694;
                break;
            case 42:
                mianWidth = 1299;
                mainHeight = 1472;
                sideWidth = 1327;
                sideHeight = 700;
                break;
            case 43:
                mianWidth = 1319;
                mainHeight = 1504;
                sideWidth = 1358;
                sideHeight = 713;
                break;
            case 44:
                mianWidth = 1337;
                mainHeight = 1535;
                sideWidth = 1389;
                sideHeight = 724;
                break;
            case 45:
                mianWidth = 1356;
                mainHeight = 1567;
                sideWidth = 1419;
                sideHeight = 736;
                break;
            case 46:
                mianWidth = 1376;
                mainHeight = 1600;
                sideWidth = 1447;
                sideHeight = 745;
                break;
            case 47:
                mianWidth = 1425;
                mainHeight = 1632;
                sideWidth = 1477;
                sideHeight = 760;
                break;
            case 48:
                mianWidth = 1451;
                mainHeight = 1664;
                sideWidth = 1506;
                sideHeight = 769;
                break;
        }
    }

}
