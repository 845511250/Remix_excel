package com.example.zuoyun.remix_excel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentDEold extends BaseFragment {
    Context context;
//    String sdCardPath = "/mnt/asec/share";
String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    int currentID;
    String childPath;

    @BindView(R.id.iv_fg2_leftup)
    ImageView iv_leftup;
    @BindView(R.id.iv_fg2_leftdown)
    ImageView iv_leftdown;
    @BindView(R.id.iv_fg2_rightup)
    ImageView iv_rightup;
    @BindView(R.id.iv_fg2_rightdown)
    ImageView iv_rightdown;
    @BindView(R.id.bt_remix)
    Button bt_remix;
    @BindView(R.id.iv_sample1)
    ImageView iv_sample1;
    @BindView(R.id.iv_sample2)
    ImageView iv_sample2;
    @BindView(R.id.sb1)
    SeekBar sb1;
    @BindView(R.id.sbrotate1)
    SeekBar sbrotate1;
    @BindView(R.id.sb2)
    SeekBar sb2;
    @BindView(R.id.sbrotate2)
    SeekBar sbrotate2;

    int startseek1=0,startseek2=0;
    int startseekrotate1=0,startseekrotate2=0;
    private PointF startPoint1 = new PointF(),startPoint2=new PointF();//起始点
    private Matrix matrix1up = new Matrix(),matrix1down=new Matrix(),matrix2up=new Matrix(), matrix2down = new Matrix();    //缩放比例
    Matrix matrixprint1=new Matrix(), matrixprint2 = new Matrix();

    @Override
    public int getLayout() {
        return R.layout.fragment_de_old;
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
            public void listen(int message,String sampleurl) {
                if(message==0){
                    iv_leftup.setImageDrawable(null);
                    iv_leftdown.setImageDrawable(null);
                    iv_rightup.setImageDrawable(null);
                    iv_rightdown.setImageDrawable(null);
                    Log.e("fragment_de_old", "message0");
                }
                else if (message == 1) {
                    Log.e("fragment_de_old", "message1");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_leftup.setImageBitmap(MainActivity.instance.bitmapLeft);
//                    iv_leftdown.setImageBitmap(MainActivity.instance.bitmapLeft);
//                    Glide.with(context).load(sampleurl).into(iv_sample1);
                    checkremix();
                }
                else if(message==2){
                    Log.e("fragment_de_old", "message2");
                    bt_remix.setClickable(true);
                    if(!MainActivity.instance.cb_fastmode.isChecked())
                        iv_rightup.setImageBitmap(MainActivity.instance.bitmapRight);
//                    iv_rightdown.setImageBitmap(MainActivity.instance.bitmapRight);
//                    Glide.with(context).load(sampleurl).into(iv_sample2);
                    checkremix();
                }
                else if (message==3){
                    bt_remix.setClickable(false);
                }
                else if (message == 10) {
                    remix();
                }
            }
        });

        matrixprint1.postScale(2.843f, 2.843f);
        matrix1up.postScale(0.2049f, 0.2049f);
        matrixprint1.postScale(0.2049f, 0.2049f);
        matrix1up.postTranslate(0, 0);
        matrixprint1.postTranslate(0 * 2.843f, 0 * 2.843f);
        iv_leftup.setImageMatrix(matrix1up);
        matrix1down.postScale(0.2425f, 0.2425f);
        matrix1down.postTranslate(190, 133);
        iv_leftdown.setImageMatrix(matrix1down);

        matrixprint2.postScale(2.843f, 2.843f);
        matrix2up.postScale(0.2049f, 0.2049f);
        matrixprint2.postScale(0.2049f, 0.2049f);
        matrix2up.postTranslate(17, 0);
        matrixprint2.postTranslate(17 * 2.843f, 0 * 2.843f);
        iv_rightup.setImageMatrix(matrix2up);
        matrix2down.postScale(0.2425f, 0.2425f);
        matrix2down.postTranslate(86, 123);
        iv_rightdown.setImageMatrix(matrix2down);


        iv_leftup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case ACTION_DOWN:
                        Log.e("sds","ACTION_DOWN");
                        startPoint1.set(event.getX(),event.getY());
                        break;
                    case ACTION_MOVE:
                        float dx = event.getX() - startPoint1.x;
                        float dy = event.getY() - startPoint1.y;
                        matrix1up.postTranslate(dx, dy);
                        iv_leftup.setImageMatrix(matrix1up);
                        matrix1down.postTranslate(dx, dy);
                        iv_leftdown.setImageMatrix(matrix1down);
                        matrixprint1.postTranslate(dx * 2.843f, dy * 2.843f);
                        startPoint1.set(event.getX(),event.getY());
                        break;
                }

                return true;
            }
        });
        iv_rightup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case ACTION_DOWN:
                        Log.e("sds","ACTION_DOWN");
                        startPoint2.set(event.getX(),event.getY());
                        break;
                    case ACTION_MOVE:
                        float dx = event.getX() - startPoint2.x;
                        float dy = event.getY() - startPoint2.y;
                        matrix2up.postTranslate(dx, dy);
                        iv_rightup.setImageMatrix(matrix2up);
                        matrix2down.postTranslate(dx, dy);
                        iv_rightdown.setImageMatrix(matrix2down);
                        matrixprint2.postTranslate(dx * 2.843f, dy * 2.843f);
                        startPoint2.set(event.getX(),event.getY());
                        break;
                }

                return true;
            }
        });

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>=10){
                    matrix1up.postScale(1f * progress / startseek1, 1f * progress / startseek1,-78, -22);
                    iv_leftup.setImageMatrix(matrix1up);
                    matrix1down.postScale(1f * progress / startseek1, 1f * progress / startseek1,190, 133);
                    iv_leftdown.setImageMatrix(matrix1down);
                    matrixprint1.postScale(1f * progress / startseek1, 1f * progress / startseek1,-78 * 2.843f, -22 * 2.843f);
                    startseek1 = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startseek1 = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()<10)
                    seekBar.setProgress(10);
            }
        });
        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>=10){
                    matrix2up.postScale(1f * progress / startseek2, 1f * progress / startseek2,2, -24);
                    iv_rightup.setImageMatrix(matrix2up);
                    matrix2down.postScale(1f * progress / startseek2, 1f * progress / startseek2,86, 123);
                    iv_rightdown.setImageMatrix(matrix2down);
                    matrixprint2.postScale(1f * progress / startseek2, 1f * progress / startseek2, 2 * 2.843f, -24 * 2.843f);
                    startseek2 = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startseek2 = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()<10)
                    seekBar.setProgress(10);
            }
        });

        //******************************************************************************************
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
                remixx();
            }
        }.start();

    }

    public void remixx(){
        //刀版left
        Bitmap bitmapDBLeft = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.low36left);
        Bitmap bitmapRemixLeft = Bitmap.createBitmap(bitmapDBLeft.getWidth(), bitmapDBLeft.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasLeft = new Canvas(bitmapRemixLeft);
        canvasLeft.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasLeft.drawBitmap(MainActivity.instance.bitmapLeft, matrixprint1, null);
        canvasLeft.drawBitmap(bitmapDBLeft,0,0,null);

        //刀版right
        Bitmap bitmapDBRight = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.low36right);
        Bitmap bitmapRemixRight = Bitmap.createBitmap(bitmapDBRight.getWidth(), bitmapDBRight.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasRight = new Canvas(bitmapRemixRight);
        canvasRight.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvasRight.drawBitmap(MainActivity.instance.bitmapRight, matrixprint2, null);
        canvasRight.drawBitmap(bitmapDBRight,0,0,null);

        //左脚左
        Bitmap bitmapLL = Bitmap.createBitmap(bitmapRemixLeft,0,0,bitmapRemixLeft.getWidth(),bitmapRemixLeft.getHeight());
        Canvas canvasLL = new Canvas(bitmapLL);
        canvasLL.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //左脚右
        Bitmap bitmapLR = Bitmap.createBitmap(bitmapRemixRight,0,0,bitmapRemixRight.getWidth(),bitmapRemixRight.getHeight());
        Canvas canvasLR = new Canvas(bitmapLR);
        canvasLR.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        //
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);

        Paint paintRed = new Paint();
        paintRed.setColor(0xffff0000);
        paintRed.setTextSize(40);
        paintRed.setTypeface(Typeface.DEFAULT_BOLD);
        paintRed.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(0xffffffff);
        rectPaint.setStyle(Paint.Style.FILL);

        String time = MainActivity.instance.orderDate_Print;

        canvasLeft.drawRect(70,470,350,520,rectPaint);
        canvasLeft.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color,70,510,paint);
        canvasLeft.drawText((currentID+1)+" 右内",180,510,paintRed);
        canvasLeft.drawText(orderItems.get(currentID).order_number,110,555,paint);
        canvasLeft.drawRect(1100, 470, 1300, 520, rectPaint);
        canvasLeft.drawText(time, 1100, 510, paint);

        canvasRight.drawRect(990,470,1270,520,rectPaint);
        canvasRight.drawText((currentID+1)+ " 右外", 990,510, paintRed);
        canvasRight.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color,1180,510,paint);
        canvasRight.drawText(orderItems.get(currentID).order_number,1020,555,paint);
        canvasRight.drawRect(50, 470, 250, 520, rectPaint);
        canvasRight.drawText(time, 50, 510, paint);

        canvasLL.drawRect(70,470,350,520,rectPaint);
        canvasLL.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color,70,510,paint);
        canvasLL.drawText((currentID+1) + " 左外",180,510, paintRed);
        canvasLL.drawText(orderItems.get(currentID).order_number,110,555,paint);
        canvasLL.drawRect(1100, 470, 1300, 520, rectPaint);
        canvasLL.drawText(time, 1100, 510, paint);

        canvasLR.drawRect(990,470,1270,520,rectPaint);
        canvasLR.drawText((currentID+1)+" 左内",990,510, paintRed);
        canvasLR.drawText(orderItems.get(currentID).size+orderItems.get(currentID).color,1180,510,paint);
        canvasLR.drawText(orderItems.get(currentID).order_number,1020,555,paint);
        canvasLR.drawRect(50, 470, 250, 520, rectPaint);
        canvasLR.drawText(time, 50, 510, paint);

        try {
            File file=new File(sdCardPath+"/生产图/"+childPath+"/");
            if(!file.exists())
                file.mkdirs();
            float scaley = getScale(orderItems.get(currentID).size+1);

            Bitmap bitmapCombine = Bitmap.createBitmap(1342, (int)(2388*scaley)+180, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine = new Canvas(bitmapCombine);
            canvasCombine.drawColor(0xffdddddd);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            //rr
            Matrix matrixCombine = new Matrix();
            matrixCombine.postScale(1, scaley);
            canvasCombine.drawBitmap(bitmapRemixRight,matrixCombine, null);
            canvasCombine.drawRect(0,597*scaley,1342,597*scaley+60,rectPaint);
            //rl
            matrixCombine.reset();
            matrixCombine.postRotate(180);
            matrixCombine.postTranslate(1342, 597+597*scaley+60);
            matrixCombine.postScale(1, scaley, 0, 597 * scaley+60);
            canvasCombine.drawBitmap(bitmapRemixLeft, matrixCombine, null);
            canvasCombine.drawRect(0,1194*scaley+60,1342,1194*scaley+120,rectPaint);
            //lr
            matrixCombine.reset();
            matrixCombine.postTranslate(0, 1194*scaley+120);
            matrixCombine.postScale(1, scaley, 0, 1194 * scaley+120);
            canvasCombine.drawBitmap(bitmapLR, matrixCombine, null);
            canvasCombine.drawRect(0,1791*scaley+120,1342,1791*scaley+180,rectPaint);
            //ll
            matrixCombine.reset();
            matrixCombine.postRotate(180);
            matrixCombine.postTranslate(1342, 597+1791*scaley+180);
            matrixCombine.postScale(1, scaley, 0, 1791 * scaley+180);
            canvasCombine.drawBitmap(bitmapLL, matrixCombine, null);

            String printColor = orderItems.get(currentID).color.equals("黑") ? "B" : "W";
            String nameCombine = orderItems.get(currentID).order_number + orderItems.get(currentID).sku + orderItems.get(currentID).size+printColor + ".jpg";
            FileOutputStream out;
            if(MainActivity.instance.cb_classify.isChecked()){
                String pathClassify = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/" + orderItems.get(currentID).size + printColor + "/";
                File fileClassify=new File(pathClassify);
                if(!fileClassify.exists())
                    fileClassify.mkdirs();
                out = new FileOutputStream(pathClassify + nameCombine);
            } else
                out = new FileOutputStream(sdCardPath + "/生产图/" + childPath + "/" + nameCombine);
            bitmapCombine.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //释放bitmap
            bitmapDBLeft.recycle();
            bitmapRemixLeft.recycle();
            bitmapDBRight.recycle();
            bitmapRemixRight.recycle();
            bitmapLL.recycle();
            bitmapLR.recycle();
            bitmapCombine.recycle();
            MainActivity.instance.bitmapLeft.recycle();
            MainActivity.instance.bitmapRight.recycle();

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
            Label label0 = new Label(0, currentID+1, orderItems.get(currentID).order_number+orderItems.get(currentID).sku+orderItems.get(currentID).size+printColor);
            sheet.addCell(label0);
            Label label1 = new Label(1, currentID+1, orderItems.get(currentID).sku+orderItems.get(currentID).size+printColor);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show();
                if(MainActivity.instance.tb_auto.isChecked()){
                    MainActivity.instance.setnext();
                }
            }
        });
    }
    public void checkremix(){
        if (MainActivity.instance.tb_auto.isChecked()){
            if(MainActivity.instance.leftsucceed&&MainActivity.instance.rightsucceed)
                remix();
        }
    }
    public float getScale(int size){
        float scale = 1.0f;
        switch (size) {
            case 32:
                scale = 1.0754f;
                break;

            case 37:
                scale = 1.0f;
                break;
            case 38:
                scale = 0.978f;
                break;
            case 39:
                scale = 0.963f;
                break;
            case 40:
                scale = 0.967f;
                break;
            case 41:
                scale = 0.953f;
                break;
            case 42:
                scale = 0.948f;
                break;
            case 43:
                scale = 0.944f;
                break;
            case 44:
                scale = 0.931f;
                break;
            case 45:
                scale = 0.924f;
                break;
            case 46:
                scale = 0.92f;
                break;
            case 47:
                scale = 0.917f;
                break;
        }
        return scale;
    }

}
