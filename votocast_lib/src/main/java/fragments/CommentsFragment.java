package fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;
import com.votocast.votocast.VC_MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import com.votocast.votocast.R2;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import class_adapter.CommentList;
import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import db.Comment;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.Toolbar_ActionMode_Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment implements View.OnClickListener {

    @BindView(R2.id.rvListCommentFragment)
    ListView rvListComment;
    @BindView(R2.id.civCommentVideoPicFragment)
    CircleImageView civCommentVideoPic;
    @BindView(R2.id.tvCommentUsernameFragment)
    TextView tvCommentUsername;
    @BindView(R2.id.tvCommentDateFragment)
    TextView tvCommentDate;
    @BindView(R2.id.tvCommentDescFragment)
    TextView tvCommentDesc;
    @BindView(R2.id.evCommentDataFragment)
    EditText evCommentData;
    @BindView(R2.id.tvLoadMoreFragment)
    TextView tvLoadMore;
    @BindView(R2.id.scrollViewFragment)
    ScrollView scrollView;
    @BindView(R2.id.viewDividerAboveFragment)
    View viewDividerAbove;
    @BindView(R2.id.tvBtnSendCommentFragment)
    TextView tvBtnSendCommentFragment;

    ArrayList<Comment> list;
    String vidId, pic, name, desc, date, userId;
    int loadMore = 1;
    CommentList myAdapter;
    ImageView toolbar_back_button;

    // ------------- edit comment ----------
    private ActionMode mActionMode;
    Toolbar toolbar;
    private Activity mActivity;
    String oldComment = "";

    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, v);

        Tracker t = ((MyAppTracker) getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Comment");
        t.send(new HitBuilders.AppViewBuilder().build());

        ((VC_MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((VC_MainActivity) getActivity()).getSupportActionBar().show();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarMain);
        ImageView iv = (ImageView) toolbar.findViewById(R.id.toolbarLogo);
        iv.setVisibility(View.GONE);
        TextView toolText = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolText.setVisibility(View.VISIBLE);
        toolText.setText("COMMENTS");
        RelativeLayout rlToolbar = (RelativeLayout) toolbar.findViewById(R.id.rlToolbar);
        rlToolbar.setVisibility(View.VISIBLE);
        toolbar_back_button = (ImageView) toolbar.findViewById(R.id.toolbar_back_button);
        toolbar_back_button.setVisibility(View.VISIBLE);
        toolbar_back_button.setOnClickListener(this);

        tvBtnSendCommentFragment.setTextColor(Color.parseColor(Constant.colorPrimary));

        Bundle bundle = getArguments();
        vidId = bundle.getString("vidId");
        pic = bundle.getString("pic");
        name = bundle.getString("name");
        desc = bundle.getString("desc");
        date = bundle.getString("date");
        userId = bundle.getString("userId");

        Log.i("CommentVidId OnScreen", "Id - " + vidId);

        Constant.setTextFontsMedium(getActivity(), tvCommentUsername);
        Constant.setTextFontsRegular(getActivity(), tvCommentDate);
        Constant.setTextFontsRegular(getActivity(), tvCommentDesc);

        Constant.setTextFontsMedium(getActivity(), tvLoadMore);

        list = new ArrayList<Comment>();
        new getComment(loadMore).execute();

        rvListComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e("CommentVidId", "long press --- " + position + " - " + list.get(position).toString());
                mActionMode = null;
                if (list.get(position).getSelf_commented().equals("yes")) {
                    onListItemSelect(position);
                }
                return true;
            }
        });
        initEditText();

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    //-------------------------edit comment action mode --------------
    private void initEditText() {
        evCommentData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("search* onTextChanged", charSequence.toString() + " - " + charSequence.toString().length() + " - " + evCommentData.getText().length());
                if (charSequence.toString().equals("")) {
                    Constant.isNormalComment = true;
                    Log.i("search onTextChanged", "----------isNormalComment ******** " + Constant.isNormalComment);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    // ------------------- action mode ----------
    //List item select method
    private void onListItemSelect(int position) {
        myAdapter.toggleSelection(position);//Toggle the selection
        boolean hasCheckedItems = myAdapter.getSelectedCount() > 0;//Check if any items are already selected or not
        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
//            ActionMode myActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), myAdapter, list, true));
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), myAdapter, list, true));
            Log.i("CommentVidId ", " **  hasCheckedItems && mActionMode == null");
        } else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            mActionMode = null;
            Log.i("CommentVidId ", "deleteRows ** !hasCheckedItems && mActionMode != null");
        }
        if (mActionMode != null) {
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(myAdapter
                    .getSelectedCount()) + " selected");
            Log.i("CommentVidId ", "deleteRows ** mActionMode != null");
        }
    }

    //Set action mode null after use
    public void setNullToActionMode(CommentList adapter) {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Edit selected rows
    public void editRows(final Activity activity, final CommentList adapter, final ArrayList<Comment> mList, final ActionMode newActionMode) {

        mActivity = activity;
        myAdapter = adapter;
        list = mList;
        rvListComment = (ListView) mActivity.findViewById(R.id.rvListCommentFragment);
        scrollView = (ScrollView) mActivity.findViewById(R.id.scrollViewFragment);
        tvLoadMore = (TextView) mActivity.findViewById(R.id.tvLoadMoreFragment);
        viewDividerAbove = mActivity.findViewById(R.id.viewDividerAboveFragment);
        evCommentData = (EditText) mActivity.findViewById(R.id.evCommentDataFragment);
        mActionMode = newActionMode;

        Constant.isNormalComment = false;
        Log.e("search isNormalComment", "------edit ----isNormalComment- " + Constant.isNormalComment);

        SparseBooleanArray selected = myAdapter.getSelectedIds();//Get selected ids
        Log.i("CommentVidId ", "deleteRows ** " + myAdapter.getSelectedIds().size());
        //Loop all selected ids
        for (int k = (selected.size() - 1); k >= 0; k--) {
            if (selected.valueAt(k)) {
                Log.i("EditComment ", "EditComment ** " + list.get(selected.keyAt(k)).getCommentId() + " - " + list.get(selected.keyAt(k)).getComment());

                Constant.editCommentPosition = selected.keyAt(k);
                Constant.editCommentId = list.get(selected.keyAt(k)).getCommentId();
                Constant.oldCommentData = list.get(selected.keyAt(k)).getComment();
                evCommentData.setText(list.get(selected.keyAt(k)).getComment());
                oldComment = list.get(selected.keyAt(k)).getComment();

                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        evCommentData.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                        evCommentData.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                    }
                }, 200);

                Log.e("EditComment 1", Constant.editCommentId + " - " + Constant.editCommentPosition + " -- " + selected.keyAt(k));

//                new editComment(selected.keyAt(k)).execute();
            }
        }
//        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use
        mActionMode = null;

    }

    //Delete selected rows
    public void deleteRows(final Activity activity, final CommentList adapter, final ArrayList<Comment> mList, final ActionMode newActionMode) {

        mActivity = activity;
        myAdapter = adapter;
        list = mList;
        rvListComment = (ListView) mActivity.findViewById(R.id.rvListCommentFragment);
        scrollView = (ScrollView) mActivity.findViewById(R.id.scrollViewFragment);
        tvLoadMore = (TextView) mActivity.findViewById(R.id.tvLoadMoreFragment);
        viewDividerAbove = mActivity.findViewById(R.id.viewDividerAboveFragment);
        mActionMode = newActionMode;

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(activity);
        myDialog.setTitle("VOTOCAST")
                .setMessage("Are you sure want to delete this comment?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SparseBooleanArray selected = myAdapter.getSelectedIds();//Get selected ids
                        Log.i("CommentVidId ", "deleteRows ** " + myAdapter.getSelectedIds().size());
                        //Loop all selected ids
                        for (int k = (selected.size() - 1); k >= 0; k--) {
                            if (selected.valueAt(k)) {
                                //If current id is selected remove the item via key
                                new deleteComment(selected.keyAt(k)).execute();
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
//        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
                        Log.i("CommentVidId ", "deleteRows ** item deleted" + myAdapter.getCount());
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

    @OnClick(R2.id.tvLoadMoreFragment)
    void fnLoadMore() {
        loadMore = loadMore + 1;
        new getComment(loadMore).execute();
    }

    @OnClick(R2.id.tvBtnSendCommentFragment)
    void sendComment() {
        String comment = evCommentData.getText().toString().trim();
        if (comment.length() > 0) {
            Log.e("search sendComment", "----------isNormalComment ******** " + Constant.isNormalComment);
            if (Constant.isNormalComment)
                new setComment(comment).execute();
            else
                new editComment(comment).execute();

            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        } else
            MyUtils.showToast(getActivity(), "Enter valid comment!");
    }

    @OnClick(R2.id.tvCommentUsernameFragment)
    void fnUser() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle b1 = new Bundle();
        b1.putString("user_id", userId);
        fragment.setArguments(b1);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_back_button) {
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }
    }

    class setComment extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        String vidComment;

        public setComment(String comment) {
            vidComment = comment;
        }

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
            pair.add(new BasicNameValuePair("video_id", vidId));
            pair.add(new BasicNameValuePair("comments", vidComment));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.comment_video_url;
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

                                        Constant.serverDate = jo.getString("current_datetime");

                                        JSONObject campObj = jo.getJSONObject("campaign");
                                        JSONArray commArr = jo.getJSONArray("comments");
                                        for (int i = 0; i < commArr.length(); i++) {
                                            JSONObject commObj = commArr.getJSONObject(i);
                                            JSONObject vidObj = commObj.getJSONObject("VideoComment");
                                            JSONObject userObj = commObj.getJSONObject("User");

                                            list.add(new Comment(vidObj.getString("id"), vidObj.getString("video_id"), userObj.getString("id"), vidObj.getString("comments"), userObj.getString("username"), userObj.getString("photo_path"), vidObj.getString("created_on"), "yes"));
                                        }

                                        myAdapter = new CommentList(list, getActivity());
                                        rvListComment.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                        rvListComment.setAdapter(myAdapter);
                                        myAdapter.notifyDataSetChanged();
                                        setListViewHeightBasedOnChildren(rvListComment);
                                        scrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });

                                        if (tvLoadMore.getVisibility() == View.GONE) {
                                            tvLoadMore.setVisibility(View.GONE);
                                            viewDividerAbove.setVisibility(View.GONE);
                                        } else {
                                            tvLoadMore.setVisibility(View.VISIBLE);
                                            viewDividerAbove.setVisibility(View.VISIBLE);
                                        }
                                        evCommentData.setText("");

                                    } else if (error == 2) {
                                        final AlertDialog.Builder myVoteHomeDialog = new AlertDialog.Builder(getActivity());
                                        myVoteHomeDialog.setTitle("VOTOCAST")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
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
                                        MyUtils.showToast(getActivity(), posts);
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

    class getComment extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        int count;

        public getComment(int loadMore) {
            count = loadMore;
        }

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
            pair.add(new BasicNameValuePair("video_id", vidId));
            pair.add(new BasicNameValuePair("page", count + ""));
            pair.add(new BasicNameValuePair("limit", "10"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.get_comment_video_url;
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
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        if (loadMore != 1)
                                            Collections.reverse(list);

                                        Constant.serverDate = jo.getString("current_datetime");
//                                        Log.i("explore-CommentDate",jo.getString("current_datetime") + " - " +Constant.serverDate);

                                        JSONObject campObj = jo.getJSONObject("campaign");
//                                        tvCommentUsername.setText(campObj.getString("title"));
//                                        tvCommentDesc.setText(campObj.getString("description"));
//                                        tvCommentDate.setText(Constant.getDateCurrentTimeZone1(campObj.getString("created_on")));

                                        tvCommentUsername.setText(name);
                                        tvCommentDesc.setText(desc);
                                        tvCommentDate.setText(Constant.getDateCurrentTimeZone1(date));

                                        if (pic != null && !pic.equals(""))
                                            Picasso.with(getActivity()).load(pic).placeholder(R.drawable.campaign_icon).into(civCommentVideoPic);
                                        else
                                            Picasso.with(getActivity()).load(R.drawable.campaign_icon).into(civCommentVideoPic);

                                        JSONArray commArr = jo.getJSONArray("comments");

                                        if (commArr.length() == 0 || commArr.length() < 10) {
                                            tvLoadMore.setVisibility(View.GONE);
                                            viewDividerAbove.setVisibility(View.GONE);
                                        } else {
                                            tvLoadMore.setVisibility(View.VISIBLE);
                                            viewDividerAbove.setVisibility(View.VISIBLE);
                                        }

                                        for (int i = 0; i < commArr.length(); i++) {
                                            JSONObject commObj = commArr.getJSONObject(i);
                                            JSONObject vidObj = commObj.getJSONObject("VideoComment");
                                            JSONObject userObj = commObj.getJSONObject("User");
                                            JSONObject blankObj = commObj.getJSONObject("0");

                                            list.add(new Comment(vidObj.getString("id"), vidObj.getString("video_id"), userObj.getString("id"), vidObj.getString("comments"), userObj.getString("username"), userObj.getString("photo_path"), vidObj.getString("created_on"), blankObj.getString("self_commented")));
                                        }

                                        Collections.reverse(list);
                                        myAdapter = new CommentList(list, getActivity());
                                        rvListComment.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                        rvListComment.setAdapter(myAdapter);

                                        if (loadMore == 1) {
                                            rvListComment.setSelection(rvListComment.getAdapter().getCount() - 1);
                                            myAdapter.registerDataSetObserver(new DataSetObserver() {
                                                @Override
                                                public void onChanged() {
                                                    super.onChanged();
                                                    rvListComment.setSelection(myAdapter.getCount() - 1);
                                                }
                                            });

                                            scrollView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                                }
                                            });
                                        }

//                                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
//                                        manager.setStackFromEnd(true);
//                                        manager.setReverseLayout(true);
//                                        rvListComment.setLayoutManager(manager);
//                                        rvListComment.setHasFixedSize(true);
                                        setListViewHeightBasedOnChildren(rvListComment);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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

    class editComment extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        String vidComment;

        public editComment(String comment) {
            vidComment = comment;
            Log.e("EditComment 1", Constant.editCommentId + " - " + Constant.editCommentPosition);
            mActivity = getActivity();
//            Log.e("EditComment 2",getActivity().toString());
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
            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_id", vidId));
            pair.add(new BasicNameValuePair("comments", vidComment));
            pair.add(new BasicNameValuePair("comment_id", Constant.editCommentId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.comment_video_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    final Bundle response = message.getData();

                    if (response.getString("error").length() == 0) {
                        final String JsonString = response.getString("data");
                        Log.e("EditComment ** ", JsonString);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                mProgressHUD.dismiss();
                                try {
                                    jo = new JSONObject(JsonString);
                                    String posts = jo.getString("message");
                                    int error = jo.getInt("error");
                                    if (error == 0) {

                                        Log.e("EditComment ", jo.toString());

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

                                        Log.e("EditComment ", "myAdapter /// " + list.size());

//                                        list.remove(editCommentPosition);
//                                        list.set(editCommentPosition,);

                                        Comment oldComment = list.get(Constant.editCommentPosition);
                                        oldComment.setComment(vidComment);
                                        list.set(Constant.editCommentPosition, oldComment);

                                        myAdapter.notifyDataSetChanged();//notify adapter
                                        setListViewHeightBasedOnChildren(rvListComment);
                                        scrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
//                                        isNormalComment = true;
//
//                                        if (rvListComment != null)
//                                            Log.e("CommentVidId ","myAdapter // not null" + list.size());
//                                        else
//                                            Log.e("CommentVidId ","myAdapter // null" + list.size());

                                        myAdapter.notifyDataSetChanged();
                                        setListViewHeightBasedOnChildren(rvListComment);
                                        scrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });

                                        if (tvLoadMore.getVisibility() == View.GONE) {
                                            tvLoadMore.setVisibility(View.GONE);
                                            viewDividerAbove.setVisibility(View.GONE);
                                        } else {
                                            tvLoadMore.setVisibility(View.VISIBLE);
                                            viewDividerAbove.setVisibility(View.VISIBLE);
                                        }
                                        evCommentData.setText("");

                                    } else if (error == 2) {
                                        final AlertDialog.Builder myVoteHomeDialog = new AlertDialog.Builder(mActivity);
                                        myVoteHomeDialog.setTitle("VOTOCAST")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                    Log.e("CommentVidId ", "Error //" + e.getMessage() + " - " + e.getLocalizedMessage());
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

    class deleteComment extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        private ProgressHUD mProgressHUD;
        ArrayList<NameValuePair> pair;
        String commentId;
        int position;

        public deleteComment(int position) {
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

            commentId = list.get(this.position).getCommentId();

            String token = Constant.getShareData(mActivity, "pref_login");
            pair = new ArrayList<NameValuePair>();
            pair.add(new BasicNameValuePair("access_token", token));
            pair.add(new BasicNameValuePair("video_comment_id", commentId));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String cURL = Constant.delete_comment_video_url;
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

                                        Log.e("CommentVidId ", "myAdapter /// " + list.size());

                                        list.remove(position);
                                        myAdapter.notifyDataSetChanged();//notify adapter
                                        setListViewHeightBasedOnChildren(rvListComment);
                                        scrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
//
//                                        if (rvListComment != null)
//                                            Log.e("CommentVidId ","myAdapter // not null" + list.size());
//                                        else
//                                            Log.e("CommentVidId ","myAdapter // null" + list.size());

                                        myAdapter.notifyDataSetChanged();
                                        setListViewHeightBasedOnChildren(rvListComment);
                                        scrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });

                                        if (tvLoadMore.getVisibility() == View.GONE) {
                                            tvLoadMore.setVisibility(View.GONE);
                                            viewDividerAbove.setVisibility(View.GONE);
                                        } else {
                                            tvLoadMore.setVisibility(View.VISIBLE);
                                            viewDividerAbove.setVisibility(View.VISIBLE);
                                        }

                                    } else if (error == 2) {
                                        final AlertDialog.Builder myVoteHomeDialog = new AlertDialog.Builder(mActivity);
                                        myVoteHomeDialog.setTitle("VOTOCAST")
                                                .setMessage(posts)
                                                .setPositiveButton("Resend Email", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        new setResendEmail().execute();
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .create()
                                                .show();
                                    } else
                                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", mActivity);
                                } catch (Exception e) {
                                    Constant.ShowErrorMessage("Error", e.getMessage(), mActivity);
                                    Log.e("CommentVidId ", "Error //" + e.getMessage() + " - " + e.getLocalizedMessage());
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
