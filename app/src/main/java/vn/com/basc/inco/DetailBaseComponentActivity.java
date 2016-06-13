package vn.com.basc.inco;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.fragment.CommentFragment;
import vn.com.basc.inco.fragment.DetailBaseComponentFragment;
import vn.com.basc.inco.model.CommentItem;
import vn.com.basc.inco.model.DetailBaseComponent;

public class DetailBaseComponentActivity extends AppCompatActivity implements CommentFragment.OnListCommentragmentInteractionListener {
    private String project_id;
    private String id;
    private int type;
    private String title;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_base_component);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.description_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.comment_selector));
         fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout.getTabAt(0).select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    fab.hide();
                }else{
                    fab.show();
                }
                mViewPager.setCurrentItem(tab.getPosition());
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
               tabLayout.getTabAt(position).select();
               if(position == 0){
                   fab.hide();
               }else{
                   fab.show();
               }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailBaseComponentActivity.this,AddCommentActivity.class);
                intent.putExtra(Globals.ID_EXTRA,DetailBaseComponentActivity.this.id);
                intent.putExtra(Globals.PROJECT_ID_EXTRA,DetailBaseComponentActivity.this.project_id);
                intent.putExtra(Globals.COMPONENT_EXTRA,DetailBaseComponentActivity.this.type);
                startActivity(intent);
            }
        });
       fab.setVisibility(View.INVISIBLE);
        this.project_id = getIntent().getStringExtra(Globals.PROJECT_ID_EXTRA);
        this.id = getIntent().getStringExtra(Globals.ID_EXTRA);
        this.type = getIntent().getIntExtra(Globals.COMPONENT_EXTRA, ComponentType.TASK);
        this.title = getIntent().getStringExtra(Globals.MESS_EXTRA);
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_base_component, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListCommentFragmentInteraction(CommentItem item) {
        Intent intent = new Intent(DetailBaseComponentActivity.this,DetailCommentActivity.class);
        intent.putExtra(Globals.MESS_EXTRA,item);
        startActivity(intent);
    }
    CommentFragment commentFragment;


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
            if(position == 0){
                 fab.hide();
                return  DetailBaseComponentFragment.newInstance(DetailBaseComponentActivity.this.project_id,DetailBaseComponentActivity.this.id,DetailBaseComponentActivity.this.type);
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //fab.show();
             commentFragment = CommentFragment.newInstance(1,DetailBaseComponentActivity.this.id,DetailBaseComponentActivity.this.type);
            return commentFragment;
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
