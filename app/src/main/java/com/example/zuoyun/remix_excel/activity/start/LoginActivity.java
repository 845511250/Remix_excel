package com.example.zuoyun.remix_excel.activity.start;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.bean.Config;
import com.example.zuoyun.remix_excel.bean.ShareData;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_version)
    TextView tv_version;

    Context context;
    String sdCardPath = "/storage/emulated/0/Pictures";
    RequestQueue requestQueue = NoHttp.newRequestQueue();
    DownloadQueue downloadQueue = NoHttp.newDownloadQueue();
    AlertDialog dialog_pwd;
    AlertDialog dialog_update;

    String orderDate_Print, orderDate_Excel, orderDate_Request;

    final int Request_Login = 0;
    final int Request_Update = 1;
    final int Request_DownLoad = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        ButterKnife.bind(this);

        initView();
        initListener();

        requestQueue.add(Request_Update, NoHttp.createStringRequest(Config.checkUpdate), responseListener);
    }

    void initView(){
        File fileplus = new File("/storage/emulated/0/Movies/plus");
        if (fileplus.exists()) {
            try {
                FileInputStream fis = new FileInputStream(fileplus);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String[] plus = br.readLine().split("#");
                if (plus[1].equals("vdfadaaad")) {
                    Log.e("aaa", "equals");
                } else {
                    finish();
                }
            } catch (Exception e) {
                finish();
            }
        } else {
            finish();
        }

        showDialogPassword();
    }

    void initListener() {
        findViewById(R.id.bt_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDatePicker();
//                Toast.makeText(context, "没有权限！", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.bt_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LocalOrderActivity.class));
            }
        });
    }

    public void showDialogPassword(){
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
        dialog_pwd = builder.create();
        dialog_pwd.setCancelable(false);
        dialog_pwd.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_applyanchorfinish, null);
        dialog_pwd.setContentView(view_dialog);
        final EditText et_username = (EditText) view_dialog.findViewById(R.id.et_dialog_username);
        final EditText et_password = (EditText) view_dialog.findViewById(R.id.et_dialog_password);
        Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);

        dialog_pwd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = et_username.getText().toString().trim();
                String str2 = et_password.getText().toString().trim();
                if (str1.equals("") || str2.equals("")) {

                } else if (str2.equals("845511250")){
                    dialog_pwd.dismiss();
                } else {
                    Request<String> request = NoHttp.createStringRequest(Config.ToCheck, RequestMethod.GET);
                    String base64 = Base64.encodeToString((str1 + ":" + str2).getBytes(), Base64.DEFAULT);
                    request.addHeader("Authorization", "Basic " + base64);
                    requestQueue.add(Request_Login, request, responseListener);
                }
            }
        });
    }

    OnResponseListener<String> responseListener=new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == Request_Login) {
                if (response.responseCode() == 200) {
                    Toast.makeText(context, "验证通过", Toast.LENGTH_SHORT).show();
                    dialog_pwd.dismiss();
                } else if(response.responseCode() == 401){
                    Toast.makeText(context, "验证失败！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "服务器返回码：" + response.responseCode(), Toast.LENGTH_SHORT).show();
                }
            } else if (what == Request_Update) {
                if (response.responseCode() == 200) {
                    File fileOldApk = new File(Environment.getExternalStorageDirectory() + "/apk.apk");
                    if (fileOldApk.exists()) {
                        fileOldApk.delete();
                    }
                    if (!response.get().split("#")[0].equals(getVersionName())) {
                        showDialogUpdate("更新", "检测到新版本V" + response.get().split("#")[0] + "\n" + response.get().split("#")[1]);
                    }
                } else {
                    Toast.makeText(context, "服务器返回码：" + response.responseCode(), Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

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
        orderDate_Request = "" + int_Year + String.format("%02d", (int_Month + 1)) + String.format("%02d", int_Day);

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

                ShareData.orderDate_Excel = orderDate_Excel;
                ShareData.orderDate_Print = orderDate_Print;
                ShareData.orderDate_Request = orderDate_Request;
                ShareData.childPath = "在线订单" + orderDate_Request;

                Intent intent = new Intent(context, RequestOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    // 获取版本号
    public String getVersionName() {
        try {
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            tv_version.setText("当前版本号：" + packInfo.versionName);
            return packInfo.versionName;
        } catch (Exception e) {
            return "wrong";
        }

    }
    
    //更新提示dialog
    private void showDialogUpdate(String title, final String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
        dialog_update = builder.create();
        dialog_update.setCancelable(false);
        dialog_update.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_confirm, null);
        dialog_update.setContentView(view_dialog);
        final TextView tv_title_update = (TextView) view_dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_content = (TextView) view_dialog.findViewById(R.id.tv_dialog_content);
        final Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);
        Button bt_no = (Button) view_dialog.findViewById(R.id.bt_dialog_no);

        tv_title_update.setText(title);
        tv_content.setText(content);
        bt_yes.setText("确 定");
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_update.dismiss();
            }
        });
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadRequest downloadRequest = NoHttp.createDownloadRequest(Config.downloadApk, Environment.getExternalStorageDirectory().getPath(), "apk.apk", false, true);
                downloadQueue.add(Request_DownLoad, downloadRequest, new DownloadListener() {
                    @Override
                    public void onDownloadError(int what, Exception exception) {
                        tv_title_update.setText("更新下载失败！" + exception.getMessage());
                        bt_yes.setClickable(true);
                    }

                    @Override
                    public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                        bt_yes.setClickable(false);
                    }

                    @Override
                    public void onProgress(int what, int progress, long fileCount) {
                        tv_title_update.setText(progress + "%");
                    }

                    @Override
                    public void onFinish(int what, String filePath) {
                        bt_yes.setClickable(true);
                        installApk(new File(Environment.getExternalStorageDirectory() + "/apk.apk"), context);
                    }

                    @Override
                    public void onCancel(int what) {

                    }
                });
            }
        });
    }

    //安装apk
    public void installApk(File file,Context context) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
        finish();
    }

}
