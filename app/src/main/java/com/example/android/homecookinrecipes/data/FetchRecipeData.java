package com.example.android.homecookinrecipes.data;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joseph on 2/28/2017.
 */

public class FetchRecipeData extends AsyncTask<String, Void, Recipe[]>{
    public static final String TAG = FetchRecipeData.class.getSimpleName();

    public interface AsyncResponse {
        void processResult(Recipe[] result);
    }

    public AsyncResponse delegate = null;

    public FetchRecipeData(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Recipe[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse = null;

        final String SORT_PARAM = "sort";
        final String KEY_PARAM = "key";
        final String API_KEY = "500bdd2ed4423c9e460ca447d1b194e2";
        final String FOOD_URL = "http://food2fork.com/api/search";

        try{
            Uri uri = Uri.parse(FOOD_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .build();

            Log.d(TAG, uri.toString());

            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!= null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
                return null;

            jsonResponse = buffer.toString();
        }catch (IOException e){
            Log.e(TAG, "Error occured");
            return null;
        } finally {
            if (urlConnection !=null)
                urlConnection.disconnect();
            if (reader != null){
                try {
                    reader.close();
                } catch (final IOException e){
                    Log.e(TAG, "Error closing stream: ", e);
                }
            }
        }

        try {
            return getRecipeFromJson(jsonResponse);
        } catch (JSONException e){
            Log.e(TAG, "JSON Exception: ", e);
            e.printStackTrace();
        }

        return null;
    }

    private Recipe[] getRecipeFromJson(String jsonStr) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("recipes");

        Recipe[] recipes = new Recipe[jsonArray.length()];

        for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonRecipe = jsonArray.getJSONObject(i);
            String title = jsonRecipe.getString("title");
            String publisher = jsonRecipe.getString("publisher");
            String image_url = jsonRecipe.getString("image_url");
            String source_url = jsonRecipe.getString("source_url");
            String recipeId = jsonRecipe.getString("recipe_id");
            double rating = jsonRecipe.getDouble("social_rank");

            recipes[i] = new Recipe(title, publisher, image_url,
                    source_url, recipeId, rating);
        }
        return recipes;
    }

    @Override
    protected void onPostExecute(Recipe[] result){

        delegate.processResult(result);
    }
}
