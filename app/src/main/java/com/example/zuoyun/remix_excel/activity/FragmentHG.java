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

public class FragmentHG extends BaseFragment {
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

    int width_front, width_back, width_pocket,width_border_small,width_border_big;
    int height_front,height_back,height_pocket,height_border_small,height_border_big;
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

    void drawTextFrontR(Canvas canvas) {
        canvas.drawRect(640, 20, 640 + 500, 20 + 25, rectPaint);
        canvas.drawText(time + "HGM男 前右 " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 640, 20 + 23, paint);
    }
    void drawTextFrontL(Canvas canvas) {
        canvas.drawRect(640, 30, 640 + 500, 30 + 25, rectPaint);
        canvas.drawText(time + "HGM男 前左 " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 640, 30 + 23, paint);
    }
    void drawTextBackL(Canvas canvas) {
        canvas.drawRect(950, 25, 950 + 500, 25 + 25, rectPaint);
        canvas.drawText(time + "HGM男 后左 " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 950, 25 + 23, paint);
    }
    void drawTextBackR(Canvas canvas) {
        canvas.drawRect(950, 25, 950 + 500, 25 + 25, rectPaint);
        canvas.drawText(time + "HGM男 后右 " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 950, 25 + 23, paint);
    }
    void drawTextPocketL(Canvas canvas) {
        canvas.drawRect(300, 16, 300 + 290, 16 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sizeStr + " " + time + "  " + orderItems.get(currentID).order_number, 300, 16 + 23, paint);
    }
    void drawTextPocketR(Canvas canvas) {
        canvas.drawRect(20, 8, 20 + 290, 8 + 25, rectPaint);
        canvas.drawText(orderItems.get(currentID).sizeStr + " " + time + "  " + orderItems.get(currentID).order_number, 20, 8 + 23, paint);
    }


    public void remixx(){
        int margin = 120;
        Matrix matrix = new Matrix();
        int width_combine = Math.max(width_front + width_back + margin, width_pocket * 4 + margin * 3);

        Bitmap bitmapCombine = Bitmap.createBitmap(width_combine, height_back * 2 + height_pocket + margin * 2, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //front
        Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 623, 157, 2077, 4186);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_frontR);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextFrontR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, width_back + margin, 0, null);

        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 2700, 157, 2077, 4186);
        canvasTemp = new Canvas(bitmapTemp);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, bitmapDB.getWidth(), bitmapDB.getHeight(), matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextFrontL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, width_back + margin, height_back + margin, null);

        //backL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 29, 40, 2671, 4420);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_backL);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextBackL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);

        //backR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapLeft, 2700, 40, 2671, 4420);
        canvasTemp = new Canvas(bitmapTemp);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, bitmapDB.getWidth(), bitmapDB.getHeight(), matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextBackR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_back + margin, null);

        //pocketR
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 623, 613, 1212, 2055);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgm_pocket_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocketR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_pocket + margin, height_back * 2 + margin * 2, null);
        canvasCombine.drawBitmap(bitmapTemp, width_pocket * 3 + margin * 3, height_back * 2 + margin * 2, null);

        //pocketL
        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 3565, 613, 1212, 2055);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgm_pocket_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocketL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_back * 2 + margin * 2, null);
        canvasCombine.drawBitmap(bitmapTemp, width_pocket * 2 + margin * 2, height_back * 2 + margin * 2, null);

//        //borderBigR
//        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 623, 613, 389, 1974);
//        canvasTemp = new Canvas(bitmapTemp);
//        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgm_border_big_r);
//        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
////        drawTextPocketR(canvasTemp);
//        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_border_big, height_border_big, true);
//        canvasCombine.drawBitmap(bitmapTemp, width_back - width_border_big, 0, null);
//
//        //borderBigL
//        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 4388, 613, 389, 1974);
//        canvasTemp = new Canvas(bitmapTemp);
//        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgm_border_big_l);
//        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
////        drawTextPocketL(canvasTemp);
//        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_border_big, height_border_big, true);
//        canvasCombine.drawBitmap(bitmapTemp, 0, height_back + margin, null);
//
//        //borderSmallR
//        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 623, 613, 222, 1669);
//        canvasTemp = new Canvas(bitmapTemp);
//        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgm_border_small_r);
//        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
////        drawTextPocketR(canvasTemp);
//        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_border_small, height_border_small, true);
//        canvasCombine.drawBitmap(bitmapTemp, width_pocket * 4 + margin * 4, height_back * 2 + margin * 2, null);
//
//        //borderSmallL
//        bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapRight, 4555, 613, 222, 1669);
//        canvasTemp = new Canvas(bitmapTemp);
//        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hgm_border_small_l);
//        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
////        drawTextPocketL(canvasTemp);
//        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_border_small, height_border_small, true);
//        canvasCombine.drawBitmap(bitmapTemp, width_pocket * 4 + width_border_small + margin * 5, height_back * 2 + margin * 2, null);

        bitmapDB.recycle();
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

    void setScale(String size) {
        width_pocket = 1266;
        height_pocket = 1989;
        width_border_small = 232;
        height_border_small = 1615;
        width_border_big = 403;
        height_border_big = 1910;
        switch (size) {
            case "S":
                width_front = 1905;
                height_front = 3884;
                width_back = 2547;
                height_back = 4135;
                id_frontR = R.drawable.hgm_front_m;
                id_backL = R.drawable.hgm_back_m;
                break;
            case "M":
                width_front = 1993;
                height_front = 3946;
                width_back = 2626;
                height_back = 4196;
                id_frontR = R.drawable.hgm_front_m;
                id_backL = R.drawable.hgm_back_m;
                break;
            case "L":
                width_front = 2081;
                height_front = 4007;
                width_back = 2703;
                height_back = 4254;
                id_frontR = R.drawable.hgm_front_m;
                id_backL = R.drawable.hgm_back_m;
                break;
            case "XL":
                width_front = 2170;
                height_front = 4051;
                width_back = 2787;
                height_back = 4304;
                id_frontR = R.drawable.hgm_front_2xl;
                id_backL = R.drawable.hgm_back_2xl;
                break;
            case "2XL":
                width_front = 2407;
                height_front = 4115;
                width_back = 3020;
                height_back = 4387;
                id_frontR = R.drawable.hgm_front_2xl;
                id_backL = R.drawable.hgm_back_2xl;
                break;
            case "3XL":
                width_front = 2525;
                height_front = 4234;
                width_back = 3126;
                height_back = 4500;
                id_frontR = R.drawable.hgm_front_2xl;
                id_backL = R.drawable.hgm_back_2xl;
                break;
            case "4XL":
                width_front = 2584;
                height_front = 4295;
                width_back = 3185;
                height_back = 4615;
                id_frontR = R.drawable.hgm_front_4xl;
                id_backL = R.drawable.hgm_back_4xl;
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
