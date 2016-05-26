package vn.com.basc.inco.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import vn.com.basc.inco.fragment.TaskFragment.OnListTaskFragmentInteractionListener;
import vn.com.basc.inco.R;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.ProjectItem;
import vn.com.basc.inco.model.TaskItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProjectItem} and makes a call to the
 * specified {@link OnListTaskFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int FADE_DURATION = 1000; // in milliseconds
    private final List<Item> mValues;
    private final OnListTaskFragmentInteractionListener mListener;
    private static int ITEM =0;
    private static int FOOTER = 1;
    public MyTaskRecyclerViewAdapter(List<Item> items, OnListTaskFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_item, parent, false);
            return new TaskViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position) instanceof TaskItem) {
            return ITEM;
        } else if (mValues.get(position) instanceof Footer) {
            return FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TaskViewHolder) {
            final TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            taskViewHolder.mItem = (TaskItem) mValues.get(position);
            taskViewHolder.mTaskNameView.setText(taskViewHolder.mItem.name);
            taskViewHolder.mProjectNameView.setText(taskViewHolder.mItem.projects);
            taskViewHolder.mStatsuView.setText(taskViewHolder.mItem.tasks_status);
            taskViewHolder.mAssginToView.setText(taskViewHolder.mItem.assigned_to);
            taskViewHolder.mPriorityView.setText(taskViewHolder.mItem.tasks_priority);
            taskViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListTaskFragmentInteraction(taskViewHolder.mItem);
                    }
                }
            });
            setFadeAnimation(taskViewHolder.mView);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTaskNameView;
        public final TextView mProjectNameView;
        public final TextView mStatsuView;
        public final TextView mPriorityView;
        public final TextView mAssginToView;
        public TaskItem mItem;
        public final CardView mCardView;
        public TaskViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.cv);
            mTaskNameView = (TextView) mCardView.findViewById(R.id.task_name);
            mProjectNameView = (TextView) mCardView.findViewById(R.id.txt_projectname);
            mStatsuView =(TextView) mCardView.findViewById(R.id.txt_status);
            mPriorityView = (TextView) mCardView.findViewById(R.id.txt_prioritty);
            mAssginToView = (TextView) mCardView.findViewById(R.id.txt_assigned_to);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTaskNameView.getText() + "'";
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
