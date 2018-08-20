package com.example.zuoyun.remix_excel.bean;

import android.util.Log;

import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;

import java.util.ArrayList;

/**
 * Created by zuoyun on 2016/11/3.
 */

public class ShareData {
    public static ArrayList<OrderItem> orderItems = new ArrayList<>();
    public static String childPath;
    public static String orderDate_Print;
    public static String orderDate_Excel;
    public static String orderDate_Request;
    public static int totalNum;


    public static void convertRequest2Local(ArrayList<ResponseOrder> responseOrders) {
        totalNum = responseOrders.size();

        orderItems.clear();
        for (ResponseOrder ro : responseOrders) {
            OrderItem orderItem = new OrderItem();
            orderItem.order_id = ro.order_id;
            orderItem.order_number = ro.order_number;
            orderItem.num = ro.quantity;
            orderItem.codeE = ro.platform;
            String size = ro.size.equals("S/M") ? "中码" : ro.size.equals("L/XL") ? "大码" : ro.size;
            if (ro.print_index.startsWith("A")) {
                orderItem.newCode = size + "-A-" + getNewCode(ro.print_index);
            } else {
                orderItem.newCode = size + "-B-" + ro.print_index.substring(ro.print_index.lastIndexOf("-") + 1);
            }

            orderItem.colorStr = ro.color;
            orderItem.color = ro.color;
            if(ro.color.equals("Black"))
                orderItem.color = "黑";
            else if (ro.color.equals("Trans"))
                orderItem.color = "透";
            else if(ro.color.equals("White"))
                orderItem.color = "白";
            else if(ro.color.equals("Brown"))
                orderItem.color = "棕色";
            else if(ro.color.equals("Beige"))
                orderItem.color = "米色";

            orderItem.sizeStr = ro.size;
            if (ro.size != "") {
                if (ro.size.equals("S/M")) {
                    orderItem.size = 0;
                } else if (ro.size.equals("L/XL")) {
                    orderItem.size = 1;
                } else {
                    try {
                        orderItem.size = Integer.parseInt(ro.size);
                    } catch (Exception e) {
                        Log.e("aaa", "size parseInt Error!!!");
                    }
                }
            }

            orderItem.printCode = "";
            orderItem.skuStr = ro.original_sku;
            orderItem.sku = ro.sku;

            orderItem.print_url = ro.print_url;
            if (ro.print_url.size() == 2) {
                orderItem.img_right = getImageName(ro.print_url.get(0));
                orderItem.img_left = getImageName(ro.print_url.get(1));
            } else if (ro.print_url.size() == 1) {
                orderItem.img_pillow = getImageName(ro.print_url.get(0));
            }

            orderItems.add(orderItem);
        }
    }

    private static String getImageName(String str){
        return str.substring(str.lastIndexOf("/")+1, str.length());
    }

    private static String getNewCode(String str){
        String str1 = str.substring(0, str.lastIndexOf("-"));
        return str.substring(str1.lastIndexOf("-") + 1, str.length());
    }

}
