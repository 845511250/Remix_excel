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

public class FragmentFG extends BaseFragment {
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

    int width_s,width_m,width_l,width_xl;
    int height_s,height_m,height_l,height_xl;

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
                } else if (message == 4) {
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
                setScale();
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

    void drawText(Canvas canvas, int left, int bottom, String str) {
        canvas.drawRect(left, bottom - 30, left + 1200, bottom, rectPaint);
        canvas.drawText("画框" + orderItems.get(currentID).sizeStr + "件套  " + time + "  " + orderItems.get(currentID).order_number, left, bottom - 3, paint);
        canvas.drawText(orderItems.get(currentID).newCode + "   验片码" + orderItems.get(currentID).codeE, left + 600, bottom - 3, paintRed);
        canvas.drawText(str, left + 1000, bottom - 3, paintRed);
    }

    public void remixx(){
        Bitmap bitmapCombine = null;

        if (orderItems.get(currentID).sizeStr.equals("3")) {
            bitmapCombine = Bitmap.createBitmap(width_xl * 3 + 50, height_xl+25, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine= new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);

            //1
            Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 68, 81, 1892, 3436);
            Canvas canvasTemp = new Canvas(bitmapTemp);

            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_xl_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "三件套 第 1 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xl, height_xl, true);
            canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
            bitmapTemp.recycle();

            //2
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1606, 81, 1892, 3436);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_xl_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "三件套 第 2 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xl, height_xl, true);
            canvasCombine.drawBitmap(bitmapTemp, width_xl + 25, 0, null);
            bitmapTemp.recycle();

            //3
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3142, 81, 1892, 3436);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_xl_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "三件套 第 3 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_xl, height_xl, true);
            canvasCombine.drawBitmap(bitmapTemp, width_xl * 2 + 50, 0, null);
            bitmapTemp.recycle();
        } else if (orderItems.get(currentID).sizeStr.equals("4")) {
            bitmapCombine = Bitmap.createBitmap(width_l * 2 + height_m + 50, height_l + 25, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine= new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);

            //2
            Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1539, 28, 1983, 4293);
            Canvas canvasTemp = new Canvas(bitmapTemp);

            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_l_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "四件套 第 2 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_l, height_l, true);
            canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
            bitmapTemp.recycle();

            //3
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3075, 281, 1983, 4293);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_l_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "四件套 第 3 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_l, height_l, true);
            canvasCombine.drawBitmap(bitmapTemp, width_l + 25, 0, null);
            bitmapTemp.recycle();

            //1
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4, 533, 1983, 3536);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_m_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "四件套 第 1 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_m, height_m, true);
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            matrix.postTranslate(width_l * 2 + 50, width_m);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            bitmapTemp.recycle();

            //4
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4610, 533, 1983, 3536);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_m_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "四件套 第 4 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_m, height_m, true);
            matrix = new Matrix();
            matrix.postRotate(-90);
            matrix.postTranslate(width_l * 2 + 50, width_m * 2 + 25);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            bitmapTemp.recycle();
        } else {
            bitmapCombine = Bitmap.createBitmap(width_s + width_l + height_m + 50, height_s * 2 + 50, Bitmap.Config.ARGB_8888);
            Canvas canvasCombine= new Canvas(bitmapCombine);
            canvasCombine.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvasCombine.drawColor(0xffffffff);

            //1
            Bitmap bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 40, 888, 1983, 2525);
            Canvas canvasTemp = new Canvas(bitmapTemp);

            Bitmap bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_s_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "五件套 第 1 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_s, height_s, true);
            canvasCombine.drawBitmap(bitmapTemp, 0, 0, null);
            bitmapTemp.recycle();

            //5
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 6179, 888, 1983, 2525);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_s_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "五件套 第 5 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_s, height_s, true);
            canvasCombine.drawBitmap(bitmapTemp, 0, height_s + 25, null);
            bitmapTemp.recycle();

            //3
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 3109, 4, 1983, 4293);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_l_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "五件套 第 3 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_l, height_l, true);
            canvasCombine.drawBitmap(bitmapTemp, width_s + 25, 0, null);
            bitmapTemp.recycle();

            //2
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 1572, 383, 1983, 3536);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_m_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "五件套 第 2 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_m, height_m, true);
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            matrix.postTranslate(width_s + width_l + 50, width_m);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            bitmapTemp.recycle();

            //4
            bitmapTemp = Bitmap.createBitmap(MainActivity.instance.bitmapPillow, 4643, 383, 1983, 3536);
            canvasTemp = new Canvas(bitmapTemp);

            bitmapDB = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.fg_m_border);
            canvasTemp.drawBitmap(bitmapDB, 0, 0, null);
            bitmapDB.recycle();
            drawText(canvasTemp, 100, 40, "五件套 第 4 片");
            bitmapTemp = Bitmap.createScaledBitmap(bitmapTemp, width_m, height_m, true);
            matrix = new Matrix();
            matrix.postRotate(-90);
            matrix.postTranslate(width_s + width_l + 50, width_m * 2 + 25);
            canvasCombine.drawBitmap(bitmapTemp, matrix, null);
            bitmapTemp.recycle();
        }

        try {
            String nameCombine = "画框" + orderItems.get(currentID).sizeStr + "件套" + orderItems.get(currentID).order_number + strPlus + ".jpg";

            String pathSave;
            if(MainActivity.instance.cb_classify.isChecked()){
                pathSave = sdCardPath + "/生产图/" + childPath + "/" + orderItems.get(currentID).sku + "/";
            } else
                pathSave = sdCardPath + "/生产图/" + childPath + "/";
            if(!new File(pathSave).exists())
                new File(pathSave).mkdirs();
            File fileSave = new File(pathSave + nameCombine);
            BitmapToJpg.save(bitmapCombine, fileSave, 130);
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
            MainActivity.instance.bitmapPillow.recycle();

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

    void setScale() {
        width_s = 2048;
        height_s = 2559;
        width_m = 2048;
        height_m = 3583;
        width_l = 2048;
        height_l = 4351;
        width_xl = 2457;
        height_xl = 4351;
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
