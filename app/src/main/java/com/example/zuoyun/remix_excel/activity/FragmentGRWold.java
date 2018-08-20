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

public class FragmentGRWold extends BaseFragment {
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
                    Log.e("fragment2", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
//                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapLeft);
                    checkremix();
                } else if (message == 2) {
                    Log.e("fragment2", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
//                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapRight);
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
        canvas.drawRect(1000, 4159, 1000 + 500, 4159 + 25, rectPaint);
        canvas.drawText("正面 " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1000, 4159 + 23, paint);
        canvas.drawText(currentID + "", 1440, 4159 + 23, paintRed);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1000, 4222, 1000 + 500, 4222 + 25, rectPaint);
        canvas.drawText("背面 " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1000, 4222 + 23, paint);
        canvas.drawText(currentID + "", 1440, 4222 + 23, paintRed);
    }
    void drawTextXiuziL(Canvas canvas) {
        canvas.drawRect(1438, 13, 1438 + 130, 13 + 25, rectPaint);
        canvas.drawText("左 " + orderItems.get(currentID).sizeStr, 1438, 13 + 23, paint);
        canvas.drawText(currentID + "", 1520, 13 + 23, paintRed);

        canvas.drawRect(1000, 3777-25, 1000 + 300, 3777, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1000, 3777 - 2, paint);
    }
    void drawTextXiuziR(Canvas canvas) {
        canvas.drawRect(1335, 13, 1335 + 130, 13 + 25, rectPaint);
        canvas.drawText("右 " + orderItems.get(currentID).sizeStr, 1335, 13 + 23, paint);
        canvas.drawText(currentID + "", 1415, 13 + 23, paintRed);

        canvas.drawRect(1000, 3777-25, 1000 + 300, 3777, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1000, 3777 - 2, paint);
    }
    void drawTextMaoziInL(Canvas canvas) {
        canvas.drawRect(1130, 7, 1130 + 180, 7 + 25, rectPaint);
        canvas.drawText("内 " + orderItems.get(currentID).sizeStr, 1130, 7 + 23, paint);
        canvas.drawText(currentID + "", 1230, 7 + 23, paintRed);

        canvas.save();
        canvas.rotate(5.1f, 1582, 22);
        canvas.drawRect(1582, 22, 1582 + 300, 22 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1582, 22 + 23, paint);
        canvas.restore();
    }
    void drawTextMaoziInR(Canvas canvas) {
        canvas.drawRect(650, 7, 650 + 180, 7 + 25, rectPaint);
        canvas.drawText("内 " + orderItems.get(currentID).sizeStr, 650, 7 + 23, paint);
        canvas.drawText(currentID + "", 750, 7 + 23, paintRed);

        canvas.save();
        canvas.rotate(-5.1f, 58, 48);
        canvas.drawRect(58, 48, 58 + 300, 48 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 58, 48 + 23, paint);
        canvas.restore();
    }
    void drawTextMaoziOutL(Canvas canvas) {
        canvas.drawRect(920, 8, 920 + 100, 8 + 25, rectPaint);
        canvas.drawText("外", 920, 8 + 23, paint);
        canvas.drawText(currentID + "", 970, 8 + 23, paintRed);

        canvas.save();
        canvas.rotate(-5.2f, 64, 58);
        canvas.drawRect(64, 58, 64 + 300, 58 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 64, 58 + 23, paint);
        canvas.restore();
    }
    void drawTextMaoziOutR(Canvas canvas) {
        canvas.drawRect(920, 8, 920 + 100, 8 + 25, rectPaint);
        canvas.drawText("外", 920, 8 + 23, paint);
        canvas.drawText(currentID + "", 970, 8 + 23, paintRed);

        canvas.save();
        canvas.rotate(5f, 1551, 30);
        canvas.drawRect(1551, 30, 1551 + 300, 30 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1551, 30 + 23, paint);
        canvas.restore();
    }
    void drawTextPocket(Canvas canvas) {
        canvas.drawRect(800, 90, 800 + 200, 90 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sizeStr, 800, 90 + 23, paint);
        canvas.drawText(currentID + "", 900, 90 + 23, paintRed);

        canvas.drawRect(1050, 90, 1050 + 300, 90 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1050, 90 + 23, paint);
    }
    void drawTextXiabai(Canvas canvas) {
        canvas.drawRect(3900, 7, 3900 + 200, 7 + 25, rectPaint);
        canvas.drawText("下摆 " + orderItems.get(currentID).sizeStr, 3900, 7 + 23, paint);
        canvas.drawText(currentID + "", 4000, 7 + 23, paintRed);

        canvas.drawRect(4200, 7, 4200 + 300, 7 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 4200, 7 + 23, paint);
    }
    void drawTextXiukouL(Canvas canvas) {
        canvas.drawRect(760, 8, 760 + 160, 8 + 25, rectPaint);
        canvas.drawText("左 " + orderItems.get(currentID).sizeStr, 760, 8 + 23, paint);
        canvas.drawText(currentID + "", 860, 8 + 23, paintRed);

        canvas.drawRect(1000, 8, 1000 + 300, 8 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1000, 8 + 23, paint);
    }
    void drawTextXiukouR(Canvas canvas) {
        canvas.drawRect(760, 8, 760 + 160, 8 + 25, rectPaint);
        canvas.drawText("右 " + orderItems.get(currentID).sizeStr, 760, 8 + 23, paint);
        canvas.drawText(currentID + "", 860, 8 + 23, paintRed);

        canvas.drawRect(1000, 8, 1000 + 300, 8 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + "  " + time + "  " + orderItems.get(currentID).order_number, 1000, 8 + 23, paint);
    }

    public void remixx(){
        Matrix matrix = new Matrix();

        Bitmap bitmapCombine = Bitmap.createBitmap(width_arm * 2 + width_maozi + 200, height_back + height_arm + height_pocket + 200, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //front
        Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1428, 2257, 2072, 4193);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_front_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 3500, 2257, 2072, 4193);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_front_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + 60, 0, null);
        bitmapTemp.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1429, 2197, 4143, 4253);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gq_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front * 2 + 120, 0, null);
        bitmapTemp.recycle();

        //arm_l
        Bitmap bitmapArm = Bitmap.createBitmap(2903, 3784, Bitmap.Config.ARGB_8888);
        Canvas canvasArm= new Canvas(bitmapArm);
        canvasArm.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasArm.drawColor(0xffffffff);

        Bitmap bitmapHalf = Bitmap.createBitmap(1452, 3784, Bitmap.Config.ARGB_8888);
        Canvas canvasHalf= new Canvas(bitmapHalf);
        canvasHalf.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasHalf.drawColor(0xffffffff);

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 4857, 2561, 1615, 3729);
        matrix.reset();
        matrix.postRotate(-11.4f);
        matrix.postTranslate(-136, 298);
        canvasArm.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 529, 2561, 1615, 3729);
        matrix.reset();
        matrix.postRotate(11.4f);
        matrix.postTranslate(2, -19);
        canvasHalf.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();
        canvasArm.drawBitmap(bitmapHalf, 1451, 0, null);
        bitmapHalf.recycle();

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gq_arm_l);
        canvasArm.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziL(canvasArm);
        bitmapArm = Bitmap.createScaledBitmap(bitmapArm, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapArm, 0, height_back + 100, null);
        bitmapArm.recycle();

        //arm_r
        bitmapArm = Bitmap.createBitmap(2903, 3784, Bitmap.Config.ARGB_8888);
        canvasArm= new Canvas(bitmapArm);
        canvasArm.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasArm.drawColor(0xffffffff);

        bitmapHalf = Bitmap.createBitmap(1452, 3784, Bitmap.Config.ARGB_8888);
        canvasHalf= new Canvas(bitmapHalf);
        canvasHalf.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasHalf.drawColor(0xffffffff);

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 4857, 2561, 1615, 3729);
        matrix.reset();
        matrix.postRotate(-11.4f);
        matrix.postTranslate(-136, 298);
        canvasArm.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 529, 2561, 1615, 3729);
        matrix.reset();
        matrix.postRotate(11.4f);
        matrix.postTranslate(2, -19);
        canvasHalf.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();
        canvasArm.drawBitmap(bitmapHalf, 1451, 0, null);
        bitmapHalf.recycle();

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gq_arm_r);
        canvasArm.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziR(canvasArm);
        bitmapArm = Bitmap.createScaledBitmap(bitmapArm, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapArm, width_arm + 100, height_back + 100, null);
        bitmapArm.recycle();

        //pocket
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 2099, 4874, 1397, 1576);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_pocket_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocket(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_back + height_arm + 200, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 3500, 4874, 1397, 1576);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_pocket_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_pocket + 100, height_back + height_arm + 200, null);
        bitmapTemp.recycle();

        //maozi_out_l
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1572, 123, 1928, 2368);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_maozi_out_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziOutL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_arm * 2 + 200, height_back + 100, null);
        bitmapTemp.recycle();

        //maozi_out_r
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 3507, 123, 1928, 2368);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_maozi_out_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziOutR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_arm * 2 + 200, height_back + height_maozi + 200, null);
        bitmapTemp.recycle();

        String nameCombine = "1_" + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
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





        bitmapCombine = Bitmap.createBitmap(width_xiabai, height_xiabai + 50, Bitmap.Config.ARGB_8888);
        canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        //xiabai
        bitmapArm = Bitmap.createBitmap(7907, 860, Bitmap.Config.ARGB_8888);
        canvasArm= new Canvas(bitmapArm);
        canvasArm.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasArm.drawColor(0xffffffff);

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 3500, 6325, 1978, 431);
        canvasArm.drawBitmap(bitmapTemp, 0, 0, null);
        canvasArm.drawBitmap(bitmapTemp, 0, 430, null);
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1524, 6325, 3954, 431);
        canvasArm.drawBitmap(bitmapTemp, 1977, 0, null);
        canvasArm.drawBitmap(bitmapTemp, 1977, 430, null);
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1524, 6325, 1978, 431);
        canvasArm.drawBitmap(bitmapTemp, 5930, 0, null);
        canvasArm.drawBitmap(bitmapTemp, 5930, 430, null);
        bitmapTemp.recycle();

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gq_xiabai);
        canvasArm.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiabai(canvasArm);
        bitmapArm = Bitmap.createScaledBitmap(bitmapArm, width_xiabai, height_xiabai, true);
        canvasCombine.drawBitmap(bitmapArm, 0, 0, null);
        bitmapArm.recycle();

        nameCombine = "2_" + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
        fileSave = new File(pathSave + nameCombine);
        BitmapToJpg.save(bitmapCombine, fileSave, 150);
        bitmapCombine.recycle();






        bitmapCombine = Bitmap.createBitmap(width_xiukou * 2 + 50, height_xiukou + 50, Bitmap.Config.ARGB_8888);
        canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //xiukou_l
        bitmapArm = Bitmap.createBitmap(1628, 619, Bitmap.Config.ARGB_8888);
        canvasArm= new Canvas(bitmapArm);
        canvasArm.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasArm.drawColor(0xffffffff);

        bitmapHalf = Bitmap.createBitmap(814, 619, Bitmap.Config.ARGB_8888);
        canvasHalf= new Canvas(bitmapHalf);
        canvasHalf.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasHalf.drawColor(0xffffffff);

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 4828, 6018, 920, 768);
        matrix.reset();
        matrix.postRotate(-11.5f);
        matrix.postTranslate(-120, 25);
        canvasArm.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 1254, 6018, 920, 768);
        matrix.reset();
        matrix.postRotate(11.5f);
        matrix.postTranslate(32, -159);
        canvasHalf.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();
        canvasArm.drawBitmap(bitmapHalf, 813, 0, null);
        bitmapHalf.recycle();

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gq_xiukou);
        canvasArm.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouL(canvasArm);
        bitmapArm = Bitmap.createScaledBitmap(bitmapArm, width_xiukou, height_xiukou, true);
        canvasCombine.drawBitmap(bitmapArm, 0, 0, null);
        bitmapArm.recycle();

        //xiukou_r
        bitmapArm = Bitmap.createBitmap(1628, 619, Bitmap.Config.ARGB_8888);
        canvasArm= new Canvas(bitmapArm);
        canvasArm.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasArm.drawColor(0xffffffff);

        bitmapHalf = Bitmap.createBitmap(814, 619, Bitmap.Config.ARGB_8888);
        canvasHalf= new Canvas(bitmapHalf);
        canvasHalf.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasHalf.drawColor(0xffffffff);

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 4828, 6018, 920, 768);
        matrix.reset();
        matrix.postRotate(-11.5f);
        matrix.postTranslate(-120, 25);
        canvasArm.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1254, 6018, 920, 768);
        matrix.reset();
        matrix.postRotate(11.5f);
        matrix.postTranslate(32, -159);
        canvasHalf.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();
        canvasArm.drawBitmap(bitmapHalf, 813, 0, null);
        bitmapHalf.recycle();

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gq_xiukou);
        canvasArm.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouR(canvasArm);
        bitmapArm = Bitmap.createScaledBitmap(bitmapArm, width_xiukou, height_xiukou, true);
        canvasCombine.drawBitmap(bitmapArm, width_xiukou + 50, 0, null);
        bitmapArm.recycle();

        nameCombine = "3_" + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
        fileSave = new File(pathSave + nameCombine);
        BitmapToJpg.save(bitmapCombine, fileSave, 150);
        bitmapCombine.recycle();






        bitmapCombine = Bitmap.createBitmap(width_maozi * 2 + 100, height_maozi + 60, Bitmap.Config.ARGB_8888);
        canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        //maozi_in_r
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 1572, 123, 1928, 2368);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_maozi_in_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziInR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //maozi_in_l
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 3507, 123, 1928, 2368);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gr_maozi_in_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextMaoziInL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_maozi, height_maozi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_maozi + 100, 0, null);
        bitmapTemp.recycle();

        nameCombine = "4_" + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
        fileSave = new File(pathSave + nameCombine);
        BitmapToJpg.save(bitmapCombine, fileSave, 150);
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
            if (MainActivity.instance.leftsucceed && MainActivity.instance.rightsucceed) {
                remix();
            }
        }
    }

    void setScale(String size) {
        switch (size) {
            case "XS":
                width_front = 3180 / 2;
                height_front = 3760;
                width_back = 3180;
                height_back = 3820;
                width_arm = 2534;
                height_arm = 3342;
                width_maozi = 1610;
                height_maozi = 2370;
                width_xiabai = 5420 - 40;
                height_xiabai = 834;
                width_pocket = 2135 / 2;
                height_pocket = 1417;
                width_xiukou = 1265;
                height_xiukou = 834;
                break;
            case "S":
                width_front = 3354 / 2;
                height_front = 3872;
                width_back = 3354;
                height_back = 3932;
                width_arm = 2664;
                height_arm = 3462;
                width_maozi = 1709;
                height_maozi = 2429;
                width_xiabai = 5764 - 40;
                height_xiabai = 834;
                width_pocket = 2255 / 2;
                height_pocket = 1457;
                width_xiukou = 1292;
                height_xiukou = 834;
                break;
            case "M":
                width_front = 3530 / 2;
                height_front = 3990;
                width_back = 3530;
                height_back = 4050;
                width_arm = 2794;
                height_arm = 3580;
                width_maozi = 1802;
                height_maozi = 2499;
                width_xiabai = 6118 - 40;
                height_xiabai = 834;
                width_pocket = 2376 / 2;
                height_pocket = 1501;
                width_xiukou = 1352;
                height_xiukou = 834;
                break;
            case "L":
                width_front = 3708 / 2;
                height_front = 4106;
                width_back = 3708;
                height_back = 4167;
                width_arm = 2926;
                height_arm = 3699;
                width_maozi = 1861;
                height_maozi = 2558;
                width_xiabai = 6473 - 40;
                height_xiabai = 834;
                width_pocket = 2498 / 2;
                height_pocket = 1544;
                width_xiukou = 1411;
                height_xiukou = 834;
                break;
            case "XL":
                width_front = 3885 / 2;
                height_front = 4225;
                width_back = 3885;
                height_back = 4285;
                width_arm = 3056;
                height_arm = 3818;
                width_maozi = 1894;
                height_maozi = 2607;
                width_xiabai = 6827 - 40;
                height_xiabai = 834;
                width_pocket = 2619 / 2;
                height_pocket = 1588;
                width_xiukou = 1470;
                height_xiukou = 834;
                break;
            case "2XL":
                width_front = 4061 / 2;
                height_front = 4342;
                width_back = 4061;
                height_back = 4403;
                width_arm = 3187;
                height_arm = 3935;
                width_maozi = 1956;
                height_maozi = 2666;
                width_xiabai = 7182 - 40;
                height_xiabai = 834;
                width_pocket = 2739 / 2;
                height_pocket = 1631;
                width_xiukou = 1529;
                height_xiukou = 834;
                break;
            case "3XL":
                width_front = 4238 / 2;
                height_front = 4459;
                width_back = 4238;
                height_back = 4521;
                width_arm = 3317;
                height_arm = 4054;
                width_maozi = 2038;
                height_maozi = 2736;
                width_xiabai = 7536 - 40;
                height_xiabai = 834;
                width_pocket = 2860 / 2;
                height_pocket = 1675;
                width_xiukou = 1588;
                height_xiukou = 834;
                break;
            case "4XL":
                width_front = 4415 / 2;
                height_front = 4576;
                width_back = 4415;
                height_back = 4638;
                width_arm = 3448;
                height_arm = 4172;
                width_maozi = 2098;
                height_maozi = 2795;
                width_xiabai = 7891 - 40;
                height_xiabai = 834;
                width_pocket = 2981 / 2;
                height_pocket = 1719;
                width_xiukou = 1647;
                height_xiukou = 834;
                break;
            case "5XL":
                width_front = 4591 / 2;
                height_front = 4576;
                width_back = 4591;
                height_back = 4637;
                width_arm = 3479;
                height_arm = 4172;
                width_maozi = 2165;
                height_maozi = 2853;
                width_xiabai = 8244 - 40;
                height_xiabai = 834;
                width_pocket = 3096 / 2;
                height_pocket = 1721;
                width_xiukou = 1705;
                height_xiukou = 834;
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
