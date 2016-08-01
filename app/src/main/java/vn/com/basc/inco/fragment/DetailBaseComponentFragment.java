package vn.com.basc.inco.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.basc.inco.DetailBaseComponentActivity;
import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.R;
import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.model.Attachment;
import vn.com.basc.inco.model.DetailBaseComponent;
import vn.com.basc.inco.model.DetailDiscussionsComponent;
import vn.com.basc.inco.model.DetailProjectComponent;
import vn.com.basc.inco.model.DetailTaskComponent;
import vn.com.basc.inco.model.DetailTicketComponent;
import vn.com.basc.inco.model.User;
import vn.com.basc.inco.network.CustomVolleyRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailBaseComponentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailBaseComponentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailBaseComponentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters
    private String projectsID;
    private String id;
    private int type;
    private TextView mDescription;
    private TextView mName;
    private TextView mDate;
    private NetworkImageView avatar;
    private LinearLayout fileContainer;
    private CardView detailCardView;
    private ProgressBar progressBar;
    private LinearLayout parent;

    public DetailBaseComponentFragment() {
        // Required empty public constructor
    }


    public static DetailBaseComponentFragment newInstance(String projectsID, String id,int type) {
        DetailBaseComponentFragment fragment = new DetailBaseComponentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, projectsID);
        args.putString(ARG_PARAM2, id);
        args.putInt(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.projectsID = getArguments().getString(ARG_PARAM1);
            this.id = getArguments().getString(ARG_PARAM2);
            this.type = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_base_component2, container, false);
        mDescription = (TextView) view.findViewById(R.id.txt_description);
        fileContainer = (LinearLayout) view.findViewById(R.id.file_container);
        detailCardView = (CardView) view.findViewById(R.id.vc_detail);
        mName = (TextView) view.findViewById(R.id.txt_name);
        mDate  = (TextView) view.findViewById(R.id.txt_create_at);
        avatar  = (NetworkImageView) view.findViewById(R.id.im_avatar);
        avatar.setDefaultImageResId(R.mipmap.ic_account_circle_white_48dp);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progress);
        parent = (LinearLayout) view.findViewById(R.id.parent_content);
        getDetailComponent();
        return view;
    }
    private  void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        parent.setVisibility(View.GONE);
    }
    private  void hideLoading(){
        parent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void loadAvatarImage(String avatar, NetworkImageView imageView){
        String url = ((INCOApplication)getActivity().getApplication()).getAvatarUrl(avatar);
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(this.getActivity().getApplicationContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView,
                R.mipmap.ic_account_circle_white_48dp, R.mipmap.ic_account_circle_white_48dp));
        imageView.setImageUrl(url, imageLoader);
    }
    private String getMimeFromFileName(String fileName) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(fileName);
        return map.getMimeTypeFromExtension(ext);
    }
    public void startDownload(Attachment attachment) {
        DownloadManager mManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        INCOApplication  incoApplication = (INCOApplication) getActivity().getApplication();
        String url = incoApplication.getUrlApi(Globals.API_DOWNLOAD_FILE)+"?id="+attachment.getId()+"&token="+incoApplication.getTokenAccess();
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(url));
        mRqRequest.setTitle(attachment.getFile());
        mRqRequest.setMimeType(getMimeFromFileName(attachment.getFile()));
        mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if(attachment.getInfo()!= null) {
            mRqRequest.setDescription(attachment.getInfo());
        }
//        mRqRequest.setDestinationUri(Uri.parse("give your local path"));
        long idDownLoad=mManager.enqueue(mRqRequest);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public View createViewDownload(Attachment attachment){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.download_item, null);
        TextView filename = (TextView) v.findViewById(R.id.txtFileName);
        TextView info = (TextView) v.findViewById(R.id.txt_info);
        ImageButton btn = (ImageButton) v.findViewById(R.id.imgDownload);

        btn.setTag(attachment);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload((Attachment) v.getTag());
            }
        });
        filename.setText(attachment.getFile());
        info.setText(attachment.getInfo());

        return v;

    }
    private View createUserDetail(User user){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.detail_avarta, null);
        NetworkImageView imageView = (NetworkImageView) v.findViewById(R.id.im_avatar);
        TextView name = (TextView) v.findViewById(R.id.txt_name);
        imageView.setDefaultImageResId(R.drawable.ic_account_box_black_24dp);
        if(user.getPhoto()!= null && user.getPhoto().length()>0) {
            loadAvatarImage(user.getPhoto(), imageView);
        }
        name.setText(user.getName());
        return  v;
    }
    private void createDetailTicket(DetailTicketComponent.Detail detail){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.detail_tickets, null);
        TextView id = (TextView) v.findViewById(R.id.txt_id);
        TextView department = (TextView) v.findViewById(R.id.txt_department);
        TextView status = (TextView) v.findViewById(R.id.txt_status);
        TextView type = (TextView) v.findViewById(R.id.txt_type);
        TextView name = (TextView) v.findViewById(R.id.txt_name);
        NetworkImageView imageView = (NetworkImageView) v.findViewById(R.id.im_avatar);
        imageView.setDefaultImageResId(R.drawable.ic_account_box_black_24dp);
        id.setText(detail.getId());
        department.setText(detail.getDepartment());
        status.setText(detail.getStatus());
        type.setText(detail.getType());
        User user = detail.getUser();
        name.setText(detail.getUser().getName());
        if(user.getPhoto()!= null && user.getPhoto().length()>0) {
            loadAvatarImage(user.getPhoto(), imageView);
        }
        detailCardView.addView(v);
    }
    private void createDetailTask(DetailTaskComponent.Detail detail){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.detail_task, null);
        TextView id = (TextView) v.findViewById(R.id.txt_id);
        TextView label = (TextView) v.findViewById(R.id.txt_label);
        TextView status = (TextView) v.findViewById(R.id.txt_type);
       TextView priority = (TextView) v.findViewById(R.id.txt_priority);
        TextView type = (TextView) v.findViewById(R.id.txt_type);
        LinearLayout team = (LinearLayout) v.findViewById(R.id.layout_team);
          id.setText(detail.getId());
        label.setText(detail.getLabel());
        status.setText(detail.getStatus());
       priority.setText(detail.getPriority());
        type.setText(detail.getType());
        List<User> users = detail.getAssigendTo();
        if(users.size()>0){
            for (int i = 0; i<users.size();i++){
                View view = createUserDetail(users.get(i));
                team.addView(view);
            }
        }
        detailCardView.addView(v);
    }
    private void createDetailDiscussions(DetailDiscussionsComponent.Detail detail){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.detail_discussions, null);
        TextView id = (TextView) v.findViewById(R.id.txt_id);
        TextView status = (TextView) v.findViewById(R.id.txt_status);
        LinearLayout team = (LinearLayout) v.findViewById(R.id.layout_team);
        id.setText(detail.getId());

        status.setText(detail.getStatus());;
        List<User> users = detail.getAssigendTo();
        if(users.size()>0){
            for (int i = 0; i<users.size();i++){
                View view = createUserDetail(users.get(i));
                team.addView(view);
            }
        }
        detailCardView.addView(v);
    }
    private void createDetailProject(DetailProjectComponent.Detail detail){
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.detail_project, null);
        TextView id = (TextView) v.findViewById(R.id.txt_id);
        TextView status = (TextView) v.findViewById(R.id.txt_type);
        TextView type = (TextView) v.findViewById(R.id.txt_type);
        LinearLayout team = (LinearLayout) v.findViewById(R.id.layout_team);
        id.setText(detail.getId());
        status.setText(detail.getStatus());
        type.setText(detail.getType());
        List<User> users = detail.getTeam();
        if(users.size()>0){
            for (int i = 0; i<users.size();i++){
                View view = createUserDetail(users.get(i));
                team.addView(view);
            }
        }
        detailCardView.addView(v);
    }
    private void getDetailComponent()  {
        showLoading();
        String url ="";
        if(type == ComponentType.PROJECT) {
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_GET_PROJECT);
        }else if(type == ComponentType.TASK){
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_GET_TASK);
        }else if(type == ComponentType.TICKET){
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_GET_TICKET);
        }else if(type == ComponentType.DISCUSSION){
            url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_GET_DISCUSSIONS);
        }
        Log.e("kienbk1910","url:"+url);
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
                        Gson gson = new Gson();
                        DetailBaseComponent detail;
                        if(type == ComponentType.PROJECT ){
                            detail = (DetailProjectComponent) gson.fromJson(productStr,
                                    new TypeToken<DetailProjectComponent>() {
                                    }.getType());
                        }else  if(type == ComponentType.TASK){
                            detail = (DetailTaskComponent) gson.fromJson(productStr,
                                    new TypeToken<DetailTaskComponent>() {
                                    }.getType());
                        }
                        else  if(type == ComponentType.TICKET){
                            detail = (DetailTicketComponent) gson.fromJson(productStr,
                                    new TypeToken<DetailTicketComponent>() {
                                    }.getType());
                        }
                        else  if(type == ComponentType.DISCUSSION){
                            detail = (DetailDiscussionsComponent) gson.fromJson(productStr,
                                    new TypeToken<DetailDiscussionsComponent>() {
                                    }.getType());
                        }
                        else {
                             detail = (DetailBaseComponent) gson.fromJson(productStr,
                                    new TypeToken<DetailBaseComponent>() {
                                    }.getType());
                        }
                        if(detail != null){
                            mDescription.setText(Html.fromHtml(detail.getDescription()));
                            mName.setText(detail.getName());
                            mDate.setText(detail.getCreate_at());
                            loadAvatarImage(detail.getPhoto(),avatar);
                            List<Attachment> attachments = detail.getAttachments();
                            Log.e("attachments", String.valueOf(attachments.size()));
                            for (int i = 0; i<attachments.size();i++){
                                fileContainer.addView(createViewDownload(attachments.get(i)));
                            }
                            if(type == ComponentType.PROJECT){
                                createDetailProject(((DetailProjectComponent)detail).getDetail());
                            }else if (type == ComponentType.TASK){
                                createDetailTask(((DetailTaskComponent)detail).getDetail());
                            }else if (type == ComponentType.TICKET){
                                createDetailTicket(((DetailTicketComponent)detail).getDetail());
                            }else if (type == ComponentType.DISCUSSION) {
                                createDetailDiscussions(((DetailDiscussionsComponent)detail).getDetail());
                            }
                            ((DetailBaseComponentActivity)getActivity()).onSetTitle(detail.getTitle());

                        }
                        Log.d("kienbk1910",productStr);

                    }
                } catch (Exception e) {
                    Log.d("kienbk1910",e.toString());

                }
                hideLoading();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Globals.TOKEN_PARAMETER,((INCOApplication)getActivity().getApplication()).getTokenAccess());
                params.put(Globals.PROJECT_ID_PARAMETER,DetailBaseComponentFragment.this.projectsID);
                if(DetailBaseComponentFragment.this.id !=null){
                    params.put(Globals.ID_PARAMETER,DetailBaseComponentFragment.this.id);
                }
                return params;
            }
        };

        INCOApplication.getInstance().addToRequestQueue(request);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
           // mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
      //  mListener = null;
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
        void onSetTitle(String title);
    }
}
