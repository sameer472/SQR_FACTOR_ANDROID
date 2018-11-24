package com.hackerkernel.user.sqrfactor.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Activities.EventDetailsActivity;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.EventClass;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hackerkernel.user.sqrfactor.Utils.MyMethods.convertDate;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {
    private static final String TAG = "EventsAdapter";

    private Context mContext;
    private List<EventClass> mEvents;

    private RequestQueue mRequestQueue;
    private ProgressDialog mPd;
    private MySharedPreferences mSp;

    String userId;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTV;
        TextView timeAgoTV;
        TextView descriptionTV;
        TextView venueTV;
        TextView eventTypeTV;

        ImageView coverIV;
      //  Button readMoreButton;
        ImageView menuThreeDotsIV;
        CardView clickableCard;

        MyViewHolder(View view) {
            super(view);
            titleTV = view.findViewById(R.id.event_title);
            descriptionTV = view.findViewById(R.id.event_desc);
            venueTV = view.findViewById(R.id.cardVenue);
            eventTypeTV = view.findViewById(R.id.cardType);
            timeAgoTV = view.findViewById(R.id.event_startTimeAgo);
            coverIV = view.findViewById(R.id.event_cover);
            menuThreeDotsIV = view.findViewById(R.id.event_3_dots);

            menuThreeDotsIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    final EventClass eventObj = mEvents.get(pos);
                    final String eventId = eventObj.getId();

                    PopupMenu popup = new PopupMenu(mContext, menuThreeDotsIV);

                    popup.getMenuInflater().inflate(R.menu.event_popup_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.event_delete: {
                                    showEventDeleteDialog(pos, eventId);
                                    break;
                                }

                                default:
                                    return true;
                            }

                            return false;
                        }
                    });

                    popup.show();
                }
            });

           // readMoreButton = view.findViewById(R.id.event_read_more);
            clickableCard = view.findViewById(R.id.clickableCard);

            //readMoreButton.setOnClickListener(this);
            clickableCard.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: called");
            int pos = getAdapterPosition();
            EventClass event = mEvents.get(pos);
            userId = event.getCreatorId();


            if (view.getId() == R.id.clickableCard) {
                String slug = event.getSlug();

                Intent i = new Intent(mContext, EventDetailsActivity.class);
                i.putExtra(BundleConstants.SLUG, slug);
                i.putExtra("userId",userId);
                mContext.startActivity(i);
            }
        }

    }

    private void showEventDeleteDialog(final int pos, final String eventId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        eventsDeleteApi(pos, eventId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void eventsDeleteApi(final int pos, final String eventId) {
        mPd.show();

        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
            mPd.dismiss();
            return;
        }
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, ServerConstants.EVENT_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPd.dismiss();

                Log.d(TAG, "onResponse: event delete response = " + response);

                try {
                    JSONObject responseObject = new JSONObject(response);
//                    JSONObject respObject = responseObject.getJSONObject("Response");

                    String responseStr = responseObject.getString("Response");
                    Toast.makeText(mContext, responseStr, Toast.LENGTH_SHORT).show();

                    mEvents.remove(pos);
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPd.dismiss();
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

                params.put("id", eventId);

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    public EventsAdapter(Context context, List<EventClass> events) {
        this.mContext = context;
        this.mEvents = events;

        mSp = MySharedPreferences.getInstance(mContext);

        mPd = new ProgressDialog(mContext);
        mPd.setMessage("Loading Please Wait..");
        mPd.setCancelable(false);
    }

    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventsAdapter.MyViewHolder(itemView);
    }

    String convertedDate;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        EventClass event = mEvents.get(position);
        String myUserId = mSp.getKey(SPConstants.USER_ID);

        holder.titleTV.setText(event.getTitle());
        try {
            convertedDate = convertDate(event.getStartTimeAgo());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timeAgoTV.setText(convertedDate);
        holder.descriptionTV.setText(event.getDescription());
        holder.venueTV.setText(event.getVenue());
        holder.eventTypeTV.setText(event.getEvent_type());
        Log.d(TAG, "onBindViewHolder: image url = " + ServerConstants.IMAGE_BASE_URL + event.getCoverUrl());

        Picasso.get().load(ServerConstants.IMAGE_BASE_URL + event.getCoverUrl()).into(holder.coverIV);

        if (myUserId.equals(event.getCreatorId())) {
            holder.menuThreeDotsIV.setVisibility(View.VISIBLE);
        }else{
            holder.menuThreeDotsIV.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

}