package com.example.zuoyun.remix_excel.activity.start.bean;

import java.util.ArrayList;

/**
 * Created by zuoyun on 2016/11/3.
 */

public class OrderItem {
    public String order_number,color,sku,img_left,img_right,img_design_left,img_design_right, img_pillow, img_3;
    public int num, size;
    public String colorStr;
    public String sizeStr, skuStr,sizeOriginal;
    public String printCode;
    public String newCode, newCodeStr, newCode_short;
    public String platform;

    public String order_id;
    public ArrayList<String> print_url;
    public ArrayList<String> imgs = new ArrayList<>();
    public String customer;

}
