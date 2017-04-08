package com.example.android.homecookinrecipes.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.homecookinrecipes.R;
import com.example.android.homecookinrecipes.data.Recipe;
import com.example.android.homecookinrecipes.data.RecipeContract;

public class SpinnerActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mSelection, mSortOrder;
    private String[] mSelectionArgs;
    private Recipe mRecipe;
    private boolean mInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        mSelection = null;
        mSelectionArgs = null;
        mSortOrder = "RANDOM() LIMIT 1";
        getLoaderManager().initLoader(1, null, this);
    }

    public void getRecipe(View view){
        int id = view.getId();
        mSelection = RecipeContract.RecipeEntry.COLUMN_TITLE + " LIKE ?";
        switch (id){
            case R.id.btn_appetizer:
                mSelectionArgs = new String[]{"%salad%"};
                break;
            case R.id.btn_beef:
                mSelectionArgs = new String[]{"%beef%"};
                break;
            case R.id.btn_bread:
                mSelectionArgs = new String[]{"%bread%"};
                break;
            case R.id.btn_cake:
                mSelectionArgs = new String[]{"%cake%"};
                break;
            case R.id.btn_chicken:
                mSelectionArgs = new String[]{"%chicken%"};
                break;
            case R.id.btn_desserts:
                mSelectionArgs = new String[]{"%cookie%"};
                break;
            case R.id.btn_fish:
                mSelectionArgs = new String[]{"%shrimp%"};
                break;
            case R.id.btn_pasta:
                mSelectionArgs = new String[]{"%pasta%"};
                break;
            case R.id.btn_pizza:
                mSelectionArgs = new String[]{"%pizza%"};
                break;
            case R.id.btn_pork:
                mSelectionArgs = new String[]{"%pork%"};
                break;
            case R.id.btn_soup:
                mSelectionArgs = new String[]{"%soup%"};
                break;
            case R.id.btn_vege:
                mSelectionArgs = new String[]{"%vegetarian%"};
                break;
            case R.id.btn_any:
                displayResult();
                return;
        }
      getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                RecipeContract.RecipeEntry.CONTENT_URI,
                null,
                mSelection,
                mSelectionArgs,
                mSortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null && cursor.getCount() != 0){
            cursor.moveToFirst();
        } else {
            Log.d("Recipe=","returned coz "+cursor.getCount());
            return;
        }
        String id = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
        String title = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TITLE));
        String publisher = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_PUBLISHER));
        String imageUrl = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE_URL));
        String sourceUrl = cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SOURCE_URL));
        double recipeRating = cursor.getDouble(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RATING));;
        int fav = cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ISFAV));
        mRecipe = new Recipe(id,title,publisher,imageUrl,sourceUrl,recipeRating,fav);

        Log.d("Recipe=", mRecipe.getTitle());
        if(mInit){
            displayResult();
        }else{
            mInit = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void displayResult(){
        Intent intent = new Intent(this,SpinResult.class);
        intent.putExtra("recipe", mRecipe);
        startActivity(intent);
    }
}
