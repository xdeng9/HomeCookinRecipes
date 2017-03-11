package com.example.android.homecookinrecipes.utility;

import com.example.android.homecookinrecipes.data.Recipe;

/**
 * Created by administrator on 3/7/17.
 */

public class Util {

    public static Recipe[] addRecipeArrays(Recipe[] old, Recipe[] fresh){

        if(old == null)
            return fresh;

        Recipe[] all = new Recipe[old.length+fresh.length];

        int i;
        for(i=0; i< old.length; i++){
            all[i] = old[i];
        }

        for(int j=0; j< fresh.length; j++){
            all[i] = fresh[j];
            i++;
        }
        return all;
    }
}
