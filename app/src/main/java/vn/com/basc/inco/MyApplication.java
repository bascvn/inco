package vn.com.basc.inco;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.model.User;

/**
 * Created by User on 22/05/2016.
 */
public class MyApplication extends Application {

        /**
         * Log or request TAG
         */
        public static final String TAG = "VolleyPatterns";

        /**
         * Global request queue for Volley
         */
        private RequestQueue mRequestQueue;

        /**
         * A singleton instance of the application class for easy access in other places
         */
        public static MyApplication sInstance;

        @Override
        public void onCreate() {
            super.onCreate();

            // initialize the singleton
            sInstance = this;
        }

        /**
         * @return ApplicationController singleton instance
         */
        public static synchronized MyApplication getInstance() {
            return sInstance;
        }

        /**
         * @return The Volley Request queue, the queue will be created if it is null
         */
        public RequestQueue getRequestQueue() {
            // lazy initialize the request queue, the queue instance will be
            // created when it is accessed for the first time
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }

            return mRequestQueue;
        }

        /**
         * Adds the specified request to the global queue, if tag is specified
         * then it is used else Default TAG is used.
         *
         * @param req
         * @param tag
         */
        public <T> void addToRequestQueue(Request<T> req, String tag) {
            // set the default tag if tag is empty
            req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

            VolleyLog.d("Adding request to queue: %s", req.getUrl());

            getRequestQueue().add(req);
        }

        /**
         * Adds the specified request to the global queue using the Default TAG.
         *
         * @param req
         * @param tag
         */
        public <T> void addToRequestQueue(Request<T> req) {
            // set the default tag if tag is empty
            req.setTag(TAG);

            getRequestQueue().add(req);
        }

        /**
         * Cancels all pending requests by the specified TAG, it is important
         * to specify a TAG so that the pending/ongoing requests can be cancelled.
         *
         * @param tag
         */
        public void cancelPendingRequests(Object tag) {
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(tag);
            }
        }

        // for save file config
        private static String INCO_USER_CONFIG = "inco_user_config";
        private static String COMPANY_ADDRESS  = "compay_address";
        private static String TOKEN_ACCESS = "token";
         private static String USER_INFO   = "user";

        public void saveCompanyAddress(String address){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(COMPANY_ADDRESS, address);
            // Commit the edits!
            editor.commit();
        }
        public void saveTokenAccess(String token){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(TOKEN_ACCESS, token);
            // Commit the edits!
            editor.commit();
        }
        public String  getTokenAccess(){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            return settings.getString(TOKEN_ACCESS,"");
        }

        public void saveUserInfo(String user){
            Log.d("saveUserInfo" ,user);
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_INFO, user);
            // Commit the edits!
            editor.commit();
        }
        public User getUserInfo(){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            Gson gson = new Gson();
            String userInfo = settings.getString(USER_INFO,"");
            Log.d("getUserInfo" ,userInfo);
            User user = gson.fromJson(userInfo,User.class);
            return user;
        }
        public String getCompanyAddress(){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            return settings.getString(COMPANY_ADDRESS,"");
        }
        public String getUrlApi(String api){
            return Globals.PROTOCOL+getCompanyAddress()+"/"+api;
        }
        public String getAvatarUrl(String avatar){
            return Globals.PROTOCOL+getCompanyAddress()+"/"+Globals.AVATAR_PATH+"/"+avatar;
        }
}
