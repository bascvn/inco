package vn.com.basc.inco.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import vn.com.basc.inco.R;
import vn.com.basc.inco.fragment.AddFileFragment;
import vn.com.basc.inco.fragment.AddFileFragment.OnListFragmentInteractionListener;
import vn.com.basc.inco.dummy.DummyContent.DummyItem;
import vn.com.basc.inco.model.UploadFile;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileRecyclerViewAdapter.ViewHolder> {

    private final List<UploadFile> mValues;
    private final OnListFragmentInteractionListener mListener;

    private  AddFileFragment addFileFragment;

    public MyFileRecyclerViewAdapter(List<UploadFile> items, OnListFragmentInteractionListener listener, AddFileFragment context) {
        mValues = items;
        mListener = listener;
        this.addFileFragment = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.up_file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mFileName.setText(mValues.get(position).getFileName());
        if(holder.mItem.getStatus() == 1){
            holder.mFileName.setTextColor(  addFileFragment.getResources().getColor(R.color.colorError));
        }else if(holder.mItem.getStatus() == 2){
            holder.mFileName.setTextColor(  addFileFragment.getResources().getColor(R.color.colorPrimary));
        }
        if(holder.mItem.getStatus() == 2){
            holder.mInfo.setVisibility(View.VISIBLE);
            holder.mInfo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    holder.mItem.setInfo(s.toString());
                }
            });
        }else {
            holder.mInfo.setVisibility(View.INVISIBLE);
        }

        holder.mProgressBar.setProgress(mValues.get(position).getProgress());
        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mListener.onDeleteFileUpload(holder.mItem);
                addFileFragment.deleteUploadFile(holder.mItem);
            }
        });
       // holder.mContentView.setText(mValues.get(position).content);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFileName;
        public final ProgressBar mProgressBar;
        public final ImageButton mImageButton;
        public final EditText mInfo;
        public UploadFile mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFileName = (TextView) view.findViewById(R.id.txt_filename);
             mProgressBar = (ProgressBar) view.findViewById(R.id.progress_upload);
            mImageButton = (ImageButton)view.findViewById(R.id.imb_remove);
            mInfo = (EditText) view.findViewById(R.id.txt_info);
        }

        @Override
        public String toString() {
            return super.toString() + " '" +  "'";
        }
    }
}
