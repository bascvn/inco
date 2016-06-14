package vn.com.basc.inco.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.R;
import vn.com.basc.inco.adapter.MyCommetRecyclerViewAdapter;
import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.model.CommentItem;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.MainFragmentINCO;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListCommentragmentInteractionListener}
 * interface.
 */
public class CommentFragment extends Fragment implements MainFragmentINCO{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ID= "key_id";
    private static final String ARG_TYPE ="type";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Item> projects;
    private OnListCommentragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RelativeLayout mEmptyStateView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private boolean hasMore = true;
    private String id;
    private String key;
    private int type;
    private String projectsID;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentFragment() {
    }


    public static CommentFragment newInstance(String columnCount, String id,int type) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_ID, id);
        args.putInt(ARG_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.projectsID = getArguments().getString(ARG_COLUMN_COUNT);
            this.id = getArguments().getString(ARG_ID,"");
            this.type = getArguments().getInt(ARG_TYPE, ComponentType.TASK);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        projects = new ArrayList<Item>();
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
        if (mRecyclerView instanceof RecyclerView) {
            Context context = view.getContext();

            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setAdapter(new MyCommetRecyclerViewAdapter(projects, mListener, (INCOApplication) getActivity().getApplication()));
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (hasMore && !(hasFooter())) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        //position starts at 0
                        if (layoutManager.findLastCompletelyVisibleItemPosition() == mRecyclerView.getAdapter().getItemCount() - 1) {
                            Log.i("kien", "get list");
                            getListProject("");
                        }
                    }
                }

            });
            getListProject("");
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

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListCommentragmentInteractionListener) {
            mListener = (OnListCommentragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTaskListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListCommentragmentInteractionListener {
        // TODO: Update argument type and name
        void onListCommentFragmentInteraction(CommentItem item);

    }
    private void getListProject(final String key)  {
        String url ="";
        if(type == ComponentType.TASK) {
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_COMMENT_OF_TASK);
        }else if(type == ComponentType.TICKET){
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_COMMENT_OF_TICKET);
        }else if(type == ComponentType.DISCUSSION){
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_COMMENT_OF_DISCUSSIONS);
        }else{
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_COMMENT_OF_PROJECT);

        }
        Log.d("kienbk1910",url);
        projects.add(new Footer());
        mRecyclerView.getAdapter().notifyItemInserted(projects.size() - 1);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("kienbk1910",response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(INCOResponse.isError(obj.getString(INCOResponse.STATUS_TAG))){
                        Log.d("kienbk1910","error");
                    }else{

                        String  productStr = obj.getString(INCOResponse.DATA_TAG);
                        Log.d("kienbk1910",productStr);
                        Gson gson = new Gson();
                        List<CommentItem> data= (List<CommentItem>) gson.fromJson(productStr,
                                new TypeToken<List<CommentItem>>() {
                                }.getType());
                        if(!data.isEmpty()){
                            int size = projects.size();
                            projects.remove(size - 1);//removes footer
                            projects.addAll(data);
                            mRecyclerView.getAdapter().notifyItemRangeChanged(size - 1, projects.size() - size);
                            checkEmptyState();
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.d("kienbk1910",e.toString());

                }
                removeFooter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                removeFooter();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,((INCOApplication)getActivity().getApplication()).getTokenAccess());
                if(projects.size() == 0) {
                    params.put(Globals.OFFSET_PARAMETER, "0");
                } else if(hasFooter()){
                    params.put(Globals.OFFSET_PARAMETER, String.valueOf(projects.size()-1));
                }else{
                    params.put(Globals.OFFSET_PARAMETER, String.valueOf(projects.size()));
                }
                if(type == ComponentType.TASK){
                    params.put(Globals.TASK_ID_PARAMETER,id);
                }else if (type == ComponentType.DISCUSSION){
                    params.put(Globals.DISCUSSION_ID_PARAMETER,id);
                }else if (type == ComponentType.TICKET){
                    params.put(Globals.TICKET_ID_PARAMETER,id);
                }else{
                    params.put(Globals.PROJECT_ID_PARAMETER,CommentFragment.this.projectsID);
                }

                params.put(Globals.LIMIT_PARAMETER, "30");
                params.put(Globals.SEARCH_PARAMETER, key);
                return params;
            }
        };

        INCOApplication.getInstance().addToRequestQueue(request);
    }
    private boolean hasFooter() {
        if(projects.size() == 0){
            return false;
        }
        return projects.get(projects.size() - 1) instanceof Footer;
    }
    public void refreshList(){
        mySwipeRefreshLayout.setRefreshing(true);
        hasMore =false;
        int size = projects.size();
        if(size > 0 ){
            projects.clear();
            mRecyclerView.getAdapter().notifyItemRangeRemoved(0,size);
        }
        // this.notifyItemRangeRemoved(0, size);

        getListProject("");
        hasMore =true;
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void searchList(String key) {
        hasMore =false;
        int size = projects.size();
        if(size > 0 ){
            projects.clear();
            mRecyclerView.getAdapter().notifyItemRangeRemoved(0,size);
        }
        // this.notifyItemRangeRemoved(0, size);

        getListProject(key);
        hasMore =true;
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

    private void removeFooter(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hasFooter()){
                    int size = projects.size();
                    projects.remove(size - 1);//removes footer
                    mRecyclerView.getAdapter().notifyItemRemoved(size-1);
                    checkEmptyState();
                }
            }
        }, 500);

    }
    private void checkEmptyState(){
        if(projects.size() == 0){
            emptyState(true);
        }else{
            emptyState(false);
        }
    }
}
