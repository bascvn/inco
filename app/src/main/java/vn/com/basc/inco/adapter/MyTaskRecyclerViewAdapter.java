package vn.com.basc.inco.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.basc.inco.R;
import vn.com.basc.inco.TaskFragment.OnTaskListFragmentInteractionListener;
import vn.com.basc.inco.model.TaskContent.TaskItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskItem} and makes a call to the
 * specified {@link OnTaskListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<TaskItem> mValues;
    private final OnTaskListFragmentInteractionListener mListener;

    public MyTaskRecyclerViewAdapter(List<TaskItem> items, OnTaskListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
      //  holder.mIdView.setText(mValues.get(position).id);
        //holder.mNameView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTaskListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public TaskItem mItem;
        public final CardView mCardView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.cv);
            mIdView = (TextView) mCardView.findViewById(R.id.task_name);
            mContentView = (TextView) mCardView.findViewById(R.id.txt_create_by);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
