package com.example.android.homecookinrecipes.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.homecookinrecipes.ui.DetailActivity;
import com.example.android.homecookinrecipes.R;

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

        String id = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
        final String title = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TITLE));
        final String publisher = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_PUBLISHER));
        final String imageUrl = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE_URL));
        final String sourceUrl = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SOURCE_URL));
        final double recipeRating = mCursor.getDouble(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RATING));
        final int fav = mCursor.getInt(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ISFAV));

        holder.recipeTitle.setText(title);
        holder.recipePublisher.setText(publisher);
        float rating = (float) recipeRating;
        holder.recipeRating.setRating(rating / 20f);

        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .crossFade()
                .into(holder.recipeImage);

        final Recipe recipe = new Recipe(id, title, publisher, imageUrl, sourceUrl, recipeRating, fav);

        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("key", recipe);
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
