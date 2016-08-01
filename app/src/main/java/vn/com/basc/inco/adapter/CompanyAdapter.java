package vn.com.basc.inco.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vn.com.basc.inco.INCOApplication;
import vn.com.basc.inco.R;
import vn.com.basc.inco.common.Globals;
import vn.com.basc.inco.common.INCOResponse;
import vn.com.basc.inco.model.Company;
import vn.com.basc.inco.network.CustomVolleyRequest;

/**
 * Created by SONY on 7/3/2016.
 */
public class CompanyAdapter extends ArrayAdapter<Company> {
    List<Company> companies;
    Context context;
    int resource;

    public CompanyAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.companies = new ArrayList<Company>();
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Company getItem(int position) {
        return companies.get(position);
    }
    private void loadLogoImage(String avatar,NetworkImageView imageView){
        String url = INCOApplication.getInstance().getUrlLogo(avatar);
        ImageLoader imageLoader = CustomVolleyRequest.getInstance(context)
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        imageView.setImageUrl(url, imageLoader);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }
        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.img_logo);
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        loadLogoImage(companies.get(position).getLogo(),networkImageView);
        ((TextView) convertView.findViewById(R.id.txt_client_code)).setText(companies.get(position).getClientCode());
        ((TextView) convertView.findViewById(R.id.txt_comany_name)).setText(companies.get(position).getClientName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    URL url = null;
                    try {
                        Map<String,Object> params = new LinkedHashMap<>();
                        params.put(Globals.SEARCH_WORD, constraint.toString());

                        StringBuilder postData = new StringBuilder();
                        for (Map.Entry<String,Object> param : params.entrySet()) {
                            if (postData.length() != 0) postData.append('&');
                            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                            postData.append('=');
                            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                        }
                        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                        url = new URL(INCOApplication.getInstance().getUrlGetCompany());
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                        conn.setDoOutput(true);
                         conn.getOutputStream().write(postDataBytes);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null)
                        {
                            sb.append(line + "\n");
                        }
                        Log.e("new","len:"+sb.toString().length());
                        Log.e("new","json:"+sb.toString());
                        JSONObject obj = new JSONObject(sb.toString());
                        if (INCOResponse.isError(obj.getString(INCOResponse.STATUS_TAG))) {
                            Log.e("login", "STATUS_TAG:");
                        } else {
                            String productStr = obj.getString(INCOResponse.DATA_TAG);
                            Log.d("kienbk1910", productStr);
                            Gson gson = new Gson();
                            List<Company> data = (List<Company>) gson.fromJson(productStr,
                                    new TypeToken<List<Company>>() {
                                    }.getType());
                           companies.clear();
                            companies.addAll(data);
                        }
                        filterResults.values = companies;
                        filterResults.count = companies.size();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
