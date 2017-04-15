package com.example.android.homecookinrecipes.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.homecookinrecipes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH = "recipe";

    public static final class RecipeEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        public static final String TABLE_NAME = "recipe";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PUBLISHER = "publisher";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_SOURCE_URL = "source_url";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_SORT = "sort_by";
        public static final String COLUMN_ISFAV = "is_fav";

        public static Uri buildRecipeUri(String id){
            String s = CONTENT_URI.toString() +"/"+id;
            return Uri.parse(s);
        }
    }
}
