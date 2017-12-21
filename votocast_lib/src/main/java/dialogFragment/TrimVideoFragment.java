package dialogFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import class_adapter.Constant;
import class_adapter.MyUtils;
import class_adapter.ThumbnailRecyclerAdapter;
import customView.rangeseekbar.RangeSeekBar;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.votocast.votocast.MyAppTracker;
import com.votocast.votocast.R;
import service.VideoCropperIntentService;
import utils.ThumbnailConfig;
import utils.ThumbnailGeneratorUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrimVideoFragment extends Fragment implements ThumbnailGeneratorUtil.ProgressListener {

    public static final String PARAM_FILE = "filePath";

    private ThumbnailRecyclerAdapter mThumbnailRecyclerAdapter;
    private ThumbnailGeneratorUtil mThumbnailGeneratorUtil;
    private String filePath;
    private String trimFilePath;
    VideoView videoviewTrimVideo;
    RecyclerView mRecyclerView;
    RangeSeekBar mRangeSeekBar;
    int minLength = 0, maxLength = 100;
    LinearLayout llTrimVideoPopup;
    int llWidth;
    List<Bitmap> bitmapList;
    TextView tvTrimVideoDuration;
    int min = 1, max = 31;
    long mainDuration;
    int cropMin, cropMax;
    TextView tvTrimVideoTitle;
    TextView tvTrimTimeDiff;
    ImageView trimdialogCancelBtn,trimdialogNextBtn,trimdialogCropBtn;

    public TrimVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trim_video, container, false);

        Bundle bundle = getArguments();
        filePath = bundle.getString(PARAM_FILE, "");
//        Log.i("trim path", filePath);

//        Log.i("trim screen", "reload");
        // setting Home and Menu Button icons

        tvTrimVideoTitle = (TextView) v.findViewById(R.id.tvTrimVideoTitle);
        Constant.setDisplayFontsSemibold(getActivity(), tvTrimVideoTitle);

        llTrimVideoPopup = (LinearLayout) v.findViewById(R.id.llTrimVideoPopup);
        tvTrimVideoDuration = (TextView) v.findViewById(R.id.tvTrimVideoDuration);
        tvTrimTimeDiff = (TextView) v.findViewById(R.id.tvTrimTimeDiff);

        trimdialogCancelBtn = (ImageView) v.findViewById(R.id.trimdialogCancelBtn);
        trimdialogNextBtn = (ImageView) v.findViewById(R.id.trimdialogNextBtn);
        trimdialogCropBtn = (ImageView) v.findViewById(R.id.trimdialogCropBtn);

        videoviewTrimVideo = (VideoView) v.findViewById(R.id.videoviewTrimVideo);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvVideoCropper);
        mRangeSeekBar = (RangeSeekBar) v.findViewById(R.id.rsbVideoCropper);

        mRangeSeekBar.setTextAboveThumbsColorResource(R.color.colorAccent);

        MediaMetadataRetriever mMediaMetadataRetriever = new MediaMetadataRetriever();
        mMediaMetadataRetriever.setDataSource(getActivity(), Uri.parse(filePath));
        String time = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);
        mainDuration = TimeUnit.MILLISECONDS.toSeconds(timeInMilliSec);

        tvTrimVideoDuration.setText(((1) / 60) + ":" + ((1) % 60) + " - " + (mainDuration / 60) + ":" + (mainDuration % 60));
        mRangeSeekBar.setRangeValues(0, mainDuration);

        videoviewTrimVideo.setVideoURI(Uri.parse(filePath));
        videoviewTrimVideo.start();
        videoviewTrimVideo.setBackgroundResource(0);

        Drawable background = llTrimVideoPopup.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(Constant.colorPrimary));
        }

        return v;
    }

    public long getSeconds(long seconds){
        return TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!filePath.equals("")) {
//            Log.i("onResume","onResume");
//            ThumbnailConfig thumbnailConfig = new ThumbnailConfig(Uri.fromFile(new File(filePath)), (llWidth/30)+50,200);  // -- with dynamic width
//            ThumbnailConfig thumbnailConfig = new ThumbnailConfig(Uri.fromFile(new File(filePath)), 50, 80);  -- new one without dynamic width
            ThumbnailConfig thumbnailConfig = new ThumbnailConfig(Uri.fromFile(new File(filePath)), 100, 100);  // -- main
            mThumbnailGeneratorUtil = new ThumbnailGeneratorUtil(getActivity(), this);
            mThumbnailGeneratorUtil.execute(thumbnailConfig);
        }

        trimdialogCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoviewTrimVideo.isPlaying()) {
                    videoviewTrimVideo.stopPlayback();
                    videoviewTrimVideo.pause();
                    videoviewTrimVideo.clearFocus();
                }
                FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }

            }
        });

        trimdialogCropBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
            }
        });

        mRangeSeekBar.setSelectedMinValue(0.5);
        mRangeSeekBar.setSelectedMaxValue(mainDuration);
        mRangeSeekBar.setMaxLabel(mainDuration);

        if(mainDuration <= 30){
            cropMin = 0;
            cropMax = (int)mainDuration;
            mRangeSeekBar.setSelectedMinValue(0.5);
            mRangeSeekBar.setSelectedMaxValue(mainDuration);
        }else{
            cropMin = 0;
            cropMax = 30;
            mRangeSeekBar.setSelectedMaxValue(30);
        }
        mRangeSeekBar.setNotifyWhileDragging(true);
        tvTrimTimeDiff.setText(String.format("%02d secs",getSeconds(cropMax - cropMin)));

        trimdialogNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoviewTrimVideo.isPlaying()) {
                    videoviewTrimVideo.stopPlayback();
                    videoviewTrimVideo.pause();
                    videoviewTrimVideo.clearFocus();
                }

                Log.i("seekbar next", cropMax+ "-" + cropMin + "-" + (cropMax - cropMin));

                if (cropMax - cropMin <= 30 && cropMax - cropMin != 0) {
                    Intent msgIntent = new Intent(getActivity(), VideoCropperIntentService.class);
                    msgIntent.putExtra(VideoCropperIntentService.SOURCE_FILE_PATH_EXTRA, filePath);
                    msgIntent.putExtra(VideoCropperIntentService.START_TIME_EXTRA, cropMin * 1000);
                    msgIntent.putExtra(VideoCropperIntentService.END_TIME_EXTRA, cropMax * 1000);
                    getActivity().startService(msgIntent);
                } else
                    MyUtils.showToast(getActivity(), "Crop video less or equal to 30 seconds!");
            }
        });

        mRangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
//                Log.i("seekbar main", minValue+ "-" + maxValue);

                maxLength = (int) maxValue;
                minLength = (int) minValue;

                tvTrimVideoDuration.setText(((minLength) / 60) + ":" + ((minLength) % 60) + " - " + (maxLength / 60) + ":" + (maxLength % 60));
                cropMin = minLength;
                cropMax = maxLength;
                if (min != minLength && minLength <= mainDuration - 30) {
//                    Log.i("seekbar 3", minLength+ "-" + min + "-" + maxLength);
                    if (maxLength - minLength > 30) {
                        mRangeSeekBar.setSelectedMaxValue(minLength + 30);
                        max = maxLength;
                        min = minLength;
                        mRangeSeekBar.setSelectedMinValue(min);
                        tvTrimVideoDuration.setText((min / 60) + ":" + (min % 60) + " - " + ((minLength + 30) / 60) + ":" + ((minLength + 30) % 60));

                        cropMin = min;
                        cropMax = (minLength + 30);
                    } else {
                        min = minLength;
                        max = maxLength;
                        mRangeSeekBar.setSelectedMinValue(min);
                        tvTrimVideoDuration.setText((min / 60) + ":" + (min % 60) + " - " + ((max) / 60) + ":" + ((max) % 60));

                        cropMin = min;
                        cropMax = max;
                    }
                }
                if (max != maxLength && maxLength > 31) {
//                    Log.i("seekbar 4", maxLength+ "-" + min);
                    if (maxLength - min > 30) {
                        mRangeSeekBar.setSelectedMinValue(maxLength - 30);
                        min = maxLength - 30;
                        max = maxLength;
                        mRangeSeekBar.setSelectedMaxValue(max);
                        tvTrimVideoDuration.setText(((maxLength - 30) / 60) + ":" + ((maxLength - 30) % 60) + " - " + (max / 60) + ":" + (max % 60));
                        cropMin = (maxLength - 30);
                        cropMax = max;
                    } else {

                        max = maxLength;
                        mRangeSeekBar.setSelectedMaxValue(max);
                        tvTrimVideoDuration.setText(((minLength) / 60) + ":" + ((minLength) % 60) + " - " + (max / 60) + ":" + (max % 60));
                        cropMin = minLength;
                        cropMax = max;
                    }

//                    Log.i("seekbar 4", maxLength+ "-" + min);
                }

//                Log.i("seekbar crop", cropMin + "-" + cropMax);
                tvTrimTimeDiff.setText(String.format("%02d secs",getSeconds(cropMax - cropMin)));

                videoviewTrimVideo.setVideoPath(filePath);
                videoviewTrimVideo.start();
                videoviewTrimVideo.seekTo(cropMin * 1000);
                videoviewTrimVideo.setBackgroundResource(0);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new
                LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false)
        );

        bitmapList = new ArrayList<>();
        llTrimVideoPopup.post(new Runnable() {
            @Override
            public void run() {
                llWidth = llTrimVideoPopup.getWidth();
//                Log.i("TEST1", "Layout width : " + llTrimVideoPopup.getWidth());

                MediaMetadataRetriever mMediaMetadataRetriever = new MediaMetadataRetriever();
                mMediaMetadataRetriever.setDataSource(getActivity(), Uri.parse(filePath));
                String time = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilliSec = Long.parseLong(time);
                long mainDuration = TimeUnit.MILLISECONDS.toSeconds(timeInMilliSec);

                mThumbnailRecyclerAdapter = new ThumbnailRecyclerAdapter(bitmapList, llWidth, mainDuration);
                mRecyclerView.setAdapter(mThumbnailRecyclerAdapter);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition() + 1;
                int last = layoutManager.findLastVisibleItemPosition() - 4;

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int status = intent.getIntExtra(VideoCropperIntentService.VIDEO_CROPPER_STATUS, 0);
                String response = intent.getStringExtra(VideoCropperIntentService.VIDEO_CROPPER_RESPONSE);

                Log.i("broadcast", status + "-" + response);
                trimFilePath = response;

                if (!trimFilePath.equals("") && getActivity() != null) {

                    DetailVideoFragment fragment = new DetailVideoFragment();
                    Bundle args = new Bundle();
                    args.putString("filePath", trimFilePath);
                    fragment.setArguments(args);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frame_container, fragment);
                    ft.addToBackStack(fragment.getClass().getName());
                    ft.commit();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("VideoCropperActionResponse");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onStart(int duration) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onProgress(Bitmap bitmap) {
        mThumbnailRecyclerAdapter.addBitmap(bitmap);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mThumbnailGeneratorUtil != null)
            mThumbnailGeneratorUtil.cancel(true);
    }

}
