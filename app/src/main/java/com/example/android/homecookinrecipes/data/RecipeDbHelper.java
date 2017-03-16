package com.example.android.homecookinrecipes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by administrator on 3/14/17.
 */

public class RecipeDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "recipe.db";

    public RecipeDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE_TABLE =
                "CREATE TABLE "+ RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                        RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeEntry.COLUMN_TITLE + " TEXT NOT NUll, " +
                        RecipeContract.RecipeEntry.COLUMN_PUBLISHER + " TEXT NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_SOURCE_URL + " TEXT NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        RecipeContract.RecipeEntry.COLUMN_ISFAV + " INTEGER NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ RecipeContract.RecipeEntry.TABLE_NAME);
    }
}
