package vn.com.basc.inco;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.FileUtility;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.fragment.AddCommentFragment;
import vn.com.basc.inco.fragment.AddFileFragment;
import vn.com.basc.inco.model.UploadFile;

public class AddCommentActivity extends INCOActivity implements AddFileFragment.OnListFragmentInteractionListener,AddCommentFragment.OnFragmentInteractionListener{
    private String project_id;
    private String id;
    private int type;
    Toolbar toolbar;
    private static final int READ_REQUEST_CODE = 42;
    private boolean isSend;
    MenuItem send;
    Lock lock;
    TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    FloatingActionButton fab;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
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
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        this.id = getIntent().getStringExtra(Globals.ID_EXTRA);
        this.type = getIntent().getIntExtra(Globals.COMPONENT_EXTRA, ComponentType.TASK);
        this.project_id = getIntent().getStringExtra(Globals.PROJECT_ID_EXTRA);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.comment_selector));

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.attachment_selector));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(mViewPager.getCurrentItem());
                tab.select();
                if(mViewPager.getCurrentItem() ==0){
                    fab.hide();
                }else{
                    fab.show();
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() ==0){
                    fab.hide();
                }else{
                    fab.show();
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0).select();
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
              //  intent.addCategory(Intent.CATEGORY_);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("*/*");

                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("kienbk1910", "Uri: " + uri.toString());
                UploadFile uploadFile = FileUtility.dumpFileMetaData(getBaseContext(),uri);
                if(uploadFile != null) {
                    ((AddFileFragment) mSectionsPagerAdapter.getItem(1)).addFileUpload(uploadFile);
                }
            }
        }
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
           if(checkDataEmpty()){
               finish();
               return true;
           }

        }
        if(id == R.id.action_send){
            if(checkUploadingFile(addFileFragment.getUploadFile())){
                Toast toast = Toast.makeText(this, "Please waiting file upload..", Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
               addComment();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }
    private boolean checkDataEmpty(){
        if(!addCommentFragment.haveData() && !addFileFragment.haveData()){
            return true;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.discard_changes)
                .setMessage(R.string.discard_changes_mes)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(R.string.discard_changes_keep, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                    }})
                .setNegativeButton(R.string.discard_changes_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
        return false;

    }
    @Override
    public void onBackPressed() {
        if(!checkDataEmpty()){
            return;
        }
        super.onBackPressed();
    }

    private void addComment()  {
        showProgressDialog("Sending...");
        String url = "";
        if(this.type == ComponentType.TASK){
            url =  ((INCOApplication)getApplication()).getUrlApi(Globals.API_ADD_COMMENT_OF_TASK);
        }else if(this.type == ComponentType.TICKET){
            url =  ((INCOApplication)getApplication()).getUrlApi(Globals.API_ADD_COMMENT_OF_TICKET);
        }else if(this.type == ComponentType.DISCUSSION){
            url =  ((INCOApplication)getApplication()).getUrlApi(Globals.API_ADD_COMMENT_OF_DISCUSS);
        }else{
            url =  ((INCOApplication)getApplication()).getUrlApi(Globals.API_ADD_COMMENT_OF_PROJECT);
        }
        Log.e("kienbk1910","url:"+url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("kienbk1910","response:"+response);
                hideProgressDialog();
                AddCommentActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(toolbar, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                hideProgressDialog();
                isSend = false;
                send.setEnabled(true);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,INCOApplication.getInstance().getTokenAccess());
                if(AddCommentActivity.this.type == ComponentType.TASK){
                    params.put(Globals.ADD_TASK_COMM_BY,((INCOApplication)getApplication()).getUserInfo().getId());
                    params.put(Globals.ADD_TASK_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_TASK_COMM_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_TASK_COMM_DES,addCommentFragment.getComments());
                    params.put(Globals.ADD_COMMENT_PRO_ID,AddCommentActivity.this.project_id);

                }else if(AddCommentActivity.this.type == ComponentType.TICKET){
                    params.put(Globals.ADD_TICKET_COMM_BY,((INCOApplication)getApplication()).getUserInfo().getId());
                    params.put(Globals.ADD_TICKET_COMM_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_TICKET_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_TICKET_DES,((AddCommentFragment) addCommentFragment).getComments());
                    params.put(Globals.ADD_COMMENT_PRO_ID,AddCommentActivity.this.project_id);
                }else if(AddCommentActivity.this.type == ComponentType.DISCUSSION){
                    params.put(Globals.ADD_DISCUSS_COMM_BY,((INCOApplication)getApplication()).getUserInfo().getId());
                    params.put(Globals.ADD_DISCUSS_COM_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_DISCUSS_ID,AddCommentActivity.this.id);
                    params.put(Globals.ADD_DISCUSS_DES,((AddCommentFragment) addCommentFragment).getComments());
                    params.put(Globals.ADD_COMMENT_PRO_ID,AddCommentActivity.this.project_id);
                }else{
                    params.put(Globals.ADD_PROJECT_COMM_DES,((AddCommentFragment) addCommentFragment).getComments());
                    params.put(Globals.ADD_PROJECT_COMM_ID,AddCommentActivity.this.project_id);
                    params.put(Globals.ADD_COMMENT_PRO_ID,AddCommentActivity.this.project_id);
                    params.put(Globals.ADD_PROJECT_COMM_BY,((INCOApplication)getApplication()).getUserInfo().getId());
                }
                List<UploadFile> fileList = addFileFragment.getUploadFile();
                for (int i = 0; i< fileList.size();i++){
                    if(fileList.get(i).getStatus() == 2) {
                        String info = fileList.get(i).getInfo();
                        Log.e("kienbk1910","attachments_info" + fileList.get(i).getId());
                        if(info == null ) {
                            info ="";
                        }
                        params.put("attachments_info[" + fileList.get(i).getId() + "]", info);
                    }
                }
               // params.put("attachments_info[27]", "fdfdf");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        INCOApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(UploadFile item) {

    }

    @Override
    public void onDeleteFileUpload(final UploadFile item) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Do you remove file")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ((AddFileFragment) mSectionsPagerAdapter.getItem(1)).deleteUploadFile(item);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
    private AddFileFragment addFileFragment;
    private AddCommentFragment addCommentFragment ;
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
                if (addCommentFragment == null){
                    addCommentFragment = AddCommentFragment.newInstance("1", "1");
                 }
                return addCommentFragment;
            }
            if (addFileFragment == null){
                 addFileFragment = AddFileFragment.newInstance(AddCommentActivity.this.type);
              }
            return addFileFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";


            }
            return null;
        }
    }
}
