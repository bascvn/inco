package vn.com.basc.inco.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import vn.com.basc.inco.AndroidMultiPartEntity;
import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.MainActivity;
import vn.com.basc.inco.MyFileRecyclerViewAdapter;
import vn.com.basc.inco.R;
import vn.com.basc.inco.common.ComponentType;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.dummy.DummyContent;
import vn.com.basc.inco.dummy.DummyContent.DummyItem;
import vn.com.basc.inco.model.UploadFile;

import static java.lang.Integer.parseInt;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AddFileFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<UploadFile> listFile;
    MyFileRecyclerViewAdapter adapter;
    int index;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AddFileFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AddFileFragment newInstance(int columnCount) {
        AddFileFragment fragment = new AddFileFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        this.listFile  = new ArrayList<UploadFile>() ;
        adapter = new MyFileRecyclerViewAdapter(this.listFile, mListener,getContext());
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(adapter);
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
        void onListFragmentInteraction(UploadFile item);
        void onDeleteFileUpload(UploadFile item);
    }
    public void addFileUpload(UploadFile file){
        if(file == null ){
            return;
        }
        if(listFile == null) {
            listFile = new ArrayList<UploadFile>();
        }
        index++;
        file.setIndex(index);
        this.listFile.add(file);
        adapter.notifyItemInserted(this.listFile.size()-1);
        if(Integer.parseInt(file.getFile_size()) > Globals.MAX_SIZE_UPLOAD){
            upadateStatus(file.getIndex(),1,file.getFileName()+"(File too big)",null);
            return ;
        }
        new UploadFileToServer(file).execute();
    }
    public void updateListFile(int progress, int index){
        for (int i = 0; i< listFile.size();i++){
            if(this.listFile.get(i).getIndex() == index){
                this.listFile.get(i).setProgress(progress);
                Log.e("kienbk1910","progess:"+String.valueOf(progress));
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }
    public List<UploadFile> getUploadFile(){
        return this.listFile;
    }
    public void upadateStatus(int index,int status,String  fileName,String id){
        for (int i = 0; i< listFile.size();i++){
            if(this.listFile.get(i).getIndex() == index){
                this.listFile.get(i).setStatus(status);
                if(fileName != null){
                    this.listFile.get(i).setFileName(fileName);
                }
                if(id != null){
                    this.listFile.get(i).setId(id);
                }
                adapter.notifyItemChanged(i);

                break;
            }
        }
    }
    public void deleteUploadFile(UploadFile uploadFile){
        for (int i = 0; i< listFile.size();i++){
            if(this.listFile.get(i).getIndex() == uploadFile.getIndex()){
                // remove server
                this.listFile.remove(i);

                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }
    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        UploadFile uploadFile ;
        int pre_progess = 0;
         public  UploadFileToServer(UploadFile uploadFile){
             this.uploadFile = uploadFile;
        }
        long totalSize = 0;
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
           // progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            updateListFile(progress[0],uploadFile.getIndex());
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String url = ((INCOApplication) getActivity().getApplication()).getUrlApi(Globals.API_UPLOAD_FILE);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                // Adding file data to http body

                InputStreamBody inputStreamBody = new InputStreamBody(getContext().getContentResolver().openInputStream(uploadFile.getUri()),  uploadFile.getFileName());
                entity.addPart("Filedata", inputStreamBody);

                // Extra parameters if you want to pass to server
                entity.addPart(Globals.TOKEN_PARAMETER,
                        new StringBody(((INCOApplication)getActivity().getApplication()).getTokenAccess()));

                if(mColumnCount == ComponentType.TASK ){
                    entity.addPart(Globals.TYPE_BIND_PARAMATER,
                            new StringBody(ComponentType.TASK_COMMENT));
                }else  if(mColumnCount == ComponentType.TICKET ) {
                    entity.addPart(Globals.TYPE_BIND_PARAMATER,
                            new StringBody(ComponentType.TICKET_COMMENT));
                }
                else  if(mColumnCount == ComponentType.DISCUSSION ) {
                    entity.addPart(Globals.TYPE_BIND_PARAMATER,
                            new StringBody(ComponentType.DISCUSSION_COMMENT));
                }
                totalSize = parseInt(uploadFile.getFile_size());
                Log.e("kienbk1910","totalSize"+ String.valueOf(totalSize));
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("kienbk1910",result);
            try {
                JSONObject obj = new JSONObject(result);
                if(INCOResponse.isError(obj.getString(INCOResponse.STATUS_TAG))){
                   upadateStatus(this.uploadFile.getIndex(),1,null,null);
                }else{

                    JSONObject data = new JSONObject(obj.getString(INCOResponse.DATA_TAG));
                    String id = data.getString("id");
                    String fileName = data.getString("fileName");
                    Log.e("kienbk1910","fileName "+fileName);
                    upadateStatus(this.uploadFile.getIndex(),2,fileName,id);
                }
            } catch (JSONException e) {
                upadateStatus(this.uploadFile.getIndex(),1,null,null);
                return;
            }
            // showing the server response in an alert dialog
           // showAlert(result);

            super.onPostExecute(result);
        }

    }

}
