package com.example.zuoyun.remix_excel.activity.start.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.MainActivity;
import com.example.zuoyun.remix_excel.activity.start.RequestOrderActivity;
import com.example.zuoyun.remix_excel.activity.start.bean.TaskCard;
import com.example.zuoyun.remix_excel.bean.ShareData;

import java.util.ArrayList;

/**
 * Created by  on 2016/9/2.
 */
public class Rv_TaskCardAdapter extends RecyclerView.Adapter<Rv_TaskCardAdapter.AnchorHotViewHolder> {
    private ArrayList<TaskCard> data;
    private Context context;
    LayoutInflater layoutInflater;
    private int selectPosition;

    public Rv_TaskCardAdapter(Context mContext, ArrayList<TaskCard> mData) {
        data = mData;
        context = mContext;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public AnchorHotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_task_card, parent, false);
//        view.getLayoutParams().height = (PixelUtil.getHeight(context) - PixelUtil.getStatusHeight((Activity) context) - PixelUtil.dp2px(context, 50)) / 2;
        return new AnchorHotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnchorHotViewHolder holder, final int position) {
        final TaskCard taskCard = data.get(position);

        holder.tv_sku.setText(taskCard.sku);
        holder.tv_sku.setTextColor(ContextCompat.getColor(context, taskCard.selected ? R.color.myred : R.color.color_black_background));
        holder.iv_open_close.setImageResource(taskCard.selected ? R.drawable.icon_up_close : R.drawable.icon_down_open);
        holder.bt_all.setText("全部 " + taskCard.responseOrders.size());
        holder.rv_taskList.setVisibility(taskCard.selected ? View.VISIBLE : View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rv_taskList.setLayoutManager(layoutManager);
        holder.rv_taskList.setAdapter(new Rv_TaskListAdapter(context, taskCard.responseOrders));

        holder.ll_sku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskCard.selected = taskCard.selected ? false : true;
                notifyDataSetChanged();
            }
        });

        holder.bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        TextView tv_sku;
        Button bt_all;
        RecyclerView rv_taskList;

        LinearLayout ll_sku;
        ImageView iv_open_close;

        public AnchorHotViewHolder(final View itemView) {
            super(itemView);
            tv_sku = (TextView) itemView.findViewById(R.id.tv_sku);
            bt_all = (Button) itemView.findViewById(R.id.bt_all);
            rv_taskList = (RecyclerView) itemView.findViewById(R.id.rv_taskList);

            ll_sku = (LinearLayout) itemView.findViewById(R.id.ll_sku);
            iv_open_close = (ImageView) itemView.findViewById(R.id.iv_open_close);
        }
    }

    private void showDialogTip(String title,String content){
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
}
