package com.example.android.homecookinrecipes.Data;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Joseph on 2/28/2017.
 */

public class FetchRecipeData extends AsyncTask<String, Void, Recipe[]>{
    public static final String TAG = FetchRecipeData.class.getSimpleName();

    private Context mContext;

    public FetchRecipeData(Context context){
        mContext = context;
    }

    @Override
    protected Recipe[] doInBackground(String... params) {
        String sort=params[0];
        final String SORT_PARAM = "sort";
        final String API_KEY = "500bdd2ed4423c9e460ca447d1b194e2";
        final String FOOD_URL = "http://food2fork.com/api/search?key="+API_KEY+"&"+sort;
        return new Recipe[0];
    }
}
