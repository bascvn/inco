package vn.com.basc.inco.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.R;
import vn.com.basc.inco.adapter.MyProjectRecyclerViewAdapter;
import vn.com.basc.inco.adapter.PushRecyclerViewAdapter;
import vn.com.basc.inco.gcm.Push;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.MainFragmentINCO;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PushFragment extends Fragment implements MainFragmentINCO {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RelativeLayout mEmptyStateView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    List<Push> pushList;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PushFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PushFragment newInstance(int columnCount) {
        PushFragment fragment = new PushFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        pushList = new ArrayList<Push>();
        // Set the adapter
        mRecyclerView = (RecyclerView)view.findViewById(R.id.list);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mEmptyStateView = (RelativeLayout) view.findViewById(R.id.empty_state);
        emptyState(false);
        Button btn_reload = (Button) view.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });

        pushList = INCOApplication.getInstance().myDatabase.getPushes();
        Log.d("kienbk1910","pushList"+pushList.size());
        if (mRecyclerView instanceof RecyclerView) {
            Context context = view.getContext();

            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mRecyclerView.setAdapter(new PushRecyclerViewAdapter(pushList, mListener));
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }

            });

        }
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("kien", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshList();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        if(pushList.size() == 0){
            emptyState(true);
        }else{
            emptyState(false);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void refreshList() {
        mySwipeRefreshLayout.setRefreshing(true);

        int size = pushList.size();
        if(size > 0 ){
            pushList.clear();
            mRecyclerView.getAdapter().notifyItemRangeRemoved(0,size);
        }
        // this.notifyItemRangeRemoved(0, size);

        pushList.addAll(INCOApplication.getInstance().myDatabase.getPushes());
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if(pushList.size() == 0){
            emptyState(true);
        }else{
            emptyState(false);
        }
        mySwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void searchList(String key) {

    }

    @Override
    public void emptyState(boolean isEmpty) {
        if(isEmpty){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyStateView.setVisibility(View.VISIBLE);
        }else{
            mEmptyStateView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Push item);
    }
}
