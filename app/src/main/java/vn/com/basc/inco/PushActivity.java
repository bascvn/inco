package vn.com.basc.inco;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import vn.com.basc.inco.R;
import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.fragment.PushFragment;
import vn.com.basc.inco.fragment.TaskFragment;
import vn.com.basc.inco.gcm.Push;

public class PushActivity extends AppCompatActivity implements PushFragment.OnListFragmentInteractionListener {
    PushFragment pushFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pushFragment  =  PushFragment.newInstance(1);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, pushFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(item.getItemId() == R.id.remove){
            INCOApplication.getInstance().myDatabase.cleanDatabase();
            pushFragment.refreshList();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_push_activity, menu);


        return true;
    }
    @Override
    public void onListFragmentInteraction(Push item) {
        Intent intent = new Intent(this, DetailBaseComponentActivity.class);
        intent.putExtra(Globals.ID_EXTRA, item.getId());
        intent.putExtra(Globals.MESS_EXTRA, item.getParent());
        if(item.getComponent().equalsIgnoreCase(ComponentType.TASK_COMMENT)){
            intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.TASK);
        }else if(item.getComponent().equalsIgnoreCase(ComponentType.TICKET_COMMENT)){
            intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.TICKET);
        }else if(item.getComponent().equalsIgnoreCase(ComponentType.DISCUSSION_COMMENT)){
            intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.DISCUSSION);
        }else{
            intent.putExtra(Globals.COMPONENT_EXTRA, ComponentType.PROJECT);

        }
        intent.putExtra(Globals.PROJECT_ID_EXTRA,item.getProject());
        startActivity(intent);
    }
}
