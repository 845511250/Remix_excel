package com.example.zuoyun.remix_excel.activity.start.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zuoyun.remix_excel.R;
import com.example.zuoyun.remix_excel.activity.start.bean.Order;

import java.util.ArrayList;

/**
 * Created by zuoyun on 2016/12/15.
 */

public class ListviewOrdersAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<Order> orders;

    public ListviewOrdersAdapter(Context mcontext,ArrayList<Order> morders){
        context = mcontext;
        orders = morders;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            Log.e("aaa", "convertView 为空");
            convertView = inflater.inflate(R.layout.item_listview_oeders, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tv_name.setText(orders.get(position).name);
        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        public ViewHolder(View view){
            tv_name = (TextView) view.findViewById(R.id.item_tv_ordername);
        }
    }
}
