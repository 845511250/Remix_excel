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
import com.example.zuoyun.remix_excel.bean.Config;
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

public class FragmentGA extends BaseFragment {
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

    int width_front, width_back, width_arm, width_belt, width_collarBack, width_collar, width_loop, width_loopTop, width_pocket;
    int height_front,height_back,height_arm,height_belt,height_collarBack,height_collar,height_loop,height_loopTop,height_pocket;

    int num;
    String strPlus = "";
    int intPlus = 1;

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
        if (Config.chooseGA == 0) {
            showDialogChoose();
        } else {
            new Thread(){
                @Override
                public void run() {
                    super.run();

                    for(num=orderItems.get(currentID).num;num>=1;num--) {
                        for(int i=0;i<currentID;i++) {
                            if (orderItems.get(currentID).order_number.equals(orderItems.get(i).order_number)) {
                                intPlus += 1;
                            }
                        }
                        strPlus = intPlus == 1 ? "" : "(" + intPlus + ")";
                        if (Config.chooseGA == 100) {
                            remixx100();
                        } else {
                            remixx165();
                        }
                        intPlus += 1;
                    }

                }
            }.start();
        }
    }

    void drawTextFrontL(Canvas canvas) {
        canvas.drawRect(100, 4445 - 25, 100 + 500, 4445, rectPaint);
        canvas.drawText(time + "  GA女浴袍_" + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 100, 4445 - 2, paint);
    }
    void drawTextFrontR(Canvas canvas) {
        canvas.drawRect(1300, 4445 - 25, 1300 + 500, 4445, rectPaint);
        canvas.drawText(time + "  GA女浴袍_" + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1300, 4445 - 2, paint);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1300, 4394 - 25, 1300 + 500, 4394, rectPaint);
        canvas.drawText(time + "  GA女浴袍_" + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1300, 4394 - 2, paint);
    }
    void drawTextArmL(Canvas canvas) {
        canvas.drawRect(1122, 12, 1122 + 40, 12 + 25, rectPaint);
        canvas.drawText(" 左", 1122, 12 + 23, paint);

        canvas.drawRect(1000, 2472 - 25, 1000 + 500, 2472, rectPaint);
        canvas.drawText("左" + "  GA女浴袍_  " + time + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 2472 - 2, paint);
    }
    void drawTextArmR(Canvas canvas) {
        canvas.drawRect(1122, 12, 1122 + 40, 12 + 25, rectPaint);
        canvas.drawText(" 右", 1122, 12 + 23, paint);

        canvas.drawRect(1000, 2472 - 25, 1000 + 500, 2472, rectPaint);
        canvas.drawText("右" + "  GA女浴袍_  " + time + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 2472 - 2, paint);
    }
    void drawTextCollarBack(Canvas canvas) {
        canvas.drawRect(1400, 9, 1400 + 100, 9 + 25, rectPaint);
        canvas.drawText(" 后领", 1400, 9 + 23, paint);
    }
    void drawTextPocketL(Canvas canvas) {
        canvas.drawRect(360, 9, 360 + 300, 9 + 25, rectPaint);
        canvas.drawText(" 左 " + orderItems.get(currentID).order_number, 360, 9 + 23, paint);
    }
    void drawTextPocketR(Canvas canvas) {
        canvas.drawRect(360, 9, 360 + 300, 9 + 25, rectPaint);
        canvas.drawText(" 右 " + orderItems.get(currentID).order_number, 360, 9 + 23, paint);
    }

    public void remixx100(){
        Bitmap bitmapCombine = Bitmap.createBitmap(4523, 16500, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //腰带
        Bitmap bitmapBelt = Bitmap.createBitmap(8572, 521, Bitmap.Config.ARGB_8888);
        Canvas canvasBelt= new Canvas(bitmapBelt);
        canvasBelt.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasBelt.drawColor(0xffffffff);

        Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2206, 2539, 4286, 521);
        canvasBelt.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1273, 2539, 4286, 521);
        canvasBelt.drawBitmap(bitmapTemp, 4285, 0, null);
        bitmapTemp.recycle();

        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_belt);
        canvasBelt.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postTranslate(521 + 4002, 0);
        canvasCombine.drawBitmap(bitmapBelt, matrix, null);
        bitmapBelt.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2273, 678, 3255, 4400);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //frontR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2273, 678, 1883, 4451);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_front_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 4599, null);
        bitmapTemp.recycle();

        //frontL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3646, 678, 1882, 4451);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_front_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 1995, 4599, null);
        bitmapTemp.recycle();

        //loopTop
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3567, 833, 666, 222);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_loop_top);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 982, 15159, null);
        bitmapTemp.recycle();

        //collarL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3649, 49, 1008, 5072);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_collar_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        matrix.reset();
        matrix.postRotate(180);
        matrix.postTranslate(1008, 5072 + 9280);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //collarR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3145, 49, 1008, 5072);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_collar_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 1095, 9855, null);
        bitmapTemp.recycle();

        //armR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 108, 1561, 2209, 2478);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_arm_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 2201, 11976, null);
        bitmapTemp.recycle();

        //armL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5486, 1561, 2209, 2478);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_arm_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 2201, 9271, null);
        bitmapTemp.recycle();

        //pocketL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4475, 2990, 808, 973);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 2621, 14642, null);
        bitmapTemp.recycle();

        //pocketR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2516, 2990, 808, 973);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 3695, 14672, null);
        bitmapTemp.recycle();

        //collarBack
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2509, 219, 2782, 668);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_collar_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextCollarBack(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 15645, null);
        bitmapTemp.recycle();

        //loopL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5337, 2532, 222, 571);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 0, 14753, null);
        bitmapTemp.recycle();

        //loopR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2206, 2532, 222, 571);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 436, 14753, null);
        bitmapTemp.recycle();

        try {
//            Matrix matrix = new Matrix();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String nameCombine = "GA女浴袍_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 120);
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
            MainActivity.instance.bitmapPillow.recycle();

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

    public void remixx165(){
        Bitmap bitmapCombine = Bitmap.createBitmap(7705, 8994, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //腰带
        Bitmap bitmapBelt = Bitmap.createBitmap(8572, 521, Bitmap.Config.ARGB_8888);
        Canvas canvasBelt= new Canvas(bitmapBelt);
        canvasBelt.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasBelt.drawColor(0xffffffff);

        Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2206, 2539, 4286, 521);
        canvasBelt.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1273, 2539, 4286, 521);
        canvasBelt.drawBitmap(bitmapTemp, 4285, 0, null);
        bitmapTemp.recycle();

        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_belt);
        canvasBelt.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postTranslate(521 + 7184, 0);
        canvasCombine.drawBitmap(bitmapBelt, matrix, null);
        bitmapBelt.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2273, 678, 3255, 4400);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //frontR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2273, 678, 1883, 4451);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_front_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 4543, null);
        bitmapTemp.recycle();

        //frontL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3646, 678, 1882, 4451);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_front_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 2004, 4543, null);
        bitmapTemp.recycle();

        //collarL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3649, 49, 1008, 5072);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_collar_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 3953, 2659, null);
        bitmapTemp.recycle();

        //collarR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3145, 49, 1008, 5072);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_collar_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 6024, 0, null);
        bitmapTemp.recycle();

        //loopTop
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3567, 833, 666, 222);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_loop_top);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 5505, 1978, null);
        bitmapTemp.recycle();

        //armR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 108, 1561, 2209, 2478);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_arm_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 4766, 5551, null);
        bitmapTemp.recycle();

        //armL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5486, 1561, 2209, 2478);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_arm_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 3211, 0, null);
        bitmapTemp.recycle();

        //pocketL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4475, 2990, 808, 973);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 5186, 3226, null);
        bitmapTemp.recycle();

        //pocketR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2516, 2990, 808, 973);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 4889, 4400, null);
        bitmapTemp.recycle();

        //collarBack
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2509, 219, 2782, 668);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_collar_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextCollarBack(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 4040, 8238, null);
        bitmapTemp.recycle();

        //loopL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5337, 2532, 222, 571);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 5513, 2438, null);
        bitmapTemp.recycle();

        //loopR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2206, 2532, 222, 571);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ga_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 5949, 2438, null);
        bitmapTemp.recycle();

        try {
//            Matrix matrix = new Matrix();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String nameCombine = "GA女浴袍_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 120);
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
            MainActivity.instance.bitmapPillow.recycle();

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
            remix();
        }
    }

    void setScale(String size) {
        width_front = 1957;
        height_front = 4573;
        width_back = 3353;
        height_back = 4522;
        width_arm = 2305;
        height_arm = 2623;
        width_belt = 8808;
        height_belt = 572;
        width_collarBack = 2811;
        height_collarBack = 694;
        width_collar = 1035;
        height_collar = 5202;
        width_loop = 222;
        height_loop = 571;
        width_loopTop = 666;
        height_loopTop = 222;
        width_pocket = 808;
        height_pocket = 973;

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

    private void showDialogChoose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTransBackGround);
        final AlertDialog dialog_choose = builder.create();
        dialog_choose.setCancelable(false);
        dialog_choose.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_confirm, null);
        dialog_choose.setContentView(view_dialog);

        TextView tv_title_update = (TextView) view_dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_content = (TextView) view_dialog.findViewById(R.id.tv_dialog_content);
        Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);
        Button bt_no = (Button) view_dialog.findViewById(R.id.bt_dialog_no);

        tv_title_update.setText("GA女浴袍");
        tv_content.setText("请选择排版宽度");
        bt_yes.setText("1 米");
        bt_no.setText("1.65 米");

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.chooseGA = 100;
                dialog_choose.dismiss();
                remix();
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.chooseGA = 165;
                dialog_choose.dismiss();
                remix();
            }
        });
    }
}
