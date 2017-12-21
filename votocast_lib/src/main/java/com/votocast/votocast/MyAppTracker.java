package com.votocast.votocast;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

/**
 * Created by Anil on 7/4/2016.
 */
public class MyAppTracker extends Application {

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-76146367-2";

    public static int GENERAL_TRACKER = 0;
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.

    }

    public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public MyAppTracker() {
        super();
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(PROPERTY_ID);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}