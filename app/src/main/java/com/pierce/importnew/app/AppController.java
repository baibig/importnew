package com.pierce.importnew.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.litepal.LitePalApplication;

/**
 * Author: pierce
 * Date: 2015/8/20
 */
public class AppController extends LitePalApplication{
    private final String TAG="appcontroller";
    private static AppController mInstance;

    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized AppController getInstance(){return mInstance;}

    public RequestQueue getRequestQueue(){
        if (mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T>  request,String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequest(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelAllRequest(String tag){
        if (mRequestQueue!=null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
