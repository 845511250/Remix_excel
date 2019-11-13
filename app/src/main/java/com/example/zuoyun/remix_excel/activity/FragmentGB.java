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

public class FragmentGB extends BaseFragment {
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

    int width_front, width_back, width_arm, width_belt, width_collar, width_loop, width_loopTop, width_pocket;
    int height_front,height_back,height_arm,height_belt,height_collar,height_loop,height_loopTop,height_pocket;

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
        if (Config.chooseGB == 0) {
            showDialogChoose();
        } else {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    setScale(orderItems.get(currentID).sizeStr);

                    for(num=orderItems.get(currentID).num;num>=1;num--) {
                        for(int i=0;i<currentID;i++) {
                            if (orderItems.get(currentID).order_number.equals(orderItems.get(i).order_number)) {
                                intPlus += 1;
                            }
                        }
                        strPlus = intPlus == 1 ? "" : "(" + intPlus + ")";
                        if (Config.chooseGB == 100) {
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
        canvas.drawRect(50, 6634 - 25, 50 + 500, 6634, rectPaint);
        canvas.drawText(time + "  GB男浴袍_" + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 50, 6634 - 2, paint);
    }
    void drawTextFrontR(Canvas canvas) {
        canvas.drawRect(1560, 6634 - 25, 1560 + 500, 6634, rectPaint);
        canvas.drawText(time + "  GB男浴袍_" + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1560, 6634 - 2, paint);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1500, 5973 - 25, 1500 + 500, 5973, rectPaint);
        canvas.drawText(time + "  GB男浴袍_" + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1500, 5973 - 2, paint);
    }
    void drawTextArmL(Canvas canvas) {
        canvas.drawRect(1253, 9, 1253 + 50, 9 + 25, rectPaint);
        canvas.drawText(" 左", 1253, 9 + 23, paint);

        canvas.drawRect(1000, 2932 - 25, 1000 + 500, 2932, rectPaint);
        canvas.drawText("左" + "  GB男浴袍  " + time + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 2932 - 2, paint);
    }
    void drawTextArmR(Canvas canvas) {
        canvas.drawRect(1253, 9, 1253 + 50, 9 + 25, rectPaint);
        canvas.drawText(" 右", 1253, 9 + 23, paint);

        canvas.drawRect(1000, 2932 - 25, 1000 + 500, 2932, rectPaint);
        canvas.drawText("右" + "  GB男浴袍  " + time + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 2932 - 2, paint);
    }
    void drawTextPocketL(Canvas canvas) {
        canvas.drawRect(480, 8, 480 + 300, 8 + 25, rectPaint);
        canvas.drawText(" 左 " + orderItems.get(currentID).order_number, 480, 8 + 23, paint);
    }
    void drawTextPocketR(Canvas canvas) {
        canvas.drawRect(480, 8, 480 + 300, 8 + 25, rectPaint);
        canvas.drawText(" 右 " + orderItems.get(currentID).order_number, 480, 8 + 23, paint);
    }

    public void remixx100(){
        Bitmap bitmapCombine = Bitmap.createBitmap(4331, 20956, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //frontR
        Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2234, 16, 2095, 6640);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_front_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 6151, null);
        bitmapTemp.recycle();

        //frontL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3871, 16, 2095, 6640);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_front_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 2181, 6151, null);
        bitmapTemp.recycle();

        //armL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5699, 1385, 2491, 2939);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_arm_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 12964, null);
        bitmapTemp.recycle();

        //armR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 9, 1382, 2491, 2939);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_arm_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 16181, null);
        bitmapTemp.recycle();

        //collarL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3871, 16, 942, 6532);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_collar_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 2596, 12964, null);
        bitmapTemp.recycle();

        //collarR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3387, 16, 942, 6532);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_collar_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        matrix.postTranslate(942 + 3315, 6532 + 14263);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2346, 682, 3502, 5979);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //belt
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 60, 2923, 8075, 525);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_belt);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postTranslate(height_belt + 3806, 0);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //pocketL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4573, 3448, 1045, 1154);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 19496, null);
        bitmapTemp.recycle();

        //pocketR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2582, 3448, 1044, 1154);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 1246, 19496, null);
        bitmapTemp.recycle();

        //loopL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5584, 2922, 205, 526);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp,  2596, 19789, null);
        bitmapTemp.recycle();

        //loopR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2406, 2923, 205, 525);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 2965, 19789, null);
        bitmapTemp.recycle();

        //loopTop
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3791, 796, 612, 205);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_loop_top);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 2596, 20513, null);
        bitmapTemp.recycle();

        try {
//            Matrix matrix = new Matrix();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String nameCombine = "GB男浴袍_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 110);
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
        Bitmap bitmapCombine = Bitmap.createBitmap(7042, 12715, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //frontR
        Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2234, 16, 2095, 6640);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_front_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 3579, 0, null);
        bitmapTemp.recycle();

        //frontL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3871, 16, 2095, 6640);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_front_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFrontL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 6051, null);
        bitmapTemp.recycle();

        //collarL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3871, 16, 942, 6532);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_collar_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 5824, 0, null);
        bitmapTemp.recycle();

        //collarR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3387, 16, 942, 6532);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_collar_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        matrix.postTranslate(942 + 2095, 6532 + 6181);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //armL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5699, 1385, 2491, 2939);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_arm_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 2833, 6737, null);
        bitmapTemp.recycle();

        //armR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 9, 1382, 2491, 2939);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_arm_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextArmR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 3106, 9777, null);
        bitmapTemp.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2346, 682, 3502, 5979);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //belt
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 60, 2923, 8075, 525);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_belt);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();

        matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postTranslate(height_belt + 6517, 1600);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //pocketL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4573, 3448, 1045, 1154);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketL(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 5839, 9959, null);
        bitmapTemp.recycle();

        //pocketR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2582, 3448, 1044, 1154);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_pocket);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketR(canvasTemp);
        canvasCombine.drawBitmap(bitmapTemp, 5839, 11388, null);
        bitmapTemp.recycle();

        //loopL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 5584, 2922, 205, 526);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 5674, 7277, null);
        bitmapTemp.recycle();

        //loopR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 2406, 2923, 205, 525);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_loop);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 6043, 7277, null);
        bitmapTemp.recycle();

        //loopTop
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3791, 796, 612, 205);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gb_loop_top);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        canvasCombine.drawBitmap(bitmapTemp, 5674, 8001, null);
        bitmapTemp.recycle();

        try {
//            Matrix matrix = new Matrix();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String nameCombine = "GB男浴袍_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 110);
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
        width_front = 2095;
        height_front = 6640;
        width_back = 3502;
        height_back = 5979;
        width_arm = 2491;
        height_arm = 2938;
        width_belt = 8075;
        height_belt = 525;
        width_collar = 942;
        height_collar = 6532;
        width_loop = 204;
        height_loop = 525;
        width_loopTop = 612;
        height_loopTop = 205;
        width_pocket = 1045;
        height_pocket = 1154;

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

        tv_title_update.setText("GB男浴袍");
        tv_content.setText("请选择排版宽度");
        bt_yes.setText("1 米");
        bt_no.setText("1.65 米");

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.chooseGB = 100;
                dialog_choose.dismiss();
                remix();
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.chooseGB = 165;
                dialog_choose.dismiss();
                remix();
            }
        });
    }

}
