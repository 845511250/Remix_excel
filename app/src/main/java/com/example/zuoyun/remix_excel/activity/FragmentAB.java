package com.example.zuoyun.remix_excel.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import com.example.zuoyun.remix_excel.tools.BitmapToJpg;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;

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

public class FragmentAB extends BaseFragment {
    Context context;
//    String sdCardPath = "/mnt/asec/share";
String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    int currentID;
    String childPath;

    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.bt_remix)
    Button bt_remix;

    float scaleX=1.0f, scaleY = 1.0f;
    int num;
    String strPlus = "";

    @Override
    public int getLayout() {
        return R.layout.fragment_ab;
    }

    @Override
    public void initData(View view) {
        context = getContext();
        ButterKnife.bind(this, view);
        orderItems=MainActivity.instance.orderItems;
        currentID = MainActivity.instance.currentID;
        childPath = MainActivity.instance.childPath;

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_left.setImageDrawable(null);
                    iv_right.setImageDrawable(null);
                    Log.e("fragment2", "message0");
                } else if (message == 1) {
                    Log.e("fragment2", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_right.setImageBitmap(MainActivity.instance.bitmapLeft);
                    checkremix();
                } else if (message == 2) {
                    Log.e("fragment2", "message2");
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
    }

    public void remix(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                for(num=orderItems.get(currentID).num;num>=1;num--) {
                    for(int i=0;i<currentID;i++) {
                        if (orderItems.get(currentID).order_number.equals(orderItems.get(i).order_number)) {
                            strPlus += "+";
                        }
                    }
                    remixx();
                    strPlus += "+";
                }
            }
        }.start();

    }

    public void remixx(){
        //1241*1603

        Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.ds);
        MainActivity.instance.bitmapLeft = Bitmap.createScaledBitmap(MainActivity.instance.bitmapLeft, 620, 1603, true);
        MainActivity.instance.bitmapRight = Bitmap.createScaledBitmap(MainActivity.instance.bitmapRight, 621, 1603, true);

        //paint
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(30);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        String time = MainActivity.instance.orderDate_Print;
        try {
            Bitmap bitmapCombine = Bitmap.createBitmap(1241+59, 1603+59, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);

            canvasCombine.drawBitmap(MainActivity.instance.bitmapLeft, 0, 0, null);
            canvasCombine.drawBitmap(MainActivity.instance.bitmapRight, 620, 0, null);
            canvasCombine.drawBitmap(bitmapDB, 0, 0, null);

            String gender = orderItems.get(currentID).skuStr.substring(0, 1).equals("M") ? "男" : "女";
            //String intSize = orderItems.get(currentID).sizeStr.substring(orderItems.get(currentID).sizeStr.indexOf("US") + 3, orderItems.get(currentID).sizeStr.indexOf("-"));
            String intSize = orderItems.get(currentID).sizeStr;
            intSize = reSize(orderItems.get(currentID).skuStr.substring(0, 1), intSize);

            canvasCombine.drawText(gender + intSize + orderItems.get(currentID).color, 563, 1599, paint);
            canvasCombine.save();
            canvasCombine.rotate(90, 10, 1000);
            canvasCombine.drawText(time + "  " + orderItems.get(currentID).order_number, 10, 996, paint);
            canvasCombine.drawText("流水号：" + (currentID + 1), 300, 996, paintRed);
            canvasCombine.restore();

            setScale(orderItems.get(currentID).skuStr.substring(0, 1) + intSize);
            Bitmap bitmapPrint = Bitmap.createScaledBitmap(bitmapCombine, (int) ((1241 + 59) * scaleX), (int) ((1603 + 59) * scaleY), true);
            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String nameCombine = orderItems.get(currentID).sku + intSize + "_" + gender + orderItems.get(currentID).color + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapPrint, fileSave, 148);

            //释放bitmap
            bitmapDB.recycle();
            bitmapCombine.recycle();
            bitmapPrint.recycle();

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
            Label label0 = new Label(0, currentID + 1, orderItems.get(currentID).order_number + orderItems.get(currentID).sku + orderItems.get(currentID).skuStr.substring(0, 1) + intSize + printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID + 1, orderItems.get(currentID).sku + orderItems.get(currentID).skuStr.substring(0, 1) + intSize + printColor);
            sheet.addCell(label1);
            Number number2 = new Number(2, currentID+1, orderItems.get(currentID).num);
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
            if(MainActivity.instance.leftsucceed&&MainActivity.instance.rightsucceed)
                remix();
        }
    }

    void setScale(String size){
        switch (size) {
            case "M7":
                scaleX = 1.0514f;
                scaleY = 1.03f;
                break;
            case "M9":
                scaleX = 1.088f;
                scaleY = 1.074f;
                break;
            case "M11":
                scaleX = 1.161f;
                scaleY = 1.147f;
                break;
            case "W5":
                scaleX = 1.011f;
                scaleY = 0.938f;
                break;
            case "W7":
                scaleX = 1.049f;
                scaleY = 0.998f;
                break;
            case "W9":
                scaleX = 1.098f;
                scaleY = 1.065f;
                break;

            default:
                showDialogSizeWrong(orderItems.get(currentID).order_number);
                break;
        }
    }

    String reSize(String gender, String size){
        String intSize = size;
        if (gender.equals("W")) {
            if (size.equals("S")) {
                intSize = "5";
            } else if (size.equals("M")) {
                intSize = "7";
            } else if (size.equals("L")) {
                intSize = "9";
            }
        } else {
            if (size.equals("S")) {
                intSize = "7";
            } else if (size.equals("M")) {
                intSize = "9";
            } else if (size.equals("L")) {
                intSize = "11";
            }
        }
        return intSize;
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
