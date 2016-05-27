package vn.com.basc.inco.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import vn.com.basc.inco.MyApplication;
import vn.com.basc.inco.R;
import vn.com.basc.inco.fragment.CommentFragment.OnListCommentragmentInteractionListener;
import vn.com.basc.inco.model.CommentItem;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.ProjectItem;
import vn.com.basc.inco.network.CustomVolleyRequest;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProjectItem} and makes a call to the
 * specified {@link OnListCommentragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCommetRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int FADE_DURATION = 1000; // in milliseconds
    private final List<Item> mValues;
    private final OnListCommentragmentInteractionListener mListener;
    private static int ITEM =0;
    private static int FOOTER = 1;
    MyApplication myApplication;
    public MyCommetRecyclerViewAdapter(List<Item> items, OnListCommentragmentInteractionListener listener, MyApplication myApp) {
        mValues = items;
        mListener = listener;
        myApplication =myApp;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item, parent, false);
            return new CommentViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position) instanceof CommentItem) {
            return ITEM;
        } else if (mValues.get(position) instanceof Footer) {
            return FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            final CommentViewHolder  commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.mItem = (CommentItem) mValues.get(position);
            commentViewHolder.mNameView.setText(commentViewHolder.mItem.name);
            commentViewHolder.mCreateAtView.setText(commentViewHolder.mItem.created_at);
            commentViewHolder.mDescriptionView.setText(Html.fromHtml(commentViewHolder.mItem.description));
            loadAvatarImage(commentViewHolder.mItem.avatar,commentViewHolder.mAvatarView);
            commentViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListCommentFragmentInteraction(commentViewHolder.mItem);
                    }
                }
            });
            setFadeAnimation(commentViewHolder.mView);
        }
    }
    private void loadAvatarImage(String avatar,NetworkImageView imageView){
        String url = myApplication.getAvatarUrl(avatar);
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(myApplication.getBaseContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView,
                R.mipmap.ic_account_circle_white_48dp, R.mipmap.ic_account_circle_white_48dp));
        imageView.setImageUrl(url, imageLoader);
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final NetworkImageView mAvatarView;
        public final TextView mDescriptionView;
        public final TextView mAttachmentByView;
        public final TextView mCreateAtView;
        public CommentItem mItem;
        public final CardView mCardView;
        public CommentViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.cv);
            mNameView = (TextView) mCardView.findViewById(R.id.txt_name);
            mAvatarView = (NetworkImageView) mCardView.findViewById(R.id.im_avatar);
            mDescriptionView =(TextView) mCardView.findViewById(R.id.txt_description);
            mAttachmentByView = (TextView) mCardView.findViewById(R.id.txt_attachment);
            mCreateAtView = (TextView) mCardView.findViewById(R.id.txt_create_at);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar getProgressBar() {
            return progressBar;
        }

        private ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.footer);
        }
    }
    private void setFadeAnimation(View view) {
       /* AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);*/
    }
}
