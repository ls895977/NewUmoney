package com.example.qqlaobing.Umoney.view.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.qqlaobing.Umoney.R;

import java.text.SimpleDateFormat;


/**
 *
 * @author zhanglizhi
 * @date 2018/7/25
 */

public class ReListView extends ListView implements AbsListView.OnScrollListener {
    private View footview;
    private View headview;
    private int totaItemCounts;
    private int lasstVisible;
    private int fistVisiable;
    private LoadListener loadListener;
    private int footViewHeight;
    private int headViewHeight;
    private int yload;
    boolean isLoading;
    private TextView updateInfo;
    private TextView updateTime;
    private ProgressBar progressBar;

    public ReListView(Context context) {
        super(context);
        initView(context);
    }

    public ReListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //拿到头布局xml
        headview= LayoutInflater.from(context).inflate(R.layout.listview_header,null);
        updateInfo=(TextView)headview.findViewById(R.id.update_info);
        updateTime=(TextView)headview.findViewById(R.id.update_time);
        progressBar=(ProgressBar)headview.findViewById(R.id.progressbar);
        updateTime.setText("更新于："+new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(System.currentTimeMillis()));
        //拿到尾布局xml
        footview=LayoutInflater.from(context).inflate(R.layout.listview_footer,null);
        //测量footview的高度
        footview.measure(0,0);
        //拿到高度
        footViewHeight=footview.getMeasuredHeight();
        //隐藏view
        footview.setPadding(0,-footViewHeight,0,0);
        headview.measure(0,0);
        headViewHeight=headview.getMeasuredHeight();
        headview.setPadding(0,-headViewHeight,0,0);
        //设置不可见
        // footview.findViewById(R.id.foot_load).setVisibility(View.GONE);
        // headview.findViewById(R.id.head).setVisibility(View.GONE);
        //添加到listview底部
        this.addFooterView(footview);
        //添加到listview头部
        this.addHeaderView(headview);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                yload=(int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY=(int)ev.getY();
                int paddingY=-headViewHeight+(moveY-yload)/2;

                if (paddingY<0){
                    updateInfo.setText("下拉刷新。。。");
                    progressBar.setVisibility(View.GONE);
                }
                if (paddingY>0){
                    updateInfo.setText("松开刷新。。。");
                    progressBar.setVisibility(View.GONE);
                }

                headview.setPadding(0,paddingY,0,0);

                break;
//            case MotionEvent.ACTION_UP:
//                headview.setPadding(0,0,0,0);
//                updateInfo.setText("正在刷新。。。");
//                progressBar.setVisibility(View.VISIBLE);
//                loadListener.pullLoad();
//
//                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totaItemCounts==lasstVisible&&scrollState==SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading=true;
                // footview.findViewById(R.id.foot_load).setVisibility(View.VISIBLE);
                footview.setPadding(0,0,0,0);
                //加载数据
                loadListener.onLoad();

            }
        }
        if (fistVisiable==0){
            headview.setPadding(0,0,0,0);
            updateInfo.setText("正在刷新。。。");
            progressBar.setVisibility(View.VISIBLE);
            loadListener.pullLoad();
        }
    }



    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.fistVisiable=firstVisibleItem;
        this.lasstVisible=firstVisibleItem+visibleItemCount;
        this.totaItemCounts=totalItemCount;

    }
    //加载完成
    public void loadComplete(){
        isLoading=false;
        //footview.findViewById(R.id.foot_load).setVisibility(View.GONE);
        footview.setPadding(0,-footViewHeight,0,0);
        headview.setPadding(0,-headViewHeight,0,0);


    }
    public void setInterface(LoadListener loadListener){
        this.loadListener=loadListener;

    }
    //接口回调
    public interface LoadListener{

        void onLoad();
        void pullLoad();


    }

}