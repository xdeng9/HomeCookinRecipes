package com.example.android.homecookinrecipes.utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.homecookinrecipes.data.Recipe;
import com.example.android.homecookinrecipes.data.RecipeContract;
import com.example.android.homecookinrecipes.sync.ImmediateSyncService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.System.in;

/**
 * Created by administrator on 3/7/17.
 */

public class Util {

    static final String SORT_PARAM = "sort";
    static final String PAGE_PARAM = "page";
    static final String KEY_PARAM = "key";
    static final String API_KEY = "500bdd2ed4423c9e460ca447d1b194e2";
    static final String FOOD_URL = "http://food2fork.com/api/search";

    public static Recipe[] addRecipeArrays(Recipe[] old, Recipe[] fresh){

        if(old == null)
            return fresh;

        Recipe[] all = new Recipe[old.length+fresh.length];

        int i;
        for(i=0; i< old.length; i++){
            all[i] = old[i];
        }

        for(int j=0; j< fresh.length; j++){
            all[i] = fresh[j];
            i++;
        }
        return all;
    }

    public static URL buildUrlWithPageNum(int page){
        Uri uri = Uri.parse(FOOD_URL).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .appendQueryParameter(SORT_PARAM, "r")
                .appendQueryParameter(PAGE_PARAM, ""+page).build();

        try{
            return new URL(uri.toString());
        } catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public static String getJsonResponse(URL url) throws IOException{
        String response = null;
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line;
            while((line = reader.readLine()) !=null){
                buffer.append(line + "\n");
            }
            response = buffer.toString();
        }finally {
            connection.disconnect();
        }

        return response;
    }

    public static ContentValues[] getContentValues(String jsonStr) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("recipes");

        ContentValues[] recipeContentValues = new ContentValues[jsonArray.length()];

        for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonRecipe = jsonArray.getJSONObject(i);
            String title = jsonRecipe.getString("title");
            String publisher = jsonRecipe.getString("publisher");
            String image_url = jsonRecipe.getString("image_url");
            String source_url = jsonRecipe.getString("source_url");
            String recipeId = jsonRecipe.getString("recipe_id");
            double rating = jsonRecipe.getDouble("social_rank");
            int isFav = 0; // 0 = not favorite, 1= favorite

            ContentValues row = new ContentValues();
            row.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, recipeId);
            row.put(RecipeContract.RecipeEntry.COLUMN_TITLE, title);
            row.put(RecipeContract.RecipeEntry.COLUMN_PUBLISHER, publisher);
            row.put(RecipeContract.RecipeEntry.COLUMN_IMAGE_URL, image_url);
            row.put(RecipeContract.RecipeEntry.COLUMN_SOURCE_URL, source_url);
            row.put(RecipeContract.RecipeEntry.COLUMN_RATING, rating);
            row.put(RecipeContract.RecipeEntry.COLUMN_ISFAV, isFav);

            recipeContentValues[i] = row;
        }

        return recipeContentValues;
    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intent = new Intent(context, ImmediateSyncService.class);
        context.startService(intent);
    }
}
