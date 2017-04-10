package com.example.android.homecookinrecipes.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.homecookinrecipes.R;
import com.example.android.homecookinrecipes.data.Recipe;
import com.example.android.homecookinrecipes.data.RecipeContract;

public class RecipeWidgetRemoteViewsService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data;
            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if(data != null){
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(
                        RecipeContract.RecipeEntry.CONTENT_URI,
                        null,
                        RecipeContract.RecipeEntry.COLUMN_RATING + "=?",
                        new String[]{"100"},
                        "RANDOM() LIMIT 5"
                );
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if(data != null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data==null? 0: data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if(i == AdapterView.INVALID_POSITION ||
                        data == null ||
                        !data.moveToPosition(i)){
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                String id = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
                final String title = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TITLE));
                final String publisher = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_PUBLISHER));
                final String imageUrl = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE_URL));
                final String sourceUrl = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SOURCE_URL));
                final double recipeRating = data.getDouble(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RATING));;
                final int fav = data.getInt(data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ISFAV));

                views.setTextViewText(R.id.widget_item_title, title);
                views.setTextViewText(R.id.widget_item_publisher, publisher);
                Recipe recipe = new Recipe(id, title, publisher, imageUrl, sourceUrl, recipeRating, fav);
                final Intent intent = new Intent();
                intent.putExtra("key",recipe);
                views.setOnClickFillInIntent(R.id.recipe_widget_list_item, intent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
