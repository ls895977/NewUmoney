package com.example.qqlaobing.Umoney.net;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhanglizhi on 2018/7/24.
 */

//如果返回头中没有Charset，默认UTF-8
public class CharsetJsonRequest extends JsonObjectRequest {

    public CharsetJsonRequest(String url, JSONObject jsonRequest,
                              Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public CharsetJsonRequest(int method, String url, JSONObject jsonRequest,
                              Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
//            Log.e("net--volley0",response.headers.toString());
            String jsonString = new String(response.data, "UTF-8");
            Log.e("net--volley",jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}

