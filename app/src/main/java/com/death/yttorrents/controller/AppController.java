package com.death.yttorrents.controller;

/**
 * Created by sidhantrajora on 07/07/16.
 */
import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SharedPreferences preferences = getSharedPreferences("APPCOUNTER", MODE_APPEND);
        if(!preferences.contains("ISFIRSTRUN"))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ISFIRSTRUN", true);
            editor.apply();
            editor.commit();
        }
    }

    /**
     * To get the instance of AppController
     * @return
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /**
     * Volley request queue
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * For adding request to queue
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * For adding request to queue (Overridden)
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * cancel the pending requests
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}