package com.example.qqlaobing.Umoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.QuanBeen;


import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class QuanAdapter extends BaseAdapter {
    private Context context;
    private List<QuanBeen> data;
    public QuanAdapter(Context context, List<QuanBeen> data){
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_quan_item,
                    null);

            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_timeto = (TextView) convertView.findViewById(R.id.tv_timeto);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.tv_title.setText(data.get(i).getTitle());
            holder.tv_amount.setText(data.get(i).getAmount());
        if (data.get(i).getStatus()==0){
            holder.tv_status.setText("已使用");
        }else {
            holder.tv_status.setText("未使用");
        }
            holder.tv_timeto.setText(data.get(i).getTimeto());
        return convertView;
    }
    class ViewHolder {
        public TextView tv_title;
        public TextView tv_amount;
        public TextView tv_status;
        public TextView tv_timeto;
    }
}
