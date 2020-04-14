package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
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

public class FragmentHU extends BaseFragment {
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

    int num;
    String strPlus = "";
    int intPlus = 1;
    boolean sizeOK = true;

    Paint rectPaint, paint, paintRed, paintBlue, rectBorderPaint, paintSmall, rectBorderPaintRed,rectBorderPaintGreen;
    String time;

    int widthCombine, heightCombine;
    int dpi;
    Rect rectCutMain,rectCutSideL,rectCutSideR,rectCutFrontSideL,rectCutFrontSideR,rectCutBackSideL, rectCutBackSideR;
    Rect rectDrawMain,rectDrawSideL,rectDrawSideR,rectDrawFrontSideL,rectDrawFrontSideR,rectDrawBackSideL, rectDrawBackSideR;

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

        //paint
        rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        rectBorderPaint = new Paint();
        rectBorderPaint.setColor(0xff000000);
        rectBorderPaint.setStyle(Paint.Style.STROKE);
        rectBorderPaint.setStrokeWidth(10);

        rectBorderPaintRed = new Paint();
        rectBorderPaintRed.setColor(0xffff0000);
        rectBorderPaintRed.setStyle(Paint.Style.FILL);

        rectBorderPaintGreen = new Paint();
        rectBorderPaintGreen.setColor(0xff00ff00);
        rectBorderPaintGreen.setStyle(Paint.Style.FILL);

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(23);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(22);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(22);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        paintSmall = new Paint();
        paintSmall.setColor(0xff000000);
        paintSmall.setTextSize(22);
        paintSmall.setTypeface(Typeface.DEFAULT_BOLD);
        paintSmall.setAntiAlias(true);

        time = MainActivity.instance.orderDate_Print;

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_pillow.setImageDrawable(null);
                    bt_remix.setClickable(false);
                } else if (message == 1) {
                    Log.e("fragment2", "message1");
                    bt_remix.setClickable(true);
                    checkremix();
                } else if (message == 2) {
                    Log.e("fragment2", "message2");
                    bt_remix.setClickable(true);
                    checkremix();
                } else if (message == 4) {
                    Log.e("fy", "message4");
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
        bt_remix.setClickable(false);
    }

    public void remix(){
        setSize();

        new Thread(){
            @Override
            public void run() {
                super.run();

                if (sizeOK) {
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

            }
        }.start();

    }

    void drawText(Canvas canvas) {
        canvas.drawRect(1000, 10, 1000 + 700, 10 + 23, rectPaint);
        canvas.drawText("HU沙发套-" + orderItems.get(currentID).sizeStr + " - " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1000, 10 + 23 - 2, paint);
    }
    void drawText2(Canvas canvas) {
        canvas.drawRect(500, 5, 500 + 500, 5 + 25, rectPaint);
        canvas.drawText("狗狗坐垫 第4片 " + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number, 500, 5 + 23, paint);
    }
    void drawText345(Canvas canvas) {
        canvas.drawRect(50, 5, 50 + 500, 5 + 25, rectPaint);
        canvas.drawText("狗狗坐垫 第5片 " + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number, 50, 5 + 23, paint);
    }

    public void remixx(){
        orderItems.get(currentID).newCodeStr = orderItems.get(currentID).newCodeStr.replace("\" ", "-");
        int margin = 100;
        Bitmap bitmapCombine = Bitmap.createBitmap(widthCombine, heightCombine, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        if (orderItems.get(currentID).sku.equals("HU1")) {
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutMain, rectDrawMain, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutSideL, rectDrawSideL, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutSideR, rectDrawSideR, null);

            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu1_main);
            canvasCombine.drawBitmap(bitmapDB, new Rect(0, 0, bitmapDB.getWidth(), bitmapDB.getHeight()), rectDrawMain, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu1_side_l);
            canvasCombine.drawBitmap(bitmapDB, rectDrawSideL.left, rectDrawSideL.top, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu1_side_r);
            canvasCombine.drawBitmap(bitmapDB, rectDrawSideR.left, rectDrawSideR.top, null);
        } else if (orderItems.get(currentID).sku.equals("HU2")) {
            //mian
            Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 18, 5, 4675, 5525);
            Canvas canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_main);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            canvasCombine.drawBitmap(bitmapTemp, 0, 9830, null);
            //back
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4916, 141, 3256, 9446);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_back);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            canvasCombine.drawBitmap(bitmapTemp, 4128, 141, null);
            //smallSideL
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 10522, 2670, 1366, 2554);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_smallside_l);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            canvasCombine.drawBitmap(bitmapTemp, 6105, 9830, null);
            //smallSideR
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 8750, 2670, 1366, 2554);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_smallside_r);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            canvasCombine.drawBitmap(bitmapTemp, 4677, 9830, null);
            //pocket
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 9566, 7456, 1108, 1160);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_pocket);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            canvasCombine.drawBitmap(bitmapTemp, 4928, 12603, null);
            //sideL
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 8281, 5631, 4793, 3956);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_side_l);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            matrix.postTranslate(3956 + 76, 0);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            //sideR
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 18, 5631, 4793, 3956);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu2_side_r);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            matrix.reset();
            matrix.postRotate(90);
            matrix.postTranslate(3956 + 85, 5000 - 80);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            bitmapTemp.recycle();

        } else {
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutMain, rectDrawMain, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutSideL, rectDrawSideL, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutSideR, rectDrawSideR, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutBackSideL, rectDrawBackSideL, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutBackSideR, rectDrawBackSideR, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutFrontSideL, rectDrawFrontSideL, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapPillow, rectCutFrontSideR, rectDrawFrontSideR, null);

            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu4_main);
            canvasCombine.drawBitmap(bitmapDB, new Rect(0, 0, bitmapDB.getWidth(), bitmapDB.getHeight()), rectDrawMain, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu3_side_l);
            canvasCombine.drawBitmap(bitmapDB, rectDrawSideL.left, rectDrawSideL.top, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu3_side_r);
            canvasCombine.drawBitmap(bitmapDB, rectDrawSideR.left, rectDrawSideR.top, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu3_back_side_l);
            canvasCombine.drawBitmap(bitmapDB, rectDrawBackSideL.left, rectDrawBackSideL.top, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu3_back_side_r);
            canvasCombine.drawBitmap(bitmapDB, rectDrawBackSideR.left, rectDrawBackSideR.top, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu3_front_side_l);
            canvasCombine.drawBitmap(bitmapDB, rectDrawFrontSideL.left, rectDrawFrontSideL.top, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hu3_front_side_r);
            canvasCombine.drawBitmap(bitmapDB, rectDrawFrontSideR.left, rectDrawFrontSideR.top, null);
            bitmapDB.recycle();

        }
        drawText(canvasCombine);

        //save
        String nameCombine = orderItems.get(currentID).newCodeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
        String pathSave;
        if(MainActivity.instance.cb_classify.isChecked()){
            pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
        } else
            pathSave = sdCardPath + "/生产图/" + childPath + "/";
        if(!new File(pathSave).exists())
            new File(pathSave).mkdirs();
        File fileSave = new File(pathSave + nameCombine);
        BitmapToJpg.save(bitmapCombine, fileSave, dpi);


        try {
//            matrix.reset();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku);
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
            if (MainActivity.instance.bitmapPillow != null) {
                MainActivity.instance.bitmapPillow.recycle();
            }
            if (MainActivity.instance.bitmapLeft != null) {
                MainActivity.instance.bitmapLeft.recycle();
                MainActivity.instance.bitmapRight.recycle();
            }

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
        if (orderItems.get(currentID).img_left == null) {
            if (MainActivity.instance.tb_auto.isChecked()) {
                remix();
            }
        } else {
            if (MainActivity.instance.tb_auto.isChecked()) {
                if (MainActivity.instance.leftsucceed && MainActivity.instance.rightsucceed) {
                    remix();
                }
            }
        }
    }

    void setSize() {
        dpi = 120;
        switch (orderItems.get(currentID).sku) {
            case "HU1":
                dpi = 150;

                rectCutMain = new Rect(925, 43, 925 + 2439, 43 + 9606);
                rectCutSideL = new Rect(3457, 5866, 3457 + 835, 5866 + 2365);
                rectCutSideR = new Rect(0, 5866, 0 + 835, 5866 + 2365);

                rectDrawMain = new Rect(0, 0, 0 + 2380, 0 + 9606);
                rectDrawSideL = new Rect(1328, 9733, 1328 + 835, 9733 + 2365);
                rectDrawSideR = new Rect(270, 9733, 270 + 835, 9733 + 2365);

                widthCombine = 2380;
                heightCombine = 12120;
                break;
            case "HU2":
                widthCombine = 7527;
                heightCombine = 15362;
                break;
            case "HU3":
                rectCutMain = new Rect(25, 13, 25 + 3475, 13 + 11768);
                rectCutSideL = new Rect(3610, 2282, 3610 + 2577, 2282 + 4643);
                rectCutSideR = new Rect(3610, 7138, 3610 + 2577, 7138 + 4643);
                rectCutBackSideL = new Rect(7243, 7682, 7243 + 876, 7682 + 4099);
                rectCutBackSideR = new Rect(6297, 7682, 6297 + 876, 7682 + 4099);
                rectCutFrontSideL = new Rect(7292, 3771, 7292 + 877, 3771 + 3154);
                rectCutFrontSideR = new Rect(6297, 3771, 6297 + 877, 3771 + 3154);

                rectDrawMain = new Rect(25, 0, 25 + 3475, 0 + 11768);
                rectDrawSideL = new Rect(3610, 13, 3610 + 2577, 13 + 4643);
                rectDrawSideR = new Rect(3610, 4704, 3610 + 2577, 4704 + 4643);
                rectDrawBackSideL = new Rect(6277, 13, 6277 + 876, 13 + 4099);
                rectDrawBackSideR = new Rect(6277, 4347, 6277 + 876, 4347 + 4099);
                rectDrawFrontSideL = new Rect(4607, 9407, 4607 + 877, 9407 + 3154);
                rectDrawFrontSideR = new Rect(3612, 9407, 3612 + 877, 9407 + 3154);

                widthCombine = 7225;
                heightCombine = 12567;
                break;
            case "HU4":
                rectCutMain = new Rect(0, 0, 0 + 6127, 0 + 11768);
                rectCutSideL = new Rect(6221, 2350, 6221 + 2577, 2350 + 4643);
                rectCutSideR = new Rect(6221, 7125, 6221 + 2577, 7125 + 4643);
                rectCutBackSideL = new Rect(9847, 7669, 9847 + 876, 7669 + 4099);
                rectCutBackSideR = new Rect(8880, 7669, 8880 + 876, 7669 + 4099);
                rectCutFrontSideL = new Rect(9847, 3839, 9847 + 877, 3839 + 3154);
                rectCutFrontSideR = new Rect(8898, 3839, 8898 + 877, 3839 + 3154);

                rectDrawMain = new Rect(0, 0, 0 + 6127, 0 + 11768);
                rectDrawSideL = new Rect(0, 11819, 0 + 2577, 11819 + 4643);
                rectDrawSideR = new Rect(2646, 11819, 2646 + 2577, 11819 + 4643);
                rectDrawBackSideL = new Rect(6184, 10559, 6184 + 876, 10559 + 4099);
                rectDrawBackSideR = new Rect(6184, 6405, 6184 + 876, 6405 + 4099);
                rectDrawFrontSideL = new Rect(6183, 3197, 6183 + 877, 3197 + 3154);
                rectDrawFrontSideR = new Rect(6183, 0, 6183 + 877, 0 + 3154);

                widthCombine = 7146;
                heightCombine = 16561;
                break;
            case "HU5":
                rectCutMain = new Rect(0, 0, 0 + 7733, 0 + 11768);
                rectCutSideL = new Rect(7811, 2135, 7811 + 2577, 2135 + 4643);
                rectCutSideR = new Rect(7811, 7125, 7811 + 2577, 7125 + 4643);
                rectCutBackSideL = new Rect(11450, 7669, 11450 + 876, 7669 + 4099);
                rectCutBackSideR = new Rect(10486, 7669, 10486 + 876, 7669 + 4099);
                rectCutFrontSideL = new Rect(11450, 3623, 11450 + 877, 3623 + 3154);
                rectCutFrontSideR = new Rect(10484, 3623, 10484 + 877, 3623 + 3154);

                rectDrawMain = new Rect(0, 0, 0 + 7733, 0 + 11768);
                rectDrawSideL = new Rect(0, 11834, 0 + 2577, 11834 + 4643);
                rectDrawSideR = new Rect(2677, 11834, 2677 + 2577, 11834 + 4643);
                rectDrawBackSideL = new Rect(6230, 11834, 6230 + 876, 11834 + 4099);
                rectDrawBackSideR = new Rect(5304, 11834, 5304 + 876, 11834 + 4099);
                rectDrawFrontSideL = new Rect(6242, 15996, 6242 + 877, 15996 + 3154);
                rectDrawFrontSideR = new Rect(5304, 15996, 5304 + 877, 15996 + 3154);

                widthCombine = 7733;
                heightCombine = 19233;
                break;
            default:
                sizeOK = false;
                break;
        }
    }

}
