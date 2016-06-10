package vn.com.basc.inco;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.FileUtility;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.fragment.AddCommentFragment;
import vn.com.basc.inco.fragment.AddFileFragment;
import vn.com.basc.inco.fragment.AddTicketConfigFragment;
import vn.com.basc.inco.fragment.AddTicketConfigFragment.OnFragmentInteractionListener;
import vn.com.basc.inco.fragment.AddTicketContentFragment;
import vn.com.basc.inco.model.UploadFile;

public class NewTicketActivity extends AppCompatActivity implements AddFileFragment.OnListFragmentInteractionListener,AddTicketContentFragment.OnFragmentInteractionListener,OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private AddTicketConfigFragment ticketConfigFragment;
    private AddTicketContentFragment addTicketContentFragment;
    private AddFileFragment addFileFragment;
    TabLayout tabLayout;
     FloatingActionButton fab;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
         tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ticket_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.setting_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.attachment_selector));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 2){

                    fab.show();
                }else{
                    fab.hide();
                }
                if(tab.getPosition() != 0){
                    hideSoftKeyboard();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(mViewPager.getCurrentItem());
                tab.select();
                if(tab.getPosition() == 2){

                    fab.show();
                }else{
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.getTabAt(0).select();

        //tabLayout.setupWithViewPager(mViewPager);
        this.id = getIntent().getStringExtra(Globals.PROJECT_ID_EXTRA);
         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private static final int READ_REQUEST_CODE = 42;
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
                    addFileFragment.addFileUpload(uploadFile);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_ticket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.e("kienbk1910","honOptionsItemSelected");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            if(addTicketContentFragment.getNameTicket().toString().trim().length() == 0){
                addTicketContentFragment.setNameError();
                tabLayout.getTabAt(0).select();
                return false;
            }
            newTicket();
            return true;
        }
        if(id == android.R.id.home) {
            Log.e("kienbk1910","home click");
            if(!checkDataEmpty()){
                return false;
            }
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    private boolean checkDataEmpty(){
        if(addFileFragment.haveData() || addTicketContentFragment.haveData()){
            new AlertDialog.Builder(this)
                    .setTitle("Discard Changes?")
                    .setMessage("If you go back now, your draft will be discarded.")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("KEEP", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {


                        }})
                    .setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return false;
        }

        return  true;
    }

    @Override
    public void onBackPressed() {
        if(!checkDataEmpty()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(UploadFile item) {

    }

    @Override
    public void onDeleteFileUpload(UploadFile item) {

    }
    private void  newTicket(){
        String url = ((INCOApplication)getApplication()).getUrlApi(Globals.API_NEW_TICKET);

        Log.e("kienbk1910","url:"+url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("kienbk1910","response:"+response);
                NewTicketActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,INCOApplication.getInstance().getTokenAccess());
                params.put(Globals.NEW_TICKET_COM_ID,id);
                params.put(Globals.NEW_TICKET_DEPA_ID,ticketConfigFragment.getDeparmentId());
                params.put(Globals.NEW_TICKET_TYPE_ID,ticketConfigFragment.getTypeTicket());
                params.put(Globals.NEW_TICKET_STATUS_ID,ticketConfigFragment.getTypeStatus());
                params.put(Globals.NEW_TIECKT_USER,ticketConfigFragment.getUserCreate());
                params.put(Globals.NEW_TICKET_NAME,addTicketContentFragment.getNameTicket());
                params.put(Globals.NEW_TICKET_DES,addTicketContentFragment.getBodyTicket());
                List<String> notifys = ticketConfigFragment.getUserNotify();
                for(int i = 0; i< notifys.size();i++){
                    params.put(Globals.EXTRA_NOTIFY,notifys.get(i));
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
                params.put(Globals.ADD_COMMENT_PRO_ID,NewTicketActivity.this.id);
                // params.put("attachments_info[27]", "fdfdf");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        INCOApplication.getInstance().addToRequestQueue(request);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                addTicketContentFragment =  AddTicketContentFragment.newInstance("1","1");
                NewTicketActivity.this.fab.hide();
                return addTicketContentFragment;

            }
            if(position == 2){
                addFileFragment = AddFileFragment.newInstance(ComponentType.NEW_TICKET);
                return addFileFragment;
            }
            ticketConfigFragment = AddTicketConfigFragment.newInstance(NewTicketActivity.this.id);
            return ticketConfigFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
