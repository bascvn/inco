package vn.com.basc.inco;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.basc.inco.adapter.MyProjectRecyclerViewAdapter;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.model.Footer;
import vn.com.basc.inco.model.Item;
import vn.com.basc.inco.model.ProjectItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProjectFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Item> projects;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private boolean hasMore = true;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProjectFragment newInstance(int columnCount) {
        ProjectFragment fragment = new ProjectFragment();
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
        projects = new ArrayList<Item>();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mRecyclerView.setAdapter(new MyProjectRecyclerViewAdapter(projects, mListener));
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (hasMore && !(hasFooter())) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        //position starts at 0
                        if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 2) {
                            getListProject();
                        }
                    }
                }
            });
            getListProject();
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProjectItem item);
    }
    private void getListProject()  {
        String url = ((MyApplication)getActivity().getApplication()).getUrlApi(Globals.API_PROJECT_LIST);
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
                        List<ProjectItem> data= (List<ProjectItem>) gson.fromJson(productStr,
                                new TypeToken<List<ProjectItem>>() {
                                }.getType());
                        if(!data.isEmpty()){
                            int size = projects.size();
                            projects.remove(size - 1);//removes footer
                            projects.addAll(data);
                            mRecyclerView.getAdapter().notifyItemRangeChanged(size - 1, projects.size() - size);
                        }
                    }
                } catch (Exception e) {
                    Log.d("kienbk1910",e.toString());
                    return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,((MyApplication)getActivity().getApplication()).getTokenAccess());
                params.put(Globals.OFFSET_PARAMETER,"0");
                params.put(Globals.LIMIT_PARAMETER, "100");
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(request);
    }
    private boolean hasFooter() {
        return projects.get(projects.size() - 1) instanceof Footer;
    }

}
