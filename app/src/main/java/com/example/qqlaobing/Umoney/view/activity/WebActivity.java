package com.example.qqlaobing.Umoney.view.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;

import com.githang.statusbar.StatusBarCompat;
import com.maning.updatelibrary.InstallUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private String url;
    //关于进度显示
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"));
        webView=(WebView)findViewById(R.id.web);
        setWebView();
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        webView.setWebChromeClient(new WebChromeClient());
        Map<String, String > map = new HashMap<String, String>() ;
                     map.put( "token" , NetRequestBusinessDefine.K_TOKEN ) ;
        webView.loadUrl(url);
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
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                //下载APK
                InstallUtils.with(WebActivity.this)
                        //必须-下载地址
                        .setApkUrl(s)
                        //非必须-下载保存的文件的完整路径+/name.apk，使用自定义路径需要获取读写权限
                        //非必须-下载回调
                        .setCallBack(new InstallUtils.DownloadCallBack() {
                            @Override
                            public void onStart() {
                                //下载开始
                                progressDialog.show();
                            }

                            @Override
                            public void onComplete(String path) {
                                progressDialog.cancel();
                                //下载完成
                                //先判断有没有安装权限---适配8.0
                                //如果不想用封装好的，可以自己去实现8.0适配
                                InstallUtils.checkInstallPermission(WebActivity.this, new InstallUtils.InstallPermissionCallBack() {
                                    @Override
                                    public void onGranted() {
                                        //去安装APK
                                        installApk(path);
                                    }

                                    @Override
                                    public void onDenied() {
                                        //弹出弹框提醒用户
                                        AlertDialog alertDialog = new AlertDialog.Builder(WebActivity.this)
                                                .setTitle("温馨提示")
                                                .setMessage("必须授权才能安装APK，请设置允许安装")
                                                .setNegativeButton("取消", null)
                                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //打开设置页面
                                                        InstallUtils.openInstallPermissionSetting(WebActivity.this, new InstallUtils.InstallPermissionCallBack() {
                                                            @Override
                                                            public void onGranted() {
                                                                //去安装APK
                                                                installApk(path);
                                                            }

                                                            @Override
                                                            public void onDenied() {
                                                                //还是不允许咋搞？
//                                                                Toast.makeText(WebActivity.this, "不允许安装咋搞？强制更新就退出应用程序吧！", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                })
                                                .create();
                                        alertDialog.show();
                                    }
                                });

                            }

                            @Override
                            public void onLoading(long total, long current) {
                                //下载中
                                progressDialog.setIndeterminate(false);
                                progressDialog.setMax((int) total);
                                progressDialog.setProgress((int)current);
                            }

                            @Override
                            public void onFail(Exception e) {
                                //下载失败
                                progressDialog.cancel();
                            }

                            @Override
                            public void cancle() {
                                //下载取消
                                progressDialog.cancel();
                            }
                        })
                        //开始下载
                        .startDownload();



            }
        });
    }
    private void installApk(String path){
        //安装APK
        InstallUtils.installAPK(WebActivity.this, path, new InstallUtils.InstallCallBack() {
            @Override
            public void onSuccess() {
                //onSuccess：表示系统的安装界面被打开
                //防止用户取消安装，在这里可以关闭当前应用，以免出现安装被取消
                Toast.makeText(WebActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                //安装出现异常，这里可以提示用用去用浏览器下载安装
                InstallUtils.installAPKWithBrower(WebActivity.this, url);
            }
        });
    }
    public void setWebView(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

//相关属性
        progressDialog =new ProgressDialog(WebActivity.this);
        progressDialog.setMessage("正在下载...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
    }


}
