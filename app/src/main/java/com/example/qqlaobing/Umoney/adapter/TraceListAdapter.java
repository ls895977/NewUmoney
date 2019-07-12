package com.example.qqlaobing.Umoney.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.Trace;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglizhi on 2018/8/1.
 */

public class TraceListAdapter extends BaseAdapter {
    private Context context;
    private List<Trace> traceList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public TraceListAdapter(Context context, List<Trace> traceList) {
        this.context = context;
        this.traceList = traceList;
    }

    @Override
    public int getCount() {
        return traceList.size();
    }

    @Override
    public Trace getItem(int position) {
        return traceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Trace trace = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trace, parent, false);
            holder.tvAcceptTime = (TextView) convertView.findViewById(R.id.tvAcceptTime);
            holder.tvAcceptStation = (TextView) convertView.findViewById(R.id.tvAcceptStation);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
//            holder.tvTopLine = (TextView) convertView.findViewById(R.id.tvTopLine);
            holder.tvDot = (TextView) convertView.findViewById(R.id.tvDot);
            convertView.setTag(holder);
        }

/*        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
//            holder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            holder.tvAcceptTime.setTextColor(0xff555555);
            holder.tvAcceptStation.setTextColor(0xff555555);
            holder.tvDot.setBackgroundResource(R.mipmap.now);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
//            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tvAcceptTime.setTextColor(0xff999999);
            holder.tvAcceptStation.setTextColor(0xff999999);
            holder.tvDot.setBackgroundResource(R.mipmap.now1);
        }*/

        holder.tvAcceptTime.setText(trace.getAcceptTime());
        if (trace.getDesc()!=null){
            holder.tv_desc.setText(trace.getDesc());
        }
        if (trace.getAcceptStation().equals("审核未通过")){
            holder.tvDot.setBackgroundResource(R.mipmap.fail);
        }else if (trace.getAcceptStation().equals("已审核通过")){
            holder.tvDot.setBackgroundResource(R.mipmap.success);
        }else {
            holder.tvDot.setBackgroundResource(R.mipmap.submit);
        }
        holder.tvAcceptStation.setText(trace.getAcceptStation());
        return convertView;
    }

//    @Override
//    public int getItemViewType(int id) {
//        if (id == 0) {
//            return TYPE_TOP;
//        }
//        return TYPE_NORMAL;
//    }

    static class ViewHolder {
        public TextView tvAcceptTime, tvAcceptStation;
        public TextView tvTopLine, tvDot,tv_desc;
    }
}