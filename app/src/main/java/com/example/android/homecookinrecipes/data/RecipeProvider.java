package com.example.android.homecookinrecipes.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by administrator on 3/15/17.
 */

public class RecipeProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String sSelectionWithId = RecipeContract.RecipeEntry.TABLE_NAME+"."+
            RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " = ?";
    private static final String sFavSelection = RecipeContract.RecipeEntry.TABLE_NAME+"."+
            RecipeContract.RecipeEntry.COLUMN_ISFAV + "= ?";
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

        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case RECIPE: {
                cursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            case RECIPE_WITH_ID: {
                cursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        null,
                        sSelectionWithId,
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        null
                );
                break;
            }
            default: throw new UnsupportedOperationException("Unkown uri: "+ uri);
        }
        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values){
        int rowsInserted =0;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case RECIPE:
                db.beginTransaction();
                try{
                    for(ContentValues contentValue : values){
                        long _id = db.insertWithOnConflict(RecipeContract.RecipeEntry.TABLE_NAME, null, contentValue, SQLiteDatabase.CONFLICT_IGNORE);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
        }

        return rowsInserted;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int numOfRowsDeleted;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if(sUriMatcher.match(uri) == RECIPE_WITH_ID){
            String id = uri.getPathSegments().get(1);
            numOfRowsDeleted = db.delete(RecipeContract.RecipeEntry.TABLE_NAME, sSelectionWithId, new String[]{id});
        }else{
            numOfRowsDeleted = db.delete(RecipeContract.RecipeEntry.TABLE_NAME, sFavSelection, new String[]{"0"});
        }

        return numOfRowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implementing getType.");
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("Not implementing update.");
    }
}
