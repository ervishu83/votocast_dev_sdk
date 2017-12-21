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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class NoticationActivity extends AppCompatActivity {

    RadioGroup rgNotificationVotes;
    RadioGroup rgNotificationComments;
    RadioGroup rgNotificationNewFollowers;
    RadioGroup rgNotificationNewContest;

    @BindView(R2.id.tvNotificationTitle1)
    TextView tvNotificationTitle1;
    @BindView(R2.id.tvNotificationTitle2)
    TextView tvNotificationTitle2;
    @BindView(R2.id.tvNotificationTitle3)
    TextView tvNotificationTitle3;
    @BindView(R2.id.tvNotificationTitle4)
    TextView tvNotificationTitle4;

    @BindView(R2.id.rbNotificationVotes1)RadioButton rbNotificationVotes1;
    @BindView(R2.id.rbNotificationVotes2)RadioButton rbNotificationVotes2;
    @BindView(R2.id.rbNotificationVotes3)RadioButton rbNotificationVotes3;

    @BindView(R2.id.rbNotificationComments1)RadioButton rbNotificationComments1;
    @BindView(R2.id.rbNotificationComments2)RadioButton rbNotificationComments2;
    @BindView(R2.id.rbNotificationComments3)RadioButton rbNotificationComments3;

    @BindView(R2.id.rbNotificationNewFollowers1)RadioButton rbNotificationNewFollowers1;
    @BindView(R2.id.rbNotificationNewFollowers2)RadioButton rbNotificationNewFollowers2;

    @BindView(R2.id.rbNotificationNewContest1)RadioButton rbNotificationNewContest1;
    @BindView(R2.id.rbNotificationNewContest2)RadioButton rbNotificationNewContest2;

    private ArrayList<NameValuePair> pair;

    String vote_share, comments, new_followers, new_contests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNotification);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setBackgroundColor(Color.parseColor(Constant.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Constant.colorPrimaryDark));
        }

        Tracker t = ((MyAppTracker)this.getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Notification");
        t.send(new HitBuilders.AppViewBuilder().build());

        rgNotificationVotes = (RadioGroup) findViewById(R.id.rgNotificationVotes);
        rgNotificationComments = (RadioGroup) findViewById(R.id.rgNotificationComments);
        rgNotificationNewFollowers = (RadioGroup) findViewById(R.id.rgNotificationNewFollowers);
        rgNotificationNewContest = (RadioGroup) findViewById(R.id.rgNotificationNewContest);

        Constant.setDisplayFontsBold(this, tvNotificationTitle1);
        Constant.setDisplayFontsBold(this, tvNotificationTitle2);
        Constant.setDisplayFontsBold(this, tvNotificationTitle3);
        Constant.setDisplayFontsBold(this, tvNotificationTitle4);

        Constant.setDisplayFontsRegular(this,rbNotificationVotes1);
        Constant.setDisplayFontsRegular(this,rbNotificationVotes2);
        Constant.setDisplayFontsRegular(this,rbNotificationVotes3);

        Constant.setDisplayFontsRegular(this,rbNotificationComments1);
        Constant.setDisplayFontsRegular(this,rbNotificationComments2);
        Constant.setDisplayFontsRegular(this,rbNotificationComments3);

        Constant.setDisplayFontsRegular(this,rbNotificationNewFollowers1);
        Constant.setDisplayFontsRegular(this,rbNotificationNewFollowers2);

        Constant.setDisplayFontsRegular(this,rbNotificationNewContest1);
        Constant.setDisplayFontsRegular(this,rbNotificationNewContest2);

        rgNotificationVotes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R2.id.rbNotificationVotes1:
//                        MyUtils.showToast(NoticationActivity.this, "off");
                        vote_share = "off";
                        break;

                    case R2.id.rbNotificationVotes2:
//                        MyUtils.showToast(NoticationActivity.this, "followers");
                        vote_share = "followers";
                        break;

                    case R2.id.rbNotificationVotes3:
//                        MyUtils.showToast(NoticationActivity.this, "everyone");
                        vote_share = "everyone";
                        break;
                }
            }
        });

        rgNotificationComments.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R2.id.rbNotificationComments1:
//                        MyUtils.showToast(NoticationActivity.this, "comment off");
                        comments = "off";
                        break;

                    case R2.id.rbNotificationComments2:
//                        MyUtils.showToast(NoticationActivity.this, "comment followers");
                        comments = "followers";
                        break;

                    case R2.id.rbNotificationComments3:
//                        MyUtils.showToast(NoticationActivity.this, "comment everyone");
                        comments = "everyone";
                        break;
                }
            }
        });

        rgNotificationNewFollowers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R2.id.rbNotificationNewFollowers1:
//                        MyUtils.showToast(NoticationActivity.this, "followers off");
                        new_followers = "off";
                        break;

                    case R2.id.rbNotificationNewFollowers2:
//                        MyUtils.showToast(NoticationActivity.this, "follower everyone");
                        new_followers = "everyone";
                        break;
                }
            }
        });

        rgNotificationNewContest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R2.id.rbNotificationNewContest1:
//                        MyUtils.showToast(NoticationActivity.this, "contest off");
                        new_contests = "off";
                        break;

                    case R2.id.rbNotificationNewContest2:
//                        MyUtils.showToast(NoticationActivity.this, "contest everyone");
                        new_contests = "on";
                        break;
                }
            }
        });
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
                new callNotification().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class callNotification extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(NoticationActivity.this, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                String token = Constant.getShareData(NoticationActivity.this, "pref_login");
                pair = new ArrayList<NameValuePair>();
                pair.add(new BasicNameValuePair("access_token", token));
                pair.add(new BasicNameValuePair("vote_share", vote_share));
                pair.add(new BasicNameValuePair("comments", comments));
                pair.add(new BasicNameValuePair("new_followers", new_followers));
                pair.add(new BasicNameValuePair("new_contests", new_contests));

                Log.i("notification", vote_share + "-" + comments + "-" + new_followers + "-" + new_contests);

            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Constant.postData(Constant.set_notifications_url, pair, new Handler(new Handler.Callback() {
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
                                    String msg = jo.getString("message");

                                    if (err.equals("0")) {
                                        finish();
                                    } else {
                                        MyUtils.showToast(NoticationActivity.this, msg);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", NoticationActivity.this);
                    }
                    return false;
                }
            }));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(NoticationActivity.this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(NoticationActivity.this).reportActivityStop(this);
    }
}
