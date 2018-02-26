package fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;
import com.votocast.votocast.VC_ContestRulesActivity;
import com.votocast.votocast.VC_MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.Constant;
import class_adapter.HomeLeaderboardBaseAdapter;
import class_adapter.ImageAdapter;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import db.LeaderBoard;
import db.NewestVideo;
import de.hdodenhof.circleimageview.CircleImageView;
import dialogFragment.LoadVideoFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampaignFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 123;
    ArrayList<LeaderBoard> myDatasetLeaderBoard;
    ArrayList<NewestVideo> myDatasetNewestVideo;
    ArrayList<NewestVideo> myDatasetFeaturedVideo;

    ListView lvCompaignFragmentLeaderboardList;
    BaseAdapter mAdapter;

    @BindView(R2.id.ivCompaignFragmentMainImage)
    ImageView ivCompaignFragmentMainImage;

    @BindView(R2.id.tvCompaignFragmentVideoCount)
    TextView tvCompaignFragmentVideoCount;

    @BindView(R2.id.tvCompaignFragmentProducersCount)
    TextView tvCompaignFragmentProducersCount;

    @BindView(R2.id.tvCompaignFragmentFollowingCount)
    TextView tvCompaignFragmentFollowingCount;

    @BindView(R2.id.btnCompaignFragmentFollowing)
    TextView btnCompaignFragmentFollowing;

    @BindView(R2.id.tvCompaignFragmentEventName)
    TextView tvCompaignFragmentEventName;

    @BindView(R2.id.tvCompaignFragmentEventDate)
    TextView tvCompaignFragmentEventDate;

    @BindView(R2.id.tvCompaignFragmentEventDesc)
    TextView tvCompaignFragmentEventDesc;

    @BindView(R2.id.llCompaignFragmentCompainName)
    LinearLayout llCompaignFragmentCompainName;

    @BindView(R2.id.tvCompaignFragmentLeaderboardText)
    TextView tvCompaignFragmentLeaderboardText;
    @BindView(R2.id.tvCompaignFragmentNewestText)
    TextView tvCompaignFragmentNewestText;

    @BindView(R2.id.tvFragmentTitle1)
    TextView tvFragmentTitle1;
    @BindView(R2.id.tvFragmentTitle2)
    TextView tvFragmentTitle2;
    @BindView(R2.id.tvFragmentTitle3)
    TextView tvFragmentTitle3;

    Context mContext;
    Activity mActivity;
    int leaderboardCount = 1;
    int newestvideoCount = 1;
    int featuredvideoCount = 1;
    GridView gridview;
    ImageAdapter mImageAdapter;
    int deviceWidth;
    String title, end_date_formatted, description, videos, users, followers;
    int IsFollow = 0;
    String username, userpic, campId;
    int followCount;
    String getCampId;
    String camp_short_code;
    TextView toolText;
    ImageView toolbar_back_button;

    @BindView(R2.id.scrollViewCampaign)
    ScrollView scrollViewCampaign;

    @BindView(R2.id.swipeContainerCampaign)
    SwipeRefreshLayout swipeContainerCampaign;
    @BindView(R2.id.ivCompaignFragmentLeadervoardMore)
    ImageView ivCompaignFragmentLeadervoardMore;
    @BindView(R2.id.ivCompaignFragmentNewestMore)
    ImageView ivCompaignFragmentNewestMore;

    @BindView(R2.id.btnCompaignFragmentSubmit)
    TextView btnCompaignFragmentSubmit;
    private int bottomCampCount = 0;

    @BindView(R2.id.tvCompaignFragmentRulesText)
    TextView tvCompaignFragmentRulesText;
    @BindView(R2.id.tvCompaignFragmentDonationText)
    TextView tvCompaignFragmentDonationText;
    @BindView(R2.id.horizontalScrollCampaign)
    HorizontalScrollView horizontalScrollCampaign;
    @BindView(R2.id.btnCompaignFragmentFollowingNew)
    TextView btnCompaignFragmentFollowingNew;
    @BindView(R2.id.llCampaignSubmitButtonBar)
    LinearLayout llCampaignSubmitButtonBar;
    @BindView(R2.id.viewExtraSpaceCampaign)
    View viewExtraSpaceCampaign;

    Menu menu;
    Tracker t;
    String from = "";

    @BindView(R2.id.tvCompaignFragmentFeaturedText)
    TextView tvCompaignFragmentFeaturedText;
    @BindView(R2.id.rlCampaignFeaturedVideo)
    RelativeLayout rlCampaignFeaturedVideo;
    GridView gridviewCompaignFragmentFeaturedVideo;

    public CampaignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.vc_fragment_compaign, container, false);
        ButterKnife.bind(this, v);

        t = ((MyAppTracker) getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
//        t.setScreenName("Campaign");
//        t.send(new HitBuilders.AppViewBuilder().build());

        ((VC_MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((VC_MainActivity) getActivity()).getSupportActionBar().show();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R.id.toolbarLogo);
        iv.setVisibility(View.GONE);
        RelativeLayout rlToolbar = (RelativeLayout) toolbar.findViewById(R.id.rlToolbar);
        rlToolbar.setVisibility(View.VISIBLE);
        toolText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolText.setVisibility(View.VISIBLE);
        toolText.setText("");
        toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);
        toolbar_back_button.setOnClickListener(this);
        ImageView toolbar_delete_button = (ImageView) toolbar.findViewById(R.id.toolbar_delete_button);
        toolbar_delete_button.setVisibility(View.GONE);

        tvCompaignFragmentRulesText.setTextColor(Color.parseColor(Constant.colorPrimary));
        tvCompaignFragmentDonationText.setTextColor(Color.parseColor(Constant.colorPrimary));
        Drawable background = btnCompaignFragmentSubmit.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(Color.parseColor(Constant.colorPrimary));
        }


        setHasOptionsMenu(true);
        mContext = getContext();

        if (getArguments().getString("from") != null && !getArguments().getString("from").equals("")) {
            from = getArguments().getString("from");
        }
        if (from.equals("main")) {
            toolbar_back_button.setVisibility(View.GONE);
        }
//        Bundle bundle = getArguments();
//        getCampId = bundle.getString("campId");
//        Log.i("getCampId", getCampId + "");
//        from = bundle.getString("from");

        if (savedInstanceState != null) {
            getCampId = savedInstanceState.getString("getCampId");
            tvCompaignFragmentVideoCount.setFocusable(true);
            tvCompaignFragmentVideoCount.setFocusableInTouchMode(true);
//            ((VC_MainActivity) getActivity()).setActionBarTitle(savedInstanceState.getString("title"));
            toolText.setText(savedInstanceState.getString("title"));
            tvCompaignFragmentVideoCount.setText(savedInstanceState.getString("videos"));
            tvCompaignFragmentProducersCount.setText(savedInstanceState.getString("users"));
            tvCompaignFragmentFollowingCount.setText(savedInstanceState.getString("followers"));
            tvCompaignFragmentEventName.setText(savedInstanceState.getString("title"));
            tvCompaignFragmentEventDate.setText(savedInstanceState.getString("end_date_formatted"));
            tvCompaignFragmentEventDesc.setText(savedInstanceState.getString("description"));
//            token = savedInstanceState.getString("token");
        } else {
            Bundle bundle = getArguments();
            getCampId = bundle.getString("campId");
            Log.i("getCampId", getCampId + "");
//            token = Constant.getShareData(getActivity(), "pref_login");
            new getCampaignDetail(getCampId).execute();
        }

        ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.play_submit, ImageSpan.ALIGN_BASELINE);
        SpannableString spannableString = new SpannableString("  SUBMIT");
        spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        btnCompaignFragmentSubmit.setText(spannableString);

        deviceWidth = getDeviceWidth();

        lvCompaignFragmentLeaderboardList = (ListView) v.findViewById(R.id.lvCompaignFragmentLeaderboardList);

        gridview = (GridView) v.findViewById(R.id.gridviewCompaignFragmentNewestVideo);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deviceWidth);
        gridview.setLayoutParams(lp);

        gridviewCompaignFragmentFeaturedVideo = (GridView) v.findViewById(R.id.gridviewCompaignFragmentFeaturedVideo);

        Constant.setTextFontsBold(getActivity(), tvCompaignFragmentFeaturedText);
        Constant.setTextFontsBold(getActivity(), tvCompaignFragmentLeaderboardText);
        Constant.setTextFontsBold(getActivity(), tvCompaignFragmentNewestText);
        Constant.setTextFontsBold(getActivity(), tvCompaignFragmentRulesText);
        Constant.setTextFontsBold(getActivity(), tvCompaignFragmentDonationText);

        Constant.setTextFontsSemibold(getActivity(), tvCompaignFragmentVideoCount);
        Constant.setTextFontsSemibold(getActivity(), tvCompaignFragmentProducersCount);
        Constant.setTextFontsSemibold(getActivity(), tvCompaignFragmentFollowingCount);
        Constant.setTextFontsRegular(getActivity(), tvFragmentTitle1);
        Constant.setTextFontsRegular(getActivity(), tvFragmentTitle2);
        Constant.setTextFontsRegular(getActivity(), tvFragmentTitle3);

        Constant.setTextFontsMedium(getActivity(), tvCompaignFragmentEventName);
        Constant.setTextFontsBold(getActivity(), tvCompaignFragmentEventDate);
        Constant.setTextFontsRegular(getActivity(), tvCompaignFragmentEventDesc);

//        myDatasetLeaderBoard = new ArrayList<LeaderBoard>();
//        myDatasetNewestVideo = new ArrayList<NewestVideo>();
//
//        new getLeaderBoardVideo(leaderboardCount).execute();
//        new getNewestVideo(newestvideoCount).execute();

        scrollViewCampaign.post(new Runnable() {
            @Override
            public void run() {
                scrollViewCampaign.fullScroll(View.FOCUS_UP);
            }
        });

//        Log.i("campaign", "campaign");

//        swipeContainerCampaign.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                leaderboardCount = 1;
//                myDatasetLeaderBoard = new ArrayList<LeaderBoard>();
//                myDatasetNewestVideo = new ArrayList<NewestVideo>();
//
//                new getCampaignDetailRefresh(getCampId).execute();
//                new getLeaderBoardVideo(leaderboardCount).execute();
//                new getNewestVideo(newestvideoCount).execute();
//            }
//        });
//        swipeContainerCampaign.setColorSchemeResources(android.R.color.holo_orange_dark);
        bottomCampCount = 0;
        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                        if (fragmentManager.getBackStackEntryCount() > 0) {
                            fragmentManager.popBackStack();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        myDatasetLeaderBoard = new ArrayList<LeaderBoard>();
        myDatasetNewestVideo = new ArrayList<NewestVideo>();
        myDatasetFeaturedVideo = new ArrayList<>();

        new getLeaderBoardVideo(leaderboardCount).execute();
        new getNewestVideo(newestvideoCount).execute();
        new getFeaturedVideo(featuredvideoCount).execute();

        swipeContainerCampaign.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leaderboardCount = 1;
                newestvideoCount = 1;
                featuredvideoCount = 1;
                myDatasetLeaderBoard = new ArrayList<LeaderBoard>();
                myDatasetNewestVideo = new ArrayList<NewestVideo>();
                myDatasetFeaturedVideo = new ArrayList<NewestVideo>();

                new getCampaignDetailRefresh(getCampId).execute();
                new getLeaderBoardVideo(leaderboardCount).execute();
                new getNewestVideo(newestvideoCount).execute();
                new getFeaturedVideo(featuredvideoCount).execute();
            }
        });
        swipeContainerCampaign.setColorSchemeColors(Color.parseColor(Constant.colorPrimary));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("getCampId", getCampId);
        outState.putString("title", title);
        outState.putString("end_date_formatted", end_date_formatted);
        outState.putString("description", description);
        outState.putString("videos", videos);
        outState.putString("users", users);
        outState.putString("followers", followers);
//        outState.putString("token",token);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @OnClick(R2.id.ivCompaignFragmentLeadervoardMore)
    void loadLeaderboardMore() {
        leaderboardCount = leaderboardCount + 1;
//        Log.i("leaderboardCount",leaderboardCount+"");
        new getLeaderBoardVideo(leaderboardCount).execute();
    }

    @OnClick(R2.id.ivCompaignFragmentNewestMore)
    void loadNewestMore() {
        newestvideoCount = newestvideoCount + 1;
        deviceWidth = deviceWidth + deviceWidth;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deviceWidth);
        gridview.setLayoutParams(lp);
        new getNewestVideo(newestvideoCount).execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_back_button) {
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }
    }

    class getCampaignDetail extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        JSONObject camp;
        String myCampId;

        public getCampaignDetail(String myCampId) {
            this.myCampId = myCampId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", myCampId));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_campaign_detail_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        final JSONObject compObj = jo.getJSONObject("campaign");

                                        if (compObj.getString("status").equals("active")) {

                                            title = compObj.getString("title");
                                            videos = jo.getString("videos");
                                            users = jo.getString("users");
                                            followers = jo.getString("followers");
                                            end_date_formatted = compObj.getString("end_date_formatted");
                                            description = compObj.getString("description");

                                            if (compObj.getString("campaign_rules").equals(""))
                                                tvCompaignFragmentRulesText.setVisibility(View.GONE);
                                            else {
                                                tvCompaignFragmentRulesText.setVisibility(View.VISIBLE);
                                                tvCompaignFragmentRulesText.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent m1 = new Intent(getActivity(), VC_ContestRulesActivity.class);
                                                        try {
                                                            m1.putExtra("ruleUrl", compObj.getString("campaign_rules"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        getActivity().startActivity(m1);
                                                    }
                                                });
                                            }
                                            if (compObj.getString("donation_link").equals(""))
                                                tvCompaignFragmentDonationText.setVisibility(View.GONE);
                                            else {
                                                tvCompaignFragmentDonationText.setVisibility(View.VISIBLE);
                                                tvCompaignFragmentDonationText.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Log.e("rules", "----------- donation");
                                                        Intent browserIntent = null;
                                                        try {
                                                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(compObj.getString("donation_link")));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        startActivity(browserIntent);
                                                    }
                                                });
                                            }

                                            campId = compObj.getString("id");
                                            username = compObj.getString("title");
                                            userpic = compObj.getString("photo_path");
                                            followCount = Integer.parseInt(jo.getString("followers"));
                                            camp_short_code = compObj.getString("short_code");

//                                            ((VC_MainActivity) getActivity()).setActionBarTitle(compObj.getString("title").toUpperCase());
                                            toolText.setText(compObj.getString("title").toUpperCase());
//                                            toolText.setText("title");
                                            t.setScreenName("Campaign - " + compObj.getString("title"));
                                            t.send(new HitBuilders.AppViewBuilder().build());

                                            tvCompaignFragmentVideoCount.setText(jo.getString("videos"));
                                            tvCompaignFragmentProducersCount.setText(jo.getString("users"));
                                            tvCompaignFragmentFollowingCount.setText(jo.getString("followers"));

                                            tvCompaignFragmentEventName.setText(compObj.getString("title"));
                                            tvCompaignFragmentEventDate.setText(compObj.getString("end_date_formatted"));
                                            tvCompaignFragmentEventDesc.setText(compObj.getString("description"));

                                            if (!compObj.getString("photo_path").equals("")) {
                                                Glide.with(getActivity()).load(compObj.getString("photo_path"))
                                                        .placeholder(R.drawable.backimage)
                                                        .error(R.drawable.backimage)
                                                        .into(ivCompaignFragmentMainImage);
                                            }
                                            ivCompaignFragmentMainImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                            if (jo.getString("allow_upload").equals("true")) {
                                                llCampaignSubmitButtonBar.setVisibility(View.VISIBLE);
                                                btnCompaignFragmentFollowingNew.setVisibility(View.GONE);
                                                menu.findItem(R.id.action_upload).setVisible(true);
                                            } else {
                                                llCampaignSubmitButtonBar.setVisibility(View.GONE);
                                                btnCompaignFragmentFollowingNew.setVisibility(View.VISIBLE);
                                                menu.findItem(R.id.action_upload).setVisible(false);
                                            }

                                            if (jo.getString("didfollowed").equals("true")) {
                                                IsFollow = 1;
                                                ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.done_white, ImageSpan.ALIGN_BASELINE);
                                                SpannableString spannableString = new SpannableString("  FOLLOWING");
                                                spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                                btnCompaignFragmentFollowing.setText(spannableString);
                                                btnCompaignFragmentFollowing.setBackgroundResource(R.drawable.button_background);
                                                btnCompaignFragmentFollowingNew.setText(spannableString);
                                                btnCompaignFragmentFollowingNew.setBackgroundResource(R.drawable.button_background);
                                            } else {
                                                IsFollow = 0;
                                                btnCompaignFragmentFollowing.setText("+ FOLLOW");
                                                btnCompaignFragmentFollowing.setBackgroundResource(R.drawable.follow_background);
                                                btnCompaignFragmentFollowingNew.setText("+ FOLLOW");
                                                btnCompaignFragmentFollowingNew.setBackgroundResource(R.drawable.follow_background);
                                            }
                                            JSONArray campNameArr = jo.getJSONArray("campaigns_list");

                                            if (campNameArr.length() > 0) {
                                                horizontalScrollCampaign.setVisibility(View.VISIBLE);
                                                viewExtraSpaceCampaign.setVisibility(View.VISIBLE);
                                                if (bottomCampCount != campNameArr.length()) {
                                                    bottomCampCount = campNameArr.length();

                                                    for (int i = 0; i < campNameArr.length(); i++) {
                                                        JSONObject campNameObj = campNameArr.getJSONObject(i);
                                                        camp = campNameObj.getJSONObject("Campaign");
                                                        TextView valueTV = new TextView(getActivity());
                                                        valueTV.setId(Integer.parseInt(camp.getString("id")));
//                                                        valueTV.setText(camp.getString("short_code"));
                                                        valueTV.setText(camp.getString("title"));
                                                        llCompaignFragmentCompainName.setWeightSum((float) campNameArr.length());
                                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                                        valueTV.setLayoutParams(params);
                                                        valueTV.setTextSize(15);
                                                        valueTV.setPadding(35, 0, 40, 0);
//                                                        valueTV.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                                                        valueTV.setTextColor(Color.parseColor(Constant.colorPrimary));
                                                        valueTV.setGravity(Gravity.CENTER);

                                                        if (!compObj.getString("id").equals(camp.getString("id"))) {
                                                            valueTV.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    CampaignFragment fragment = new CampaignFragment();
                                                                    Bundle b1 = new Bundle();
                                                                    b1.putString("campId", String.valueOf(view.getId()));
                                                                    fragment.setArguments(b1);
                                                                    FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                                    FragmentTransaction ft = fm.beginTransaction();
                                                                    ft.replace(R.id.frame_container, fragment);
                                                                    ft.addToBackStack(fragment.getClass().getName());
//                                                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                                    ft.commit();
                                                                }
                                                            });
                                                        }
                                                        ((LinearLayout) llCompaignFragmentCompainName).addView(valueTV);
                                                    }
                                                    if (campNameArr.length() > 2) {
                                                        HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                        params.gravity = Gravity.LEFT;
                                                        llCompaignFragmentCompainName.setLayoutParams(params);
                                                    }
                                                }
                                            } else {
                                                horizontalScrollCampaign.setVisibility(View.GONE);
                                                viewExtraSpaceCampaign.setVisibility(View.GONE);
                                            }
                                        }
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class getCampaignDetailRefresh extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        String myCampId;

        public getCampaignDetailRefresh(String myCampId) {
            this.myCampId = myCampId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", myCampId));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_campaign_detail_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        final JSONObject compObj = jo.getJSONObject("campaign");

                                        if (compObj.getString("status").equals("active")) {

                                            title = compObj.getString("title");
                                            videos = jo.getString("videos");
                                            users = jo.getString("users");
                                            followers = jo.getString("followers");
                                            end_date_formatted = compObj.getString("end_date_formatted");
                                            description = compObj.getString("description");

                                            if (compObj.getString("campaign_rules").equals(""))
                                                tvCompaignFragmentRulesText.setVisibility(View.GONE);
                                            else {
                                                tvCompaignFragmentRulesText.setVisibility(View.VISIBLE);
                                                tvCompaignFragmentRulesText.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent m1 = new Intent(getActivity(), VC_ContestRulesActivity.class);
                                                        try {
                                                            m1.putExtra("ruleUrl", compObj.getString("campaign_rules"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        getActivity().startActivity(m1);
                                                    }
                                                });
                                            }
                                            if (compObj.getString("donation_link").equals(""))
                                                tvCompaignFragmentDonationText.setVisibility(View.GONE);
                                            else {
                                                tvCompaignFragmentDonationText.setVisibility(View.VISIBLE);
                                                tvCompaignFragmentDonationText.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent browserIntent = null;
                                                        try {
                                                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(compObj.getString("donation_link")));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        startActivity(browserIntent);
                                                    }
                                                });
                                            }

                                            campId = compObj.getString("id");
                                            username = compObj.getString("title");
                                            userpic = compObj.getString("photo_path");
                                            followCount = Integer.parseInt(jo.getString("followers"));
                                            camp_short_code = compObj.getString("short_code");

//                                            ((VC_MainActivity) getActivity()).setActionBarTitle(compObj.getString("title").toUpperCase());
                                            toolText.setText(compObj.getString("title").toUpperCase());
//                                            toolText.setText("title");

                                            tvCompaignFragmentVideoCount.setText(jo.getString("videos"));
                                            tvCompaignFragmentProducersCount.setText(jo.getString("users"));
                                            tvCompaignFragmentFollowingCount.setText(jo.getString("followers"));

                                            tvCompaignFragmentEventName.setText(compObj.getString("title"));
                                            tvCompaignFragmentEventDate.setText(compObj.getString("end_date_formatted"));
                                            tvCompaignFragmentEventDesc.setText(compObj.getString("description"));

                                            if (!compObj.getString("photo_path").equals("")) {
                                                Glide.with(getActivity()).load(compObj.getString("photo_path"))
                                                        .placeholder(R.drawable.backimage)
                                                        .error(R.drawable.backimage)
                                                        .into(ivCompaignFragmentMainImage);
                                            }
                                            ivCompaignFragmentMainImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                            if (jo.getString("allow_upload").equals("true"))
                                                btnCompaignFragmentSubmit.setVisibility(View.VISIBLE);
                                            else
                                                btnCompaignFragmentSubmit.setVisibility(View.GONE);

                                            if (jo.getString("didfollowed").equals("true")) {
                                                IsFollow = 1;
                                                ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.done_white, ImageSpan.ALIGN_BASELINE);
                                                SpannableString spannableString = new SpannableString("  FOLLOWING");
                                                spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                                btnCompaignFragmentFollowing.setText(spannableString);
                                                btnCompaignFragmentFollowing.setBackgroundResource(R.drawable.button_background);
                                                btnCompaignFragmentFollowingNew.setText(spannableString);
                                                btnCompaignFragmentFollowingNew.setBackgroundResource(R.drawable.button_background);
                                            } else {
                                                IsFollow = 0;
                                                btnCompaignFragmentFollowing.setText("+ FOLLOW");
                                                btnCompaignFragmentFollowing.setBackgroundResource(R.drawable.follow_background);
                                                btnCompaignFragmentFollowingNew.setText("+ FOLLOW");
                                                btnCompaignFragmentFollowingNew.setBackgroundResource(R.drawable.follow_background);
                                            }
                                            swipeContainerCampaign.setRefreshing(false);

                                        }
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class getLeaderBoardVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int count, position;

        public getLeaderBoardVideo(int leaderboardCount) {
            count = leaderboardCount;
//            Log.i("count",count+"");
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            Log.i("camp id",getCampId);
            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", getCampId));
            pair.add(new BasicNameValuePair("page", count + ""));
            pair.add(new BasicNameValuePair("limit", "10"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_leaderboard_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    jo = new JSONObject(JsonString);
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        JSONObject compObj = jo.getJSONObject("campaign");
                                        JSONArray leaderArr = jo.getJSONArray("videos");

                                        if (compObj.getString("status").equals("active")) {
                                            for (int i = 0; i < leaderArr.length(); i++) {
                                                JSONObject joLeader = leaderArr.getJSONObject(i);
                                                JSONObject videoObj = joLeader.getJSONObject("Video");
                                                JSONObject userObj = joLeader.getJSONObject("User");
                                                JSONObject blankObj = joLeader.getJSONObject("0");

                                                String leaderboardText = videoObj.getString("description").replaceAll("\n", " ");

                                                if (leaderboardText.length() > 40)
                                                    leaderboardText = leaderboardText.substring(0, 40) + ".. " + compObj.getString("title");
                                                else
                                                    leaderboardText = leaderboardText + " " + compObj.getString("title");

                                                if (count == 1)
                                                    position = i + 1;
                                                else
                                                    position = (i + 1) + ((count - 1) * 10);
                                                myDatasetLeaderBoard.add(new LeaderBoard(position, compObj.getInt("id"), videoObj.getString("campaign_id"), videoObj.getString("id"), videoObj.getString("user_id"), videoObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers") + " followers", leaderboardText, blankObj.getString("likes"), videoObj.getString("views"), videoObj.getString("shares"), videoObj.getString("rank"), videoObj.getString("is_winner"), "camp"));

                                            }
                                            if (leaderboardCount == 1 && leaderArr.length() == 0) {
                                                tvCompaignFragmentLeaderboardText.setVisibility(View.GONE);
                                                ivCompaignFragmentLeadervoardMore.setVisibility(View.GONE);
                                            } else {
                                                tvCompaignFragmentLeaderboardText.setVisibility(View.VISIBLE);
                                                ivCompaignFragmentLeadervoardMore.setVisibility(View.VISIBLE);
                                            }

                                            if (leaderArr.length() < 10)
                                                ivCompaignFragmentLeadervoardMore.setVisibility(View.GONE);
                                            else
                                                ivCompaignFragmentLeadervoardMore.setVisibility(View.VISIBLE);

                                            mAdapter = new HomeLeaderboardBaseAdapter(getActivity(), myDatasetLeaderBoard);
                                            lvCompaignFragmentLeaderboardList.setAdapter(mAdapter);

                                            setListViewHeightBasedOnChildren(lvCompaignFragmentLeaderboardList);
                                        }
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class getNewestVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int count, position;

        public getNewestVideo(int newestvideoCount) {
            count = newestvideoCount;
//            Log.i("count",count+"");
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            Log.i("camp id",getCampId);
            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", getCampId));
            pair.add(new BasicNameValuePair("page", count + ""));
            pair.add(new BasicNameValuePair("limit", "9"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_newest_videos_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        JSONObject compObj = jo.getJSONObject("campaign");
                                        JSONArray leaderArr = jo.getJSONArray("videos");

                                        if (compObj.getString("status").equals("active")) {

                                            for (int i = 0; i < leaderArr.length(); i++) {
                                                JSONObject joLeader = leaderArr.getJSONObject(i);
                                                JSONObject videoObj = joLeader.getJSONObject("Video");
                                                JSONObject userObj = joLeader.getJSONObject("User");

                                                if (count == 1)
                                                    position = i + 1;
                                                else
                                                    position = ((i + 1) + ((count - 1) * 10)) - (count - 1);
//                                                Log.i("NewPosition", position + "");

                                                myDatasetNewestVideo.add(new NewestVideo(position, videoObj.getString("id"), videoObj.getString("photo_path"), "campaign", compObj.getString("id")));

                                            }

                                            if (newestvideoCount == 1 && leaderArr.length() == 0) {
                                                tvCompaignFragmentNewestText.setVisibility(View.GONE);
                                                ivCompaignFragmentNewestMore.setVisibility(View.GONE);
                                            } else {
                                                tvCompaignFragmentNewestText.setVisibility(View.VISIBLE);
                                                ivCompaignFragmentNewestMore.setVisibility(View.VISIBLE);
                                            }

                                            Log.i("newest video", leaderArr.length() + "");
                                            if (leaderArr.length() < 9)
                                                ivCompaignFragmentNewestMore.setVisibility(View.GONE);
                                            else
                                                ivCompaignFragmentNewestMore.setVisibility(View.VISIBLE);

                                            mImageAdapter = new ImageAdapter(getActivity(), getDeviceWidth(), myDatasetNewestVideo);
                                            gridview.setAdapter(mImageAdapter);
                                            if (count > 2)
                                                setGridViewHeightBasedOnChildren(gridview, 3);

                                        }
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class getFeaturedVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int count, position;

        public getFeaturedVideo(int featuredvideoCount) {
            count = featuredvideoCount;
//            Log.i("count",count+"");
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            Log.i("camp id",getCampId);
            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", getCampId));
//            pair.add(new BasicNameValuePair("page", count + ""));
//            pair.add(new BasicNameValuePair("limit", "9"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_featuredvideo_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        JSONObject compObj = jo.getJSONObject("campaign");
                                        JSONArray leaderArr = jo.getJSONArray("videos");

                                        if (compObj.getString("status").equals("active")) {

                                            for (int i = 0; i < leaderArr.length(); i++) {
                                                JSONObject joLeader = leaderArr.getJSONObject(i);
                                                JSONObject videoObj = joLeader.getJSONObject("Video");
                                                JSONObject userObj = joLeader.getJSONObject("User");

                                                if (count == 1)
                                                    position = i + 1;
                                                else
                                                    position = ((i + 1) + ((count - 1) * 10)) - (count - 1);
//                                                Log.i("NewPosition", position + "");

                                                myDatasetFeaturedVideo.add(new NewestVideo(position, videoObj.getString("id"), videoObj.getString("photo_path"), "campaign", compObj.getString("id")));

                                            }

                                            if (featuredvideoCount == 1 && leaderArr.length() == 0) {
                                                tvCompaignFragmentFeaturedText.setVisibility(View.GONE);
                                                rlCampaignFeaturedVideo.setVisibility(View.GONE);
                                            } else {
                                                tvCompaignFragmentFeaturedText.setVisibility(View.VISIBLE);
                                                rlCampaignFeaturedVideo.setVisibility(View.VISIBLE);
                                            }

                                            Log.i("newest video", leaderArr.length() + "");
//                                            if (leaderArr.length() < 9)
//                                                ivCompaignFragmentFeaturedMore.setVisibility(View.GONE);
//                                            else
//                                                ivCompaignFragmentFeaturedMore.setVisibility(View.VISIBLE);

//                                            mImageAdapter = new ImageAdapter(getActivity(), getDeviceWidth(), myDatasetFeaturedVideo);
//                                            gridviewCompaignFragmentFeaturedVideo.setAdapter(mImageAdapter);
//                                            if (count > 2) {
//                                                RelativeLayout.LayoutParams lpGridviewCompaignFragmentFeaturedVideo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deviceWidth);
//                                                gridviewCompaignFragmentFeaturedVideo.setLayoutParams(lpGridviewCompaignFragmentFeaturedVideo);
//                                                setGridViewHeightBasedOnChildren(gridviewCompaignFragmentFeaturedVideo, 3);
//                                            }

                                            mImageAdapter = new ImageAdapter(getActivity(), getDeviceWidth(), myDatasetFeaturedVideo);
                                            gridviewCompaignFragmentFeaturedVideo.setAdapter(mImageAdapter);
                                            RelativeLayout.LayoutParams lpGridviewCompaignFragmentFeaturedVideo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deviceWidth);
                                            gridviewCompaignFragmentFeaturedVideo.setLayoutParams(lpGridviewCompaignFragmentFeaturedVideo);
                                            if (count > 2)
                                                setGridViewHeightBasedOnChildren(gridviewCompaignFragmentFeaturedVideo, 3);

                                        }
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    @OnClick({R2.id.btnCompaignFragmentFollowing, R2.id.btnCompaignFragmentFollowingNew})
    void IsCampaignFollow() {
        if (IsFollow == 0) {
            IsFollow = 1;
            new setUserFollower().execute();
            tvCompaignFragmentFollowingCount.setText(String.valueOf(followCount + 1));
            followCount = followCount + 1;
            ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.done_white, ImageSpan.ALIGN_BASELINE);
            SpannableString spannableString = new SpannableString("  FOLLOWING");
            spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            btnCompaignFragmentFollowing.setText(spannableString);
            btnCompaignFragmentFollowing.setBackgroundResource(R.drawable.button_background);
            btnCompaignFragmentFollowingNew.setText(spannableString);
            btnCompaignFragmentFollowingNew.setBackgroundResource(R.drawable.button_background);
        } else {
            dialogUnFollowUser();
        }
    }

    private void dialogUnFollowUser() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vc_unfollow_user_dialog);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tvDialogUnfollowUserName);
        text.setText(username);
        CircleImageView image = (CircleImageView) dialog.findViewById(R.id.ivDialogUnfollowUserPic);
        if (!userpic.equals("") && userpic != null)
            Picasso.with(getActivity()).load(userpic)
                    .placeholder(R.drawable.campaign_icon)
                    .error(R.drawable.campaign_icon)
                    .into(image);
        else
            Picasso.with(getActivity()).load(R.drawable.campaign_icon)
                    .into(image);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnDialogUnfollowCancel);
        Button btnUnfollow = (Button) dialog.findViewById(R.id.btnDialogUnfollow);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setUserUnFollower().execute();
                dialog.dismiss();
                IsFollow = 0;
                tvCompaignFragmentFollowingCount.setText(String.valueOf(followCount - 1));
                followCount = followCount - 1;
                btnCompaignFragmentFollowing.setText("+ FOLLOW");
                btnCompaignFragmentFollowing.setBackgroundResource(R.drawable.follow_background);
                btnCompaignFragmentFollowingNew.setText("+ FOLLOW");
                btnCompaignFragmentFollowingNew.setBackgroundResource(R.drawable.follow_background);
            }
        });
        dialog.show();
    }

    class setUserFollower extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        //        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressHUD = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                }
//            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", campId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.follow_campaign_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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

    class setUserUnFollower extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressHUD = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                }
//            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("campaign_id", campId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.unfollow_campaign_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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

    @OnClick(R2.id.btnCompaignFragmentSubmit)
    void videoSubmit() {

        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            MyUtils.showToast(getActivity(), "Please wait videos are loading..");

            Constant.saveSharedData(getActivity(), "camp_id", campId);
            Constant.saveSharedData(getActivity(), "short_code", camp_short_code);

            LoadVideoFragment fragment = new LoadVideoFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(fragment.getClass().getName());
            ft.commit();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.menu_home_fragment, menu);
        menu.findItem(R.id.action_upload).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
//                if(from.equals("home")){
//                    FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.replace(R.id.frame_container, new HomeFragment());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.commit();
//                }
//                if(from.equals("search")){
//                    FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.replace(R.id.frame_container, new SearchFragment());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.commit();
//                }
                return true;
            case R2.id.action_upload:

                int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED) {

                    MyUtils.showToast(getActivity(), "Please wait videos are loading..");

                    Constant.saveSharedData(getActivity(), "camp_id", campId);
                    Constant.saveSharedData(getActivity(), "short_code", camp_short_code);

                    LoadVideoFragment fragment = new LoadVideoFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.commit();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(getActivity(), "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    MyUtils.showToast(getActivity(),"Now reselect video");
//                    someFlag = true;

//                    new Timer().schedule(new TimerTask() {
//                        @Override public void run() {
                    MyUtils.showToast(getActivity(), "Please wait videos are loading..");

                    Constant.saveSharedData(getActivity(), "camp_id", "");
                    Constant.saveSharedData(getActivity(), "short_code", "");
                    LoadVideoFragment fragment = new LoadVideoFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.commitAllowingStateLoss();
//                        }
//                    }, 0);

                } else {
                    Toast.makeText(getActivity(), "Permission Denied, We can not get videos.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public int getDeviceWidth() {
        int deviceWidth = 0;

        Point size = new Point();
        WindowManager windowManager = getActivity().getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(size);
            deviceWidth = size.x;
        } else {
            Display display = windowManager.getDefaultDisplay();
            deviceWidth = display.getWidth();
        }
        return deviceWidth;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

//        for (int i = 0; i < listAdapter.getCount() - 1; i++) {   --- if we get more space

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        int rows = listAdapter.getCount() / columns;
        if (listAdapter.getCount() % columns > 0) {
            rows++;
        }
        for (int i = 0; i < rows; i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getHorizontalSpacing() * rows);
        gridView.setLayoutParams(params);
        gridView.requestLayout();
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
}
