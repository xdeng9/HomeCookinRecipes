package com.example.android.homecookinrecipes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by administrator on 3/15/17.
 */

public class RecipeProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int RECIPE = 1;
    private static final int RECIPE_WITH_ID = 2;
    private RecipeDbHelper mDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = RecipeContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, RecipeContract.PATH, RECIPE);
        uriMatcher.addURI(authority, RecipeContract.PATH + "/#", RECIPE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implementing getType.");
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
