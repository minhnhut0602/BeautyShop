package mimishop.yanji.com.mimishop.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KCJ on 3/24/2015.
 */
public class ServerAPICall {

    public final static int REQUEST_GET  = 0;
    public final static int REQUEST_POST  = 1;
    public final static int REQUEST_PUT  = 2;
    public final static int REQUEST_DELETE  = 3;
    public static final String ERR_INVALID_FORMAT_DATA = "자료형식이 틀립니다.";
    public static final String ERR_NO_NETWORK = "네트워크에 연결할수 없습니다.\n" + "다시 시도하여 주십시오.";
    private static ServerAPICall appInstance;
    public static ServerAPICall getInstance() {
        if (appInstance == null) {
            appInstance = new ServerAPICall();

        }
        return appInstance;
    }

    private  SessionManager sessionManager = null;

    public boolean uploadFile(String url, String file_var_name, File file, final JsonHttpResponseHandler handler ) {

        RequestParams  w_reqParams = new RequestParams();
        try {
            w_reqParams.put(file_var_name, file);
        } catch (FileNotFoundException e) {
            return false;
        }

        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("app-key",Common.mKey);
        client.addHeader("device-id",Common.mDeviceId);
        client.addHeader("device-model",Common.mDeviceModel);
        client.addHeader("os-type",Common.mOsType);
        client.addHeader("os-version",Common.mOsVersion);
        client.addHeader("app-version",Common.mAppVersion);
        client.addHeader("app-market",Common.mMarket);

        client.post(url, w_reqParams, handler);

        return  true;
    }

    public void callPOST(String url, onReponseListener listener, onErrorListener errorListener, Map<String, String> postParams) {
        _callServerAPI(Request.Method.POST, url, listener, errorListener, postParams);
    }

    public void callGET(String url, onReponseListener listener, onErrorListener errorListener) {
        _callServerAPI(Request.Method.GET, url, listener, errorListener, null);
    }

    private void _callServerAPI (int method, final String url, final onReponseListener listener, final onErrorListener errorListener, final Map<String, String> postParams) {
        StringRequest strReq = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        s = s.replace(s.substring(0,s.indexOf('{')-1),"");
                        try {
                            if(url.contains(ServerAPIPath.API_BASE_URL) == false) {
                                listener.onResponse(s);
                                return;
                            }

                            JSONObject response = new JSONObject(s);

                            String code = response.getString("result_code");
                            String msg = response.getString("result_msg");


                            Logger.d(url, "onResponse : " + response);
                            Logger.d(url, "onResponse - code : " + code);
                            Logger.d(url, "onResponse - msg : " + msg);

                            if (!code.equals("0")) {
                                errorListener.onErrorResponse(msg);
                                return;
                            }

                            Object result = response.get("result_data");
                            if(result.toString().equals("null") == true) {
                                listener.onResponse(null);
                            }
                            else {
                                listener.onResponse(result);
                            }
                        }
                        catch (JSONException e) {
                            errorListener.onErrorResponse(ERR_INVALID_FORMAT_DATA);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        errorListener.onErrorResponse(ERR_NO_NETWORK);
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {

                HashMap<String, String> headers = new HashMap<String, String>();

                SessionManager.getInstance().initHeader(headers);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if(postParams != null) {
                    params = postParams;
                }
                return params;
            }
        };

        Utility.setDelayPolicy(strReq);
        AppController.getInstance().addToRequestQueue(strReq, url);
    }
}

