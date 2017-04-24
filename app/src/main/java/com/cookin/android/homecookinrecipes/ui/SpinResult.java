package com.cookin.android.homecookinrecipes.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cookin.android.homecookinrecipes.R;
import com.cookin.android.homecookinrecipes.data.Recipe;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.cookin.android.homecookinrecipes.R.id.publisher_tv;

public class SpinResult extends AppCompatActivity {

    private Recipe mRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_result);

        Intent intent = getIntent();
        mRecipe = intent.getExtras().getParcelable("recipe");

        String recipeTitle = mRecipe.getTitle();
        String recipePublisher = mRecipe.getPublisher();
        String imageUrl = mRecipe.getImage_url();
        float recipeRating = (float) mRecipe.getRating();

        AdView adView = (AdView) findViewById(R.id.adView_spin);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        TextView titleView = (TextView) findViewById(R.id.recipe_title);
        TextView publisherView = (TextView) findViewById(R.id.publisher_tv);
        ImageView backdrop = (ImageView) findViewById(R.id.imageView) ;
        RatingBar ratingBarView = (RatingBar) findViewById(R.id.ratingBar);

        Glide.with(this).load(imageUrl).fitCenter().into(backdrop);
        titleView.setText(recipeTitle);
        publisherView.setText(recipePublisher);
        ratingBarView.setRating(recipeRating);

    }

    public void viewRecipe(View view){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("key", mRecipe);
        startActivity(intent);
    }

    public void spinAgain(View view){
        Intent intent = new Intent(this, SpinnerActivity.class);
        startActivity(intent);
    }
}
