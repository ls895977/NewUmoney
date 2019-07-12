package com.example.qqlaobing.Umoney.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.MyLoanBeen;

import java.util.List;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class MyLoanAdapter extends BaseAdapter {
    private Context context;
    private List<MyLoanBeen> data;
    public MyLoanAdapter(Context context, List<MyLoanBeen> data){
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_myloan,
                    null);

            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            //holder.tvLook = (TextView) convertView.findViewById(R.id.tv_look);
            holder.tvMark = (TextView) convertView.findViewById(R.id.tv_mark);
            holder.tvInterest = (TextView) convertView.findViewById(R.id.tv_interest);
            holder.tvLoanMoney = (TextView) convertView.findViewById(R.id.tv_loanmoney);
            holder.tvServiceMoney = (TextView) convertView.findViewById(R.id.tv_servicemoney);
            holder.tvYear = (TextView) convertView.findViewById(R.id.tv_year);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvRemainTime = (TextView) convertView.findViewById(R.id.tv_remaintime);
            holder.tv_wenhao = (TextView) convertView.findViewById(R.id.tv_wenhao);
            //绑定ViewHolder对象
            convertView.setTag(holder);
        } else {
            //取出ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }
            holder.tvNumber.setText("单号："+data.get(i).getOrderno());
        Log.e("listod",data.get(i).getOrderno());
            holder.tvServiceMoney.setText(data.get(i).getInterest());
            if (data.get(i).getStatus().equals("0")){
                holder.tvMark.setText("待审核");
            }else if (data.get(i).getStatus().equals("1")){
                holder.tvMark.setText("已审核");
            }else if (data.get(i).getStatus().equals("2")){
                holder.tvMark.setText("已放款");
            }else if (data.get(i).getStatus().equals("3")){
                holder.tvMark.setText("已逾期");
            }else if (data.get(i).getStatus().equals("4")){
                holder.tvMark.setText("已还款");
            }else if (data.get(i).getStatus().equals("5")){
                holder.tvMark.setText("已延期");
            }
            holder.tvInterest.setText(data.get(i).getOverduefee()+"%");
            holder.tvLoanMoney.setText(data.get(i).getDamount());
            holder.tvYear.setText(data.get(i).getDeadline());
//            holder.tvTime.setText(data.get(i));
            holder.tvRemainTime.setText(data.get(i).getRefundtime());
        holder.tv_wenhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(view,i);
            }
        });

        return convertView;
    }
    class ViewHolder {
        public TextView tvNumber;
        public TextView tvLook;
        public TextView tvMark;
        public TextView tvLoanMoney;
        public TextView tvServiceMoney;
        public TextView tvYear;
        public TextView tvTime;
        public TextView tvRemainTime;
        public TextView tvInterest;
        public TextView tv_wenhao;

    }
    private void showPopupWindow(View view,int i) {

        // 一个自定义的布局，作为显示的内容
        View pwView = LayoutInflater.from(context).inflate(
                R.layout.pop_window, null);
        // 设置按钮的点击事件
        TextView tv_fuwu=(TextView)pwView.findViewById(R.id.tv_fuwu);
        TextView tv_guanli=(TextView)pwView.findViewById(R.id.tv_guanli);
        TextView tv_zhengxin=(TextView)pwView.findViewById(R.id.tv_zhengxin);
        TextView tv_shenhe=(TextView)pwView.findViewById(R.id.tv_shenhe);
        TextView tv_shenhe_desc=(TextView)pwView.findViewById(R.id.tv_shenhe_desc);
        TextView tv_fuwu_desc=(TextView)pwView.findViewById(R.id.tv_fuwu_desc);
        TextView tv_guanli_desc=(TextView)pwView.findViewById(R.id.tv_guanli_desc);
        TextView tv_zhengxin_desc=(TextView)pwView.findViewById(R.id.tv_zhengxin_desc);

        tv_fuwu.setText(data.get(i).getFeiYongList().get(0).getVal()+"元");
        tv_fuwu_desc.setText(data.get(i).getFeiYongList().get(0).getDesc());
        tv_guanli.setText(data.get(i).getFeiYongList().get(1).getVal()+"元");
        tv_guanli_desc.setText(data.get(i).getFeiYongList().get(1).getDesc());
        tv_shenhe.setText(data.get(i).getFeiYongList().get(2).getVal()+"元");
        tv_shenhe_desc.setText(data.get(i).getFeiYongList().get(2).getDesc());
        tv_zhengxin.setText(data.get(i).getFeiYongList().get(3).getVal()+"元");
        tv_zhengxin_desc.setText(data.get(i).getFeiYongList().get(3).getDesc());

        final PopupWindow popupWindow = new PopupWindow(pwView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

//        popupWindow.setTouchInterceptor(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                Log.i("mengdd", "onTouch : ");
//
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
//
//        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        // 我觉得这里是API的一个bug
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.selectmenu_bg_downward));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }
}
