package com.example.android.homecookinrecipes.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.homecookinrecipes.DetailActivity;
import com.example.android.homecookinrecipes.R;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by administrator on 3/5/17.
 */

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public RecipeRecyclerAdapter(Context context) {
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeTitle;
        TextView recipePublisher;
        RatingBar recipeRating;

        public ViewHolder(View view) {
            super(view);
            recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
            recipeTitle = (TextView) view.findViewById(R.id.recipe_title);
            recipePublisher = (TextView) view.findViewById(R.id.recipe_publisher);
            recipeRating = (RatingBar) view.findViewById(R.id.recipe_rating);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.recipeTitle.setText(mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TITLE)));
        holder.recipePublisher.setText(mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_PUBLISHER)));
        float rating = (float) mCursor.getDouble(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RATING));
        holder.recipeRating.setRating(rating / 20f);

        Glide.with(mContext)
                .load(mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE_URL)))
                .fitCenter()
                .into(holder.recipeImage);

        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        else
            return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

}
