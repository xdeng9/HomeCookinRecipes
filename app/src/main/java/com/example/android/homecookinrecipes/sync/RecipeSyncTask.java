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

    private static final int MAX_API_REQUEST = 5;
    private static final String TAG = RecipeSyncTask.class.getSimpleName();
    private static boolean loaded;

    synchronized public static void syncRecipe(Context context){

        try{
            loaded = false;
            ContentResolver resolver = context.getContentResolver();
            resolver.delete(RecipeContract.RecipeEntry.CONTENT_URI, null, null);

            int i, j;
            for(i=1; i<= MAX_API_REQUEST; i++){
                URL url = Util.buildUrlWithPageNum(i, "r");
                String jsonResponse = Util.getJsonResponse(url);
                ContentValues[] values = Util.getContentValues(jsonResponse, "r");

                if(values !=null && values.length != 0){
                    resolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, values);
                }
            }
            for(j=1; j<= MAX_API_REQUEST; j++){
                URL url = Util.buildUrlWithPageNum(j, "t");
                Log.d(TAG, url.toString());
                String jsonResponse = Util.getJsonResponse(url);
                Log.d(TAG, jsonResponse);
                ContentValues[] values = Util.getContentValues(jsonResponse, "t");

                if(values !=null && values.length != 0){
                    resolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI, values);
                }
            }
            if((i+j) >= MAX_API_REQUEST*2){
                loaded = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isLoaded(){
        return loaded;
    }
}
