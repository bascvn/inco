package vn.com.basc.inco;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.TaskItem;

public class AddCommentActivity extends AppCompatActivity {
    private String project_id;
    private String id;
    private int type;
    private EditText mTxtComments;
    Toolbar toolbar;
    private ProgressBar mProgressBar;
    private boolean isSend;
    MenuItem send;
    Lock lock;

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.id = getIntent().getStringExtra(Globals.ID_EXTRA);
        this.type = getIntent().getIntExtra(Globals.COMPONENT_EXTRA, ComponentType.TASK);
        this.project_id = getIntent().getStringExtra(Globals.PROJECT_ID_EXTRA);
        mTxtComments = (EditText) findViewById(R.id.txtComments);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        lock = new ReentrantLock();
        isSend = false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_comment, menu);
         send = menu.findItem(R.id.action_send);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == android.R.id.home){
            finish();
            return true;
        }
        if(id == R.id.action_send){
            lock.lock();
            Log.e("kienbk1910","cllick");
            if(isSend == false) {
                isSend = true;
                 send.setEnabled(false);
                lock.unlock();
                if (mTxtComments.getText().toString().trim().length() == 0) {
                    Snackbar.make(toolbar, "Content empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }

                addComment();
                return true;
            }
            lock.unlock();
        }


        return super.onOptionsItemSelected(item);
    }
    private void addComment()  {
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "";
        if(this.type == ComponentType.TASK){
            url =  ((INCOApplication)getApplication()).getUrlApi(Globals.API_ADD_COMMENT_OF_TASK);
        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isSend = false;
                send.setEnabled(true);
                mProgressBar.setVisibility(View.INVISIBLE);
                AddCommentActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(toolbar, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                isSend = false;
                send.setEnabled(true);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,((INCOApplication)getApplication()).getTokenAccess());
                if(AddCommentActivity.this.type == ComponentType.TASK){
                    params.put(Globals.ADD_TASK_COMM_BY,((INCOApplication)getApplication()).getUserInfo().getId());
                    params.put(Globals.ADD_TASK_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_TASK_COMM_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_TASK_COMM_DES,AddCommentActivity.this.mTxtComments.getText().toString());
                    params.put(Globals.ADD_COMMENT_PRO_ID,AddCommentActivity.this.project_id);
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        INCOApplication.getInstance().addToRequestQueue(request);
    }
}
