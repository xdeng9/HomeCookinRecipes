package com.example.android.homecookinrecipes.utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.homecookinrecipes.data.Recipe;
import com.example.android.homecookinrecipes.data.RecipeContract;
import com.example.android.homecookinrecipes.sync.ImmediateSyncService;
import com.example.android.homecookinrecipes.sync.RecipeJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

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
import java.util.concurrent.TimeUnit;

import static android.R.attr.id;
import static java.lang.System.in;

public class Util {

    static final String SORT_PARAM = "sort";
    static final String PAGE_PARAM = "page";
    static final String KEY_PARAM = "key";
    static final String API_KEY = "500bdd2ed4423c9e460ca447d1b194e2";
    static final String FOOD_URL = "http://food2fork.com/api/search";

    static final int SYNC_INTERVAL_HOURS = 48;
    static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);

    public static boolean sIntialized;

   public static void scheduleFirebaseJobDispatcherSycn(@NonNull final Context context){
       Driver driver = new GooglePlayDriver(context);
       FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

       Job syncRecipeJob = dispatcher.newJobBuilder()
               .setService(RecipeJobService.class)
               .setTag("recipe-sync")
               .setConstraints(Constraint.ON_ANY_NETWORK)
               .setLifetime(Lifetime.FOREVER)
               .setRecurring(true)
               .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS*2))
               .setReplaceCurrent(true)
               .build();

       dispatcher.schedule(syncRecipeJob);
   }

   public synchronized static void initialize(@NonNull final Context context){
       if (sIntialized) return;
       sIntialized = true;

       scheduleFirebaseJobDispatcherSycn(context);

       Thread checkForEmpty = new Thread(new Runnable() {
           @Override
           public void run() {

               Cursor cursor = context.getContentResolver().query(
                       RecipeContract.RecipeEntry.CONTENT_URI,
                       null,
                       null,
                       null,
                       null
               );

               if(cursor == null || cursor.getCount()==0)
                   startImmediateSync(context);

               cursor.close();
           }
       });

       checkForEmpty.start();
   }

    public static URL buildUrlWithPageNum(int page, String sortOrder){
        Uri uri = Uri.parse(FOOD_URL).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .appendQueryParameter(SORT_PARAM, sortOrder)
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

    public static ContentValues[] getContentValues(String jsonStr, String sortOrder) throws JSONException{

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
            row.put(RecipeContract.RecipeEntry.COLUMN_SORT, sortOrder);
            row.put(RecipeContract.RecipeEntry.COLUMN_ISFAV, isFav);

            recipeContentValues[i] = row;
        }

        return recipeContentValues;
    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intent = new Intent(context, ImmediateSyncService.class);
        context.startService(intent);
    }

    public static void updateFavRecipe(final Context context, String id, int isFav){
        final Uri uri = RecipeContract.RecipeEntry.buildRecipeUri(id);
        final ContentValues value = new ContentValues();
        value.put(RecipeContract.RecipeEntry.COLUMN_ISFAV, isFav);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver().update(
                        uri,
                        value,
                        null,
                        null
                );
            }
        });
        thread.start();
    }

    public static boolean isConnected(Context context){
        boolean connected;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
        connected = activeNetWork != null && activeNetWork.isConnectedOrConnecting();

        return connected;
    }

}
