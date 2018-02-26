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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

public class VC_ReportIssueActivity extends AppCompatActivity {

    @BindView(R2.id.evReportIssues)EditText evReportIssues;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReportIssue);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().show();
        toolbar.setBackgroundColor(Color.parseColor(Constant.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Constant.colorPrimaryDark));
        }

        Tracker t = ((MyAppTracker)this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Report Issue");
        t.send(new HitBuilders.AppViewBuilder().build());

        InputFilter[] lengthFilter = new InputFilter[1];
        lengthFilter[0] = new InputFilter.LengthFilter(400); //Filter to 9 characters
        evReportIssues.setFilters(lengthFilter);

        evReportIssues.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("Text count on",start + "-" + before + " - " + count);
                if(start >= 399) {
                    MyUtils.showToast(VC_ReportIssueActivity.this, "Maximum limit is 400 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R2.id.toolbar_back_button)void fnBack(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R2.id.action_done:
                if (!evReportIssues.getText().toString().trim().equals("")) {
                    String issue = evReportIssues.getText().toString();
                    new setReportIssuess(issue).execute();
                }
                else
                    MyUtils.showToast(this,"Enter valid report issue!");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class setReportIssuess extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        String issue;
        ProgressHUD mProgressHUD;

        public setReportIssuess(String issue) {
            this.issue =issue;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(VC_ReportIssueActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(VC_ReportIssueActivity.this, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("issue",issue));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.report_issue_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                        MyUtils.showToast(VC_ReportIssueActivity.this, posts);
                                        finish();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ReportIssueActivity.this);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), VC_ReportIssueActivity.this);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ReportIssueActivity.this);
                    }
                    if (mProgressHUD != null && mProgressHUD.isShowing())
                        mProgressHUD.dismiss();
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(VC_ReportIssueActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(VC_ReportIssueActivity.this).reportActivityStop(this);
    }
}
