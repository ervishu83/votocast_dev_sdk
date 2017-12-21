package fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.ByteArrayBody;
import com.parse.entity.mime.content.StringBody;
import com.votocast.votocast.ChangePasswordActivity;
import com.votocast.votocast.HelpActivity;
import com.votocast.votocast.LoginActivity;
import com.votocast.votocast.MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.NoticationActivity;
import com.votocast.votocast.PrivacyPolicyActivity;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;
import com.votocast.votocast.ReportIssueActivity;
import com.votocast.votocast.RulesActivity;
import com.votocast.votocast.SupportActivity;
import com.votocast.votocast.TermsOfServiceActivity;

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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.AndroidMultiPartEntity;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import de.hdodenhof.circleimageview.CircleImageView;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, View.OnTouchListener {


    @BindView(R2.id.tvSettingTitle1)
    TextView tvSettingTitle1;
    @BindView(R2.id.tvSettingTitle2)
    TextView tvSettingTitle2;
    @BindView(R2.id.tvSettingTitle3)
    TextView tvSettingTitle3;
    @BindView(R2.id.tvSettingTitle4)
    TextView tvSettingTitle4;
    @BindView(R2.id.tvSettingTitle5)
    TextView tvSettingTitle5;

    @BindView(R2.id.tvSettingText1)
    TextView tvSettingText1;
    @BindView(R2.id.tvSettingText2)
    TextView tvSettingText2;
    @BindView(R2.id.tvSettingText3)
    TextView tvSettingText31;
    @BindView(R2.id.tvSettingText4)
    TextView tvSettingText4;
    @BindView(R2.id.tvSettingText5)
    TextView tvSettingText5;
    @BindView(R2.id.tvSettingText6)
    TextView tvSettingText6;
    @BindView(R2.id.tvSettingText7)
    TextView tvSettingText7;
    @BindView(R2.id.tvSettingText8)
    TextView tvSettingText8;
    @BindView(R2.id.tvSettingText9)
    TextView tvSettingText9;
    @BindView(R2.id.tvSettingText10)
    TextView tvSettingText10;
    @BindView(R2.id.tvSettingText11)
    TextView tvSettingText11;
    @BindView(R2.id.tvSettingText12)
    TextView tvSettingText12;
    @BindView(R2.id.tvSettingText13)
    TextView tvSettingText13;
    @BindView(R2.id.tvSettingText14)
    TextView tvSettingText14;
    @BindView(R2.id.tvSettingText15)
    TextView tvSettingText15;
    @BindView(R2.id.tvSettingText16)
    TextView tvSettingText16;
    @BindView(R2.id.tvSettingText17)
    TextView tvSettingText17;
    @BindView(R2.id.tvSettingText18)
    TextView tvSettingText18;
    @BindView(R2.id.tvSettingText19)
    TextView tvSettingText19;
    @BindView(R2.id.tvSettingText20)
    TextView tvSettingText20;
    @BindView(R2.id.tvSettingText21)
    TextView tvSettingText21;

    @BindView(R2.id.btnSettingFb)
    LoginButton btnSettingFb;
    @BindView(R2.id.btnSettingTwitter)
    Button btnSettingTwitter;

    private String is_fb_connect;
    private String is_twitter_connect;
    private CallbackManager callbackManager;

    private static Twitter twitter;
    private static RequestToken requestToken;

    String oauth_url, oauth_verifier, profile_url;
    Dialog auth_dialog;
    WebView web;
    AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private ImageLoader imageLoader;
    //    String token;
    String twitterMetadata, fbMetadata;
    ImageView toolbar_back_button;
    String inviteText;
    //---------------------- edit profile ------------------

    protected static final int SELECT_PICTURE = 1;
    private static final int PROFIL_PERMISSION_REQUEST_CODE = 111;
    private static final int REQUEST_CAMERA = 2;

    @BindView(R2.id.civProfilePic1)
    CircleImageView civProfilePic;

    @BindView(R2.id.evProfileName1)
    EditText evProfileName;

    @BindView(R2.id.evProfileUserName1)
    EditText evProfileUserName;

    @BindView(R2.id.evProfileWebsite1)
    EditText evProfileWebsite;

    @BindView(R2.id.evProfileAbout1)
    EditText evProfileAbout;

    @BindView(R2.id.evProfileLocation1)
    AutoCompleteTextView evProfileLocation;

    @BindView(R2.id.evProfileEmail1)
    EditText evProfileEmail;

    String name = "", username = "", website = "", about = "", location = "", email = "", phone = "";
    ArrayList<NameValuePair> pair;
    private String imagepath;
    public static ArrayList<String> cityStateData;
    private static String[] COUNTRIES = {};
    String City, State;
    int charCount = 0;
    ArrayAdapter<String> adapter;

    @BindView(R2.id.evProfilePhone1)
    MaskedEditText evProfilePhone;

    JSONObject jo;
    JSONArray posts = null;
    byte[] imageData = null;

    Menu menu;
    String selectedLoc;
    ArrayList<String> locCityArr;
    long totalSize = 0;

    // ----------- complete edit profile ------------------
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;

    // [START define_variables]
    private GoogleApiClient mGoogleApiClient;
    // [END define_variables]

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        callbackManager = CallbackManager.Factory.create();

        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Settings");
        t.send(new HitBuilders.AppViewBuilder().build());

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R.id.toolbarLogo);
        iv.setVisibility(View.GONE);
        TextView toolText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolText.setVisibility(View.VISIBLE);
        toolText.setText("SETTINGS");
        RelativeLayout rlToolbar = (RelativeLayout) toolbar.findViewById(R.id.rlToolbar);
        rlToolbar.setVisibility(View.VISIBLE);
        toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);
        toolbar_back_button.setOnClickListener(this);
        ImageView toolbar_delete_button = (ImageView) toolbar.findViewById(R.id.toolbar_delete_button);
        toolbar_delete_button.setVisibility(View.INVISIBLE);

        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
//        ((MainActivity) getActivity()).setActionBarTitle("SETTINGS");

//        if(savedInstanceState != null)
//            token = savedInstanceState.getString("token");
//        else
//            token = Constant.getShareData(getActivity(), "pref_login");

        Constant.setDisplayFontsBold(getActivity(), tvSettingTitle1);
        Constant.setDisplayFontsBold(getActivity(), tvSettingTitle2);
        Constant.setDisplayFontsBold(getActivity(), tvSettingTitle3);
        Constant.setDisplayFontsBold(getActivity(), tvSettingTitle4);
        Constant.setDisplayFontsBold(getActivity(), tvSettingTitle5);

        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText1);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText2);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText31);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText4);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText5);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText6);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText7);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText8);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText9);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText10);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText11);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText12);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText13);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText14);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText15);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText16);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText17);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText18);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText19);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText20);
        Constant.setDisplayFontsSemibold(getActivity(), tvSettingText21);

//        new getUserDetail().execute();

//        if (Build.VERSION.SDK_INT >= 19) // KITKAT
//        {
//            web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        btnSettingFb.setBackgroundResource(R.drawable.connect_before_setting);
        btnSettingFb.setText("");
        btnSettingFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        is_fb_connect = Constant.getShareData(getActivity(), "is_fab_connect");
        is_twitter_connect = Constant.getShareData(getActivity(), "is_twitter_connect");

        btnSettingFb.setOnClickListener(this);
        btnSettingTwitter.setOnClickListener(this);

        Log.i("fb", is_fb_connect);
        Log.i("twit", is_twitter_connect);

        if (is_fb_connect.equals("false")) {
            btnSettingFb.setBackgroundResource(R.drawable.connect_before_setting);
            btnSettingFb.setReadPermissions(Arrays.asList("email"));
            btnSettingFb.setFragment(this);
            btnSettingFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    System.out.println("onSuccess");
                    ProgressHUD mProgressHUD;
                    mProgressHUD = ProgressHUD.show(getContext(), "", false, false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });

                    final String accessToken = loginResult.getAccessToken().getToken();
                    Log.i("accessToken1", accessToken + "-" + loginResult.getAccessToken().getExpires());

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
                    Constant.saveSharedData(getActivity(), "is_fab_connect", "true");
                    btnSettingFb.setBackgroundResource(R.drawable.connect_green_setting);
//                    btnSettingFb.setText("");
//                    btnSettingFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
            btnSettingFb.setBackgroundResource(R.drawable.connect_green_setting);
            btnSettingFb.setText("");
            btnSettingFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            btnSettingFb.setClickable(false);
        }

        if (is_twitter_connect.equals("false"))
            btnSettingTwitter.setBackgroundResource(R.drawable.connect_before_setting);
        else {
            btnSettingTwitter.setBackgroundResource(R.drawable.connect_green_setting);
        }

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

        evProfileName.setOnTouchListener(this);
        evProfileUserName.setOnTouchListener(this);
        evProfileWebsite.setOnTouchListener(this);
        evProfileAbout.setOnTouchListener(this);
        evProfileEmail.setOnTouchListener(this);
        evProfilePhone.setOnTouchListener(this);
        evProfileLocation.setOnTouchListener(this);


        // ------------- Invite frineds ------------
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(AppInvite.API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }
        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, getActivity(), autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                if (result.getStatus().isSuccess()) {
                                    // Extract information from the intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String invitationId = AppInviteReferral.getInvitationId(intent);

                                    // Because autoLaunchDeepLink = true we don't have to do anything
                                    // here, but we could set that to false and manually choose
                                    // an Activity to launch to handle the deep link here.
                                    // ...
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
                        ProfileFragment fragment = new ProfileFragment();
                        Bundle b1 = new Bundle();
                        b1.putString("user_id", "0");
                        fragment.setArguments(b1);
                        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        return true;
                    }
                }
                return false;
            }
        });
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        menu.findItem(R.id.action_done).setVisible(true);
        return false;
    }

    @OnClick(R2.id.ivSettingFragmentProfilePicBack)
    void fnGoToProfile() {
        profilePicDialog();
//        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//
//            Intent intent = new Intent("android.intent.action.GET_CONTENT");
//            intent.setType("image/*");
//            startActivityForResult(intent, SELECT_PICTURE);
//
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(getActivity(), "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
//            } else {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PROFIL_PERMISSION_REQUEST_CODE);
//            }
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("token",token);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("fragment","on resume");
        new getUserDetail().execute();
        if (Constant.getShareData(getActivity(), "is_fab_connect").equals("false"))
            btnSettingFb.setBackgroundResource(R.drawable.connect_before_setting);
        else {
            btnSettingFb.setBackgroundResource(R.drawable.connect_green_setting);
        }
        if (Constant.getShareData(getActivity(), "is_twitter_connect").equals("false"))
            btnSettingTwitter.setBackgroundResource(R.drawable.connect_before_setting);
        else {
            btnSettingTwitter.setBackgroundResource(R.drawable.connect_green_setting);
        }

        cityStateData = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        adapter.setNotifyOnChange(true);
        evProfileLocation.setAdapter(adapter);

        evProfileLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                selectedLoc = s.toString();

                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("access_token", Constant.getShareData(getActivity(), "pref_login")));
                pair.add(new BasicNameValuePair("term", s.toString()));

                Constant.postData(Constant.city_state_url, pair, new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        final Bundle response = message.getData();

                        if (response.getString("error").length() == 0) {
                            final String JsonString = response.getString("data");

                            if(getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            jo = new JSONObject(JsonString);
                                            posts = jo.getJSONArray("data");

                                            for (int i = 0; i < posts.length(); i++) {
                                                try {
                                                    JSONObject temp = posts.getJSONObject(i);
                                                    JSONObject tempState = temp.getJSONObject("State");
                                                    String cityState = tempState.getString("city");
                                                    cityState = cityState + ", " + tempState.getString("state");
//                                            Log.i("loc", tempState.getString("city") + "-" + tempState.getString("state"));
                                                    cityStateData.add(cityState);
                                                    locCityArr.add(cityState);
                                                } catch (Exception e) {
                                                    Log.e("Error", "" + e.toString());
                                                }
                                            }
                                            Log.i("city", cityStateData.toString());
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            }
                        } else {
                            Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                        }
                        return false;
                    }

                }));
                Log.i("city size", cityStateData.size() + "");
                if (cityStateData != null && cityStateData.size() > 0) {
                    COUNTRIES = cityStateData.toArray(new String[cityStateData.size()]);
//                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, R.layout.autocomplete, COUNTRIES);
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                    adapter.setNotifyOnChange(true);
                    evProfileLocation.setThreshold(charCount);
                    evProfileLocation.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        inflater.inflate(R.menu.menu_profile, menu);
        menu.findItem(R.id.action_done).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                MyUtils.showToast(getActivity(),"backimage");

                ProfileFragment fragment = new ProfileFragment();
                Bundle b1 = new Bundle();
                b1.putString("user_id", "0");
                b1.putString("from", "setting");

                fragment.setArguments(b1);
                FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                return true;
            case R2.id.action_done:
                updateProfile();
                return true;
//            case R2.id.action_edit:
//                startActivity(new Intent(getActivity(), ProfileActivity.class));
//                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R2.id.tvSettingText20)
    void fnLogout() {
        Constant.saveSharedData(getActivity(), "pref_login", "");
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @OnClick(R2.id.llSettingChangePwd)
    void changePwd() {
        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
    }

    @OnClick(R2.id.llSettingNotification)
    void notification() {
        startActivity(new Intent(getActivity(), NoticationActivity.class));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_back_button) {
//            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
//            if (fragmentManager.getBackStackEntryCount() > 0) {
//                fragmentManager.popBackStack();
//            }
            ProfileFragment fragment = new ProfileFragment();
            Bundle b1 = new Bundle();
            b1.putString("user_id", "0");
            fragment.setArguments(b1);
            FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
        if (view.getId() == R.id.btnSettingTwitter) {
            Log.i("twit login", Constant.getShareData(getActivity(), "is_twitter_connect"));
            if (Constant.getShareData(getActivity(), "is_twitter_connect").equals("false")) {
                twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret));
                new TokenGet().execute();
            } else {
                twittLogout();
            }
        } else if (view.getId() == R.id.btnSettingFb) {
//            Log.i("fb click", "click");
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(com.facebook.AccessToken oldAccessToken, com.facebook.AccessToken currentAccessToken) {
                    if (currentAccessToken == null) {
                        Log.i("fb", "log out");
                        Constant.saveSharedData(getActivity(), "is_fab_connect", "false");
                        btnSettingFb.setBackgroundResource(R.drawable.connect_before_setting);
                        btnSettingFb.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                }
            };
        }
    }

    private void twittLogout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("Are you sure");
        builder1.setMessage("Unlink your twitter account?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Constant.saveSharedData(getActivity(), "is_twitter_connect", "false");
//                        Log.e("Twitter log", Constant.getShareData(getActivity(),"is_twitter_connect"));
                        btnSettingTwitter.setBackgroundResource(R.drawable.connect_before_setting);

                        android.webkit.CookieManager.getInstance().removeAllCookie();

//                        twitter = new TwitterFactory().getInstance();
//                        twitter.setOAuthConsumer(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret));
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private class TokenGet extends AsyncTask<String, String, String> {
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            btnSettingTwitter.setBackgroundResource(R.drawable.connect_green_setting);
//            Constant.saveSharedData(getActivity(), "is_twitter_connect", "true");
            mProgressHUD = ProgressHUD.show(getActivity(), "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

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
//                web.getSettings().setDomStorageEnabled(true);
//                web.loadUrl(oauth_url);

                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
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

                                btnSettingTwitter.setBackgroundResource(R.drawable.connect_green_setting);
                                Constant.saveSharedData(getActivity(), "is_twitter_connect", "true");
                                Log.e("Twitter log", Constant.getShareData(getActivity(), "is_twitter_connect"));

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
                            Toast.makeText(getActivity(), "Sorry ! Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                web.loadUrl(oauth_url);
                auth_dialog.show();
                auth_dialog.setCancelable(true);
            } else {
                Toast.makeText(getActivity(), "Sorry ! Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class getUserDetail extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;

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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_profile_url;
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
                                    inviteText = jo.getString("invite_message");
                                    if (error == 0) {

                                        JSONObject userObj = jo.getJSONObject("user");

//                                        ((MainActivity) getActivity()).setActionBarTitle(compObj.getString("title"));

                                        evProfileName.setText(userObj.getString("name"));
                                        name = userObj.getString("name");
                                        evProfileUserName.setText(userObj.getString("username"));
                                        username = userObj.getString("username");
                                        evProfileEmail.setText(userObj.getString("email"));
                                        email = userObj.getString("email");
                                        evProfilePhone.setMaskedText(userObj.getString("phone"));

//                                        evProfilePhone.setText(userObj.getString("phone"));
                                        if (!userObj.getString("city").equals(""))
                                            evProfileLocation.setText(userObj.getString("city") + ", " + userObj.getString("state"));

                                        City = userObj.getString("city");
                                        State = userObj.getString("state");
                                        evProfileAbout.setText(userObj.getString("about"));
                                        about = userObj.getString("about");
                                        evProfileWebsite.setText(userObj.getString("website"));
                                        website = userObj.getString("website");


                                        if (!userObj.getString("photo_path").equals("")) {
//                                            Picasso.with(getActivity()).load(userObj.getString("photo_path"))
//                                                    .placeholder(R.drawable.user_default)
//                                                    .error(R.drawable.user_default)
//                                                    .into(civProfilePic);
                                            imageLoader.displayImage(userObj.getString("photo_path"), civProfilePic, new ImageLoadingListener() {
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

                                        } else
                                            civProfilePic.setImageResource(R.drawable.user_default);

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

    class addFbToken extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String accessToken;
        ArrayList<NameValuePair> pair;

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
                String token = Constant.getShareData(getActivity(), "pref_login");
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

                String token = Constant.getShareData(getActivity(), "pref_login");
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
//                                        btnDetailPopupShareTwitter.setBackgroundResource(R.drawable.no);
//                                        isShareTwitter = 0;
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

    @OnClick(R2.id.llSettingHelp)
    void fnHelp() {
        startActivity(new Intent(getActivity(), HelpActivity.class));
    }

    @OnClick(R2.id.llSettingRules)
    void fnRules() {
        startActivity(new Intent(getActivity(), RulesActivity.class));
    }

    @OnClick(R2.id.llSettingTerms)
    void fnTerms() {
        startActivity(new Intent(getActivity(), TermsOfServiceActivity.class));
    }

    @OnClick(R2.id.llSettingPrivacy)
    void fnPrivacyPolicy() {
        startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
    }

    @OnClick(R2.id.llSettingReports)
    void fnSupport() {
        startActivity(new Intent(getActivity(), SupportActivity.class));
    }

    @OnClick(R2.id.llSettingReportsIssues)
    void fnReportIssues() {
        startActivity(new Intent(getActivity(), ReportIssueActivity.class));
    }

    // ---------------- Invite friends ----------
    @OnClick(R2.id.llSettingInviteFriends)
    void fnInviteFriends() {
        onInviteClicked();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        showMessage(getString(R.string.google_play_services_error));
    }

    // [START on_invite_clicked]
    private void onInviteClicked() {
        String PACKAGE_NAME = getActivity().getApplicationContext().getPackageName();
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(inviteText.substring(0, 99))
//                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                .setCustomImage(Uri.parse("android.resource://"+ PACKAGE_NAME +"/drawable/logo1"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    // [END on_invite_clicked]
    // [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == getActivity().RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]
                showMessage(getString(R.string.send_failed));
                // [END_EXCLUDE]
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                if (data != null) {

                    final Uri selectedImage = data.getData();

                    String picturePath = getPath(getActivity(), selectedImage);
                    Log.i("PicturePath", picturePath);
//                    Picasso.with(getActivity()).load(picturePath)
//                            .error(R.drawable.user_default)
//                            .into(civProfilePic);
                    imagepath = picturePath;
                    imageLoader.displayImage("file://" + imagepath, civProfilePic, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                            DownloadTask downloadTask = new DownloadTask(
//                                    mContext);
//                            downloadTask.execute(bitmap);
                            if (imagepath != null) {
                                try {
                                    Bitmap bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
                                    civProfilePic.setImageBitmap(bmp);
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    imageData = bos.toByteArray();
                                    Log.i("ProfileImage bmp",bmp.toString());
                                    Log.i("ProfileImage",imageData.toString());
                                    new callUploadProfilePic().execute();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });


                } else {
                    Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if (requestCode == REQUEST_CAMERA) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                civProfilePic.setImageBitmap(imageBitmap);
                Log.i("PicturePath", extras.get("data").toString());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                imageData = bos.toByteArray();
                new callUploadProfilePic().execute();
            }
        }
    }

    // [END on_activity_result]
    private void showMessage(String msg) {
//        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
//        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
        MyUtils.showToast(getActivity(), msg);
    }

    //--------------update profile------------

    public boolean validateUrl(String url) {

        Pattern regexTag = Pattern.compile("^(https?|ftp|file)://[a-zA-Z0-9\\-\\.]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$");       // ------- with http://
        Matcher matcherTag = regexTag.matcher(url);
//        return matcherTag.matches();
        if (matcherTag.matches()) {
            return true;
        } else {
            Pattern regex = Pattern.compile("^[a-zA-Z0-9\\-\\.]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$");          // ----- without http://
//        Pattern regex = Pattern.compile("^(https?|ftp|file)://[a-zA-Z0-9\\-\\.]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$");       // ------- with http://
            Matcher matcher = regex.matcher(url);
            return matcher.matches();
        }
    }

    private void updateProfile() {
        name = evProfileName.getText().toString().trim();
        username = evProfileUserName.getText().toString().trim();
        website = evProfileWebsite.getText().toString().trim();
        about = evProfileAbout.getText().toString().trim();
        location = evProfileLocation.getText().toString().trim();
        email = evProfileEmail.getText().toString().trim();
//        phone = evProfilePhone.getText().toString().trim();

//        Log.i("Website", validateUrl(website) + "");
//        if (validateUrl(website)) {
//            if (!website.substring(0, 7).equals("http://") && !website.substring(0, 8).equals("https://")) {
//                website = "http://" + website;
//            }
//        }

        if (username.length() > 0) {
            if (website.length() > 0) {
                if (validateUrl(website)) {
                    if (!website.substring(0, 7).equals("http://") && !website.substring(0, 8).equals("https://")) {
                        website = "http://" + website;
                    }
                    if (location.length() > 0) {
                        String location = evProfileLocation.getText().toString();
                        if (location.contains(",")) {
                            try {
                                City = location.split(",")[0];
                                State = location.split(",")[1];
                            } catch (Exception e) {
//                                    new callEditProfile().execute();
                            }
                            if (email.length() > 0 && MyUtils.isValidEmail(email)) {
                                if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() > 0) {
                                    if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() == 10) {
                                        phone = evProfilePhone.getUnmaskedText().toString().trim();
                                        new callEditProfile().execute();
                                    } else
                                        MyUtils.showToast(getActivity(), "Enter valid phone number");
                                } else {
                                    new callEditProfile().execute();
                                }
                            } else
                                MyUtils.showToast(getActivity(), "Enter valid email");
                        } else
                            MyUtils.showToast(getActivity(), "Select proper location!");
                    } else {
                        if (email.length() > 0 && MyUtils.isValidEmail(email)) {
                            if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() > 0) {
                                if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() == 10) {
                                    phone = evProfilePhone.getUnmaskedText().toString().trim();
                                    new callEditProfile().execute();
                                } else
                                    MyUtils.showToast(getActivity(), "Enter valid phone number");
                            } else {
                                new callEditProfile().execute();
                            }
                        } else
                            MyUtils.showToast(getActivity(), "Enter valid email");
                    }
                } else
                    MyUtils.showToast(getActivity(), "Enter valid website");
            } else {
                if (location.length() > 0) {
                    String location = evProfileLocation.getText().toString();
                    if (location.contains(",")) {
                        try {
                            City = location.split(",")[0];
                            State = location.split(",")[1];
                        } catch (Exception e) {
                        }
                        if (email.length() > 0 && MyUtils.isValidEmail(email)) {
                            if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() > 0) {
                                if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() == 10) {
                                    phone = evProfilePhone.getUnmaskedText().toString().trim();
                                    new callEditProfile().execute();
                                } else
                                    MyUtils.showToast(getActivity(), "Enter valid phone number");
                            } else {
                                new callEditProfile().execute();
                            }
                        } else
                            MyUtils.showToast(getActivity(), "Enter valid email");
                    } else
                        MyUtils.showToast(getActivity(), "Select proper location!");
                } else {
                    if (email.length() > 0 && MyUtils.isValidEmail(email)) {
                        if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() > 0) {
                            if (evProfilePhone.getUnmaskedText().replace(" ", "").trim().length() == 10) {
                                phone = evProfilePhone.getUnmaskedText().toString().trim();
                                new callEditProfile().execute();
                            } else
                                MyUtils.showToast(getActivity(), "Enter valid phone number");
                        } else {
                            new callEditProfile().execute();
                        }
                    } else
                        MyUtils.showToast(getActivity(), "Enter valid email");
                }
            }
        } else
            MyUtils.showToast(getActivity(), "Username is compulsory!");
    }

    class callEditProfile extends AsyncTask<Void, Void, JSONObject> {
        JSONObject jo;
        ProgressHUD mProgressHUD;

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
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String token = Constant.getShareData(getActivity(), "pref_login");

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(Constant.edit_profile_url);
                postRequest.addHeader("Authorization",Constant.Authorization);

                MultipartEntity reqEntity = new MultipartEntity();
                try {

                    reqEntity.addPart("access_token", new StringBody(token));
                    reqEntity.addPart("name", new StringBody(name));
                    reqEntity.addPart("username", new StringBody(username));
                    reqEntity.addPart("website", new StringBody(website));
                    reqEntity.addPart("about", new StringBody(about));
                    reqEntity.addPart("city", new StringBody(City));

                    reqEntity.addPart("state", new StringBody(State));
                    reqEntity.addPart("email", new StringBody(email));
                    reqEntity.addPart("phone", new StringBody(phone));


                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                postRequest.setEntity(reqEntity);
                HttpResponse response = null;
                try {
                    response = httpClient.execute(postRequest);

                    // Log.e("Response", ""+response);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(response
                            .getEntity().getContent(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String sResponse;
                StringBuilder s = new StringBuilder();
                try {
                    while ((sResponse = reader.readLine()) != null) {
                        s = s.append(sResponse);
                    }
                    String res = s.toString();
                    Log.e("VIDEO RESPONSE", s.toString());
                    JSONObject obj = new JSONObject(res);

                    final String msg = obj.getString("message");
                    String err = obj.getString("error");
                    Log.e("VIDEO msg", msg);

                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                        mProgressHUD.dismiss();

                    return obj;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
                return null;
            }

            return null;
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
                String errMsg = "";
                try {
                    msg = result.getString("message");
                    String err = result.getString("error");
//                    String msg = "video uploaded successfully";
                    Log.e("VIDEO msg", msg);
                    menu.findItem(R.id.action_done).setVisible(false);

                    if (err.equals("0")) {
//                        MyUtils.showToast(getActivity(), msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//                        finish();

                    } else {
                        JSONObject validation = result.getJSONObject("validation");
                        if (validation.has("email")) {
                            JSONArray errMsgArr = validation.getJSONArray("email");
                            errMsg = errMsg + " " + errMsgArr.getString(0);
                            Log.i("Error", errMsg);
                        }
                        if (validation.has("username")) {
                            JSONArray errMsgArr = validation.getJSONArray("username");
                            errMsg = errMsg + " " + errMsgArr.getString(0);
                            Log.i("Error", msg);
                        }
//                                        Log.i("log",jo.getJSONObject("validation").toString());
//                                        Log.i("log",validation.getJSONArray("email").toString());
//                                        Log.i("log",errMsgArr.get(0).toString());

                        MyUtils.showToast(getActivity(), errMsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class callUploadProfilePic extends AsyncTask<Void, Integer, JSONObject> {
        JSONObject obj;
        ProgressHUD mProgressHUD;

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
            Log.i("Progress",String.valueOf(values[0]) + "%");
            mProgressHUD.setMessage(String.valueOf(values[0]) + "%");
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            String responseString;
            String token = Constant.getShareData(getActivity(), "pref_login");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constant.edit_profile_url);
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
                Log.i("ProfilePicData",imageData.toString());
                entity.addPart("access_token", new StringBody(token));
                if (imageData != null) {
                    entity.addPart("photo", new ByteArrayBody(imageData, "image/jpeg", "test.jpg"));
                }

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    obj = new JSONObject(EntityUtils.toString(r_entity));
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                    return null;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return obj;
            //            try {
//                String token = Constant.getShareData(getActivity(), "pref_login");
//
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost postRequest = new HttpPost(Constant.edit_profile_url);
//
////                MultipartEntity reqEntity = new MultipartEntity();
//
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
////                    Log.i("ProfilePicDate",imageData.toString());
//
//                    reqEntity.addPart("access_token", new StringBody(token));
//                    if (imageData != null) {
//                        reqEntity.addPart("photo", new ByteArrayBody(imageData, "image/jpeg", "test.jpg"));
//                    }
//
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                totalSize = reqEntity.getContentLength();
//                postRequest.setEntity(reqEntity);
//                HttpResponse response = null;
//                try {
//                    response = httpClient.execute(postRequest);
//                    Log.i("Progress totalSize",response.getEntity().getContentLength()+"");
//                    // Log.e("Response", ""+response);
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
////                    totalSize = response.getEntity().getContentLength();
////                    Log.i("Progress totalSize",totalSize+"");
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
//                String sResponse;
//                StringBuilder s = new StringBuilder();
//                try {
//                    while ((sResponse = reader.readLine()) != null) {
//                        s = s.append(sResponse);
//                    }
//                    String res = s.toString();
//                    Log.e("Profile Pic RESPONSE", s.toString());
//                    JSONObject obj = new JSONObject(res);
//              /*          Status = obj.getString(MobyiUtils.VALID);
//                        Msg = obj.getString(MobyiUtils.MSG);
//                        // Log.e("aaa", Status);*/
//
////                    obj = obj.getJSONObject("message");
//
//                    final String msg = obj.getString("message");
//                    String err = obj.getString("error");
//                    Log.e("Profile pic msg", msg);
//
//                    if (mProgressHUD.isShowing() && mProgressHUD != null)
//                        mProgressHUD.dismiss();
//
//                    return obj;
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e) {
//                // TODO: handle exception
//
//                e.printStackTrace();
//                return null;
//            }
//
//            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (mProgressHUD.isShowing() && mProgressHUD != null)
                mProgressHUD.dismiss();

            if (result != null) {
                Log.e("Profile pic msg obj", result.toString());
                final String msg;
                try {
                    msg = result.getString("message");
                    String err = result.getString("error");
                    Log.e("Profile pic msg", msg);

                    if (err.equals("0")) {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
                Toast.makeText(getActivity(), "Error occurred while uploading video!", Toast.LENGTH_SHORT).show();
        }
    }

    // ------------- profile pic -----------------------------------
    @OnClick(R2.id.civProfilePic1)
    void changePic() {

        profilePicDialog();

//        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//
//            Intent intent = new Intent("android.intent.action.GET_CONTENT");
//            intent.setType("image/*");
//            startActivityForResult(intent, SELECT_PICTURE);
//
//        } else {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(getActivity(), "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
//            } else {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PROFIL_PERMISSION_REQUEST_CODE);
//            }
//        }
    }

    public void profilePicDialog(){

        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            final CharSequence[] items = { "Take Photo", "Choose from Library","Remove Photo",
                    "Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Take Photo")) {

//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(photo));
////                        imageUri = Uri.fromFile(photo);
//                        SettingsFragment.this.startActivityForResult(intent, REQUEST_CAMERA);

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            SettingsFragment.this.startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                        }
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                    else if (items[item].equals("Choose from Library"))
                    {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_PICTURE);
                    }
                    else if (items[item].equals("Remove Photo")) {
                        civProfilePic.setImageResource(R.drawable.user_default);
                        imageData = new byte[0];
                        new callUploadProfilePic().execute();
                    }
                    else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
//            Dialog d = builder.show();
//            int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//            TextView tv = (TextView) d.findViewById(textViewId);
//            tv.setTextColor(getResources().getColor(R.color.colorPrimary));

        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Please allow in external storage permission for upload profile picture.", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PROFIL_PERMISSION_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PROFIL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
//                    intent.setType("image/*");
//                    startActivityForResult(intent, SELECT_PICTURE);
                    profilePicDialog();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot upload profile picture.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//
//                if (data != null) {
//
//                    final Uri selectedImage = data.getData();
//
//                    String picturePath = getPath(this, selectedImage);
//                    imagepath = picturePath;
////                    Log.i("image", imagepath);
//                    imageLoader.displayImage("file://" + picturePath, civProfilePic, new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String s, View view) {
//
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
////                            DownloadTask downloadTask = new DownloadTask(
////                                    mContext);
////                            downloadTask.execute(bitmap);
//                            if (imagepath != null) {
//                                try {
//                                    Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
//                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                                    imageData = bos.toByteArray();
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String s, View view) {
//
//                        }
//                    });
//                    // Bitmap b = BlurBuilder.blur(mContext,BitmapFactory.decodeFile(picturePath));
//         /*           DownloadTask downloadTask = new DownloadTask(
//                            mContext);
//                    downloadTask.execute(bmpProfile);*/
//                    // Blur_profilepic.setImageBitmap(b);
//
//                    //  cursor.close();
//                } else {
//                    Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show();
//                }
//
//                return;
//            }
//        }
//    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

}
