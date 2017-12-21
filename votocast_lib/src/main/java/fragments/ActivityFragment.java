package fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.votocast.votocast.MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import class_adapter.ActivityAdapter;
import class_adapter.CommentList;
import class_adapter.Constant;
import class_adapter.ProgressHUD;
import db.ActivityClass;
import db.Comment;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import utils.Toolbar_ActionMode_Callback;
import utils.Toolbar_ActionMode_Callback_Activity_Notification;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    @BindView(R2.id.listActivityFragment)
    StickyListHeadersListView listActivityFragment;
    ArrayList<ActivityClass> activityList;
    ActivityAdapter mAdapter;
    String token;
    private ActionMode mActionMode;
    public Activity mActivity;

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity, container, false);
        ButterKnife.bind(this, v);

        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Activity");
        t.send(new HitBuilders.AppViewBuilder().build());

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R.id.toolbarLogo);
        iv.setVisibility(View.GONE);
        RelativeLayout rlToolbar = (RelativeLayout) toolbar.findViewById(R.id.rlToolbar);
        rlToolbar.setVisibility(View.VISIBLE);
        TextView toolText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolText.setVisibility(View.VISIBLE);
        toolText.setText("ACTIVITY");
        ImageView toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.GONE);
        ImageView toolbar_delete_button = (ImageView) toolbar.findViewById(R.id.toolbar_delete_button);
        toolbar_delete_button.setVisibility(View.INVISIBLE);

//        ((MainActivity) getActivity()).setActionBarTitle("ACTIVITY");

        if(savedInstanceState != null)
            token = savedInstanceState.getString("token");
        else
            token = Constant.getShareData(getActivity(), "pref_login");

//        activityList = new ArrayList<ActivityClass>();
//        new getActivityList().execute();

        listActivityFragment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e("CommentVidId","long press --- " + position + " - " + activityList.get(position).toString());
                mActionMode = null;
                if(activityList.get(position).getNodeId() == 4) {
                    onListItemSelect(position);
                }
                return true;
            }
        });

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        final AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
                        myDialog.setTitle("Confirm")
                                .setMessage("Do you really want to close this app?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .create()
                                .show();
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }
    // ------------------- action mode ----------
    //List item select method
    private void onListItemSelect(int position) {
        mAdapter.toggleSelection(position);//Toggle the selection
        boolean hasCheckedItems = mAdapter.getSelectedCount() > 0;//Check if any items are already selected or not
        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
//            ActionMode myActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), myAdapter, list, true));
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback_Activity_Notification(getActivity(), mAdapter, activityList, true));
            Log.i("deleteNotification "," **  hasCheckedItems && mActionMode == null");
        }
        else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            mActionMode = null;
            Log.i("deleteNotification ","deleteRows ** !hasCheckedItems && mActionMode != null");
        }
        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(mAdapter
                    .getSelectedCount()) + " selected");
            Log.i("deleteNotification ","deleteRows ** mActionMode != null");
        }
    }
    //Set action mode null after use
    public void setNullToActionMode(ActivityAdapter adapter) {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows(final Activity activity, final ActivityAdapter adapter, final ArrayList<ActivityClass> mList, final ActionMode newActionMode) {

        mActivity = activity;
        mAdapter = adapter;
        activityList = mList;
        listActivityFragment = (StickyListHeadersListView) mActivity.findViewById(R.id.listActivityFragment);
//        scrollView = (ScrollView) mActivity.findViewById(R.id.scrollViewFragment);
//        tvLoadMore = (TextView) mActivity.findViewById(R.id.tvLoadMoreFragment);
//        viewDividerAbove = mActivity.findViewById(R.id.viewDividerAboveFragment);
        mActionMode = newActionMode;

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(activity);
        myDialog.setTitle("VOTOCAST")
                .setMessage("Are you sure want to delete this notification?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SparseBooleanArray selected = mAdapter.getSelectedIds();//Get selected ids
                        Log.i("deleteNotification ","deleteRows ** " + mAdapter.getSelectedIds().size());
                        //Loop all selected ids
                        for (int k = (selected.size() - 1); k >= 0; k--) {
                            if (selected.valueAt(k)) {
                                //If current id is selected remove the item via key
                                new deleteNotification(selected.keyAt(k)).execute();
//                mActionMode.finish();
//                list.remove(selected.keyAt(i));
//                myAdapter.notifyDataSetChanged();//notify adapter
//                setListViewHeightBasedOnChildren(rvListComment);
//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.fullScroll(View.FOCUS_DOWN);
//                    }
//                });
                            }
                        }
                        Log.i("deleteNotification ","deleteRows ** item deleted" + mAdapter.getCount());
                        mActionMode.finish();//Finish action mode after use
                        mActionMode = null;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mActionMode.finish();//Finish action mode after use
                        mActionMode = null;
                    }
                });

        AlertDialog alert = myDialog.create();
        alert.show();

    }

    // ----------------- end of action mode ------------------------

    @Override
    public void onResume() {
        super.onResume();
        activityList = new ArrayList<ActivityClass>();
        new getActivityList().execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("token",token);
        super.onSaveInstanceState(outState);
    }

    class getActivityList extends AsyncTask<Void, Void, Void> {
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

            String cURL = Constant.get_activities_url;
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
                                    Constant.serverDate = jo.getString("current_datetime");

                                    Log.i("activity1",jo.toString());

                                    if (jo.getInt("error") == 0) {
                                        Log.i("activity2",jo.getInt("error")+"");

                                        Log.i("activity3",jo.getJSONArray("campaigns").toString()+"");

                                        JSONArray notificationArr = jo.getJSONArray("notifications");
//                                        Log.i("activity",notificationArr.length()+"");
                                        if(notificationArr.length() > 0) {
                                            for (int i = 0; i < notificationArr.length(); i++) {
                                                JSONObject notificationObj = notificationArr.getJSONObject(i);
                                                JSONObject actDetail = notificationObj.getJSONObject("Activity");
                                                JSONObject mainUser = notificationObj.getJSONObject("User");
//                                                JSONObject otherUser = notificationObj.getJSONObject("Campaign");

                                                activityList.add(new ActivityClass(4, i, actDetail.getString("id"),actDetail.getString("action"), actDetail.getString("content") ,actDetail.getString("created_on"), mainUser.getString("id"), mainUser.getString("username"), mainUser.getString("photo_path"), "", "", ""));
//                                                Log.e("Notification",actDetail.getString("content"));
                                            }
                                        }

                                        JSONArray campArr = jo.getJSONArray("campaigns");
                                        Log.i("activity",campArr.length()+"");
                                        if(campArr.length() > 0) {
                                            for (int i = 0; i < campArr.length(); i++) {
                                                JSONObject campObj = campArr.getJSONObject(i);
                                                JSONObject actDetail = campObj.getJSONObject("Activity");
                                                JSONObject mainUser = campObj.getJSONObject("User");
                                                JSONObject otherUser = campObj.getJSONObject("Campaign");

                                                activityList.add(new ActivityClass(1, i, actDetail.getString("id"),actDetail.getString("action"), actDetail.getString("content"), actDetail.getString("created_on"), mainUser.getString("id"), mainUser.getString("username"), mainUser.getString("photo_path"), otherUser.getString("id"), otherUser.getString("title"), ""));

                                            }
                                        }
                                        JSONArray userArr = jo.getJSONArray("users");
                                        if(userArr.length() > 0) {
                                            for (int i = 0; i < userArr.length(); i++) {
                                                JSONObject userObj = userArr.getJSONObject(i);

                                                JSONObject actDetail = userObj.getJSONObject("Activity");
                                                JSONObject mainUser = userObj.getJSONObject("User");
                                                JSONObject otherUser = userObj.getJSONObject("User_Other");

//                                            Log.i("activity",2 + "-" +i + "-" + actDetail.getString("action")+ "-" + actDetail.getString("created_on")+ "-" + mainUser.getString("id")+ "-" + mainUser.getString("username")+ "-" + mainUser.getString("photo_path")+ "-" +otherUser.getString("id")+ "-" + otherUser.getString("short_code")+ "-" +otherUser.getString("photo_path"))););
                                                activityList.add(new ActivityClass(2, i, actDetail.getString("id"),actDetail.getString("action"), actDetail.getString("content"), actDetail.getString("created_on"), mainUser.getString("id"), mainUser.getString("username"), mainUser.getString("photo_path"), otherUser.getString("id"), otherUser.getString("username"), otherUser.getString("photo_path")));

                                            }
                                        }

                                        JSONArray vidArr = jo.getJSONArray("videos");
                                        if(vidArr.length() > 0){
                                            for (int i = 0; i < vidArr.length(); i++) {
                                                JSONObject vidObj = vidArr.getJSONObject(i);
                                                JSONObject actDetail = vidObj.getJSONObject("Activity");
                                                JSONObject mainUser = vidObj.getJSONObject("User");
                                                JSONObject otherUser = vidObj.getJSONObject("Video");

                                                activityList.add(new ActivityClass(3, i, actDetail.getString("id"),actDetail.getString("action"), actDetail.getString("content"), actDetail.getString("created_on"), mainUser.getString("id"), mainUser.getString("username"), mainUser.getString("photo_path"), otherUser.getString("id"), "", otherUser.getString("photo_path")));
                                            }
                                        }


                                        Log.i("activity",activityList.toString());
                                        mAdapter = new ActivityAdapter(Constant.getShareData(getActivity(),"myId"),getActivity(), activityList);
                                        listActivityFragment.setAdapter(mAdapter);
                                    }
                                } catch (Exception e) {
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

    class deleteNotification extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        private ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        String activity_id;
        int position;

        public deleteNotification(int position) {
            this.position = position;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressHUD = ProgressHUD.show(mActivity, "", false, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            activity_id = activityList.get(this.position).getActionId();

            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("activity_id", activity_id));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.delete_activities_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

//                                        Constant.serverDate = jo.getString("current_datetime");
//
//                                        JSONObject campObj = jo.getJSONObject("campaign");
//                                        JSONArray commArr = jo.getJSONArray("comments");
//                                        for (int i = 0; i < commArr.length(); i++) {
//                                            JSONObject commObj = commArr.getJSONObject(i);
//                                            JSONObject vidObj = commObj.getJSONObject("VideoComment");
//                                            JSONObject userObj = commObj.getJSONObject("User");
//                                            JSONObject blankObj = commObj.getJSONObject("0");
//
//                                            list.add(new Comment(vidObj.getString("id"), vidObj.getString("video_id"), userObj.getString("id"), vidObj.getString("comments"), userObj.getString("username"), userObj.getString("photo_path"), vidObj.getString("created_on"),blankObj.getString("self_commented")));
//                                        }

                                        activityList.remove(position);
                                        mAdapter.notifyDataSetChanged();//notify adapter
                                    }
//
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(),mActivity);
                                    Log.e("CommentVidId ","Error //" + e.getMessage() + " - " + e.getLocalizedMessage());
                                }
                                if (mProgressHUD.isShowing() && mProgressHUD != null)
                                    mProgressHUD.dismiss();
                            }
                        });
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                    }
                    return false;
                }
            }));
            super.onPostExecute(aVoid);
        }
    }
}
