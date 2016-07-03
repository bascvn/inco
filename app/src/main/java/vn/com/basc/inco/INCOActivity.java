package vn.com.basc.inco;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.model.UploadFile;
import vn.com.basc.inco.view.BadgeDrawable;

/**
 * Created by SONY on 6/16/2016.
 */
public class INCOActivity extends AppCompatActivity {
    private ProgressDialog progressBar = null;
    public  void showProgressDialog(String title){
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage(title);
        progressBar.setIndeterminate(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
    public void hideProgressDialog(){
        progressBar.dismiss();
    }
    public  boolean checkUploadingFile( List<UploadFile> fileList){
        for (int i = 0;i <fileList.size();i++){
            if(fileList.get(i).getStatus() ==1){
                return  true;
            }
        }
        return false;
    }
    public  void setBadgeCount(Context context, LayerDrawable icon, int count) {
        BadgeDrawable badge; // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge); //getting the layer 2
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        }
        else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(String.valueOf(count));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);

    }
    LayerDrawable icon =null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("kienbk1910","Have onCreateOptionsMenu");
        MenuItem itemCart = menu.findItem(R.id.action_notificaions);
        if(itemCart != null) {
             icon = (LayerDrawable) itemCart.getIcon();
// Update LayerDrawable's BadgeDrawable
            Log.d("kienbk1910","Have getIcon");
            setBadgeCount(INCOActivity.this,icon,INCOApplication.getInstance().myDatabase.getNumberPushUnRead());
        }
        return super.onCreateOptionsMenu(menu);
    }
    static  int count  = 0;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("kienbk1910","Have mess");
                // checking for type intent filter
               if (intent.getAction().equals(Globals.PUSH_NOTIFICATION)) {
                    // new push notification is received
                   Log.d("kienbk1910","Have PUSH_NOTIFICATION");

                    if(icon!= null){
                        Log.d("kienbk1910","Have setBadgeCount");
                        invalidateOptionsMenu();
                    }
                }
            }
        };
        Log.d("kienbk1910","Have onCreate");
    }
    @Override
    protected void onPause() {
        Log.d("kienbk1910","Have onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        Log.d("kienbk1910","Have onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Globals.PUSH_NOTIFICATION));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_notificaions ){
            INCOApplication.getInstance().myDatabase.readAlLPushes();
            invalidateOptionsMenu();
            Intent intent = new Intent(this, PushActivity.class);
            startActivityForResult(intent,1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
