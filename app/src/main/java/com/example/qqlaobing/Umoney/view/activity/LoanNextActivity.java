package com.example.qqlaobing.Umoney.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.ApplyConfirmPresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.OrderInfoPresenterImpl;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanNextActivity extends AppCompatActivity {

    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.tv_loanmoney)
    TextView tvLoanmoney;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_money_huan)
    TextView tvMoneyHuan;
    @BindView(R.id.btn_loan)
    Button btnLoan;
    private LoadingDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_get:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject jsonObject=response.getJSONObject("data");
                            JSONObject loan=jsonObject.getJSONObject("loan");
                            tvLoanmoney.setText(loan.getString("amount"));
                            tvMoney.setText(loan.getString("amount"));
                            tvMoneyHuan.setText(loan.getString("amount"));
                            tvTime.setText(loan.getString("deadline"));
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                        } else {
                            Toast.makeText(LoanNextActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_POST_APPLY:
                    dialog.close();
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("111","main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            Toast.makeText(LoanNextActivity.this,"贷款提交成功",Toast.LENGTH_LONG).show();
                            LoanNextActivity.this.finish();
                        } else {
                            Toast.makeText(LoanNextActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loan_next);
        ButterKnife.bind(this);
        OrderInfoPresenterImpl orderInfoPresenter=new OrderInfoPresenterImpl(mHandler,NetRequestBusinessDefine.oderno);
        orderInfoPresenter.postData();
    }

    @OnClick(R.id.btn_loan)
    public void onViewClicked() {
        ApplyConfirmPresenterImpl applyConfirmPresenter=new ApplyConfirmPresenterImpl(mHandler,NetRequestBusinessDefine.oderid);
        applyConfirmPresenter.postData();
        dialog = new LoadingDialog(LoanNextActivity.this, "上传中...");
        dialog.show();
    }
}
