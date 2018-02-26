package com.votocast.votocast;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;

public class VC_ChangePasswordActivity extends AppCompatActivity {

    @BindView(R2.id.evChangePwdOld)
    EditText evChangePwdOld;
    @BindView(R2.id.evChangePwdNew)
    EditText evChangePwdNew;
    @BindView(R2.id.evChangePwdConfirm)
    EditText evChangePwdConfirm;

    String oldPwd, NewPwd, ConfirmPwd;
    ArrayList<NameValuePair> pair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vc_activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChangePwd);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(Constant.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Constant.colorPrimaryDark));
        }

        ButterKnife.bind(this);

        Tracker t = ((MyAppTracker)this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Change Password");
        t.send(new HitBuilders.AppViewBuilder().build());

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Constant.setDisplayFontsRegular(this, evChangePwdOld);
        Constant.setDisplayFontsRegular(this, evChangePwdNew);
        Constant.setDisplayFontsRegular(this, evChangePwdConfirm);
    }

    @OnClick(R2.id.toolbar_back_button)void fnBack(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R2.id.action_done:
                changePwd();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changePwd() {
        oldPwd = evChangePwdOld.getText().toString().trim();
        NewPwd = evChangePwdNew.getText().toString().trim();
        ConfirmPwd = evChangePwdConfirm.getText().toString().trim();

        if (oldPwd.length() > 6) {
            if (NewPwd.length() > 6) {
                if (ConfirmPwd.length() > 6) {
                    if (NewPwd.equals(ConfirmPwd)) {
                        if(!oldPwd.equals(NewPwd)) {
                            new callPwdChange().execute();
                        }else
                            MyUtils.showToast(this, "Old password and New password must not be same");
                    } else
                        MyUtils.showToast(this, "New password and confirmPwd does not match");
                } else
                    MyUtils.showToast(this, "Confirm password should be at least 6 characters long");
            } else
                MyUtils.showToast(this, "New password should be at least 6 characters long");
        } else
            MyUtils.showToast(this, "Old password should be at least 6 characters long");
    }

    class callPwdChange extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD=ProgressHUD.show(VC_ChangePasswordActivity.this,"",false,false,new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                String token = Constant.getShareData(VC_ChangePasswordActivity.this, "pref_login");
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("access_token", token));
                pair.add(new BasicNameValuePair("old_password", oldPwd));
                pair.add(new BasicNameValuePair("new_password", NewPwd));

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Constant.postData(Constant.change_password_url, pair, new Handler(new Handler.Callback() {
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
                                        String errMsg = jo.getString("message");
                                        MyUtils.showToast(VC_ChangePasswordActivity.this, errMsg );
                                        finish();
                                    } else {
//                                        JSONObject validation = jo.getJSONObject("validation");
//                                        JSONArray errMsgArr = validation.getJSONArray("email");
//
//                                        MyUtils.showToast(VC_ChangePasswordActivity.this, errMsgArr.get(0).toString());

                                        String errMsg = jo.getString("message");
                                        MyUtils.showToast(VC_ChangePasswordActivity.this, errMsg );
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ChangePasswordActivity.this);
                    }
                    return false;
                }
            }));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(VC_ChangePasswordActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(VC_ChangePasswordActivity.this).reportActivityStop(this);
    }
}
