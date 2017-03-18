package com.example.android.homecookinrecipes.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.homecookinrecipes.data.RecipeContract;
import com.example.android.homecookinrecipes.utility.Util;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by administrator on 3/16/17.
 */

public class RecipeSyncTask {

    private static final int MAX_API_REQUEST = 4;
    private static final String TAG = RecipeSyncTask.class.getSimpleName();

    synchronized public static void syncRecipe(Context context){

        try{

            ContentResolver resolver = context.getContentResolver();
            resolver.delete(RecipeContract.RecipeEntry.CONTENT_URI, null, null);

            for(int i=1; i<= MAX_API_REQUEST; i++){
                URL url = Util.buildUrlWithPageNum(i);
                Log.d(TAG, url.toString());
                String jsonResponse = Util.getJsonResponse(url);
                Log.d(TAG, jsonResponse);
                ContentValues[] values = Util.getContentValues(jsonResponse);

                if(values !=null && values.length != 0){
                    resolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, values);
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
