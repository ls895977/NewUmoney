package com.example.qqlaobing.Umoney.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;

import java.util.HashMap;
import java.util.Map;

public class RenZhengWebOpeFragment extends Fragment {
    private WebView webView;
    private String orderno="D-180725CAF1";
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_web_ope, container, false);
        webView=(WebView)rootView.findViewById(R.id.web);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "token" , NetRequestBusinessDefine.K_TOKEN ) ;

        webView.loadUrl(NetRequestBusinessDefine.K_HOST_NAME+"/public/tmobile?token="+NetRequestBusinessDefine.K_TOKEN);
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
        return rootView;
    }
}
