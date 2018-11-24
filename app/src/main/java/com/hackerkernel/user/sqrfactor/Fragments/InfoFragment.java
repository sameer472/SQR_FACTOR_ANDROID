package com.hackerkernel.user.sqrfactor.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.user.sqrfactor.Activities.CompetitionDetailActivity;
import com.hackerkernel.user.sqrfactor.Adapters.AttachmentsAdapter;
import com.hackerkernel.user.sqrfactor.Adapters.JuryAdapter;
import com.hackerkernel.user.sqrfactor.Adapters.PartnersAdapter;
import com.hackerkernel.user.sqrfactor.Adapters.PrizesAdapter;
import com.hackerkernel.user.sqrfactor.Constants.BundleConstants;
import com.hackerkernel.user.sqrfactor.Constants.Constants;
import com.hackerkernel.user.sqrfactor.Constants.SPConstants;
import com.hackerkernel.user.sqrfactor.Constants.ServerConstants;
import com.hackerkernel.user.sqrfactor.Network.MyVolley;
import com.hackerkernel.user.sqrfactor.Pojo.AttachmentClass;
import com.hackerkernel.user.sqrfactor.Pojo.JuryClass;
import com.hackerkernel.user.sqrfactor.Pojo.PartnerClass;
import com.hackerkernel.user.sqrfactor.Pojo.PrizeClass;
import com.hackerkernel.user.sqrfactor.PostContentHandlerParserForCompetition;
import com.hackerkernel.user.sqrfactor.R;
import com.hackerkernel.user.sqrfactor.Storage.MySharedPreferences;
import com.hackerkernel.user.sqrfactor.Utils.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    private Button mRegisterButton;
    private RecyclerView mPrizesRecyclerView;
    private RecyclerView mJuryRecyclerView;
    private RecyclerView mPartnersRecyclerView;
    private RecyclerView mAttachmentsRecyclerview;
    private TextView mEligibilityCriteriaTV;
    private TextView fullPostDescription;

    private List<PrizeClass> mPrizes;
    private PrizesAdapter mPrizesAdapter;

    private List<JuryClass> mJuryList;
    private JuryAdapter mJuryAdapter;

    private List<PartnerClass> mPartners;
    private PartnersAdapter mPartnersAdapter;

    private List<AttachmentClass> mAttachments;
    private AttachmentsAdapter mAttachmentsAdapter;

    private LinearLayout mWebView;

    private TextView ebr;
    private TextView ar;
    private TextView lmr;

    private TextView mEbrStaticTV;
    private TextView mArStaticTV;
    private TextView mLmrStaticTV;

    private ProgressBar mPb;
    private LinearLayout mContentLayout;
    private MySharedPreferences mSp;
    private RequestQueue mRequestQueue;
    private float m_downX;

    private LinearLayout mRtLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_info, container, false);

        fullPostDescription=view.findViewById(R.id.fullPostDescription);
        mWebView = view.findViewById(R.id.webView);
        mPrizesRecyclerView = view.findViewById(R.id.info_prize_rv);
        mJuryRecyclerView = view.findViewById(R.id.info_jury_rv);
        mPartnersRecyclerView = view.findViewById(R.id.info_partner_rv);
        mAttachmentsRecyclerview = view.findViewById(R.id.info_attachments_rv);
        mRegisterButton = view.findViewById(R.id.register_btn);
        mEligibilityCriteriaTV = view.findViewById(R.id.info_eligibility_criteria);

        ebr = view.findViewById(R.id.set_ebr);
        ar = view.findViewById(R.id.set_ar);
        lmr = view.findViewById(R.id.set_lmr);

        mEbrStaticTV = view.findViewById(R.id.info_ebr_tv);
        mArStaticTV = view.findViewById(R.id.info_ar_tv);
        mLmrStaticTV = view.findViewById(R.id.info_lmr_tv);

        mRtLayout = view.findViewById(R.id.info_rt_layout);

        mPrizesRecyclerView.setHasFixedSize(true);
        mPrizesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPrizesRecyclerView.setNestedScrollingEnabled(false);

        mPrizes = new ArrayList<>();
        mPrizesAdapter = new PrizesAdapter(getActivity(), mPrizes);
        mPrizesRecyclerView.setAdapter(mPrizesAdapter);

        mJuryRecyclerView.setHasFixedSize(true);
        mJuryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mJuryRecyclerView.setNestedScrollingEnabled(false);

        mJuryList = new ArrayList<>();
        mJuryAdapter = new JuryAdapter(getActivity(), mJuryList);
        mJuryRecyclerView.setAdapter(mJuryAdapter);

        mPartnersRecyclerView.setHasFixedSize(true);
        mPartnersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPartnersRecyclerView.setNestedScrollingEnabled(false);

        mPartners = new ArrayList<>();
        mPartnersAdapter = new PartnersAdapter(getActivity(), mPartners);
        mPartnersRecyclerView.setAdapter(mPartnersAdapter);


        mAttachmentsRecyclerview.setHasFixedSize(true);
        mAttachmentsRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAttachmentsRecyclerview.setNestedScrollingEnabled(false);

        mAttachments = new ArrayList<>();
        mAttachmentsAdapter = new AttachmentsAdapter(getActivity(), mAttachments);
        mAttachmentsRecyclerview.setAdapter(mAttachmentsAdapter);

        mPb = view.findViewById(R.id.pb);
        mContentLayout = view.findViewById(R.id.content_layout);
        mSp = MySharedPreferences.getInstance(getActivity());
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        Intent i = getActivity().getIntent();
        String slug = i.getStringExtra(BundleConstants.SLUG);

        competitionDetailApi(slug);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CompetitionDetailActivity)getActivity()).participateCheckApi();
            }
        });

        return view;
    }

    private void competitionDetailApi(String slug) {
        mPb.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (!NetworkUtil.isNetworkAvailable()) {
            Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
            mPb.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            return;
        }

        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        Log.d(TAG, "SHIVANI: url = " + ServerConstants.COMPETITION_DETAIL + slug);
        StringRequest request = new StringRequest(Request.Method.GET, ServerConstants.COMPETITION_DETAIL + slug, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: competition detail response = " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject competitionObj = responseObject.getJSONObject("competition");

                    String eligibilityCriteria = competitionObj.getString("eligibility_criteria");
                    mEligibilityCriteriaTV.setText(eligibilityCriteria);

                    String brief = competitionObj.getString("brief");
                    Log.d("brief: ", brief);
                    initWebView(brief);
                    JSONArray prizesArray = competitionObj.getJSONArray("users_competitions_award");
                    Log.d(TAG, "prizesArray: " + prizesArray.toString());

                    if (prizesArray.length() > 0) {

                        for (int i = 0; i < prizesArray.length(); i++) {
                            JSONObject singleObject = prizesArray.getJSONObject(i);

                            String id = singleObject.getString("id");
                            String type = singleObject.getString("award_type");
                            String amount = singleObject.getString("award_amount");
                            String currency = singleObject.getString("award_currency");
                            String extra = singleObject.getString("award_extra");

                            if (type.equals("1_prize")) {
                                type = "1st Prize - " + amount + " " + currency;

                            } else if (type.equals("2_prize")) {
                                type = "2nd Prize - " + amount + " " + currency;

                            } else if (type.equals("3_prize")) {
                                type = "3rd Prize - " + amount + " " + currency;
                            }

                            PrizeClass prize = new PrizeClass(id, type, amount, currency, extra);
                            mPrizes.add(prize);
                            mPrizesAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(getActivity(), "No prize found", Toast.LENGTH_SHORT).show();
                    }

                    JSONArray juryArray = competitionObj.getJSONArray("user_competition_jury");
                    Log.d("juryArray: ", juryArray + "");

                    if (juryArray.length() != 0) {

                        for (int i = 0; i < juryArray.length(); i++) {
                            JSONObject singleObject = juryArray.getJSONObject(i);
                            if (singleObject.has("user")) {
                                Log.d(TAG, "singleObject.: " + singleObject.getString("user"));
                                if (!singleObject.getString("user").toString().equals("null")) {
                                    JSONObject userObject = singleObject.getJSONObject("user");
                                    Log.d(TAG, "userObject: " + userObject.toString());


                                    String id = singleObject.getString("id");
                                    String fullName = singleObject.getString("jury_fullname");
                                    String imageUrl = userObject.getString("profile");


                                    JuryClass jury = new JuryClass(id, fullName, imageUrl);
                                    mJuryList.add(jury);
                                    mJuryAdapter.notifyDataSetChanged();
                                }


                            }


                        }

                    } else {
                        Toast.makeText(getActivity(), "No Jury", Toast.LENGTH_SHORT).show();
                    }

                    JSONArray partnersArray = competitionObj.getJSONArray("user_competition_partner");

                    if (partnersArray.length() > 0) {

                        for (int i = 0; i < partnersArray.length(); i++) {
                            JSONObject singleObject = partnersArray.getJSONObject(i);

                            if (singleObject.has("user")) {
                                Log.d(TAG, "singleObject.: " + singleObject.getString("user"));
                                if (!singleObject.getString("user").toString().equals("null")) {

                                    JSONObject userObject = singleObject.getJSONObject("user");

                                    String id = singleObject.getString("id");
                                    String name = singleObject.getString("partner_name");
                                    String imageUrl = userObject.getString("profile");


                                    PartnerClass partner = new PartnerClass(id, name, imageUrl);
                                    mPartners.add(partner);
                                    mPartnersAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), "No Report found", Toast.LENGTH_SHORT).show();
                    }

                    JSONArray attachmentsArray = competitionObj.getJSONArray("user_competition_attachment");

                    if (attachmentsArray.length() > 0) {

                        for (int i = 0; i < attachmentsArray.length(); i++) {
                            JSONObject singleObject = attachmentsArray.getJSONObject(i);

                            String id = singleObject.getString("id");
                            String attachmentUrl = singleObject.getString("attach_documents");


                            AttachmentClass attachment = new AttachmentClass(id, attachmentUrl);
                            mAttachments.add(attachment);
                            mAttachmentsAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(getActivity(), "No Report found", Toast.LENGTH_SHORT).show();
                    }

                    JSONArray registrationType = competitionObj.getJSONArray("user_competition_registration_type");

                    if(registrationType.length() > 0) {
                        for (int i = 0; i < registrationType.length(); i++) {
                            JSONObject singleObject = registrationType.getJSONObject(i);

                            String rt = singleObject.getString("registration_type");
                            String sd = singleObject.getString("start_date");
                            String ed = singleObject.getString("end_date");
                            String cur = singleObject.getString("currency");
                            String amt = singleObject.getString("amount");
                            String typ = singleObject.getString("type");

                            if(rt.equals("early_bird")){
                                mEbrStaticTV.setVisibility(View.VISIBLE);

                                if(typ.equals("Indian")){
                                    ebr.setText("From " + sd + " to " + ed + "\n" + cur + " " +amt + "  (" + typ +")");

                                } else if(typ.equals("International")){
                                    ebr.append("\n" + cur + " " +amt + "  (" + typ + ")" );
                                }
                            }
                            else if(rt.equals("advance")){
                                mArStaticTV.setVisibility(View.VISIBLE);

                                if(typ.equals("Indian")){
                                    ar.setText("From " + sd + " to " + ed + "\n" + cur + " " +amt + "  (" + typ +")");

                                } else if(typ.equals("International")){
                                    ar.append("\n" + cur + " " +amt + "  (" + typ + ")" );
                                }
                            }
                            else if(rt.equals("last_minute")){
                                mLmrStaticTV.setVisibility(View.VISIBLE);

                                if(typ.equals("Indian")){
                                    lmr.setText("From " + sd + " to " + ed + "\n" + cur + " " +amt + "  (" + typ +")");

                                } else if(typ.equals("International")){
                                    lmr.append("\n" + cur + " " +amt + "  (" + typ + ")" );
                                }
                            }
                        }
                    } else {
                        mRtLayout.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPb.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);

                Activity activity = getActivity();
                if(activity != null){
                    NetworkUtil.handleSimpleVolleyRequestError(error, activity);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();
                headers.put(getString(R.string.accept), getString(R.string.application_json));
                headers.put(getString(R.string.authorization), Constants.AUTHORIZATION_HEADER + mSp.getKey(SPConstants.API_KEY));
                return headers;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }

    private void initWebView(final String brief) {

//        mWebView.loadData(brief, "text/html", null);
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setDisplayZoomControls(true);
//        mWebView.setWebChromeClient(new MyWebChromeClient());
//        mWebView.getSettings().setJavaScriptEnabled(true);


        Log.v("Brief@info",brief);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try { Thread.sleep(100); }
                catch (InterruptedException e) {}

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        PostContentHandlerParserForCompetition postContentHandlerParserForCompetition=new PostContentHandlerParserForCompetition(getApplicationContext(),brief,mWebView,fullPostDescription);
                        postContentHandlerParserForCompetition.setContentToView();
                    }
                });
            }
        };
        thread.start();

    }
//
//    private void runOnUiThread(Runnable runnable) {
//
//    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        private MyWebChromeClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            Intent i = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
            getActivity().startActivity(i);
            return true;
        }

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
//
//    private void initWebView() {
//        mWebView.setWebChromeClient(new MyWebChromeClient(getActivity()));
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                if (url.endsWith(".pdf")) {
//                    getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
//                    return true;
//                }
//                if ((url != null) && (url.startsWith("whatsapp://"))) {
//                    view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
//                    return true;
//                } else {
//                    mWebView.loadUrl(url);
//                }
//
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//
//            }
//        });
//        mWebView.setInitialScale(1);
//        mWebView.clearCache(true);
//        mWebView.clearHistory();
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setHorizontalScrollBarEnabled(false);
//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getPointerCount() > 1) {
//                    return true;
//                }
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        m_downX = event.getX();
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP: {
//                        event.setLocation(m_downX, event.getY());
//                    }
//                    break;
//                }
//
//                return false;
//            }
//        });
//    }

}
