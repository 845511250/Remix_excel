package com.example.zuoyun.remix_excel.activity.start.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.MainActivity;
import com.example.zuoyun.remix_excel.activity.start.RequestOrderActivity;
import com.example.zuoyun.remix_excel.activity.start.bean.TaskList;
import com.example.zuoyun.remix_excel.bean.ResponseOrder;
import com.example.zuoyun.remix_excel.bean.ShareData;

import java.util.ArrayList;

/**
 * Created by  on 2016/9/2.
 */
public class Rv_TaskListAdapter extends RecyclerView.Adapter<Rv_TaskListAdapter.AnchorHotViewHolder> {
    private ArrayList<ResponseOrder> responseOrders;
    private Context context;
    LayoutInflater layoutInflater;

    ArrayList<TaskList> data = new ArrayList<>();
    private int selectPosition;

    public Rv_TaskListAdapter(Context mContext, ArrayList<ResponseOrder> mData) {
        responseOrders = mData;
        context = mContext;
        layoutInflater = LayoutInflater.from(context);

//        int size = (int) Math.ceil(responseOrders.size() / 200d);
//        for (int i = 0; i < size; i++) {
//            if (i == size - 1) {
//                TaskList taskList = new TaskList();
//                int start = i * 200;
//                taskList.name = (start + 1) + " - " + (responseOrders.size());
//                taskList.responseOrders = new ArrayList<>();
//                taskList.responseOrders.addAll(responseOrders.subList(start, responseOrders.size()));
//                data.add(taskList);
//            } else {
//                TaskList taskList = new TaskList();
//                int start = i * 200;
//                taskList.name = (start + 1) + " - " + (start + 200);
//                taskList.responseOrders = new ArrayList<>();
//                taskList.responseOrders.addAll(responseOrders.subList(start, start + 200));
//                data.add(taskList);
//            }
//        }

        String color = "NotInit";
        String size = "NotInit";
        String sign = "NotInit";
        int taskPosition;
        for (ResponseOrder ro : responseOrders) {
            boolean isNew = true;
            taskPosition = data.size() - 1;

            if (ro.print_index.startsWith("A")) {
                if (taskPosition != -1) {
                    if (ro.color.equals(color) && ro.size.equals(size) && data.get(taskPosition).group.equals(getGroup(ro.print_index))) {
                        isNew = false;
                    }
                }

                if (isNew) {
                    TaskList taskList = new TaskList();
                    taskList.sku = ro.sku;
                    taskList.color = ro.color;
                    taskList.size = ro.size;
                    taskList.group = getGroup(ro.print_index);
                    taskList.responseOrders = new ArrayList<>();
                    taskList.responseOrders.add(ro);

                    color = ro.color;
                    size = ro.size;

//                if (!ro.print_index.substring(0, 1).equals(sign)) {
//                    sign = ro.print_index.substring(0, 1);
//                    taskList.sign = sign;
//                }

                    data.add(taskList);
                } else {
                    data.get(taskPosition).responseOrders.add(ro);
                }
            } else {
                if (taskPosition != -1) {
                    if (sign.equals("B")) {
                        isNew = false;
                    }
                }

                if (isNew) {
                    TaskList taskList = new TaskList();
                    taskList.sku = ro.sku;
                    taskList.color = "捆绑发货";
                    taskList.size = "";
                    taskList.group = "1";
                    taskList.responseOrders = new ArrayList<>();
                    taskList.responseOrders.add(ro);

                    color = ro.color;
                    size = ro.size;
                    sign = "B";

                    data.add(taskList);
                } else {
                    data.get(taskPosition).responseOrders.add(ro);
                }
            }

        }
    }


    @Override
    public AnchorHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_task_list, parent, false);
        return new AnchorHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnchorHotViewHolder holder, final int position) {
        TaskList taskList = data.get(position);

//        holder.tv_sign.setVisibility(taskList.sign == null ? View.GONE : View.VISIBLE);
//        if (taskList.sign != null) {
//            holder.tv_sign.setText(taskList.sign.equals("A") ? "单件发货" : "混合发货");
//        }
        if (!taskList.color.equals("捆绑发货")) {
            holder.tv_task.setText(getColorCH(taskList.color) + taskList.size + "第" + taskList.group + "组  剩余" + taskList.responseOrders.size());
        } else {
            holder.tv_task.setText(getColorCH(taskList.color) + "  剩余" + taskList.responseOrders.size());
        }

        holder.tv_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = position;
                showDialogTip("提示", "确定开始选中的任务吗");
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class AnchorHotViewHolder extends RecyclerView.ViewHolder {
        TextView tv_task;
        TextView tv_sign;

        public AnchorHotViewHolder(final View itemView) {
            super(itemView);
            tv_task = (TextView) itemView.findViewById(R.id.tv_task);
            tv_sign = (TextView) itemView.findViewById(R.id.tv_sign);
        }
    }

    private void showDialogTip(String title, final String content){
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
        tv_content.setText(content);
        bt_yes.setText("确定");
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_tip.dismiss();
                ShareData.convertRequest2Local(data.get(selectPosition).responseOrders);
                context.startActivity(new Intent(context, MainActivity.class));
                RequestOrderActivity.instance.showDialogTip("提示", "请点击确定刷新");
            }
        });
    }

    String getGroup(String str){
        String str1 = str.substring(0, str.lastIndexOf("-"));
        return str1.substring(str1.lastIndexOf("-") + 1, str1.length());
    }

    String getRanking(String str){
        return str.substring(str.lastIndexOf("-") + 1, str.length());
    }

    String getColorCH(String str){
        if(str.equals("Black"))
            return "黑";
        if (str.equals("Trans"))
            return "透";
        if(str.equals("White"))
            return "白";
        if(str.equals("Brown"))
            return "棕";
        if(str.equals("Beige"))
            return "米";

        return str;
    }

}
