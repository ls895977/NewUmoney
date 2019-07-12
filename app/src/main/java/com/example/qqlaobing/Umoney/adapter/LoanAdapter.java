package com.example.qqlaobing.Umoney.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.MyLoanBeen;

import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class LoanAdapter extends BaseAdapter {
    private Context context;
    private List<MyLoanBeen> data;
    public LoanAdapter(Context context, List<MyLoanBeen> data){
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
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_loan,
                    null);

            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            //holder.tvLook = (TextView) convertView.findViewById(R.id.tv_look);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_loanmoney = (TextView) convertView.findViewById(R.id.tv_loanmoney);
            holder.tv_servicemoney = (TextView) convertView.findViewById(R.id.tv_servicemoney);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }
//            holder.tvNumber.setText("单号："+data.get(i).getOrderno());
//        Log.e("listod",data.get(i).getOrderno());
            holder.tv_servicemoney.setText("￥"+data.get(i).getInterest());
            if (data.get(i).getStatus().equals("0")){
                holder.tv_type.setText("待审核");
            }else if (data.get(i).getStatus().equals("1")){
                holder.tv_type.setText("已审核");
            }else if (data.get(i).getStatus().equals("2")){
                holder.tv_type.setText("已放款");
            }else if (data.get(i).getStatus().equals("3")){
                holder.tv_type.setText("已逾期");
            }else if (data.get(i).getStatus().equals("4")){
                holder.tv_type.setText("已还款");
                holder.tv_name.setText("已还金额");
            }else if (data.get(i).getStatus().equals("5")){
                holder.tv_type.setText("已延期");
            }
//            holder.tvInterest.setText(data.get(i).getOverduefee()+"%");
            holder.tv_money.setText("￥"+data.get(i).getDamount());
            holder.tv_loanmoney.setText("￥"+data.get(i).getDamount());
            holder.tv_time.setText(data.get(i).getTime());
//            holder.tvTime.setText(data.get(i));
//            holder.tvRemainTime.setText(data.get(i).getRefundtime());

        return convertView;
    }
    class ViewHolder {
        public TextView tv_name;
        public TextView tv_money;
        public TextView tv_type;
        public TextView tv_loanmoney;
        public TextView tv_servicemoney;
        public TextView tv_time;


    }}
