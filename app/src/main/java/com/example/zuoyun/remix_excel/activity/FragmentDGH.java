package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

public class FragmentDGH extends BaseFragment {
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

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_pillow.setImageDrawable(null);
                } else if (message == 4) {
                    Log.e("fragment_dg", "message4");
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_pillow.setImageBitmap(MainActivity.instance.bitmapPillow);
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
        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(28);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(28);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        String time = MainActivity.instance.orderDate_Print;
        try {
            Bitmap bitmapPillow;
            Bitmap bitmapremix;
            Canvas canvasremix;
            Bitmap bitmapBorderDG = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.border_dg);
            Bitmap bitmapBorderDH = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.border_dh);

            if (orderItems.get(currentID).sku.equals("DG")) {
                bitmapPillow = Bitmap.createScaledBitmap(MainActivity.instance.bitmapPillow, 2300, 2300, true);
                bitmapremix = Bitmap.createBitmap(2300+93, 2300+93, Bitmap.Config.ARGB_8888);//43*43cm
                canvasremix = new Canvas(bitmapremix);
                canvasremix.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                canvasremix.drawColor(0xffffffff);
                canvasremix.drawBitmap(bitmapPillow, 0, 0, null);
                canvasremix.drawBitmap(bitmapBorderDG, 0, 0, null);

                canvasremix.drawRect(600, 2274, 1270, 2300, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 601, 2297, paintRed);
                canvasremix.drawText(time+"    "+orderItems.get(currentID).order_number+"    抱枕套", 852, 2297, paint);
            } else {
                bitmapPillow = Bitmap.createScaledBitmap(MainActivity.instance.bitmapPillow, 2000, 2000, true);
                bitmapremix = Bitmap.createBitmap(2000+2100, 2000+220, Bitmap.Config.ARGB_8888);//88.2*43cm
                canvasremix = new Canvas(bitmapremix);
                canvasremix.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                canvasremix.drawColor(0xffffffff);
                canvasremix.drawBitmap(bitmapPillow, 0, 0, null);
                canvasremix.drawBitmap(bitmapBorderDH, 0, 0, null);
                canvasremix.drawBitmap(bitmapPillow, 2100, 0, null);
                canvasremix.drawBitmap(bitmapBorderDH, 2100, 0, null);

                canvasremix.drawRect(600, 1974, 1270, 2000, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 601, 1997, paintRed);
                canvasremix.drawText(time+"    "+orderItems.get(currentID).order_number+"    购物袋", 852, 1997, paint);

                canvasremix.drawRect(600+2100, 1974, 1270+2100, 2000, rectPaint);
                canvasremix.drawText(orderItems.get(currentID).newCode, 601 + 2100, 1997, paintRed);
                canvasremix.drawText(time+"    "+orderItems.get(currentID).order_number+"    购物袋", 852+2100, 1997, paint);
            }
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku : "";
            String nameCombine = noNewCode + orderItems.get(currentID).sku + orderItems.get(currentID).newCode + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapremix, fileSave, 118);

            //释放bitmap
            bitmapremix.recycle();
            bitmapPillow.recycle();
            bitmapBorderDG.recycle();
            bitmapBorderDH.recycle();

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
