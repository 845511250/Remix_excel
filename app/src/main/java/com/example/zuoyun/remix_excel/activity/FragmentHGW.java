package com.example.zuoyun.remix_excel.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class FragmentHGW extends BaseFragment {
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

    int width_front, width_back, width_pocket,width_borderTop,width_borderPocket;
    int height_front,height_back,height_pocket,height_borderTop,height_borderPocket;
    int id_frontR,id_backL;

    int num;
    String strPlus = "";
    int intPlus = 1;
    boolean sizeOK = true;

    Paint rectPaint, paint, paintRed, paintBlue, rectBorderPaint, paintSmall;
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

        //paint
        rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        rectBorderPaint = new Paint();
        rectBorderPaint.setColor(0xff000000);
        rectBorderPaint.setStyle(Paint.Style.STROKE);
        rectBorderPaint.setStrokeWidth(2);

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(22);
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
        setSize(orderItems.get(currentID).sizeStr);

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

    void drawTextFrontR(Canvas canvas) {
        canvas.save();
        canvas.rotate(2.4f, 827, 18);
        canvas.drawRect(827, 18, 827 + 500, 18 + 22, rectPaint);
        canvas.drawText(time + "  HGW女" + orderItems.get(currentID).sizeStr + "  前右  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 827, 18 + 20, paint);
        canvas.restore();
    }
    void drawTextFrontL(Canvas canvas) {
        canvas.save();
        canvas.rotate(-2.4f, 541, 41);
        canvas.drawRect(541, 41, 541 + 500, 41 + 22, rectPaint);
        canvas.drawText(time + "  HGW女" + orderItems.get(currentID).sizeStr + "  前左  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 541, 41 + 20, paint);
        canvas.restore();
    }
    void drawTextBackL(Canvas canvas) {
        if (orderItems.get(currentID).sizeStr.equals("3XL") || orderItems.get(currentID).sizeStr.equals("4XL")) {
            canvas.save();
            canvas.rotate(-4.5f, 333, 158);
            canvas.drawRect(333, 158, 333 + 500, 158 + 22, rectPaint);
            canvas.drawText(time + "  HGW女" + orderItems.get(currentID).sizeStr + "  后左  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 333, 158 + 20, paint);
            canvas.restore();
        } else {
            canvas.save();
            canvas.rotate(-10f, 830, 158);
            canvas.drawRect(830, 158, 830 + 500, 158 + 22, rectPaint);
            canvas.drawText(time + "  HGW女" + orderItems.get(currentID).sizeStr + "  后左  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 830, 158 + 20, paint);
            canvas.restore();
        }
    }
    void drawTextBackR(Canvas canvas) {
        if (orderItems.get(currentID).sizeStr.equals("3XL") || orderItems.get(currentID).sizeStr.equals("4XL")) {
            canvas.save();
            canvas.rotate(4.4f, 1628, 120);
            canvas.drawRect(1628, 120, 1628 + 500, 120 + 22, rectPaint);
            canvas.drawText(time + "  HGW女" + orderItems.get(currentID).sizeStr + "  后右  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1628, 120 + 20, paint);
            canvas.restore();
        } else {
            canvas.save();
            canvas.rotate(10f, 1019, 53);
            canvas.drawRect(1019, 53, 1019 + 500, 53 + 22, rectPaint);
            canvas.drawText(time + "  HGW女" + orderItems.get(currentID).sizeStr + "  后右  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1019, 53 + 20, paint);
            canvas.restore();
        }
    }
    void drawTextPocketR(Canvas canvas) {
        canvas.save();
        canvas.rotate(6.2f, 518, 60);
        canvas.drawRect(518, 60, 518 + 400, 60 + 22, rectPaint);
        canvas.drawText(time + "  HGW_" + orderItems.get(currentID).sizeStr + "  右  " + orderItems.get(currentID).order_number, 518, 60 + 20, paint);
        canvas.restore();
    }
    void drawTextPocketL(Canvas canvas) {
        canvas.save();
        canvas.rotate(-5.8f, 438, 69);
        canvas.drawRect(438, 69, 438 + 400, 69 + 22, rectPaint);
        canvas.drawText(time + "  HGW_" + orderItems.get(currentID).sizeStr + "  左  " + orderItems.get(currentID).order_number, 438, 69 + 20, paint);
        canvas.restore();
    }
    void drawTextBorderTop(Canvas canvas) {
        canvas.drawRect(100, 10, 100 + 400, 10 + 22, rectPaint);
        canvas.drawText(orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number, 100, 10 + 20, paint);
    }
    void drawTextborderPocketL(Canvas canvas) {
        canvas.save();
        canvas.rotate(4.1f, 686, 7);
        canvas.drawRect(686, 7, 686 + 230, 7 + 22, rectPaint);
        canvas.drawText(time + " 左袋边 " + orderItems.get(currentID).order_number, 686, 7 + 20, paint);
        canvas.restore();
    }
    void drawTextborderPocketR(Canvas canvas) {
        canvas.save();
        canvas.rotate(-4.1f, 27, 23);
        canvas.drawRect(27, 23, 27 + 230, 23 + 22, rectPaint);
        canvas.drawText(time + " 右袋边 " + orderItems.get(currentID).order_number, 27, 23 + 20, paint);
        canvas.restore();
    }


    public void remixx(){
        int margin = 120;
        Matrix matrix = new Matrix();
        int combineWidth = width_front + width_back + height_borderTop + margin * 2;
        int combineHeight = Math.max(width_borderTop, height_front + height_pocket + height_back + margin * 2);
        if (orderItems.get(currentID).sizeStr.equals("4XL")) {
            combineWidth = width_front + width_back + height_borderTop + margin * 2 - 350;
        }

        Bitmap bitmapF = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapRight;
        Bitmap bitmapB = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapLeft;

        Bitmap bitmapCombine = Bitmap.createBitmap(combineWidth, combineHeight, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //front
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 552, 747, 1898, 1923);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_frontR);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextFrontR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);

        bitmapTemp = Bitmap.createBitmap(bitmapF, 2450, 746, 1898, 1923);
        canvasTemp = new Canvas(bitmapTemp);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, bitmapDB.getWidth(), bitmapDB.getHeight(), matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextFrontL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, 0, null);

        //backL
        bitmapTemp = Bitmap.createBitmap(2438, 2315, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();
        matrix.postTranslate(-10, -481);
        matrix.postRotate(-8.3f);
        matrix.postTranslate(-232, 282);
        canvasTemp.drawBitmap(bitmapB, matrix, null);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_backL);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextBackL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        if (orderItems.get(currentID).sizeStr.equals("4XL")) {
            canvasCombine.drawBitmap(bitmapTemp, 0, height_front + height_back + margin * 3, null);
        } else {
            canvasCombine.drawBitmap(bitmapTemp, 0, height_front + height_pocket + margin * 2, null);
        }


        //backR
        bitmapTemp = Bitmap.createBitmap(2438, 2315, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();
        matrix.postTranslate(-2450, -481);
        matrix.postRotate(8.3f);
        matrix.postTranslate(252, -71);
        canvasTemp.drawBitmap(bitmapB, matrix, null);

        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, bitmapDB.getWidth(), bitmapDB.getHeight(), matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextBackR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        if (orderItems.get(currentID).sizeStr.equals("4XL")) {
            canvasCombine.drawBitmap(bitmapTemp, width_front + margin - 350, height_front + margin, null);
        } else {
            canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_front + margin, null);
        }

        //pocketR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 556, 685, 1079, 1298);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgw_pocket_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocketR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front, null);

        //pocketL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 3269, 685, 1079, 1298);
        canvasTemp = new Canvas(bitmapTemp);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, bitmapDB.getWidth(), bitmapDB.getHeight(), matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocketL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_pocket + margin, height_front + margin, null);

        //borderPocketR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 552, 746, 936, 736);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgw_border_pocket_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapTemp = Bitmap.createBitmap(bitmapTemp, 0, 0, 936, 736, matrix, true);
        drawTextborderPocketR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_borderPocket, height_borderPocket, true);
        canvasCombine.drawBitmap(bitmapTemp, combineWidth - width_borderPocket - height_borderTop - margin, combineHeight - height_borderPocket, null);

        //borderPocketL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 3411, 746, 937, 736);
        canvasTemp = new Canvas(bitmapTemp);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, bitmapDB.getWidth(), bitmapDB.getHeight(), matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapTemp = Bitmap.createBitmap(bitmapTemp, 0, 0, 936, 736, matrix, true);
        drawTextborderPocketL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_borderPocket, height_borderPocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_back + margin, height_front + height_back + margin * 2, null);

        //borderTop
        bitmapTemp = Bitmap.createBitmap(1615 * 2 + 1368 * 2, 562, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        Bitmap bitmapCut = Bitmap.createBitmap(bitmapB, 2912, 32, 1615, 562);
        canvasTemp.drawBitmap(bitmapCut, 0, 0, null);

        bitmapCut = Bitmap.createBitmap(1368, 562, Bitmap.Config.ARGB_8888);
        Canvas canvasCut = new Canvas(bitmapCut);
        matrix.reset();
        matrix.postRotate(-4f);
        matrix.postTranslate(-40, 0);
        canvasCut.drawBitmap(Bitmap.createBitmap(bitmapF, 674, 230, 1405, 662), matrix, null);
        canvasTemp.drawBitmap(bitmapCut, 1615, 0, null);

        bitmapCut = Bitmap.createBitmap(1368, 562, Bitmap.Config.ARGB_8888);
        canvasCut = new Canvas(bitmapCut);
        matrix.reset();
        matrix.postRotate(4f);
        matrix.postTranslate(6, -100);
        canvasCut.drawBitmap(Bitmap.createBitmap(bitmapF, 2823, 230, 1405, 662), matrix, null);
        canvasTemp.drawBitmap(bitmapCut, 1615 + 1368, 0, null);

        bitmapCut = Bitmap.createBitmap(bitmapB, 375, 32, 1615, 562);
        canvasTemp.drawBitmap(bitmapCut, 1615 + 1368 * 2, 0, null);
        bitmapCut.recycle();
        canvasTemp.drawRect(0, 0, 1615 * 2 + 1368 * 2, 562, rectBorderPaint);

        drawTextBorderTop(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_borderTop, height_borderTop, true);
        matrix.reset();
        matrix.postRotate(90);
        if (orderItems.get(currentID).sizeStr.equals("4XL")) {
            matrix.postTranslate(height_borderTop + width_front + width_back + margin * 2 - 350, 0);
        } else {
            matrix.postTranslate(height_borderTop + width_front + width_back + margin * 2, 0);
        }
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();




        String nameCombine = orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
        String pathSave;
        if(MainActivity.instance.cb_classify.isChecked()){
            pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
        } else
            pathSave = sdCardPath + "/生产图/" + childPath + "/";
        if(!new File(pathSave).exists())
            new File(pathSave).mkdirs();
        File fileSave = new File(pathSave + nameCombine);
        BitmapToJpg.save(bitmapCombine, fileSave, 149);
        bitmapCombine.recycle();




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

    void setSize(String size) {
        switch (size) {
            case "XS":
                width_front = 1631;
                height_front = 1745;
                width_back = 2171;
                height_back = 2140;
                width_pocket = 1037;
                height_pocket = 1244;
                width_borderTop = 4984;
                height_borderTop = 562;
                width_borderPocket = 907;
                height_borderPocket = 706;
                id_frontR = R.drawable.hgw_front_r_s;
                id_backL = R.drawable.hgw_back_l_s;
                break;
            case "S":
                width_front = 1750;
                height_front = 1804;
                width_back = 2290;
                height_back = 2199;
                width_pocket = 1037;
                height_pocket = 1244;
                width_borderTop = 5457;
                height_borderTop = 562;
                width_borderPocket = 907;
                height_borderPocket = 706;
                id_frontR = R.drawable.hgw_front_r_s;
                id_backL = R.drawable.hgw_back_l_s;
                break;
            case "M":
                width_front = 1868;
                height_front = 1864;
                width_back = 2408;
                height_back = 2256;
                width_pocket = 1079;
                height_pocket = 1298;
                width_borderTop = 5929;
                height_borderTop = 562;
                width_borderPocket = 937;
                height_borderPocket = 736;
                id_frontR = R.drawable.hgw_front_r_s;
                id_backL = R.drawable.hgw_back_l_s;
                break;
            case "L":
                width_front = 1958;
                height_front = 1923;
                width_back = 2498;
                height_back = 2315;
                width_pocket = 1079;
                height_pocket = 1298;
                width_borderTop = 6283;
                height_borderTop = 562;
                width_borderPocket = 936;
                height_borderPocket = 736;
                id_frontR = R.drawable.hgw_front_r_xl;
                id_backL = R.drawable.hgw_back_l_xl;
                break;
            case "XL":
                width_front = 2077;
                height_front = 2012;
                width_back = 2617;
                height_back = 2408;
                width_pocket = 1127;
                height_pocket = 1383;
                width_borderTop = 6756;
                height_borderTop = 562;
                width_borderPocket = 965;
                height_borderPocket = 765;
                id_frontR = R.drawable.hgw_front_r_xl;
                id_backL = R.drawable.hgw_back_l_xl;
                break;
            case "2XL":
                width_front = 2166;
                height_front = 2101;
                width_back = 2706;
                height_back = 2499;
                width_pocket = 1134;
                height_pocket = 1413;
                width_borderTop = 7110;
                height_borderTop = 562;
                width_borderPocket = 965;
                height_borderPocket = 765;
                id_frontR = R.drawable.hgw_front_r_xl;
                id_backL = R.drawable.hgw_back_l_xl;
                break;
            case "3XL":
                width_front = 2374;
                height_front = 2249;
                width_back = 2914;
                height_back = 2588;
                width_pocket = 1153;
                height_pocket = 1502;
                width_borderTop = 7937;
                height_borderTop = 562;
                width_borderPocket = 965;
                height_borderPocket = 819;
                id_frontR = R.drawable.hgw_front_r_3xl;
                id_backL = R.drawable.hgw_back_l_3xl;
                break;
            case "4XL":
                width_front = 2527;
                height_front = 2344;
                width_back = 3071;
                height_back = 2678;
                width_pocket = 1160;
                height_pocket = 1533;
                width_borderTop = 8527;
                height_borderTop = 562;
                width_borderPocket = 965;
                height_borderPocket = 819;
                id_frontR = R.drawable.hgw_front_r_3xl;
                id_backL = R.drawable.hgw_back_l_3xl;
                break;
            default:
                showDialogSizeWrong(orderItems.get(currentID).order_number);
                sizeOK = false;
                break;
        }
    }

    public void showDialogSizeWrong(final String order_number){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog dialog_finish;
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
                dialog_finish = builder.create();
                dialog_finish.setCancelable(false);
                dialog_finish.show();
                View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_finish, null);
                dialog_finish.setContentView(view_dialog);
                TextView tv_title = (TextView) view_dialog.findViewById(R.id.tv_dialog_title);
                TextView tv_content = (TextView) view_dialog.findViewById(R.id.tv_dialog_content);
                Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);

                tv_title.setText("错误！");
                tv_content.setText("单号："+order_number+"读取尺码失败");
                bt_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_finish.dismiss();
                        getActivity().finish();
                    }
                });
            }
        });
    }

}
