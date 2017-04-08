package com.example.android.homecookinrecipes;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class RecipeAnalytics extends Application{
    private Tracker mTracker;

    public void startTracking(){
        if(mTracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.track_app);
        }
    }

    public Tracker getTracker(){
        startTracking();

        return mTracker;
    }
}
