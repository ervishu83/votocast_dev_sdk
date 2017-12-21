package customView.rangeseekbar;

/**
 * Created by Anil on 5/9/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import com.votocast.votocast.R;

public class VideoTimelineView extends View {

    private long videoLength = 0;
    private float progressLeft = 0;
    private float progressRight = 1;
    private Paint paint;
    private Paint paint2;
    private boolean pressedLeft = false;
    private boolean pressedRight = false;
    private float pressDx = 0;
    private MediaMetadataRetriever mediaMetadataRetriever = null;
    private VideoTimelineViewDelegate delegate = null;
    private ArrayList<Bitmap> frames = new ArrayList<>();
    private AsyncTask<Integer, Integer, Bitmap> currentTask = null;
    private static final Object sync = new Object();
    private long frameTimeOffset = 0;
    private int frameWidth = 0;
    private int frameHeight = 0;
    private int framesToLoad = 0;
    private Drawable pickDrawable = null;

    public interface VideoTimelineViewDelegate {
        void onLeftProgressChanged(float progress);

        void onRifhtProgressChanged(float progress);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(0xff66d1ee);
        paint2 = new Paint();
        paint2.setColor(0x7f000000);
        pickDrawable = getResources().getDrawable(R.drawable.videotrimmer);
    }

    public VideoTimelineView(Context context) {
        super(context);
        init(context);
    }

    public VideoTimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public float getLeftProgress() {
        return progressLeft;
    }

    public float getRightProgress() {
        return progressRight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();

        int width = getMeasuredWidth() - (32);
        int startX = (int) (width * progressLeft) + (16);
        int endX = (int) (width * progressRight) + (16);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int additionWidth = (12);
            if (startX - additionWidth <= x && x <= startX + additionWidth && y >= 0 && y <= getMeasuredHeight()) {
                pressedLeft = true;
                pressDx = (int) (x - startX);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            } else if (endX - additionWidth <= x && x <= endX + additionWidth && y >= 0 && y <= getMeasuredHeight()) {
                pressedRight = true;
                pressDx = (int) (x - endX);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (pressedLeft) {
                pressedLeft = false;
                return true;
            } else if (pressedRight) {
                pressedRight = false;
                return true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (pressedLeft) {
                startX = (int) (x - pressDx);
                if (startX < (16)) {
                    startX = (16);
                } else if (startX > endX) {
                    startX = endX;
                }
                progressLeft = (float) (startX - (16)) / (float) width;
                if (delegate != null) {
                    delegate.onLeftProgressChanged(progressLeft);
                }
                invalidate();
                return true;
            } else if (pressedRight) {
                endX = (int) (x - pressDx);
                if (endX < startX) {
                    endX = startX;
                } else if (endX > width + (16)) {
                    endX = width + (16);
                }
                progressRight = (float) (endX - (16)) / (float) width;
                if (delegate != null) {
                    delegate.onRifhtProgressChanged(progressRight);
                }
                invalidate();
                return true;
            }
        }
        return false;
    }

    public void setVideoPath(String path) {
        mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(path);
            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            videoLength = Long.parseLong(duration);
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }
    }

    public void setDelegate(VideoTimelineViewDelegate delegate) {
        this.delegate = delegate;
    }

    private void reloadFrames(int frameNum) {
        if (mediaMetadataRetriever == null) {
            return;
        }
        if (frameNum == 0) {
            frameHeight = (400);
            framesToLoad = (getMeasuredWidth() - (16)) / frameHeight;
            frameWidth = (int) Math.ceil((float) (getMeasuredWidth() - (16)) / (float) framesToLoad);
            frameTimeOffset = videoLength / framesToLoad;
        }
        currentTask = new AsyncTask<Integer, Integer, Bitmap>() {
            private int frameNum = 0;

            @Override
            protected Bitmap doInBackground(Integer... objects) {
                frameNum = objects[0];
                Bitmap bitmap = null;
                if (isCancelled()) {
                    return null;
                }
                try {
                    bitmap = mediaMetadataRetriever.getFrameAtTime(frameTimeOffset * frameNum * 1000);
                    if (isCancelled()) {
                        return null;
                    }
                    if (bitmap != null) {
                        Log.e("draw", "bit map not null" + frameHeight);
                        Bitmap result = Bitmap.createBitmap(frameWidth, frameHeight, bitmap.getConfig());
                        Canvas canvas = new Canvas(result);
                        float scaleX = (float) frameWidth / (float) bitmap.getWidth();
                        float scaleY = (float) frameHeight / (float) bitmap.getHeight();
                        float scale = scaleX > scaleY ? scaleX : scaleY;
                        int w = (int) (bitmap.getWidth() * scale);
                        int h = (int) (bitmap.getHeight() * scale);
                        Log.e("draw", "w-" +w + "- h -" + h);
                        Log.e("draw", "bitmap" + bitmap.getHeight());
                        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        Rect destRect = new Rect((frameWidth - w) / 2, (frameHeight - h) / 2, w, h);
                        canvas.drawBitmap(bitmap, srcRect, destRect, null);
                        bitmap.recycle();
                        bitmap = result;
                    }
                } catch (Exception e) {
                    Log.e("tmessages", e.toString());
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (!isCancelled()) {
                    frames.add(bitmap);
                    invalidate();
                    if (frameNum < framesToLoad) {
                        reloadFrames(frameNum + 1);
                    }
                }
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, frameNum, null, null);
        } else {
            currentTask.execute(frameNum, null, null);
        }
    }

    public void destroy() {
        synchronized (sync) {
            try {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                    mediaMetadataRetriever = null;
                }
            } catch (Exception e) {
                Log.e("tmessages", e.toString());
            }
        }
        for (Bitmap bitmap : frames) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        frames.clear();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
    }

    public void clearFrames() {
        for (Bitmap bitmap : frames) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        frames.clear();
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth() - (36);
        int startX = (int) (width * progressLeft) + (16);
        int endX = (int) (width * progressRight) + (16);

        canvas.save();
        canvas.clipRect((16), 0, width + (20), (44));
        if (frames.isEmpty() && currentTask == null) {
            reloadFrames(0);
            Log.i("draw", "if1");
        } else {
            Log.i("draw", "else");
            int offset = 0;
            for (Bitmap bitmap : frames) {
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, (20) + offset * frameWidth, (2), null);
                }
                offset++;
            }
        }

        canvas.drawRect((160), (2), startX, (42), paint2);
        canvas.drawRect(endX +(4),  (2),  (16) + width +  (4),  (42), paint2);

        canvas.drawRect(startX, 0, startX + (2), (44), paint);
        canvas.drawRect(endX + (2), 0, endX + (4), (44), paint);
        canvas.drawRect(startX + (2), 0, endX + (4), (2), paint);
        canvas.drawRect(startX + (2), (42), endX + (4), (44), paint);
        canvas.restore();


//old
//        canvas.drawRect((16), (2), startX, (42), paint2);
//        canvas.drawRect(endX +(4),  (2),  (16) + width +  (4),  (42), paint2);
//
//        canvas.drawRect(startX, 0, startX +  (2),  (44), paint);
//        canvas.drawRect(endX +  (2), 0, endX +  (4),  (44), paint);
//        canvas.drawRect(startX +  (2), 0, endX +  (4),  (2), paint);
//        canvas.drawRect(startX +  (2),  (42), endX +  (4),  (44), paint);
//        canvas.restore();

        int drawableWidth = pickDrawable.getIntrinsicWidth();
        int drawableHeight = pickDrawable.getIntrinsicHeight();

        pickDrawable.setBounds(startX - drawableWidth / 2, getMeasuredHeight() - drawableHeight, startX + drawableWidth / 2, getMeasuredHeight());
        pickDrawable.draw(canvas);

        pickDrawable.setBounds(endX - drawableWidth / 2 + (4), getMeasuredHeight() - drawableHeight, endX + drawableWidth / 2 + (4), getMeasuredHeight());
        pickDrawable.draw(canvas);
    }
}
