package com.example.android.homecookinrecipes.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by administrator on 3/1/17.
 */

public class Recipe implements Parcelable{

    private String title, publisher, image_url, source_url, recipeId;
    private double rating;

    public Recipe (String title, String publisher, String image_url, String source_url, String recipeId, double rating){
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

    public String getRecipeId(){
        return recipeId;
    }

    public double getRating(){
        return rating;
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        image_url = in.readString();
        source_url = in.readString();
        recipeId = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(publisher);
        parcel.writeString(image_url);
        parcel.writeString(source_url);
        parcel.writeString(recipeId);
        parcel.writeDouble(rating);
    }
}
