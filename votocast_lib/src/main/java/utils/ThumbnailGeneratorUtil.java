package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class ThumbnailGeneratorUtil extends AsyncTask<ThumbnailConfig, Bitmap, Void> {

    int frameNumber = 1;
    private Context mContext;
    private MediaMetadataRetriever mMediaMetadataRetriever;
    private ProgressListener mProgressListener;

    public ThumbnailGeneratorUtil(Context context) {
        mContext = context;
        mProgressListener = (ProgressListener) context;
        mMediaMetadataRetriever = new MediaMetadataRetriever();
    }

    public ThumbnailGeneratorUtil(Context context, ProgressListener mProgressListener) {
        mContext = context;
        this.mProgressListener = mProgressListener;
        mMediaMetadataRetriever = new MediaMetadataRetriever();
        Log.i("ThumbnailGeneratorUtil", "ThumbnailGeneratorUtil");
    }

    @Override
    protected Void doInBackground(ThumbnailConfig... params) {

        if (params != null) {
            int duration = getDuration(params[0].getUri());
            mProgressListener.onStart(duration);
            while (frameNumber < duration) {
                final Bitmap bitmap;
                if (params[0] != null) {
                    bitmap = Bitmap.createScaledBitmap(
//                            mMediaMetadataRetriever.getFrameAtTime(frameNumber * 3000000),
                            mMediaMetadataRetriever.getFrameAtTime(frameNumber * 1000000),
                            params[0].getWidth(),
                            params[0].getHeight(),
                            false);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Thread.currentThread().stop();
                        e.printStackTrace();
                    }
                    publishProgress(bitmap);
                    frameNumber++;
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Bitmap... bitmap) {
        super.onProgressUpdate(bitmap);
        mProgressListener.onProgress(bitmap[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressListener.onFinish();
    }

    public int getDuration(Uri uri) {
        mMediaMetadataRetriever.setDataSource(mContext, uri);
        String time = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);
        long duration = timeInMilliSec / 1000;

        Log.i("duration",duration + "");

        if(duration < 60) {

            duration = 8;
            Log.i("duration < 60 ",duration + "");
        }
        else if (duration < 70 && duration > 60) {
            duration = duration / 7;
        }
        else if(duration > 70 && duration < 100)
            duration = duration / 10;

        else if(duration > 100 && duration < 140)
            duration = duration / 14;
        else if(duration > 140 && duration < 180)
            duration = duration / 17;

        else if(duration > 180 && duration < 220)
            duration = duration / 22;
        else if(duration > 220 && duration < 270)
            duration = duration / 27;

        else if(duration > 270 && duration < 350)
            duration = duration / 35;
        else if(duration > 350 && duration < 450)
            duration = duration / 45;
        else if(duration > 450 && duration < 550)
            duration = duration / 55;
        else if(duration > 550 && duration < 650)
            duration = duration / 65;
        else if(duration > 650 && duration < 750)
            duration = duration / 75;
        else if(duration > 750 && duration < 850)
            duration = duration / 85;
        else if(duration > 850 && duration < 950)
            duration = duration / 95;
        else if(duration > 950 && duration < 1050)
            duration = duration / 105;
        else
            duration = duration / 200;

        Log.i("duration thumb generate", duration + "-" + timeInMilliSec);
        return (int) duration;
    }

    public interface ProgressListener {
        void onStart(int duration);

        void onFinish();

        void onProgress(Bitmap bitmap);
    }
}
