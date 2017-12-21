package com.votocast.votocast;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;

public class ContestRulesActivity extends AppCompatActivity {

    @BindView(R2.id.webContestRules)WebView webContestRules;
    ProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_rules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarContestRules);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        toolbar.setBackgroundColor(Color.parseColor(Constant.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Constant.colorPrimaryDark));
        }

        Tracker t = ((MyAppTracker)this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Contest Rules");
        t.send(new HitBuilders.AppViewBuilder().build());

        WebSettings settings = webContestRules.getSettings();
        settings.setJavaScriptEnabled(true);
        webContestRules.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        Intent m1 = getIntent();
        String url = m1.getStringExtra("ruleUrl");
        Log.i("RulesUrl",url);

        Log.e("rules","----------- activity called");

        if (Build.VERSION.SDK_INT >= 19) // KITKAT
        {
            webContestRules.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mProgressHUD = ProgressHUD.show(this, "", false, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        webContestRules.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
//                Log.i(TAG, "Finished loading URL: " +url);
                if (mProgressHUD.isShowing()) {
                    mProgressHUD.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (mProgressHUD.isShowing()) {
                    mProgressHUD.dismiss();
                }
                MyUtils.showToast(ContestRulesActivity.this,"Something goes wrong!");
            }
        });

        webContestRules.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
//        webContestRules.loadUrl(url);
    }

    @OnClick(R2.id.toolbar_back_button)void fnBack(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(ContestRulesActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(ContestRulesActivity.this).reportActivityStop(this);
    }
}
