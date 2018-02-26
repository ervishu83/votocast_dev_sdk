package fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.apradanas.simplelinkabletext.LinkableTextView;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;
import com.votocast.votocast.VC_MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;
import com.votocast.votocast.VC_ReportsActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.CommentExplore;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import db.Reports;
import db.Comment;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements View.OnClickListener {

    @BindView(R2.id.civListItemVotocastProfilePic)
    CircleImageView civListItemVotocastProfilePic;
    @BindView(R2.id.tvListItemProfileName)
    TextView tvListItemProfileName;
    @BindView(R2.id.tvListItemProfileFollower)
    TextView tvListItemProfileFollower;
    @BindView(R2.id.ivListItemProfileSettingIcon)
    ImageView ivListItemProfileSettingIcon;

//    @BindView(R2.id.videoviewExploreVideoPlayer)
//    VideoPlayerView videoviewExploreVideoPlayer;

    @BindView(R2.id.tvListItemVideoLike)
    TextView tvListItemVideoLike;
    @BindView(R2.id.tvListItemVideoPlay)
    TextView tvListItemVideoPlay;
    @BindView(R2.id.tvListItemVideoShare)
    TextView tvListItemVideoShare;

    @BindView(R2.id.tvListItemProfileVote)
    TextView tvListItemProfileVote;
    @BindView(R2.id.tvListItemProfileShare)
    TextView tvListItemProfileShare;
    @BindView(R2.id.tvListItemProfileComment)
    TextView tvListItemProfileComment;

//    @BindView(R2.id.ivListItemDots)
//    ImageView ivListItemDots;
    @BindView(R2.id.tvListItemExploreDesc)
    LinkableTextView tvListItemExploreDesc;

    @BindView(R2.id.listComments)
    ListView listComments;

//    @BindView(R2.id.videoviewHomeVideo)
//    VideoView videoviewHomeVideo;

//    @BindView(R2.id.ivExploreVideoMute)
//    ImageView ivExploreVideoMute;

    @BindView(R2.id.ivExploreBanner)
    ImageView ivExploreBanner;
    @BindView(R2.id.scrollExplore)
    ScrollView scrollExplore;

    //    @BindView(R2.id.ivExploreVidPlay)
//    ImageView ivExploreVidPlay;
    @BindView(R2.id.ivExploreVidPrevious)
    ImageView ivExploreVidPrevious;
    @BindView(R2.id.ivExploreVidNext)
    ImageView ivExploreVidNext;

    @BindView(R2.id.llExploreVideoStrip)
    LinearLayout llExploreVideoStrip;
//    @BindView(R2.id.coverExplore)
//    ImageView coverExplore;

    @BindView(R2.id.videoplayerExplore)
    JCVideoPlayer videoplayerExplore;

    String view, like, followers, shares, rank, date;
    String didFollow, didLike,self_video;
    String vidId, userId;
    int valLike, valFollow, valShare;
    String videoUrl;

    ArrayList<Comment> list;
    ArrayList<Reports> reportList;
    CommentExplore adp;

    String username, userPic;
    String from = "";
    private String desc;
    String list_type, list_id, videotype, pos, prevVidId, nextVidId;
//    ProgressHUD mProgressHUD;
    ImageView toolbar_delete_button;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.vc_fragment_explore, container, false);
        ButterKnife.bind(this, v);

        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Explore");
        t.send(new HitBuilders.AppViewBuilder().build());

        ((VC_MainActivity)  getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((VC_MainActivity)  getActivity()).getSupportActionBar().show();

        Toolbar toolbar = (Toolbar)  getActivity().findViewById(R.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R.id.toolbarLogo);
        iv.setVisibility(View.VISIBLE);
        TextView toolText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolText.setVisibility(View.GONE);
        toolText.setText("");
        ImageView toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.GONE);
        toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);

        toolbar_delete_button = (ImageView) toolbar.findViewById(R.id.toolbar_delete_button);
        toolbar_delete_button.setVisibility(View.VISIBLE);

        toolbar_back_button.setOnClickListener(this);
        toolbar_delete_button.setOnClickListener(this);

        if (savedInstanceState != null) {
            vidId = savedInstanceState.getString("vidId");
            from = savedInstanceState.getString("from");
            list_type = savedInstanceState.getString("list_type");
            list_id = savedInstanceState.getString("list_id");
            videotype = savedInstanceState.getString("videotype");
            pos = savedInstanceState.getString("pos");
//            token = savedInstanceState.getString("token");
        } else {
            Bundle b1 = getArguments();
            vidId = b1.getString("vidId");
            from = b1.getString("from", "");
            list_type = b1.getString("list_type", "");
            list_id = b1.getString("list_id", "");
            videotype = b1.getString("videotype", "");
            pos = b1.getString("pos", "");
//            token = Constant.getShareData(getActivity(), "pref_login");
        }
//        Log.i("VideoDetail", list_type + "-" + list_id + "-" + videotype + "-" + pos);

        Constant.setTextFontsMedium(getActivity(), tvListItemProfileName);
        Constant.setTextFontsRegular(getActivity(), tvListItemProfileFollower);

        Constant.setTextFontsSemibold(getActivity(), tvListItemProfileVote);
        Constant.setTextFontsSemibold(getActivity(), tvListItemProfileShare);
        Constant.setTextFontsSemibold(getActivity(), tvListItemProfileComment);

        Constant.setTextFontsMedium(getActivity(), tvListItemVideoLike);
        Constant.setTextFontsMedium(getActivity(), tvListItemVideoPlay);
        Constant.setTextFontsMedium(getActivity(), tvListItemVideoShare);

        Constant.setTextFontsRegular(getActivity(), tvListItemExploreDesc);

        scrollExplore.post(new Runnable() {
            @Override
            public void run() {
                scrollExplore.fullScroll(View.FOCUS_UP);
            }
        });

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        JCVideoPlayer.releaseAllVideos();
                        if (from.equals("video")) {
//                            Log.i("fromExplore", from + "- if");
                            TabLayout.Tab tab = ((VC_MainActivity) getActivity()).getTabLayout().getTabAt(0);
                            tab.select();
                        } else {
//                            Log.i("fromExplore", from + "- else");
                            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                            if (fragmentManager.getBackStackEntryCount() > 0) {
                                fragmentManager.popBackStack();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        setRetainInstance(true);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        list = new ArrayList<Comment>();
        reportList = new ArrayList<Reports>();

        new getVideoDetail(vidId, pos).execute();
        new getComment(vidId).execute();
//        new setViewVideo(vidId).execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("vidId", vidId);
        outState.putString("from", from);
        outState.putString("list_type", list_type);
        outState.putString("list_id", list_id);
        outState.putString("videotype", videotype);
        outState.putString("pos", pos);
//        outState.putString("token",token);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R2.id.rlListItemOne)
    void fnGotoUser() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle b1 = new Bundle();
        b1.putString("user_id", userId);
        fragment.setArguments(b1);
        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

    @OnClick(R2.id.ivListItemProfileSettingIcon)
    void fnFollow() {
        if (didFollow.equals("true")) {
            dialogUnFollowUser();
        } else {
            didFollow = "true";
//            isFollow = isFollow + 1;
            valFollow = valFollow + 1;
            tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
            ivListItemProfileSettingIcon.setImageResource(R.drawable.person_green);
            new setUserFollower().execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @OnClick(R2.id.ivExploreVidPrevious)
    void fnGoToPrevious() {
        list = new ArrayList<Comment>();
        reportList = new ArrayList<Reports>();
        JCVideoPlayer.releaseAllVideos();

//        int newPos = Integer.parseInt(pos) - 1;
        pos = String.valueOf(Integer.valueOf(pos) - 1);
        Log.i("VideoDetail", "Prev- " + pos);
        new getVideoDetail(prevVidId, pos).execute();
        new getComment(prevVidId).execute();
//        new setViewVideo(prevVidId).execute();
        llExploreVideoStrip.setVisibility(View.GONE);
    }

    @OnClick(R2.id.ivExploreVidNext)
    void fnGoToNext() {

        list = new ArrayList<Comment>();
        reportList = new ArrayList<Reports>();
        JCVideoPlayer.releaseAllVideos();

//        int newPos = Integer.parseInt(pos) + 1;
        pos = String.valueOf(Integer.valueOf(pos) + 1);
        Log.i("VideoDetail", "Next- " + pos);
        new getVideoDetail(nextVidId, pos).execute();
        new getComment(nextVidId).execute();
//        new setViewVideo(nextVidId).execute();
        llExploreVideoStrip.setVisibility(View.GONE);
    }

    @OnClick(R2.id.tvListItemProfileVote)
    void fnExploreVote() {
        if (didLike.equals("true")) {
            //            isLike = isLike - 1;
            didLike = "false";
            valLike = valLike - 1;
            tvListItemVideoLike.setText(String.valueOf(valLike));
            tvListItemProfileVote.setTextColor(getActivity().getResources().getColor(R.color.homeVoteShare));
            new setUnLikeVideo().execute();

        } else {
//            isLike = isLike + 1;
            didLike = "true";
            valLike = valLike + 1;
            tvListItemVideoLike.setText(String.valueOf(valLike));
            tvListItemProfileVote.setTextColor(Color.parseColor(Constant.colorPrimary));
            new setLikeVideo().execute();
        }
    }

    @OnClick(R2.id.tvListItemProfileShare)
    void fnExploreShare() {
        JCVideoPlayer.releaseAllVideos();
        dialogShare();
    }

    @OnClick(R2.id.ivListItemDots)
    void fnDotsShare() {
        JCVideoPlayer.releaseAllVideos();
        dialogShare();
    }

    @OnClick(R2.id.tvListItemProfileComment)
    void fnExploreComment() {
        JCVideoPlayer.releaseAllVideos();
        CommentsFragment fragment = new CommentsFragment();
        Bundle b1 = new Bundle();
        b1.putString("vidId", vidId);
        b1.putString("pic", userPic);
        b1.putString("name", username);
        b1.putString("desc", desc);
        b1.putString("date", date);
        b1.putString("userId", userId);
        fragment.setArguments(b1);
        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_back_button) {
            JCVideoPlayer.releaseAllVideos();
            if (from.equals("video")) {
                Log.i("fromExplore", from + "- if");
                TabLayout.Tab tab = ((VC_MainActivity) getActivity()).getTabLayout().getTabAt(0);
                tab.select();
            } else {
                Log.i("fromExplore", from + "- else");
                FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
            }
        }else if(view.getId() == R.id.toolbar_delete_button){
            Log.e("******","delete btn click");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("VOTOCAST")
                    .setMessage("Are you sure want to delete this video? ")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            new deleteVideo(vidId).execute();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    class deleteVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String videoId;

        public deleteVideo(String videoId) {
            this.videoId = videoId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", videoId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.delete_video_url;
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
                                        JCVideoPlayer.releaseAllVideos();
                                        if (from.equals("video")) {
                                            Log.i("fromExplore", from + "- if");
                                            TabLayout.Tab tab = ((VC_MainActivity) getActivity()).getTabLayout().getTabAt(0);
                                            tab.select();
                                        } else {
                                            Log.i("fromExplore", from + "- else");
                                            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                            if (fragmentManager.getBackStackEntryCount() > 0) {
                                                fragmentManager.popBackStack();
                                            }
                                        }
                                    } else if (error == 2) {
                                        final AlertDialog.Builder myShareDialog = new AlertDialog.Builder(getActivity());
                                        myShareDialog.setTitle("O2Life")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
                                    } else if (error == 1) {
                                        Constant.ShowErrorMessage("Error", posts, getActivity());
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class getVideoDetail extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String campId;
        String vidId;
        String pos;
        ProgressHUD mProgressHUD1;

        public getVideoDetail(String vidId, String pos) {
            this.vidId = vidId;
            this.pos = pos;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD1 = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
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
            pair.add(new BasicNameValuePair("video_id", vidId));

            if (!list_type.equals(""))
                pair.add(new BasicNameValuePair("list_type", list_type));
            if (!list_id.equals(""))
                pair.add(new BasicNameValuePair("list_id", list_id));
            if (!videotype.equals(""))
                pair.add(new BasicNameValuePair("videotype", videotype));
            if (!pos.equals(""))
                pair.add(new BasicNameValuePair("pos", pos));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_video_url;
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

                                        JSONObject vidObj = jo.getJSONObject("Video");
                                        JSONObject userObj = jo.getJSONObject("User");
                                        JSONObject campObj = jo.getJSONObject("Campaign");

                                        view = vidObj.getString("views");
                                        like = jo.getString("likes");
                                        followers = jo.getString("followers");
                                        shares = vidObj.getString("shares");
                                        rank = vidObj.getString("rank");
                                        date = vidObj.getString("created_on");

                                        String myUserId = Constant.getShareData(getActivity(), "myId");
                                        if (myUserId.equals(userObj.getString("id")))
                                            ivListItemProfileSettingIcon.setVisibility(View.GONE);
                                        else
                                            ivListItemProfileSettingIcon.setVisibility(View.VISIBLE);

                                        if (!jo.getString("cnt_comments").equals("0"))
                                            tvListItemProfileComment.setText("COMMENTS(" + jo.getString("cnt_comments") + ")");
                                        else
                                            tvListItemProfileComment.setText("COMMENTS");

                                        if (Integer.parseInt(rank) <= 10) {
                                            if (vidObj.getString("is_winner").equals("yes"))
                                                ivExploreBanner.setImageResource(R.drawable.winner_small);
                                            else {
                                                switch (Integer.parseInt(rank)) {
                                                    case 1:
                                                        ivExploreBanner.setImageResource(R.drawable.rank1);
                                                        break;
                                                    case 2:
                                                        ivExploreBanner.setImageResource(R.drawable.rank2);
                                                        break;
                                                    case 3:
                                                        ivExploreBanner.setImageResource(R.drawable.rank3);
                                                        break;
                                                    case 4:
                                                        ivExploreBanner.setImageResource(R.drawable.rank4);
                                                        break;
                                                    case 5:
                                                        ivExploreBanner.setImageResource(R.drawable.rank5);
                                                        break;
                                                    case 6:
                                                        ivExploreBanner.setImageResource(R.drawable.rank6);
                                                        break;
                                                    case 7:
                                                        ivExploreBanner.setImageResource(R.drawable.rank7);
                                                        break;
                                                    case 8:
                                                        ivExploreBanner.setImageResource(R.drawable.rank8);
                                                        break;
                                                    case 9:
                                                        ivExploreBanner.setImageResource(R.drawable.rank9);
                                                        break;
                                                    case 10:
                                                        ivExploreBanner.setImageResource(R.drawable.rank10);
                                                        break;
                                                }
                                            }
                                            ivExploreBanner.setVisibility(View.VISIBLE);
                                        } else
                                            ivExploreBanner.setVisibility(View.GONE);

                                        valFollow = Integer.parseInt(followers);
                                        valLike = Integer.parseInt(like);
                                        valShare = Integer.parseInt(shares);

                                        didFollow = jo.getString("didfollowed");
                                        didLike = jo.getString("didliked");
                                        self_video = jo.getString("self_video");

                                        if(self_video.equals("yes"))
                                            toolbar_delete_button.setVisibility(View.VISIBLE);
                                        else
                                            toolbar_delete_button.setVisibility(View.INVISIBLE);

                                        if (didFollow.equals("true")) {
                                            ivListItemProfileSettingIcon.setImageResource(R.drawable.person_green);
                                        } else {
                                            ivListItemProfileSettingIcon.setImageResource(R.drawable.person_orange);
                                        }

                                        if (didLike.equals("true"))
                                            tvListItemProfileVote.setTextColor(Color.parseColor(Constant.colorPrimary));
                                        else
                                            tvListItemProfileVote.setTextColor(getActivity().getResources().getColor(R.color.homeVoteShare));

                                        tvListItemVideoLike.setText(like);
                                        tvListItemVideoPlay.setText(view);
                                        tvListItemVideoShare.setText(shares);
                                        videoUrl = vidObj.getString("video_path");

                                        if (vidObj.getString("photo_path") != null && !vidObj.getString("photo_path").equals("")) {
                                            Glide.with(getActivity()).load(vidObj.getString("photo_path")).placeholder(R.drawable.backimage).into(videoplayerExplore.ivThumb);
                                        } else {
                                            Glide.with(getActivity()).load(R.drawable.backimage).into(videoplayerExplore.ivThumb);
                                        }
                                        String token = Constant.getShareData(getActivity(), "pref_login");
                                        videoplayerExplore.setVideoId(Integer.parseInt(vidObj.getString("id")), token, Constant.video_show_url , Constant.Authorization);
                                        videoplayerExplore.setUp(vidObj.getString("video_path"), "");
                                        videoplayerExplore.setVideoViews(view);
                                        videoplayerExplore.setVideoLike(like);

//                                        mVideoPlayerManager.playNewVideo(null, videoviewExploreVideoPlayer, vidObj.getString("video_path"));

                                        campId = campObj.getString("id");

                                        userId = userObj.getString("id");
                                        tvListItemProfileName.setText(userObj.getString("username"));
                                        tvListItemProfileFollower.setText(followers + " followers");

                                        username = userObj.getString("username");
                                        userPic = userObj.getString("photo_path");

                                        if (!userObj.getString("photo_path").equals("")) {
                                            Picasso.with(getActivity()).load(userObj.getString("photo_path"))
                                                    .placeholder(R.drawable.campaign_icon)
                                                    .error(R.drawable.campaign_icon)
                                                    .into(civListItemVotocastProfilePic);
                                        }else
                                            Picasso.with(getActivity()).load(R.drawable.campaign_icon).into(civListItemVotocastProfilePic);

                                        Link linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                                                .setUnderlined(false)
                                                .setTextColor(Color.parseColor(Constant.colorPrimary))
                                                .setClickListener(new Link.OnClickListener() {
                                                    @Override
                                                    public void onClick(String text) {
                                                        // do something
                                                        CampaignFragment fragment = new CampaignFragment();
                                                        Bundle b1 = new Bundle();
                                                        b1.putString("campId", campId);
//                                                        b1.putString("from", "explore");
//                                                        b1.putString("fromId", vidId);
                                                        fragment.setArguments(b1);
                                                        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                        FragmentTransaction ft = fm.beginTransaction();
                                                        ft.replace(R.id.frame_container, fragment);
                                                        ft.addToBackStack(fragment.getClass().getName());
//                                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                        ft.commit();

                                                    }
                                                });
                                        Link linkWithoutHashtag = new Link(Pattern.compile("(VC_\\w+)"))
                                                .setUnderlined(false)
                                                .setTextColor(Color.parseColor(Constant.colorPrimary))
                                                .setClickListener(new Link.OnClickListener() {
                                                    @Override
                                                    public void onClick(String text) {
                                                        // do something
                                                        CampaignFragment fragment = new CampaignFragment();
                                                        Bundle b1 = new Bundle();
                                                        b1.putString("campId", campId);
//                                                        b1.putString("from", "explore");
//                                                        b1.putString("fromId", vidId);
                                                        fragment.setArguments(b1);
                                                        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                        FragmentTransaction ft = fm.beginTransaction();
                                                        ft.replace(R.id.frame_container, fragment);
                                                        ft.addToBackStack(fragment.getClass().getName());
//                                                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                        ft.commit();

                                                    }
                                                });
                                        List<Link> links = new ArrayList<>();
                                        links.add(linkHashtag);
                                        links.add(linkWithoutHashtag);

                                        Log.i("explore", vidObj.getString("description"));
                                        desc = vidObj.getString("description").replace("\\","");
//                                        tvListItemExploreDesc.setText(vidObj.getString("description"));
                                        tvListItemExploreDesc.setText(desc + " " + campObj.getString("title")).addLinks(links).build();

                                        JSONArray reportArr = jo.getJSONArray("flags");
                                        for (int i = 0; i < reportArr.length(); i++) {
                                            JSONObject reportObj = reportArr.getJSONObject(i);
                                            JSONObject catObj = reportObj.getJSONObject("VideoReportCategory");

                                            reportList.add(new Reports(catObj.getString("id"), catObj.getString("title"), catObj.getString("description")));
                                        }

                                        if (jo.has("prev")) {
                                            ivExploreVidPrevious.setVisibility(View.VISIBLE);
                                            JSONObject prevObj = jo.getJSONObject("prev");
                                            JSONObject data = prevObj.getJSONObject("Video");
                                            prevVidId = data.getString("id");
                                        } else
                                            ivExploreVidPrevious.setVisibility(View.INVISIBLE);

                                        if (jo.has("next")) {
                                            ivExploreVidNext.setVisibility(View.VISIBLE);
                                            JSONObject nextObj = jo.getJSONObject("next");
                                            JSONObject data = nextObj.getJSONObject("Video");
                                            nextVidId = data.getString("id");
                                        } else
                                            ivExploreVidNext.setVisibility(View.INVISIBLE);

                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                                if (mProgressHUD1.isShowing() && mProgressHUD1 != null)
                                    mProgressHUD1.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD1.isShowing() && mProgressHUD1 != null)
                            mProgressHUD1.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class getComment extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        String vidId;

        public getComment(String vidId) {
            this.vidId = vidId;
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
            pair.add(new BasicNameValuePair("video_id", vidId));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_comment_video_url;
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

                                        Constant.serverDate = jo.getString("current_datetime");
//                                        Log.i("explore Date",jo.getString("current_datetime") + " - " +Constant.serverDate);

                                        JSONArray commArr = jo.getJSONArray("comments");
                                        for (int i = 0; i < commArr.length(); i++) {
                                            JSONObject commObj = commArr.getJSONObject(i);
                                            JSONObject vidObj = commObj.getJSONObject("VideoComment");
                                            JSONObject userObj = commObj.getJSONObject("User");
                                            JSONObject blankObj = commObj.getJSONObject("0");

                                            list.add(new Comment(vidObj.getString("id"), vidObj.getString("video_id"), userObj.getString("id"), vidObj.getString("comments"), userObj.getString("username"), userObj.getString("photo_path"), vidObj.getString("created_on"),blankObj.getString("self_commented")));
                                        }
                                        adp = new CommentExplore(getActivity(), list);
                                        listComments.setAdapter(adp);
                                        setListViewHeightBasedOnChildren(listComments);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
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

    private void dialogUnFollowUser() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vc_unfollow_user_dialog);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.tvDialogUnfollowUserName);
        text.setText(username);
        ImageView image = (ImageView) dialog.findViewById(R.id.ivDialogUnfollowUserPic);
        if (!userPic.equals(""))
            Picasso.with(getActivity()).load(userPic)
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
                //            isFollow = isFollow - 1;
                didFollow = "false";
                valFollow = valFollow - 1;
                tvListItemProfileFollower.setText(String.valueOf(valFollow) + " followers");
                ivListItemProfileSettingIcon.setImageResource(R.drawable.person_orange);
                new setUserUnFollower().execute();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    class setUserFollower extends AsyncTask<Void, Void, Void> {
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
                            }
                        });
                    } else {
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
        ArrayList<NameValuePair> pair;

        protected void onPreExecute() {
            super.onPreExecute();
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
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setLikeVideo extends AsyncTask<Void, Void, Void> {
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
            pair.add(new BasicNameValuePair("video_id", String.valueOf(vidId)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.like_video_url;
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
//                                        MyUtils.showToast(getActivity(), posts);
                                    } else if (error == 2) {
                                        final AlertDialog.Builder myVoteExploreDialog = new AlertDialog.Builder(getActivity());
                                        myVoteExploreDialog.setTitle("VOTOCAST")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        didLike = "false";
                                                        valLike = valLike - 1;
                                                        tvListItemVideoLike.setText(String.valueOf(valLike));
                                                        tvListItemProfileVote.setTextColor(getActivity().getResources().getColor(R.color.homeVoteShare));
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        didLike = "false";
                                                        valLike = valLike - 1;
                                                        tvListItemVideoLike.setText(String.valueOf(valLike));
                                                        tvListItemProfileVote.setTextColor(getActivity().getResources().getColor(R.color.homeVoteShare));
                                                    }
                                                })
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setResendEmail extends AsyncTask<Void, Void, Void> {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.resend_email_url;
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
                                        MyUtils.showToast(getActivity(), posts);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    class setUnLikeVideo extends AsyncTask<Void, Void, Void> {
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
            pair.add(new BasicNameValuePair("video_id", String.valueOf(vidId)));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.unlike_video_url;
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
//                                        MyUtils.showToast(getActivity(), posts);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    public void dialogShare() {
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vc_share_dialog);

        // set values for custom dialog components - text, image and button
        TextView tvShareReports = (TextView) dialog.findViewById(R.id.tvShareReports);
        TextView tvShareFb = (TextView) dialog.findViewById(R.id.tvShareFb);
        TextView tvShareTweet = (TextView) dialog.findViewById(R.id.tvShareTweet);
        TextView tvShareCopy = (TextView) dialog.findViewById(R.id.tvShareCopy);
        TextView tvDeleteVideo = (TextView) dialog.findViewById(R.id.tvDeleteVideo);
        tvDeleteVideo.setVisibility(View.GONE);

        tvShareReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m1 = new Intent(getActivity(), VC_ReportsActivity.class);
                m1.putExtra("vidId", vidId);
                m1.putExtra("report", reportList);
                startActivity(m1);
                dialog.dismiss();
            }
        });
        tvShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setShareVideo("facebook").execute();
                dialog.dismiss();
            }
        });
        tvShareTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setShareVideo("twitter").execute();
                dialog.dismiss();
            }
        });
        tvShareCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setShareVideo("url").execute();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("Encode", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
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

    class setShareVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String type;

        public setShareVideo(String type) {
            this.type = type;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(getActivity(), "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", String.valueOf(vidId)));
            pair.add(new BasicNameValuePair("type", type));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.video_share_url;
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

                                        if(type.equals("facebook"))
                                        {
                                            ShareDialog shareDialog;
                                            FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
                                            shareDialog = new ShareDialog(getActivity());
                                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                    .setContentUrl(Uri.parse(Constant.main_url + "/video/" + vidId))
                                                    .build();
                                            shareDialog.show(linkContent);

                                            tvListItemVideoShare.setText(String.valueOf(valShare + 1));
                                        }else if(type.equals("twitter")){
                                            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                                                    urlEncode(""),
                                                    urlEncode(Constant.main_url + "/video/" + vidId));
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

                                            List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                                            for (ResolveInfo info : matches) {
                                                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                                                    intent.setPackage(info.activityInfo.packageName);
                                                }
                                            }
                                            getActivity().startActivity(intent);
                                            tvListItemVideoShare.setText(String.valueOf(valShare + 1));
                                        }else if(type.equals("url"))
                                        {
                                            int sdk = android.os.Build.VERSION.SDK_INT;
                                            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                clipboard.setText(Constant.main_url + "/video/" + vidId);
                                                MyUtils.showToast(getActivity(), "Url copied!");
                                            } else {
                                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                android.content.ClipData clip = android.content.ClipData.newPlainText("Video", Constant.main_url + "/video/" + vidId);
                                                clipboard.setPrimaryClip(clip);
                                                MyUtils.showToast(getActivity(), "Url copied!");
                                            }
                                            tvListItemVideoShare.setText(String.valueOf(valShare + 1));
                                        }

                                    } else if (error == 2) {
                                        final AlertDialog.Builder myVoteExploreDialog = new AlertDialog.Builder(getActivity());
                                        myVoteExploreDialog.setTitle("VOTOCAST")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), getActivity());
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
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
