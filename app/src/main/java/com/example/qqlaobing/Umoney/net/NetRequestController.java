package com.example.qqlaobing.Umoney.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qqlaobing.Umoney.application.GeneralApplication;
import com.example.qqlaobing.Umoney.view.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class NetRequestController {

    private static NetRequestController mInstance = null;
    private final String BOUNDARY = "------" + UUID.randomUUID().toString(); // 随机生成边界值
    private final String NEW_LINE = "\r\n"; // 换行符
    private final String MULTIPART_FORM_DATA = "multipart/form-data"; // 数据类型
    private String charSet = "utf-8"; // 编码

    private Context mContext;
    private com.android.volley.RequestQueue mQueue = null;
    private Map<String, Handler> aHandlerMap = new HashMap<String, Handler>();
    private String username;

    private NetRequestController(Context aContext) {
        mContext = aContext;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public static NetRequestController getInstance(Context aContext) {
        if (mInstance == null) {
            mInstance = new NetRequestController(aContext);
        }
        return mInstance;
    }

    /**
     * 方 法 名: registHandler 功能描述: 注册接受服务器返回数据的接受者 参数说明:
     *
     * @param aHandler 消息接受者 返 回 值: void
     */
    public void registHandler(Handler aHandler) {
        if (null == aHandler) {
            return;
        }
        // 作为handlerMap的key
        String handlerName = aHandler.getClass().getName();

        // HashMap会自动判断存在或者不存在 所以不用判断是不是已经存在相同的key和value
        aHandlerMap.put(handlerName, aHandler);
    }

    /**
     * 方 法 名: UnRegistHandler 功能描述: 取消接受服务器返回的数据的消息 参数说明:
     *
     * @param aHandler 消息接受者 返 回 值: void
     */
    public void unRegistHandler(Handler aHandler) {
        if (null == aHandler) {
            return;
        }
        String handlerClassName = aHandler.getClass().getName();
        // 从map中删除 就不用发送message了
        aHandlerMap.remove(handlerClassName);
    }

    /**
     * 方 法 名: requestJsonObject 功能描述: 发起JSON格式的http post网络请求 参数说明:
     *
     * @param aHandlerClassName
     * @param aParams
     * @param aKey
     * @param aUrl              返 回 值: void
     */
    public void requestJsonObject(final String aHandlerClassName,
                                  final HashMap<String, Object> aParams, final int aKey, String aUrl) {

//        final String s_token = GlobalSettingParameter.K_TOKEN + "$" + System.currentTimeMillis();
        Log.e("xxxxx", NetRequestBusinessDefine.K_TOKEN+"----------------"+aUrl);
        try {
            final String des_token ="" ;//EncodeUtil.get3DES(s_token);
            if (aParams!=null){
                Log.e("hhhhhhhhhhh","--------------"+(new JSONObject(aParams)).toString());
            }

            JsonObjectRequest request = new CharsetJsonRequest(
                    aParams == null ? Request.Method.GET : Request.Method.POST, aUrl,
                    aParams == null ? null : new JSONObject(aParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject arg0) {
                            Log.e("zhongyingjie--0",arg0.toString());
                            Handler handlerMsg = aHandlerMap
                                    .get(aHandlerClassName);
                            if (null == handlerMsg) {
                                return;
                            }
                            Message msg = new Message();
                            msg.obj = arg0;
                            msg.what = aKey;
                            aParams.put("token",NetRequestBusinessDefine.K_TOKEN);
                            handlerMsg.sendMessage(msg);
                            if (aKey!=NetRequestBusinessDefine.K_HOME){
                                try {
                                    if (arg0.getString("status").equals("401")){
                                        Intent intent=new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    Log.e("zhongyingjie--1", arg0.toString());
                    Message sysMsg = GeneralApplication
                            .getInstance()
                            .getAppSystemHandler()
                            .obtainMessage(
                                    NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
                    GeneralApplication.getInstance()
                            .getAppSystemHandler().sendMessage(sysMsg);
                    Handler handlerMsg = aHandlerMap
                            .get(aHandlerClassName);
                    if (null == handlerMsg) {
                        return;
                    }
                    Message msg = new Message();
                    msg.arg1 = 404;
                    msg.obj = arg0;
                    msg.what = aKey;
                    SharedPreferences share=mContext.getSharedPreferences("umoney", Activity.MODE_PRIVATE);
                    if (share!=null){
                        username = share.getString("username","");
                    }

                    if (aKey!=NetRequestBusinessDefine.K_ERROR) {
                        handlerMsg.sendMessage(msg);
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("content", arg0.toString());
                        params.put("customer_id", username);
                        params.put("app", "android");
                        Log.e("ssssssssssaaaa", params.toString());
                        Log.e("ssssssssssaaaa", "错误已上传");
                        GeneralApplication
                                .getInstance()
                                .getNetRequestController()
                                .requestJsonObject(
                                        aHandlerClassName,
                                        params,
                                        NetRequestBusinessDefine.K_ERROR,
                                        NetRequestRULBuilder
                                                .buildRequestUrl(NetRequestBusinessDefine.K_ERROR));
                        GeneralApplication.getInstance().getNetRequestController()
                                .registHandler(aHandlerMap
                                        .get(aHandlerClassName));
                    }
                    Toast.makeText(mContext, "网络错误，请检查是否连接网络。",
                            Toast.LENGTH_SHORT).show();
                }
            })
            {
                /**
                 * 获取实体的方法，把参数拼接成表单提交的数据格式
                 *
                 * @return
                 * @throws AuthFailureError
                 */
                @Override
                public byte[] getBody(){
                    if (aParams == null || aParams.size() <= 0) {
                        return super.getBody();
                    }
                    // ------WebKitFormBoundarykR96Kta4gvMACHfq                 第一行
                    // Content-Disposition: form-data; name="login_username"    第二行
                    //                                                          第三行
                    // abcde                                                    第四行

                    // ------WebKitFormBoundarykR96Kta4gvMACHfq--               结束行

                    // 开始拼接数据
                    StringBuffer stringBuffer = new StringBuffer();
                    for (String key : aParams.keySet()) {
                        Object value = aParams.get(key);
                        stringBuffer.append("--" + BOUNDARY).append(NEW_LINE); // 第一行
                        stringBuffer.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(NEW_LINE); // 第二行
                        stringBuffer.append(NEW_LINE); // 第三行
                        stringBuffer.append(value).append(NEW_LINE); // 第四行
                    }
                    // 所有参数拼接完成，拼接结束行
                    stringBuffer.append("--" + BOUNDARY + "--").append(NEW_LINE);// 结束行
                    try {
                        Log.e("sssssssss",stringBuffer.toString());
                        return stringBuffer.toString().getBytes(charSet);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        // 使用默认的编码方式，Android为utf-8
                        return stringBuffer.toString().getBytes();
                    }
                }

                /**
                 * 该方法的作用：在 http 头部中声明内容类型为表单数据
                 *
                 * @return
                 */
                @Override
                public String getBodyContentType() {
                    // multipart/form-data; boundary=----WebKitFormBoundarykR96Kta4gvMACHfq
                    return MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY;
                }
                @Override
                public Map<String, String> getHeaders(){
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("token", NetRequestBusinessDefine.K_TOKEN);
                    headers.put("Connection", "close");
                    return headers;
                }
            };

            Log.e("hhhhhhhhhhh","--------------kkk");
            request.setRetryPolicy(new DefaultRetryPolicy(30000*1,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(true);
            request.setTag(aHandlerClassName);
            mQueue.add(request);
        } catch (Exception e) {
            Log.e("zhonglz--2", e.toString());
                Message sysMsg = GeneralApplication.getInstance()
                        .getAppSystemHandler()
                        .obtainMessage(NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
                GeneralApplication.getInstance().getAppSystemHandler()
                        .sendMessage(sysMsg);
                this.mQueue.cancelAll(aHandlerClassName);
                e.printStackTrace();
                Handler handlerMsg = aHandlerMap.get(aHandlerClassName);
                if (null == handlerMsg) {

                return;
            }
            Message msg = new Message();
            msg.arg1 = 404;
            msg.obj = e.getMessage();
            msg.what = aKey;
            handlerMsg.sendMessage(msg);
        }
    }


    /**
     * 方 法 名: requestJsonObject 功能描述: 发起JSON格式的http post网络请求 参数说明:
     *
     * @param aHandlerClassName
     * @param aParams
     * @param aKey
     * @param aUrl              返 回 值: void
     */
    public void requestJsonObjectPost(final String aHandlerClassName,
                                  HashMap<String, Object> aParams, final int aKey, String aUrl) {
        String s_token = "";

//        final String s_token = GlobalSettingParameter.K_TOKEN + "$" + System.currentTimeMillis();

        Log.e("xxxxx--post", s_token+"----------------"+aUrl);
        try {
            final String des_token = "EncodeUtil.get3DES(s_token)";
            Log.e("bbbbbbbbbbbbb",des_token);
            Log.e("hhhhhhhhhhh","--------------"+(new JSONObject(aParams)).toString());
            JsonObjectRequest request = new JsonObjectRequest(
                    aParams == null ? Request.Method.POST : Request.Method.POST, aUrl,
                    aParams == null ? null : new JSONObject(aParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject arg0) {
                            Log.e("zhongyingjie--post--0",arg0.toString());
                            Handler handlerMsg = aHandlerMap
                                    .get(aHandlerClassName);
                            if (null == handlerMsg) {
                                return;
                            }
                            Message msg = new Message();
                            msg.obj = arg0;
                            msg.what = aKey;
                            handlerMsg.sendMessage(msg);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    Log.e("zhongyingjie--post--1", arg0.toString());
                    Message sysMsg = GeneralApplication
                            .getInstance()
                            .getAppSystemHandler()
                            .obtainMessage(
                                    NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
                    GeneralApplication.getInstance()
                            .getAppSystemHandler().sendMessage(sysMsg);
                    Handler handlerMsg = aHandlerMap
                            .get(aHandlerClassName);
                    if (null == handlerMsg) {
                        return;
                    }
                    Message msg = new Message();
                    msg.arg1 = 404;
                    msg.obj = arg0;
                    msg.what = aKey;
                    handlerMsg.sendMessage(msg);
                    Toast.makeText(mContext, "网络错误，请检查是否连接网络。",
                            Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type",
                            "application/json; charset=utf-8");
                    headers.put("ContentEncoding", "utf-8");
                    headers.put("Accept-Encoding", "gzip");
                    headers.put("AccessCode",des_token);

                    return headers;
                }
            };
            Log.e("hhhhhhhhhhh","--------------kkk");
            request.setRetryPolicy(new DefaultRetryPolicy(60000 * 3,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(true);
            request.setTag(aHandlerClassName);
            mQueue.add(request);
        } catch (Exception e) {
            Log.e("zhongyingjie--post--2", e.toString());
            Message sysMsg = GeneralApplication.getInstance()
                    .getAppSystemHandler()
                    .obtainMessage(NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
            GeneralApplication.getInstance().getAppSystemHandler()
                    .sendMessage(sysMsg);
            this.mQueue.cancelAll(aHandlerClassName);
            e.printStackTrace();
            Handler handlerMsg = aHandlerMap.get(aHandlerClassName);
            if (null == handlerMsg) {

                return;
            }
            Message msg = new Message();
            msg.arg1 = 404;
            msg.obj = e.getMessage();
            msg.what = aKey;
            handlerMsg.sendMessage(msg);
        }
    }

    /**
     * 方 法 名: requestJsonObject 功能描述: 发起JSON格式的http post网络请求 参数说明:
     *
     * @param aHandlerClassName
     * @param aParams
     * @param aKey
     * @param aUrl              返 回 值: void
     */
    public void requestGetJsonObject(final String aHandlerClassName,
                                     HashMap<String, Object> aParams, final int aKey, String aUrl) {

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET , aUrl,
                    aParams == null ? null : new JSONObject(aParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject arg0) {
                            Handler handlerMsg = aHandlerMap
                                    .get(aHandlerClassName);
                            if (null == handlerMsg) {
                                return;
                            }

                            Message msg = new Message();
                            msg.obj = arg0;
                            msg.what = aKey;
                            handlerMsg.sendMessage(msg);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    Log.e("shiwenliang", arg0.toString());
                    Message sysMsg = GeneralApplication
                            .getInstance()
                            .getAppSystemHandler()
                            .obtainMessage(
                                    NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
                    GeneralApplication.getInstance()
                            .getAppSystemHandler().sendMessage(sysMsg);
                    Handler handlerMsg = aHandlerMap
                            .get(aHandlerClassName);
                    if (null == handlerMsg) {
                        return;
                    }
                    Message msg = new Message();
                    msg.arg1 = 404;
                    msg.obj = arg0;
                    msg.what = aKey;
                    handlerMsg.sendMessage(msg);
                    Toast.makeText(mContext, "网络错误，请检查是否连接网络。",
                            Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type",
                            "application/json; charset=utf-8");
                    headers.put("ContentEncoding", "utf-8");
                    headers.put("Accept-Encoding", "gzip");
//                    headers.put("User-Agent", ShareUtile.getStringSpParams(GeneralApplication.getInstance(), NetRequestBusinessDefine.PHONE_V, NetRequestBusinessDefine.PHONE_V));
                    headers.put("token",  NetRequestBusinessDefine.K_TOKEN);
                    return headers;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(60000 * 3,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(true);
            request.setTag(aHandlerClassName);
            mQueue.add(request);


        } catch (Exception e) {
            Message sysMsg = GeneralApplication.getInstance()
                    .getAppSystemHandler()
                    .obtainMessage(NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
            GeneralApplication.getInstance().getAppSystemHandler()
                    .sendMessage(sysMsg);
            this.mQueue.cancelAll(aHandlerClassName);
            e.printStackTrace();
            Handler handlerMsg = aHandlerMap.get(aHandlerClassName);
            if (null == handlerMsg) {

                return;
            }
            Message msg = new Message();
            msg.arg1 = 404;
            msg.obj = e.getMessage();
            msg.what = aKey;
            handlerMsg.sendMessage(msg);
        }

    }

    /**
     * 方 法 名：requestForStringGet 功能描述：用get方法发送请求 参数说明：@param aHandlerClassName
     * 参数说明：@param aKey 参数说明：@param url 返 回 值：void
     */
    public void requestForStringGet(final String aHandlerClassName,
                                    final int aKey, String url) {
        try {
            StringRequest request = new StringRequest(url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String arg0) {
                            Handler handlerMsg = aHandlerMap
                                    .get(aHandlerClassName);
                            if (null == handlerMsg) {
                                return;
                            }
                            Message msg = new Message();
                            msg.obj = formatStringToJsonObject(arg0);
                            msg.what = aKey;
                            handlerMsg.sendMessage(msg);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    Log.e("shiwenliang", arg0.toString());
                    Message sysMsg = GeneralApplication
                            .getInstance()
                            .getAppSystemHandler()
                            .obtainMessage(
                                    NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
                    GeneralApplication.getInstance()
                            .getAppSystemHandler().sendMessage(sysMsg);
                    Handler handlerMsg = aHandlerMap
                            .get(aHandlerClassName);
                    if (null == handlerMsg) {
                        return;
                    }
                    Message msg = new Message();
                    msg.arg1 = 404;
                    msg.obj = arg0;
                    msg.what = aKey;
                    handlerMsg.sendMessage(msg);
                    Toast.makeText(mContext, "网络错误，请检查是否连接网络。",
                            Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type",
                            "application/json; charset=utf-8");
                    headers.put("ContentEncoding", "utf-8");
                    headers.put("Accept-Encoding", "gzip");
//                    headers.put("User-Agent", ShareUtile.getStringSpParams(GeneralApplication.getInstance(), NetRequestBusinessDefine.PHONE_V, NetRequestBusinessDefine.PHONE_V));
                    headers.put("token",  NetRequestBusinessDefine.K_TOKEN);
                    return headers;
                }
            };

            request.setShouldCache(true);
            request.setTag(aHandlerClassName);
            mQueue.add(request);
        } catch (Exception e) {
            Message sysMsg = GeneralApplication.getInstance()
                    .getAppSystemHandler()
                    .obtainMessage(NetRequestBusinessDefine.K_FINAL_TYPE_GENERAL);
            GeneralApplication.getInstance().getAppSystemHandler()
                    .sendMessage(sysMsg);
            this.mQueue.cancelAll(aHandlerClassName);
            e.printStackTrace();
            Handler handlerMsg = aHandlerMap.get(aHandlerClassName);
            if (null == handlerMsg) {
                return;
            }
            Message msg = new Message();
            msg.arg1 = 404;
            msg.obj = e.getMessage();
            msg.what = aKey;
            handlerMsg.sendMessage(msg);
        }
    }




    public JSONObject formatStringToJsonObject(String response) {
        try {
            if (response != null && response.length() > 0) {
                int startIndex = response.indexOf('{');
                int latestIndex = response.lastIndexOf('}');
                String resp = response.substring(startIndex, latestIndex + 1);
                return new JSONObject(resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void requestForImageStringGet(final String aHandlerClassName,
                                         final int aKey, String url) {
        try {
            StringRequest request = new StringRequest(url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String arg0) {
                            Handler handlerMsg = aHandlerMap
                                    .get(aHandlerClassName);
                            if (null == handlerMsg) {
                                return;
                            }
                            Message msg = new Message();
                            msg.obj = arg0;
                            msg.what = aKey;
                            handlerMsg.sendMessage(msg);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    Toast.makeText(mContext, "网络错误，请检查是否连接网络。",Toast.LENGTH_LONG).show();
                }
            });
            request.setShouldCache(true);
            request.setTag(aHandlerClassName);
            mQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方 法 名: cancelRequest 功能描述: 取消网络请求 参数说明: 网络请求的tag 返 回 值: void
     */
    public void cancelRequest(final String aHandlerClassName) {
        try {
            this.mQueue.cancelAll(aHandlerClassName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方 法 名: reqeustMultipart 功能描述: 文件图片上传 参数说明:
     *
     * @param aHandlerClassName 接受消息的handle
     * @param url               请求的Url
     * @param stringParams      文本参数
     * @param fileParams        单文件参数
     * @param aKey              请求接口的索引 返 回 值: void
     */
    public void reqeustMultipart(final String aHandlerClassName, String url,
                                 HashMap<String, Object> stringParams,
                                 HashMap<String, Object> fileParams, final int aKey) {

        MultipartRequest multipartRequest = new MultipartRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Handler handlerMsg = aHandlerMap.get(aHandlerClassName);
                        if (null == handlerMsg) {
                            return;
                        }
                        Message msg = new Message();
                        msg.obj = response;
                        msg.what = aKey;
                        handlerMsg.sendMessage(msg);
                    }
                });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 添加header
        multipartRequest.addHeader("token", NetRequestBusinessDefine.K_TOKEN);

        // 通过MultipartEntity来设置参数
        MultipartEntity multi = multipartRequest.getMultiPartEntity();
        // 文本参数
        if (stringParams != null) {
            Iterator<Map.Entry<String, Object>> iterator = stringParams
                    .entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                multi.addStringPart(entry.getKey(),
                        String.valueOf(entry.getValue()));
            }
        }
        // 单个文件的
        if (fileParams != null) {
            Iterator<Map.Entry<String, Object>> iterator = fileParams
                    .entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (String.valueOf(entry.getValue()) != null
                        && String.valueOf(entry.getValue()).length() > 0) {
                    multi.addFilePart(entry.getKey(),
                            new File(String.valueOf(entry.getValue())));
                }
            }
        }
        // 将请求添加到队列中
        mQueue.add(multipartRequest);
    }
}
