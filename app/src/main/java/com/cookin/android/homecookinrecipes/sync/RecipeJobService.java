package com.cookin.android.homecookinrecipes.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by administrator on 3/16/17.
 */

public class RecipeJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchRecipeTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mFetchRecipeTask = new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                RecipeSyncTask.syncRecipe(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                jobFinished(job, false);
            }
        };

        mFetchRecipeTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchRecipeTask != null){
            mFetchRecipeTask.cancel(true);
        }
        return false;
    }
}
