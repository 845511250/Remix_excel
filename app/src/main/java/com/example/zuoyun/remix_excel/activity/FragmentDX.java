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

import static com.example.zuoyun.remix_excel.activity.MainActivity.doRecycle;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDX extends BaseFragment {
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

    int width, height;
    int num;
    String strPlus = "";
    int intPlus = 1;
    Paint rectPaint, paint, paintRed, paintBlue;
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

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(30);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        paintBlue = new Paint();
        paintBlue.setColor(0xff0000ff);
        paintBlue.setTextSize(30);
        paintBlue.setTypeface(Typeface.DEFAULT_BOLD);
        paintBlue.setAntiAlias(true);

        time = MainActivity.instance.orderDate_Print;

        MainActivity.instance.setMessageListener(new MainActivity.MessageListener() {
            @Override
            public void listen(int message, String sampleurl) {
                if (message == 0) {
                    iv_pillow.setImageDrawable(null);
                } else if (message == 4) {
                    Log.e("fragmentDL", "message4");
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

    void drawText(Canvas canvas, int left, int bottom) {
        canvas.drawRect(left, bottom - 28, left + 500, bottom, rectPaint);
        canvas.drawText(time + "   " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).sizeStr, left, bottom - 2, paint);
        canvas.drawText(orderItems.get(currentID).newCode, left + 330, bottom - 2, paintRed);
    }
    void drawTextRotate(Canvas canvas, int degree, int left, int bottom) {
        canvas.save();
        canvas.rotate(degree, (float) left, (float) bottom);
        canvas.drawRect(left, bottom - 28, left + 500, bottom, rectPaint);
        canvas.drawText(time + "   " + orderItems.get(currentID).order_number + "   " + orderItems.get(currentID).sizeStr, left, bottom - 2, paint);
        canvas.drawText(orderItems.get(currentID).newCode, left + 330, bottom - 2, paintRed);
        canvas.restore();
    }

    public void remixx(){

        String sizeStr = orderItems.get(currentID).sizeStr;
        if (sizeStr.startsWith("CHILD")) {
            sizeStr = "S";
        }

        //bitmapCombine
        Bitmap bitmapCombine;
        if (sizeStr.equals("S")) {
            bitmapCombine = Bitmap.createBitmap(3802, 4007 + 180, Bitmap.Config.ARGB_8888);
        } else {
            if (sizeStr.equals("M"))
                bitmapCombine = Bitmap.createBitmap(4650, 4964 + 180, Bitmap.Config.ARGB_8888);
            else if (sizeStr.equals("L"))
                bitmapCombine = Bitmap.createBitmap(5131, 5278 + 180, Bitmap.Config.ARGB_8888);
            else {
                showDialogSizeWrong(orderItems.get(currentID).order_number);
                return;
            }
        }

        Canvas canvasCombine = new Canvas(bitmapCombine);
        canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasCombine.drawColor(0xffffffff);
        Matrix matrixCombine = new Matrix();

        //part1
        Bitmap bitmapDBPart1 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part1);
        Bitmap bitmapPart1 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 431, 59, 3342, 807);
        Canvas canvasPart1 = new Canvas(bitmapPart1);
        canvasPart1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart1.drawBitmap(bitmapDBPart1, 0, 0, null);
        drawText(canvasPart1, 1110, 807);
        doRecycle(bitmapDBPart1);

        setScale(sizeStr, 1);
        bitmapPart1 = Bitmap.createScaledBitmap(bitmapPart1, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 847);
            canvasCombine.drawBitmap(bitmapPart1, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 1040);
            canvasCombine.drawBitmap(bitmapPart1, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 1160);
            canvasCombine.drawBitmap(bitmapPart1, matrixCombine, null);
        }
        doRecycle(bitmapPart1);

        //part2
        Bitmap bitmapDBPart2 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part2);
        Bitmap bitmapPart2 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 431, 736, 3342, 386);
        Canvas canvasPart2 = new Canvas(bitmapPart2);
        canvasPart2.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart2.drawBitmap(bitmapDBPart2, 0, 0, null);
        drawText(canvasPart2, 1110, 28);
        drawText(canvasPart2, 1110, 386);
        doRecycle(bitmapDBPart2);

        setScale(sizeStr, 2);
        bitmapPart2 = Bitmap.createScaledBitmap(bitmapPart2, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 1528);
            canvasCombine.drawBitmap(bitmapPart2, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 1957);
            canvasCombine.drawBitmap(bitmapPart2, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 2071);
            canvasCombine.drawBitmap(bitmapPart2, matrixCombine, null);
        }
        doRecycle(bitmapPart2);

        //part3
        Bitmap bitmapDBPart3 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part3);
        Bitmap bitmapPart3 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1205, 1052, 1787, 2358);
        Canvas canvasPart3 = new Canvas(bitmapPart3);
        canvasPart3.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart3.drawBitmap(bitmapDBPart3, 0, 0, null);
        //drawText(canvasPart3, 744, 30);
        drawText(canvasPart3, 424, 2358);
        doRecycle(bitmapDBPart3);

        setScale(sizeStr, 3);
        bitmapPart3 = Bitmap.createScaledBitmap(bitmapPart3, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(1902, 1911);
            canvasCombine.drawBitmap(bitmapPart3, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2366, 2424);
            canvasCombine.drawBitmap(bitmapPart3, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2599, 2561);
            canvasCombine.drawBitmap(bitmapPart3, matrixCombine, null);
        }
        doRecycle(bitmapPart3);

        //part4
        Bitmap bitmapDBPart4 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part4);
        Bitmap bitmapPart4 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1025, 2134, 2135, 619);
        Canvas canvasPart4 = new Canvas(bitmapPart4);
        canvasPart4.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart4.drawBitmap(bitmapDBPart4, 0, 0, null);
        //drawText(canvasPart4, 924, 28);
        drawText(canvasPart4, 600, 619);
        drawTextRotate(canvasPart4, 90, 5, 220);
        drawTextRotate(canvasPart4, -90, 2135, 570);
        doRecycle(bitmapDBPart4);

        setScale(sizeStr, 4);
        bitmapPart4 = Bitmap.createScaledBitmap(bitmapPart4, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 3434);
            canvasCombine.drawBitmap(bitmapPart4, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 4343);
            canvasCombine.drawBitmap(bitmapPart4, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 4657);
            canvasCombine.drawBitmap(bitmapPart4, matrixCombine, null);
        }
        doRecycle(bitmapPart4);

        //part5
        Bitmap bitmapDBPart5 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part5);
        Bitmap bitmapPart5 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1032, 2636, 2132, 1143);
        Canvas canvasPart5 = new Canvas(bitmapPart5);
        canvasPart5.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart5.drawBitmap(bitmapDBPart5, 0, 0, null);
        drawText(canvasPart5, 600, 28);
        drawText(canvasPart5, 600, 1143);
        drawTextRotate(canvasPart5, 90, 2, 220);
        drawTextRotate(canvasPart5, -90, 2132, 570);
        doRecycle(bitmapDBPart5);

        setScale(sizeStr, 5);
        bitmapPart5 = Bitmap.createScaledBitmap(bitmapPart5, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(2905, 1979);
            canvasCombine.drawBitmap(bitmapPart5, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(3577, 2537);
            canvasCombine.drawBitmap(bitmapPart5, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postRotate(90);
            matrixCombine.postTranslate(3910, 2745);
            canvasCombine.drawBitmap(bitmapPart5, matrixCombine, null);
        }
        doRecycle(bitmapPart5);

        //part6
        Bitmap bitmapDBPart6 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part6);
        Bitmap bitmapPart6 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 155, 2231, 992, 1347);
        Canvas canvasPart6 = new Canvas(bitmapPart6);
        canvasPart6.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart6.drawBitmap(bitmapDBPart6, 0, 0, null);
        drawTextRotate(canvasPart6, 91, 962, 500);
        doRecycle(bitmapDBPart6);

        setScale(sizeStr, 6);
        bitmapPart6 = Bitmap.createScaledBitmap(bitmapPart6, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postTranslate(2975, 1148);
            canvasCombine.drawBitmap(bitmapPart6, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postTranslate(3640, 1275);
            canvasCombine.drawBitmap(bitmapPart6, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postTranslate(3979, 1228);
            canvasCombine.drawBitmap(bitmapPart6, matrixCombine, null);
        }
        doRecycle(bitmapPart6);

        //part7
        Bitmap bitmapDBPart7 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part7);
        Bitmap bitmapPart7 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3044, 2231, 992, 1347);
        Canvas canvasPart7 = new Canvas(bitmapPart7);
        canvasPart7.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasPart7.drawBitmap(bitmapDBPart7, 0, 0, null);
        drawTextRotate(canvasPart7, -91, 40, 850);
        doRecycle(bitmapDBPart7);

        setScale(sizeStr, 7);
        bitmapPart7 = Bitmap.createScaledBitmap(bitmapPart7, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postTranslate(2975, 2452);
            canvasCombine.drawBitmap(bitmapPart7, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postTranslate(3640, 2899);
            canvasCombine.drawBitmap(bitmapPart7, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postTranslate(3979, 2886);
            canvasCombine.drawBitmap(bitmapPart7, matrixCombine, null);
        }
        doRecycle(bitmapPart7);

        //part8
        Bitmap bitmapDBPart8 = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.dx_part8);
        Bitmap bitmapPart8 = Bitmap.createBitmap(3983, 965, Bitmap.Config.ARGB_8888);
        Canvas canvasPart8 = new Canvas(bitmapPart8);
        canvasPart8.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Bitmap bitmapPart8_1 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 181, 2256, 965, 802);
        Bitmap bitmapPart8_2 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 923, 3661, 2378, 965);
        Bitmap bitmapPart8_3 = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3068, 2256, 965, 803);
        matrixCombine.reset();
        matrixCombine.postRotate(-90);
        matrixCombine.postTranslate(0, 965);
        canvasPart8.drawBitmap(bitmapPart8_1, matrixCombine, null);
        canvasPart8.drawBitmap(bitmapPart8_2, 802, 0, null);
        matrixCombine.reset();
        matrixCombine.postRotate(90);
        matrixCombine.postTranslate(3983, 0);
        canvasPart8.drawBitmap(bitmapPart8_3, matrixCombine, null);
        canvasPart8.drawBitmap(bitmapDBPart8, 0, 0, null);
        drawText(canvasPart8, 1537, 28);

        doRecycle(bitmapPart8_1);
        doRecycle(bitmapPart8_2);
        doRecycle(bitmapPart8_3);
        doRecycle(bitmapDBPart8);

        setScale(sizeStr, 8);
        bitmapPart8 = Bitmap.createScaledBitmap(bitmapPart8, width, height, true);
        if(sizeStr.equals("S")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 0);
            canvasCombine.drawBitmap(bitmapPart8, matrixCombine, null);
        }
        else if(sizeStr.equals("M")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 0);
            canvasCombine.drawBitmap(bitmapPart8, matrixCombine, null);
        }
        else if(sizeStr.equals("L")){
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 0);
            canvasCombine.drawBitmap(bitmapPart8, matrixCombine, null);
        }
        doRecycle(bitmapPart8);

        try {
            String noNewCode = orderItems.get(currentID).newCode.equals("") ? orderItems.get(currentID).sku + orderItems.get(currentID).sizeStr : "";
            String nameCombine = noNewCode + orderItems.get(currentID).sku + orderItems.get(currentID).newCode + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 150);

            //释放bitmap
            doRecycle(bitmapCombine);
            doRecycle(MainActivity.instance.bitmapPillow);

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

    void setScale(String size, int part) {
        switch (size + part) {
            case "S1":
                width = 2717;
                height = 609;
                break;
            case "S2":
                width = 2717;
                height = 314;
                break;
            case "S3":
                width = 1459;
                height = 1908;
                break;
            case "S4":
                width = 1743;
                height = 573;
                break;
            case "S5":
                width = 1743;
                height = 934;
                break;
            case "S6":
                width = 827;
                height = 1182;
                break;
            case "S7":
                width = 827;
                height = 1182;
                break;
            case "S8":
                width = 3308;
                height = 780;
                break;

            case "M1":
                width = 3426;
                height = 727;
                break;
            case "M2":
                width = 3426;
                height = 384;
                break;
            case "M3":
                width = 1843;
                height = 2363;
                break;
            case "M4":
                width = 2186;
                height = 621;
                break;
            case "M5":
                width = 2186;
                height = 1152;
                break;
            case "M6":
                width = 1010;
                height = 1371;
                break;
            case "M7":
                width = 1010;
                height = 1371;
                break;
            case "M8":
                width = 4076;
                height = 969;
                break;

            case "L1":
                width = 3780;
                height = 827;
                break;
            case "L2":
                width = 3780;
                height = 420;
                break;
            case "L3":
                width = 2009;
                height = 2599;
                break;
            case "L4":
                width = 2363;
                height = 638;
                break;
            case "L5":
                width = 2363;
                height = 1235;
                break;
            case "L6":
                width = 1152;
                height = 1501;
                break;
            case "L7":
                width = 1152;
                height = 1501;
                break;
            case "L8":
                width = 4489;
                height = 1099;
                break;
            default:
                showDialogSizeWrong(orderItems.get(currentID).order_number);
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
