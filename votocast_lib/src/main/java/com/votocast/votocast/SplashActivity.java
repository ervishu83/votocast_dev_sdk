package com.votocast.votocast;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONObject;

import class_adapter.Constant;
import class_adapter.MyUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Fabric.with(this, new Crashlytics());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        Tracker t = ((MyAppTracker) this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Splash");
        t.send(new HitBuilders.AppViewBuilder().build());

//        Intent intent = getIntent();
//        Constant.Authorization = intent.getStringExtra("authToken");

        setAppColors();
//        final String token = Constant.getShareData(SplashActivity.this, "pref_login");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (token.equals(""))
//                    startActivity(new Intent(SplashActivity.this, VC_LoginActivity.class));
//                else
//                    startActivity(new Intent(SplashActivity.this, VC_MainActivity.class));
//                finish();
//            }
//        }, 4000);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(SplashActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(SplashActivity.this).reportActivityStop(this);
    }

    public void setAppColors() {
        new callAccountData().execute();
    }

    class callAccountData extends AsyncTask<Void, Void, Void> {
        JSONObject jo;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Constant.getData(Constant.get_account_appdata_url, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(final Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    jo = new JSONObject(JsonString);
                                    Log.i("log", jo.toString());
                                    String err = jo.getString("error");

                                    if (err.equals("0")) {
                                        String errMsg = jo.getString("message");
                                        JSONObject dataObj = jo.getJSONObject("account_appdata");

                                        Constant.colorPrimary= dataObj.getString("main_app_color");
                                        Constant.colorPrimaryDark= dataObj.getString("primary_dark_color");
                                        Constant.unselectedTab= dataObj.getString("unselected_tab_color");
                                        Constant.searchEditBack= dataObj.getString("search_background_color");

//                                        Constant.colorPrimary= "#8acac9";
//                                        Constant.colorPrimaryDark= "#F57C89";
//                                        Constant.unselectedTab= "#90f1ef";
//                                        Constant.searchEditBack= "#3b3b3b";
//                                        Log.e("Color --- " , Constant.colorPrimary);

                                        final String token = Constant.getShareData(SplashActivity.this, "pref_login");
                                        if (token.equals(""))
                                            startActivity(new Intent(SplashActivity.this, VC_LoginActivity.class));
                                        else
                                            startActivity(new Intent(SplashActivity.this, VC_MainActivity.class));
                                        finish();

                                    } else {
                                        String errMsg = jo.getString("message");
                                        MyUtils.showToast(SplashActivity.this, errMsg);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", SplashActivity.this);
                    }
                    return false;
                }
            }));
        }
    }
}
