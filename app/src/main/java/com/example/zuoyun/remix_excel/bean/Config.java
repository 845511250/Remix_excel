package com.example.zuoyun.remix_excel.bean;

/**
 * Created by zuoyun on 2016/11/3.
 */

public class Config {
//    public final static String BaseUrl = "http://192.168.10.191";
    public final static String BaseUrl = "http://factory.forudesigns.cn:8080";
//    public final static String BaseUrl = "http://192.168.10.242";

    public final static String ToCheck = "http://factory.forudesigns.cn:8080";

    public final static String checkUpdate = "http://845511250.top/zhengyuapi/checkupdate.php";
    public final static String downloadApk = "http://845511250.top/zhengyuapi/app-release.apk";
    public final static String getOrders = BaseUrl + "/api/items";
    public final static String getSkus = BaseUrl + "/api/sku";
    public final static String markFinished = BaseUrl + "/api/printed";
    public final static String downloadImg = BaseUrl + "/api/downloads";

    public static int chooseGA;//100,165
    public static int chooseGB;//100,165
}
