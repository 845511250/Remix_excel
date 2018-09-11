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

public class FragmentGS100 extends BaseFragment {
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

    int width_front, width_back, width_arm, width_maozi, width_xiabai, width_pocket, width_xiukou;
    int height_front,height_back,height_arm,height_maozi,height_xiabai,height_pocket,height_xiukou;
    int width_cut, height_cut;

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
        paint.setTextSize(26);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(26);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(26);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        paintSmall = new Paint();
        paintSmall.setColor(0xff000000);
        paintSmall.setTextSize(26);
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
                    Log.e("jiake", "message1");
                    bt_remix.setClickable(true);
                    checkremix();
                } else if(message==2){
                    Log.e("jiake", "message2");
                    bt_remix.setClickable(true);
                    checkremix();
                } else if (message == 4) {
                    Log.e("jiake", "message4");
//                    if(!MainActivity.instance.cb_fastmode.isChecked())
//                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapPillow);
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
        new Thread(){
            @Override
            public void run() {
                super.run();
                setScale(orderItems.get(currentID).sizeStr);

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

    void drawTextFront(Canvas canvas) {
        canvas.drawRect(1400, 4797 - 25, 1400 + 500, 4797, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + " " + orderItems.get(currentID).newCode, 1400, 4797 - 2, paint);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1400, 4858 - 25, 1400 + 500, 4858, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + " " + orderItems.get(currentID).newCode, 1400, 4858 - 2, paint);
    }
    void drawTextXiuziL(Canvas canvas) {
        canvas.drawRect(1185, 14, 1185 + 30, 14 + 25, rectPaint);
        canvas.drawText("左", 1185, 14 + 23, paint);

        canvas.drawRect(1000, 3302 - 25, 1000 + 500, 3302, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 3302 - 2, paint);
    }
    void drawTextXiuziR(Canvas canvas) {
        canvas.drawRect(1185, 14, 1185 + 30, 14 + 25, rectPaint);
        canvas.drawText("右", 1185, 14 + 23, paint);

        canvas.drawRect(1000, 3302 - 25, 1000 + 500, 3302, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 3302 - 2, paint);
    }
    void drawTextMaoziInL(Canvas canvas) {
        canvas.save();
        canvas.rotate(6, 1291, 22);
        canvas.drawRect(1291, 22, 1291 + 400, 22 + 25, rectPaint);
        canvas.drawText("内左" + orderItems.get(currentID).sizeStr + " " + time + " " + orderItems.get(currentID).order_number, 1291, 22 + 23, paint);
        canvas.restore();
    }
    void drawTextMaoziInR(Canvas canvas) {
        canvas.save();
        canvas.rotate(-6, 108, 65);
        canvas.drawRect(108, 65, 108 + 400, 65 + 25, rectPaint);
        canvas.drawText("内右" + orderItems.get(currentID).sizeStr + " " + time + " " + orderItems.get(currentID).order_number, 108, 65 + 23, paint);
        canvas.restore();
    }
    void drawTextMaoziOutL(Canvas canvas) {
        canvas.save();
        canvas.rotate(-6.4f, 137, 73);
        canvas.drawRect(137, 73, 137 + 400, 73 + 25, rectPaint);
        canvas.drawText("外左" + orderItems.get(currentID).sizeStr + " " + time + " " + orderItems.get(currentID).order_number, 137, 73 + 23, paint);
        canvas.restore();
    }
    void drawTextMaoziOutR(Canvas canvas) {
        canvas.save();
        canvas.rotate(5.9f, 1284, 31);
        canvas.drawRect(1284, 31, 1284 + 400, 31 + 25, rectPaint);
        canvas.drawText("外右" + orderItems.get(currentID).sizeStr + " " + time + " " + orderItems.get(currentID).order_number, 1284, 31 + 23, paint);
        canvas.restore();
    }
    void drawTextXiabaiFront(Canvas canvas) {
        canvas.drawRect(1500, 10, 1500 + 300, 10 + 25, rectPaint);
        canvas.drawText("前下摆  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 1500, 10 + 23, paint);
    }
    void drawTextXiabaiBack(Canvas canvas) {
        canvas.drawRect(1500, 10, 1500 + 300, 10 + 25, rectPaint);
        canvas.drawText("后下摆  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 1500, 10 + 23, paint);
    }
    void drawTextXiukouL(Canvas canvas) {
        canvas.drawRect(700, 10, 700 + 300, 10 + 25, rectPaint);
        canvas.drawText("左  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 700, 10 + 23, paint);
    }
    void drawTextXiukouR(Canvas canvas) {
        canvas.drawRect(700, 10, 700 + 300, 10 + 25, rectPaint);
        canvas.drawText("右  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 700, 8 + 23, paint);
    }
    void drawTextPocket(Canvas canvas) {
        canvas.drawRect(1000, 137, 1000 + 300, 137 + 25, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 1000, 137 + 23, paint);
    }

    public void remixx(){
        int margin = 130;
        int marginHeight = Math.max(height_front + height_back + margin, height_maozi * 4);
        Matrix matrix = new Matrix();

        Bitmap bitmapF = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapRight;
        Bitmap bitmapB = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapLeft;

        Bitmap bitmapCombine = Bitmap.createBitmap(width_back + width_maozi + margin, marginHeight + height_arm + height_xiabai * 2 + height_pocket + margin * 5, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //front
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 2419, 2305, 3261, 4804);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_front);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2419, 2244, 3261, 4865);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front + margin, null);
        bitmapTemp.recycle();

        //maomianL
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2243, 21, 1806, 2475);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_maomian_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziOutL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, 0, null);
        bitmapTemp.recycle();

        //maomianR
        bitmapTemp = Bitmap.createBitmap(bitmapB, 4049, 21, 1806, 2475);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_maomian_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziOutR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_maozi, null);
        bitmapTemp.recycle();

        //maoliR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 2243, 21, 1805, 2464);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_maoli_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziInR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_maozi * 2, null);
        bitmapTemp.recycle();

        //maoliL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 4049, 21, 1805, 2464);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_maoli_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziInL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_maozi * 3, null);
        bitmapTemp.recycle();

        //xiuziL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 5680, 2994, 2412, 3309);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_arm_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, marginHeight + margin, null);
        bitmapTemp.recycle();

        //xiuziR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 7, 2994, 2412, 3309);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_arm_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapTemp, width_arm + margin, marginHeight + margin, null);
        bitmapTemp.recycle();

        //xiabaiFront
        bitmapTemp = Bitmap.createBitmap(bitmapF, 2459, 6987, 3179, 892);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_xiabai_front);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiabaiFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiabai, height_xiabai, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, marginHeight + height_arm + margin * 2, null);
        bitmapTemp.recycle();

        //xiabaiBack
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2459, 6987, 3179, 892);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_xiabai_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiabaiBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiabai, height_xiabai, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, marginHeight + height_arm + height_xiabai + margin * 3, null);
        bitmapTemp.recycle();

        //xiukouL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 6150, 6184, 1473, 892);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_xiukou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiukou, height_xiukou, true);
        canvasCombine.drawBitmap(bitmapTemp, width_xiabai + margin, marginHeight + height_arm + margin * 2, null);
        bitmapTemp.recycle();

        //xiukouR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 477, 6184, 1473, 892);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_xiukou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiukou, height_xiukou, true);
        canvasCombine.drawBitmap(bitmapTemp, width_xiabai + margin, marginHeight + height_arm + height_xiabai + margin * 3, null);
        bitmapTemp.recycle();

        //pocket
        bitmapTemp = Bitmap.createBitmap(bitmapF, 2801 - (width_cut - 2498) / 2, 4821, width_cut, height_cut);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, 2498, 1441, true);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gs_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocket(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, marginHeight + height_arm + height_xiabai * 2 + margin * 4, null);
        bitmapTemp.recycle();


        try {
//            matrix.reset();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String nameCombine = orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 150);
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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku);
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

    void setScale(String size) {
        switch (size) {
            case "XS":
                width_front = 2965;
                height_front = 4569;
                width_back = 2965;
                height_back = 4629;
                width_arm = 2174;
                height_arm = 3131;
                width_maozi = 1718;
                height_maozi = 2416;
                width_xiabai = 2884;
                height_xiabai = 892;
                width_pocket = 2379;
                height_pocket = 1383;
                width_xiukou = 1355;
                height_xiukou = 892;
                width_cut = 2616;
                height_cut = 1454;
                break;
            case "S":
                width_front = 3113;
                height_front = 4686;
                width_back = 3113;
                height_back = 4747;
                width_arm = 2292;
                height_arm = 3220;
                width_maozi = 1757;
                height_maozi = 2464;
                width_xiabai = 3031;
                height_xiabai = 892;
                width_pocket = 2498;
                height_pocket = 1441;
                width_xiukou = 1414;
                height_xiukou = 892;
                width_cut = 2616;
                height_cut = 1477;
                break;
            case "M":
                width_front = 3261;
                height_front = 4804;
                width_back = 3261;
                height_back = 4865;
                width_arm = 2412;
                height_arm = 3309;
                width_maozi = 1806;
                height_maozi = 2475;
                width_xiabai = 3179;
                height_xiabai = 892;
                width_pocket = 2498;
                height_pocket = 1441;
                width_xiukou = 1473;
                height_xiukou = 892;
                width_cut = 2498;
                height_cut = 1441;
                break;
            case "L":
                width_front = 3408;
                height_front = 4921;
                width_back = 3408;
                height_back = 4982;
                width_arm = 2531;
                height_arm = 3398;
                width_maozi = 1858;
                height_maozi = 2523;
                width_xiabai = 3327;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1532;
                height_xiukou = 892;
                width_cut = 2504;
                height_cut = 1462;
                break;
            case "XL":
                width_front = 3556;
                height_front = 5038;
                width_back = 3556;
                height_back = 5100;
                width_arm = 2649;
                height_arm = 3488;
                width_maozi = 1894;
                height_maozi = 2534;
                width_xiabai = 3474;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1591;
                height_xiukou = 892;
                width_cut = 2399;
                height_cut = 1428;
                break;
            case "2XL":
                width_front = 3703;
                height_front = 5156;
                width_back = 3703;
                height_back = 5218;
                width_arm = 2768;
                height_arm = 3577;
                width_maozi = 1985;
                height_maozi = 2711;
                width_xiabai = 3624;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1652;
                height_xiukou = 892;
                width_cut = 2304;
                height_cut = 1395;
                break;
            case "XXL":
                width_front = 3703;
                height_front = 5156;
                width_back = 3703;
                height_back = 5218;
                width_arm = 2768;
                height_arm = 3577;
                width_maozi = 1985;
                height_maozi = 2711;
                width_xiabai = 3624;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1652;
                height_xiukou = 892;
                width_cut = 2304;
                height_cut = 1395;
                break;
            case "3XL":
                width_front = 3851;
                height_front = 5273;
                width_back = 3851;
                height_back = 5336;
                width_arm = 2887;
                height_arm = 3666;
                width_maozi = 2044;
                height_maozi = 2759;
                width_xiabai = 3769;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1710;
                height_xiukou = 892;
                width_cut = 2216;
                height_cut = 1364;
                break;
            case "XXXL":
                width_front = 3851;
                height_front = 5273;
                width_back = 3851;
                height_back = 5336;
                width_arm = 2887;
                height_arm = 3666;
                width_maozi = 2044;
                height_maozi = 2759;
                width_xiabai = 3769;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1710;
                height_xiukou = 892;
                width_cut = 2216;
                height_cut = 1364;
                break;
            case "4XL":
                width_front = 3999;
                height_front = 5391;
                width_back = 3999;
                height_back = 5454;
                width_arm = 3006;
                height_arm = 3756;
                width_maozi = 2104;
                height_maozi = 2818;
                width_xiabai = 3917;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1769;
                height_xiukou = 892;
                width_cut = 2134;
                height_cut = 1334;
                break;
            case "XXXXL":
                width_front = 3999;
                height_front = 5391;
                width_back = 3999;
                height_back = 5454;
                width_arm = 3006;
                height_arm = 3756;
                width_maozi = 2104;
                height_maozi = 2818;
                width_xiabai = 3917;
                height_xiabai = 892;
                width_pocket = 2617;
                height_pocket = 1498;
                width_xiukou = 1769;
                height_xiukou = 892;
                width_cut = 2134;
                height_cut = 1334;
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

    String getColor(String color){
        if (color.equals("White")) {
            return "白灯";
        } else if (color.equals("Green")) {
            return "绿灯";
        } else if (color.equals("Blue")) {
            return "蓝灯";
        } else if (color.equals("Red")) {
            return "红灯";
        } else {
            return "无灯";
        }
    }
}
