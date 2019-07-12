package com.example.qqlaobing.Umoney.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.qqlaobing.Umoney.BuildConfig;
import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.UserInfoBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.HomePresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.LoginPresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.RegisterPresenterImpl;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;
import com.example.qqlaobing.Umoney.view.myview.TimeButton;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zlz
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.rl_dongtai)
    RelativeLayout rlDongtai;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.rl_mima)
    RelativeLayout rlMima;
    @BindView(R.id.ll_change)
    LinearLayout llChange;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    @BindView(R.id.tv_change_line)
    TextView tvChangeLine;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.rl_duanxin)
    RelativeLayout rlDuanxin;
    @BindView(R.id.rl_password)
    RelativeLayout rlPassword;
    private Button btnLogin, btn_login_code;
    private EditText etPassword, etUsername, et_validatecode;
    private TimeButton btnGetCode;
    private TextView tvRegiste, tv_error;
    private ImageView iv_logo;
    private boolean iscode = true;
    private UserInfoBeen userInfoBeen;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_LOGIN:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        Log.e("eeeeeeeee", resultCode);
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject member = results.getJSONObject("member");
                            Log.e("eeeeeeeee", response.getJSONObject("data").toString());
                            NetRequestBusinessDefine.K_TOKEN = member.getString("token");
                            Log.e("eeeeeeeee", member.getString("token"));
                            NetRequestBusinessDefine.login = true;
                            SharedPreferences sharedPreferences = getSharedPreferences("umoney", Context.MODE_PRIVATE); //私有数据
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            editor.putString("username", etUsername.getText().toString());
                            editor.putString("token", NetRequestBusinessDefine.K_TOKEN);
                            editor.putString("xieyi", "1");
                            editor.commit();//提交修改
                            HomePresenterImpl homePresenter = new HomePresenterImpl(mHandler);
                            homePresenter.getData();

                        } else {
                            dialog.close();
                            Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_HOME:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        Log.e("111", "main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                           /* startActivity(new Intent(LoginActivity.this,CertificationCenterActivity.class));
                            LoginActivity.this.finish();*/
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            if (!data.getString("loan").equals("null")) {
                                dialog.close();
                                NetRequestBusinessDefine.loan = true;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            } else {
                                Log.e("111", "main2");
                                dialog.close();
                                NetRequestBusinessDefine.loan = false;
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Log.e("111", "main");
                                NetRequestBusinessDefine.login = true;
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        } else {
                            dialog.close();
                            Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_UPDATA:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        Log.e("eeeeeeeee", resultCode);
                        if (resultCode.equals("1")) {
                            int versionCode = BuildConfig.VERSION_CODE;
                            Log.e("versionCode", versionCode + "");
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            if (versionCode < data.getInt("code")) {
                                dialog.close();
                                Log.e("url", data.getString("app_link"));
                                upData(data.getString("app_link"));
                            } else {
                                if (iscode) {
                                    LoginPresenterImpl loginPresenter = new LoginPresenterImpl(mHandler);
                                    loginPresenter.codeLogain(etUsername.getText().toString(), et_validatecode.getText().toString(), address);
                                } else {
                                    LoginPresenterImpl loginPresenter = new LoginPresenterImpl(mHandler, etUsername.getText().toString(), etPassword.getText().toString(), address,getPackageName());
                                    loginPresenter.postData();
                                }

                            }

                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GETS:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("net", msg.obj.toString());
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            if (!response.isNull("data")) {
                                Toast.makeText(LoginActivity.this, "验证码发送成功", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_LOGIN_CODE:
                    dialog.close();
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("net", msg.obj.toString());
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject member = results.getJSONObject("member");
                            Log.e("eeeeeeeee", response.getJSONObject("data").toString());
                            NetRequestBusinessDefine.K_TOKEN = member.getString("token");
                            Log.e("eeeeeeeee", member.getString("token"));
                            NetRequestBusinessDefine.login = true;
                            SharedPreferences sharedPreferences = getSharedPreferences("umoney", Context.MODE_PRIVATE); //私有数据
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            editor.putString("username", etUsername.getText().toString());
                            editor.putString("token", NetRequestBusinessDefine.K_TOKEN);

                            editor.commit();//提交修改
                            HomePresenterImpl homePresenter = new HomePresenterImpl(mHandler);
                            homePresenter.getData();
                        } else {
                            Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private LoadingDialog dialog;
    private String address = "";
    private int tpye = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#33A0FF"));
        test();
        initview();
        initData();
    }

    private void initData() {
        btnLogin.setOnClickListener(this);
        tvRegiste.setOnClickListener(this);
//        btn_login_code.setOnClickListener(this);
        tv_error.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
        rlDongtai.setOnClickListener(this);
        rlMima.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvPassword.setOnClickListener(this);
        btnLogin.getBackground().setAlpha(200);

        SharedPreferences share = getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (share != null) {
            etUsername.setText(share.getString("username", ""));
        }
    }


    private void initview() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        etPassword = (EditText) findViewById(R.id.et_password);
        etUsername = (EditText) findViewById(R.id.et_username);
        et_validatecode = (EditText) findViewById(R.id.et_validatecode);
        tvRegiste = (TextView) findViewById(R.id.tv_registe);
        tv_error = (TextView) findViewById(R.id.tv_error);
        btnGetCode = (TimeButton) findViewById(R.id.btn_getcode);
//        getLocation();
    }

    private void getLocation() {
        Log.e("ssss", "sssssssss");

        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.e("ssss", "ssssssssqqs");
                if (aMapLocation.getErrorCode() == 0) {
                    address = aMapLocation.getAddress();
                    Log.e("address", address);
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//可在其中解析amapLocation获取相应内容。
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
//                    getLocation();
                }
            }
        };
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
//该方法默认为false。
        mLocationOption.setOnceLocation(true);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (etUsername.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
//                else if (address.equals("")) {
////                    getLocation();
//                }
                else {
                    dialog = new LoadingDialog(this, "玩命加载中...");
                    dialog.show();
                    LoginPresenterImpl presenter = new LoginPresenterImpl(mHandler);
                    presenter.getData();
                }
                break;
            case R.id.btn_getcode:
                if (isMobile(etUsername.getText().toString())) {
                    //验证码接口
                    btnGetCode.setLenght(60000);
                    btnGetCode.starTime();
                    RegisterPresenterImpl presenter = new RegisterPresenterImpl(mHandler, etUsername.getText().toString(), "2");
                    presenter.getData();
                } else {
                    Toast.makeText(LoginActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_change_password:
                iscode = true;
                rlDuanxin.setVisibility(View.GONE);
                rlPassword.setVisibility(View.VISIBLE);
                tvChangePassword.setTextColor(Color.parseColor("#33A0FF"));
                tvChangeLine.setBackgroundColor(Color.parseColor("#67e6d8"));
                tvLine.setBackgroundColor(Color.parseColor("#ffffff"));
                etPassword.setVisibility(View.GONE);
                llChange.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_password:
                iscode = false;
                rlDuanxin.setVisibility(View.VISIBLE);
                rlPassword.setVisibility(View.GONE);
                tvChangeLine.setBackgroundColor(Color.parseColor("#ffffff"));
                tvPassword.setTextColor(Color.parseColor("#33A0FF"));
                tvLine.setBackgroundColor(Color.parseColor("#67e6d8"));
                etPassword.setVisibility(View.VISIBLE);
                llChange.setVisibility(View.GONE);
                break;
//            case R.id.btn_login_code:
//                if (etUsername.getText().toString().equals("")) {
//                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
//                } else if (et_validatecode.getText().toString().equals("")) {
//                    Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
//                } else if (address.equals("")) {
//                    getLocation();
//                } else {
//                    dialog = new LoadingDialog(this, "玩命加载中...");
//                    dialog.show();
//                    iscode = true;
//                    LoginPresenterImpl presenter = new LoginPresenterImpl(mHandler);
//                    presenter.getData();
//                }
//                break;
            case R.id.tv_registe:
                Log.e("ee", "ee");
                startActivity(new Intent(LoginActivity.this, RegisteActivity.class));
                break;
            case R.id.tv_error:
                Log.e("ee", "ee");
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
                break;
            default:
                break;
        }
    }

    public void upData(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示");
        builder.setMessage("退出");
        builder.setNegativeButton("退出软件", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginActivity.this.finish();
            }
        });
        builder.setPositiveButton("开始更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        Log.e("destroy", "destroy");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void test() {
        // 要申请的权限 数组 可以同时申请多个权限
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (Build.VERSION.SDK_INT >= 23) {
            //如果超过6.0才需要动态权限，否则不需要动态权限
            //如果同时申请多个权限，可以for循环遍历
            for (int i = 0; i < permissions.length; i++) {
                int check = ContextCompat.checkSelfPermission(LoginActivity.this, permissions[i]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (check != PackageManager.PERMISSION_GRANTED) {
                    //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 321);
                }
            }

        } else {
            //写入你需要权限才能使用的方法
//            run();
        }
    }

    //    // 用户权限 申请 的回调方法
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {

            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//                getLocation();
                Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();

            } else {
                for (int i = 0; i < permissions.length; i++) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
//                        showDialogTipUserGoToAppSettting();
                        Toast.makeText(this, "请前往设置打开权限", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginActivity.this.finish();
                        finish();
                    }
                }
            }

        }
    }
    // 提示用户去应用设置界面手动开启权限
//
//    private void showDialogTipUserGoToAppSettting() {
//
//        dialog = new AlertDialog.Builder(this)
//                .setTitle("存储权限不可用")
//                .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
//                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 跳转到应用设置界面
//                        goToAppSetting();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                }).setCancelable(false).show();
//    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
    //
//            @Override
//     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                super.onActivityResult(requestCode, resultCode, data);
//                if (requestCode == 123) {
//
//                       if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                 // 检查该权限是否已经获取
//                               int i = ContextCompat.checkSelfPermission(this, permissions[0]);
//                                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//                               if (i != PackageManager.PERMISSION_GRANTED) {
//                                         // 提示用户应该去应用设置界面手动开启权限
//                                         showDialogTipUserGoToAppSettting();
//                                     } else {
//                                        if (dialog != null && dialog.isShowing()) {
//                                                 dialog.dismiss();
//                                             }
//                                       Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
//                                    }
//                            }
//                    }
//            }
}
