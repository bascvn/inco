package vn.com.basc.inco;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.database.INCODatabase;
import vn.com.basc.inco.model.Company;
import vn.com.basc.inco.model.User;

/**
 * Created by User on 22/05/2016.
 */
public class INCOApplication extends Application {

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
        public static INCOApplication sInstance;
        public static INCODatabase myDatabase;
        @Override
        public void onCreate() {
            super.onCreate();

            // initialize the singleton
            sInstance = this;
            myDatabase = new INCODatabase(this);
        }

        /**
         * @return ApplicationController singleton instance
         */
        public static synchronized INCOApplication getInstance() {
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
        private static String USER_CHECK_REMEMBER = "remember";
        private static String USER_REMEMBER = "user_remember";
        private static String PASS_REMEMBER = "pass_remember";
      //  private static String COMMAPY__REMEMBER = "compay_remember";

        public void setRemember(boolean remember){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(USER_CHECK_REMEMBER, remember);
            // Commit the edits!
            editor.commit();
        }
        public boolean getRemember() {
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            return settings.getBoolean(USER_CHECK_REMEMBER, false);
        }
        public void setPassWord(String pass){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PASS_REMEMBER, pass);
            // Commit the edits!
            editor.commit();
        }
    public void removeRemember(){
        SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(USER_REMEMBER).remove(PASS_REMEMBER).remove(COMPANY_ADDRESS).commit();
    }
    public String getPassWord(){
        SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
        return settings.getString(PASS_REMEMBER,"");
    }

    public void setEmail(String email){
        SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_REMEMBER, email);
        // Commit the edits!
        editor.commit();
    }
    public void removeToken(){
        Log.d("removeToken" ,"removeToken");
        SharedPreferences preferences = getSharedPreferences(INCO_USER_CONFIG, 0);
        preferences.edit().remove(TOKEN_ACCESS).commit();
    }
    public String getEmail(){
        SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
        return settings.getString(USER_REMEMBER,"");
    }
        public void saveCompanyAddress(String address){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(COMPANY_ADDRESS, address);
            // Commit the edits!
            editor.commit();
        }
        public void saveTokenAccess(String token){
            Log.d("saveUserInfo" ,token);
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(TOKEN_ACCESS, token);
            // Commit the edits!
            editor.commit();
        }
        public String  getTokenAccess(){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            String token = settings.getString(TOKEN_ACCESS,"");
            Log.d("saveUserInfo" ,token);
            return token;
        }

        public void saveUserInfo(String user){
            Log.d("saveUserInfo" ,user);
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_INFO, user);
            // Commit the edits!
            editor.commit();
        }
        public  void saveVersionBuild(String version){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("version", version);
            // Commit the edits!
            editor.commit();
        }
        public String getVersionBuild(){
            SharedPreferences settings = getSharedPreferences(INCO_USER_CONFIG, 0);
            Gson gson = new Gson();
            String version = settings.getString("version","0");
             return version;
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
        public String getUrlLogo(String logo){
            return Globals.PROTOCOL+Globals.LOGO_PATH+"/"+logo;
        }
        public String getUrlApi(String api){
            return Globals.NEW_PROTOCOL+getCompanyAddress()+"."+Globals.NEW_DOMAIN+"/"+getCompanyAddress()+api;
        }
        public String getUrlGetCompany(){
            return Globals.PROTOCOL+Globals.API_GET_COMPANY;
        }
        public String getUrlGetCompanyStatus(){
            return Globals.PROTOCOL+Globals.API_GET_COMPANY_STATUS;
        }
        public String getAvatarUrl(String avatar){
            return Globals.NEW_PROTOCOL+getCompanyAddress()+"."+Globals.NEW_DOMAIN+"/"+getCompanyAddress()+"/"+Globals.AVATAR_PATH+"/"+avatar;
        }
    public String getVersion(){
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String verison = String.format(getResources().getString(R.string.version),versionName+"("+ String.valueOf(versionCode)+")");
        return verison;
    }
    public void checkCompanyStatus(final String ClientCode, final Activity activity){

        //  RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        final String url = this.getUrlGetCompanyStatus();
        Log.d("response", "url:" + url);
        Log.d("response", "ClientCode:" + ClientCode);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (INCOResponse.isError(obj.getString(INCOResponse.STATUS_TAG))) {
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.alert)
                                .setMessage(R.string.company_not_exits)
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton(android.R.string.yes, null).show();
                                //.setNegativeButton(android.R.string.no, null).show();
                    } else {
                        String productStr = obj.getString(INCOResponse.DATA_TAG);
                        Log.d("kienbk1910", productStr);
                        Gson gson = new Gson();
                        final Company data = (Company) gson.fromJson(productStr,
                                new TypeToken<Company>() {
                                }.getType());
                        if(data.getStatus() != null && data.getStatus().equals("0")){
                            new AlertDialog.Builder(activity)
                                    .setTitle(R.string.alert)
                                    .setMessage(data.getMsg())
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            INCOApplication.getInstance().myDatabase.cleanDatabase();

                                            INCOApplication.getInstance().removeToken();
                                            Intent intent = new Intent(activity, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }else {
                            if (data.getStatus() != null && !data.getStatus().equals("1")) {
                                new AlertDialog.Builder(activity)
                                        .setTitle(R.string.alert)
                                        .setMessage(data.getMsg())
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setPositiveButton(android.R.string.yes, null)
                                        .show();
                            } else {
                                if(data.getBuildNumber() == null){
                                    return;
                                }
                                int version = Integer.parseInt(data.getBuildNumber());
                                if(version > BuildConfig.VERSION_CODE && !INCOApplication.getInstance().getVersionBuild().equals(data.getBuildNumber())) {
                                    String verison = String.format(getResources().getString(R.string.have_new_version),data.getAndroid_ver());

                                    new AlertDialog.Builder(activity)
                                            .setTitle(R.string.alert)
                                            .setMessage(verison )
                                            .setIcon(R.mipmap.ic_launcher)
                                            .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    try {
                                                        Intent myIntent =new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl_android()));
                                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(myIntent);
                                                    }catch (Exception e){

                                                    }
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    INCOApplication.getInstance().saveVersionBuild(data.getBuildNumber());

                                                }
                                            }).show();
                                }
                            }
                        }

                    }
                }catch (JSONException e){
                    Log.e("login", "JSONException:"+e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              /*  NetworkResponse response = error.networkResponse;
                error.hashCode();
                Log.d("response",String.valueOf(response.statusCode));
                if(response != null && response.data != null){
                    Log.d("response",String.valueOf(response.statusCode));
                    switch(response.statusCode){

                        case 400:

                            break;
                    }
                    //Additional cases
                }*/

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Globals.CLIENT_CODE,ClientCode);

                return params;
            }
        };
       /* int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);*/
        // queue.add(request);

        INCOApplication.getInstance().addToRequestQueue(request);
    }
}
