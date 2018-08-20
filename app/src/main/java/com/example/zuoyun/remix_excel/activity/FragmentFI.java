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

public class FragmentFI extends BaseFragment {
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

    int width_front,width_back,width_xiabai,width_xiukou, width_xiuzi, width_lingkou;
    int height_front,height_back,height_xiabai,height_xiukou, height_xiuzi, height_lingkou;

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
        orderItems= MainActivity.instance.orderItems;
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
                } else if (message == 2) {
                    Log.e("jiake", "message2");
                    bt_remix.setClickable(true);
                    checkremix();
                } else {
                    if (message == 4) {
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
        canvas.drawRect(200, 3862-25, 800, 3862, rectPaint);
        canvas.drawText("FI套头卫衣  " + orderItems.get(currentID).newCode + "   " + time + "  " + orderItems.get(currentID).order_number, 200, 3862 - 2, paint);
    }
    void drawTextXiukouR(Canvas canvas) {
        canvas.drawRect(500, 6, 500 + 500, 6 + 25, rectPaint);
        canvas.drawText("袖口右 " + orderItems.get(currentID).newCode + "  " + time + "  " + orderItems.get(currentID).order_number, 500, 6 + 23, paint);
    }
    void drawTextXiukouL(Canvas canvas) {
        canvas.drawRect(500, 6, 500 + 500, 6 + 25, rectPaint);
        canvas.drawText("袖口左 " + orderItems.get(currentID).newCode + "  " + time + "  " + orderItems.get(currentID).order_number, 500, 6 + 23, paint);
    }
    void drawTextLingkou(Canvas canvas) {
        canvas.drawRect(1390, 10, 1390 + 300, 10 + 25, rectPaint);
        canvas.drawText("领口 " + orderItems.get(currentID).newCode, 1390, 10 + 23, paint);
    }
    void drawTextXiabai(Canvas canvas) {
        canvas.drawRect(3300, 10, 3300 + 500, 10 + 25, rectPaint);
        canvas.drawText("下摆 " + orderItems.get(currentID).newCode + "  " + time + "  " + orderItems.get(currentID).order_number, 3300, 10 + 23, paint);
    }
    void drawTextXiuziR(Canvas canvas) {
        canvas.drawRect(1300, 3570 - 25, 1300 + 500, 3570, rectPaint);
        canvas.drawText("袖子右 " + orderItems.get(currentID).newCode + "  " + time + "  " + orderItems.get(currentID).order_number, 1300, 3570 - 2, paint);
    }
    void drawTextXiuziL(Canvas canvas) {
        canvas.drawRect(1300, 3570 - 25, 1300 + 500, 3570, rectPaint);
        canvas.drawText("袖子左 " + orderItems.get(currentID).newCode + "  " + time + "  " + orderItems.get(currentID).order_number, 1300, 3570 - 2, paint);
    }

    public void remixx(){
        int margin = 100;

        Bitmap bitmapF = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapRight;
        Bitmap bitmapB = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapLeft;

        if (bitmapF.getWidth() == 5516) {
            bitmapF = Bitmap.createScaledBitmap(bitmapF, 9193, 4772, true);
        }
        if (bitmapB.getWidth() == 5516) {
            bitmapB = Bitmap.createScaledBitmap(bitmapB, 9193, 4772, true);
        }

        Bitmap bitmapCombine = Bitmap.createBitmap(width_front + height_lingkou + margin, height_front * 2 + height_xiuzi * 2 + height_xiukou + margin * 5, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //右袖子
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 0, 291, 2804, 3575);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_xiuzi_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiuzi, height_xiuzi, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front * 2 + margin * 2, null);
        bitmapTemp.recycle();

        //左袖子
        bitmapTemp = Bitmap.createBitmap(bitmapF, 6389, 291, 2804, 3575);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_xiuzi_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiuziL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiuzi, height_xiuzi, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front * 2 + height_xiuzi + margin * 3, null);
        bitmapTemp.recycle();

        //前
        bitmapTemp = Bitmap.createBitmap(bitmapF, 2804, 0, 3585, 3868);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_front);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_front, height_front, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
        bitmapTemp.recycle();

        //后面
        bitmapTemp = Bitmap.createBitmap(bitmapB, 2804, 0, 3585, 3864);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_back);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextFront(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_back, height_back, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front + margin, null);
        bitmapTemp.recycle();

        //下摆
        bitmapTemp = Bitmap.createBitmap(3585 * 2, 906, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawColor(0xffffffff);

        Bitmap bitmapCut = Bitmap.createBitmap(bitmapB, 2804, 3866, 3585, 906);
        canvasTemp.drawBitmap(bitmapCut, 0, 0, null);
        bitmapCut = Bitmap.createBitmap(bitmapF, 2804, 3866, 3585, 906);
        canvasTemp.drawBitmap(bitmapCut, 3585, 0, null);
        bitmapCut.recycle();

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_xiabai);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiabai(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiabai, height_xiabai, true);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postTranslate(height_xiabai + width_xiuzi + margin, height_front * 2 + margin * 2);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //领口
        bitmapTemp = Bitmap.createBitmap(bitmapF, 3118, 221, 2958, 322);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_lingkou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextLingkou(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_lingkou, height_lingkou, true);

        matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postTranslate(height_lingkou + width_front + margin, 0);
        canvasCombine.drawBitmap(bitmapTemp, matrix, null);
        bitmapTemp.recycle();

        //袖口左
        bitmapTemp = Bitmap.createBitmap(bitmapF, 7146, 3866, 1290, 906);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_xiukou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouL(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiukou, height_xiukou, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_front * 2 + height_xiuzi * 2 + margin * 4, null);
        bitmapTemp.recycle();

        //袖口右
        bitmapTemp = Bitmap.createBitmap(bitmapF, 757, 3866, 1290, 906);
        canvasTemp = new Canvas(bitmapTemp);
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fi_xiukou);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextXiukouR(canvasTemp);
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xiukou, height_xiukou, true);
        canvasCombine.drawBitmap(bitmapTemp, width_xiukou + margin, height_front * 2 + height_xiuzi * 2 + margin * 4, null);
        bitmapTemp.recycle();



        try {
//            matrix.reset();
//            matrix.postRotate(-90, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
//            bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);

            String sex = orderItems.get(currentID).newCodeStr.contains("女") ? "女" : "男";
            String nameCombine = "FI圆领卫衣" + sex + orderItems.get(currentID).sizeStr + orderItems.get(currentID).order_number + strPlus + ".jpg";

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
        if (orderItems.get(currentID).newCodeStr.contains("女")) {
            switch (size) {
                case "XS":
                    width_front = 3169;
                    height_front = 3782;
                    width_back = 3169;
                    height_back = 3782;
                    width_xiuzi = 2409;
                    height_xiuzi = 3431;
                    width_xiukou = 1205;
                    height_xiukou = 892;
                    width_xiabai = 5031;
                    height_xiabai = 892;
                    width_lingkou = 2861;
                    height_lingkou = 348;
                    break;
                case "S":
                    width_front = 3316;
                    height_front = 3898;
                    width_back = 3316;
                    height_back = 3898;
                    width_xiuzi = 2541;
                    height_xiuzi = 3520;
                    width_xiukou = 1264;
                    height_xiukou = 892;
                    width_xiabai = 5327;
                    height_xiabai = 892;
                    width_lingkou = 2914;
                    height_lingkou = 348;
                    break;
                case "M":
                    width_front = 3463;
                    height_front = 4015;
                    width_back = 3463;
                    height_back = 4015;
                    width_xiuzi = 2672;
                    height_xiuzi = 3609;
                    width_xiukou = 1323;
                    height_xiukou = 892;
                    width_xiabai = 5622;
                    height_xiabai = 892;
                    width_lingkou = 3008;
                    height_lingkou = 348;
                    break;
                case "L":
                    width_front = 3611;
                    height_front = 4132;
                    width_back = 3611;
                    height_back = 4132;
                    width_xiuzi = 2803;
                    height_xiuzi = 3397;
                    width_xiukou = 1382;
                    height_xiukou = 892;
                    width_xiabai = 5917;
                    height_xiabai = 892;
                    width_lingkou = 3061;
                    height_lingkou = 348;
                    break;
                case "XL":
                    width_front = 3758;
                    height_front = 4249;
                    width_back = 3758;
                    height_back = 4249;
                    width_xiuzi = 2933;
                    height_xiuzi = 3786;
                    width_xiukou = 1441;
                    height_xiukou = 892;
                    width_xiabai = 6212;
                    height_xiabai = 892;
                    width_lingkou = 3156;
                    height_lingkou = 348;
                    break;
                case "XXL":
                    width_front = 3905;
                    height_front = 4366;
                    width_back = 3905;
                    height_back = 4366;
                    width_xiuzi = 3065;
                    height_xiuzi = 3875;
                    width_xiukou = 1500;
                    height_xiukou = 892;
                    width_xiabai = 6508;
                    height_xiabai = 892;
                    width_lingkou = 3250;
                    height_lingkou = 348;
                    break;
                case "2XL":
                    width_front = 3905;
                    height_front = 4366;
                    width_back = 3905;
                    height_back = 4366;
                    width_xiuzi = 3065;
                    height_xiuzi = 3875;
                    width_xiukou = 1500;
                    height_xiukou = 892;
                    width_xiabai = 6508;
                    height_xiabai = 892;
                    width_lingkou = 3250;
                    height_lingkou = 348;
                    break;
                case "XXXL":
                    width_front = 4053;
                    height_front = 4483;
                    width_back = 4053;
                    height_back = 4483;
                    width_xiuzi = 3195;
                    height_xiuzi = 3963;
                    width_xiukou = 1559;
                    height_xiukou = 892;
                    width_xiabai = 6803;
                    height_xiabai = 892;
                    width_lingkou = 3303;
                    height_lingkou = 348;
                    break;
                case "3XL":
                    width_front = 4053;
                    height_front = 4483;
                    width_back = 4053;
                    height_back = 4483;
                    width_xiuzi = 3195;
                    height_xiuzi = 3963;
                    width_xiukou = 1559;
                    height_xiukou = 892;
                    width_xiabai = 6803;
                    height_xiabai = 892;
                    width_lingkou = 3303;
                    height_lingkou = 348;
                    break;
                case "XXXXL":
                    width_front = 4200;
                    height_front = 4601;
                    width_back = 4200;
                    height_back = 4601;
                    width_xiuzi = 3326;
                    height_xiuzi = 4052;
                    width_xiukou = 1618;
                    height_xiukou = 892;
                    width_xiabai = 7098;
                    height_xiabai = 892;
                    width_lingkou = 3398;
                    height_lingkou = 348;
                    break;
                case "4XL":
                    width_front = 4200;
                    height_front = 4601;
                    width_back = 4200;
                    height_back = 4601;
                    width_xiuzi = 3326;
                    height_xiuzi = 4052;
                    width_xiukou = 1618;
                    height_xiukou = 892;
                    width_xiabai = 7098;
                    height_xiabai = 892;
                    width_lingkou = 3398;
                    height_lingkou = 348;
                    break;

                default:
                    showDialogSizeWrong(orderItems.get(currentID).order_number);
                    sizeOK = false;
                    break;
            }
        } else {
            switch (size) {
                case "2XS":
                    width_front = 3169;
                    height_front = 3782;
                    width_back = 3169;
                    height_back = 3782;
                    width_xiuzi = 2409;
                    height_xiuzi = 3431;
                    width_xiukou = 1205;
                    height_xiukou = 892;
                    width_xiabai = 5031;
                    height_xiabai = 892;
                    width_lingkou = 2861;
                    height_lingkou = 348;
                    break;
                case "XXS":
                    width_front = 3169;
                    height_front = 3782;
                    width_back = 3169;
                    height_back = 3782;
                    width_xiuzi = 2409;
                    height_xiuzi = 3431;
                    width_xiukou = 1205;
                    height_xiukou = 892;
                    width_xiabai = 5031;
                    height_xiabai = 892;
                    width_lingkou = 2861;
                    height_lingkou = 348;
                    break;
                case "XS":
                    width_front = 3316;
                    height_front = 3898;
                    width_back = 3316;
                    height_back = 3898;
                    width_xiuzi = 2541;
                    height_xiuzi = 3520;
                    width_xiukou = 1264;
                    height_xiukou = 892;
                    width_xiabai = 5327;
                    height_xiabai = 892;
                    width_lingkou = 2914;
                    height_lingkou = 348;
                    break;
                case "S":
                    width_front = 3463;
                    height_front = 4015;
                    width_back = 3463;
                    height_back = 4015;
                    width_xiuzi = 2672;
                    height_xiuzi = 3609;
                    width_xiukou = 1323;
                    height_xiukou = 892;
                    width_xiabai = 5622;
                    height_xiabai = 892;
                    width_lingkou = 3008;
                    height_lingkou = 348;
                    break;
                case "M":
                    width_front = 3611;
                    height_front = 4132;
                    width_back = 3611;
                    height_back = 4132;
                    width_xiuzi = 2803;
                    height_xiuzi = 3397;
                    width_xiukou = 1382;
                    height_xiukou = 892;
                    width_xiabai = 5917;
                    height_xiabai = 892;
                    width_lingkou = 3061;
                    height_lingkou = 348;
                    break;
                case "L":
                    width_front = 3758;
                    height_front = 4249;
                    width_back = 3758;
                    height_back = 4249;
                    width_xiuzi = 2933;
                    height_xiuzi = 3786;
                    width_xiukou = 1441;
                    height_xiukou = 892;
                    width_xiabai = 6212;
                    height_xiabai = 892;
                    width_lingkou = 3156;
                    height_lingkou = 348;
                    break;
                case "XL":
                    width_front = 3905;
                    height_front = 4366;
                    width_back = 3905;
                    height_back = 4366;
                    width_xiuzi = 3065;
                    height_xiuzi = 3875;
                    width_xiukou = 1500;
                    height_xiukou = 892;
                    width_xiabai = 6508;
                    height_xiabai = 892;
                    width_lingkou = 3250;
                    height_lingkou = 348;
                    break;
                case "2XL":
                    width_front = 4053;
                    height_front = 4483;
                    width_back = 4053;
                    height_back = 4483;
                    width_xiuzi = 3195;
                    height_xiuzi = 3963;
                    width_xiukou = 1559;
                    height_xiukou = 892;
                    width_xiabai = 6803;
                    height_xiabai = 892;
                    width_lingkou = 3303;
                    height_lingkou = 348;
                    break;
                case "XXL":
                    width_front = 4053;
                    height_front = 4483;
                    width_back = 4053;
                    height_back = 4483;
                    width_xiuzi = 3195;
                    height_xiuzi = 3963;
                    width_xiukou = 1559;
                    height_xiukou = 892;
                    width_xiabai = 6803;
                    height_xiabai = 892;
                    width_lingkou = 3303;
                    height_lingkou = 348;
                    break;
                case "3XL":
                    width_front = 4200;
                    height_front = 4601;
                    width_back = 4200;
                    height_back = 4601;
                    width_xiuzi = 3326;
                    height_xiuzi = 4052;
                    width_xiukou = 1618;
                    height_xiukou = 892;
                    width_xiabai = 7098;
                    height_xiabai = 892;
                    width_lingkou = 3398;
                    height_lingkou = 348;
                    break;
                case "XXXL":
                    width_front = 4200;
                    height_front = 4601;
                    width_back = 4200;
                    height_back = 4601;
                    width_xiuzi = 3326;
                    height_xiuzi = 4052;
                    width_xiukou = 1618;
                    height_xiukou = 892;
                    width_xiabai = 7098;
                    height_xiabai = 892;
                    width_lingkou = 3398;
                    height_lingkou = 348;
                    break;
                case "4XL":
                    width_front = 4348;
                    height_front = 4718;
                    width_back = 4348;
                    height_back = 4718;
                    width_xiuzi = 3457;
                    height_xiuzi = 4141;
                    width_xiukou = 1677;
                    height_xiukou = 892;
                    width_xiabai = 7394;
                    height_xiabai = 892;
                    width_lingkou = 3492;
                    height_lingkou = 348;
                    break;
                case "XXXXL":
                    width_front = 4348;
                    height_front = 4718;
                    width_back = 4348;
                    height_back = 4718;
                    width_xiuzi = 3457;
                    height_xiuzi = 4141;
                    width_xiukou = 1677;
                    height_xiukou = 892;
                    width_xiabai = 7394;
                    height_xiabai = 892;
                    width_lingkou = 3492;
                    height_lingkou = 348;
                    break;

                default:
                    showDialogSizeWrong(orderItems.get(currentID).order_number);
                    sizeOK = false;
                    break;
            }
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
