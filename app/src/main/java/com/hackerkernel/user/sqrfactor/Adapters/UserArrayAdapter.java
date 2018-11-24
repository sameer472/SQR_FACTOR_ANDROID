package com.hackerkernel.user.sqrfactor.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.UserClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserArrayAdapter extends ArrayAdapter<UserClass> implements Filterable {
    private static final String TAG = "UserArrayAdapter";

    private final Context mContext;
    String str;
    public List<UserClass> mUsersMain;
    private final int mLayoutResourceId;

    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    String searchQuery;

    public UserArrayAdapter(Context context, int resource, String str) {
        super(context, resource);
        Log.d( "UserArrayAdapter: ","OKOKOKOKO");
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.str = str;
        mUsersMain = new ArrayList<>();

        mSp = MySharedPreferences.getInstance(context);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();


    }

    public UserArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        this.mLayoutResourceId = resource;

        mSp = MySharedPreferences.getInstance(context);
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
    }

    public int getCount() {
        return mUsersMain.size();
    }

    public UserClass getItem(int position) {
        return mUsersMain.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.user_list__layout, parent, false);
        //get Country
        UserClass user = mUsersMain.get(position);

        TextView name =  view.findViewById(R.id.name);
        name.setText(user.getName());
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        //get data from the web
                        String term = constraint.toString();
                        searchQuery = term;
                        userSearchApi(searchQuery);
                    } catch (Exception e) {
                        Log.d("HUS", "EXCEPTION " + e);
                    }
                    filterResults.values = mUsersMain;
                    filterResults.count = mUsersMain.size();
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

        return myFilter;
    }

    private class UsersApi extends AsyncTask<String, Void, ArrayList<UserClass>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onpre: ");
        }

        @Override
        protected ArrayList doInBackground(String... params) {
            try {
                //Create a new COUNTRY SEARCH url Ex "search.php?term=india"
                Log.d(TAG, "doInBackground: "+params[0]);
                String NEW_URL = ServerConstants.USER_SEARCH;

                URL url = new URL(NEW_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                HashMap<String, String> paramss = new HashMap<>();
                paramss.put("search",params[0]);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", mSp.getKey(SPConstants.API_KEY));

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                //parse JSON and store it in the list
                String jsonString = sb.toString();
                ArrayList<UserClass> mUsers = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray usersArray = jsonObject.getJSONArray("users");
                Log.d(TAG, "doInBackgroundUser: "+usersArray);
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject singleObj = usersArray.getJSONObject(i);
                    String name;
                    if(singleObj.getString("first_name")!=null && singleObj.getString("first_name").equals("null") && singleObj.getString("first_name")!=null && singleObj.getString("last_name").equals("null") ){
                        name = singleObj.getString("name");
                    }else {
                        name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
                    }
                    String profileUrl = singleObj.getString("profile");
                    String email = singleObj.getString("email");
                    String mobileNumber = singleObj.getString("mobile_number");

                    UserClass user = new UserClass(name, profileUrl, email, mobileNumber);
                    mUsers.add(user);

                }
                return mUsers;

            } catch (Exception e) {
                Log.d("HUS", "EXCEPTION " + e);
                return null;
            }
        }
    }

    private void userSearchApi(final String searchQuery) {
        Log.d(TAG, "userSearchApi: called");
        Log.d(TAG, "userSearchApi: serach query = " + searchQuery);
//        mUsers.clear();


        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.USER_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: user search response = " + response);
                List<UserClass> mUsers = new ArrayList<>();

                try {
                    JSONObject responseObject = new JSONObject(response);

                    JSONArray usersArray = responseObject.getJSONArray("users");

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject singleObj = usersArray.getJSONObject(i);

                        String id = singleObj.getString("id");
                        String name;
                        if(singleObj.getString("first_name")!=null && singleObj.getString("first_name").equals("null") && singleObj.getString("first_name")!=null && singleObj.getString("last_name").equals("null") ){
                            name = singleObj.getString("name");
                        }else {
                            name = singleObj.getString("first_name") + " " + singleObj.getString("last_name");
                        }
                        String profileUrl = singleObj.getString("profile");
                        String email = singleObj.getString("email");
                        String mobileNumber = singleObj.getString("mobile_number");

                        UserClass user = new UserClass(id, name, profileUrl, email, mobileNumber);
                        mUsers.add(user);
                        //mUsersMain.clear();
                        mUsersMain = mUsers;
                    }
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mUsersMain.clear();
                NetworkUtil.handleSimpleVolleyRequestError(error, mContext);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(mContext.getString(R.string.accept), mContext.getString(R.string.application_json));
                headers.put(mContext.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("search", searchQuery);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }


    private class UserSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String NEW_URL = URLEncoder.encode(params[0],"UTF-8");

                URL url = new URL(NEW_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);

                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", mSp.getKey(SPConstants.API_KEY));

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                }

                //return the countryList
                return sb.toString();

            } catch (Exception e) {
                Log.d("HUS", "EXCEPTION " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
