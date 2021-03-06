package ca.dev.activcountwebapp;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import java.util.Random;


public class UpdateWidgetService extends Service {
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.activcount_widget_5x2);
        //ComponentName theWidget = new ComponentName(this, UpdatingWidget.class);
        ComponentName theWidget = new ComponentName(this, Widget_5x2_calendar.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);

        return super.onStartCommand(intent, flags, startId);
    }
}
