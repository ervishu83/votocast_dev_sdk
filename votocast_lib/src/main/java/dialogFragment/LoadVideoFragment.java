package dialogFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ProgressHUD;
import db.UploadVideo;

import com.votocast.votocast.VC_MainActivity;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadVideoFragment extends Fragment {

    Cursor cursor;
    ArrayList<UploadVideo> thumbs = new ArrayList<UploadVideo>();
    GridView gridviewUploadVideo;
    String filePath = "";
    MediaMetadataRetriever mMediaMetadataRetriever;
    TextView uploadVideoTitle;
    VideoPlayerView videoviewLoadVideoPlayer;
    ScrollView scrollViewLoadVideos;
    ImageView ivLoadVidPlay;
    int isComplete;
    private int previousSelectedPosition = -1, previousSelectedImageId = -1;

    // ---------------- new code--------
//    ArrayList<String> pathArr;

    private VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });


    public LoadVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View v = inflater.inflate(R.layout.fragment_load_video, container, false);
        ((VC_MainActivity) getActivity()).getSupportActionBar().hide();
        ((VC_MainActivity) getActivity()).getTabLayout().setVisibility(View.GONE);

        Tracker t = ((MyAppTracker)getActivity().getApplication()).getTracker(MyAppTracker.TrackerName.APP_TRACKER);
        t.setScreenName("Add Videos");
        t.send(new HitBuilders.AppViewBuilder().build());

        thumbs = new ArrayList<UploadVideo>();

        scrollViewLoadVideos = (ScrollView) v.findViewById(R.id.scrollViewLoadVideos);
        ImageView dialogCancelBtn = (ImageView) v.findViewById(R.id.dialogCancelBtn);
        ImageView dialogNextBtn = (ImageView) v.findViewById(R.id.dialogNextBtn);
        ivLoadVidPlay = (ImageView) v.findViewById(R.id.ivLoadVidPlay);

//        videoviewUploadVideo = (VideoView) dialog.findViewById(R.id.videoviewUploadVideo);
        gridviewUploadVideo = (GridView) v.findViewById(R.id.gridviewUploadVideo);

        uploadVideoTitle = (TextView) v.findViewById(R.id.uploadVideoTitle);
        Constant.setDisplayFontsSemibold(getActivity(), uploadVideoTitle);

        videoviewLoadVideoPlayer = (VideoPlayerView) v.findViewById(R.id.videoviewLoadVideoPlayer);
        videoviewLoadVideoPlayer.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
            @Override
            public void onVideoPreparedMainThread() {
                // We hide the cover when video is prepared. Playback is about to start
//                mVideoCover.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoStoppedMainThread() {
                // We show the cover when video is stopped
//                mVideoCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                // We show the cover when video is completed
//                mVideoCover.setVisibility(View.VISIBLE);
            }
        });

        dialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelUpload();
            }
        });

        mMediaMetadataRetriever = new MediaMetadataRetriever();
        dialogNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoPlayerManager.resetMediaPlayer();
                TrimVideoFragment fragment = new TrimVideoFragment();
                Bundle args = new Bundle();
                args.putString("filePath", filePath);
                fragment.setArguments(args);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame_container, fragment);
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();
            }
        });

//        new getVideoList().execute();
        new getCampaignList().execute();

        gridviewUploadVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.i("FileSelect1", thumbs.get(position).getFilename());
                filePath = thumbs.get(position).getFilename();
                mVideoPlayerManager.playNewVideo(null, videoviewLoadVideoPlayer, thumbs.get(position).getFilename());

                ImageView iv = (ImageView) view.findViewById(thumbs.get(position).getImageId());
                iv.setPadding(6, 6, 6, 6);
                iv.setBackgroundResource(R.drawable.image_border);

                LinearLayout llGrid = (LinearLayout) gridviewUploadVideo.getChildAt(previousSelectedPosition);
                LinearLayout llGridFirst = (LinearLayout) gridviewUploadVideo.getChildAt(0);
                if (previousSelectedPosition != -1) {

                    ImageView previousSelectedView = (ImageView) llGrid.findViewById(previousSelectedImageId);
                    previousSelectedView.setSelected(false);
                    previousSelectedView.setPadding(0, 0, 0, 0);
                }
                else
                {
                    RelativeLayout rlGrid = (RelativeLayout) llGridFirst.getChildAt(0);
                    FrameLayout flGrid = (FrameLayout) rlGrid.getChildAt(0);
                    ImageView fisrtGridItem = (ImageView) flGrid.getChildAt(0);
                    Log.i("Gridview",flGrid.getChildCount() + "="+ flGrid.getChildAt(0)+"");
                    Log.i("Gridview",fisrtGridItem.toString()+"");
                    fisrtGridItem.setPadding(0, 0, 0, 0);
                }
                // Set the current selected view position as previousSelectedPosition
                previousSelectedPosition = position;
                previousSelectedImageId = thumbs.get(position).getImageId();

                scrollViewLoadVideos.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollViewLoadVideos.fullScroll(View.FOCUS_UP);
                    }
                });
            }
        });

        videoviewLoadVideoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoviewLoadVideoPlayer.getVideoUrlDataSource() != null) {
                    String state = videoviewLoadVideoPlayer.getCurrentState().toString();
//                    if (isPlay == 0) {
                    if (state.equals("STARTED")) {
                        ivLoadVidPlay.setVisibility(View.VISIBLE);
                        videoviewLoadVideoPlayer.pause();
                    }
//                    } else {
                    if (state.equals("PAUSED")) {
                        ivLoadVidPlay.setVisibility(View.GONE);
                        videoviewLoadVideoPlayer.start();
                    }
//                    }
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
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        ((VC_MainActivity) getActivity()).getTabLayout().setVisibility(View.VISIBLE);
                        TabLayout.Tab tab;

                        if (Constant.getShareData(getActivity(), "camp_id").equals("")) {
                            tab = ((VC_MainActivity) getActivity()).getTabLayout().getTabAt(0);
                            tab.select();
                        } else {
                            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                            if (fragmentManager.getBackStackEntryCount() > 0) {
                                fragmentManager.popBackStack();
                            }
                        }
                        mVideoPlayerManager.resetMediaPlayer();
                        return true;
                    }
                }
                return false;
            }
        });

        return v;
    }

    private void cancelUpload(){
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((VC_MainActivity) getActivity()).getTabLayout().setVisibility(View.VISIBLE);
        TabLayout.Tab tab;

        if (Constant.getShareData(getActivity(), "camp_id").equals("")) {
            tab = ((VC_MainActivity) getActivity()).getTabLayout().getTabAt(0);
            tab.select();
        } else {
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }
        mVideoPlayerManager.resetMediaPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int getDeviceWidth() {
        int deviceWidth = 0;

        Point size = new Point();
        WindowManager windowManager = getActivity().getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(size);
            deviceWidth = size.x;
        } else {
            Display display = windowManager.getDefaultDisplay();
            deviceWidth = display.getWidth();
        }
        return deviceWidth;
    }

    class getVideoList extends AsyncTask<Void, Void, Void> {
        ProgressHUD mProgressHUD;

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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final int deviceWidth = getDeviceWidth();

                        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DURATION};
                        final String selection = MediaStore.Images.Media.DATA + " like ? ";
                        final String[] selectionArgs = new String[]{"%/DCIM/Camera%"};

                        cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
// only 4 camera // cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
//                    cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Video.Thumbnails._ID);
//                    pathArr = new ArrayList<String>();

                        if (null != cursor) {
                            int count = 0;
                            if (cursor != null) {
                                isComplete = cursor.getCount();
                                while (cursor.moveToNext()) {
                                    count = count + 1;
//                                Log.i("CursorVideo",cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                                    thumbs.add(new UploadVideo(new Integer(count), cursor.getString(0), cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))));
//                                pathArr.add(cursor.getString(0));
                                }
                                cursor.close();
                            }
                        }

                        if(thumbs.size() > 0) {
                            VideoAdapter adapter = new VideoAdapter(getActivity(), deviceWidth, thumbs);
                            gridviewUploadVideo.setAdapter(adapter);
                            setGridViewHeightBasedOnChildren(gridviewUploadVideo, 4);
                        }else{
                            //show alert saying you do not have any content
                            MyUtils.showToast(((FragmentActivity) getActivity()), "You do not have any videos in your gallery!");
                            cancelUpload();
                        }

                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                    }
                });
            }
            super.onPostExecute(aVoid);
        }
    }

    public class VideoAdapter extends BaseAdapter {
        private Context mContext;
        private int actionbarHeight;
        ArrayList<UploadVideo> thumbs;

        public VideoAdapter(Context c, int actionbarHeight, ArrayList<UploadVideo> thumbs) {
            mContext = c;
            this.actionbarHeight = actionbarHeight;
            this.thumbs = thumbs;
        }

        public int getCount() {
            return thumbs.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder = new Holder();
            final View rowView;

            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_upload_video, parent, false);
            holder.videoDuration = (TextView) rowView.findViewById(R.id.videoDuration);
            holder.uploadImageview = (ImageView) rowView.findViewById(R.id.uploadImageview);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(actionbarHeight / 4, actionbarHeight / 4);

            holder.uploadImageview.setId(thumbs.get(position).getImageId());
            holder.uploadImageview.setLayoutParams(lp);
            holder.uploadImageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.uploadImageview.setAdjustViewBounds(true);
            holder.uploadImageview.setPadding(0, 0, 0, 0);

            if (thumbs.get(position).getDuration() != null) {
                if (!thumbs.get(position).getDuration().contains(":"))
                    holder.videoDuration.setText(String.format("%02d:%02d", getMinute(Long.parseLong(thumbs.get(position).getDuration())), getSeconds(Long.parseLong(thumbs.get(position).getDuration()))));
                else {
                    holder.videoDuration.setText(thumbs.get(position).getDuration());
                }
            }

//            filePath = thumbs.get(0).getFilename();
            Log.i("FileSelect2", filePath + "-" + position);
            if (filePath.equals("")) {
                mVideoPlayerManager.playNewVideo(null, videoviewLoadVideoPlayer, thumbs.get(0).getFilename());
                if (position == 0) {
                    holder.uploadImageview.setPadding(6, 6, 6, 6);
                    holder.uploadImageview.setBackgroundResource(R.drawable.image_border);
                    holder.uploadImageview.setSelected(true);
                    rowView.setSelected(true);
                }
                filePath = thumbs.get(0).getFilename();
            } else {
                mVideoPlayerManager.playNewVideo(null, videoviewLoadVideoPlayer, filePath);
//                Log.i("Gridview", position + "-" + convertView.toString() );
//                ImageView iv = (ImageView) rowView.findViewById(thumbs.get(0).getImageId());
//                iv.setPadding(0,0,0,0);
//                Log.i("Gridview", iv.toString());
            }

//            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(thumbs.get(position).getFilename(), MediaStore.Video.Thumbnails.MINI_KIND);
            Glide.with(mContext)
                    .load(thumbs.get(position).getFilename())
                    .placeholder(R.drawable.backimage)
                    .centerCrop()
                    .override(50, 50)
                    .into(holder.uploadImageview);
            return rowView;
        }

        public class Holder {
            TextView videoDuration;
            ImageView uploadImageview;
        }
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    public long getMinute(long seconds) {
        return TimeUnit.MILLISECONDS.toMinutes(seconds) - (TimeUnit.MILLISECONDS.toHours(seconds) * 60);
    }

    public long getSeconds(long seconds) {
        return TimeUnit.MILLISECONDS.toSeconds(seconds) - (TimeUnit.MILLISECONDS.toMinutes(seconds) * 60);
    }

    class getCampaignList extends AsyncTask<Void, Void, Void> {
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

            String cURL = Constant.get_campaings_list_to_upload_url;
            Constant.postData(cURL, pair, new Handler(new Handler.Callback() {
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
                                        JSONObject jo = new JSONObject(JsonString);
                                        String posts = jo.getString("message");
                                        int error = jo.getInt("error");
                                        if (error == 0) {
                                            new getVideoList().execute();
                                        }else if(error == 1) {
                                            AlertDialog.Builder myalrt = new AlertDialog.Builder(getActivity());
                                            myalrt.setTitle("Alert");
                                            myalrt.setMessage(posts);
                                            myalrt.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                                    ((VC_MainActivity) getActivity()).getTabLayout().setVisibility(View.VISIBLE);
                                                    TabLayout.Tab tab;

                                                    if (Constant.getShareData(getActivity(), "camp_id").equals("")) {
                                                        tab = ((VC_MainActivity) getActivity()).getTabLayout().getTabAt(0);
                                                        tab.select();
                                                    } else {
                                                        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                                                        if (fragmentManager.getBackStackEntryCount() > 0) {
                                                            fragmentManager.popBackStack();
                                                        }
                                                    }
                                                    mVideoPlayerManager.resetMediaPlayer();
                                                }
                                            });
                                            myalrt.create().show();

                                        }else
                                            Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                                    } catch (Exception e) {
                                    }
                                    if (mProgressHUD.isShowing() && mProgressHUD != null)
                                        mProgressHUD.dismiss();
                                }
                            });
                        }
                    } else {
                        if (mProgressHUD.isShowing() && mProgressHUD != null)
                            mProgressHUD.dismiss();
                        Constant.ShowErrorMessage("Error", "Sorry, an error has occurred. Please try again later.", getActivity());
                    }
                    if (mProgressHUD.isShowing() && mProgressHUD != null)
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
        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
    }
}
