package com.example.qqlaobing.Umoney.view.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qqlaobing.Umoney.R;
import com.example.qqlaobing.Umoney.been.ContactBeen;
import com.example.qqlaobing.Umoney.net.NetRequestBusinessDefine;
import com.example.qqlaobing.Umoney.presenter.presenterImpl.ContactPresenterImpl;
import com.example.qqlaobing.Umoney.view.activity.ContactActivity;
import com.example.qqlaobing.Umoney.view.activity.RenZhengActivity;
import com.example.qqlaobing.Umoney.view.myview.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RenZhengContectFragment extends Fragment implements View.OnClickListener {
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    private EditText et_contactname,et_contactname2;
    private TextView et_contactphone,et_contactphone2,tv_finish;
    private ImageView tv_exit;
    private Spinner sp_choose2,sp_choose;
    //点击的哪个联系人
    private int choose=0;
    private String tel,relation1,relation2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case NetRequestBusinessDefine.K_CONTACT:
                    try {
                        if (msg.arg1 == 404) {
                            dialog.close();
                            return;
                        }
                        JSONObject response = (JSONObject) msg.obj;
                        String resultCode = response.getString("status");
                        if (resultCode.equals("1")) {
                            JSONObject results = response.getJSONObject("data");
                            dialog.close();
                            Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            //通过intent对象返回结果，必须要调用一个setResult方法，
                            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以
                            RenZhengActivity renZhengActivity=(RenZhengActivity)getActivity();
                            renZhengActivity.ChangeFragment(4);
                        } else {
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
    private List<ContactBeen> phoneDtos;
    private JSONArray jsonArray;
    private LoadingDialog dialog;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        initView();
        initData();
        return rootView;
    }
    private void initData() {
        ContactPresenterImpl contactPresenter=new ContactPresenterImpl(mHandler);
        contactPresenter.getData();

        et_contactphone.setOnClickListener(this);
        et_contactphone2.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        sp_choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_choose.setTextAlignment(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_choose2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_choose2.setTextAlignment(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                        0);//申请权限
            } else {//拥有当前权限

            }
        }
        et_contactname = (EditText) rootView.findViewById(R.id.et_contactname);
        et_contactphone = (TextView) rootView.findViewById(R.id.et_contactphone);
        et_contactname2 = (EditText) rootView.findViewById(R.id.et_contactname2);
        et_contactphone2 = (TextView) rootView.findViewById(R.id.et_contactphone2);
        tv_finish = (TextView) rootView.findViewById(R.id.tv_finish);
        tv_exit=(ImageView) rootView.findViewById(R.id.tv_exit);
        sp_choose2=(Spinner)rootView.findViewById(R.id.sp_choose2);
        sp_choose=(Spinner)rootView.findViewById(R.id.sp_choose);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_contactphone:
                getPhone();
                choose=1;
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), 0);
                break;
            case R.id.et_contactphone2:
                choose=2;
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), 0);
                break;
            case R.id.tv_exit:
                break;
            case R.id.tv_finish:
                if (et_contactphone.getText().toString().equals("")||et_contactname.getText().toString().equals("")||
                        et_contactname2.getText().toString().equals("")||et_contactphone2.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"信息填写不完整",Toast.LENGTH_LONG).show();
                }else {
                    dialog = new LoadingDialog(getActivity(), "上传中...");
                    dialog.show();
                    List<ContactBeen> list=new ArrayList<>();
                    ContactBeen contactBeen=new ContactBeen(et_contactname.getText().toString(),et_contactphone.getText().toString(),sp_choose.getSelectedItem().toString());
                    ContactBeen contactBeen2=new ContactBeen(et_contactname2.getText().toString(),et_contactphone2.getText().toString(),sp_choose2.getSelectedItem().toString());
                    list.add(contactBeen);
                    list.add(contactBeen2);
                    ContactPresenterImpl contactPresenter=new ContactPresenterImpl(mHandler,list,jsonArray);
                    contactPresenter.postData();
                }
                break;
            default:break;
        }

    }

    //获取所有联系人
    public List<ContactBeen> getPhone(){
        jsonArray = new JSONArray();

        phoneDtos = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor=cr.query(phoneUri,null,null,null,null);
        while (cursor.moveToNext()){

            //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name=cursor.getString(nameFieldColumnIndex);
            Log.e("contact",name.toString());
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            tel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactBeen phoneDto = new ContactBeen(name,tel);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name",name);
                jsonObject.put("telephone",tel);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("contact",tel);
            phoneDtos.add(phoneDto);
        }
        Log.e("contact", phoneDtos.toString());
        return phoneDtos;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(data==null) { return; }
                //处理返回的data,获取选择的联系人信息
                Uri uri=data.getData(); String[] contacts=getPhoneContacts(uri);
                if (choose==1){
                    et_contactname.setText(contacts[0]);
                    et_contactphone.setText(contacts[1]);
                }else if (choose==2){
                    et_contactname2.setText(contacts[0]);
                    et_contactphone2.setText(contacts[1]);
                }

                break;
            default:break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String[] getPhoneContacts(Uri uri){
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getActivity().getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0]=cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone != null){
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
