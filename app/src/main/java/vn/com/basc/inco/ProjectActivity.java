package vn.com.basc.inco;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import android.support.design.widget.TabLayout;

import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.fragment.DiscussionFragment;
import vn.com.basc.inco.fragment.TaskFragment;
import vn.com.basc.inco.fragment.TaskFragment.OnListTaskFragmentInteractionListener;
import vn.com.basc.inco.fragment.TicketFragment;
import vn.com.basc.inco.model.DiscussionItem;
import vn.com.basc.inco.model.TaskItem;
import vn.com.basc.inco.model.TicketItem;

public class ProjectActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        OnListTaskFragmentInteractionListener,TicketFragment.OnListTicketragmentInteractionListener,DiscussionFragment.OnListDiscussionFragmentInteractionListener {

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
    private String id;
    private String name;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
         fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.task_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ticket_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.comment_selector));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.getTabAt(0).select();
        id = getIntent().getStringExtra(Globals.ID_EXTRA);
        name = getIntent().getStringExtra(Globals.MESS_EXTRA);
        getSupportActionBar().setTitle(name);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(mViewPager.getCurrentItem());
                tab.select();
                if(position == 1){
                    fab.show();
                }else{
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(ProjectActivity.this,NewTicketActivity.class);
              intent.putExtra(Globals.PROJECT_ID_EXTRA,ProjectActivity.this.id);
              startActivity(intent);
          }
      });
        fab.setVisibility(View.VISIBLE);
      
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_detail) {
            Intent intent = new Intent(ProjectActivity.this,DetailBaseComponentActivity.class);
            intent.putExtra(Globals.PROJECT_ID_EXTRA,ProjectActivity.this.id);
            intent.putExtra(Globals.COMPONENT_EXTRA,ComponentType.PROJECT);
            intent.putExtra(Globals.MESS_EXTRA,name);
            startActivity(intent);
            return true;
        }
        if(id == android.R.id.home){
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition() ==1){
            fab.show();
        }else{
            fab.hide();
        }
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void  onListTaskFragmentInteraction(TaskItem item) {
        Intent intent = new Intent(ProjectActivity.this, DetailBaseComponentActivity.class);
        intent.putExtra(Globals.ID_EXTRA, item.id);
        intent.putExtra(Globals.MESS_EXTRA, item.name);
        intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.TASK);
        intent.putExtra(Globals.PROJECT_ID_EXTRA,this.id);
        startActivity(intent);

    }

    @Override
    public void onListTicketFragmentInteraction(TicketItem item) {
        Intent intent = new Intent(ProjectActivity.this, DetailBaseComponentActivity.class);
        intent.putExtra(Globals.ID_EXTRA, item.id);
        intent.putExtra(Globals.MESS_EXTRA, item.name);
        intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.TICKET);
        intent.putExtra(Globals.PROJECT_ID_EXTRA,this.id);
        startActivity(intent);
    }

    @Override
    public void onListDiscussionFragmentInteraction(DiscussionItem item) {
        Intent intent = new Intent(ProjectActivity.this, DetailBaseComponentActivity.class);
        intent.putExtra(Globals.ID_EXTRA, item.id);
        intent.putExtra(Globals.MESS_EXTRA, item.name);
        intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.DISCUSSION);
        intent.putExtra(Globals.PROJECT_ID_EXTRA,this.id);
        startActivity(intent);
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
                fab.hide();
                return TaskFragment.newInstance(1,id);
            }else if(position == 1){
                fab.show();
                return TicketFragment.newInstance(1,id);
            }
            fab.hide();
            return DiscussionFragment.newInstance(1,id);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

            }
            return null;
        }
    }


}
