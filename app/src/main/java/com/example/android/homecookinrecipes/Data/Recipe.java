package com.example.android.homecookinrecipes.Data;

/**
 * Created by administrator on 3/1/17.
 */

public class Recipe {

    private String title, publisher, image_url, source_url;
    private int recipeId;
    private double rating;

    public Recipe(){}

    public Recipe (String title, String publisher, String image_url, String source_url, int recipeId, double rating){
        this.title = title;
        this.publisher = publisher;
        this.image_url = image_url;
        this.source_url = source_url;
        this.recipeId = recipeId;
        this.rating = rating;
    }

    public String getTitle(){
        return title;
    }

    public  String getPublisher(){
        return publisher;
    }

    public String getImage_url(){
        return image_url;
    }

    public String getSource_url(){
        return source_url;
    }

    public int getRecipeId(){
        return recipeId;
    }

    public double getRating(){
        return rating;
    }
}
