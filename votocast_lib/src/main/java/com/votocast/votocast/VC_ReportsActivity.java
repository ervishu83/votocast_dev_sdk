package com.votocast.votocast;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import db.Reports;

public class VC_ReportsActivity extends AppCompatActivity {

    @BindView(R2.id.reportRadioGroup)
    RadioGroup reportRadioGroup;
    ArrayList<Reports> reportList;
    String reportId="",vidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReports);
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
        t.setScreenName("Reports");
        t.send(new HitBuilders.AppViewBuilder().build());

        Intent m1 = getIntent();
        reportList = (ArrayList<Reports>) m1.getSerializableExtra("report");
        vidId = m1.getStringExtra("vidId");


        for (int i = 0; i < reportList.size(); i++) {

//            final View v = getLayoutInflater().inflate(R.layout.report_item, null);
//            TextView reportTitle = (TextView) v.findViewById(R.id.reportTitle);
//            TextView reportDesc = (TextView) v.findViewById(R.id.reportDesc);
//            final RadioButton reportRadio = (RadioButton) v.findViewById(R.id.reportCheckBox);

//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            v.setLayoutParams(params);

//            Constant.setDisplayFontsSemibold(this, reportTitle);
//            Constant.setDisplayFontsRegular(this, reportDesc);

//            reportRadio.setId(Integer.parseInt(reportList.get(i).getId()));
//            reportTitle.setText(reportList.get(i).getTitle());
//            reportDesc.setText(reportList.get(i).getDesc());


//            reportRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    int id = compoundButton.getId();
////                    if(id == 1 && b == true){
//                    for (int i = 0; i < reportList.size(); i++) {
//                        if (id != Integer.parseInt(reportList.get(i).getId()) && b== true) {
//                            RadioButton ch1 = (RadioButton) v.findViewById(Integer.parseInt(reportList.get(i).getId()));
//                            ch1.setChecked(false);
//                        }
//                    }
////                }
//                }
//            });

            RadioButton button = new RadioButton(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setId(Integer.parseInt(reportList.get(i).getId()));
            button.setText(Html.fromHtml("<b>"+ reportList.get(i).getTitle() +"</b><br>"+ reportList.get(i).getDesc() +""));
            button.setGravity(Gravity.CENTER_VERTICAL);
            button.setPadding(10,10,10,10);
            button.setCompoundDrawablePadding(20);
            button.setButtonDrawable(R.drawable.ic_checkbox);

            reportRadioGroup.addView(button);
        }

        reportRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
//                Log.i("radio",checkedId + "");
                reportId = String.valueOf(checkedId);
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
                if (!reportId.equals(""))
                    new reportVideo().execute();
                else
                    MyUtils.showToast(this,"Please select an option or tap back arrow.");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class reportVideo extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ArrayList<NameValuePair> pair;
        ProgressHUD mProgressHUD;

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(VC_ReportsActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String token = Constant.getShareData(VC_ReportsActivity.this, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", vidId));
            pair.add(new BasicNameValuePair("report_id", reportId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.report_video_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        VC_ReportsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                        MyUtils.showToast(VC_ReportsActivity.this, posts);
                                        finish();
                                    }else if (error == 2) {
                                        final AlertDialog.Builder myReportDialog = new AlertDialog.Builder(VC_ReportsActivity.this);
                                        myReportDialog.setTitle("VOTOCAST")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel",null)
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ReportsActivity.this);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), VC_ReportsActivity.this);
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ReportsActivity.this);
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

            String token = Constant.getShareData(VC_ReportsActivity.this, "pref_login");
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {
                                        MyUtils.showToast(VC_ReportsActivity.this, posts);
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ReportsActivity.this);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), VC_ReportsActivity.this);
                                }
                            }
                        });
                    } else {
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", VC_ReportsActivity.this);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(VC_ReportsActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(VC_ReportsActivity.this).reportActivityStop(this);
    }
}
