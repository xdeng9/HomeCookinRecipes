package com.example.android.homecookinrecipes.data;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.homecookinrecipes.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by administrator on 3/5/17.
 */

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder> {

    private Recipe[] mRecipes;

    public RecipeRecyclerAdapter(Recipe[] recipes){
        mRecipes = recipes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView recipeImage;
        TextView recipeTitle;
        TextView recipePublisher;
        RatingBar recipeRating;

        public ViewHolder(View view){
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

        holder.recipeTitle.setText(mRecipes[position].getTitle());
        holder.recipePublisher.setText(mRecipes[position].getPublisher());
        float rating = (float) mRecipes[position].getRating();
        holder.recipeRating.setRating(rating/20f);
    }

    @Override
    public int getItemCount() {
        return mRecipes.length;
    }
}
