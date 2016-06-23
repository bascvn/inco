package vn.com.basc.inco.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.R;
import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.fragment.PushFragment.OnListFragmentInteractionListener;
import vn.com.basc.inco.gcm.Push;
import vn.com.basc.inco.network.CustomVolleyRequest;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Push} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PushRecyclerViewAdapter extends RecyclerView.Adapter<PushRecyclerViewAdapter.ViewHolder> {

    private final List<Push> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PushRecyclerViewAdapter(List<Push> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_push, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getTitle());
        holder.mDate.setText(mValues.get(position).getDate());
        holder.mContentView.setText(mValues.get(position).getMessage());
        holder.mParent.setText(mValues.get(position).getParent());
        loadAvatarImage(holder.mItem.getPhoto(),holder.mAvatar);
        if(holder.mItem.getComponent().equalsIgnoreCase(ComponentType.TASK_COMMENT)){
            holder.mComponent.setImageResource(R.drawable.fa_tasks);
        }else if(holder.mItem.getComponent().equalsIgnoreCase(ComponentType.TICKET_COMMENT)){
            holder.mComponent.setImageResource(R.drawable.fa_bell);
        }else if(holder.mItem.getComponent().equalsIgnoreCase(ComponentType.DISCUSSION_COMMENT)){
            holder.mComponent.setImageResource(R.drawable.fa_comments);
        }else{
            holder.mComponent.setImageResource(R.drawable.fa_sitemap);

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
    private void loadAvatarImage(String avatar,NetworkImageView imageView){
        String url = INCOApplication.getInstance().getAvatarUrl(avatar);
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(INCOApplication.getInstance().getBaseContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView,
                R.drawable.ic_account_box_black_24dp, R.drawable.ic_account_box_black_24dp));
        imageView.setImageUrl(url, imageLoader);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final NetworkImageView mAvatar;
        public final TextView mName;
        public final  TextView mDate;
        public final TextView mContentView;
        public final  TextView mParent;
        public final ImageView mComponent;
        public Push mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAvatar = (NetworkImageView) view.findViewById(R.id.im_avatar);
            mName = (TextView) view.findViewById(R.id.txt_name);
            mDate = (TextView) view.findViewById(R.id.txt_create_at);
            mParent = (TextView) view.findViewById(R.id.txt_parent);
            mComponent = (ImageView) view.findViewById(R.id.im_component);
            mContentView = (TextView) view.findViewById(R.id.txt_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
