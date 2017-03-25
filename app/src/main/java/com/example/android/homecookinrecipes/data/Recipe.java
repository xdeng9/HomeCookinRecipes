package com.example.android.homecookinrecipes.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable{

    private String recipeId, title, publisher, image_url, source_url;
    private double rating;
    private int fav;

    public Recipe (String recipeId, String title, String publisher,
                   String image_url, String source_url, double rating, int fav){
        this.recipeId = recipeId;
        this.title = title;
        this.publisher = publisher;
        this.image_url = image_url;
        this.source_url = source_url;
        this.rating = rating;
        this.fav = fav;
    }

    public String getRecipeId(){
        return recipeId;
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

    public double getRating(){
        return rating;
    }

    public int getFav() {
        return fav;
    }

    protected Recipe(Parcel in) {
        recipeId = in.readString();
        title = in.readString();
        publisher = in.readString();
        image_url = in.readString();
        source_url = in.readString();
        rating = in.readDouble();
        fav = in.readInt();
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
        parcel.writeString(recipeId);
        parcel.writeString(title);
        parcel.writeString(publisher);
        parcel.writeString(image_url);
        parcel.writeString(source_url);
        parcel.writeDouble(rating);
        parcel.writeInt(fav);
    }
}
