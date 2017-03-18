package com.example.android.homecookinrecipes.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by administrator on 3/18/17.
 */

public class ImmediateSyncService extends IntentService {

    public ImmediateSyncService(){
        super("ImmediateSyncService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RecipeSyncTask.syncRecipe(this);
    }
}
