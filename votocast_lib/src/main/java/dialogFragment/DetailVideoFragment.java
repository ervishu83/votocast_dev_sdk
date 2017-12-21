package dialogFragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.entity.mime.content.ByteArrayBody;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.votocast.votocast.MainActivity;
import com.votocast.votocast.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import class_adapter.AndroidMultiPartEntity;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import fragments.ExploreFragment;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailVideoFragment extends Fragment implements View.OnClickListener {

    public static final String PARAM_FILE = "filePath";
    //    private static int serverResponseCode;
    TextView tvDetailPopupCampaigns, tvDetailPopupProducers;
    EditText evDetailPopupAboutVideo;
    Button btnDetailPopupShareFbClick, btnDetailPopupShareTwitter, btnDetailPopupSubmit;
    private String filePath = "";
    VideoView videoviewDetailVideo;
    TextView tvDetailDialogTitle;
    ImageView ivDetailVideoMute;

    private static Twitter twitter;
    private static RequestToken requestToken;

    String oauth_url, oauth_verifier, profile_url;
    Dialog auth_dialog;
    WebView web;
    AccessToken accessToken;
    Bitmap videoThumb;
    byte[] videoThumbByteArray;

    CallbackManager callbackManager;

    LoginButton btnDetailPopupShareFb;
    private String my_access_token, is_fb_connect, is_twitter_connect;
    String desc;

    int isShareFb, isShareTwitter;
    private ArrayList<NameValuePair> pair;

    String short_code, campId, username;
    String twitterMetadata, fbMetadata;

    VideoPlayerView videoviewDetailVideoPlayer;
    ImageView ivDetailVidPlay;
    long totalSize = 0, imageSize = 0;

    private VideoPlayerManager<MetaData> mVideoPlayerManager1 = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });
    private int isMute = 0;
    private int isPlay = 0;
    private String token;

    public DetailVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View v = inflater.inflate(R.layout.fragment_detail_video, container, false);

//        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
//        t.setScreenName("Detail Video");
//        t.send(new HitBuilders.AppViewBuilder().build());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        if (Build.VERSION.SDK_INT >= 19) // KITKAT
//        {
//            web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        tvDetailDialogTitle = (TextView) v.findViewById(R.id.tvDetailDialogTitle);
        Constant.setDisplayFontsSemibold(getActivity(), tvDetailDialogTitle);
        ivDetailVideoMute = (ImageView) v.findViewById(R.id.ivDetailVideoMute);
        videoviewDetailVideoPlayer = (VideoPlayerView) v.findViewById(R.id.videoviewDetailVideoPlayer);
        btnDetailPopupShareFbClick = (Button) v.findViewById(R.id.btnDetailPopupShareFbClick);

        ImageView detailDialogBackBtn = (ImageView) v.findViewById(R.id.detailDialogBackBtn);
        ImageView detailDialogNextBtn = (ImageView) v.findViewById(R.id.detailDialogNextBtn);
        ivDetailVidPlay = (ImageView) v.findViewById(R.id.ivDetailVidPlay);

        tvDetailPopupCampaigns = (TextView) v.findViewById(R.id.tvDetailPopupCampaigns);
        tvDetailPopupProducers = (TextView) v.findViewById(R.id.tvDetailPopupProducers);

        evDetailPopupAboutVideo = (EditText) v.findViewById(R.id.evDetailPopupAboutVideo);

        btnDetailPopupShareFb = (LoginButton) v.findViewById(R.id.btnDetailPopupShareFb);
        btnDetailPopupShareTwitter = (Button) v.findViewById(R.id.btnDetailPopupShareTwitter);
        btnDetailPopupSubmit = (Button) v.findViewById(R.id.btnDetailPopupSubmit);

        videoviewDetailVideo = (VideoView) v.findViewById(R.id.videoviewDetailVideo);

        username = Constant.getShareData(getContext(), "username");
        tvDetailPopupProducers.setText(username);

        campId = Constant.getShareData(getActivity(), "camp_id");
        short_code = Constant.getShareData(getActivity(), "short_code");
        if (!short_code.equals(""))
            tvDetailPopupCampaigns.setText(short_code);

        detailDialogBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoPlayerManager1.resetMediaPlayer();
                FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
            }
        });

        tvDetailPopupCampaigns.setTextColor(Color.parseColor(Constant.colorPrimary));
        tvDetailPopupProducers.setTextColor(Color.parseColor(Constant.colorPrimary));
        btnDetailPopupSubmit.setBackgroundColor(Color.parseColor(Constant.colorPrimary));

        tvDetailPopupCampaigns.setOnClickListener(this);
        ivDetailVideoMute.setOnClickListener(this);

        btnDetailPopupShareFb.setBackgroundResource(R.drawable.connect);
        btnDetailPopupShareFb.setText("");
        btnDetailPopupShareFb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        Log.i("detailpath", (savedInstanceState != null) + "-" + filePath);
        if (savedInstanceState != null) {
            Log.i("detailpath inside", savedInstanceState.getString("filepath"));
//            token = savedInstanceState.getString("token");
            filePath = savedInstanceState.getString("filepath");
            mVideoPlayerManager1.playNewVideo(null, videoviewDetailVideoPlayer, filePath);
        } else {
            Bundle bundle = getArguments();
            filePath = bundle.getString(PARAM_FILE, "");
            Log.i("detailpath", filePath);
            mVideoPlayerManager1.playNewVideo(null, videoviewDetailVideoPlayer, filePath);
//            token = Constant.getShareData(getActivity(), "pref_login");
        }

        videoviewDetailVideoPlayer.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
            @Override
            public void onVideoPreparedMainThread() {
                // We hide the cover when video is prepared. Playback is about to start
//                mVideoCover.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoStoppedMainThread() {
                // We show the cover when video is stopped
//                mVideoCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                // We show the cover when video is completed
//                mVideoCover.setVisibility(View.VISIBLE);
                Log.i("detailpath thread", filePath);
                mVideoPlayerManager1.playNewVideo(null, videoviewDetailVideoPlayer, filePath);
            }
        });

        token = Constant.getShareData(getActivity(), "pref_login");
        my_access_token = Constant.getShareData(getActivity(), "pref_login");
        is_fb_connect = Constant.getShareData(getActivity(), "is_fab_connect");
        is_twitter_connect = Constant.getShareData(getActivity(), "is_twitter_connect");

        Log.i("fb", is_fb_connect);
        Log.i("twit", is_twitter_connect);

        btnDetailPopupShareTwitter.setOnClickListener(this);
        btnDetailPopupShareFb.setOnClickListener(this);
        btnDetailPopupShareFbClick.setOnClickListener(this);
        btnDetailPopupSubmit.setOnClickListener(this);

        videoThumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        videoThumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
        videoThumbByteArray = stream.toByteArray();

        if (is_fb_connect.equals("false")) {
            btnDetailPopupShareFbClick.setVisibility(View.GONE);
            callbackManager = CallbackManager.Factory.create();
            btnDetailPopupShareFb.setBackgroundResource(R.drawable.connect);
            btnDetailPopupShareFb.setReadPermissions(Arrays.asList("email"));
            btnDetailPopupShareFb.setFragment(this);
            btnDetailPopupShareFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    System.out.println("onSuccess");
                    ProgressHUD mProgressHUD;
                    mProgressHUD = ProgressHUD.show(getContext(), "", false, false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    btnDetailPopupShareFb.setBackgroundResource(R.drawable.no);
                    isShareFb = 0;
                    final String accessToken = loginResult.getAccessToken().getToken();
                    Log.i("accessToken1", accessToken + "-" + loginResult.getAccessToken().getExpires());

                    Constant.saveSharedData(getActivity(), "is_fab_connect", "true");
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.i("LoginActivity", response.toString());
                            // Get facebook data from login

                            JSONObject user = (JSONObject) response.getJSONObject();
                            try {
                                String email = user.getString("email");
                                Log.i("LoginActivity", object.toString());

                                fbMetadata = user.toString();
                                new addFbToken(accessToken).execute();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("LoginActivity1", e.toString());
                                Constant.ShowFbErrorMessage("Error", "We are unable to get email from your Facebook account. Please try again", getActivity());
//                            LoginManager.getInstance().logOut();
//                            Toast.makeText(LoginActivity.this, "Your Facebook account is not verified", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                    request.setParameters(parameters);
                    request.executeAsync();
                    mProgressHUD.dismiss();
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });
        } else {
            btnDetailPopupShareFbClick.setVisibility(View.VISIBLE);
            btnDetailPopupShareFb.setVisibility(View.GONE);
            btnDetailPopupShareFbClick.setBackgroundResource(R.drawable.no_upload);
            isShareFb = 0;
        }

        if (is_twitter_connect.equals("true")) {
            isShareTwitter = 0;
            btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.no_upload);
        } else {
            btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.connect);
        }

        detailDialogNextBtn.setOnClickListener(this);
        videoviewDetailVideoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoviewDetailVideoPlayer.getVideoUrlDataSource() != null) {
                    String state = videoviewDetailVideoPlayer.getCurrentState().toString();
                    if (isPlay == 0) {
                        if (state.equals("STARTED")) {
                            ivDetailVidPlay.setVisibility(View.VISIBLE);
                            videoviewDetailVideoPlayer.pause();
                            isPlay = 1;
                        }
                    } else {
                        if (state.equals("PAUSED")) {
                            ivDetailVidPlay.setVisibility(View.GONE);
                            videoviewDetailVideoPlayer.start();
                            isPlay = 0;
                        }
                    }
                }
            }
        });
        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mVideoPlayerManager1.resetMediaPlayer();
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
        mVideoPlayerManager1.playNewVideo(null, videoviewDetailVideoPlayer, filePath);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.i("detailpath activity", filePath);
//        filePath = savedInstanceState.getString("filepath");
//        mVideoPlayerManager1.playNewVideo(null, videoviewDetailVideoPlayer, filePath);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filepath", filePath);
        outState.putString("token", token);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constant.getShareData(getActivity(), "is_fab_connect").equals("false")) {
//            Log.i("fb activity if", Constant.getShareData(getActivity(), "is_fab_connect"));
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.detailDialogNextBtn) {
            desc = evDetailPopupAboutVideo.getText().toString().trim();
            if (desc.length() > 0) {
                Log.i("submit", campId + "--" + desc + "--" + isShareFb + "--" + isShareTwitter);

                if (!campId.equals("")) {
                    if (videoviewDetailVideoPlayer.getVideoUrlDataSource() != null) {
                        String state = videoviewDetailVideoPlayer.getCurrentState().toString();
                        if (isPlay == 0) {
                            if (state.equals("STARTED")) {
                                ivDetailVidPlay.setVisibility(View.VISIBLE);
                                videoviewDetailVideoPlayer.pause();
                                isPlay = 1;
                            }
                        }
                    }
                    new sendVideo().execute();
                } else
                    MyUtils.showToast(getActivity(), "Select campaign !");
            } else
                MyUtils.showToast(getActivity(), "Enter something about video !");
        }
        if (view.getId() == R.id.ivDetailVideoMute) {
            Log.i("mute click", "click-" + isMute);
            if (isMute == 0) {
                isMute = isMute + 1;
                videoviewDetailVideoPlayer.muteVideo();
                ivDetailVideoMute.setImageResource(R.drawable.mute);
            } else {
                isMute = isMute - 1;
                videoviewDetailVideoPlayer.unMuteVideo();
                ivDetailVideoMute.setImageResource(R.drawable.sound);
            }
        }
//        if (view.getId() == R.id.btnDetailPopupShareFb) {
//            if (Constant.getShareData(getActivity(), "is_fab_connect").equals("true")) {
//                if (isShareFb == 0) {
//                    btnDetailPopupShareFb.setBackgroundResource(R.drawable.yes);
//                    isShareFb = 1;
//                } else {
//                    btnDetailPopupShareFb.setBackgroundResource(R.drawable.no);
//                    isShareFb = 0;
//                }
//            }
//        }
        if (view.getId() == R.id.btnDetailPopupShareFbClick) {
            if (Constant.getShareData(getActivity(), "is_fab_connect").equals("true")) {
                if (isShareFb == 0) {
                    btnDetailPopupShareFbClick.setBackgroundResource(R.drawable.yes_upload);
                    isShareFb = 1;
                } else {
                    btnDetailPopupShareFbClick.setBackgroundResource(R.drawable.no_upload);
                    isShareFb = 0;
                }
            }
        }
        if (view.getId() == R.id.btnDetailPopupShareTwitter) {
            if (Constant.getShareData(getActivity(), "is_twitter_connect").equals("false")) {
                twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret));
                new TokenGet().execute();
            } else {
                if (isShareTwitter == 0) {
                    btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.yes_upload);
                    isShareTwitter = 1;
                } else {
                    btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.no_upload);
                    isShareTwitter = 0;
                }
            }
        }
        if (view.getId() == R.id.btnDetailPopupSubmit) {
            desc = evDetailPopupAboutVideo.getText().toString().trim();
            if (desc.length() > 0) {
//                upLoad2Server(filePath, my_access_token, desc, videoByteArray);
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
                Log.i("submit", campId + "--" + desc + "--" + isShareFb + "--" + isShareTwitter);

                if (!campId.equals("")) {
                    if (videoviewDetailVideoPlayer.getVideoUrlDataSource() != null) {
                        String state = videoviewDetailVideoPlayer.getCurrentState().toString();
                        if (isPlay == 0) {
                            if (state.equals("STARTED")) {
                                ivDetailVidPlay.setVisibility(View.VISIBLE);
                                videoviewDetailVideoPlayer.pause();
                                isPlay = 1;
                            }
                        }
                    }
                    new sendVideo().execute();
                } else
                    MyUtils.showToast(getActivity(), "Select campaign !");
//                    }
//                });
            } else
                MyUtils.showToast(getActivity(), "Enter something about video !");
        }
        if (view.getId() == R.id.tvDetailPopupCampaigns) {
            campListDialog();
        }
    }

    private void campListDialog() {

        ArrayList<NameValuePair> pair;

        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radio_camp_list);

        // set values for custom dialog components - text, image and button
        final RadioGroup campRadioGroup = (RadioGroup) dialog.findViewById(R.id.campRadioGroup);
        String token = Constant.getShareData(getActivity(), "pref_login");
        pair = new ArrayList<NameValuePair>();
        pair.add(new BasicNameValuePair("access_token", token));

        String cURL = Constant.get_campaings_list_to_upload_url;
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
                                JSONObject jo = new JSONObject(JsonString);
                                String posts = jo.getString("message");
                                int error = jo.getInt("error");
                                if (error == 0) {

                                    JSONArray campArr = jo.getJSONArray("campaigns_list");
                                    for (int i = 0; i < campArr.length(); i++) {
                                        JSONObject campObj = campArr.getJSONObject(i);
                                        final JSONObject dataObj = campObj.getJSONObject("Campaign");

                                        RadioButton button = new RadioButton(getActivity());

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                        button.setLayoutParams(params);

                                        button.setId(Integer.parseInt(dataObj.getString("id")));
                                        button.setText(dataObj.getString("title"));
                                        button.setPadding(8, 8, 8, 8);
//                                            button.setChecked(i == currentHours); // Only select button with same index as currently selected number of hours
                                        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                Log.i("radio", compoundButton.getId() + "-" + b);
                                                try {
                                                    if (compoundButton.getId() == Integer.parseInt(dataObj.getString("id")) && b == true) {
                                                        tvDetailPopupCampaigns.setText(dataObj.getString("title"));
                                                        campId = dataObj.getString("id");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                dialog.dismiss();
                                            }
                                        });
                                        campRadioGroup.addView(button);
                                    }

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

        dialog.show();
    }

    private class TokenGet extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return oauth_url;
        }

        @Override
        protected void onPostExecute(String oauth_url) {
            if (oauth_url != null) {
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(getActivity());
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView) auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }


                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && authComplete == false) {
                            authComplete = true;
                            Log.e("Url", url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");

                            try {
                                accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                                Log.e("AccessToken", accessToken.toString());
                                long userID = accessToken.getUserId();
                                final User user = twitter.showUser(userID);
                                String username = user.getName();
                                Log.e("Twitter username", username);

                                Log.e("Twitter ACCESS_TOKEN", accessToken.getToken());
                                Log.e("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());

//                                btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.no);
//                                isShareTwitter = 0;
//                                Constant.saveSharedData(getActivity(), "is_twitter_connect", "true");

                                Log.e("ACCESS_USEr", accessToken.getToken() + "-" + user.toString());
                                twitterMetadata = user.toString();
                                new addTwitterToken(accessToken.getToken()).execute();

                                profile_url = user.getOriginalProfileImageURL();
                                Log.e("Twitter IMAGE_URL", user.getOriginalProfileImageURL());

                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }

                            auth_dialog.dismiss();
                        } else if (url.contains("denied")) {
                            auth_dialog.dismiss();
                            Toast.makeText(getActivity(), "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);
            } else {
                Toast.makeText(getActivity(), "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class sendVideo extends AsyncTask<Void, Integer, JSONObject> {
        ProgressHUD mProgressHUD;
        JSONObject obj;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//            mProgressDialog.show();
            super.onPreExecute();

            mProgressHUD = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            Log.i("Progress", String.valueOf(values[0]) + "%");
            mProgressHUD.setMessage(String.valueOf(values[0]) + "%");
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constant.add_video_url);
            httppost.addHeader("Authorization",Constant.Authorization);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) (((num / 1024) * 100) / ((new File(filePath).length())/1024)));
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
//                File sourceFile = new File(filePath);
//
////                // Adding file data to http body
//                entity.addPart("image", new FileBody(sourceFile));
//
//                // Extra parameters if you want to pass to server
//                entity.addPart("website",
//                        new StringBody("www.androidhive.info"));
//                entity.addPart("email", new StringBody("abc@gmail.com"));
                entity.addPart("access_token", new StringBody(token));
                entity.addPart("campaign_id", new StringBody(campId));
                entity.addPart("description", new StringBody(desc));

                Log.e("UploadVideo**",token + " - " + campId );

                if (isShareFb == 0)
                    entity.addPart("facebook", new StringBody("false"));
                else
                    entity.addPart("facebook", new StringBody("true"));

                if (isShareTwitter == 0)
                    entity.addPart("twitter", new StringBody("false"));
                else
                    entity.addPart("twitter", new StringBody("true"));

                entity.addPart("video", new FileBody(new File(filePath), "video/mp4"));
                entity.addPart("photo", new ByteArrayBody(videoThumbByteArray, "image/jpeg", "test.jpg"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                Log.e("UploadVideo**",entity.toString() );

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    obj = new JSONObject(EntityUtils.toString(r_entity));
                    Log.e("UploadVideo**",obj.toString());
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                    Log.e("UploadVideo**", responseString + " --- " + response.getStatusLine().toString());
                    return null;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("UploadVideo**", "ClientProtocolException - " + e.toString());
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("UploadVideo**", "IOException - " + e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return obj;

            // ------------- old code ---------------
////            getActivity().runOnUiThread(new Runnable() {
////                public void run() {
//            try {
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost postRequest = new HttpPost(Constant.add_video_url);
////                MultipartEntity reqEntity = new MultipartEntity();
//                AndroidMultiPartEntity reqEntity = new AndroidMultiPartEntity(
//                        new AndroidMultiPartEntity.ProgressListener() {
//
//                            @Override
//                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
//                            }
//                        });
//                try {
//
//                    reqEntity.addPart("access_token", new StringBody(token));
//                    reqEntity.addPart("campaign_id", new StringBody(campId));
//                    reqEntity.addPart("description", new StringBody(desc));
//
//                    if (isShareFb == 0)
//                        reqEntity.addPart("facebook", new StringBody("false"));
//                    else
//                        reqEntity.addPart("facebook", new StringBody("true"));
//
//                    if (isShareTwitter == 0)
//                        reqEntity.addPart("twitter", new StringBody("false"));
//                    else
//                        reqEntity.addPart("twitter", new StringBody("true"));
//
////                    if(imageData!=null){
//                    reqEntity.addPart("video", new FileBody(new File(filePath), "video/mp4"));
//                    reqEntity.addPart("photo", new ByteArrayBody(videoThumbByteArray, "image/jpeg", "test.jpg"));
////                    }
//
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                totalSize = reqEntity.getContentLength();
//                Log.i("Progress - totalSize",totalSize + "%");
//                postRequest.setEntity(reqEntity);
//                HttpResponse response = null;
//                try {
//                    response = httpClient.execute(postRequest);
//
//                    Log.e("Response", "" + response);
//                } catch (ClientProtocolException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                BufferedReader reader = null;
//                try {
//                    reader = new BufferedReader(new InputStreamReader(response
//                            .getEntity().getContent(), "UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalStateException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                if (mProgressHUD.isShowing() && mProgressHUD != null)
//                    mProgressHUD.dismiss();
//
//                String sResponse;
//                StringBuilder s = new StringBuilder();
//                try {
//                    while ((sResponse = reader.readLine()) != null) {
//                        s = s.append(sResponse);
//                    }
//                    String res = s.toString();
//                    Log.e("VIDEO RESPONSE", s.toString());
//                    JSONObject obj = new JSONObject(res);
//
////                    obj = obj.getJSONObject("message");
////                    Log.e("VIDEO msg obj", obj.toString());
////                    final String msg = obj.getString("message");
////                    String err = obj.getString("error");
////                    String msg = "video uploaded successfully";
////                    Log.e("VIDEO msg", msg);
//                    return obj;
//
////                    if (mProgressHUD.isShowing() && mProgressHUD != null)
////                        mProgressHUD.dismiss();
//
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                return obj;
//
//            } catch (Exception e) {
//                // TODO: handle exception
//
//                e.printStackTrace();
//                return null;
//            }
//
////            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (mProgressHUD.isShowing() && mProgressHUD != null)
                mProgressHUD.dismiss();

            if (result != null) {
                Log.e("VIDEO msg obj", result.toString());
                final String msg;
                try {
                    msg = result.getString("message");
                    String err = result.getString("error");
                    Log.i("Errooorrr", Integer.parseInt(err) + "");
//                    String msg = "video uploaded successfully";
                    Log.e("VIDEO msg", msg);

                    if (err.equals("0")) {
//                        MyUtils.showToast(getActivity(), msg);

                        File dir = new File(Environment.getExternalStorageDirectory()
                                + File.separator
                                + "VOTOCAST"
                                + File.separator
                                + "Media"
                                + File.separator
                                + "Video");
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                new File(dir, children[i]).delete();
                            }
                        }
                        Log.i("Errooorrr", "Error 0");
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        String vidId = result.getString("video_id");
                        mVideoPlayerManager1.resetMediaPlayer();

                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        ((MainActivity) getActivity()).getTabLayout().setVisibility(View.VISIBLE);

                        ExploreFragment fragment = new ExploreFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("vidId", vidId);
                        b1.putString("from", "video");
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
//                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                        ft.addToBackStack(new HomeFragment().getClass().getName());
                        ft.commit();

//                        TabLayout.Tab tab= ((MainActivity) getActivity()).getTabLayout().getTabAt(0);
//                        tab.select();

                    } else if (Integer.parseInt(err) == 2) {
                        Log.i("Errooorrr", "Error 2");
                        final AlertDialog.Builder myVideoDialog = new AlertDialog.Builder(getActivity());
                        myVideoDialog.setTitle("VOTOCAST")
                                .setMessage(msg)
                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new setResendEmail().execute();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    } else {
//                        MyUtils.showToast(getActivity(), msg);
                        mVideoPlayerManager1.resetMediaPlayer();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        File dir = new File(Environment.getExternalStorageDirectory()
                                + File.separator
                                + "VOTOCAST"
                                + File.separator
                                + "Media"
                                + File.separator
                                + "Video");
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                new File(dir, children[i]).delete();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else
                Toast.makeText(getActivity(), "Error occurred while uploading video!", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), posts, Toast.LENGTH_LONG).show();
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

    class addFbToken extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String accessToken;

        public addFbToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
//                String token = Constant.getShareData(getActivity(), "pref_login");
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("access_token", token));
                pair.add(new BasicNameValuePair("fb_token", accessToken));
                pair.add(new BasicNameValuePair("metadata", fbMetadata));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.postData(Constant.add_fbtoken_url, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    jo = new JSONObject(JsonString);
//                                    Log.i("log", jo.toString());
                                    String err = jo.getString("error");
//                                    String msg = jo.getString("message");

                                    if (err.equals("0")) {

                                    } else {
                                        JSONObject validation = jo.getJSONObject("validation");
                                        JSONArray errMsgArr = validation.getJSONArray("email");
//                                        Log.i("log",jo.getJSONObject("validation").toString());
//                                        Log.i("log",validation.getJSONArray("email").toString());
//                                        Log.i("log",errMsgArr.get(0).toString());

                                        MyUtils.showToast(getActivity(), errMsgArr.get(0).toString());
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

    class addTwitterToken extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String accessToken;
        ArrayList<NameValuePair> pair;

        public addTwitterToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO Auto-generated method stub

            try {

//                String token = Constant.getShareData(getActivity(), "pref_login");
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("access_token", token));
                pair.add(new BasicNameValuePair("twitter_token", accessToken));
                pair.add(new BasicNameValuePair("metadata", twitterMetadata));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.postData(Constant.add_twitter_token_url, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    jo = new JSONObject(JsonString);
//                                    Log.i("log", jo.toString());
                                    String err = jo.getString("error");
//                                    String msg = jo.getString("message");

                                    if (err.equals("0")) {
                                        btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.no);
                                        isShareTwitter = 0;
                                        Constant.saveSharedData(getActivity(), "is_twitter_connect", "true");
                                    } else {
                                        JSONObject validation = jo.getJSONObject("validation");
                                        JSONArray errMsgArr = validation.getJSONArray("email");
//                                        Log.i("log",jo.getJSONObject("validation").toString());
//                                        Log.i("log",validation.getJSONArray("email").toString());
//                                        Log.i("log",errMsgArr.get(0).toString());

                                        MyUtils.showToast(getActivity(), errMsgArr.get(0).toString());
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
    public void onPause() {
        super.onPause();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
//    }
}
