package fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.votocast.votocast.MainActivity;
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
import butterknife.OnClick;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import class_adapter.SearchAdapter;
import class_adapter.SearchCampaignAdapter;
import class_adapter.SearchListAdapter;
import db.LeaderBoard;
import db.Search;
import db.SearchCampaign;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    EditText etSearchbar;
    ArrayList<LeaderBoard> myDatasetLeaderBoard;
    BaseAdapter baseAdapter;

    @BindView(R2.id.lvSeatchFragmentVideoList)
    ListView lvSeatchFragmentVideoList;
    private Activity mActivity;
//    LinearLayout llCompaignFragmentCompainName;
//    @BindView(R2.id.horizontalScrollSearch)HorizontalScrollView horizontalScrollSearch;

//    @BindView(R2.id.viewExtraSpaceSearch)View viewExtraSpaceSearch;
//    @BindView(R2.id.scrollViewSearch)ScrollView scrollViewSearch;

    // ---------- search result ----------

    @BindView(R2.id.etSearchRsultFragmentText)
    EditText etSearchRsultFragmentText;
    @BindView(R2.id.llSearchResultLayer)
    LinearLayout llSearchResultLayer;
    @BindView(R2.id.listSearchResultFragment)
    StickyListHeadersListView listSearchResultFragment;

    ArrayList<NameValuePair> pair;
    JSONObject jo;

    ArrayList<Search> searchList;
    SearchAdapter searchAdapter;
    private int bottomCampCount = 0;

    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
//    @BindView(R2.id.scrollViewCampaign)
//    ScrollView scrollViewCampaign;
    @BindView(R2.id.lvSeatchFragmentCampaignVideoList)
    ListView lvSeatchFragmentCampaignVideoList;
//    @BindView(R2.id.viewExtraSpaceCampaignSearch)
//    View viewExtraSpaceCampaignSearch;
    @BindView(R2.id.mainSearchDetailView)
    LinearLayout mainSearchDetailView;
    ArrayList<SearchCampaign> searchCampaignList;
    SearchCampaignAdapter mSearchCampaignAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);

        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Search");
        t.send(new HitBuilders.AppViewBuilder().build());

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        etSearchbar = (EditText) v.findViewById(R.id.etSearchbar);
        ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.search_white, ImageSpan.ALIGN_BASELINE);

        SpannableString spannableString = new SpannableString("  Search");
        spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        etSearchbar.setHint(spannableString);

        Toolbar searchToolbar = (Toolbar) v.findViewById(R.id.searchToolbar);
        searchToolbar.setBackgroundColor(Color.parseColor(Constant.colorPrimary));
        tabLayout.setTabTextColors(Color.parseColor(Constant.unselectedTab),Color.parseColor(Constant.colorPrimaryDark));
        Drawable background = etSearchbar.getBackground();
        Drawable backgroundText = etSearchRsultFragmentText.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(Constant.searchEditBack));
            ((ShapeDrawable)backgroundText).getPaint().setColor(Color.parseColor(Constant.searchEditBack));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(Constant.searchEditBack));
            ((GradientDrawable)backgroundText).setColor(Color.parseColor(Constant.searchEditBack));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(Constant.searchEditBack));
            ((ColorDrawable)backgroundText).setColor(Color.parseColor(Constant.searchEditBack));
        }

        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Campaigns"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        bottomCampCount = 0;

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        final AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
                        myDialog.setTitle("Confirm")
                                .setMessage("Do you really want to close this app?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .create()
                                .show();
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("token",token);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("SearchOnResume", "onResume");
        (new Handler()).postDelayed(new Runnable() {

            public void run() {
//                horizontalScrollSearch.setVisibility(View.VISIBLE);
//                lvSeatchFragmentVideoList.setVisibility(View.VISIBLE);
//                viewExtraSpaceSearch.setVisibility(View.VISIBLE);
                etSearchbar.setVisibility(View.VISIBLE);
//                scrollViewSearch.setVisibility(View.VISIBLE);
                mainSearchDetailView.setVisibility(View.VISIBLE);

                etSearchRsultFragmentText.setText("");
                listSearchResultFragment.setVisibility(View.GONE);
                llSearchResultLayer.setVisibility(View.GONE);
            }
        }, 100);

        myDatasetLeaderBoard = new ArrayList<LeaderBoard>();
        searchCampaignList = new ArrayList<SearchCampaign>();
        new getLeaderBoardVideo().execute();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    lvSeatchFragmentVideoList.setVisibility(View.VISIBLE);
                    lvSeatchFragmentCampaignVideoList.setVisibility(View.GONE);

//                    scrollViewSearch.setVisibility(View.VISIBLE);
//                    scrollViewCampaign.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
//                    scrollViewSearch.setVisibility(View.GONE);
//                    scrollViewCampaign.setVisibility(View.VISIBLE);
                    lvSeatchFragmentVideoList.setVisibility(View.GONE);
                    lvSeatchFragmentCampaignVideoList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        etSearchbar.setOnClickListener(this);

        etSearchRsultFragmentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
                    Log.i("search text", "blank");
                } else {
                    Log.i("search text", count + "- " + start + "-" + before + "-" + s.toString());

                    pair = new ArrayList<NameValuePair>();
                    pair.add(new BasicNameValuePair("access_token", Constant.getShareData(getActivity(), "pref_login")));
                    pair.add(new BasicNameValuePair("terms", s.toString()));

                    Constant.postData(Constant.search_all_url, pair, new Handler(new Handler.Callback() {
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
                                            if (jo.getString("error").equals("0")) {

                                                searchList = new ArrayList<Search>();
                                                JSONArray campArr = jo.getJSONArray("campaigns");

                                                for (int i = 0; i < campArr.length(); i++) {
                                                    JSONObject campObj = campArr.getJSONObject(i);
                                                    JSONObject campDetail = campObj.getJSONObject("Campaign");
                                                    JSONObject blankObj = campObj.getJSONObject("0");

                                                    searchList.add(new Search(campDetail.getString("id"), 1, i, campDetail.getString("id"), campDetail.getString("photo_path"), campDetail.getString("short_code"), campDetail.getString("title"), "true", blankObj.getString("didfollowed"), blankObj.getString("total_videos")));
                                                }

                                                JSONArray userArr = jo.getJSONArray("users");

                                                for (int i = 0; i < userArr.length(); i++) {
                                                    JSONObject userObj = userArr.getJSONObject(i);

                                                    JSONObject userDetail = userObj.getJSONObject("User");
                                                    JSONObject userFollow = userObj.getJSONObject("0");

                                                    searchList.add(new Search(userDetail.getString("id"), 2, i, userDetail.getString("id"), userDetail.getString("photo_path"), userDetail.getString("name"), "@" + userDetail.getString("username"), "", userFollow.getString("didfollowed"), ""));
                                                }

//                                                Log.i("search list2", searchList.toString());

//                                                horizontalScrollSearch.setVisibility(View.GONE);
//                                                viewExtraSpaceSearch.setVisibility(View.GONE);
//                                                scrollViewSearch.setVisibility(View.GONE);
                                                mainSearchDetailView.setVisibility(View.GONE);
//                                                lvSeatchFragmentVideoList.setVisibility(View.GONE);
                                                listSearchResultFragment.setVisibility(View.VISIBLE);

                                                if (searchList.size() > 0) {

//                                                    llCompaignFragmentCompainName.setVisibility(View.GONE);
//                                                    lvSeatchFragmentVideoList.setVisibility(View.GONE);
//                                                    listSearchResultFragment.setVisibility(View.VISIBLE);

                                                    searchAdapter = new SearchAdapter(getActivity(), searchList, listSearchResultFragment);
                                                    listSearchResultFragment.setAdapter(searchAdapter);

                                                } else {
                                                    MyUtils.showToast(getActivity(), "Result not found !");

//                                                    llCompaignFragmentCompainName.setVisibility(View.VISIBLE);
//                                                    lvSeatchFragmentVideoList.setVisibility(View.VISIBLE);
//                                                    listSearchResultFragment.setVisibility(View.GONE);
                                                }

//                                            setListViewHeightBasedOnChildren(lvSearchCampaignList);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            } else {
                                Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                            }
                            return false;
                        }
                    }));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                mAdapter = new SearchAdapter(SearchActivity.this,searchList);
//                lvSearchCampaignList.setAdapter(mAdapter);

//                setListViewHeightBasedOnChildren(lvSearchCampaignList);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @OnClick(R2.id.btnSearchResultFragmentCancel)
    void setOnCancelBtn() {

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

//        horizontalScrollSearch.setVisibility(View.VISIBLE);
//        lvSeatchFragmentVideoList.setVisibility(View.VISIBLE);
//        viewExtraSpaceSearch.setVisibility(View.VISIBLE);
        etSearchbar.setVisibility(View.VISIBLE);
//        scrollViewSearch.setVisibility(View.VISIBLE);
        mainSearchDetailView.setVisibility(View.VISIBLE);

        etSearchRsultFragmentText.setText("");
        listSearchResultFragment.setVisibility(View.GONE);
        llSearchResultLayer.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.etSearchbar) {
            etSearchbar.setVisibility(View.GONE);

            (new Handler()).postDelayed(new Runnable() {

                public void run() {

                    etSearchRsultFragmentText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                    etSearchRsultFragmentText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));

                }
            }, 200);

            llSearchResultLayer.setVisibility(View.VISIBLE);
        }
    }

    class getLeaderBoardVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int count,position;
        JSONObject camp;

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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.search_featured_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @TargetApi(Build.VERSION_CODES.KITKAT)
                            @Override
                            public void run() {

                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        // ------------------- video list ---------------
                                        JSONArray leaderArr = jo.getJSONArray("videos");
                                        for (int i = 0; i < leaderArr.length(); i++) {
                                            JSONObject joLeader = leaderArr.getJSONObject(i);
                                            JSONObject videoObj = joLeader.getJSONObject("Video");
                                            JSONObject compObj = joLeader.getJSONObject("Campaign");
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
//                                            Log.i("Position", position + "");

                                            myDatasetLeaderBoard.add(new LeaderBoard(position,userObj.getInt("id"), videoObj.getString("campaign_id"), videoObj.getString("id"), videoObj.getString("user_id"), videoObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers") + " followers", leaderboardText, blankObj.getString("likes"), videoObj.getString("views"), videoObj.getString("shares"), videoObj.getString("rank"), videoObj.getString("is_winner"), "search"));

                                        }
                                        baseAdapter = new SearchListAdapter(getActivity(), myDatasetLeaderBoard);
                                        lvSeatchFragmentVideoList.setAdapter(baseAdapter);
//                                        setListViewHeightBasedOnChildren(lvSeatchFragmentVideoList);

                                        //-------------------- campaign list ---------------

                                        JSONArray campaignArr = jo.getJSONArray("campaigns_list");
                                        for (int i = 0; i < campaignArr.length(); i++) {
                                            JSONObject joLeader = campaignArr.getJSONObject(i);
                                            JSONObject campObj = joLeader.getJSONObject("Campaign");
                                            JSONObject blankObj = joLeader.getJSONObject("0");

                                            searchCampaignList.add(new SearchCampaign(campObj.getString("id"), campObj.getString("title"), campObj.getString("photo"), campObj.getString("photo_path"), campObj.getString("is_featured"), campObj.getString("status"), campObj.getString("videos"), campObj.getString("users"), campObj.getString("followers"), campObj.getString("didfollowed"), blankObj.getString("allow_upload")));
                                            Log.e("Campaign Video",searchCampaignList.get(i).toString());
                                        }
                                        mSearchCampaignAdapter = new SearchCampaignAdapter(getActivity(), searchCampaignList);
                                        lvSeatchFragmentCampaignVideoList.setAdapter(mSearchCampaignAdapter);

//                                        JSONArray campNameArr = jo.getJSONArray("campaigns_list");
////                                        Log.i("search",campNameArr.toString());
//                                        if(campNameArr.length() > 0) {
//                                            horizontalScrollSearch.setVisibility(View.VISIBLE);
//                                            viewExtraSpaceSearch.setVisibility(View.VISIBLE);
//                                            if(bottomCampCount != campNameArr.length()) {
//                                                bottomCampCount = campNameArr.length();
//
//                                                for (int i = 0; i < campNameArr.length(); i++) {
//                                                    JSONObject campNameObj = campNameArr.getJSONObject(i);
//                                                    camp = campNameObj.getJSONObject("Campaign");
//                                                    TextView valueTV = new TextView(getActivity());
//                                                    valueTV.setId(Integer.parseInt(camp.getString("id")));
////                                                    valueTV.setText(camp.getString("short_code"));
//                                                    valueTV.setText(camp.getString("title"));
//                                                    llCompaignFragmentCompainName.setWeightSum((float) campNameArr.length());
//                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
//                                                    valueTV.setLayoutParams(params);
//                                                    valueTV.setTextSize(15);
//                                                    valueTV.setPadding(35, 0, 40, 0);
//                                                    valueTV.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
//                                                    valueTV.setGravity(Gravity.CENTER);
//                                                    Log.i("id1", camp.getString("id"));
////                                            campId = camp.getString("id");
//                                                    valueTV.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View view) {
//                                                            Log.i("view", view.getId() + "");
//                                                            CampaignFragment fragment = new CampaignFragment();
//                                                            Bundle b1 = new Bundle();
//                                                            b1.putString("campId", String.valueOf(view.getId()));
////                                                    b1.putString("from","search");
//                                                            fragment.setArguments(b1);
//                                                            FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//                                                            FragmentTransaction ft = fm.beginTransaction();
//                                                            ft.replace(R.id.frame_container, fragment);
////                                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                                                            ft.addToBackStack(fragment.getClass().getName());
//                                                            ft.commit();
//                                                        }
//                                                    });
//                                                    ((LinearLayout) llCompaignFragmentCompainName).addView(valueTV);
//                                                }
//                                                if (campNameArr.length() > 2)
//                                                {
//                                                    HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                                                    params.gravity = Gravity.LEFT;
//                                                    llCompaignFragmentCompainName.setLayoutParams(params);
//                                                }
//                                            }
//                                        }else {
//                                            horizontalScrollSearch.setVisibility(View.GONE);
//                                            viewExtraSpaceSearch.setVisibility(View.GONE);
////                                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
////                                            params.setMargins(0,0,0,0);
////                                            lvSeatchFragmentVideoList.setLayoutParams(params);
//                                        }

                                    }
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
