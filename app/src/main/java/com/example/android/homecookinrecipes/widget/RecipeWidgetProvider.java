package com.example.android.homecookinrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.homecookinrecipes.R;
import com.example.android.homecookinrecipes.ui.DetailActivity;

public class RecipeWidgetProvider extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        for(int appWidgetId : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
            Intent intent = new Intent(context, RecipeWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widget_list, intent);
            Intent onClickIntent = new Intent(context, DetailActivity.class);
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(onClickIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        if(intent.getAction().equals("com.example.android.homecookinrecipes.ACTION_DATA_CHANGED")){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgets = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgets, R.id.widget_list);
        }
    }
}
