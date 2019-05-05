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

public class FragmentHF100 extends BaseFragment {
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

    int width_front, width_arm, width_xiabai, width_xiukou;
    int height_front,height_arm,height_xiabai,height_xiukou;

    int id_front = R.drawable.hf_front_l;

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
        canvas.drawRect(1400, 3267 - 25, 1400 + 500, 3267, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sku + "  " + orderItems.get(currentID).sizeStr + "前  " + orderItems.get(currentID).order_number + " " + orderItems.get(currentID).newCode, 1400, 3267 - 2, paint);
    }
    void drawTextBack(Canvas canvas) {
        canvas.drawRect(1400, 3267 - 25, 1400 + 500, 3267, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sku + "  " + orderItems.get(currentID).sizeStr + "后  " + orderItems.get(currentID).order_number + " " + orderItems.get(currentID).newCode, 1400, 3267 - 2, paint);
    }
    void drawTextXiuziL(Canvas canvas) {
        canvas.drawRect(1046, 10, 1046 + 30, 10 + 25, rectPaint);
        canvas.drawText("左", 1046, 10 + 23, paint);

        canvas.drawRect(1000, 2472 - 25, 1000 + 500, 2472, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sizeStr + "左  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 2472 - 2, paint);
    }
    void drawTextXiuziR(Canvas canvas) {
        canvas.drawRect(1046, 10, 1046 + 30, 10 + 25, rectPaint);
        canvas.drawText("右", 1046, 10 + 23, paint);

        canvas.drawRect(1000, 2472 - 25, 1000 + 500, 2472, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sizeStr + "右  " + orderItems.get(currentID).order_number + "  " + orderItems.get(currentID).newCode, 1000, 2472 - 2, paint);
    }
    void drawTextXiabaiFront(Canvas canvas) {
        canvas.drawRect(1500, 7, 1500 + 300, 7 + 25, rectPaint);
        canvas.drawText("前下摆  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 1500, 7 + 23, paint);
    }
    void drawTextXiabaiBack(Canvas canvas) {
        canvas.drawRect(1500, 7, 1500 + 300, 7 + 25, rectPaint);
        canvas.drawText("后下摆  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 1500, 7 + 23, paint);
    }
    void drawTextXiukouL(Canvas canvas) {
        canvas.drawRect(700, 7, 700 + 300, 7 + 25, rectPaint);
        canvas.drawText("左  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 700, 7 + 23, paint);
    }
    void drawTextXiukouR(Canvas canvas) {
        canvas.drawRect(700, 7, 700 + 300, 7 + 25, rectPaint);
        canvas.drawText("右  " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 700, 7 + 23, paint);
    }

    public void remixx(){
        int margin = 90;
        Matrix matrix = new Matrix();

        Bitmap bitmapF = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapRight;
        Bitmap bitmapB = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapLeft;

        Bitmap bitmapCombine;

        bitmapCombine = Bitmap.createBitmap(width_front + height_xiabai + 60, height_front * 2 + height_arm * 2 + margin * 4, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //front
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 2229, 0, 3142, 3274);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_front);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //xiuziL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 5370, 795, 2230, 2479);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hf_sleeve);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front * 2 + margin * 2, null);
        bitmapTemp.recycle();

        //back
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2229, 0, 3142, 3274);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), id_front);
        matrix.reset();
        matrix.postScale(-1, 1);
        bitmapDB = Bitmap.createBitmap(bitmapDB, 0, 0, 3142, 3274, matrix, true);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front + margin, null);
        bitmapTemp.recycle();

        //xiuziR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 0, 795, 2230, 2479);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hf_sleeve);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_arm, height_arm, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front * 2 + height_arm + margin * 3, null);
        bitmapTemp.recycle();

        //xiukouL
        bitmapTemp = Bitmap.createBitmap(bitmapF, 5877, 3197, 1218, 690);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hf_xiukou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiukou, height_xiukou, true);

        matrix.reset();
        matrix.postRotate(-90);
        matrix.postTranslate(width_front + 60, height_front);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //xiukouR
        bitmapTemp = Bitmap.createBitmap(bitmapF, 506, 3197, 1218, 690);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hf_xiukou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiukou, height_xiukou, true);

        matrix.reset();
        matrix.postRotate(-90);
        matrix.postTranslate(width_front + 60, width_xiukou + height_front + 1100);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //xiabaiFront
        bitmapTemp = Bitmap.createBitmap(bitmapF, 2525, 3197, 2551, 690);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hf_xiabai);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiabaiFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiabai, height_xiabai, true);

        matrix.reset();
        matrix.postRotate(-90);
        matrix.postTranslate(width_front + 60, width_xiabai + height_front + 1100 + width_xiukou + margin);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //xiabaiBack
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2525, 3197, 2551, 690);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hf_xiabai);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiabaiBack(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiabai, height_xiabai, true);

        matrix.reset();
        matrix.postRotate(-90);
        matrix.postTranslate(width_front + 60, width_xiabai + height_front + 1100 + width_xiukou + width_xiabai + margin * 2);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
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
            case "2XS":
                width_front = 3472;
                height_front = 3761;
                width_arm = 2534;
                height_arm = 2900;
                width_xiukou = 1324;
                height_xiukou = 892;
                width_xiabai = 2707;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_xs;
                break;
            case "XS":
                width_front = 3619;
                height_front = 3879;
                width_arm = 2626;
                height_arm = 2983;
                width_xiukou = 1383;
                height_xiukou = 892;
                width_xiabai = 2855;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_xs;
                break;
            case "S":
                width_front = 3767;
                height_front = 3997;
                width_arm = 2712;
                height_arm = 3057;
                width_xiukou = 1442;
                height_xiukou = 892;
                width_xiabai = 3002;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_xs;
                break;
            case "M":
                width_front = 3915;
                height_front = 4115;
                width_arm = 2797;
                height_arm = 3131;
                width_xiukou = 1508;
                height_xiukou = 892;
                width_xiabai = 3150;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_l;
                break;
            case "L":
                width_front = 4062;
                height_front = 4233;
                width_arm = 2883;
                height_arm = 3205;
                width_xiukou = 1560;
                height_xiukou = 892;
                width_xiabai = 3298;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_l;
                break;
            case "XL":
                width_front = 4299;
                height_front = 4352;
                width_arm = 3066;
                height_arm = 3279;
                width_xiukou = 1649;
                height_xiukou = 892;
                width_xiabai = 3504;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_l;
                break;
            case "2XL":
                width_front = 4535;
                height_front = 4469;
                width_arm = 3243;
                height_arm = 3353;
                width_xiukou = 1738;
                height_xiukou = 892;
                width_xiabai = 3711;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_3xl;
                break;
            case "3XL":
                width_front = 4771;
                height_front = 4588;
                width_arm = 3420;
                height_arm = 3426;
                width_xiukou = 1826;
                height_xiukou = 892;
                width_xiabai = 3918;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_3xl;
                break;
            case "4XL":
                width_front = 5007;
                height_front = 4706;
                width_arm = 3597;
                height_arm = 3500;
                width_xiukou = 1915;
                height_xiukou = 892;
                width_xiabai = 4124;
                height_xiabai = 892;

                id_front = R.drawable.hf_front_3xl;
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
