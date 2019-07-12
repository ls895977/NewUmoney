package com.example.qqlaobing.Umoney.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.UserInfoPresenterImpl;
import com.example.qqlaobing.Umoney.utils.Auth;
import com.example.qqlaobing.Umoney.utils.PictureUtils;
import com.example.qqlaobing.Umoney.view.activity.CertificationCenterActivity;
import com.example.qqlaobing.Umoney.view.activity.RenZhengActivity;
import com.example.qqlaobing.Umoney.view.activity.UserInfoActivity;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.namee.permissiongen.PermissionGen;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RenZhengUserinfoFragment extends Fragment implements View.OnClickListener{
    private String authKey = "f932ff6d-6c73-4c31-9acf-b97575fc31ca";
    private String urlNotify = "http://211.149.180.198";
    private ImageView iv_zheng,iv_zheng_add,iv_zheng_delet,tv_exit,
            iv_fan,iv_fan_add,iv_fan_delet,iv_people,iv_people_add,iv_people_delet;
    private TextView tv_finish;
    private EditText et_username,et_cardid,et_telephone;
    private String idcardimg1,idcardimg2,idcardimg3,key1,key2,key3;
    private String photoPath;
    protected File cameraFile;
    private String[] imagePath = new String[3];
    private String[] imageKey = new String[3];
    private int position=1;//1为正面，2为反面，3为人
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                dialog.close();
                Toast.makeText(getActivity(),"无返回结果",Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_POSTUSER_INFO:
                    dialog.close();
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
//                        JSONObject results = response.getJSONObject("data");
                        if (resultCode.equals("401")){
                            dialog.close();
                            Toast.makeText(getActivity(),"登录凭证失效，请重新登录",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (resultCode.equals("1")) {
//                            dialog.close();
                            Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                            RenZhengActivity renZhengActivity=(RenZhengActivity)getActivity();
                            renZhengActivity.ChangeFragment(2);
                        }
                        else {
//                            dialog.close();
                            Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
//                            Toast.makeText(UserInfoActivity.this,"1111",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
//                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    };
    private KeyGenerator keyGen;
    private Recorder recorder;
    private UploadManager uploadManager;
    String accessKey = "XwR_q3yAUhRxGPQaQRqJGZPP_joMJ40sVf7EgUy1";
    String secretKey = "YLsDZ4W_YnHMfYiY9Y1L2SyGXZRp5OVc0yYEZOFY";
    String bucket = "imgserver";
    private String upToken;
    private LoadingDialog dialog;
    private String sanfang;
    private String url_frontcard;
    private String url_backcard;
    private String url_photoliving;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
        initView();
        initData();
        return rootView;
    }
    private void initData() {
        iv_zheng.setOnClickListener(this);
        iv_zheng_add.setOnClickListener(this);
        iv_zheng_delet.setOnClickListener(this);
        iv_fan.setOnClickListener(this);
        iv_fan_add.setOnClickListener(this);
        iv_fan_delet.setOnClickListener(this);
        iv_people.setOnClickListener(this);
        iv_people_add.setOnClickListener(this);
        iv_people_delet.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_exit.setOnClickListener(this);

        Auth auth = Auth.create(accessKey, secretKey);
        upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
    }
    private void initView() {
        iv_zheng=(ImageView)rootView.findViewById(R.id.iv_zheng);
        iv_zheng_add=(ImageView)rootView.findViewById(R.id.iv_zheng_add);
        iv_zheng_delet=(ImageView)rootView.findViewById(R.id.iv_zheng_delet);
        iv_fan=(ImageView)rootView.findViewById(R.id.iv_fan);
        iv_fan_add=(ImageView)rootView.findViewById(R.id.iv_fan_add);
        iv_fan_delet=(ImageView)rootView.findViewById(R.id.iv_fan_delet);
        iv_people=(ImageView)rootView.findViewById(R.id.iv_people);
        iv_people_add=(ImageView)rootView.findViewById(R.id.iv_people_add);
        iv_people_delet=(ImageView)rootView.findViewById(R.id.iv_people_delet);
        tv_exit=(ImageView)rootView.findViewById(R.id.tv_exit);
        tv_finish=(TextView) rootView.findViewById(R.id.tv_finish);
        et_username=(EditText) rootView.findViewById(R.id.et_username);
        et_cardid=(EditText) rootView.findViewById(R.id.et_cardid);
        et_telephone=(EditText) rootView.findViewById(R.id.et_telephone);
    }
    private void getData(){
        try {
            JSONObject jsonObject=new JSONObject(sanfang);
            url_frontcard = jsonObject.getString("url_frontcard");
            url_backcard = jsonObject.getString("url_backcard");
            url_photoliving = jsonObject.getString("url_photoliving");
            et_username.setText(jsonObject.getString("id_name"));
            et_cardid.setText(jsonObject.getString("id_no"));
            SharedPreferences share = getActivity().getSharedPreferences("umoney", Activity.MODE_PRIVATE);
            if (share != null) {
                et_telephone.setText(share.getString("username", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        UserInfoPresenterImpl userInfoPresenter=new UserInfoPresenterImpl(mHandler);
//        userInfoPresenter.getData();
        Glide.with(getActivity()).load(url_frontcard)
                .into(new SimpleTarget<GlideDrawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv_zheng.setBackground(resource);
                    }
                });
        new getImageCacheAsyncTask(getActivity(),0).execute(url_frontcard);
        iv_zheng_add.setVisibility(View.INVISIBLE);
        Glide.with(getActivity()).load(url_backcard)
                .into(new SimpleTarget<GlideDrawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Log.e("resource",resource.toString());
                        iv_fan.setBackground(resource);
                    }
                });
        iv_fan_add.setVisibility(View.INVISIBLE);
        new getImageCacheAsyncTask(getActivity(),1).execute(url_backcard);
        Glide.with(getActivity()).load(url_photoliving)
                .into(new SimpleTarget<GlideDrawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv_people.setBackground(resource);
                    }
                });
        new getImageCacheAsyncTask(getActivity(),2).execute(url_photoliving);
        iv_people_add.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_zheng_add:
//                position=1;
//                test();
//                ShowMenu(iv_zheng_add);
                start();
                break;
            case R.id.iv_zheng_delet:
                photoPath=null;
                iv_zheng.setBackgroundDrawable(null);
                iv_zheng_add.setVisibility(View.VISIBLE);
                iv_zheng_delet.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_fan_add:
//                position=2;
//                test();
//                ShowMenu(iv_fan_add);
                start();
                break;
            case R.id.iv_fan_delet:
                photoPath=null;
                iv_fan.setBackgroundDrawable(null);
                iv_fan_add.setVisibility(View.VISIBLE);
                iv_fan_delet.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_people_add:
//                position=3;
//                test();
//                ShowMenu(iv_people_add);
                start();
                break;
            case R.id.iv_people_delet:
                photoPath=null;
                iv_people.setBackgroundDrawable(null);
                iv_people_add.setVisibility(View.VISIBLE);
                iv_people_delet.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_finish:
                if (et_username.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请输入姓名",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (et_cardid.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"请输入身份证号",Toast.LENGTH_SHORT).show();
                    break;
                }
//                if (et_telephone.getText().toString().equals("")){
//                    Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
//                    break;
//                }
                if (iv_zheng.getBackground()==null||iv_fan.getBackground()==null||iv_people.getBackground()==null){
                    Toast.makeText(getActivity(),"请上传照片",Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    Log.i("qiniu","start0");
                    if (imagePath[0]==null||imagePath[1]==null||imagePath[2]==null){
                        imagePath=null;
//                        Toast.makeText(this,"请重新上传照片",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Log.i("qiniu","start0");
                    Log.i("qiniu","----------"+imagePath.length);
                    dialog = new LoadingDialog(getActivity(), "上传中。。。");
                    dialog.show();
                    for (int i=0;i<imagePath.length;i++){
                        Log.e("qiniu","start");
                        Log.e("qiniu1",imagePath.length+"");
                        Log.e("qiniu2",imagePath[i]+"");
                        String token = upToken;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String key1 = "icon_"+i+"" + sdf.format(new Date());
                        final int finalI = i;
                        uploadManager.put(imagePath[i], key1, token,
                                new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                                        if(info.isOK()) {
                                            imageKey[finalI]=key;
                                            imageKey[finalI]=key;
                                            if (!(imageKey[2]==null)&&!(imageKey[0]==null)&&!(imageKey[1]==null)){
                                                //验证接口
//                                               Toast.makeText(UserInfoActivity.this,"后台开始上传",Toast.LENGTH_SHORT).show();
                                                Log.e("qiniu", imageKey.toString());
                                                UserInfoPresenterImpl userInfoPresenter=new UserInfoPresenterImpl(mHandler,et_username.getText().toString(),et_cardid.getText().toString(),
                                                        et_telephone.getText().toString(),imageKey[0],imageKey[1],imageKey[2]);
                                                userInfoPresenter.postData();
                                                imageKey[0]=null;
                                                imageKey[1]=null;
                                                imageKey[2]=null;
                                            }
                                            Log.e("qiniu", "成功");
                                            Log.e("qiniu", res.toString());
                                        } else {
                                            dialog.close();
                                            Looper.prepare();
                                            Toast.makeText(getActivity(),"图片上传失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                                            Log.e("qiniu", "错误");
                                            Looper.loop();
                                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        }
                                        Log.e("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                    }
                                }, null);
                    }
                }
                break;
            case R.id.tv_exit:
                break;
            default:break;
        }
    }
    public void start(){
        String id = "demo_"+System.currentTimeMillis();
        AuthBuilder mAuthBuilder = new AuthBuilder(id, authKey, urlNotify, new OnResultListener() {
            @Override
            public void onResult(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    int code=jsonObject.getInt("ret_code");
                    if (code==000000){
                        sanfang=s;
                        getData();
//                        Intent intent=new Intent(CertificationCenterActivity.this,UserInfoActivity.class);
//                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("error",s);
            }

        });
        mAuthBuilder.faceAuth(getActivity());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) { // capture new image
                if (photoPath != null)
                    Log.e("eeeee",cameraFile.toString());

                Luban.with(getActivity())
                        .load(cameraFile)                     //传人要压缩的图片
                        //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调

                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }
                            @Override
                            public void onSuccess(File file) {
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                                photoPath = file.getAbsolutePath();
                                Log.e("qiniu222",photoPath);
                                Log.e("qiniu","111111");
                                Drawable drawable=Drawable.createFromPath(photoPath);
                                if (position==1){
                                    imagePath[0]=photoPath;
                                    Log.e("qiniu",photoPath);
                                    Log.e("qiniu1",imagePath[0]);
                                    idcardimg1=photoPath;
                                    iv_zheng.setBackgroundDrawable(drawable);
                                    iv_zheng_add.setVisibility(View.INVISIBLE);
                                    iv_zheng_delet.setVisibility(View.VISIBLE);
                                }else if (position==2){
                                    imagePath[1]=photoPath;
                                    idcardimg2=photoPath;
                                    iv_fan.setBackgroundDrawable(drawable);
                                    iv_fan_add.setVisibility(View.INVISIBLE);
                                    iv_fan_delet.setVisibility(View.VISIBLE);
                                }else if (position==3){
                                    imagePath[2]=photoPath;
                                    idcardimg3=photoPath;
                                    iv_people.setBackgroundDrawable(drawable);
                                    iv_people_add.setVisibility(View.INVISIBLE);
                                    iv_people_delet.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过去出现问题时调用
                            }
                        }).launch();
            } else if (requestCode == 2) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (!TextUtils.isEmpty(selectedImage.getAuthority())) {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
// 获取选择照片的数据视图
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
// 从数据视图中获取已选择图片的路径
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        Log.e("path",picturePath);
                        Luban.with(getActivity())
                                .load(getFileByUri(selectedImage,getActivity()))                     //传人要压缩的图片
                                //设定压缩档次，默认三挡
                                .setCompressListener(new OnCompressListener() { //设置回调

                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    }
                                    @Override
                                    public void onSuccess(File file) {
                                        // TODO 压缩成功后调用，返回压缩后的图片文件
                                        photoPath=file.toString();
                                        Log.e("qiniu","111111");
                                        Drawable drawable=Drawable.createFromPath(photoPath);
                                        if (position==1){
                                            imagePath[0]=photoPath;
                                            idcardimg1=photoPath;
                                            iv_zheng.setBackgroundDrawable(drawable);
                                            iv_zheng_add.setVisibility(View.INVISIBLE);
                                            iv_zheng_delet.setVisibility(View.VISIBLE);
                                        }else if (position==2){
                                            imagePath[1]=photoPath;
                                            idcardimg2=photoPath;
                                            iv_fan.setBackgroundDrawable(drawable);
                                            iv_fan_add.setVisibility(View.INVISIBLE);
                                            iv_fan_delet.setVisibility(View.VISIBLE);
                                        }else if (position==3){
                                            imagePath[2]=photoPath;
                                            idcardimg3=photoPath;
                                            iv_people.setBackgroundDrawable(drawable);
                                            iv_people_add.setVisibility(View.INVISIBLE);
                                            iv_people_delet.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // TODO 当压缩过去出现问题时调用
                                    }
                                }).launch();

//                        sendPicByUri(selectedImage);
                    }
                }
            }
        }
    }

    public void ShowMenu(View view) {
        //创建弹出式菜单对象（最低版本11）
        //第二个参数是绑定的那个view
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.popomenu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_camera:
                        showTakePicture();
                        break;
                    case R.id.menu_photo:
                        Intent intent;
                        if (Build.VERSION.SDK_INT < 19) {
                            intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");

                        } else {
                            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        }
                        startActivityForResult(intent, 2);
                        break;
                    default:
                        break;
                }
                return false;
            }

        });
        //显示(这一行代码不要忘记了)
        popup.show();
    }
    private void showTakePicture() {
        PermissionGen.with(getActivity())
                .addRequestCode(10)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .request();
        startTake();
    }
    private void startTake() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机应用
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //创建临时图片文件
            try {
                cameraFile = PictureUtils.createPublicImageFile();
                photoPath = cameraFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置Action为拍照
            if (cameraFile != null) {
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //这里加入flag
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() +".fileproviderdzhang", cameraFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }
    public void test() {
        // 要申请的权限 数组 可以同时申请多个权限
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            //如果超过6.0才需要动态权限，否则不需要动态权限
            //如果同时申请多个权限，可以for循环遍历
            for (int i = 0; i < permissions.length; i++) {
                int check = ContextCompat.checkSelfPermission(getActivity(), permissions[i]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (check == PackageManager.PERMISSION_GRANTED) {
                    //写入你需要权限才能使用的方法
//                run();
                } else {
                    //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }

        } else {
            //写入你需要权限才能使用的方法
//            run();
        }
    }
    public static File getFileByUri(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;
        private final int  i;

        public getImageCacheAsyncTask(Context context,int i) {
            this.context = context;
            this.i=i;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            imagePath[i]=result.getPath();
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            Log.e("path", path);
        }
    }
}
