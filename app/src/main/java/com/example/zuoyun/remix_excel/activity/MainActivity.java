package com.example.zuoyun.remix_excel.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.start.bean.Order;
import com.example.zuoyun.remix_excel.activity.start.bean.OrderItem;
import com.example.zuoyun.remix_excel.activity.start.bean.RemakeItem;
import com.example.zuoyun.remix_excel.bean.Config;
import com.example.zuoyun.remix_excel.bean.ResponseData;
import com.example.zuoyun.remix_excel.bean.ShareData;
import com.example.zuoyun.remix_excel.tools.CircularProgress;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainActivity extends FragmentActivity {
    public static MainActivity instance;
    Context context;

    @BindView(R.id.et_filter)
    EditText et_filter;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tb_auto)
    ToggleButton tb_auto;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.cb_classify)
    CheckBox cb_classify;
    @BindView(R.id.cb_fastmode)
    CheckBox cb_fastmode;
    @BindView(R.id.tv_finishRemixx)
    TextView tv_finishRemixx;

    int currentID = 0,totalWrong=0,totalNum;
    String currentSKU="";
    boolean leftdone=false, rightdone = false,leftsucceed=false, rightsucceed = false;

    String picturesPath = "/storage/emulated/0/Pictures/pictures";
//    String picturesPath = "/storage/emulated/0/Download";
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    ArrayList<RemakeItem> remakeItems=new ArrayList<>();
    public Bitmap bitmapLeft, bitmapRight, bitmapPillow;

    FragmentManager fragmentManager;
    MessageListener messageListener;
    public CircularProgress progress;
    ArrayList<Order> orders = new ArrayList<>();
    String childPath,orderDate_Print, orderDate_Excel, orderDate_Request;
    AlertDialog dialog,dialog_pwd;
    RequestQueue requestQueue = NoHttp.newRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        instance = this;
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        progress = new CircularProgress(context);
        //initDialog();
        initviews();

        //doRequestCheck();
    }

    void initviews(){
        orderItems = ShareData.orderItems;
        totalNum = ShareData.totalNum;
        childPath = ShareData.childPath;
        orderDate_Excel = ShareData.orderDate_Excel;
        orderDate_Print = ShareData.orderDate_Print;
        orderDate_Request = ShareData.orderDate_Request;

        setfg();
    }

    @OnClick(R.id.tb_auto)
    void setauto(){
        if(tb_auto.isChecked()){
            if (et_filter.getText().toString().trim().equals("")) {
                messageListener.listen(10, "");
            } else {
                if(et_filter.getText().toString().trim().equals(orderItems.get(currentID).sku))
                    messageListener.listen(10, "");
                else
                    setnext();
            }
        }
    }

    @OnClick(R.id.bt_next)
    void setnext(){
        if (messageListener != null) {
            messageListener.listen(0, "");
        }

        currentID++;
        if (currentID < orderItems.size()) {
            leftsucceed = false;
            rightsucceed = false;

            if (et_filter.getText().toString().trim().equals("")) {
                setfg();
            }else {
                if(et_filter.getText().toString().trim().equals(orderItems.get(currentID).sku))
                    setfg();
                else
                    setnext();
            }
        } else {
            showDialogFinish();
        }
    }
    @OnClick(R.id.bt_previous)
    void setprevious(){
        messageListener.listen(0, "");

        if(currentID>0) {
            currentID--;
            messageListener.listen(0,"");
            setfg();
        }
    }

    @OnClick(R.id.bt_search)
    void setsearch(){
        String searchStr = et_search.getText().toString().trim();
        et_search.setText("");
        int position=-1;
        if(!searchStr.equals("")){
            for (int i=0;i<orderItems.size();i++) {
                if (orderItems.get(i).order_number.length() >= searchStr.length()) {
                    if (orderItems.get(i).order_number.substring(0, searchStr.length()).equalsIgnoreCase(searchStr)) {
                        position = i;
                        break;
                    }
                }
            }
            if(position!=-1){
                currentID = position - 1;
                setnext();
            } else
                Toast.makeText(context,"没有此订单！",Toast.LENGTH_SHORT).show();
        }
    }

    void setfg(){
        boolean firstOK = true;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (orderItems.get(currentID).sku) {
            case "DD":
                tv_title.setText("高帮鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDD());
                break;
            case "DE":
                tv_title.setText("低帮鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDE());
                break;
            case "DF":
                tv_title.setText("旧一脚蹬 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDF());
                break;
            case "DG":
                tv_title.setText("枕套 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDGH());
                break;
            case "DH":
                tv_title.setText("购物袋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDGH());
                break;
            case "DN":
                tv_title.setText("DN " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDNP());
                break;
            case "DP":
                tv_title.setText("DP " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDNP());
                break;
            case "DL":
                tv_title.setText("DL " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDL());
                break;
            case "DM":
                tv_title.setText("DM " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDM());
                break;
            case "DK":
                tv_title.setText("袜子 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentSock());
                break;
            case "DQ":
                tv_title.setText("跑鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDQ());
                break;
            case "DT":
                tv_title.setText("新一脚蹬成人 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDT());
                break;
            case "DJ":
                tv_title.setText("Toms " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDJ());
                break;
            case "AB":
                tv_title.setText("拖鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentAB());
                break;
            case "DV":
                tv_title.setText("儿童跑鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDV());
                break;
            case "DX":
                tv_title.setText("全印花背包 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDX());
                break;
            case "DY":
                tv_title.setText("皮靴 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDY());
                break;
            case "DU":
                tv_title.setText("新一脚蹬儿童 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDU());
                break;
            case "FA":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FAS":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FAM":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FAL":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA1":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA2":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA3":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA4":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA5":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA6":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA7":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA8":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA9":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA10":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA11":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FA12":
                tv_title.setText("被罩 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFA());
                break;
            case "FB":
                tv_title.setText("高跟鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFB());
                break;
            case "FC":
                tv_title.setText("DY(皮革) " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFC());
                break;
            case "FF":
                tv_title.setText("雪地靴 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFF());
                break;
            case "FH":
                tv_title.setText("新跑鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFH());
                break;
            case "FG":
                tv_title.setText("画框 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFG());
                break;
            case "FV":
                tv_title.setText("车座套 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFV());
                break;
            case "FW":
                tv_title.setText("换鞋垫高帮 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDD());
                break;
            case "FX":
                tv_title.setText("换鞋垫低帮 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDE());
                break;
            case "DZ":
                tv_title.setText("行李套 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentDZ());
                break;
            case "FI":
                tv_title.setText("圆领卫衣 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFI());
                break;
            case "FY":
                tv_title.setText("毛毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFY());
                break;
            case "FYS":
                tv_title.setText("毛毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFY());
                break;
            case "FYL":
                tv_title.setText("毛毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentFY());
                break;
            case "GA":
                tv_title.setText("浴袍女 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGA());
                break;
            case "GB":
                tv_title.setText("浴袍男 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGB());
                break;
            case "GC":
                tv_title.setText("男背心 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGC());
                break;
            case "GD":
                tv_title.setText("女背心 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGD());
                break;
            case "GI":
                tv_title.setText("挂毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGI());
                break;
            case "GL":
                tv_title.setText("浴帘 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGL());
                break;
            case "GO":
                tv_title.setText("车前脚垫 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGO());
                break;
            case "GP":
                tv_title.setText("车前后脚垫 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGP());
                break;
            case "GQM":
                tv_title.setText("Adam卫衣男 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGQM());
                break;
            case "GQW":
                tv_title.setText("Adam卫衣女 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGQW());
                break;
            case "GQY":
                tv_title.setText("Adam卫衣童 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGQY());
                break;
            case "GRM":
                tv_title.setText("Adam拉链卫衣男 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGRM());
                break;
            case "GRW":
                tv_title.setText("Adam拉链卫衣女 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGRW());
                break;
            case "GRY":
                tv_title.setText("Adam拉链卫衣童 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGRY());
                break;
            case "GS":
                tv_title.setText("卫衣裙 " + orderItems.get(currentID).order_number);
//                if (orderItems.get(currentID).sizeStr.equals("2XL")) {
//                    transaction.replace(R.id.frame_main, new FragmentGS100());
//                } else {
//                    transaction.replace(R.id.frame_main, new FragmentGS165());
//                }
                transaction.replace(R.id.frame_main, new FragmentGS165());
                break;
            case "GT":
                tv_title.setText("丝巾 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGT());
                break;
            case "GUM":
                tv_title.setText("Adam男夹克 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGUM());
                break;
            case "GUW":
                tv_title.setText("Adam女夹克 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGUW());
                break;
            case "GWW":
                tv_title.setText("帽巾 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGW());
                break;
            case "GX":
                tv_title.setText("瑜伽裤 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentGX());
                break;
            case "HB":
                tv_title.setText("短瑜伽裤 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHB());
                break;
            case "HC":
                tv_title.setText("遮阳板 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHC());
                break;
            case "HD":
                tv_title.setText("棉拖鞋 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHD());
                break;
            case "HF":
                tv_title.setText("落肩衣 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHF100());
                break;
            case "HG":
                tv_title.setText("沙滩裤 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHG());
                break;
            case "HGM":
                tv_title.setText("沙滩裤 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHG());
                break;
            case "HGW":
                tv_title.setText("沙滩裤 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHGW());
                break;
            case "HH":
                tv_title.setText("裙子 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHH());
                break;
            case "HI":
                tv_title.setText("加绒马丁靴 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHI());
                break;
            case "HJY":
                tv_title.setText("毛毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHJ());
                break;
            case "HJS":
                tv_title.setText("毛毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHJ());
                break;
            case "HJM":
                tv_title.setText("毛毯 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHJ());
                break;
            case "HK":
                tv_title.setText("新款 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentHK());
                break;
            case "R":
                tv_title.setText("围裙 " + orderItems.get(currentID).order_number);
                transaction.replace(R.id.frame_main, new FragmentR());
                break;

            default:
                firstOK = false;
                showDialogTip("错误！", "订单号 " + orderItems.get(currentID).order_number + " 商品暂未添加，跳过此单号并继续？");
                break;
        }

        if (firstOK) {
            transaction.commitAllowingStateLoss();
            tv_progress.setText((currentID + 1) + " / " + totalNum);

            getsetBitmap();
        }
    }

    public void getsetBitmap(){
        currentSKU = orderItems.get(currentID).sku;
        tv_finishRemixx.setText("加载中...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("aaa", "开始 "+orderItems.get(currentID).order_number);
                        if(orderItems.get(currentID).img_left!=null){
                            String img_left = orderItems.get(currentID).img_left;
                            bitmapLeft = BitmapFactory.decodeFile(picturesPath + "/" + img_left);
                            String img_right = orderItems.get(currentID).img_right;
                            bitmapRight = BitmapFactory.decodeFile(picturesPath + "/" + img_right);

                            if (bitmapLeft == null || bitmapRight == null) {
                                showDialogNoImage();
                            }else{
                                tv_finishRemixx.setText("加载完成");

                                leftsucceed = true;
                                messageListener.listen(1,orderItems.get(currentID).img_design_left);
                                rightsucceed = true;
                                messageListener.listen(2,orderItems.get(currentID).img_design_right);
                            }
                        } else if (orderItems.get(currentID).img_pillow!=null) {
                            final String img_pillow = orderItems.get(currentID).img_pillow;
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    if(bitmapPillow!=null){
                                        bitmapPillow.recycle();
                                    }
                                    bitmapPillow = BitmapFactory.decodeFile(picturesPath + "/" + img_pillow);
                                    if (bitmapPillow == null) {
                                        Log.e("aaa", img_pillow);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showDialogNoImage();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_finishRemixx.setText("加载完成");
                                                messageListener.listen(4,orderItems.get(currentID).img_pillow);
                                            }
                                        });
                                    }
                                }
                            }.start();
                        } else {
                            tv_tip.setText("当前图片不存在 请操作下一个");
                            messageListener.listen(3, "");
                        }
                    }
                });
            }
        }).start();
    }

    public static String getLastNewCode(String str){
        return str.substring(str.lastIndexOf("-")+1, str.length());
    }
    public static String getLast2NewCode(String str){
        return str.substring(str.indexOf("-"), str.length());
    }
    public static String getRedNewCode(String str){
        return str.substring(str.indexOf("-") + 1, str.length());
    }

    public void initDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
        dialog = builder.create();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog, null);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setContentView(view_dialog);
        dialog.hide();
    }

    String[] gqList = new String[]{"1539795", "1596072", "1602647", "1597401", "1526215", "154352", "1596173",
            "1605154", "1573050", "1609073", "1551699", "1490506", "1609073", "1600986", "1585723", "1553543",
            "1577504"
    };
    boolean findWrongGQ(){
        for (String str : gqList) {
            if (orderItems.get(currentID).img_right.contains(str)) {
                return true;
            }
        }
        return false;
    }
    public void writeWrong(){
        //写入excel
        try{
            File file = new File("/storage/emulated/0/Pictures/生产图/" + childPath + "/");
            if(!file.exists())
                file.mkdirs();
            totalWrong++;
            String writePath = "/storage/emulated/0/Pictures/生产图/" + childPath + "/问题卫衣过滤列表.xls";
            File fileWrite = new File(writePath);
            if(!fileWrite.exists()){
                WritableWorkbook book = Workbook.createWorkbook(fileWrite);
                WritableSheet sheet = book.createSheet("sheet1", 0);
                Label label0 = new Label(0, 0, "订单号");
                sheet.addCell(label0);
                book.write();
                book.close();
            }

            Workbook book = Workbook.getWorkbook(fileWrite);
            WritableWorkbook workbook = Workbook.createWorkbook(fileWrite,book);
            WritableSheet sheet = workbook.getSheet(0);
            Label label0 = new Label(0, totalWrong, orderItems.get(currentID).order_number);
            sheet.addCell(label0);
            workbook.write();
            workbook.close();

            if (tb_auto.isChecked())
                setnext();
        }catch (Exception e){

        }
    }

    public interface MessageListener{
        void listen(int message,String sampleurl);
    }
    public void setMessageListener(MessageListener listener){
        messageListener = listener;
    }

    public void showDialogFinish(){
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

        tv_title.setText("做图完毕");
        tv_content.setText("已完成第 "+orderItems.size()+" 个订单，请检查！");
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_finish.dismiss();
                finish();
            }
        });
    }

    public void showDialogRequestError(int code){
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

        tv_title.setText("网络错误 code " + code);
        tv_content.setText("后台网络请求出错，无法连接，请检查！");
        bt_yes.setText("退出");
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_finish.dismiss();
                finish();
            }
        });
    }

    public void showDialogNoImage(){
        doRequestDownloadImg();

        final AlertDialog dialog_noimage;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
        dialog_noimage = builder.create();
        dialog_noimage.setCancelable(true);
        dialog_noimage.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_noimage, null);
        dialog_noimage.setContentView(view_dialog);
        TextView tv_title = (TextView) view_dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_content = (TextView) view_dialog.findViewById(R.id.tv_dialog_content);
        Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);
        Button bt_skip = (Button) view_dialog.findViewById(R.id.bt_dialog_skip);

        tv_title.setText("缺少原图！");
        tv_title.setTextColor(0xffdd0000);
        tv_content.setText("订单号 "+orderItems.get(currentID).order_number+" 缺少原图，请下载后继续！");
        tv_content.setTextColor(0xffdd0000);
        bt_yes.setText("已经下载，继续-->");

        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_noimage.dismiss();
                setnext();
            }
        });
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_noimage.dismiss();
                getsetBitmap();
            }
        });
    }
    public void showDialogTip(String title,String content){
        final AlertDialog dialog_tip;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
        dialog_tip = builder.create();
        dialog_tip.setCancelable(true);
        dialog_tip.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_finish, null);
        dialog_tip.setContentView(view_dialog);
        TextView tv_title = (TextView) view_dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_content = (TextView) view_dialog.findViewById(R.id.tv_dialog_content);
        Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);

        tv_title.setText(title);
        tv_title.setTextColor(0xffdd0000);
        tv_content.setText(content);
        tv_content.setTextColor(0xffdd0000);
        bt_yes.setText("跳过-->");
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_tip.dismiss();
                setnext();
            }
        });
    }

    FilenameFilter filenameFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".xls");
        }
    };

    // getPathFromUri
    public String getPathFromUri(Uri uri) {
        if (uri.getScheme().equalsIgnoreCase("content")) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        } else {
            return uri.getPath();
        }

    }

    public static void doRecycle(Bitmap bitmap){
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    public void doRequestFinish(){
        if (orderItems.get(currentID).order_id != null) {
            Request<String> request = NoHttp.createStringRequest(Config.markFinished, RequestMethod.GET);
            request.setConnectTimeout(5000);
            request.addHeader("Accept", "application/json");
            request.addHeader("Authorization", "Basic aGFsZXk6T3NjYXJzMjAxNw==");
            request.add("order_number", orderItems.get(currentID).order_number);
            request.add("order_id", orderItems.get(currentID).order_id);
            requestQueue.add(1, request, responseListener);
        }
    }
    public void doRequestDownloadImg(){
        Request<String> request = NoHttp.createStringRequest(Config.downloadImg, RequestMethod.POST);
        request.setConnectTimeout(5000);
        request.addHeader("Accept", "application/json");
        request.addHeader("Authorization", "Basic aGFsZXk6T3NjYXJzMjAxNw==");
        request.add("urls", JSON.toJSONString(orderItems.get(currentID).print_url));
        requestQueue.add(2, request, responseListener);
    }

    OnResponseListener<String> responseListener=new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == 1) {
                if (response.responseCode() == 200) {
                    Log.e("aaa", "doRequestFinish 200");
                } else {
                    Log.e("aaa", "doRequestFinish " + response.responseCode());
                }
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            if (what == 1) {
                ResponseData responseData = JSON.parseObject(response.get(), ResponseData.class);
                if (responseData != null) {
                    Toast.makeText(context, responseData.message, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFinish(int what) {

        }
    };

    public static String getRandomNum(){
        int mRandom = 0;
        if (new Random().nextInt(10) < 3) {
            switch (new Random().nextInt(3)+1) {
                case 1:
                    mRandom = new Random().nextInt(9) + 1;
                    break;
                case 2:
                    mRandom = new Random().nextInt(90) + 10;
                    break;
                case 3:
                    mRandom = new Random().nextInt(900) + 100;
                    break;
            }
        } else {
            switch (new Random().nextInt(3)+4) {
                case 4:
                    mRandom = new Random().nextInt(9000) + 1000;
                    break;
                case 5:
                    mRandom = new Random().nextInt(90000) + 10000;
                    break;
                case 6:
                    mRandom = new Random().nextInt(900000) + 100000;
                    break;
            }
        }
        return mRandom + "";
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
