package fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.votocast.votocast.VC_MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import class_adapter.Constant;
import class_adapter.HomeTestAdapter;
import class_adapter.ProgressHUD;
import cz.msebera.android.httpclient.Header;
import db.Home;
import db.Reports;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestHomeFragment_old extends Fragment {

    @BindView(R2.id.mlistView)ListView mlistView;
    @BindView(R2.id.llTestHomeFragmentCompainName)LinearLayout llTestHomeFragmentCompainName;
    @BindView(R2.id.tvHomeMessage)TextView tvHomeMessage;
    @BindView(R2.id.horizontalScrollHome)HorizontalScrollView horizontalScrollHome;
    @BindView(R2.id.rlHomeEmptyState)RelativeLayout rlHomeEmptyState;
//    @BindView(R2.id.viewExtraSpaceHome)View viewExtraSpaceHome;

    ArrayList<Home> dblist;
    ArrayList<Reports> reportList;
    HomeTestAdapter myAdapter;

    int loadMore = 1;
    private String regid;

    //--- load more ------
    private int preLast = 0;
//    String token;

    SwipeRefreshLayout swipeContainerHome;
    int bottomCampCount = 0;
//    Activity getActivity();


    public TestHomeFragment_old() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test_home, container, false);
        ButterKnife.bind(this, v);

        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Home");
        t.send(new HitBuilders.AppViewBuilder().build());

        ((VC_MainActivity)  getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((VC_MainActivity)  getActivity()).getSupportActionBar().show();

        Toolbar toolbar = (Toolbar)  getActivity().findViewById(R2.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R2.id.toolbarLogo);
        iv.setVisibility(View.VISIBLE);
//        RelativeLayout rlToolbar = (RelativeLayout) toolbar.findViewById(R2.id.rlToolbar);
//        rlToolbar.setVisibility(View.GONE);
        TextView toolText = (TextView) toolbar.findViewById(R2.id.toolbarTitle);
        toolText.setVisibility(View.GONE);
        toolText.setText("");
        ImageView toolbar_back_button = (ImageView) toolbar.findViewById(R2.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.GONE);

        setHasOptionsMenu(true);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        swipeContainerHome = (SwipeRefreshLayout) v.findViewById(R2.id.swipeContainerHomeTest);
        bottomCampCount = 0;

        dblist = new ArrayList<Home>();
        reportList = new ArrayList<Reports>();
        loadMore = 1;
//        new getHomeVideo(loadMore).execute();
        new setDeviceData().execute();
        getHomeData(loadMore);

        mlistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                JCVideoPlayer.setOnScrollStop();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
//                        Log.d("LastItem", "Last");
                        preLast = lastItem;
                        loadMore = loadMore + 1;
//                        new getHomeVideo(loadMore).execute();
                        getHomeData(loadMore);
                    }
                }
            }
        });

        //-----------swipe to refresh --------
        swipeContainerHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                preLast = 0;
                loadMore = 1;
                dblist = new ArrayList<Home>();
                reportList = new ArrayList<Reports>();
//                new getHomeVideo(loadMore).execute();
                new getHomeVideoRefresh().execute();

            }
        });
        // Configure the refreshing colors
        swipeContainerHome.setColorSchemeResources(android.R.color.holo_orange_dark);
        setRetainInstance(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
//        new setDeviceData().execute();
//        mVideoPlayerManager.resetMediaPlayer();
//        getActivity() = getActivity();

    }

    class getHomeVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int count;
        JSONObject camp;
        String campId;

        public getHomeVideo(int leaderboardCount) {
            count = leaderboardCount;
//            Log.i("count",count+"");
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", "1"));
            pair.add(new BasicNameValuePair("page", count + ""));
            pair.add(new BasicNameValuePair("limit", "10"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_home_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        JSONArray leaderArr = jo.getJSONArray("videos");

                                        reportList = new ArrayList<Reports>();
                                        JSONArray reportArr = jo.getJSONArray("flags");
                                        for (int i = 0; i < reportArr.length(); i++) {
                                            JSONObject reportObj = reportArr.getJSONObject(i);
                                            JSONObject catObj = reportObj.getJSONObject("VideoReportCategory");

                                            reportList.add(new Reports(catObj.getString("id"), catObj.getString("title"), catObj.getString("description")));
                                        }

                                        if(loadMore == 1 && leaderArr.length() == 0){
                                            tvHomeMessage.setText(jo.getString("no_videos_message"));
                                            tvHomeMessage.setVisibility(View.VISIBLE);
                                            rlHomeEmptyState.setVisibility(View.VISIBLE);
                                            swipeContainerHome.setVisibility(View.GONE);
                                        }else
                                        {
                                            rlHomeEmptyState.setVisibility(View.GONE);
                                            tvHomeMessage.setVisibility(View.GONE);
                                            swipeContainerHome.setVisibility(View.VISIBLE);
                                        }

                                        for (int i = 0; i < leaderArr.length(); i++) {
                                            JSONObject joLeader = leaderArr.getJSONObject(i);

                                            JSONObject videoObj = joLeader.getJSONObject("Video");
                                            JSONObject campObj = joLeader.getJSONObject("Campaign");
                                            JSONObject userObj = joLeader.getJSONObject("User");
                                            JSONObject blankObj = joLeader.getJSONObject("0");

//                                            dblist.add(new Home(getActivity(),i, Integer.parseInt(videoObj.getString("id")), campObj.getString("id"), userObj.getString("id"), campObj.getString("title"), userObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers"), videoObj.getString("photo_path"), videoObj.getString("video_path"), blankObj.getString("likes"), videoObj.getString("views"), videoObj.getString("shares"), videoObj.getString("description"), blankObj.getString("didfollowed"), blankObj.getString("didliked"), videoObj.getString("rank"), videoObj.getString("is_winner"), blankObj.getString("cnt_comments"), videoObj.getString("created_on"),reportList));
                                        }

                                        myAdapter = new HomeTestAdapter(getActivity(), dblist);

                                        if (loadMore > 1) {

                                            int index = mlistView.getFirstVisiblePosition();
                                            View v = mlistView.getChildAt(0);
                                            int top = (v == null) ? 0 : v.getTop();

                                            // notify dataset changed or re-assign adapter here
                                            mlistView.setAdapter(myAdapter);
                                            myAdapter.notifyDataSetChanged();
                                            // restore the position of listview
                                            mlistView.setSelectionFromTop(index, top);

                                        } else
                                            mlistView.setAdapter(myAdapter);

                                        setListViewHeightBasedOnChildren(mlistView);

                                        if (loadMore == 1) {
                                            swipeContainerHome.setRefreshing(false);
                                            JSONArray campNameArr = jo.getJSONArray("campaigns_list");

                                            if(campNameArr.length() > 0){
                                                horizontalScrollHome.setVisibility(View.VISIBLE);
//                                                viewExtraSpaceHome.setVisibility(View.VISIBLE);
                                                if (bottomCampCount != campNameArr.length()) {
                                                    bottomCampCount = campNameArr.length();

                                                    for (int i = 0; i < campNameArr.length(); i++) {
                                                        JSONObject campNameObj = campNameArr.getJSONObject(i);
                                                        camp = campNameObj.getJSONObject("Campaign");
                                                        TextView valueTV = new TextView(getActivity());
                                                        valueTV.setId(Integer.parseInt(camp.getString("id")));
                                                        valueTV.setText(camp.getString("title"));
                                                        llTestHomeFragmentCompainName.setWeightSum((float) campNameArr.length());
                                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                                        valueTV.setLayoutParams(params);
                                                        valueTV.setTextSize(15);
                                                        valueTV.setPadding(35, 0, 40, 0);
                                                        valueTV.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                                                        valueTV.setGravity(Gravity.CENTER);
                                                        Log.i("id1", camp.getString("id"));
                                                        campId = camp.getString("id");
                                                        valueTV.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Log.i("view", view.getId() + "");
                                                                CampaignFragment fragment = new CampaignFragment();
                                                                Bundle b1 = new Bundle();
                                                                b1.putString("campId", String.valueOf(view.getId()));
                                                                fragment.setArguments(b1);
                                                                FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                                FragmentTransaction ft = fm.beginTransaction();
                                                                ft.replace(R2.id.frame_container, fragment);
                                                                ft.addToBackStack(fragment.getClass().getName());
                                                                ft.commit();
                                                            }
                                                        });
                                                        ((LinearLayout) llTestHomeFragmentCompainName).addView(valueTV);
                                                    }
                                                    if (campNameArr.length() > 2)
                                                    {
                                                        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                        params.gravity = Gravity.LEFT;
                                                        llTestHomeFragmentCompainName.setLayoutParams(params);
                                                    }
                                                }
                                            }else {
                                                horizontalScrollHome.setVisibility(View.GONE);
//                                                viewExtraSpaceHome.setVisibility(View.GONE);
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setDeviceData extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");

            String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String osVersion = Build.VERSION.RELEASE;
            int currentVersion = getAppVersion(getActivity());

            regid = FirebaseInstanceId.getInstance().getToken();

            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("os_type", "android"));
            pair.add(new BasicNameValuePair("os_version", osVersion));
            pair.add(new BasicNameValuePair("device_token", regid));
            pair.add(new BasicNameValuePair("device_id", deviceId));
            pair.add(new BasicNameValuePair("app_version", String.valueOf(currentVersion)));


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.add_device_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
//                                if (mProgressHUD.isShowing() && mProgressHUD != null)
//                                    mProgressHUD.dismiss();
//                            }
//                        });
                    } else {
//                        if (mProgressHUD.isShowing() && mProgressHUD != null)
//                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    class getHomeVideoRefresh extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", "1"));
            pair.add(new BasicNameValuePair("page", "1"));
            pair.add(new BasicNameValuePair("limit", "10"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_home_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        JSONArray leaderArr = jo.getJSONArray("videos");

                                        JSONArray reportArr = jo.getJSONArray("flags");
                                        for (int i = 0; i < reportArr.length(); i++) {
                                            JSONObject reportObj = reportArr.getJSONObject(i);
                                            JSONObject catObj = reportObj.getJSONObject("VideoReportCategory");

                                            reportList.add(new Reports(catObj.getString("id"), catObj.getString("title"), catObj.getString("description")));
                                        }

                                        if(leaderArr.length() == 0){
                                            rlHomeEmptyState.setVisibility(View.VISIBLE);
                                            tvHomeMessage.setVisibility(View.VISIBLE);
                                            swipeContainerHome.setVisibility(View.GONE);
                                        }else
                                        {
                                            rlHomeEmptyState.setVisibility(View.GONE);
                                            tvHomeMessage.setVisibility(View.GONE);
                                            swipeContainerHome.setVisibility(View.VISIBLE);
                                        }

                                        for (int i = 0; i < leaderArr.length(); i++) {
                                            JSONObject joLeader = leaderArr.getJSONObject(i);

                                            JSONObject videoObj = joLeader.getJSONObject("Video");
                                            JSONObject campObj = joLeader.getJSONObject("Campaign");
                                            JSONObject userObj = joLeader.getJSONObject("User");
                                            JSONObject blankObj = joLeader.getJSONObject("0");

//                                          dblist.add(new Home(getActivity(),i, Integer.parseInt(videoObj.getString("id")), campObj.getString("id"), userObj.getString("id"), campObj.getString("title"), userObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers"), videoObj.getString("photo_path"), videoObj.getString("video_path"), blankObj.getString("likes"), videoObj.getString("views"), videoObj.getString("shares"), videoObj.getString("description"), blankObj.getString("didfollowed"), blankObj.getString("didliked"), videoObj.getString("rank"), videoObj.getString("is_winner"), blankObj.getString("cnt_comments"), videoObj.getString("created_on"),reportList));
                                        }

                                        myAdapter = new HomeTestAdapter(getActivity(), dblist);
                                        myAdapter.notifyDataSetChanged();
                                        mlistView.setAdapter(myAdapter);
                                        swipeContainerHome.setRefreshing(false);
                                        setListViewHeightBasedOnChildren(mlistView);
                                    }
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
//                                if (mProgressHUD.isShowing() && mProgressHUD != null)
//                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
//                        if (mProgressHUD.isShowing() && mProgressHUD != null)
//                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void getHomeData(final int count){
        final ProgressHUD mProgressHUD;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        String token = Constant.getShareData(getActivity(), "pref_login");
        params.put("access_token", token);
        params.put("campaign_id", "1");
        params.put("page", count + "");
        params.put("limit", "10");

//        System.out.println("call home video");

        mProgressHUD = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        client.post(Constant.get_home_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,final JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("response - " + count + " = " + response.toString());

                if(getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jo =response;
                                String posts = jo.getString("message");
                                int error = jo.getInt("error");
                                if (error == 0) {

                                    JSONArray leaderArr = jo.getJSONArray("videos");

                                    reportList = new ArrayList<Reports>();
                                    JSONArray reportArr = jo.getJSONArray("flags");
                                    for (int i = 0; i < reportArr.length(); i++) {
                                        JSONObject reportObj = reportArr.getJSONObject(i);
                                        JSONObject catObj = reportObj.getJSONObject("VideoReportCategory");

                                        reportList.add(new Reports(catObj.getString("id"), catObj.getString("title"), catObj.getString("description")));
                                    }

                                    if(loadMore == 1 && leaderArr.length() == 0){
                                        tvHomeMessage.setText(jo.getString("no_videos_message"));
                                        tvHomeMessage.setVisibility(View.VISIBLE);
                                        rlHomeEmptyState.setVisibility(View.VISIBLE);
                                        swipeContainerHome.setVisibility(View.GONE);
                                    }else
                                    {
                                        rlHomeEmptyState.setVisibility(View.GONE);
                                        tvHomeMessage.setVisibility(View.GONE);
                                        swipeContainerHome.setVisibility(View.VISIBLE);
                                    }

                                    for (int i = 0; i < leaderArr.length(); i++) {
                                        JSONObject joLeader = leaderArr.getJSONObject(i);

                                        JSONObject videoObj = joLeader.getJSONObject("Video");
                                        JSONObject campObj = joLeader.getJSONObject("Campaign");
                                        JSONObject userObj = joLeader.getJSONObject("User");
                                        JSONObject blankObj = joLeader.getJSONObject("0");
//                                        dblist.add(new Home(getActivity(),i, Integer.parseInt(videoObj.getString("id")), campObj.getString("id"), userObj.getString("id"), campObj.getString("title"), userObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers"), videoObj.getString("photo_path"), videoObj.getString("video_path"), blankObj.getString("likes"), videoObj.getString("views"), videoObj.getString("shares"), videoObj.getString("description"), blankObj.getString("didfollowed"), blankObj.getString("didliked"), videoObj.getString("rank"), videoObj.getString("is_winner"), blankObj.getString("cnt_comments"), videoObj.getString("created_on"),reportList));
                                    }

                                    myAdapter = new HomeTestAdapter(getActivity(), dblist);

                                    if (loadMore > 1) {

                                        int index = mlistView.getFirstVisiblePosition();
                                        View v = mlistView.getChildAt(0);
                                        int top = (v == null) ? 0 : v.getTop();

                                        // notify dataset changed or re-assign adapter here
                                        mlistView.setAdapter(myAdapter);
                                        myAdapter.notifyDataSetChanged();
                                        // restore the position of listview
                                        mlistView.setSelectionFromTop(index, top);

                                    } else
                                        mlistView.setAdapter(myAdapter);

                                    setListViewHeightBasedOnChildren(mlistView);

                                    if (loadMore == 1) {
                                        swipeContainerHome.setRefreshing(false);
                                        JSONArray campNameArr = jo.getJSONArray("campaigns_list");

                                        if(campNameArr.length() > 0){
                                            horizontalScrollHome.setVisibility(View.VISIBLE);
//                                                viewExtraSpaceHome.setVisibility(View.VISIBLE);
                                            if (bottomCampCount != campNameArr.length()) {
                                                bottomCampCount = campNameArr.length();

                                                for (int i = 0; i < campNameArr.length(); i++) {
                                                    JSONObject campNameObj = campNameArr.getJSONObject(i);
                                                    JSONObject camp = campNameObj.getJSONObject("Campaign");
                                                    TextView valueTV = new TextView(getActivity());
                                                    valueTV.setId(Integer.parseInt(camp.getString("id")));
                                                    valueTV.setText(camp.getString("title"));
                                                    llTestHomeFragmentCompainName.setWeightSum((float) campNameArr.length());
                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                                    valueTV.setLayoutParams(params);
                                                    valueTV.setTextSize(15);
                                                    valueTV.setPadding(35, 0, 40, 0);
                                                    valueTV.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                                                    valueTV.setGravity(Gravity.CENTER);
                                                    Log.i("id1", camp.getString("id"));
                                                    String campId = camp.getString("id");
                                                    valueTV.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Log.i("view", view.getId() + "");
                                                            CampaignFragment fragment = new CampaignFragment();
                                                            Bundle b1 = new Bundle();
                                                            b1.putString("campId", String.valueOf(view.getId()));
                                                            fragment.setArguments(b1);
                                                            FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                            FragmentTransaction ft = fm.beginTransaction();
                                                            ft.replace(R2.id.frame_container, fragment);
                                                            ft.addToBackStack(fragment.getClass().getName());
                                                            ft.commit();
                                                        }
                                                    });
                                                    ((LinearLayout) llTestHomeFragmentCompainName).addView(valueTV);
                                                }
                                                if (campNameArr.length() > 2)
                                                {
                                                    HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    params.gravity = Gravity.LEFT;
                                                    llTestHomeFragmentCompainName.setLayoutParams(params);
                                                }
                                            }
                                        }else {
                                            horizontalScrollHome.setVisibility(View.GONE);
//                                                viewExtraSpaceHome.setVisibility(View.GONE);
                                        }
                                    }

                                }
                            } catch (Exception e) {
//                    Constant.ShowErrorMessage("Error", e.getMessage()+"", getActivity());
//                    MyUtils.showToast(getActivity(),e.getMessage());
                                Log.i("Home","Errpr : " + e.getMessage());
                            }
                            if(mProgressHUD.isShowing() && mProgressHUD != null)
                                mProgressHUD.dismiss();
                        }
                    });
                }else {
                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                        mProgressHUD.dismiss();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline

                // Do something with the response
                System.out.println("timeline - " +timeline.toString());
                if(mProgressHUD.isShowing() && mProgressHUD != null)
                    mProgressHUD.dismiss();
            }
        });
    }
}
