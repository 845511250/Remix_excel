package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
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

public class FragmentKY extends BaseFragment {
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

    int sideWidth, sideHeight, mainWidth, mainHeight, tongueWidth, tongueHeight;
    int sideMarginTop;
    int num;
    String strPlus = "";
    int intPlus = 1;

    Paint rectPaint, paint, paintRed, paintBlue,paintRectBlack;
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

        rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        paintRectBlack = new Paint();
        paintRectBlack.setColor(0xff000000);
        paintRectBlack.setStyle(Paint.Style.FILL);

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(16);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(18);
        paintRed.setAntiAlias(true);


        time = MainActivity.instance.orderDate_Print;


        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_pillow.setImageDrawable(null);
                    Log.e("fragment2", "message0");
                }  else if (message == 4) {
                    Log.e("aaa", "message4");
                    bt_remix.setClickable(true);
                    checkremix();
                }  else if (message == 3) {
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
                    intPlus = orderItems.get(currentID).num - num + 1;
                    for(int i=0;i<currentID;i++) {
                        if (orderItems.get(currentID).order_number.equals(orderItems.get(i).order_number)) {
                            intPlus += orderItems.get(i).num;
                        }
                    }
                    strPlus = intPlus == 1 ? "" : "(" + intPlus + ")";
                    remixx();
                }
            }
        }.start();

    }


    void drawTextKYS(Canvas canvas) {
        canvas.save();
        canvas.rotate(53.7f, 668, 719);
        canvas.drawRect(668, 719 - 15, 668 + 195, 719, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + orderItems.get(currentID).color + " " + orderItems.get(currentID).order_number + strPlus + " " + time, 668, 719 - 2, paint);
        canvas.restore();
    }
    void drawTextKYL(Canvas canvas) {
        canvas.save();
        canvas.rotate(50.2f, 730, 882);
        canvas.drawRect(730, 882 - 15, 730 + 195, 882, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + orderItems.get(currentID).color + " " + orderItems.get(currentID).order_number + strPlus + " " + time, 730, 882 - 2, paint);
        canvas.restore();
    }
    void drawBigTextKYS(Canvas canvas) {
        canvas.save();
        canvas.rotate(53.7f, 668, 719);
        canvas.drawRect(668, 719 - 15, 668 + 195, 719, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + orderItems.get(currentID).color + orderItems.get(currentID).order_number + "共" + orderItems.get(currentID).newCode + "个" + strPlus + " " + time, 668, 719 - 2, paint);
        canvas.restore();

        if (orderItems.get(currentID).platform.equals("zy")) {
            paintRed.setTextSize(35);
            canvas.drawText(time + " " + orderItems.get(currentID).color, 1100, 838, paintRed);
            canvas.drawText(orderItems.get(currentID).order_number + " 共" + orderItems.get(currentID).newCode + "个", 950, 885, paintRed);
        }
    }
    void drawBigTextKYL(Canvas canvas) {
        canvas.save();
        canvas.rotate(50.2f, 721, 872);
        canvas.drawRect(721, 872 - 15, 721 + 210, 872, rectPaint);
        canvas.drawText(orderItems.get(currentID).sku + orderItems.get(currentID).color + orderItems.get(currentID).order_number + "共" + orderItems.get(currentID).newCode + "个" + strPlus + " " + time, 721, 872 - 2, paint);
        canvas.restore();

        if (orderItems.get(currentID).platform.equals("zy")) {
            paintRed.setTextSize(36);
            canvas.drawText(orderItems.get(currentID).order_number + " 共" + orderItems.get(currentID).newCode + "个", 1050, 70, paintRed);
        }
    }

    public void remixx(){
        Bitmap bitmapTemp = null;
        Bitmap bitmapDB = null;


        if (orderItems.get(currentID).sku.equals("KYL")) {
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 45, 25, 1410, 1050);
            Canvas canvasTemp = new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getResources(), R.drawable.kyl);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            if (orderItems.get(currentID).platform.equals("zy")) {
                drawBigTextKYL(canvasTemp);
            } else {
                drawTextKYL(canvasTemp);
            }
            bitmapDB.recycle();
        } else if (orderItems.get(currentID).sku.equals("KYS")) {
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 49, 78, 1406, 963);
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, 1300, 890, true);
            Canvas canvasTemp = new Canvas(bitmapTemp);
            canvasTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            bitmapDB = BitmapFactory.decodeResource(getResources(), R.drawable.kys);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            if (orderItems.get(currentID).platform.equals("zy")) {
                drawBigTextKYS(canvasTemp);
            } else {
                drawTextKYS(canvasTemp);
            }
            bitmapDB.recycle();
        }


        try {
            String nameCombine = orderItems.get(currentID).sku + "_" + orderItems.get(currentID).color + "耳挂_" + orderItems.get(currentID).order_number + strPlus + ".jpg";
            if (orderItems.get(currentID).platform.equals("zy")) {
                nameCombine = orderItems.get(currentID).order_number + "_" + orderItems.get(currentID).sku + "_" + orderItems.get(currentID).color + "耳挂_共" + orderItems.get(currentID).newCode + "个" + strPlus + ".jpg";
            }

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapTemp, fileSave, 148);

            //释放bitmap
            bitmapTemp.recycle();

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
            MainActivity.recycleExcelImages();
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

}
