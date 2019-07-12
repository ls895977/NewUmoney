package com.example.qqlaobing.Umoney.shoushijiesuo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */

public class AddGestureLockActivity extends AppCompatActivity {

    private GestureLockLayout gll_lock_view;
    private SPUtils spUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gesture_lock);
        gll_lock_view= (GestureLockLayout) findViewById(R.id.gll_lock_view);

        spUtils=new SPUtils(this, SharedPreferencesFinal.DEMO);

        gll_lock_view.setMinCount(4);//最少连接四个点
        gll_lock_view.setDotCount(3);//设置手势解锁view 每行每列点的个数
        switch (SharedPreferencesFinal.FROM){
            case 1:
                gll_lock_view.setMode(GestureLockLayout.RESET_MODE);//设置手势解锁view 模式为重置密码模式
                gll_lock_view.setOnLockResetListener(new GestureLockLayout.OnLockResetListener() {
                    @Override
                    public void onConnectCountUnmatched(int connectCount, int minCount) {

                        //连接数小于最小连接数时调用
                        showToast("最少连接" + minCount + "个点,请重新输入");
                    }

                    @Override
                    public void onFirstPasswordFinished(List<Integer> answerList) {
                        //第一次绘制手势成功时调用
                        gll_lock_view.resetGesture();
                        showToast("请再次绘制解锁图案");
                    }

                    @Override
                    public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {
                        //第二次密码绘制成功时调用
                        if (isMatched) {
                            Log.e("count22",answerList.toString()+"");
                            //两次答案一致，保存
                            //do something
                            showToast("手势密码绘制成功");
                            spUtils.putBoolean(SharedPreferencesFinal.GP_STATUS, true);
                            String pwd="";
                            for (int i=0;i<answerList.size();i++){
                                pwd+=answerList.get(i);
                            }
                            spUtils.putString(SharedPreferencesFinal.GP_PWD,pwd);
                            Log.e("count222",pwd);
                            AddGestureLockActivity.this.finish();

                        } else {
                            showToast("两次图案不同，请重新绘制");
                        }
                    }
                });
                break;
            case 0:
                gll_lock_view.setMode(GestureLockLayout.VERIFY_MODE);
                spUtils=new SPUtils(this, SharedPreferencesFinal.DEMO);
                gll_lock_view.setAnswer(spUtils.getString(SharedPreferencesFinal.GP_PWD,""));
                gll_lock_view.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
                    @Override
                    public void onGestureSelected(int id) {
                        Log.e("count",id+"");
                        //每选中一个点时调用
                    }
                    @Override
                    public void onGestureFinished(boolean isMatched) {
                        //绘制手势解锁完成时调用
                        if (isMatched) {
                            //密码匹配
                            showToast("验证成功");
                            spUtils.putBoolean(SharedPreferencesFinal.GP_STATUS,false);
                            AddGestureLockActivity.this.finish();
                        } else {
                            //不匹配
                            if (gll_lock_view.getTryTimes() < 4 && gll_lock_view.getTryTimes() > 0) {
                                showToast("还有" + gll_lock_view.getTryTimes() + "次机会");
                            } else if (gll_lock_view.getTryTimes() == 0) {
                                showToast("跳转到登录界面");
                            } else {
                                showToast("手势错误，请重新绘制");
                            }
                        }
                    }

                    @Override
                    public void onGestureTryTimesBoundary() {
                        //超出最大尝试次数时调用
                        gll_lock_view.setTouchable(false);
                    }
                });
                break;
            default:break;
        }


    }

    private void showToast(String msg){
        Toast.makeText(AddGestureLockActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 验证的代码如下：（快下班了，不写了。。）
     */


}
