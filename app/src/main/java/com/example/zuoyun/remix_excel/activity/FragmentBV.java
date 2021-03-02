package com.example.zuoyun.remix_excel.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
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

public class FragmentBV extends BaseFragment {
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

    int width_front,width_back, width_xiuzi;
    int height_front,height_back, height_xiuzi;
    int id_front,id_back,id_sleeve_l, id_sleeve_r;

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
        paint.setTextSize(20);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(20);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(20);
        paintBlue.setAntiAlias(true);

        paintSmall = new Paint();
        paintSmall.setColor(0xff000000);
        paintSmall.setTextSize(20);
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
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapLeft);
                        checkremix();
                } else if (message == 2) {
                    Log.e("fragment2", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapRight);
                        checkremix();
                }else if (message == 3) {
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
                        for (int i = 0; i < currentID; i++) {
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
        canvas.drawRect(200, 3408 - 20, 200 + 600, 3408, rectPaint);
        canvas.drawText("BV女T恤- " + orderItems.get(currentID).sizeStr + "   " + time + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCodeStr, 200, 3408 - 2, paint);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1070, 3863 - 20, 1070 + 600, 3863, rectPaint);
        canvas.drawText("BV女T恤- " + orderItems.get(currentID).sizeStr + "   " + time + "  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCodeStr, 1070, 3863 - 2, paint);
    }

    void drawTextXiuziL(Canvas canvas) {
        canvas.drawRect(500, 1735 - 20, 500 + 500, 1735, rectPaint);
        canvas.drawText("左袖子" + orderItems.get(currentID).sizeStr + "   " + time + "  " + orderItems.get(currentID).order_number, 500, 1735 - 2, paint);
    }
    void drawTextXiuziR(Canvas canvas) {
        canvas.drawRect(500, 1735 - 20, 500 + 500, 1735, rectPaint);
        canvas.drawText("右袖子" + orderItems.get(currentID).sizeStr + "   " + time + "  " + orderItems.get(currentID).order_number, 500, 1735 - 2, paint);
    }

    public void remixx(){
        Bitmap bitmapCombine = Bitmap.createBitmap(width_front + width_back + width_xiuzi + 180, Math.max(height_back, height_xiuzi * 2 + 120), Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        Bitmap bitmapF = MainActivity.instance.bitmapRight;
        Bitmap bitmapB = MainActivity.instance.bitmapLeft;

        //前
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 1946, 322, 2707, 3412);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_front);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);

        //后面
        bitmapTemp = Bitmap.createBitmap(bitmapB, 1946, 15, 2707, 3870);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + 60, 0, null);

        //左袖子
        bitmapTemp = Bitmap.createBitmap(bitmapF, 4653, 15, 1925, 1743);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_sleeve_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextXiuziL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiuzi, height_xiuzi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + width_back + 120, 0, null);

        //右袖子
        bitmapTemp = Bitmap.createBitmap(bitmapF, 21, 15, 1925, 1743);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_sleeve_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiuzi, height_xiuzi, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + width_back + 120, height_xiuzi + 120, null);
        bitmapTemp.recycle();


        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String nameCombine = "女T恤- " + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";

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
                width_front = 2540;
                height_front = 3277;
                width_back = 3542;
                height_back = 3713;
                width_xiuzi = 1814;
                height_xiuzi = 1711;
                id_front = R.drawable.bv_front_s;
                id_back = R.drawable.bv_back_s;
                id_sleeve_l = R.drawable.bv_sleeve_l_s;
                id_sleeve_r = R.drawable.bv_sleeve_r_s;
                break;
            case "S":
                width_front = 2658;
                height_front = 3345;
                width_back = 2660;
                height_back = 3771;
                width_xiuzi = 1910;
                height_xiuzi = 1799;
                id_front = R.drawable.bv_front_s;
                id_back = R.drawable.bv_back_s;
                id_sleeve_l = R.drawable.bv_sleeve_l_s;
                id_sleeve_r = R.drawable.bv_sleeve_r_s;
                break;
            case "M":
                width_front = 2776;
                height_front = 3414;
                width_back = 2778;
                height_back = 3830;
                width_xiuzi = 2005;
                height_xiuzi = 1888;
                id_front = R.drawable.bv_front_s;
                id_back = R.drawable.bv_back_s;
                id_sleeve_l = R.drawable.bv_sleeve_l_s;
                id_sleeve_r = R.drawable.bv_sleeve_r_s;
                break;
            case "L":
                width_front = 3012;
                height_front = 3586;
                width_back = 3015;
                height_back = 4010;
                width_xiuzi = 2194;
                height_xiuzi = 2006;
                id_front = R.drawable.bv_front_xl;
                id_back = R.drawable.bv_back_xl;
                id_sleeve_l = R.drawable.bv_sleeve_l_xl;
                id_sleeve_r = R.drawable.bv_sleeve_r_xl;
                break;
            case "XL":
                width_front = 3249;
                height_front = 3727;
                width_back = 3251;
                height_back = 4186;
                width_xiuzi = 2384;
                height_xiuzi = 2124;
                id_front = R.drawable.bv_front_xl;
                id_back = R.drawable.bv_back_xl;
                id_sleeve_l = R.drawable.bv_sleeve_l_xl;
                id_sleeve_r = R.drawable.bv_sleeve_r_xl;
                break;
            case "2XL":
                width_front = 3486;
                height_front = 3865;
                width_back = 3488;
                height_back = 4363;
                width_xiuzi = 2573;
                height_xiuzi = 2242;
                id_front = R.drawable.bv_front_xl;
                id_back = R.drawable.bv_back_xl;
                id_sleeve_l = R.drawable.bv_sleeve_l_xl;
                id_sleeve_r = R.drawable.bv_sleeve_r_xl;
                break;
            case "3XL":
                width_front = 3722;
                height_front = 4004;
                width_back = 3724;
                height_back = 4542;
                width_xiuzi = 2762;
                height_xiuzi = 2361;
                id_front = R.drawable.bv_front_3xl;
                id_back = R.drawable.bv_back_3xl;
                id_sleeve_l = R.drawable.bv_sleeve_l_3xl;
                id_sleeve_r = R.drawable.bv_sleeve_r_3xl;
                break;
            case "4XL":
                width_front = 3959;
                height_front = 4143;
                width_back = 3960;
                height_back = 4719;
                width_xiuzi = 2951;
                height_xiuzi = 2479;
                id_front = R.drawable.bv_front_3xl;
                id_back = R.drawable.bv_back_3xl;
                id_sleeve_l = R.drawable.bv_sleeve_l_3xl;
                id_sleeve_r = R.drawable.bv_sleeve_r_3xl;
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
