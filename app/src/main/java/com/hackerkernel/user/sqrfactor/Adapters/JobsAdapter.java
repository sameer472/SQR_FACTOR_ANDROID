package com.hackerkernel.user.sqrfactor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackerkernel.user.sqrfactor.Activities.JobDetailsActivity;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Parser.JsonParser;
import com.hackerkernel.user.sqrfactor.Pojo.Jobs.JobBean;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.MyMethods;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.hackerkernel.user.sqrfactor.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private Context context;
    List<JobBean> jobBeanList;
    JobBean jobBean;
    private MySharedPreferences mSp;
    int iposition;
    private RequestQueue mRequestQueue;
    String status;


    public JobsAdapter(List<JobBean> jobBeanList, Context context) {
        super();
        mSp = MySharedPreferences.getInstance(context);
        mRequestQueue = Volley.newRequestQueue(context);
        this.jobBeanList = jobBeanList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        jobBean = jobBeanList.get(position);

        if (jobBean.getUser_id().equals(mSp.getKey(SPConstants.USER_ID))) {
            holder.menuImageView.setVisibility(View.VISIBLE);
        } else {
            holder.menuImageView.setVisibility(View.GONE);
        }

        holder.job_title.setText(jobBean.getJob_title());
        try {
            holder.created_at.setText(MyMethods.convertDate(jobBean.getCreated_at()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.job_title2.setText("Job Title - " + jobBean.getJob_title());
        holder.category.setText("Category - " + jobBean.getCategory());
        holder.type_of_position.setText("Type of position - " + jobBean.getType_of_position());
        holder.work_experience.setText("work_experience - " + jobBean.getWork_experience());
        holder.salary_type.setText("Type - " + jobBean.getSalary_type());
        holder.description.setText("description - " + jobBean.getDescription());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> strings = new ArrayList<>();
                try {

                    strings.add(jobBeanList.get(position).getJob_title());
                    strings.add(jobBeanList.get(position).getCategory());
                    strings.add(jobBeanList.get(position).getType_of_position());
                    strings.add(jobBeanList.get(position).getSkillsBeanList().get(position).getSkills());
                    strings.add(jobBeanList.get(position).getEduQulBeanList().get(position).getEducational_qualification());
                    strings.add(jobBeanList.get(position).getFirm());
                    strings.add(jobBeanList.get(position).getCountry_id() + "/" + jobBeanList.get(position).getState_id() + "/" + jobBeanList.get(position).getCity_id());
                    strings.add(jobBeanList.get(position).getJob_offer_expires_on());
                    strings.add(jobBeanList.get(position).getMaximum_salary() + " to " + jobBeanList.get(position).getMinimum_salary());
                    strings.add(jobBeanList.get(position).getDescription());
                    strings.add(jobBeanList.get(position).getUser_id());//11
                    strings.add(jobBeanList.get(position).getSkillsBeanList().get(position).getUsers_job_id());//12
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jobBeanList.get(position).getUser_id().equals(mSp.getKey(SPConstants.USER_ID))) {
                    status = "true";
                } else {
                    status = "false";
                }

                Intent intent = new Intent(context, JobDetailsActivity.class);
                intent.putStringArrayListExtra("jobDetails", (ArrayList<String>) strings);
                intent.putExtra("currentUserPost", status);
                context.startActivity(intent);
            }
        });

        holder.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iposition = position;
                showPopupMenu(view);
            }
        });

    }


    @Override
    public int getItemCount() {
        return jobBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView job_title, created_at, job_title2, category, type_of_position, work_experience, salary_type, description;
        public CardView card_view;
        public ImageView menuImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            job_title = (TextView) itemView.findViewById(R.id.job_title);
            created_at = (TextView) itemView.findViewById(R.id.created_at);
            job_title2 = (TextView) itemView.findViewById(R.id.job_title2);
            category = (TextView) itemView.findViewById(R.id.category);
            type_of_position = (TextView) itemView.findViewById(R.id.type_of_position);
            work_experience = (TextView) itemView.findViewById(R.id.work_experience);
            salary_type = (TextView) itemView.findViewById(R.id.salary_type);
            description = (TextView) itemView.findViewById(R.id.description);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            menuImageView = (ImageView) itemView.findViewById(R.id.menu);
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.job_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    delete();
                    return true;

                default:
            }
            return false;
        }
    }

    public void delete() {
        Log.d("getParams: ", jobBeanList.get(iposition).getUser_id() + " == " + jobBeanList.get(iposition).getId());
        ViewUtils.showProgressBar(context);
        StringRequest request = new StringRequest(Request.Method.POST, "https://archsqr.in/api/job/jobsdelete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ViewUtils.dismissProgressBar();
                Log.d("onResponseDelete: ", response);
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ViewUtils.dismissProgressBar();

                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    try {
                        String body = new String(error.networkResponse.data, "UTF-8");
                        Log.d("handleSimpleVolley: ", body);
                        if (statusCode == 400 || statusCode == 401) {
                            //server error
                            String errorMsg = JsonParser.SimpleParser(body);
                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();

                        } else if (statusCode == 422) {
                            JSONObject object = new JSONObject(body);
                            JSONObject responseObj = object.getJSONObject("Response");
                            String message = responseObj.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorString = MyVolley.handleVolleyError(error);
                            Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        NetworkUtil.showParsingErrorAlert(context);
                    }
                } else {
                    String errorString = MyVolley.handleVolleyError(error);
                    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                Log.d("getHeaders: ", Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                header.put(context.getString(R.string.accept), context.getString(R.string.application_json));
                header.put(context.getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", jobBeanList.get(iposition).getId());
                params.put("user_id", jobBeanList.get(iposition).getUser_id());
                return params;
            }
        };

        mRequestQueue.add(request);
    }
}
