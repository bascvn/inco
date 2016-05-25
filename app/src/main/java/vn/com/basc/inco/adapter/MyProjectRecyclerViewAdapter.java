package vn.com.basc.inco.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import vn.com.basc.inco.ProjectFragment.OnListFragmentInteractionListener;
import vn.com.basc.inco.R;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.ProjectItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProjectItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProjectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int FADE_DURATION = 1000; // in milliseconds
    private final List<Item> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static int ITEM =0;
    private static int FOOTER = 1;
    public MyProjectRecyclerViewAdapter(List<Item> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_item, parent, false);
            return new ProjectViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position) instanceof ProjectItem) {
            return ITEM;
        } else if (mValues.get(position) instanceof Footer) {
            return FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProjectViewHolder) {
            final ProjectViewHolder projectViewHolder = (ProjectViewHolder) holder;
            projectViewHolder.mItem = (ProjectItem) mValues.get(position);
            projectViewHolder.mIdView.setText(projectViewHolder.mItem.id);
            projectViewHolder.mNameView.setText(projectViewHolder.mItem.name);
            projectViewHolder.mCreateBy.setText(projectViewHolder.mItem.created_by);
            projectViewHolder.mCreateAt.setText(projectViewHolder.mItem.created_at);
            projectViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(projectViewHolder.mItem);
                    }
                }
            });
            setFadeAnimation(projectViewHolder.mView);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mCreateBy;
        public final TextView mCreateAt;
        public ProjectItem mItem;
        public final CardView mCardView;
        public ProjectViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.cv);
            mIdView = (TextView) mCardView.findViewById(R.id.id_project);
            mNameView = (TextView) mCardView.findViewById(R.id.name_project);
            mCreateBy =(TextView) mCardView.findViewById(R.id.txt_create_by);
            mCreateAt = (TextView) mCardView.findViewById(R.id.txt_create_at);
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
