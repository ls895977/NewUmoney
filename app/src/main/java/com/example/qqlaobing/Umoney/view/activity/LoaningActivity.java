package com.example.qqlaobing.Umoney.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.adapter.TraceListAdapter;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.been.Trace;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.HomePresenterImpl;
import com.example.qqlaobing.Umoney.view.fragment.ReListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;

public class LoaningActivity extends Fragment implements View.OnClickListener {
    @BindView(R.id.tv_auto)
    AutoVerticalScrollTextView tvAuto;
    @BindView(R.id.banner_auto)
    Banner bannerAuto;
    @BindView(R.id.tv_describle)
    TextView tvDescrible;
    Unbinder unbinder;
    private TextView tv_loansure, tv_loan;
    private TextView tvLimitMoney, tv_card_no;
    private double mExitTime;
    private List<Trace> traceList;
    private ReListView listView;
    private String url;
    private String orderid;
    private List list = new ArrayList();
    private AutoVerticalScrollTextViewUtil autoVerticalScrollTextViewUtil;
    private JSONArray latest_loads;
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
                        traceList = new ArrayList<>();
                        Log.e("111", "main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            JSONObject data = results.getJSONObject("data");
                            tvDescrible.setText(data.getString("loan_desc"));
                            latest_loads = data.getJSONArray("latest_loads");
                            for (int i = 0; i < latest_loads.length(); i++) {
                                JSONObject latestLog = latest_loads.getJSONObject(i);
                                list.add("尾号" + latestLog.getString("telephone") + "用户" + "成功申请" + latestLog.getString("amount") + "元");
                            }
                            Log.e("zlz", "aaaaaaaa");
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

                            if (!data.getString("amount_limit").equals("")) {
                                tvLimitMoney.setText(data.getString("amount_limit"));
                            }
//                            if (!data.getString("card_desc_cn").equals("")){
//                                tv_card_cn.setText(data.getString("card_desc_cn"));
//                            }
//                            if (!data.getString("card_desc_en").equals("")){
//                                tv_card_en.setText(data.getString("card_desc_en"));
//                            }
//                            if (!data.getString("card_no").equals("")){
//                                tv_card_no.setText(data.getString("card_no"));
//                            }
                            if (data.getString("loan").equals("null")) {
                                Log.e("eeee", "false222");
                                NetRequestBusinessDefine.loan = false;
                                MainActivity mainActivity = (MainActivity) getActivity();
                                Intent intent = new Intent(mainActivity, MainActivity.class);
                                startActivity(intent);
                                mainActivity.finish();
                                break;
                            }
                            JSONObject loan = data.getJSONObject("loan");
                            JSONArray step = loan.getJSONArray("step");
                            for (int i = 0; i < step.length(); i++) {
                                Trace trace = new Trace();
                                trace.setAcceptTime(((JSONObject) step.get(step.length() - i - 1)).getString("addtime"));
                                trace.setAcceptStation(((JSONObject) step.get(step.length() - i - 1)).getString("act"));
                                if (((JSONObject) step.get(step.length() - i - 1)).has("desc")) {
                                    Log.e("desc", ((JSONObject) step.get(step.length() - i - 1)).getString("desc"));
                                    trace.setDesc(((JSONObject) step.get(step.length() - i - 1)).getString("desc"));
                                }
                                traceList.add(trace);
                            }

                            if (loan.getInt("shenhestatus") == 0) {
                                Log.e("111", "1");
                                tv_loansure.setVisibility(View.VISIBLE);
                                String time = loan.getString("next_loan_time");
                                tv_loansure.setText("下次借款时间：" + time);
//                                tv_certificationcenter.setEnabled(false);
                            }
                            if (loan.getInt("showconfirm") == 1) {
                                Log.e("111", "2");
                                showConfirm = 1;
                                tv_loansure.setText("立即借款");
                                tv_loansure.setVisibility(View.VISIBLE);
                                orderno = loan.getString("orderno");
                                orderid = loan.getString("id");
                                NetRequestBusinessDefine.oderid = orderno;
                                NetRequestBusinessDefine.oderno = orderid;
                                Log.e("111", loan.getString("orderno") + "");
                            }
                            if (!loan.getString("h5_desc").equals("")) {
                                tv_loansure.setVisibility(View.VISIBLE);
                                tv_loansure.setText(loan.getString("h5_title"));
                                url = loan.getString("h5_url");
                                showConfirm = 2;
                                Trace trace = new Trace();
                                trace.setDesc(loan.getString("h5_desc"));
                                trace.setAcceptStation("审核未通过");
                                traceList.add(trace);
                            }
                            if (loan.getInt("showcontact") == 1) {
                                Log.e("111", "3");
                                showcontact = 1;
                                showConfirm = 0;
                                orderno = loan.getString("orderno");
                                orderid = loan.getString("id");
                                NetRequestBusinessDefine.oderid = orderno;
                                NetRequestBusinessDefine.oderno = orderid;
                                if (loan.getString("pay_url").equals("")) {
                                    tv_loan.setVisibility(View.GONE);
                                } else {
                                    tv_loan.setVisibility(View.VISIBLE);
                                }
                                pay_url = loan.getString("pay_url");
                                tv_loansure.setText("点击联系我们");
                                tv_loansure.setVisibility(View.VISIBLE);
                            }
                            if (loan.getInt("status") == 0) {
                                Log.e("111", "4");
                                tv_loansure.setVisibility(View.VISIBLE);
                                tv_loansure.setText("等待审核");
//                                tv_certificationcenter.setEnabled(false);
                            }
                            TraceListAdapter adapter = new TraceListAdapter(getActivity(), traceList);
                            listView.setAdapter(adapter);
                            listView.loadComplete();

                        } else {
                            listView.loadComplete();
                            Toast.makeText(GeneralApplication.getInstance(), response.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        listView.loadComplete();
                        e.printStackTrace();
                    }
                    break;
                case NetRequestBusinessDefine.K_POST_APPLY:
                    try {
                        if (msg.arg1 == 404) {
                            return;
                        }
                        Log.e("111", "main1");
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            tv_loansure.setText("等待放款");
                            tv_loansure.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "贷款提交成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_LONG).show();
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
    private int showConfirm;
    private int showcontact;
    private String orderno;
    private ImageView iv_cancel;
    private ImageView iv_line_step2;
    private View rootView;
    private Handler handler = new Handler();
    private String pay_url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_loaning, container, false);
        initView();
        initData();
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initData() {
//        tv_certificationcenter.setOnClickListener(this);
//        tv_certificationcenter.setEnabled(false);
        tv_loansure.setOnClickListener(this);
        tv_loansure.setVisibility(View.INVISIBLE);
        iv_cancel.setOnClickListener(this);
        SharedPreferences share = getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
        if (share != null) {
            tv_card_no.setText(share.getString("username", ""));
        }
        HomePresenterImpl homePresenter = new HomePresenterImpl(mHandler);
        homePresenter.getData();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                HomePresenterImpl homePresenter = new HomePresenterImpl(mHandler);
                homePresenter.getData();
                handler.postDelayed(this, 1000 * 60 * 2);
            }
        };
        handler.postDelayed(runnable, 1000 * 60 * 2);
        listView.setInterface(new ReListView.LoadListener() {
            @Override
            public void onLoad() {
                listView.loadComplete();
            }

            @Override
            public void pullLoad() {
                HomePresenterImpl homePresenter = new HomePresenterImpl(mHandler);
                homePresenter.getData();
            }
        });
    }

    private void initView() {
        listView = (ReListView) rootView.findViewById(R.id.lvTrace);
//        tv_certificationcenter=(TextView)rootView.findViewById(R.id.tv_certificationcenter);
        tv_loansure = (TextView) rootView.findViewById(R.id.tv_loansure);
//        tv_card_en=(TextView)rootView.findViewById(R.id.tv_card_en);
//        tv_card_cn=(TextView)rootView.findViewById(R.id.tv_card_cn);
        tv_card_no = (TextView) rootView.findViewById(R.id.tv_card_no);
        tvLimitMoney = (TextView) rootView.findViewById(R.id.tv_limitmoney);
        tv_loan = (TextView) rootView.findViewById(R.id.tv_loan);
        tv_loan.setOnClickListener(this);
        iv_cancel = (ImageView) rootView.findViewById(R.id.iv_cancel);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_certificationcenter:
//                startActivity(new Intent(getActivity(),CertificationCenterActivity.class));
//                getActivity().finish();
//                break;
            case R.id.iv_cancel:
                break;
            case R.id.tv_loansure:
                if (showConfirm == 2) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                if (showConfirm == 1) {
                    //立即借款
                    Intent intent = new Intent(getActivity(), LoanActivity.class);
                    startActivity(intent);
//                    ApplyConfirmPresenterImpl applyConfirmPresenter=new ApplyConfirmPresenterImpl(mHandler,orderno);
//                    applyConfirmPresenter.postData();
                } else if (showcontact == 1) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    url = NetRequestBusinessDefine.K_HOST_NAME + "/public/contactus?orderno=" + orderno + "&token=" + NetRequestBusinessDefine.K_TOKEN;
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                break;
            case R.id.tv_loan:
                Intent intent = new Intent(getActivity(), WebActivity.class);
                url = NetRequestBusinessDefine.K_HOST_NAME + pay_url;
                intent.putExtra("url", url);
                startActivity(intent);
                //还款
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
}
