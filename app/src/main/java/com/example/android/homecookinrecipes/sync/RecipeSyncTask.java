package com.example.android.homecookinrecipes.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.homecookinrecipes.data.RecipeContract;
import com.example.android.homecookinrecipes.utility.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by administrator on 3/16/17.
 */

public class RecipeSyncTask {

    private static final int MAX_API_REQUEST = 10;

    synchronized public static void syncRecipe(Context context) {

        try {
            ContentResolver resolver = context.getContentResolver();
            resolver.delete(RecipeContract.RecipeEntry.CONTENT_URI, null, null);

            ArrayList<ContentValues> allRecipeList = new ArrayList<>();
            for (int i = 1; i <= MAX_API_REQUEST; i++) {
                URL url = Util.buildUrlWithPageNum(i, "r");
                String jsonResponse = Util.getJsonResponse(url);
                ContentValues[] values = Util.getContentValues(jsonResponse, "r");

                if (values != null && values.length != 0) {
                    allRecipeList.addAll(Arrays.asList(values));
                }
            }
            for (int j = 1; j <= MAX_API_REQUEST; j++) {
                URL url = Util.buildUrlWithPageNum(j, "t");
                String jsonResponse = Util.getJsonResponse(url);
                ContentValues[] values = Util.getContentValues(jsonResponse, "t");

                if (values != null && values.length != 0) {
                    allRecipeList.addAll(Arrays.asList(values));
                }
            }
            resolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI,
                    allRecipeList.toArray(new ContentValues[allRecipeList.size()]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
