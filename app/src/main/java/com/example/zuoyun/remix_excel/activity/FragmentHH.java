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

public class FragmentHH extends BaseFragment {
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

    int width_up, width_down, width_pocket;
    int height_up,height_down,height_pocket;

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
        paint.setTextSize(22);
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

    void drawTextUp(Canvas canvas, String LR) {
        canvas.drawRect(670, 1905 - 22, 670 + 350, 1905, rectPaint);
        canvas.drawText("HH-" + LR + " " + orderItems.get(currentID).sizeStr + "  " + orderItems.get(currentID).order_number, 670, 1905 - 2, paint);
        canvas.drawRect(1090, 1905 - 22, 1090 + 300, 1905, rectPaint);
        canvas.drawText(time + "   " + orderItems.get(currentID).newCodeStr, 1090, 1905 - 2, paint);
    }


    void drawTextDown(Canvas canvas, String LR) {
        canvas.save();
        canvas.rotate(4.5f, 3499, 427);
        canvas.drawRect(3499, 427, 3499 + 165, 427 + 22, rectPaint);
        canvas.drawText(time + "  " + orderItems.get(currentID).sizeStr + LR, 3500, 427 + 20, paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(-4.5f, 3698, 439);
        canvas.drawRect(3698, 439, 3698 + 160, 439 + 22, rectPaint);
        canvas.drawText(orderItems.get(currentID).order_number, 3700, 439 + 20, paint);
        canvas.restore();
    }
    void drawTextPocket1(Canvas canvas,String LR) {
        canvas.save();
        canvas.rotate(90, 6, 510);
        canvas.drawRect(6, 510 - 22, 6 + 500, 510, rectPaint);
        canvas.drawText(" " + LR + " " + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 6, 510 - 2, paint);
        canvas.restore();
    }
    void drawTextPocket2(Canvas canvas,String LR) {
        canvas.save();
        canvas.rotate(-90, 918, 1100);
        canvas.drawRect(918, 1100 - 22, 918 + 500, 1100, rectPaint);
        canvas.drawText(" " + LR + " " + orderItems.get(currentID).sizeStr + "  " + time + "  " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).newCodeStr, 918, 1100 - 2, paint);
        canvas.restore();
    }


    public void remixx(){
        int margin = 120;
        Matrix matrix = new Matrix();
        int combineHeight = Math.max(height_down * 2 + height_pocket + margin * 2, height_down * 2 + height_up + margin);

        Bitmap bitmapF = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapRight;
        Bitmap bitmapB = orderItems.get(currentID).img_left == null ? MainActivity.instance.bitmapPillow : MainActivity.instance.bitmapLeft;

        Bitmap bitmapCombine = Bitmap.createBitmap(width_down, combineHeight, Bitmap.Config.ARGB_8888);
        Canvas canvasCombine= new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);

        //down
        Bitmap bitmapTemp = Bitmap.createBitmap(bitmapF, 20, 1947, 7360, 3818);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hh_down);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextDown(canvasTemp, "前");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_down, height_down, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);

        bitmapTemp = Bitmap.createBitmap(bitmapB, 20, 1947, 7360, 3818);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextDown(canvasTemp, "后");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_down, height_down, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_down + margin, null);

        //up
        bitmapTemp = Bitmap.createBitmap(bitmapF, 2636, 35, 2129, 1913);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hh_up);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextUp(canvasTemp, "前");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_up, height_up, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, combineHeight - height_up, null);

        bitmapTemp = Bitmap.createBitmap(bitmapB, 2636, 35, 2129, 1913);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        bitmapDB.recycle();
        drawTextUp(canvasTemp, "后");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_up, height_up, true);
        canvasCombine.drawBitmap(bitmapTemp, width_down - width_up, combineHeight - height_up, null);

        //pocketL后
        bitmapTemp = Bitmap.createBitmap(924, 1503, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix.reset();
        matrix.postRotate(-61.9f);
        matrix.postTranslate(-218, 1217);
        canvasTemp.drawBitmap(Bitmap.createBitmap(bitmapB, 1484, 1555, 1483, 990), matrix, null);

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hh_pocket_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocket2(canvasTemp, "左后");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, 0, height_down - height_pocket / 2, null);

        //pocketL前
        bitmapTemp = Bitmap.createBitmap(924, 1503, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix.reset();
        matrix.postRotate(61.9f);
        matrix.postTranslate(443, -91);
        canvasTemp.drawBitmap(Bitmap.createBitmap(bitmapF, 4434, 1557, 1483, 990), matrix, null);

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hh_pocket_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocket1(canvasTemp, "左前");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_down-width_pocket, height_down - height_pocket / 2, null);

        //pocketR后
        bitmapTemp = Bitmap.createBitmap(924, 1503, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix.reset();
        matrix.postRotate(61.9f);
        matrix.postTranslate(443, -91);
        canvasTemp.drawBitmap(Bitmap.createBitmap(bitmapB, 4434, 1557, 1483, 990), matrix, null);

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hh_pocket_l);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocket1(canvasTemp, "右后");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_down / 2 + margin, height_down * 2 + margin * 2, null);

        //pocketR前
        bitmapTemp = Bitmap.createBitmap(924, 1503, Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(bitmapTemp);
        canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        matrix.reset();
        matrix.postRotate(-61.9f);
        matrix.postTranslate(-218, 1217);
        canvasTemp.drawBitmap(Bitmap.createBitmap(bitmapF, 1484, 1555, 1483, 990), matrix, null);

        bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.hh_pocket_r);
        canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
        drawTextPocket2(canvasTemp, "右前");
        bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_pocket, height_pocket, true);
        canvasCombine.drawBitmap(bitmapTemp, width_down / 2 - width_pocket - margin, height_down * 2 + margin * 2, null);
        bitmapTemp.recycle();

        matrix.reset();
        matrix.postRotate(180, bitmapCombine.getWidth() / 2, bitmapCombine.getHeight() / 2);
        bitmapCombine = Bitmap.createBitmap(bitmapCombine, 0, 0, bitmapCombine.getWidth(), bitmapCombine.getHeight(), matrix, true);


        String nameCombine = orderItems.get(currentID).sku + "_" + orderItems.get(currentID).sizeStr + "_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
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

        try {
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
                width_up = 1705;
                height_up = 1674;
                width_down = 6333;
                height_down = 3379;
                width_pocket = 881;
                height_pocket = 1439;
                break;
            case "S":
                width_up = 1799;
                height_up = 1723;
                width_down = 6600;
                height_down = 3481;
                width_pocket = 881;
                height_pocket = 1444;
                break;
            case "M":
                width_up = 1894;
                height_up = 1770;
                width_down = 6865;
                height_down = 3583;
                width_pocket = 881;
                height_pocket = 1449;
                break;
            case "L":
                width_up = 2012;
                height_up = 1841;
                width_down = 7140;
                height_down = 3700;
                width_pocket = 928;
                height_pocket = 1515;
                break;
            case "XL":
                width_up = 2129;
                height_up = 1913;
                width_down = 7360;
                height_down = 3818;
                width_pocket = 924;
                height_pocket = 1503;
                break;
            case "2XL":
                width_up = 2248;
                height_up = 1986;
                width_down = 7561;
                height_down = 3937;
                width_pocket = 922;
                height_pocket = 1490;
                break;
            case "3XL":
                width_up = 2367;
                height_up = 2057;
                width_down = 7516;
                height_down = 4052;
                width_pocket = 994;
                height_pocket = 1543;
                break;
            case "4XL":
                width_up = 2485;
                height_up = 2130;
                width_down = 7611;
                height_down = 4166;
                width_pocket = 993;
                height_pocket = 1525;
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
