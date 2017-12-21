package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class VideoCropperIntentService extends IntentService {

    public static final String SOURCE_FILE_PATH_EXTRA = "SourceFilePathExtra";
    public static final String START_TIME_EXTRA = "StartTimeExtra";
    public static final String END_TIME_EXTRA = "EndTimeExtra";

    public static final String VIDEO_CROPPER_STATUS = "Status";
    public static final String VIDEO_CROPPER_RESPONSE = "Response";

    public static final int STATUS_FAILURE = 0;
    public static final int STATUS_SUCCESS = 1;

    LocalBroadcastManager mLocalBroadcastManager;

    /**
     * External Storage Directory - Video
     */
    File videoDirectory = new File(Environment.getExternalStorageDirectory()
            + File.separator
            + "VOTOCAST"
            + File.separator
            + "Media"
            + File.separator
            + "Video");

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public VideoCropperIntentService(){
        super(null);
    }

    public VideoCropperIntentService(String name) {
        super(name);
    }

    public void startTrim(String src, int startMs, int endMs) {

        // Creating directory on External Storage if not already created
        videoDirectory.mkdirs();

        File sourceFile = new File(src);

        FileDataSourceImpl file;
        try {
            file = new FileDataSourceImpl(new File(src));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sendResponse(STATUS_FAILURE, "File not found. Try Again.");
            return;
        }

        Movie movie;
        try {
            movie = MovieCreator.build(sourceFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            sendResponse(STATUS_FAILURE, "Failure on MovieCreator.Build(). Try Again.");
            return;
        }

        // remove all tracks we will create new tracks from the old
        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        double startTime = startMs / 1000;
        double endTime = endMs / 1000;
        boolean timeCorrected = false;
        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    // This exception here could be a false positive in case we have multiple tracks
                    // with sync samples at exactly the same positions. E.g. a single movie containing
                    // multiple qualities of the same video (Microsoft Smooth Streaming file)
                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                timeCorrected = true;
            }
        }
        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            long startSample = -1;
            long endSample = -1;

            for (int i = 0; i < track.getSampleDurations().length; i++) {
                if (currentTime <= startTime) {

                    // current sample is still before the new starttime
                    startSample = currentSample;
                }
                if (currentTime <= endTime) {
                    // current sample is after the new start time and still before the new endtime
                    endSample = currentSample;
                } else {
                    // current sample is after the end of the cropped video
                    break;
                }
                currentTime += (double) track.getSampleDurations()[i] / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
            movie.addTrack(new CroppedTrack(track, startSample, endSample));
        }

        Container out = new DefaultMp4Builder().build(movie);
        MovieHeaderBox mvhd = Path.getPath(out, "moov/mvhd");
        mvhd.setMatrix(Matrix.ROTATE_180);

        File destinationFile = new File(videoDirectory, "VID_TRIMMED_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".mp4");
        try {
            destinationFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            sendResponse(STATUS_FAILURE, "Failure on createNewFile(). Its not possible to create the file.");
            return;
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(destinationFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sendResponse(STATUS_FAILURE, "Failure on FileOutputStream(). File Not Found Exception");
            return;
        }

        WritableByteChannel fc = fos.getChannel();
        try {
            out.writeContainer(fc);
            fc.close();
            fos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            sendResponse(STATUS_FAILURE, "Failure. Try again.");
        }

        sendResponse(STATUS_SUCCESS, destinationFile.getAbsolutePath());
    }

    private double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        if (intent != null) {
            String sourceFilePath = intent.getStringExtra(SOURCE_FILE_PATH_EXTRA);
            int startTime = intent.getIntExtra(START_TIME_EXTRA, -1);
            int endTime = intent.getIntExtra(END_TIME_EXTRA, -1);

            startTrim(sourceFilePath, startTime, endTime);
        }
    }

    /**
     * To send response to activity with broadcast.
     *
     * @param status   : success status. 0 means Failure and 1 means Success
     * @param response : reponse string. It may contain file path if success or reason if failure
     */
    private void sendResponse(int status, String response) {
        Intent intent = new Intent();
        intent.setAction("VideoCropperActionResponse");
        intent.putExtra(VIDEO_CROPPER_STATUS, status);
        intent.putExtra(VIDEO_CROPPER_RESPONSE, response);
        mLocalBroadcastManager.sendBroadcast(intent);
    }
}