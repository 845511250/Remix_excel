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

public class FragmentGUM extends BaseFragment {
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

    int width_front, width_back, width_arm, width_pocket_in, width_pocket_out;
    int height_front,height_back,height_arm,height_pocket_in,height_pocket_out;

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
                } else if (message == 4) {
                    Log.e("fy", "message4");
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapPillow);
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
        canvas.drawRect(250, 3415 - 25, 250 + 500, 3415, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 250, 3415 - 2, paint);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1000, 3448, 1000 + 500, 3448 + 25, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1000, 3448 + 23, paint);
    }
    void drawTextXiuziL(Canvas canvas) {
        canvas.drawRect(1000, 3185-25, 1000 + 500, 3185, rectPaint);
        canvas.drawText("左  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1000, 3185 - 2, paint);
    }
    void drawTextXiuziR(Canvas canvas) {
        canvas.drawRect(1000, 3185-25, 1000 + 500, 3185, rectPaint);
        canvas.drawText("右  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 1000, 3185 - 2, paint);
    }
    void drawTextPocketInL(Canvas canvas) {
        canvas.drawRect(10, 6, 10 + 250, 6 + 25, rectPaint);
        canvas.drawText("左  " + orderItems.get(currentID).order_number, 10, 6 + 23, paint);
    }
    void drawTextPocketInR(Canvas canvas) {
        canvas.drawRect(10, 6, 10 + 250, 6 + 25, rectPaint);
        canvas.drawText("右  " + orderItems.get(currentID).order_number , 10, 6 + 23, paint);
    }
    void drawTextPocketOutL(Canvas canvas) {
        canvas.drawRect(10, 946-25, 10 + 250, 946, rectPaint);
        canvas.drawText("左  " + orderItems.get(currentID).order_number , 10, 946 - 2, paint);
    }
    void drawTextPocketOutR(Canvas canvas) {
        canvas.drawRect(10, 946-25, 10 + 250, 946, rectPaint);
        canvas.drawText("右  " + orderItems.get(currentID).order_number , 10, 946 - 2, paint);
    }

    public void remixx(){
        int margin = 130;
        Matrix matrix = new Matrix();

        Bitmap bitmapF = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapRight;
        Bitmap bitmapB = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapLeft;

        Bitmap bitmapCombine = Bitmap.createBitmap(width_front + width_arm + margin, height_back + height_front * 2 + margin * 2, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //front
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 2452, 76, 1666, 4090);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_front_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_back + margin, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(bitmapF, 4082, 76, 1666, 4090);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_front_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_back + height_front + margin, null);
        bitmapTemp.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2452, 33, 3296, 3480);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //arm_l
        bitmapTemp = Bitmap.createBitmap(bitmapF, 5747, 708, 2437, 3191);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_arm_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_back + margin, null);
        bitmapTemp.recycle();

        //arm_r
        bitmapTemp = Bitmap.createBitmap(bitmapF, 15, 708, 2437, 3191);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_arm_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_back + height_arm + margin * 2, null);
        bitmapTemp.recycle();

        //pocket
        bitmapTemp = Bitmap.createBitmap(bitmapF, 5010, 2278, 297, 983);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_pocket_in_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketInL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket_in, height_pocket_in, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + margin, height_back + height_arm * 2 + margin * 3, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(bitmapF, 2892, 2278, 297, 983);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_pocket_in_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketInR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket_in, height_pocket_in, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + width_pocket_in + margin * 2, height_back + height_arm * 2 + margin * 3, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(bitmapF, 5010, 2278, 421, 954);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_pocket_out);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketOutL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket_out, height_pocket_out, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + width_pocket_in * 2 + margin * 3, height_back + height_arm * 2 + margin * 3, null);
        bitmapTemp.recycle();

        bitmapTemp = Bitmap.createBitmap(bitmapF, 2769, 2278, 421, 954);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.gum_pocket_out);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextPocketOutR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket_out, height_pocket_out, true);
        canvasCombine.drawBitmap(bitmapTemp, width_front + width_pocket_in * 2 + width_pocket_out + margin * 4, height_back + height_arm * 2 + margin * 3, null);
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
            case "S":
                width_front = 1861;
                height_front = 4760;
                width_back = 3676;
                height_back = 3996;
                width_arm = 3652;
                height_arm = 3722;
                width_pocket_in = 329;
                height_pocket_in = 1143;
                width_pocket_out = 468;
                height_pocket_out = 1105;
                break;
            case "M":
                width_front = 1934;
                height_front = 4878;
                width_back = 3823;
                height_back = 4115;
                width_arm = 2783;
                height_arm = 3811;
                width_pocket_in = 342;
                height_pocket_in = 1171;
                width_pocket_out = 487;
                height_pocket_out = 1132;
                break;
            case "L":
                width_front = 2009;
                height_front = 4996;
                width_back = 3972;
                height_back = 4234;
                width_arm = 2916;
                height_arm = 3902;
                width_pocket_in = 356;
                height_pocket_in = 1200;
                width_pocket_out = 506;
                height_pocket_out = 1160;
                break;
            case "XL":
                width_front = 2081;
                height_front = 5112;
                width_back = 4118;
                height_back = 4350;
                width_arm = 3046;
                height_arm = 3989;
                width_pocket_in = 368;
                height_pocket_in = 1227;
                width_pocket_out = 524;
                height_pocket_out = 1186;
                break;
            case "2XL":
                width_front = 2216;
                height_front = 5284;
                width_back = 4265;
                height_back = 4469;
                width_arm = 3176;
                height_arm = 4078;
                width_pocket_in = 392;
                height_pocket_in = 1269;
                width_pocket_out = 558;
                height_pocket_out = 1226;
                break;
            case "3XL":
                width_front = 2308;
                height_front = 5431;
                width_back = 4462;
                height_back = 4610;
                width_arm = 3308;
                height_arm = 4166;
                width_pocket_in = 408;
                height_pocket_in = 1304;
                width_pocket_out = 581;
                height_pocket_out = 1261;
                break;
            case "4XL":
                width_front = 2400;
                height_front = 5590;
                width_back = 4664;
                height_back = 4724;
                width_arm = 3438;
                height_arm = 4255;
                width_pocket_in = 425;
                height_pocket_in = 1342;
                width_pocket_out = 604;
                height_pocket_out = 1297;
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
