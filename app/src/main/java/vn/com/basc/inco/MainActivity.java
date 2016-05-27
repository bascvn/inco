package vn.com.basc.inco;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import vn.com.basc.inco.fragment.DiscussionFragment;
import vn.com.basc.inco.fragment.ProjectFragment;
import vn.com.basc.inco.fragment.ProjectFragment.OnListFragmentInteractionListener;
import vn.com.basc.inco.fragment.TaskFragment;
import vn.com.basc.inco.fragment.TaskFragment.OnListTaskFragmentInteractionListener;
import vn.com.basc.inco.fragment.TicketFragment;
import vn.com.basc.inco.fragment.TicketFragment.OnListTicketragmentInteractionListener;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.model.DiscussionItem;
import vn.com.basc.inco.model.MainFragmentINCO;
import vn.com.basc.inco.model.ProjectItem;
import vn.com.basc.inco.model.TaskItem;
import vn.com.basc.inco.model.TicketItem;
import vn.com.basc.inco.model.User;
import vn.com.basc.inco.network.CustomVolleyRequest;
import com.android.volley.toolbox.ImageLoader;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnListFragmentInteractionListener,
        OnListTaskFragmentInteractionListener,OnListTicketragmentInteractionListener,DiscussionFragment.OnListDiscussionFragmentInteractionListener,
        SearchView.OnQueryTextListener  {
    private NetworkImageView imageUserAvatar;
    private FrameLayout mFrameLayout;
    private ProjectFragment projectFragment;
    private TaskFragment taskFragment;
    private TicketFragment tickFragment;
    private DiscussionFragment discussionFragment;

    private MainFragmentINCO mainFragmentINCO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrameLayout  = (android.widget.FrameLayout) findViewById(R.id.fragment_container);

       // getSupportActionBar().setTitle(R.string.navigation_drawer_menu_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.navigation_drawer_menu_project);

        initProjectFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNavItemCount(R.id.nav_camera, 10);

        TextView mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        TextView mUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);
        imageUserAvatar  = (NetworkImageView) navigationView.getHeaderView(0).findViewById(R.id.imageUserAvatar);
        imageUserAvatar.setDefaultImageResId(R.mipmap.ic_account_circle_white_48dp);
        MyApplication myApplication = (MyApplication) getApplication();
        User user = myApplication.getUserInfo();
        if(user != null && user.getName()!= null){
            mUserName.setText(user.getName());
        }
        if(user != null && user.getEmail()!= null){
            mUserEmail.setText(user.getEmail());
        }
        if(user != null && user.getPhoto()!= null && user.getPhoto().length()>0){
            loadAvatarImage(user.getPhoto());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
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
        if(id== R.id.menu_refresh){
            mainFragmentINCO.refreshList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            initProjectFragment();
        } else if (id == R.id.nav_gallery) {
            initTaskFragment();
        } else if (id == R.id.nav_slideshow) {
            initTicketFragment();
        } else if (id == R.id.nav_manage) {
            initDiscussionFragment();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavItemCount( int itemId, int count) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
        if(count > 0){
            //view.setBackgroundResource(R.drawable.badge_circle);
        }
    }

    @Override
    public void onListFragmentInteraction(ProjectItem item) {
        Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
        // EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        intent.putExtra(Globals.ID_EXTRA, item.id);
        intent.putExtra(Globals.MESS_EXTRA, item.name);
        startActivity(intent);
    }

    @Override
    public void onListTaskFragmentInteraction(TaskItem item) {
        Intent intent = new Intent(MainActivity.this, ListCommentActivity.class);
        intent.putExtra(Globals.ID_EXTRA, item.id);
        startActivity(intent);
    }

    private void loadAvatarImage(String avatar){
        String url = ((MyApplication)getApplication()).getAvatarUrl(avatar);
        ImageLoader  imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageUserAvatar,
                R.mipmap.ic_account_circle_white_48dp, R.mipmap.ic_account_circle_white_48dp));
        imageUserAvatar.setImageUrl(url, imageLoader);
    }
    public void showSnackbarError(String error){
        Snackbar.make(mFrameLayout,error, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(mainFragmentINCO != null) {
            mainFragmentINCO.searchList(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(mainFragmentINCO != null) {
            mainFragmentINCO.searchList(newText);
        }
        return true;
    }
    private void initProjectFragment(){
        if(projectFragment == null){
            projectFragment =  ProjectFragment.newInstance(1);

        }
        getSupportActionBar().setTitle(R.string.navigation_drawer_menu_project);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, projectFragment);
        fragmentTransaction.commit();
        mainFragmentINCO = projectFragment;
    }
    private void initTaskFragment(){
        if(taskFragment == null){
            taskFragment =  TaskFragment.newInstance(1);

        }
        getSupportActionBar().setTitle(R.string.navigation_drawer_menu_task);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, taskFragment);
        fragmentTransaction.commit();
        mainFragmentINCO = taskFragment;
    }
    private void initTicketFragment(){
        if(tickFragment == null){
            tickFragment =  TicketFragment.newInstance(1);

        }
        getSupportActionBar().setTitle(R.string.navigation_drawer_menu_tickets);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, tickFragment);
        fragmentTransaction.commit();
        mainFragmentINCO = tickFragment;
    }
    private void initDiscussionFragment(){
        if(discussionFragment == null){
            discussionFragment =  DiscussionFragment.newInstance(1);

        }
        getSupportActionBar().setTitle(R.string.navigation_drawer_menu_discussions);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, discussionFragment);
        fragmentTransaction.commit();
        mainFragmentINCO = discussionFragment;
    }
    @Override
    public void onListTicketFragmentInteraction(TicketItem item) {

    }

    @Override
    public void onListDiscussionFragmentInteraction(DiscussionItem item) {

    }
}
