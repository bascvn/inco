package vn.com.basc.inco;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.dummy.DummyContent;
import vn.com.basc.inco.fragment.CommentFragment;
import vn.com.basc.inco.model.CommentItem;

public class ListCommentActivity extends AppCompatActivity implements CommentFragment.OnListCommentragmentInteractionListener,ListCommentFragment.OnListCommentFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        String id = getIntent().getStringExtra(Globals.ID_EXTRA);
        int type = getIntent().getIntExtra(Globals.COMPONENT_EXTRA,ComponentType.TASK);
        CommentFragment fragment =  CommentFragment.newInstance(1,id,type);
        String name = getIntent().getStringExtra(Globals.MESS_EXTRA);
        getSupportActionBar().setTitle(name);
        //ListCommentFragment fragment =  ListCommentFragment.newInstance(1);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListCommentFragmentInteraction(CommentItem item) {

    }

    @Override
    public void onListCommentFragmentInteraction(DummyContent.DummyItem item) {

    }
}
