package com.example.qqlaobing.Umoney.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qqlaobing.Umoney.R;


/**
 * Created by zhanglizhi on 2018/7/5.
 */

public class BankCardDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private EditText et_number;
    private Button bt_cancel,bt_sure;

    public BankCardDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    public BankCardDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BankCardDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bankcar);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            default:break;
        }
    }


    private onFinishClickListener listener;
    public interface onFinishClickListener{
        public void onFinishClick(String category);
    }
    public void onFinishClick(onFinishClickListener onFinishClickListener){
        this.listener=onFinishClickListener;
    }
}
