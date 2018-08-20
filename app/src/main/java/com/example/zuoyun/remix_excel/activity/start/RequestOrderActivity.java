package com.example.zuoyun.remix_excel.activity.start;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.start.adapter.Rv_TaskCardAdapter;
import com.example.zuoyun.remix_excel.activity.start.bean.TaskCard;
import com.example.zuoyun.remix_excel.bean.Config;
import com.example.zuoyun.remix_excel.bean.ResponseData;
import com.example.zuoyun.remix_excel.bean.ResponseOrder;
import com.example.zuoyun.remix_excel.bean.ShareData;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestOrderActivity extends AppCompatActivity {
    public static RequestOrderActivity instance;
    @BindView(R.id.switch_isAll)
    Switch switch_isAll;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_isAll)
    TextView tv_isAll;
    @BindView(R.id.rv_taskCard)
    RecyclerView rv_taskCard;
    @BindView(R.id.iv_refresh)
    ImageView iv_refresh;
    @BindView(R.id.swip_refresh)
    SwipeRefreshLayout swip_refresh;

    Context context;
    RequestQueue requestQueue = NoHttp.newRequestQueue();
    ArrayList<ResponseOrder> responseOrders;
    ArrayList<TaskCard> taskCards = new ArrayList<>();
    Rv_TaskCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_order);
        context = this;
        instance = this;
        ButterKnife.bind(this);

        initviews();
        initData();
    }

    void initviews(){
        swip_refresh.setColorSchemeResources(R.color.colorAppRed, R.color.colorMyGreen, R.color.colorMyBlue);
//        swip_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
        swip_refresh.setEnabled(false);

        switch_isAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tv_isAll.setText("所有任务");
                } else {
                    tv_isAll.setText("未完成任务");
                }
                refresh();
            }
        });
    }

    void initData(){
        tv_date.setText(ShareData.orderDate_Print);
        getData();
    }

    void setRv(){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
        rv_taskCard.setLayoutManager(layoutManager);
        adapter = new Rv_TaskCardAdapter(context, taskCards);
        rv_taskCard.setAdapter(adapter);

        for (TaskCard tc : taskCards) {
            Log.e("aaa", tc.sku + ":" + tc.responseOrders.size());
        }
    }

    @OnClick(R.id.iv_refresh)
    void clickRefresh(){
        refresh();
    }

    public void getData(){
        switch_isAll.setClickable(false);

        Request<String> request = NoHttp.createStringRequest(Config.getOrders, RequestMethod.GET);
        request.setConnectTimeout(10000);
        request.addHeader("Accept", "application/json");
        request.addHeader("Authorization", "Basic aGFsZXk6T3NjYXJzMjAxNw==");
        request.add("marker", ShareData.orderDate_Request);
        requestQueue.add(1, request, responseListener);
    }
    OnResponseListener<String> responseListener=new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
            swip_refresh.setRefreshing(true);
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            swip_refresh.setRefreshing(false);
            switch_isAll.setClickable(true);
            if (what == 1) {
                if (response.responseCode() == 200) {
                    responseOrders = (ArrayList<ResponseOrder>) JSON.parseArray(response.get(), ResponseOrder.class);
                    if (responseOrders != null) {
                        convertData();
                        setRv();
                    } else {
                        Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ResponseData responseData = JSON.parseObject(response.get(), ResponseData.class);
                    Toast.makeText(context, responseData.message, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            if (what == 1) {
                Toast.makeText(context, "请求失败:" + response.responseCode(), Toast.LENGTH_SHORT).show();
            }
            swip_refresh.setRefreshing(false);
            switch_isAll.setClickable(true);
        }

        @Override
        public void onFinish(int what) {

        }
    };
    void refresh(){
        taskCards.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        getData();
    }

    void convertData(){
        for (ResponseOrder ro : responseOrders) {
            if (switch_isAll.isChecked()) {
                boolean isNew = true;
                int cardPosition = 0;
                for(int i=0;i<taskCards.size();i++) {
                    if (taskCards.get(i).sku.equals(ro.sku)) {
                        isNew = false;
                        cardPosition = i;
                        break;
                    }
                }
                if (isNew) {
                    TaskCard taskCard = new TaskCard();
                    taskCard.sku = ro.sku;
                    taskCard.responseOrders = new ArrayList<>();
                    taskCard.responseOrders.add(ro);

                    taskCards.add(taskCard);
                } else {
                    taskCards.get(cardPosition).responseOrders.add(ro);
                }
            } else {
                if (ro.printed == null) {
                    boolean isNew = true;
                    int cardPosition = 0;
                    for(int i=0;i<taskCards.size();i++) {
                        if (taskCards.get(i).sku.equals(ro.sku)) {
                            isNew = false;
                            cardPosition = i;
                            break;
                        }
                    }
                    if (isNew) {
                        TaskCard taskCard = new TaskCard();
                        taskCard.sku = ro.sku;
                        taskCard.responseOrders = new ArrayList<>();
                        taskCard.responseOrders.add(ro);

                        taskCards.add(taskCard);
                    } else {
                        taskCards.get(cardPosition).responseOrders.add(ro);
                    }
                }
            }
        }
    }

    public void showDialogTip(String title, final String content){
        final AlertDialog dialog_tip;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogTransBackGround);
        dialog_tip = builder.create();
        dialog_tip.setCancelable(false);
        dialog_tip.show();
        View view_dialog = LayoutInflater.from(context).inflate(R.layout.item_dialog_finish, null);
        dialog_tip.setContentView(view_dialog);
        TextView tv_title = (TextView) view_dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_content = (TextView) view_dialog.findViewById(R.id.tv_dialog_content);
        Button bt_yes = (Button) view_dialog.findViewById(R.id.bt_dialog_yes);

        tv_title.setText(title);
        tv_content.setText(content);
        bt_yes.setText("确定");
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_tip.dismiss();
                refresh();
            }
        });
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
