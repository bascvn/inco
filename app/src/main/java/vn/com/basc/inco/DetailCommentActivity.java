package vn.com.basc.inco;

import android.app.DownloadManager;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.model.Attachment;
import vn.com.basc.inco.model.CommentItem;
import vn.com.basc.inco.network.CustomVolleyRequest;

public class DetailCommentActivity extends AppCompatActivity implements View.OnClickListener{
    private CommentItem commentItem;
    private TextView mComment;
    private NetworkImageView imageUserAvatar;
    private LinearLayout fileContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mComment = (TextView) findViewById(R.id.txtComments);
        imageUserAvatar  = (NetworkImageView) findViewById(R.id.avatar);
        fileContainer = (LinearLayout) findViewById(R.id.file_container);
        imageUserAvatar.setDefaultImageResId(R.mipmap.ic_account_circle_white_48dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commentItem = (CommentItem) getIntent().getSerializableExtra(Globals.MESS_EXTRA);
        mComment.setText(Html.fromHtml(commentItem.description));
        getSupportActionBar().setTitle(commentItem.name);

        loadAvatarImage(commentItem.avatar);
        List<Attachment> attachments = commentItem.attachments;
        for (int i = 0; i<attachments.size();i++){
            fileContainer.addView(createViewDownload(attachments.get(i)));
        }
    }
    public View createViewDownload(Attachment attachment){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.download_item, null);
        TextView filename = (TextView) v.findViewById(R.id.txtFileName);
        ImageButton btn = (ImageButton) v.findViewById(R.id.imgDownload);
        btn.setTag(attachment);
        btn.setOnClickListener(DetailCommentActivity.this);
        filename.setText(attachment.getFile());

        return v;

    }
    private void loadAvatarImage(String avatar){
        String url = ((INCOApplication)getApplication()).getAvatarUrl(avatar);
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageUserAvatar,
                R.mipmap.ic_account_circle_white_48dp, R.mipmap.ic_account_circle_white_48dp));
        imageUserAvatar.setImageUrl(url, imageLoader);
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
    public void onClick(View v) {
        Attachment attachment = (Attachment) v.getTag();

        startDownload(attachment);
    }
    public void startDownload(Attachment attachment) {
        DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        INCOApplication  incoApplication = (INCOApplication) getApplication();
        String url = incoApplication.getUrlApi(Globals.API_DOWNLOAD_FILE)+"?id="+attachment.getId()+"&token="+incoApplication.getTokenAccess();
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(url));
        mRqRequest.setTitle(attachment.getFile());
        mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        mRqRequest.setDescription("This is Test File");
//        mRqRequest.setDestinationUri(Uri.parse("give your local path"));
        long idDownLoad=mManager.enqueue(mRqRequest);
    }
}
