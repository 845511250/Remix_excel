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
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentHO extends BaseFragment {
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

    int width_main, height_main;
    int width_side, height_side;
    int x_cut_l,y_cut_l,x_cut_r,y_cut_r,x_cut_main,y_cut_main;
    int x_print_l,y_print_l,x_print_r,y_print_r;
    int width_combine,height_combine;
    int y_mark;

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
        paint.setTextSize(25);
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
        canvas.save();
        canvas.rotate(90, 10, height_main - 1100);
        canvas.drawRect(10 - 200, height_main - 1100 - 25, 10 + 700, height_main - 1100, rectPaint);
        canvas.drawText("下边  沙发垫 尺码" + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 10 - 200, height_main - 1100 - 2, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(-90, width_main - 10, 1100);
        canvas.drawRect(width_main - 10 - 200, 1100 - 25, width_main - 10 + 700, 1100, rectPaint);
        canvas.drawText("上边  沙发垫 尺码" + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, width_main - 10 - 200, 1100 - 2, paint);
        canvas.restore();

    }

    void drawTextSide(Canvas canvas) {
        canvas.drawRect(800, 10, 800 + 900, 10 + 25, rectPaint);
        canvas.drawText("沙发垫侧边 尺码" + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 800, 10 + 23, paint);
    }

    public void remixx(){
        //
        Bitmap bitmapCombine = null;
        orderItems.get(currentID).newCodeStr = orderItems.get(currentID).newCodeStr.replace("\"", "");

        if (orderItems.get(currentID).sku.equals("HO1") || orderItems.get(currentID).sku.equals("HO2")) {
            bitmapCombine = Bitmap.createBitmap(width_combine, height_combine, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine= new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);


            Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_main, y_cut_main, width_main, height_main);
            Canvas canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            drawText(canvasTemp);
            canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);

            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_l, y_cut_l, width_side, height_side);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            drawTextSide(canvasTemp);
            canvasCombine.drawBitmap(bitmapTemp, x_print_l, y_print_l, null);

            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_r, y_cut_r, width_side, height_side);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            drawTextSide(canvasTemp);
            canvasCombine.drawBitmap(bitmapTemp, x_print_r, y_print_r, null);

            bitmapTemp.recycle();

            if (num == 1) {
                if (MainActivity.instance.bitmapPillow != null) {
                    MainActivity.instance.bitmapPillow.recycle();
                }
            }

            //标记缝接点
            canvasCombine.drawRect(0, y_cut_l - y_cut_main, 20, y_cut_l - y_cut_main + height_side, rectBorderPaintRed);
            canvasCombine.drawRect(width_main - 20, y_cut_l - y_cut_main, width_main, y_cut_l - y_cut_main + height_side, rectBorderPaintGreen);
            canvasCombine.drawRect(x_print_l + width_side - 20, y_print_l + 1, x_print_l + width_side, y_print_l + 1 + height_side, rectBorderPaintRed);
            canvasCombine.drawRect(x_print_r, y_print_r, x_print_r + 20, y_print_r + height_side, rectBorderPaintGreen);

            //标记三角点
            Bitmap bitmapMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_triangle_left);
            bitmapMark = Bitmap.createScaledBitmap(bitmapMark, 30, 30, true);
            canvasCombine.drawBitmap(bitmapMark, 0, y_mark - 15, null);
            bitmapMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_triangle_right);
            bitmapMark = Bitmap.createScaledBitmap(bitmapMark, 30, 30, true);
            canvasCombine.drawBitmap(bitmapMark, width_main - 30, y_mark - 15, null);

            //画黑线框
            canvasCombine.drawRect(0, 0, width_main - 5, height_main - 5, rectBorderPaint);
            canvasCombine.drawRect(5, 5, width_main - 5, height_main - 5, rectBorderPaint);

            canvasCombine.drawRect(x_print_l, y_print_l + 1, x_print_l + width_side - 5, y_print_l + height_side - 5, rectBorderPaint);
            canvasCombine.drawRect(x_print_l + 5, y_print_l + 6, x_print_l + width_side - 5, y_print_l + height_side - 5, rectBorderPaint);

            canvasCombine.drawRect(x_print_r, y_print_r + 1, x_print_r + width_side - 5, y_print_r + height_side - 5, rectBorderPaint);
            canvasCombine.drawRect(x_print_r + 5, y_print_r + 6, x_print_r + width_side - 5, y_print_r + height_side - 5, rectBorderPaint);

            //画圆角
            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho1);
            canvasCombine.drawBitmap(bitmapDB, 0, 0, null);
            canvasCombine.drawBitmap(bitmapDB, x_print_l, y_print_l, null);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho2);
            canvasCombine.drawBitmap(bitmapDB, width_main - 500, 0, null);
            canvasCombine.drawBitmap(bitmapDB, x_print_r + width_side - 500, y_print_r, null);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho3);
            canvasCombine.drawBitmap(bitmapDB, 0, height_main - 500, null);
            canvasCombine.drawBitmap(bitmapDB, x_print_l, y_print_l + height_side - 500, null);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho4);
            canvasCombine.drawBitmap(bitmapDB, width_main - 500, height_main - 500, null);
            canvasCombine.drawBitmap(bitmapDB, x_print_r + width_side - 500, y_print_r + height_side - 500, null);

            //save
            String nameCombine = orderItems.get(currentID).newCodeStr + "-沙发垫-" + orderItems.get(currentID).order_number + strPlus + ".jpg";
            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 100);
            bitmapCombine.recycle();

        } else if (orderItems.get(currentID).sku.equals("HO5")) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            matrix.postTranslate(0, width_main);

            bitmapCombine = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_main, y_cut_main, width_main, height_main,matrix,true);
            Canvas canvasCombine= new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.rotate(-90);
            canvasCombine.translate(-width_main, 0);
            drawText(canvasCombine);

            if (num == 1) {
                if (MainActivity.instance.bitmapPillow != null) {
                    MainActivity.instance.bitmapPillow.recycle();
                }
            }

            //标记三角点
            Bitmap bitmapMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_triangle_left);
            bitmapMark = Bitmap.createScaledBitmap(bitmapMark, 30, 30, true);
            canvasCombine.drawBitmap(bitmapMark, 0, y_mark - 15, null);
            canvasCombine.drawBitmap(bitmapMark, 0, y_mark * 2 - 15, null);
            bitmapMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_triangle_right);
            bitmapMark = Bitmap.createScaledBitmap(bitmapMark, 30, 30, true);
            canvasCombine.drawBitmap(bitmapMark, width_main - 30, y_mark - 15, null);
            canvasCombine.drawBitmap(bitmapMark, width_main - 30, y_mark * 2 - 15, null);

            //画黑线框
            canvasCombine.drawRect(0, 0, width_main - 5, height_main - 5, rectBorderPaint);
            canvasCombine.drawRect(5, 5, width_main - 5, height_main - 5, rectBorderPaint);

            //画圆角
            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho1);
            canvasCombine.drawBitmap(bitmapDB, 0, 0, null);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho2);
            canvasCombine.drawBitmap(bitmapDB, width_main - 500, 0, null);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho3);
            canvasCombine.drawBitmap(bitmapDB, 0, height_main - 500, null);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho4);
            canvasCombine.drawBitmap(bitmapDB, width_main - 500, height_main - 500, null);

            //save
            String nameCombine = orderItems.get(currentID).newCodeStr + "-沙发垫-" + orderItems.get(currentID).order_number + strPlus + ".jpg";
            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 100);
            bitmapCombine.recycle();

        }else{
            //side
            bitmapCombine = Bitmap.createBitmap(height_side * 2 + 20, width_side, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine= new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);

            Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_l, y_cut_l, width_side, height_side);
            Canvas canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            drawTextSide(canvasTemp);
            //标记缝接点
            canvasTemp.drawRect(width_side - 20, 1, width_side, 1 + height_side, rectBorderPaintRed);
            //画黑线框
            canvasTemp.drawRect(0, 0, width_side - 5, height_side - 5, rectBorderPaint);
            canvasTemp.drawRect(5, 5, width_side - 5, height_side - 5, rectBorderPaint);
            //画圆角
            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho1);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho3);
            canvasTemp.drawBitmap(bitmapDB, 0, height_side - 500, null);

            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            matrix.postTranslate(0, width_side);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);

            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_r, y_cut_r, width_side, height_side);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            drawTextSide(canvasTemp);
            //标记缝接点
            canvasTemp.drawRect(0, 0, 20, height_side, rectBorderPaintGreen);
            //画黑线框
            canvasTemp.drawRect(0, 0, width_side - 5, height_side - 5, rectBorderPaint);
            canvasTemp.drawRect(5, 5, width_side - 5, height_side - 5, rectBorderPaint);
            //画圆角
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho2);
            canvasTemp.drawBitmap(bitmapDB, width_side - 500, 0, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho4);
            canvasTemp.drawBitmap(bitmapDB, width_side - 500, height_side - 500, null);

            matrix.reset();
            matrix.postRotate(90);
            matrix.postTranslate(height_side * 2 + 20, 0);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            bitmapTemp.recycle();

            //save Side
            String nameCombine = orderItems.get(currentID).newCodeStr + "-沙发垫侧边-" + orderItems.get(currentID).order_number + strPlus + ".jpg";
            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 100);
            bitmapCombine.recycle();

            //main
            matrix.reset();
            matrix.postRotate(-90);
            matrix.postTranslate(0, width_main);
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, x_cut_main, y_cut_main, width_main, height_main, matrix, true);
            canvasTemp= new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasTemp.rotate(-90);
            canvasTemp.translate(-width_main, 0);
            drawText(canvasTemp);

            if (num == 1) {
                if (MainActivity.instance.bitmapPillow != null) {
                    MainActivity.instance.bitmapPillow.recycle();
                }
            }

            //标记缝接点
            canvasTemp.drawRect(0, y_cut_l - y_cut_main, 20, y_cut_l - y_cut_main + height_side, rectBorderPaintRed);
            canvasTemp.drawRect(width_main - 20, y_cut_l - y_cut_main, width_main, y_cut_l - y_cut_main + height_side, rectBorderPaintGreen);
            //标记三角点
            Bitmap bitmapMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_triangle_left);
            bitmapMark = Bitmap.createScaledBitmap(bitmapMark, 30, 30, true);
            canvasTemp.drawBitmap(bitmapMark, 0, y_mark - 15, null);
            bitmapMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_triangle_right);
            bitmapMark = Bitmap.createScaledBitmap(bitmapMark, 30, 30, true);
            canvasTemp.drawBitmap(bitmapMark, width_main - 30, y_mark - 15, null);
            //画黑线框
            canvasTemp.drawRect(0, 0, width_main - 5, height_main - 5, rectBorderPaint);
            canvasTemp.drawRect(5, 5, width_main - 5, height_main - 5, rectBorderPaint);
            //画圆角
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho1);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho2);
            canvasTemp.drawBitmap(bitmapDB, width_main - 500, 0, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho3);
            canvasTemp.drawBitmap(bitmapDB, 0, height_main - 500, null);
            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ho4);
            canvasTemp.drawBitmap(bitmapDB, width_main - 500, height_main - 500, null);


            //save Main
            nameCombine = orderItems.get(currentID).newCodeStr + "-沙发垫-" + orderItems.get(currentID).order_number + strPlus + ".jpg";
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapTemp, fileSave, 100);
            bitmapTemp.recycle();

        }

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
            if (MainActivity.instance.bitmap3 != null) {
                MainActivity.instance.bitmapLeft.recycle();
                MainActivity.instance.bitmapRight.recycle();
                MainActivity.instance.bitmap3.recycle();
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
        switch (orderItems.get(currentID).sku) {
            case "HO1":
                width_main = 2380;
                height_main = 8075;
                width_side = 2420 + 60;
                height_side = 2300;
                x_cut_main = 5707;
                y_cut_main = 1362;
                x_cut_l = 3286;
                y_cut_l = 5590;
                x_cut_r = 8090 - 60;
                y_cut_r = 5590;
                width_combine = width_side;
                height_combine = height_main + height_side * 2 + 40;
                x_print_l = 0;
                y_print_l = height_main + 20;
                x_print_r = 0;
                y_print_r = height_main + height_side + 40;
                y_mark = 2817;
                break;
            case "HO2":
                width_main = 2891;
                height_main = 9764;
                width_side = 2851 + 60;
                height_side = 2300;
                x_cut_main = 5453;
                y_cut_main = 18;
                x_cut_l = 2600;
                y_cut_l = 5590;
                x_cut_r = 8346 - 60;
                y_cut_r = 5590;
                width_combine = width_side;
                height_combine = height_main + height_side * 2 + 40;
                x_print_l = 0;
                y_print_l = height_main + 20;
                x_print_r = 0;
                y_print_r = height_main + height_side + 40;
                y_mark = 3760;
                break;
            case "HO3":
                width_main = 4956;
                height_main = 8031;
                width_side = 2420 + 60;
                height_side = 2300;
                x_cut_main = 4422;
                y_cut_main = 1394;
                x_cut_l = 1998;
                y_cut_l = 5590;
                x_cut_r = 9380 - 60;
                y_cut_r = 5590;
                width_combine = width_side * 2 + 20;
                height_combine = height_main + height_side + 20;
                x_print_l = 0;
                y_print_l = height_main + 20;
                x_print_r = width_side + 20;
                y_print_r = height_main + 20;
                y_mark = 2995;
                break;
            case "HO4":
                width_main = 5592;
                height_main = 8111;
                width_side = 2420 + 60;
                height_side = 2300;
                x_cut_main = 4104;
                y_cut_main = 1199;
                x_cut_l = 1682;
                y_cut_l = 5590;
                x_cut_r = 9701 - 60;
                y_cut_r = 5590;
                width_combine = width_main;
                height_combine = height_main + height_side + 20;
                x_print_l = 0;
                y_print_l = height_main + 20;
                x_print_r = width_side + 50;
                y_print_r = height_main + 20;
                y_mark = 2972;
                break;
            case "HO5":
                width_main = 7246;
                height_main = 8032;
                x_cut_main = 3277;
                y_cut_main = 1199;
                width_combine = width_main;
                height_combine = height_main;
                y_mark = 2678;
                break;
            case "HO6":
                width_main = 7250;
                height_main = 8032;
                width_side = 2420 + 60;
                height_side = 2300;
                x_cut_main = 3277;
                y_cut_main = 1394;
                x_cut_l = 856;
                y_cut_l = 5590;
                x_cut_r = 10524 - 60;
                y_cut_r = 5590;
                width_combine = width_main;
                height_combine = height_main + height_side + 20;
                x_print_l = 0;
                y_print_l = height_main + 20;
                x_print_r = width_side + 50;
                y_print_r = height_main + 20;
                y_mark = 2790;
                break;
            case "HO7":
                width_main = 8062;
                height_main = 9134;
                width_side = 2850 + 60;
                height_side = 2300;
                x_cut_main = 2867;
                y_cut_main = 647;
                x_cut_l = 10;
                y_cut_l = 5590;
                x_cut_r = 10936 - 60;
                y_cut_r = 5590;
                width_combine = width_main;
                height_combine = height_main + height_side + 20;
                x_print_l = 0;
                y_print_l = height_main + 20;
                x_print_r = width_side + 400;
                y_print_r = height_main + 20;
                y_mark = 3740;
                break;
            default:
                sizeOK = false;
                break;
        }
    }

}
