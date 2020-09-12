package ca.dev.activcountwebapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class UpdatingWidget extends AppWidgetProvider {
    private PendingIntent service;

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent (context, UpdateService.class);

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 600, service);
    }

    @Override
    public void onReceive (Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetmanager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetmanager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted (Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled (Context context) {
        super.onEnabled (context);
    }

    @Override
    public void onDisabled (Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored (Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
