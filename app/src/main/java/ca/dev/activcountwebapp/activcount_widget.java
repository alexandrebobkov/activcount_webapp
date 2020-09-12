package ca.dev.activcountwebapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.RemoteViews;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class activcount_widget extends AppWidgetProvider {

    static private IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private PendingIntent service;


    public static Bitmap BuildUpdate (String txt_time, float size, Context context) {
        Paint paint = new Paint();
        paint.setTextSize(size);
        Typeface ourCustomTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Comfortaa-Regular.ttf");
        paint.setTypeface(ourCustomTypeface);
        //paint.setColor(Color.parseColor("#3A9F44"));
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(txt_time) + 0.5f);
        int height = (int) (baseline+paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawText(txt_time, 0, baseline, paint);
        return image;
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent batteryStatus = context.registerReceiver(null, ifilter);

        CharSequence widgetText_001 = context.getString(R.string.business_name_en);
        CharSequence widgetText_002 = context.getString(R.string.contact_phone);
        CharSequence brandname = context.getString(R.string.my_brand);

        //ImageView image = (ImageView) findViewById(R.id.logo);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activcount_widget);
        //views.setTextViewText(R.id.appwidget_text_001, widgetText_001);
        views.setTextViewText(R.id.appwidget_text_002, widgetText_002);
        views.setTextViewText(R.id.brand_name, brandname);
        views.setImageViewResource(R.id.logo, R.mipmap.ic_logo);

        SimpleDateFormat date_format = new SimpleDateFormat("d MMM, yyyy");
        SimpleDateFormat time_format = new SimpleDateFormat("H : mm");

        //views.setImageViewBitmap(R.id.img_time, BuildUpdate("20:25", 100f, context));
        views.setImageViewBitmap(R.id.img_time, BuildUpdate(time_format.format(new Date()), 115f, context));
        views.setImageViewBitmap(R.id.img_date, BuildUpdate(date_format.format(new Date()), 80f, context));
        views.setImageViewBitmap(R.id.img_business_name, BuildUpdate("Alexander Specialised Accounting Services", 80f, context));

        //views.setImageViewBitmap(R.id.imageView);
        //ImageView logo_view = (ImageView)R.id.imageView;

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /*@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }*/
    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent (context, UpdateService.class);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 30000, service);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

