package fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.picasso.Picasso;
import com.votocast.votocast.MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.Constant;
import class_adapter.ImageAdapter;
import class_adapter.ProfileAdapter;
import class_adapter.ProgressHUD;
import db.LeaderBoard;
import db.NewestVideo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    @BindView(R2.id.ivProfileFragmentMainImage)
    ImageView ivProfileFragmentMainImage;

    @BindView(R2.id.tvProfileFragmentVideoCount)
    TextView tvProfileFragmentVideoCount;

    @BindView(R2.id.tvProfileFragmentVotesCount)
    TextView tvProfileFragmentVotesCount;

    @BindView(R2.id.tvProfileFragmentFollowersCount)
    TextView tvProfileFragmentFollowersCount;

    @BindView(R2.id.civProfileFragmentProfilePic)
    CircleImageView civProfileFragmentProfilePic;

    @BindView(R2.id.tvProfileFragmentUserName)
    TextView tvProfileFragmentUserName;

    @BindView(R2.id.tvProfileFragmentUserAddress)
    TextView tvProfileFragmentUserAddress;

    @BindView(R2.id.tvProfileFragmentUserDesc)
    TextView tvProfileFragmentUserDesc;

    @BindView(R2.id.tvProfileFragmentUserSite)
    TextView tvProfileFragmentUserSite;

    @BindView(R2.id.lvProfileFragmentLeaderboardList)
    ListView lvProfileFragmentLeaderboardList;

    @BindView(R2.id.tvProfileFragmentLeaderboardText)
    TextView tvProfileFragmentLeaderboardText;

    @BindView(R2.id.btnProfileFragmentSettings)
    Button btnProfileFragmentSettings;
    @BindView(R2.id.btnProfileFragmentFollower)
    TextView btnProfileFragmentFollower;

    @BindView(R2.id.tvProfileTitle1)
    TextView tvProfileTitle1;
    @BindView(R2.id.tvProfileTitle2)
    TextView tvProfileTitle2;
    @BindView(R2.id.tvProfileTitle3)
    TextView tvProfileTitle3;

    @BindView(R2.id.scrollViewProfile)
    ScrollView scrollViewProfile;

    ArrayList<LeaderBoard> myDataset;
    BaseAdapter mAdapter;
    String userId;
    int IsFollow = 0, followCount;
    String username, userpic;
    private ImageLoader imageLoader;
    //    String from, fromId;
    //    String token;
    TextView toolText;
    ImageView toolbar_back_button;
    int loadMore = 1;
    int newestvideoCount = 1;

    @BindView(R2.id.swipeContainerProfile)
    SwipeRefreshLayout swipeContainerProfile;
    @BindView(R2.id.ivProfileFragmentMore)
    ImageView ivProfileFragmentMore;

    // -----------------newest video --------

    @BindView(R2.id.tvProfileFragmentNewestText)
    TextView tvProfileFragmentNewestText;

    //    @BindView(R2.id.gridviewProfileFragmentNewestVideo)
    GridView gridviewProfileFragmentNewestVideo;

    @BindView(R2.id.ivProfileFragmentNewestMore)
    ImageView ivProfileFragmentNewestMore;
    private int deviceWidth;
    ArrayList<NewestVideo> myDatasetNewestVideo;
    ImageAdapter mImageAdapter;
    Activity mAcivity;
    Tracker t;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);

        t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
//        t.setScreenName("Profile");
//        t.send(new HitBuilders.AppViewBuilder().build());

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R.id.toolbarLogo);
        iv.setVisibility(View.GONE);
        toolText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolText.setVisibility(View.VISIBLE);
        toolText.setText("");
        RelativeLayout rlToolbar = (RelativeLayout) toolbar.findViewById(R.id.rlToolbar);
        rlToolbar.setVisibility(View.VISIBLE);
        toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);
        toolbar_back_button.setOnClickListener(this);
        ImageView toolbar_delete_button = (ImageView) toolbar.findViewById(R.id.toolbar_delete_button);
        toolbar_delete_button.setVisibility(View.INVISIBLE);

        tvProfileFragmentUserSite.setTextColor(Color.parseColor(Constant.colorPrimary));
        tvProfileFragmentUserSite.setLinkTextColor(Color.parseColor(Constant.colorPrimary));
        Drawable background = btnProfileFragmentFollower.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
        }

        setHasOptionsMenu(true);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ProfileFragment fragment;
        if (savedInstanceState != null) {
            userId = savedInstanceState.getString("userId");
//            token = savedInstanceState.getString("token");
        } else {
            Bundle bundle = getArguments();
            userId = bundle.getString("user_id");
//            token = Constant.getShareData(getActivity(), "pref_login");
        }

        Constant.setTextFontsBold(getActivity(), tvProfileFragmentLeaderboardText);
        Constant.setTextFontsBold(getActivity(), tvProfileFragmentNewestText);

        Constant.setTextFontsSemibold(getActivity(), tvProfileFragmentVideoCount);
        Constant.setTextFontsSemibold(getActivity(), tvProfileFragmentVotesCount);
        Constant.setTextFontsSemibold(getActivity(), tvProfileFragmentFollowersCount);
        Constant.setTextFontsRegular(getActivity(), tvProfileTitle1);
        Constant.setTextFontsRegular(getActivity(), tvProfileTitle2);
        Constant.setTextFontsRegular(getActivity(), tvProfileTitle3);

        Constant.setTextFontsMedium(getActivity(), tvProfileFragmentUserName);
        Constant.setTextFontsBold(getActivity(), tvProfileFragmentUserAddress);
        Constant.setTextFontsRegular(getActivity(), tvProfileFragmentUserDesc);
        Constant.setTextFontsRegular(getActivity(), tvProfileFragmentUserSite);

        if (userId.equals("0") || userId == null) {
            btnProfileFragmentSettings.setVisibility(View.VISIBLE);
            btnProfileFragmentFollower.setVisibility(View.GONE);
        } else {
            btnProfileFragmentSettings.setVisibility(View.GONE);
            btnProfileFragmentFollower.setVisibility(View.VISIBLE);
        }

        deviceWidth = getDeviceWidth();
        gridviewProfileFragmentNewestVideo = (GridView) v.findViewById(R.id.gridviewProfileFragmentNewestVideo);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deviceWidth);
        gridviewProfileFragmentNewestVideo.setLayoutParams(lp);

//        myDataset = new ArrayList<LeaderBoard>();
//        myDatasetNewestVideo = new ArrayList<NewestVideo>();
//
//
//        new getUserDetail().execute();
//        new getUserLeaderBoardDetail(loadMore).execute();
//        new getNewestVideo(newestvideoCount).execute();

        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/.temp_tmp";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(getActivity(),
                    CACHE_DIR);

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                    getActivity())
                    .defaultDisplayImageOptions(defaultOptions)
                    .discCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

            ImageLoaderConfiguration config = builder.build();
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

        } catch (Exception e) {

        }

        scrollViewProfile.post(new Runnable() {
            @Override
            public void run() {
                scrollViewProfile.fullScroll(View.FOCUS_UP);
            }
        });

//        swipeContainerProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                myDataset = new ArrayList<LeaderBoard>();
//                new getUserDetail().execute();
//                loadMore = 1;
//                new getUserLeaderBoardDetail(loadMore).execute();
//            }
//        });
//        swipeContainerProfile.setColorSchemeResources(android.R.color.holo_orange_dark);
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
    public void onResume() {
        super.onResume();
        myDataset = new ArrayList<LeaderBoard>();
        myDatasetNewestVideo = new ArrayList<NewestVideo>();

        new getUserDetail().execute();
//        new getUserLeaderBoardDetail(loadMore).execute();
//        new getNewestVideo(newestvideoCount).execute();

        swipeContainerProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myDataset = new ArrayList<LeaderBoard>();
                myDatasetNewestVideo = new ArrayList<NewestVideo>();
                new getUserDetail().execute();
                loadMore = 1;
                newestvideoCount = 1;
//                new getUserLeaderBoardDetail(loadMore).execute();
//                new getNewestVideo(newestvideoCount).execute();
            }
        });
        swipeContainerProfile.setColorSchemeResources(android.R.color.holo_orange_dark);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAcivity = activity;
        Log.i("profileFragment", "on Attach" + activity);
    }

    private int getDeviceWidth() {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("userId", userId);
//        outState.putString("token",token);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile_fragment, menu);
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
//                if (from.equals("home")) {
//                    FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.replace(R.id.frame_container, new HomeFragment());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.commit();
//                }
//                if (from.equals("search")) {
//                    FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.replace(R.id.frame_container, new SearchFragment());
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.commit();
//                }
//                if (from.equals("camp")) {
//                    CampaignFragment fragment = new CampaignFragment();
//                    Bundle b1 = new Bundle();
//                    b1.putString("campId", fromId);
////                    b1.putString("from", from);
////                    b1.putString("fromId", fromId);
//                    fragment.setArguments(b1);
//                    FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.replace(R.id.frame_container, fragment);
//                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.commit();
//                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R2.id.btnProfileFragmentSettings)
    void editProfile() {
//        startActivity(new Intent(getActivity(), SettingsActivity.class));

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, new SettingsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
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

    @OnClick(R2.id.ivProfileFragmentMore)
    void fnLoadMore() {
        loadMore = loadMore + 1;
        new getUserLeaderBoardDetail(loadMore).execute();
    }

    @OnClick(R2.id.ivProfileFragmentNewestMore)
    void loadNewestMore() {
        newestvideoCount = newestvideoCount + 1;
        deviceWidth = deviceWidth + deviceWidth;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deviceWidth);
        gridviewProfileFragmentNewestVideo.setLayoutParams(lp);
        new getNewestVideo(newestvideoCount).execute();
    }

    class getUserDetail extends AsyncTask<Void, Void, Void> {
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
            if (!userId.equals("0"))
                pair.add(new BasicNameValuePair("user_id", String.valueOf(userId)));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_user_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        if(getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                mProgressHUD.dismiss();
                                    try {
                                        jo = new JSONObject(JsonString);
                                        String msg = jo.getString("message");
                                        int error = jo.getInt("error");
                                        if (error == 0) {

                                            new getUserLeaderBoardDetail(loadMore).execute();
                                            new getNewestVideo(newestvideoCount).execute();

                                            JSONObject userObj = jo.getJSONObject("user");
                                            Log.i("Profile", userId + "--" + Constant.getShareData(getActivity(), "myId"));
                                            if (userId.equals("0") || userId.equals(Constant.getShareData(getActivity(), "myId"))) {
                                                btnProfileFragmentSettings.setVisibility(View.VISIBLE);
                                                btnProfileFragmentFollower.setVisibility(View.GONE);
                                            } else {
                                                btnProfileFragmentSettings.setVisibility(View.GONE);
                                                btnProfileFragmentFollower.setVisibility(View.VISIBLE);
                                            }

                                            username = userObj.getString("username");
                                            userpic = userObj.getString("photo_path");

//                                        ((MainActivity) getActivity()).setActionBarTitle(userObj.getString("username").toUpperCase());
                                            toolText.setText(userObj.getString("username").toUpperCase());
                                            t.setScreenName("Profile - " + userObj.getString("username"));
                                            t.send(new HitBuilders.AppViewBuilder().build());

                                            tvProfileFragmentVideoCount.setText(jo.getString("videos"));
                                            tvProfileFragmentVotesCount.setText(jo.getString("votes"));
                                            tvProfileFragmentFollowersCount.setText(jo.getString("followers"));

                                            followCount = Integer.parseInt(jo.getString("followers"));

                                            if (userObj.getString("name").equals(""))
                                                tvProfileFragmentUserName.setText(userObj.getString("username"));
                                            else
                                                tvProfileFragmentUserName.setText(userObj.getString("name"));

                                            if (!userObj.getString("city").equals(""))
                                                tvProfileFragmentUserAddress.setText(userObj.getString("city") + ", " + userObj.getString("state"));

                                            if (userObj.getString("about").equals(""))
                                                tvProfileFragmentUserDesc.setVisibility(View.GONE);
                                            else {
                                                tvProfileFragmentUserDesc.setVisibility(View.VISIBLE);
                                                tvProfileFragmentUserDesc.setText(userObj.getString("about"));
                                            }
                                            if (userObj.getString("website").equals(""))
                                                tvProfileFragmentUserSite.setVisibility(View.GONE);
                                            else {
                                                tvProfileFragmentUserSite.setVisibility(View.VISIBLE);
//                                            tvProfileFragmentUserSite.setText(userObj.getString("website"));
                                                String content = "<a href=\"" + userObj.getString("website") + "\" >" + userObj.getString("website") + "</a> ";
                                                Spannable s = (Spannable) Html.fromHtml(content);
                                                for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
                                                    s.setSpan(new UnderlineSpan() {
                                                        public void updateDrawState(TextPaint tp) {
                                                            tp.setUnderlineText(false);
                                                        }
                                                    }, s.getSpanStart(u), s.getSpanEnd(u), 0);
                                                }
                                                tvProfileFragmentUserSite.setText(s);
//                                            tvProfileFragmentUserSite.setText(Html.fromHtml( "<a href=\""+ userObj.getString("website") +"\" >"+ userObj.getString("website") +"</a> "));
                                                tvProfileFragmentUserSite.setMovementMethod(LinkMovementMethod.getInstance());
                                            }

                                            Log.i("user image", userObj.getString("photo_path"));
                                            if (!userObj.getString("photo_path").equals("")) {
//                                            Picasso.with(getActivity()).load(userObj.getString("photo_path"))
//                                                    .placeholder(R.drawable.user_default)
//                                                    .error(R.drawable.user_default)
//                                                    .into(civProfileFragmentProfilePic);
//                                            civProfileFragmentProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                Glide.with(getActivity()).load(userObj.getString("photo_path"))
                                                        .placeholder(getResources().getColor(R.color.tabColor))
                                                        .error(getResources().getColor(R.color.tabColor))
                                                        .into(ivProfileFragmentMainImage);
                                                ivProfileFragmentMainImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                imageLoader.displayImage(userObj.getString("photo_path"), civProfileFragmentProfilePic, new ImageLoadingListener() {
                                                    @Override
                                                    public void onLoadingStarted(String s, View view) {
                                                    }

                                                    @Override
                                                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                                                    }

                                                    @Override
                                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                                    }

                                                    @Override
                                                    public void onLoadingCancelled(String s, View view) {

                                                    }
                                                });
                                            } else {
                                                civProfileFragmentProfilePic.setImageResource(R.drawable.user_default);
//                                            ivProfileFragmentMainImage.setBackgroundColor(getResources().getColor(R.color.tabColor));
                                            }

                                            if (!userId.equals("0")) {
                                                if (jo.getString("didfollowed").equals("true")) {
                                                    IsFollow = 1;
//                                                btnProfileFragmentFollower.setText("Following");
                                                    btnProfileFragmentFollower.setBackgroundResource(R.drawable.button_background);

                                                    ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.done_white, ImageSpan.ALIGN_BASELINE);
                                                    SpannableString spannableString = new SpannableString("  FOLLOWING");
                                                    spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                                    btnProfileFragmentFollower.setText(spannableString);
                                                } else {
                                                    IsFollow = 0;
                                                    btnProfileFragmentFollower.setText("+ FOLLOW");
//                                                btnProfileFragmentFollower.setBackgroundColor(getResources().getColor(R.color.opacityBottom));
                                                    btnProfileFragmentFollower.setBackgroundResource(R.drawable.follow_background);
                                                }
                                            }
                                            swipeContainerProfile.setRefreshing(false);
                                        }else if(error == 2){
                                            AlertDialog.Builder alrt = new AlertDialog.Builder(getActivity());
                                            alrt.setTitle("Alert");
                                            alrt.setMessage(msg);
                                            alrt.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                    if (fragmentManager.getBackStackEntryCount() > 0) {
                                                        fragmentManager.popBackStack();
                                                    }
                                                }
                                            });
                                            alrt.create().show();
                                        }
                                        else
                                            Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                    } catch (Exception e) {
                                        Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                    }
//                                if (mProgressHUD.isShowing() && mProgressHUD != null)
//                                    mProgressHUD.dismiss();
                                }
                            });
                        }

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

    class getUserLeaderBoardDetail extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int loadMore,position;

        public getUserLeaderBoardDetail(int loadMore) {
            this.loadMore = loadMore;
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
            if (!userId.equals("0"))
                pair.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            pair.add(new BasicNameValuePair("page", loadMore + ""));
            pair.add(new BasicNameValuePair("limit", "10"));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_user_leadreboard_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        if(getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                mProgressHUD.dismiss();
                                    try {
                                        jo = new JSONObject(JsonString);
                                        String posts = jo.getString("message");
                                        int error = jo.getInt("error");
                                        if (error == 0) {

                                            JSONObject userObj = jo.getJSONObject("user");
                                            JSONArray leaderArr = jo.getJSONArray("videos");

                                            if (leaderArr.length() > 0) {
                                                tvProfileFragmentLeaderboardText.setVisibility(View.VISIBLE);
                                                ivProfileFragmentMore.setVisibility(View.VISIBLE);
                                                for (int i = 0; i < leaderArr.length(); i++) {
                                                    JSONObject joLeader = leaderArr.getJSONObject(i);
                                                    JSONObject videoObj = joLeader.getJSONObject("Video");
                                                    JSONObject campObj = joLeader.getJSONObject("Campaign");
                                                    JSONObject blankObj = joLeader.getJSONObject("0");

                                                    String leaderboardText = videoObj.getString("description").replaceAll("\n", " ");

                                                    if (leaderboardText.length() > 30)
                                                        leaderboardText = leaderboardText.substring(0, 30) + ".. " + campObj.getString("title");
                                                    else
                                                        leaderboardText = leaderboardText + " " + campObj.getString("title");

                                                    if (loadMore == 1)
                                                        position = i + 1;
                                                    else
                                                        position = (i + 1) + ((loadMore - 1) * 10);
//                                                Log.i("Position", position + "");

//                                                myDataset.add(new LeaderBoard(videoObj.getString("photo_path"), userObj.getString("username"), "122,328 followers", leaderboardText, videoObj.getString("likes"), videoObj.getString("views"), "237"));
//                                                if(String.valueOf(userObj.getInt("id")) != null)
                                                    myDataset.add(new LeaderBoard(position,userObj.getInt("id"), videoObj.getString("campaign_id"), videoObj.getString("id"), videoObj.getString("user_id"), videoObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers") + " followers", leaderboardText, blankObj.getString("likes"), videoObj.getString("views"), videoObj.getString("shares"), videoObj.getString("rank"), videoObj.getString("is_winner"), "profile"));
//                                                else
//                                                    myDataset.add(new LeaderBoard(1, videoObj.getString("campaign_id"), videoObj.getString("id"), videoObj.getString("user_id"), videoObj.getString("photo_path"), userObj.getString("username"), blankObj.getString("followers") + " followers", leaderboardText, videoObj.getString("likes"), videoObj.getString("views"), "25", "profile"));

                                                }
//                                            if (mProgressHUD.isShowing() && mProgressHUD != null)
//                                                mProgressHUD.dismiss();


                                                if (loadMore == 1 && leaderArr.length() == 0) {
                                                    Log.i("Profile leader if", leaderArr.length() + "");
                                                    tvProfileFragmentLeaderboardText.setVisibility(View.GONE);
                                                    ivProfileFragmentMore.setVisibility(View.GONE);
                                                } else {
                                                    Log.i("Profile leader else", leaderArr.length() + "");
                                                    if (leaderArr.length() < 10)
                                                        ivProfileFragmentMore.setVisibility(View.GONE);
//                                                else
//                                                    ivProfileFragmentMore.setVisibility(View.GONE);

                                                    tvProfileFragmentLeaderboardText.setVisibility(View.VISIBLE);
                                                }


                                                if (myDataset.size() > 0) {
                                                    mAdapter = new ProfileAdapter(getActivity(), myDataset);
                                                    lvProfileFragmentLeaderboardList.setAdapter(mAdapter);
                                                    setListViewHeightBasedOnChildren(lvProfileFragmentLeaderboardList);
                                                }
                                            } else {
//                                            MyUtils.showToast(getActivity(), "No rank10 videos found");
                                                tvProfileFragmentLeaderboardText.setVisibility(View.GONE);
                                                ivProfileFragmentMore.setVisibility(View.GONE);
                                            }

                                        } else
                                            Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                    } catch (Exception e) {
                                        Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                    }
                                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                                        mProgressHUD.dismiss();
                                }
                            });
                        }
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                        mProgressHUD.dismiss();
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
        int count,position;

        public getNewestVideo(int newestvideoCount) {
            count = newestvideoCount;
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

//            Log.i("camp id",getCampId);
            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            if (!userId.equals("0"))
                pair.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            pair.add(new BasicNameValuePair("page", count + ""));
            pair.add(new BasicNameValuePair("limit", "9"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_user_newestvideo_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        if(getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                mProgressHUD.dismiss();
                                    try {
                                        jo = new JSONObject(JsonString);
                                        String posts = jo.getString("message");
                                        int error = jo.getInt("error");
                                        if (error == 0) {

                                            JSONObject userMainObj = jo.getJSONObject("user");
                                            JSONArray leaderArr = jo.getJSONArray("videos");

                                            if (userMainObj.getString("status").equals("active")) {

                                                for (int i = 0; i < leaderArr.length(); i++) {
                                                    JSONObject joLeader = leaderArr.getJSONObject(i);
                                                    JSONObject videoObj = joLeader.getJSONObject("Video");
                                                    JSONObject userObj = joLeader.getJSONObject("User");

                                                    if (count == 1)
                                                        position = i + 1;
                                                    else
                                                        position = ((i + 1) + ((count - 1) * 10)) - (count - 1);
//                                                Log.i("NewPosition", position + "");

                                                    myDatasetNewestVideo.add(new NewestVideo(position,videoObj.getString("id"), videoObj.getString("photo_path"),"user",userMainObj.getString("id")));

                                                }

                                                if (newestvideoCount == 1 && leaderArr.length() == 0) {
                                                    Log.i("Profile newest if", leaderArr.length() + "");
                                                    tvProfileFragmentNewestText.setVisibility(View.GONE);
                                                    ivProfileFragmentNewestMore.setVisibility(View.GONE);
                                                } else {
                                                    Log.i("Profile newest else", leaderArr.length() + "");
                                                    if (leaderArr.length() < 9)
                                                        ivProfileFragmentNewestMore.setVisibility(View.GONE);
                                                    else
                                                        ivProfileFragmentNewestMore.setVisibility(View.VISIBLE);

                                                    tvProfileFragmentNewestText.setVisibility(View.VISIBLE);
//                                                ivProfileFragmentNewestMore.setVisibility(View.VISIBLE);
                                                }

                                                Log.i("newest video", leaderArr.length() + "");

                                                if (myDatasetNewestVideo.size() > 0) {
                                                    mImageAdapter = new ImageAdapter(getActivity(), getDeviceWidth(), myDatasetNewestVideo);
                                                    gridviewProfileFragmentNewestVideo.setAdapter(mImageAdapter);
//                                                if (leaderArr.length() < 9)
                                                    setGridViewHeightBasedOnChildren(gridviewProfileFragmentNewestVideo,3);
                                                }
                                            }
                                        } else
                                            Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                    } catch (Exception e) {
                                        Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                    }
                                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                                        mProgressHUD.dismiss();
                                }
                            });
                        }
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                        mProgressHUD.dismiss();
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    @OnClick(R2.id.btnProfileFragmentFollower)
    void setIsFollow() {
        Log.i("user_id", "IsFollow-" + IsFollow);
        if (IsFollow == 0) {
            IsFollow = 1;
            new setUserFollower().execute();
            tvProfileFragmentFollowersCount.setText(String.valueOf(followCount + 1));
            followCount = followCount + 1;
//            btnProfileFragmentFollower.setText("Following");
            btnProfileFragmentFollower.setBackgroundResource(R.drawable.button_background);
//            btnProfileFragmentFollower.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_white, 0, 0, 0);

            ImageSpan imageHint = new ImageSpan(getContext(), R.drawable.done_white, ImageSpan.ALIGN_BASELINE);
            SpannableString spannableString = new SpannableString("  FOLLOWING");
            spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            btnProfileFragmentFollower.setText(spannableString);

        } else {
            dialogUnFollowUser();
        }
    }

    private void dialogUnFollowUser() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.unfollow_user_dialog);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tvDialogUnfollowUserName);
        text.setText(username);
        ImageView image = (ImageView) dialog.findViewById(R.id.ivDialogUnfollowUserPic);
        if (!userpic.equals(""))
            Picasso.with(getActivity()).load(userpic)
                    .placeholder(R.drawable.user_default)
                    .error(R.drawable.user_default)
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
                tvProfileFragmentFollowersCount.setText(String.valueOf(followCount - 1));
                followCount = followCount - 1;
                btnProfileFragmentFollower.setText("+ FOLLOW");
//                btnProfileFragmentFollower.setBackgroundColor(getResources().getColor(R.color.opacityBottom));
                btnProfileFragmentFollower.setBackgroundResource(R.drawable.follow_background);
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
            pair.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.follow_user_url;
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
            pair.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.unfollow_user_url;
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
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
        if(listAdapter.getCount() % columns> 0){
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
