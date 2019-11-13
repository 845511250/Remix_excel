package com.example.zuoyun.remix_excel.activity.start;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.MainActivity;
import com.example.zuoyun.remix_excel.activity.start.adapter.ListviewOrdersAdapter;
import com.example.zuoyun.remix_excel.activity.start.bean.Order;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;
import com.example.zuoyun.remix_excel.bean.ShareData;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalOrderActivity extends AppCompatActivity {
    @BindView(R.id.lv_orders)
    ListView lv_orders;

    Context context;
    String sdCardPath = "/storage/emulated/0/Pictures";
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    ArrayList<Order> orders = new ArrayList<>();
    String childPath,orderDate_Print, orderDate_Excel, orderDate_Request;
    int totalNum;

    DataFormatter dataFormatter = new DataFormatter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_order);
        ButterKnife.bind(this);
        context = this;

        initviews();
    }

    void initviews(){
        File files = new File(sdCardPath + "/Excel订单/");
        if (files.list(filenameFilter).length > 0) {
            for (File file : files.listFiles(filenameFilter)) {
                Order order = new Order(file.getName(), file.getAbsolutePath());
                orders.add(order);
            }
            lv_orders.setAdapter(new ListviewOrdersAdapter(context, orders));
        }
        lv_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("aaa", orders.get(position).path);

                if(orders.get(position).path.endsWith("xls")){
                    childPath = orders.get(position).name.substring(0, orders.get(position).name.length() - 4);
                    readExcelOrderOld(orders.get(position).path);
                }else {
                    childPath = orders.get(position).name.substring(0, orders.get(position).name.length() - 5);
                    readExcelOrderNew(orders.get(position).path);
                }

                childPath = childPath.replace("...", "");
                totalNum = orderItems.size();
            }
        });
    }

    FilenameFilter filenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".xls") || name.endsWith(".xlsx");
        }
    };

    public void readExcelOrderOld(String path){
        orderItems.clear();
        try{
            Workbook workbook = WorkbookFactory.create(new File(path));
            Sheet sheet = workbook.getSheetAt(0);
            Row row;
            int rows = sheet.getLastRowNum() + 1;
            Log.e("aaa", "total rows: " + rows);

            for (int i = 0; i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null && getContent(row, 0) != "" && getContent(row, 13).contains(".")) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.order_number = getContent(row, 0);
                    orderItem.num = Integer.parseInt(getContent(row, 2));
                    orderItem.codeE = getContent(row, 4);

                    orderItem.colorStr = getContent(row, 15);
                    orderItem.color = orderItem.colorStr;
                    if (orderItem.color.equalsIgnoreCase("Black"))
                        orderItem.color = "黑";
                    else if (orderItem.color.equalsIgnoreCase("Trans"))
                        orderItem.color = "透";
                    else if (orderItem.color.equalsIgnoreCase("White"))
                        orderItem.color = "白";
                    else if (orderItem.color.equalsIgnoreCase("Brown"))
                        orderItem.color = "棕色";
                    else if (orderItem.color.equalsIgnoreCase("Beige"))
                        orderItem.color = "米色";
                    else if (orderItem.color.equalsIgnoreCase(""))
                        orderItem.color = "白";

                    String sizestr = getContent(row, 16);
                    orderItem.sizeStr = sizestr;
                    if (sizestr != "") {
                        if (sizestr.equalsIgnoreCase("S/M")) {
                            orderItem.size = 0;
                        } else if (sizestr.equalsIgnoreCase("L/XL")) {
                            orderItem.size = 1;
                        } else if (sizestr.endsWith(")")) {
                            try {
                                orderItem.size = Integer.parseInt(sizestr.substring(sizestr.length() - 3, sizestr.length() - 1));
                            } catch (Exception e) {
                                Log.e("aaa", "size parseInt Error!!!");
                            }
                        } else {
                            try {
                                orderItem.size = Integer.parseInt(sizestr);
                            } catch (Exception e) {
                                Log.e("aaa", "size parseInt Error!!!");
                            }
                        }

                    }

                    String print_index = getContent(row, 3);
                    orderItem.newCode = print_index;
                    orderItem.newCodeStr = print_index;
                    String size = orderItem.sizeStr.equals("S/M") ? "中码" : orderItem.sizeStr.equals("L/XL") ? "大码" : orderItem.size == 0 ? orderItem.sizeStr : orderItem.size + "";
                    if (print_index.startsWith("A")) {
                        orderItem.newCode = size + "-A-" + getNewCode(print_index);
                    } else if (print_index.startsWith("B")) {
//                        orderItem.newCode = size + "-B-" + print_index.substring(print_index.lastIndexOf("-") + 1);
                        orderItem.newCode = size + "-B-" + getNewCode(print_index);
                    } else {
                        orderItem.newCode = size + "-" + print_index;
                    }

                    orderItem.printCode = "";

                    String SKU = getContent(row, 1);
                    orderItem.skuStr = SKU;
                    orderItem.sku = SKU;
                    if (SKU.equals("CASCS30") || SKU.equals("CASCS37") || SKU.equals("CASCS65") || SKU.equals("CASCS30DHL") || SKU.equals("CASCS37DHL") || SKU.equals("CASCS65DHL") || SKU.equals("WOMENHIGHTOP") || SKU.equals("WOMENHIGHTOPDHL") || SKU.equals("MENHIGHTOP") || SKU.equals("MENHIGHTOPDHL") || SKU.equals("WOMENHIGHTOPFEDEX") || SKU.equals("MENHIGHTOPFEDEX"))
                        orderItem.sku = "DD";//高帮
                    else if (SKU.equals("CASCS38") || SKU.equals("CASCS66") || SKU.equals("CASCS31") || SKU.equals("CASCS38DHL") || SKU.equals("CASCS66DHL") || SKU.equals("CASCS31DHL") || SKU.equals("WOMENLOWTOP") || SKU.equals("WOMENLOWTOPDHL") || SKU.equals("MENLOWTOP") || SKU.equals("MENLOWTOPDHL") || SKU.equals("WOMENLOWTOPFEDEX") || SKU.equals("MENLOWTOPFEDEX"))
                        orderItem.sku = "DE";//低帮
                    else if (SKU.equals("CASCS47") || SKU.equals("CASCS95") || SKU.equals("CASCS45") || SKU.equals("CASCS47DHL") || SKU.equals("CASCS95DHL") || SKU.equals("CASCS45DHL"))
                        orderItem.sku = "DF";//一脚蹬
                    else if (SKU.equals("CHCPC52") || SKU.equals("CHCPC52DHL"))
                        orderItem.sku = "DG";//枕套
                    else if (SKU.equals("CASB059") || SKU.equals("CASB059DHL"))
                        orderItem.sku = "DH";//购物袋
                    else if (SKU.equals("CASB037") || SKU.equals("CASB037DHL"))
                        orderItem.sku = "DL";//PU包
                    else if (SKU.equals("CASB058") || SKU.equals("CASB058DHL"))
                        orderItem.sku = "DM";//PU包
                    else if (SKU.equals("CASB055") || SKU.equals("CASB055DHL"))
                        orderItem.sku = "DN";//PU包
                    else if (SKU.equals("CASB056") || SKU.equals("CASB056DHL"))
                        orderItem.sku = "DP";//PU包
                    else if (SKU.equals("CREWSOCKS") || SKU.equals("CREWSOCKSDHL"))
                        orderItem.sku = "DK";//长袜
                    else if (SKU.equals("MENRUNNINGSHOE") || SKU.equals("WOMENRUNNINGSHOE") || SKU.equals("MENRUNNINGSHOEDHL") || SKU.equals("WOMENRUNNINGSHOEDHL") || SKU.equals("MENRUNNINGSHOEFEDEX") || SKU.equals("WOMENRUNNINGSHOEFEDEX"))
                        orderItem.sku = "DQ";//跑鞋
                    else if (SKU.equals("KIDRUNNINGSHOE") || SKU.equals("KIDRUNNINGSHOEDHL") || SKU.equals("KIDRUNNINGSHOEFEDEX"))
                        orderItem.sku = "DV";//儿童跑鞋
                    else if (SKU.equals("MENSLIPON") || SKU.equals("WOMENSLIPON") || SKU.equals("MENSLIPONDHL") || SKU.equals("WOMENSLIPONDHL") || SKU.equals("MENSLIPONFEDEX") || SKU.equals("WOMENSLIPONFEDEX"))
                        orderItem.sku = "DT";//新一脚蹬成人
                    else if (SKU.equals("KIDSLIPON") || SKU.equals("KIDSLIPONDHL"))
                        orderItem.sku = "DU";//新一脚蹬儿童
                    else if (SKU.equals("TOMS") || SKU.equals("TOMSDHL"))
                        orderItem.sku = "DJ";//Toms鞋
                    else if (SKU.equals("MENFLIPFLOP") || SKU.equals("WOMENFLIPFLOP") || SKU.equals("MENFLIPFLOPDHL") || SKU.equals("WOMENFLIPFLOPDHL"))
                        orderItem.sku = "AB";//拖鞋
                    else if (SKU.equals("MENBOOTS") || SKU.equals("WOMENBOOTS") || SKU.equals("MENBOOTSDHL") || SKU.equals("WOMENBOOTSDHL") || SKU.equals("MENBOOTSFEDEX") || SKU.equals("WOMENBOOTSFEDEX"))
                        orderItem.sku = "DY";//靴子
                    else if (SKU.equals("MENLEATHERBOOTS") || SKU.equals("WOMENLEATHERBOOTS") || SKU.equals("MENLEATHERBOOTSDHL") || SKU.equals("WOMENLEATHERBOOTSDHL") || SKU.equals("MENLEATHERBOOTSFEDEX") || SKU.equals("WOMENLEATHERBOOTSFEDEX"))
                        orderItem.sku = "FC";//靴子
                    else if (SKU.equals("BACKPACK") || SKU.equals("BACKPACKDHL") || SKU.equals("BACKPACKFEDEX"))
                        orderItem.sku = "DX";//全印花背包
                    else if (SKU.equals("DUVET") || SKU.equals("DUVETDHL") || SKU.equals("DUVETFEDEX"))
                        orderItem.sku = "FA";//被罩
                    else if (SKU.equals("HEELS") || SKU.equals("HEELSDHL") || SKU.equals("HEELSFEDEX"))
                        orderItem.sku = "FB";//高跟鞋
                    else if (SKU.equals("WOMENFURBOOTS") || SKU.equals("WOMENFURBOOTSDHL") || SKU.equals("MENFURBOOTS") || SKU.equals("MENFURBOOTSDHL"))
                        orderItem.sku = "FF";//雪地靴
                    else if (SKU.equals("WOMENATHLETICSNEAKER") || SKU.equals("WOMENATHLETICSNEAKERDHL") || SKU.equals("MENATHLETICSNEAKER") || SKU.equals("MENATHLETICSNEAKERDHL"))
                        orderItem.sku = "FH";//新跑鞋


                    String[] images = getContent(row, 13).trim().split(" ");
                    if (images.length == 2) {
                        orderItem.img_right = getImageName(images[0]);
                        orderItem.img_left = getImageName(images[1]);
                    } else if (images.length == 1) {
                        orderItem.img_pillow = getImageName(images[0]);
                    }
                    orderItems.add(orderItem);
                }
            }
            //for循环结束
            showDialogDatePicker();
        }
        catch (Exception e){
            Toast.makeText(context, "读取订单失败！", Toast.LENGTH_SHORT).show();
            Log.e("aaa", e.getMessage());
        }
    }
    public void readExcelOrderNew(String path){
        orderItems.clear();
        try{
            Workbook workbook = WorkbookFactory.create(new File(path));
            Sheet sheet = workbook.getSheetAt(0);
            Row row;
            int rows = sheet.getLastRowNum() + 1;
            Log.e("aaa", "total rows: " + rows);

            for (int i = 0; i < rows; i++) {
                OrderItem orderItem = new OrderItem();
                row = sheet.getRow(i);
                if (row != null && getContent(row, 0) != "" && getContent(row, 5).contains(".")) {
                    orderItem.order_number = getContent(row, 0);
                    orderItem.num = Integer.parseInt(getContent(row, 2));
                    orderItem.codeE = getContent(row, 4);

                    orderItem.customer = "";
                    if(path.contains("pillowprofits"))
                        orderItem.customer = "adam";
                    else if(path.contains("4u2-正丁"))
                        orderItem.customer = "u2-正丁";
                    else if(path.contains("4u2-正域"))
                        orderItem.customer = "4u2-正域";
                    else if(path.contains("zhengding-vietnam"))
                        orderItem.customer = "zhengding-vietnam";

                    orderItem.colorStr = getContent(row, 7);
                    orderItem.color = orderItem.colorStr;
                    if (orderItem.color.equalsIgnoreCase("Black"))
                        orderItem.color = "黑";
                    else if (orderItem.color.equalsIgnoreCase("Trans"))
                        orderItem.color = "透";
                    else if (orderItem.color.equalsIgnoreCase("White"))
                        orderItem.color = "白";
                    else if (orderItem.color.equalsIgnoreCase("Brown"))
                        orderItem.color = "棕色";
                    else if (orderItem.color.equalsIgnoreCase("Beige"))
                        orderItem.color = "米色";
                    else if (orderItem.color.equalsIgnoreCase(""))
                        orderItem.color = "白";

                    String sizestr = getContent(row, 8);
                    orderItem.sizeStr = sizestr;
                    orderItem.sizeOriginal = getContent(row, 10);
                    if (!sizestr.equals("")) {
                        if (sizestr.equalsIgnoreCase("S/M")) {
                            orderItem.size = 0;
                        } else if (sizestr.equalsIgnoreCase("L/XL")) {
                            orderItem.size = 1;
                        } else if (sizestr.endsWith(")")) {
                            try {
                                orderItem.size = Integer.parseInt(sizestr.substring(sizestr.length() - 3, sizestr.length() - 1));
                            } catch (Exception e) {
                                Log.e("aaa", "size parseInt Error!!!");
                            }
                        } else {
                            try {
                                orderItem.size = Integer.parseInt(sizestr);
                            } catch (Exception e) {
                                Log.e("aaa", "size parseInt Error!!!");
                            }
                        }

                    }

                    String print_index = getContent(row, 3);
                    orderItem.newCode = print_index;
                    orderItem.newCodeStr = print_index;
                    String size = orderItem.sizeStr.equals("S/M") ? "中码" : orderItem.sizeStr.equals("L/XL") ? "大码" : orderItem.sizeStr;
                    if (print_index.startsWith("A")) {
                        orderItem.newCode = size + "-A-" + getNewCode(print_index);
                    } else if (print_index.startsWith("B")) {
//                        orderItem.newCode = size + "-B-" + print_index.substring(print_index.lastIndexOf("-") + 1);
                        orderItem.newCode = size + "-B-" + getNewCode(print_index);
                    } else {
                        orderItem.newCode = size + "-" + print_index;
                    }

                    String SKU = getContent(row, 1);
                    orderItem.skuStr = SKU;
                    orderItem.sku = SKU;
                    if (SKU.equals("MENFLIPFLOP") || SKU.equals("WOMENFLIPFLOP") || SKU.equals("MENFLIPFLOPDHL") || SKU.equals("WOMENFLIPFLOPDHL"))
                        orderItem.sku = "AB";//拖鞋

                    String[] images = getContent(row, 5).trim().split(" ");
                    if (images.length == 2) {
                        orderItem.img_right = getImageName(images[0]);
                        orderItem.img_left = getImageName(images[1]);
                    } else if (images.length == 1) {
                        orderItem.img_pillow = getImageName(images[0]);
                    }
                    orderItems.add(orderItem);
                }
            }
            //for循环结束
            showDialogDatePicker();
        }
        catch (Exception e){
            Toast.makeText(context, "读取订单失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private String getContent(Row row, int column) {
        return dataFormatter.formatCellValue(row.getCell(column));
    }

    String getImageName(String str){
        return str.substring(str.lastIndexOf("/")+1, str.length());
    }

    private static String getNewCode(String str){
        String str1 = str.substring(0, str.lastIndexOf("-"));
        return str.substring(str1.lastIndexOf("-") + 1, str.length());
    }

    public void showDialogDatePicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK) {
                    Log.e("ds", "back");
                    System.exit(0);
                }
                return false;
            }
        });
        final AlertDialog dialog_date = builder.create();
        dialog_date.setCancelable(true);
        dialog_date.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_datepicker, null);
        dialog_date.setContentView(view_dialog);
        final DatePicker datePicker = (DatePicker) view_dialog.findViewById(R.id.date_picker);
        Button bt_select_date = (Button) view_dialog.findViewById(R.id.bt_select_date);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int int_Year = calendar.get(Calendar.YEAR);
        int int_Month = calendar.get(Calendar.MONTH);
        int int_Day = calendar.get(Calendar.DAY_OF_MONTH);
        orderDate_Print = (int_Month + 1) + "月" + int_Day + "日";
        orderDate_Excel = int_Year + "-" + (int_Month + 1) + "-" + int_Day;

        datePicker.init(int_Year, int_Month, int_Day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                orderDate_Print = (monthOfYear + 1) + "月" + dayOfMonth + "日";
                orderDate_Excel = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                orderDate_Request = "" + year + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", dayOfMonth);
                Log.e("orderDate_Excel", orderDate_Excel);
            }
        });
        bt_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, orderDate_Excel, Toast.LENGTH_SHORT).show();
                dialog_date.dismiss();

                ShareData.orderItems.clear();
                ShareData.orderItems = orderItems;
                ShareData.totalNum = totalNum;
                ShareData.childPath = childPath;
                ShareData.orderDate_Excel = orderDate_Excel;
                ShareData.orderDate_Print = orderDate_Print;
                ShareData.orderDate_Request = orderDate_Request;

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
