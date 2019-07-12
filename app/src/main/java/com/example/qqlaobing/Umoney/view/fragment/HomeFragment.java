package com.example.qqlaobing.Umoney.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.QuanBeen;
import com.example.qqlaobing.Umoney.been.UserInfoBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.HomePresenterImpl;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.LoanMoneyPresenterImpl;
import com.example.qqlaobing.Umoney.view.activity.CertificationCenterActivity;
import com.example.qqlaobing.Umoney.view.activity.LoanActivity;
import com.example.qqlaobing.Umoney.view.activity.LoginActivity;
import com.example.qqlaobing.Umoney.view.activity.MainActivity;
import com.example.qqlaobing.Umoney.view.activity.RenZhengActivity;
import com.example.qqlaobing.Umoney.view.activity.StartActivity;
import com.example.qqlaobing.Umoney.view.activity.WebActivity;
import com.example.qqlaobing.Umoney.view.myview.PickerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;

/**
 * Created by zhanglizhi on 2018/7/13.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.tv_auto)
    AutoVerticalScrollTextView tvAuto;
    @BindView(R.id.banner_auto)
    Banner bannerAuto;
    Unbinder unbinder;
    private View rootView;
    private TextView tv_limitmoney, tv_loan;
    private PickerView pv_money, pv_time;
    private ImageView tv_certificationcenter, iv_clock;
    private boolean click = false;
    private TextView tv_card_en, tv_card_cn, tv_card_no;
    private String mmoney, day;
    private List<QuanBeen> time = new ArrayList<QuanBeen>();
    private List<String> money = new ArrayList<String>();
    private List<String> money1 = new ArrayList<String>();
    private List<String> loantime = new ArrayList<String>();
    private List list = new ArrayList();
    private AutoVerticalScrollTextViewUtil autoVerticalScrollTextViewUtil;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_HOME:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("111", "main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            if (!data.getString("amount_limit").equals("")) {
                                tv_limitmoney.setText(data.getString("amount_limit"));
                            }
                                /*if (!data.getString("card_desc_cn").equals("")) {
                                    tv_card_cn.setText(data.getString("card_desc_cn"));
                                }
                                if (!data.getString("card_desc_en").equals("")) {
                                    tv_card_cn.setText(data.getString("card_desc_en"));
                                }
                                if (!data.getString("card_no").equals("")) {
                                    tv_card_no.setText(data.getString("card_no"));
                                }*/
                            JSONObject loans = data.getJSONObject("loans");
                            JSONArray days = loans.getJSONArray("days");
                            JSONArray amount = loans.getJSONArray("amount");
                            Log.e("zlz","aaaaaaaa");
                            latest_loads = data.getJSONArray("latest_loads");
                            for (int i = 0; i < latest_loads.length(); i++) {
                                JSONObject latestLog = latest_loads.getJSONObject(i);
                                list.add("尾号" + latestLog.getString("telephone") + "用户" + "成功申请" + latestLog.getString("amount") + "元");
                            }
                            Log.e("zlz","aaaaaaaa");
                            // 初始化AutoVerticalScrollTextView控制器

                            autoVerticalScrollTextViewUtil = new AutoVerticalScrollTextViewUtil(tvAuto, (ArrayList<CharSequence>) list);
                            // 设置滚动的时间间隔
                            autoVerticalScrollTextViewUtil.setDuration(4000);
                            autoVerticalScrollTextViewUtil.stop();
                            // 开启滚动
                            autoVerticalScrollTextViewUtil.start();


                            JSONArray banner = data.getJSONArray("ads");
                            final List<String> images = new ArrayList<>();
                            final List<String> urls = new ArrayList<>();
                            for (int a = 0; a < banner.length(); a++) {
                                JSONObject object = banner.getJSONObject(a);
                                images.add(object.getString("image"));
                                urls.add(object.getString("url"));
                            }
                            bannerAuto.setImageLoader(new GlideImageLoader());
                            bannerAuto.setImages(images);
                            bannerAuto.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                            bannerAuto.isAutoPlay(true);
                            bannerAuto.setDelayTime(5000);
                            bannerAuto.start();
                            bannerAuto.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    Intent intent = new Intent(getActivity(), WebActivity.class);
                                    intent.putExtra("url", urls.get(position));
                                    startActivity(intent);
                                }
                            });

                            NetRequestBusinessDefine.STEP=data.getInt("step")+1;
                            Log.e("zlzstep",NetRequestBusinessDefine.STEP+"");
                            if (data.getString("authstatus").equals("1")) {
                                click = true;
                            } else {
                                click = false;
                                Log.e("111", "main");
                            }
                            for (int i = 0; i < days.length(); i++) {
                                JSONObject day = days.getJSONObject(i);
                                QuanBeen quanBeen = new QuanBeen();
                                quanBeen.setStatus(day.getInt("status"));
                                quanBeen.setTimeto(day.getString("val") + "天");
                                time.add(quanBeen);
                                loantime.add(day.getString("val") + "天");
                            }
                            pv_money.setVisibility(View.VISIBLE);
                            pv_time.setVisibility(View.VISIBLE);
                            for (int j = 0; j < amount.length(); j++) {
                                JSONObject object = amount.getJSONObject(j);
                                money.add(object.getString("amount"));
                                money1.add(object.getString("amount"));
                            }
                            Log.e("eeeeemmmm", money.toString());
                            pv_money.setData(money);
                            pv_time.setData(loantime);
                            if (money.size() != 0 && loantime.size() != 0) {
                                pv_time.setSelected(1);
                                pv_money.setSelected(1);
                                day = time.get(1).getTimeto();
                                mmoney = money.get(1);
                            }

                            pv_time.setOnSelectListener(new PickerView.onSelectListener() {
                                @Override
                                public void onSelect(String text, int position) {
                                    Log.e("eeeee", position + "val");
                                    if (time.get(position).getStatus() == 0) {
                                        iv_clock.setVisibility(View.VISIBLE);
                                        tv_loan.setEnabled(false);
                                    } else {
                                        iv_clock.setVisibility(View.GONE);
                                        tv_loan.setEnabled(true);
                                    }
                                    day = time.get(position).getTimeto();
                                }
                            });
                            pv_money.setOnSelectListener(new PickerView.onSelectListener() {
                                @Override
                                public void onSelect(String text, int position) {
                                    Log.e("eeeee", position + "val");
                                    Log.e("eeeee", position + "--" + money.get(position));
                                    Log.e("eeeee", money.toString());

                                    mmoney = money1.get(position);

                                }
                            });


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_GET_MONEY:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        Log.e("11111",resultCode);
                        if (resultCode.equals("401")){
                            NetRequestBusinessDefine.login=false;
                        }
                        if (resultCode.equals("1")) {
                            NetRequestBusinessDefine.loan=true;
                            MainActivity mainActivity=(MainActivity)getActivity();
                            mainActivity.gotoHomeFragment();
                        } else {
                            Log.e("11111",response.getString("msg"));
                            Log.e("11111","1");
                            Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_LONG).show();
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
    private UserInfoBeen userInfoBeen;
    private HomePresenterImpl homePresenter;
    private JSONArray latest_loads;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initData();
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initData() {
        SharedPreferences share1 = getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (!share1.getString("token", "").equals("")) {
            Log.e("eeee", "12343");
            NetRequestBusinessDefine.login = true;
            NetRequestBusinessDefine.K_TOKEN = share1.getString("token", "");
            Log.e("eeee", share1.getString("token", ""));
            homePresenter = new HomePresenterImpl(mHandler);
            homePresenter.getData();
        }
        SharedPreferences share = getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (share != null) {
            tv_card_no.setText(share.getString("username", ""));
        }
    }

    private void initView() {
        tv_certificationcenter = rootView.findViewById(R.id.tv_certificationcenter);
        tv_limitmoney = rootView.findViewById(R.id.tv_limitmoney);
        tv_loan = rootView.findViewById(R.id.tv_loan);
        pv_money = rootView.findViewById(R.id.pv_money);
        tv_card_no = rootView.findViewById(R.id.tv_card_no);
        pv_time = rootView.findViewById(R.id.pv_time);
        iv_clock = rootView.findViewById(R.id.iv_clock);
        iv_clock.setVisibility(View.GONE);
        tv_loan.setOnClickListener(this);
        tv_certificationcenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_loan:
                if (!NetRequestBusinessDefine.login) {
                    MainActivity mainActivity1 = (MainActivity) getActivity();
                    startActivity(new Intent(mainActivity1, LoginActivity.class));
                    mainActivity1.finish();
                    break;
                }
                if (click) {
                    SharedPreferences share = getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
//                    if (share.getString("xieyi", "").equals("1")) {
//                        PopWindowavator(view);
//                    }else {
//                        MainActivity mainActivity = (MainActivity) getActivity();
//                        mainActivity.goTOLoan();
//                    }
                    LoanMoneyPresenterImpl loanMoneyPresenter=new LoanMoneyPresenterImpl(mHandler);
                    loanMoneyPresenter.postData();

                } else {
                    Toast.makeText(getActivity(), "请先完善认证资料", Toast.LENGTH_SHORT).show();
                    MainActivity mainActivity5 = (MainActivity) getActivity();
                    startActivity(new Intent(mainActivity5, RenZhengActivity.class));
                    mainActivity5.finish();
                }
                break;
            case R.id.tv_certificationcenter:
                if (!NetRequestBusinessDefine.login) {
                    MainActivity mainActivity4 = (MainActivity) getActivity();
                    startActivity(new Intent(mainActivity4, LoginActivity.class));
                    mainActivity4.finish();
                    break;
                }
                startActivity(new Intent(getActivity(), CertificationCenterActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, final ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            Glide.with(context).load(path).placeholder(R.drawable.center_bg)
                    .error(R.drawable.center_bg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            imageView.setBackground(glideDrawable);
                        }
                    });
//            //Glide 加载图片简单用法
//            Glide.with(context).load(path).into(imageView);
        }
    }
    private void PopWindowavator(View v) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_xieyi, null);
        final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        ColorDrawable cd = new ColorDrawable();
        pop.setBackgroundDrawable(cd);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(new poponDismissListener());
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);
        //设置 背景的颜色为 0.5f 的透明度
        backgroundAlpha(0.5f);
        view.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = v.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        pop.dismiss();
                    }
                }
                return true;
            }
        });
        WebView webView=view.findViewById(R.id.web);
        TextView textView=view.findViewById(R.id.tv_sure);
        CheckBox cb_checkbox=view.findViewById(R.id.cb_checkbox);
        textView.setBackgroundResource(R.drawable.btn_noselect);
        textView.setEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // User settings

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(NetRequestBusinessDefine.K_HOST_NAME+"/public/agreement");
//        webView.loadUrl(NetworkUtil.SERVER_HOST+"/public/contactus?orderno="+orderno+"&token="+GlobalSettingParameter.TOKEN);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = v.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        pop.dismiss();
                    }
                }
                return true;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("umoney", Context.MODE_PRIVATE); //私有数据
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString("xieyi", "2");
                editor.commit();//提交修改
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.goTOLoan();
                pop.dismiss();
            }
        });
        cb_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    textView.setBackgroundResource(R.drawable.btn_allshap);
                    textView.setEnabled(true);
                }else {
                    textView.setBackgroundResource(R.drawable.btn_noselect);
                    textView.setEnabled(false);
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }
    }

}
