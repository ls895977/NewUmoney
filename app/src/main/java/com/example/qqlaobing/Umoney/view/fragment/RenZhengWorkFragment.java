package com.example.qqlaobing.Umoney.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.WorkBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.WorkPresenterImpl;
import com.example.qqlaobing.Umoney.utils.Auth;
import com.example.qqlaobing.Umoney.utils.PictureUtils;
import com.example.qqlaobing.Umoney.view.activity.RenZhengActivity;
import com.example.qqlaobing.Umoney.view.activity.WorkCheckActivity;
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

public class RenZhengWorkFragment extends Fragment implements View.OnClickListener {
    private String photoPath;
    protected File cameraFile;
    private EditText et_profession,et_work_position,et_unit_name,et_unit_phone,et_loan_use;
    private CheckBox cb_marray,cb_dog;
    private ImageView iv_work,iv_work_add,iv_work_delet,tv_exit;
    private TextView tv_finish,et_unit_location;
    private WorkBeen workBeen;
    private String mar="";
    private KeyGenerator keyGen;
    private Recorder recorder;
    private UploadManager uploadManager;
    String accessKey = "XwR_q3yAUhRxGPQaQRqJGZPP_joMJ40sVf7EgUy1";
    String secretKey = "YLsDZ4W_YnHMfYiY9Y1L2SyGXZRp5OVc0yYEZOFY";
    String bucket = "imgserver";
    String photoKey;
    private String upToken;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_WORK:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
//                        JSONObject results = response.getJSONObject("data");
                        if (resultCode.equals("1")) {
                            dialog.close();
                            Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                            RenZhengActivity renZhengActivity=(RenZhengActivity)getActivity();
                            renZhengActivity.ChangeFragment(6);
                        }else {
                            Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_LONG).show();
                            dialog.close();
                        }
                    } catch (JSONException e) {
                        dialog.close();
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }
    };
    private LoadingDialog dialog;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_find, container, false);
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
    private void initView() {
        et_profession=(EditText)rootView.findViewById(R.id.et_profession);
        et_work_position=(EditText)rootView.findViewById(R.id.et_work_position);
        et_unit_name=(EditText)rootView.findViewById(R.id.et_unit_name);
        et_unit_phone=(EditText)rootView.findViewById(R.id.et_unit_phone);
        et_unit_location=(TextView) rootView.findViewById(R.id.et_unit_location);
        et_loan_use=(EditText)rootView.findViewById(R.id.et_loan_use);
        cb_marray=(CheckBox)rootView.findViewById(R.id.cb_marray);
        cb_dog=(CheckBox)rootView.findViewById(R.id.cb_dog);
        iv_work=(ImageView)rootView.findViewById(R.id.iv_work);
        iv_work_add=(ImageView)rootView.findViewById(R.id.iv_work_add);
        iv_work_delet=(ImageView)rootView.findViewById(R.id.iv_work_delet);
        tv_finish=(TextView) rootView.findViewById(R.id.tv_finish);
        tv_exit=(ImageView) rootView.findViewById(R.id.tv_exit);


    }
    private void initData() {
        workBeen=new WorkBeen();
        Auth auth = Auth.create(accessKey, secretKey);
        upToken = auth.uploadToken(bucket);
        System.out.println(upToken);

        tv_exit.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        cb_marray.setOnClickListener(this);
        cb_dog.setOnClickListener(this);
        iv_work.setOnClickListener(this);
        iv_work_add.setOnClickListener(this);
        iv_work_delet.setOnClickListener(this);
//        et_unit_location.setOnClickListener(this);

        cb_dog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cb_marray.setChecked(false);
                    mar="未婚";
                }else {
                    cb_marray.setChecked(true);
                }
            }
        });
        cb_marray.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cb_dog.setChecked(false);
                    mar="已婚";
                }else {
                    cb_dog.setChecked(true);
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_finish:
                if (et_profession.getText().toString().equals("")||
                        et_unit_location.getText().toString().equals("")||et_unit_name.getText().toString().equals("")||
                        et_unit_phone.getText().toString().equals("")||et_work_position.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"信息填写不完整",Toast.LENGTH_SHORT).show();
                    break;
                }
//                else if (mar.equals("")){
//                    Toast.makeText(this,"请选择婚姻状况",Toast.LENGTH_SHORT).show();
//                    break;
//                }
                else {
                    workBeen.setName(et_unit_name.getText().toString());
                    workBeen.setAddress(et_unit_location.getText().toString());
                    workBeen.setTelephone(et_unit_phone.getText().toString());
                    workBeen.setUse("");
                    workBeen.setWork(et_work_position.getText().toString());
                    workBeen.setTrade(et_profession.getText().toString());
                    workBeen.setMarriage("");
                }
                if (iv_work.getBackground()!=null){
                    QiNIu();
                }else {
                    workBeen.setWorkimg("");
                    dialog = new LoadingDialog(getActivity(), "上传中...");
                    dialog.show();
                    WorkPresenterImpl workPresenter=new WorkPresenterImpl(mHandler,workBeen);
                    workPresenter.postData();
                }
                break;
            case R.id.iv_work_add:
                test();
                ShowMenu(iv_work_add);
                break;
            case R.id.iv_work_delet:
                iv_work.setBackgroundDrawable(null);
                iv_work_add.setVisibility(View.VISIBLE);
                iv_work_delet.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_exit:
                break;
           /* case R.id.et_unit_location:
                selectAddress();
                break;*/
            default:break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) { // capture new image
                if (photoPath != null) {
                    Log.e("eeeee", cameraFile.toString());
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
                                    Drawable drawable = Drawable.createFromPath(photoPath);
                                    iv_work.setBackgroundDrawable(drawable);
                                    iv_work_add.setVisibility(View.INVISIBLE);
                                    iv_work_delet.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过去出现问题时调用
                                }
                            }).launch();
                }
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
                                        Drawable drawable=Drawable.createFromPath(photoPath);
                                        iv_work.setBackgroundDrawable(drawable);
                                        iv_work_add.setVisibility(View.INVISIBLE);
                                        iv_work_delet.setVisibility(View.VISIBLE);
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

    public void QiNIu(){
        Log.i("qiniu","start");
        String token = upToken;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_"+ sdf.format(new Date())+".jpg";
        dialog = new LoadingDialog(getActivity(), "上传中...");
        dialog.show();
        uploadManager.put(photoPath, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            try {
                                photoKey=res.get("key").toString();
                                workBeen.setWorkimg(photoKey);
                                WorkPresenterImpl workPresenter=new WorkPresenterImpl(mHandler,workBeen);
                                workPresenter.postData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("qiniu", "成功");
                            Log.i("qiniu", res.toString());
                        } else {
                            dialog.close();
                            Toast.makeText(getActivity(),"资料上传失败，请检查网络连接",Toast.LENGTH_LONG).show();
                            Log.i("qiniu", "错误");
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }
}
