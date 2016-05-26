package vn.com.basc.inco.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import vn.com.basc.inco.R;
import vn.com.basc.inco.fragment.TaskFragment.OnListTaskFragmentInteractionListener;
import vn.com.basc.inco.fragment.TicketFragment;
import vn.com.basc.inco.fragment.TicketFragment.OnListTicketragmentInteractionListener;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.ProjectItem;
import vn.com.basc.inco.model.TaskItem;
import vn.com.basc.inco.model.TicketItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProjectItem} and makes a call to the
 * specified {@link OnListTaskFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTicketRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int FADE_DURATION = 1000; // in milliseconds
    private final List<Item> mValues;
    private final OnListTicketragmentInteractionListener mListener;
    private static int ITEM =0;
    private static int FOOTER = 1;
    public MyTicketRecyclerViewAdapter(List<Item> items, TicketFragment.OnListTicketragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ticket_item, parent, false);
            return new TicketViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.get(position) instanceof TicketItem) {
            return ITEM;
        } else if (mValues.get(position) instanceof Footer) {
            return FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TicketViewHolder) {
            final TicketViewHolder  tickViewHolder = (TicketViewHolder) holder;
            tickViewHolder.mItem = (TicketItem) mValues.get(position);
            tickViewHolder.mTicketNameView.setText(tickViewHolder.mItem.name);
            tickViewHolder.mProjectNameView.setText(tickViewHolder.mItem.project);
            tickViewHolder.mStatsuView.setText(tickViewHolder.mItem.tickets_status);
            tickViewHolder.mCreateByView.setText(tickViewHolder.mItem.create_by);
            tickViewHolder.mDeparmetView.setText(tickViewHolder.mItem.departments);
            tickViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListTicketFragmentInteraction(tickViewHolder.mItem);
                    }
                }
            });
            setFadeAnimation(tickViewHolder.mView);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTicketNameView;
        public final TextView mProjectNameView;
        public final TextView mStatsuView;
        public final TextView mDeparmetView;
        public final TextView mCreateByView;
        public TicketItem mItem;
        public final CardView mCardView;
        public TicketViewHolder(View view) {
            super(view);
            mView = view;
            mCardView = (CardView) view.findViewById(R.id.cv);
            mTicketNameView = (TextView) mCardView.findViewById(R.id.ticket_name);
            mProjectNameView = (TextView) mCardView.findViewById(R.id.txt_projectname);
            mStatsuView =(TextView) mCardView.findViewById(R.id.txt_status);
            mDeparmetView = (TextView) mCardView.findViewById(R.id.txt_deparment);
            mCreateByView = (TextView) mCardView.findViewById(R.id.txt_create_by);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTicketNameView.getText() + "'";
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
