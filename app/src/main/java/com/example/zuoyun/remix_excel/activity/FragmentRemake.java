package com.example.zuoyun.remix_excel.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;
import com.example.zuoyun.remix_excel.activity.start.bean.RemakeItem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by zuoyun on 2016/11/4.
 */

public class FragmentRemake extends BaseFragment {
//    String sdCardPath = "/mnt/asec/share";
String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems;
    ArrayList<RemakeItem> remakeItems;
    String childPath;

    Handler handler=new Handler();
    Runnable runnable;
    int remakeid,position=0;
    Bitmap bitmap;
    float scale;

    ProgressBar pb_remake;
    TextView tv_remake_tip;

    @Override
    public int getLayout() {
        return R.layout.fragmentremake;
    }

    @Override
    public void initData(View view) {
        pb_remake = (ProgressBar) view.findViewById(R.id.pb_remake);
        tv_remake_tip = (TextView) view.findViewById(R.id.tv_remake_tip);
        orderItems = MainActivity.instance.orderItems;
        remakeItems = MainActivity.instance.remakeItems;
        childPath = MainActivity.instance.childPath;

        pb_remake.setMax(remakeItems.size());
        runnable=new Runnable() {
            @Override
            public void run() {
                remake();
            }
        };
        handler.post(runnable);
    }

    public void remake(){
        String nameBegin=null;
        if(position==remakeItems.size()) {
            tv_remake_tip.setVisibility(View.VISIBLE);
        }else{
            remakeid = remakeItems.get(position).id - 1;
            String printColor = orderItems.get(remakeid).color.equals("黑") ? "B" : "W";
            String imagename = orderItems.get(remakeid).order_number + orderItems.get(remakeid).sku
                    + orderItems.get(remakeid).size+printColor + ".jpg";
            bitmap = BitmapFactory.decodeFile(sdCardPath + "/生产图/" + childPath + "/" + imagename);
            if (bitmap == null) {
                Toast.makeText(getContext(),"未找到当日生产图",Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {
                if(orderItems.get(remakeid).sku.equals("DD")){
                    scale = getDDScale(orderItems.get(remakeid).size + 1);
                    int x = 0, y=0, width = 1413, height = (int) (770 * scale);
                    switch (remakeItems.get(position).part) {
                        case "右外":
                            y = 0;
                            nameBegin = "RR";
                            break;
                        case "右内":
                            nameBegin = "RL";
                            y = (int) (770 * scale + 80);
                            break;
                        case "左内":
                            nameBegin = "LR";
                            y = (int) (1540 * scale + 80);
                            break;
                        case "左外":
                            y = (int) (2310 * scale + 160);
                            nameBegin = "LL";
                            break;
                    }
                    bitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
                }
                else if(orderItems.get(remakeid).sku.equals("DE")){
                    scale = getDEScale(orderItems.get(remakeid).size + 1);
                    int x = 0, y=0, width = 1342, height = (int) (597 * scale);
                    switch (remakeItems.get(position).part) {
                        case "右外":
                            y = 0;
                            nameBegin = "RR";
                            break;
                        case "右内":
                            y = (int) (597 * scale + 60);
                            nameBegin = "RL";
                            break;
                        case "左内":
                            y = (int) (1194 * scale + 120);
                            nameBegin = "LR";
                            break;
                        case "左外":
                            y = (int) (1791 * scale + 180);
                            nameBegin = "LL";
                            break;
                    }
                    bitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
                }
                else if(orderItems.get(remakeid).sku.equals("DF")){
                    scale = getlazyScale(orderItems.get(remakeid).size);
                    int x = 0, y=0, width = 836, height = (int) (678 * scale);
                    switch (remakeItems.get(position).part) {
                        case "左":
                            y = 0;
                            nameBegin = "L";
                            break;
                        case "右":
                            y = (int) (678 * scale);
                            nameBegin = "R";
                            break;
                    }
                    bitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
                }

                try{
                    File file = new File(sdCardPath + "/补图/" + childPath + "/");
                    if(!file.exists())
                        file.mkdirs();
                    String savename = sdCardPath + "/补图/" + childPath + "/" +nameBegin+ imagename;
                    FileOutputStream out = new FileOutputStream(savename);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();

                    //写入excel
                    String writePath = sdCardPath + "/补图/" + childPath + "/生产单.xls";
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
                    Label label0 = new Label(0, position+1, nameBegin+orderItems.get(remakeid).order_number+orderItems.get(remakeid).sku+orderItems.get(remakeid).size+printColor);
                    sheet.addCell(label0);
                    Label label1 = new Label(1, position+1, orderItems.get(remakeid).sku+orderItems.get(remakeid).size+printColor);
                    sheet.addCell(label1);
                    Number number = new Number(2, position+1, orderItems.get(remakeid).num);
                    sheet.addCell(number);
                    Label label3 = new Label(3, position+1, "小左");
                    sheet.addCell(label3);

                    workbook.write();
                    workbook.close();
                }catch (Exception e){}

                position++;
                pb_remake.setProgress(position);
                handler.post(runnable);
            }
        }
    }

    public float getDDScale(int size){
        float scale = 1.0f;
        switch (size) {
            case 37:
                scale = 1.0f;
                break;
            case 38:
                scale = 0.99f;
                break;
            case 39:
                scale = 0.985f;
                break;
            case 40:
                scale = 0.976f;
                break;
            case 41:
                scale = 0.954f;
                break;
            case 42:
                scale = 0.943f;
                break;
            case 43:
                scale = 0.943f;
                break;
            case 44:
                scale = 0.939f;
                break;
            case 45:
                scale = 0.936f;
                break;
            case 46:
                scale = 0.929f;
                break;
            case 47:
                scale = 0.923f;
                break;
        }
        return scale;
    }

    public float getDEScale(int size){
        float scale = 1.0f;
        switch (size) {
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

    public float getlazyScale(int size){
        float scale = 1.0f;
        switch (size) {
            case 36:
                scale = 0.932f;
                break;
            case 37:
                scale = 0.937f;
                break;
            case 38:
                scale = 0.942f;
                break;
            case 39:
                scale = 0.951f;
                break;
            case 40:
                scale = 0.96f;
                break;
            case 41:
                scale = 0.969f;
                break;
            case 42:
                scale = 0.977f;
                break;
            case 43:
                scale = 0.985f;
                break;
            case 44:
                scale = 0.993f;
                break;
            case 45:
                scale = 1.0f;
                break;
            case 46:
                scale = 1.0f;
                break;
        }
        return scale;
    }
}
