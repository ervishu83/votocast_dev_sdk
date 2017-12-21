package com.votocast.votocast;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yqritc.scalablevideoview.ScalableVideoView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;

public class LoginActivity extends AppCompatActivity {

    @BindView(R2.id.loginVideo)ScalableVideoView loginVideo;

    // --------------- login controls

//    @BindView(R2.id.tvLoginLogoText)
//    LinearLayout tvLoginLogoText;
    @BindView(R2.id.tvLoginSignupLink)
    LinearLayout tvLoginSignupLink;

    @BindView(R2.id.etLoginUsername)
    EditText etLoginUsername;
    @BindView(R2.id.etLoginPassword)
    EditText etLoginPassword;
    @BindView(R2.id.btnLoginSignup)
    Button btnLoginSignup;
    @BindView(R2.id.tvLoginForgotPwd)
    LinearLayout tvLoginForgotPwd;

    @BindView(R2.id.tvLoginSignupLink1)
    TextView tvLoginSignupLink1;
    @BindView(R2.id.tvLoginSignupLink2)
    TextView tvLoginSignupLink2;

    @BindView(R2.id.tvLoginFacebookClick)TextView tvLoginFacebookClick;
    @BindView(R2.id.tvLoginFacebook)
    LoginButton tvLoginFacebook;
    @BindView(R2.id.frameLoginFacebook)
    FrameLayout frameLoginFacebook;

    // ---------------- signup controls

    @BindView(R2.id.etSignupEmail)
    EditText etSignupEmail;
    @BindView(R2.id.etSignupUsername)
    EditText etSignupUsername;
    @BindView(R2.id.etSignupPassword)
    EditText etSignupPassword;
    @BindView(R2.id.etSignupConfirmPassword)
    EditText etSignupConfirmPassword;
    @BindView(R2.id.btnSignup)
    Button btnSignup;
    @BindView(R2.id.tvSignupLoginagain)
    LinearLayout tvSignupLoginagain;

    @BindView(R2.id.tvSignupLoginagain1)
    TextView tvSignupLoginagain1;
    @BindView(R2.id.tvSignupLoginagain2)
    TextView tvSignupLoginagain2;

    @BindView(R2.id.tvSignupText1)TextView tvSignupText1;
    @BindView(R2.id.tvSignupText2)TextView tvSignupText2;
    @BindView(R2.id.tvSignupText3)TextView tvSignupText3;
    @BindView(R2.id.tvSignupText4)TextView tvSignupText4;

    @BindView(R2.id.tvSignupText)LinearLayout tvSignupText;
    @BindView(R2.id.frameSignupFacebook)FrameLayout frameSignupFacebook;
    @BindView(R2.id.tvSignupFacebook)LoginButton tvSignupFacebook;
    @BindView(R2.id.tvSignupFacebookClick)TextView tvSignupFacebookClick;

    // ----------- forgot pwd controls

    @BindView(R2.id.etForgotPwdEmail)
    EditText etForgotPwdEmail;
    @BindView(R2.id.btnForgotPwd)
    Button btnForgotPwd;
    @BindView(R2.id.tvForgotPwdLoginagain)
    TextView tvForgotPwdLoginagain;

    @BindView(R2.id.tvLoginForgotText1)
    TextView tvLoginForgotText1;
    @BindView(R2.id.tvLoginForgotText2)
    TextView tvLoginForgotText2;

    // ----- main logo control
    @BindView(R2.id.tvLoginLogoText1)
    TextView tvLoginLogoText1;
    @BindView(R2.id.tvLoginLogoText2)
    TextView tvLoginLogoText2;

    //    @BindView(R2.id.tvLoginFacebook)TextView tvLoginFacebook;

//    Toolbar toolbar;
    ArrayList<NameValuePair> pair;
    String email, username, password, confirmPwd;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().hide();
        ButterKnife.bind(this);

        Tracker t = ((MyAppTracker)this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Login");
        t.send(new HitBuilders.AppViewBuilder().build());

        try {
            loginVideo.setRawData(R.raw.login);
            loginVideo.setVolume(0, 0);
            loginVideo.setLooping(true);
            loginVideo.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    loginVideo.start();
                }
            });
        } catch (IOException ioe) {
            //ignore
        }

        Drawable background = btnLoginSignup.getBackground();
        Drawable backgroundSignup = btnSignup.getBackground();
        Drawable backgroundForgotPwd = btnForgotPwd.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
            ((ShapeDrawable)backgroundSignup).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
            ((ShapeDrawable)backgroundForgotPwd).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
            ((GradientDrawable)backgroundSignup).setColor(Color.parseColor(Constant.colorPrimary));
            ((GradientDrawable)backgroundForgotPwd).setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
            ((ColorDrawable)backgroundSignup).setColor(Color.parseColor(Constant.colorPrimary));
            ((ColorDrawable)backgroundForgotPwd).setColor(Color.parseColor(Constant.colorPrimary));
        }

        tvLoginFacebook.setBackgroundResource(0);
        tvLoginFacebook.setText("");
        tvLoginFacebook.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        tvSignupFacebook.setBackgroundResource(0);
        tvSignupFacebook.setText("");
        tvSignupFacebook.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        Constant.setTextFontsRegular(this, tvLoginLogoText1);
        Constant.setTextFontsRegular(this, tvLoginForgotText1);
        Constant.setTextFontsRegular(this, tvLoginSignupLink1);
        Constant.setTextFontsRegular(this, tvSignupLoginagain1);
        Constant.setTextFontsRegular(this,tvLoginFacebookClick);
        Constant.setTextFontsRegular(this,tvSignupFacebookClick);
        Constant.setTextFontsRegular(this,tvSignupText1);
        Constant.setTextFontsRegular(this,tvSignupText3);

        Constant.setTextFontsBold(this, tvLoginLogoText2);
        Constant.setTextFontsSemibold(this, tvLoginForgotText2);
        Constant.setTextFontsBold(this, tvLoginSignupLink2);
        Constant.setTextFontsBold(this, tvSignupLoginagain2);
        Constant.setTextFontsBold(this, tvSignupText2);
        Constant.setTextFontsBold(this, tvSignupText4);

        Constant.setTextFontsRegular(this, etLoginUsername);
        Constant.setTextFontsRegular(this, etLoginPassword);
        Constant.setTextFontsRegular(this, btnLoginSignup);

        Constant.setTextFontsRegular(this, etSignupEmail);
        Constant.setTextFontsRegular(this, etSignupUsername);
        Constant.setTextFontsRegular(this, etSignupPassword);
        Constant.setTextFontsRegular(this, etSignupConfirmPassword);
        Constant.setTextFontsRegular(this, btnSignup);

        Constant.setTextFontsRegular(this, etForgotPwdEmail);
        Constant.setTextFontsRegular(this, btnForgotPwd);

        // -------- login fb login
//        tvLoginFacebook.setOnClickListener(this);
        tvLoginFacebook.setReadPermissions(Arrays.asList("email"));
        tvLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                ProgressHUD mProgressHUD;

                Log.i("fb login", "fb login");

                mProgressHUD = ProgressHUD.show(LoginActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });

                final String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login

                        JSONObject user = (JSONObject) response.getJSONObject();
                        try {
                            String email = user.getString("email");
                            Log.i("LoginActivity", object.toString());

                            new callWsLogin(accessToken).execute(object);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("LoginActivity1", e.toString());
                            Constant.ShowFbErrorMessage("Error", "We are unable to get email from your Facebook account. Please try again", LoginActivity.this);
//                            LoginManager.getInstance().logOut();
//                            Toast.makeText(LoginActivity.this, "Your Facebook account is not verified", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
//                progressDialog.dismiss();

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

        // --------- signup fb login
//        tvSignupFacebook.setOnClickListener(this);
        tvSignupFacebook.setReadPermissions(Arrays.asList("email"));
        tvSignupFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                ProgressHUD mProgressHUD;

                Log.i("fb login", "fb login");

                mProgressHUD = ProgressHUD.show(LoginActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });

                final String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login

                        JSONObject user = (JSONObject) response.getJSONObject();
                        try {
                            String email = user.getString("email");
                            Log.i("LoginActivity", object.toString());

                            new callWsLogin(accessToken).execute(object);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("LoginActivity1", e.toString());
                            Constant.ShowFbErrorMessage("Error", "We are unable to get email from your Facebook account. Please try again", LoginActivity.this);
//                            LoginManager.getInstance().logOut();
//                            Toast.makeText(LoginActivity.this, "Your Facebook account is not verified", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
//                progressDialog.dismiss();

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

        //---------- keyboard done action
        etLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    login();
                }
                return false;
            }
        });
        etSignupConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    signUp();
                }
                return false;
            }
        });

        etForgotPwdEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    forgotPwd();
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    class callWsLogin extends AsyncTask<Object, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        String accessToken;

        public callWsLogin(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            mProgressHUD = ProgressHUD.show(LoginActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Object... params) {
            // TODO Auto-generated method stub

            try {
                JSONObject user = (JSONObject) params[0];
                String profile_id = user.getString("id");

//                Log.i("fb", "user-" + user.toString());

                String name = user.getString("first_name") + " " + user.getString("last_name");
                String myEmail = user.getString("email");

                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("facebook_id", profile_id));
                pair.add(new BasicNameValuePair("email", myEmail));
                pair.add(new BasicNameValuePair("name", name));
                pair.add(new BasicNameValuePair("facebook_token", accessToken));
                pair.add(new BasicNameValuePair("facebook_metadata", user.toString()));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.postData(Constant.register_url, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();

                                try {

                                    jo = new JSONObject(JsonString);
//                                    Log.i("log", jo.toString());
                                    String err = jo.getString("error");
//                                    String msg = jo.getString("message");

                                    if (err.equals("0")) {
                                        String token = jo.getString("access_token");
//                                        Log.i("log", token);
                                        Constant.saveSharedData(LoginActivity.this, "pref_login", token);
                                        Constant.saveSharedData(LoginActivity.this, "is_fab_connect", "true");
                                        Constant.saveSharedData(LoginActivity.this, "is_twitter_connect", "false");

                                        JSONObject user = jo.getJSONObject("user");
                                        Log.i("log user", user.getString("username"));
                                        Constant.saveSharedData(LoginActivity.this, "username", user.getString("username"));
                                        Constant.saveSharedData(LoginActivity.this, "myId", user.getString("id"));

//                                        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        JSONObject validation = jo.getJSONObject("validation");
                                        JSONArray errMsgArr = validation.getJSONArray("email");
//                                        Log.i("log",jo.getJSONObject("validation").toString());
//                                        Log.i("log",validation.getJSONArray("email").toString());
//                                        Log.i("log",errMsgArr.get(0).toString());

                                        MyUtils.showToast(LoginActivity.this, errMsgArr.get(0).toString());
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", LoginActivity.this);
                    }
                    return false;
                }

            }));
        }
    }

    @OnClick(R2.id.tvLoginSignupLink)
    void signup() {
        etSignupEmail.setVisibility(View.VISIBLE);
        etSignupUsername.setVisibility(View.VISIBLE);
        etSignupPassword.setVisibility(View.VISIBLE);
        etSignupConfirmPassword.setVisibility(View.VISIBLE);
        btnSignup.setVisibility(View.VISIBLE);
        tvSignupLoginagain.setVisibility(View.VISIBLE);
        tvSignupText1.setVisibility(View.VISIBLE);
        tvSignupText.setVisibility(View.VISIBLE);
        frameSignupFacebook.setVisibility(View.VISIBLE);

        etLoginUsername.setVisibility(View.GONE);
        etLoginPassword.setVisibility(View.GONE);
        btnLoginSignup.setVisibility(View.GONE);
        tvLoginForgotPwd.setVisibility(View.GONE);
        tvLoginSignupLink.setVisibility(View.GONE);
        tvLoginFacebook.setVisibility(View.GONE);
        frameLoginFacebook.setVisibility(View.GONE);

        etForgotPwdEmail.setVisibility(View.GONE);
        btnForgotPwd.setVisibility(View.GONE);
        tvForgotPwdLoginagain.setVisibility(View.GONE);
//        toolbar.setTitle("Signup");
    }

    @OnClick({R2.id.tvSignupLoginagain, R2.id.tvForgotPwdLoginagain})
    void doLogin() {
        etSignupEmail.setVisibility(View.GONE);
        etSignupUsername.setVisibility(View.GONE);
        etSignupPassword.setVisibility(View.GONE);
        etSignupConfirmPassword.setVisibility(View.GONE);
        btnSignup.setVisibility(View.GONE);
        tvSignupLoginagain.setVisibility(View.GONE);
        tvSignupText1.setVisibility(View.GONE);
        tvSignupText.setVisibility(View.GONE);
        frameSignupFacebook.setVisibility(View.GONE);

        etLoginUsername.setVisibility(View.VISIBLE);
        etLoginPassword.setVisibility(View.VISIBLE);
        btnLoginSignup.setVisibility(View.VISIBLE);
        tvLoginForgotPwd.setVisibility(View.VISIBLE);
        tvLoginSignupLink.setVisibility(View.VISIBLE);
        tvLoginFacebook.setVisibility(View.VISIBLE);
        frameLoginFacebook.setVisibility(View.VISIBLE);

        etForgotPwdEmail.setVisibility(View.GONE);
        btnForgotPwd.setVisibility(View.GONE);
        tvForgotPwdLoginagain.setVisibility(View.GONE);
//        toolbar.setTitle("Login");
    }

    @OnClick(R2.id.tvLoginForgotPwd)
    void forgotPwdLink() {
        etSignupEmail.setVisibility(View.GONE);
        etSignupUsername.setVisibility(View.GONE);
        etSignupPassword.setVisibility(View.GONE);
        etSignupConfirmPassword.setVisibility(View.GONE);
        btnSignup.setVisibility(View.GONE);
        tvSignupText1.setVisibility(View.GONE);
        tvSignupText.setVisibility(View.GONE);
        frameSignupFacebook.setVisibility(View.GONE);

        tvSignupLoginagain.setVisibility(View.VISIBLE);

        etLoginUsername.setVisibility(View.GONE);
        etLoginPassword.setVisibility(View.GONE);
        btnLoginSignup.setVisibility(View.GONE);
        tvLoginForgotPwd.setVisibility(View.GONE);
        tvLoginSignupLink.setVisibility(View.GONE);
        tvLoginFacebook.setVisibility(View.GONE);
        frameLoginFacebook.setVisibility(View.GONE);

        etForgotPwdEmail.setVisibility(View.VISIBLE);
        btnForgotPwd.setVisibility(View.VISIBLE);
//        tvForgotPwdLoginagain.setVisibility(View.VISIBLE);
//        toolbar.setTitle("Forgot password");
    }

    @OnClick(R2.id.btnSignup)
    void signUp() {

        email = etSignupEmail.getText().toString().trim();
        username = etSignupUsername.getText().toString().trim();
        password = etSignupPassword.getText().toString().trim();
        confirmPwd = etSignupConfirmPassword.getText().toString().trim();

        if (email.length() > 0 && MyUtils.isValidEmail(email)) {
            if (username.length() > 0) {
                if (password.length() >= 6) {
                    if (confirmPwd.length() >= 6) {
                        if (confirmPwd.equals(password)) {
                            new callSignup().execute();
                        } else
                            MyUtils.showToast(this, "Password and confirmPwd does not match");
                    } else
                        MyUtils.showToast(this, "Confirm password should be at least 6 characters long");
                } else
                    MyUtils.showToast(this, "Password should be at least 6 characters long");
            } else
                MyUtils.showToast(this, "Enter valid username");
        } else
            MyUtils.showToast(this, "Enter valid email");
    }

    class callSignup extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(LoginActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("email", email));
                pair.add(new BasicNameValuePair("username", username));
                pair.add(new BasicNameValuePair("password", password));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Constant.postData(Constant.register_url, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();

                                try {

                                    jo = new JSONObject(JsonString);
                                    Log.i("log", jo.toString());
                                    String err = jo.getString("error");
                                    String msg = "";

                                    if (err.equals("0")) {
                                        String token = jo.getString("access_token");
                                        Log.i("log", token);
                                        Constant.saveSharedData(LoginActivity.this, "pref_login", token);
                                        Constant.saveSharedData(LoginActivity.this, "is_fab_connect", "false");
                                        Constant.saveSharedData(LoginActivity.this, "is_twitter_connect", "false");

                                        JSONObject user = jo.getJSONObject("user");
                                        Log.i("log user", user.getString("username"));
                                        Constant.saveSharedData(LoginActivity.this, "username", user.getString("username"));
                                        Constant.saveSharedData(LoginActivity.this, "myId", user.getString("id"));

//                                        startActivity(new Intent(LoginActivity.this, LoginActivity.class));

                                        Log.e("VOTOCAST",token + " - " + user.getString("username") + " - " + user.getString("id"));

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        JSONObject validation = jo.getJSONObject("validation");
                                        if(validation.has("email")) {
                                            JSONArray errMsgArr = validation.getJSONArray("email");
                                            msg = msg + " " +errMsgArr.getString(0);
                                            Log.i("Error",msg);
                                        }
                                        if(validation.has("username")) {
                                            JSONArray errMsgArr = validation.getJSONArray("username");
                                            msg = msg + " " +errMsgArr.getString(0);
                                            Log.i("Error",msg);
                                        }
//                                        Log.i("log",jo.getJSONObject("validation").toString());
//                                        Log.i("log",validation.getJSONArray("email").toString());
//                                        Log.i("log",errMsgArr.get(0).toString());

                                        MyUtils.showToast(LoginActivity.this, msg);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", LoginActivity.this);
                    }
                    return false;
                }
            }));
        }
    }

    @OnClick(R2.id.btnLoginSignup)
    void login() {
        username = etLoginUsername.getText().toString().trim();
        password = etLoginPassword.getText().toString().trim();

        if (username.length() > 0) {
            if (password.length() >= 6) {
                new callLogin().execute();
            } else
                MyUtils.showToast(this, "Password should be at least 6 characters long");
        } else
            MyUtils.showToast(this, "Enter valid username");
    }

    class callLogin extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//            mProgressDialog.show();
            super.onPreExecute();

            mProgressHUD = ProgressHUD.show(LoginActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("username", username));
                pair.add(new BasicNameValuePair("password", password));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Constant.postData(Constant.login_url, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();

                                try {

                                    jo = new JSONObject(JsonString);
                                    Log.i("log", jo.toString());
                                    String err = jo.getString("error");
//                                    String msg = jo.getString("message");

                                    if (err.equals("0")) {
                                        String token = jo.getString("access_token");
                                        Log.i("log", token);
                                        Constant.saveSharedData(LoginActivity.this, "pref_login", token);
                                        Constant.saveSharedData(LoginActivity.this, "is_fab_connect", "false");
                                        Constant.saveSharedData(LoginActivity.this, "is_twitter_connect", "false");

                                        JSONObject user = jo.getJSONObject("user");
                                        Log.i("log user", user.getString("username"));
                                        Constant.saveSharedData(LoginActivity.this, "username", user.getString("username"));
                                        Constant.saveSharedData(LoginActivity.this, "myId", user.getString("id"));

                                        Log.e("VOTOCAST",token + " - " + user.getString("username") + " - " + user.getString("id"));

//                                        MyUtils.showToast(LoginActivity.this,"Login successful");
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
//                                        JSONObject validation = jo.getJSONObject("validation");
                                        String errMsg = jo.getString("message");
//                                        Log.i("log",jo.getJSONObject("validation").toString());
//                                        Log.i("log",validation.getJSONArray("email").toString());
//                                        Log.i("log",errMsgArr.get(0).toString());

                                        MyUtils.showToast(LoginActivity.this, errMsg);
                                    }

                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), LoginActivity.this);
                                }
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", LoginActivity.this);
                    }
                    return false;
                }
            }));
        }
    }

    @OnClick(R2.id.btnForgotPwd)
    void forgotPwd() {

        email = etForgotPwdEmail.getText().toString().trim();

        if (email.length() > 0 && MyUtils.isValidEmail(email)) {
            new callForgotPwd().execute();
        } else
            MyUtils.showToast(this, "Enter valid email");
    }

    class callForgotPwd extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//            mProgressDialog.show();
            super.onPreExecute();

            mProgressHUD = ProgressHUD.show(LoginActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("email", email));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Constant.postData(Constant.forgot_url, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();

                                try {

                                    jo = new JSONObject(JsonString);
//                                    jo = jo.getJSONObject("message");

                                    String msg = jo.getString("message");
                                    String err = jo.getString("error");

                                    if (err.equals("0")) {

                                        MyUtils.showToast(LoginActivity.this, "" + msg);
                                        etSignupEmail.setVisibility(View.GONE);
                                        etSignupUsername.setVisibility(View.GONE);
                                        etSignupPassword.setVisibility(View.GONE);
                                        etSignupConfirmPassword.setVisibility(View.GONE);
                                        btnSignup.setVisibility(View.GONE);
                                        tvSignupLoginagain.setVisibility(View.GONE);

                                        etLoginUsername.setVisibility(View.VISIBLE);
                                        etLoginPassword.setVisibility(View.VISIBLE);
                                        btnLoginSignup.setVisibility(View.VISIBLE);
                                        tvLoginForgotPwd.setVisibility(View.VISIBLE);
                                        tvLoginSignupLink.setVisibility(View.VISIBLE);

                                        etForgotPwdEmail.setVisibility(View.GONE);
                                        btnForgotPwd.setVisibility(View.GONE);
                                        tvForgotPwdLoginagain.setVisibility(View.GONE);
//                                        toolbar.setTitle("Login");

                                    } else {
                                        MyUtils.showToast(LoginActivity.this, "" + msg);
                                    }

                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), LoginActivity.this);
                                }
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", LoginActivity.this);
                    }
                    return false;
                }
            }));
        }
    }

    @OnClick(R2.id.tvSignupText2)void fnGoToTerms(){
        startActivity(new Intent(this, TermsOfServiceActivity.class));
    }
    @OnClick(R2.id.tvSignupText4)void fnGoToPolicy(){
        startActivity(new Intent(this, PrivacyPolicyActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStop(this);
    }
}
