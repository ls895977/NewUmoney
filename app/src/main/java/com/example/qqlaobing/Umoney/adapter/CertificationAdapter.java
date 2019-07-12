package com.example.qqlaobing.Umoney.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.qqlaobing.Umoney.R;

import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class CertificationAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private int[] images;
    private String[] list;
    public CertificationAdapter(Context context, List<String> data,int[] images,String[] list){
        this.context=context;
        this.images=images;
        this.list=list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_certification,
                    null);

            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_cer);
            holder.tv_finish = (TextView) convertView.findViewById(R.id.tv_finish);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        if (i==images.length){
            holder.textView.setText(data.get(i));
        }else {
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.imageView.setImageResource(images[i]);
            holder.textView.setText(data.get(i));
            if (list[i].equals("已认证")) {
                holder.tv_finish.setTextColor(Color.parseColor("#33A0FF"));
            } else {
                holder.tv_finish.setTextColor(Color.parseColor("#9DA4B7"));
            }
            holder.tv_finish.setText(list[i]);
        }
        return convertView;
    }
    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView tv_finish;
    }
}
