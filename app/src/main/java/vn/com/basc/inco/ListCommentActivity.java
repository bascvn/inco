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
    private String project_id;
    private String id;
    private int type;
    CommentFragment fragment;
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
                Intent intent = new Intent(ListCommentActivity.this,AddCommentActivity.class);
                intent.putExtra(Globals.ID_EXTRA,ListCommentActivity.this.id);
                intent.putExtra(Globals.PROJECT_ID_EXTRA,ListCommentActivity.this.project_id);
                intent.putExtra(Globals.COMPONENT_EXTRA,ListCommentActivity.this.type);
                startActivity(intent);

            }
        });
         this.id = getIntent().getStringExtra(Globals.ID_EXTRA);
         this.type = getIntent().getIntExtra(Globals.COMPONENT_EXTRA,ComponentType.TASK);
         this.project_id = getIntent().getStringExtra(Globals.PROJECT_ID_EXTRA);
         fragment =  CommentFragment.newInstance(1,this.id,this.type);
        String name = getIntent().getStringExtra(Globals.MESS_EXTRA);
        getSupportActionBar().setTitle(name);
        //ListCommentFragment fragment =  ListCommentFragment.newInstance(1);
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment.refreshList();
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
        Intent intent = new Intent(ListCommentActivity.this,DetailCommentActivity.class);
        intent.putExtra(Globals.MESS_EXTRA,item);
        startActivity(intent);
    }

    @Override
    public void onListCommentFragmentInteraction(DummyContent.DummyItem item) {

    }
}
