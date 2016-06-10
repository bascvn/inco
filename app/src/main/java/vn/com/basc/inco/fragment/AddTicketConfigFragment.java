package vn.com.basc.inco.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.MainActivity;
import vn.com.basc.inco.R;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.common.SpinnerItem;
import vn.com.basc.inco.common.TicketForm;
import vn.com.basc.inco.model.ProjectItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTicketConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTicketConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTicketConfigFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Spinner spinnerDeparments;
    private Spinner spinnerType;
    private Spinner spinnerStatus;
    private RadioGroup userLayout;
    List<SpinnerItem> listDepaments;
    List<SpinnerItem> listTypes;
    List<SpinnerItem> listStatus;
    private LinearLayout notifyUser;
    public AddTicketConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddTicketConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTicketConfigFragment newInstance(String param1) {
        AddTicketConfigFragment fragment = new AddTicketConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            loadTicketForm(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view  = inflater.inflate(R.layout.fragment_add_ticket_config, container, false);
        spinnerDeparments  = (Spinner) view.findViewById(R.id.spinner_deparments);
        spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
        spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        userLayout = (RadioGroup) view.findViewById(R.id.userContainer);
        notifyUser = (LinearLayout) view.findViewById(R.id.notifyUser);
        return view;
    }
    public String getDeparmentId(){
        SpinnerItem spinnerItem = (SpinnerItem) spinnerDeparments.getSelectedItem();
        return spinnerItem.getId();
    }
    public String getTypeTicket(){
        SpinnerItem spinnerItem = (SpinnerItem) spinnerType.getSelectedItem();
        return spinnerItem.getId();
    }
    public String getTypeStatus(){
        SpinnerItem spinnerItem = (SpinnerItem) spinnerStatus.getSelectedItem();
        return spinnerItem.getId();
    }
    public String getUserCreate(){
        RadioButton  user = (RadioButton) userLayout.findViewById(userLayout.getCheckedRadioButtonId());
        return (String) user.getTag();
    }
    public List<String> getUserNotify(){
        List<String > users = new ArrayList<String>();
        for(int i=0;i<notifyUser.getChildCount();i++)
        {

            View v =  (View)notifyUser.getChildAt(i);
            if (v instanceof CheckBox) {
                CheckBox b = (CheckBox) v;
                if(b.isChecked()){
                    users.add(v.getTag().toString());
                }
            }
        }
        return users;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public List<SpinnerItem> getListSpinnerItems(HashMap<String,String> map){
        List<SpinnerItem> result = new ArrayList<SpinnerItem>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.e("kienbk1910",entry.getValue());
            result.add(new SpinnerItem(entry.getKey(), entry.getValue()));
        }
        return  result;
    }
    public int getIndexof(List<SpinnerItem> list, String id){
        for (int i = 0; i< list.size();i++){
            if(list.get(i).getValue().equalsIgnoreCase(id) ){
                return i;
            }
        }
        return 0;
    }
    public void loadTicketForm(final String project_id){

        //  RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        String url = ((INCOApplication)getActivity().getApplication()).getUrlApi(Globals.API_GET_TICKET_FORM);
        Log.d("response","url:"+url );
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login",response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(INCOResponse.isError(obj.getString(INCOResponse.STATUS_TAG))){

                    }else{

                        String  data = obj.getString(INCOResponse.DATA_TAG);

                        Gson gson = new Gson();
                        TicketForm ticketForm = (TicketForm) gson.fromJson(data,
                                new TypeToken<TicketForm>() {
                                }.getType());
                        listDepaments = getListSpinnerItems(ticketForm.deparments);
                        ArrayAdapter<SpinnerItem> spinnerAdapter = new ArrayAdapter<SpinnerItem>(getActivity(), android.R.layout.simple_spinner_item,listDepaments);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDeparments.setAdapter(spinnerAdapter);

                        listTypes = getListSpinnerItems(ticketForm.ticketsTypes);
                        ArrayAdapter<SpinnerItem> listAdapter = new ArrayAdapter<SpinnerItem>(getActivity(), android.R.layout.simple_spinner_item,listTypes);
                        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(listAdapter);
                        spinnerType.setSelection(getIndexof(listTypes,ticketForm.ticketsTypesDefault));

                        listStatus = getListSpinnerItems(ticketForm.ticketsStatus);
                        ArrayAdapter<SpinnerItem> statusAdapter = new ArrayAdapter<SpinnerItem>(getActivity(), android.R.layout.simple_spinner_item,listStatus);
                        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerStatus.setAdapter(statusAdapter);
                        spinnerStatus.setSelection(getIndexof(listStatus,ticketForm.ticketsStatusDefault));
                        buildLayoutUser(ticketForm.users);
                        buildNotifyLayout(ticketForm.notify);
                    }
                } catch (JSONException e) {

                    return;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              /*  NetworkResponse response = error.networkResponse;
                error.hashCode();
                Log.d("response",String.valueOf(response.statusCode));
                if(response != null && response.data != null){
                    Log.d("response",String.valueOf(response.statusCode));
                    switch(response.statusCode){

                        case 400:

                            break;
                    }
                    //Additional cases
                }*/


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,((INCOApplication)getActivity().getApplication()).getTokenAccess());
                params.put(Globals.PROJECT_ID_PARAMETER,AddTicketConfigFragment.this.mParam1);
                return params;
            }
        };
       /* int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);*/
        // queue.add(request);
        INCOApplication.getInstance().addToRequestQueue(request);
    }
    private void buildNotifyLayout(LinkedHashMap<String,LinkedHashMap<String,String>> user){
        for (Map.Entry<String, LinkedHashMap<String,String>> entry : user.entrySet()) {
            TextView  title = new TextView(getContext());
            title.setText(entry.getKey());
            title.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            HashMap<String,String> userGroup = entry.getValue();
            notifyUser.addView(title);
            for (Map.Entry<String,String> entryUser : userGroup.entrySet()){
                CheckBox userRadio = new CheckBox(getContext());
                userRadio.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                userRadio.setText(entryUser.getValue());
                userRadio.setTag(entryUser.getKey());
                notifyUser.addView(userRadio);
            }
        }
    }
    private void buildLayoutUser(LinkedHashMap<String,LinkedHashMap<String,String>> user){
         INCOApplication incoApplication = (INCOApplication) getActivity().getApplication();
         String currentUser = incoApplication.getUserInfo().getId();
        RadioButton current = null;
        for (Map.Entry<String, LinkedHashMap<String,String>> entry : user.entrySet()) {
            TextView  title = new TextView(getContext());
            title.setText(entry.getKey());
            title.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
            HashMap<String,String> userGroup = entry.getValue();
            userLayout.addView(title);
            for (Map.Entry<String,String> entryUser : userGroup.entrySet()){
                RadioButton userRadio = new RadioButton(getContext());
                userRadio.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                userRadio.setText(entryUser.getValue());
                userRadio.setTag(entryUser.getKey());
                if(entryUser.getKey().equalsIgnoreCase(currentUser)){
                    current = userRadio;

                }
                userLayout.addView(userRadio);
            }


        }
        if(current != null){
            current.setChecked(true);
        }
    }
}
