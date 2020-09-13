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
import android.webkit.WebView;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class activcount_widget extends AppWidgetProvider {

    private WebView web_view;
    static private IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private PendingIntent service;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private String ACTION_DATE_ALARM = "WidgetDateAlarm";

    /** FONTS **/
    private static String path_font_comfortaa  = "fonts/Comfortaa-Regular.ttf";
    private static String path_font_archistico = "fonts/Archistico_Simple.ttf";
    private static String path_font_fff_tusj = "fonts/FFF_Tusj.ttf";
    private static String path_font_jura_light = "fonts/Jura-Light.ttf";


    public static Bitmap BuildUpdate (String text, String font_path, float size, Context context) {
        Paint paint = new Paint();
        paint.setTextSize(size);
        Typeface ourCustomTypeface = Typeface.createFromAsset(context.getAssets(), font_path);// "fonts/Comfortaa-Regular.ttf");
        paint.setTypeface(ourCustomTypeface);
        //paint.setColor(Color.parseColor("#3A9F44"));
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.5f);
        int height = (int) (baseline+paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Setup update button to send an update request as a pending intent.
        Intent intentUpdate = new Intent (context, activcount_widget.class);
        // The intent action must be an app widget update.
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Include the widget ID to be updated as an intent extra.
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        // Wrap it all in a pending intent to send a broadcast.
        // Use the app widget ID as the request code (third argument) so that
        // each intent is unique.
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup logo icon to launch webview as a pending intent.
        Intent intentWeb = new Intent (context, MainActivity.class);
        // Wrap it all in a pending intent to send a broadcast.
        // Use the app widget ID as the request code (third argument) so that
        // each intent is unique.
        PendingIntent pendingWeb = PendingIntent.getActivity(context,appWidgetId,intentWeb,PendingIntent.FLAG_UPDATE_CURRENT);

        CharSequence widgetText_001 = context.getString(R.string.business_name_en);
        CharSequence widgetText_002 = context.getString(R.string.contact_phone);
        CharSequence brandname = context.getString(R.string.my_brand);

        //ImageView image = (ImageView) findViewById(R.id.logo);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activcount_widget);
        //views.setTextViewText(R.id.appwidget_text_001, widgetText_001);
        views.setTextViewText(R.id.appwidget_text_contact, widgetText_002);
        views.setTextViewText(R.id.brand_name, brandname);
        views.setImageViewResource(R.id.logo, R.mipmap.ic_logo);
        // Assign the pending intent to the button onClick handler
        views.setOnClickPendingIntent(R.id.btn_refresh, pendingUpdate);
        //views.setOnClickPendingIntent(R.id.logo, pendingUpdate);
        views.setOnClickPendingIntent(R.id.logo, pendingWeb);
        views.setOnClickPendingIntent(R.id.img_time, pendingUpdate);
        views.setOnClickPendingIntent(R.id.img_date, pendingUpdate);

        SimpleDateFormat date_format = new SimpleDateFormat("d MMM, yyyy");
        SimpleDateFormat time_format = new SimpleDateFormat("HH : mm");

        //views.setImageViewBitmap(R.id.img_time, BuildUpdate("20:25", 100f, context));
        views.setImageViewBitmap(R.id.img_time, BuildUpdate(time_format.format(new Date()), path_font_fff_tusj, 70f, context));
        views.setImageViewBitmap(R.id.img_date, BuildUpdate(date_format.format(new Date()), path_font_fff_tusj, 80f, context));
        views.setImageViewBitmap(R.id.img_business_name, BuildUpdate("Alexander Specialised Accounting Services", path_font_comfortaa,80f, context));
        views.setImageViewBitmap(R.id.appwidget_imgtxt_contact, BuildUpdate("+1 (343) 202 - 2043", path_font_comfortaa, 40f, context));

        //views.setImageViewBitmap(R.id.imageView);
        //ImageView logo_view = (ImageView)R.id.imageView;

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public void launch_web(Context c) {
        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        //mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
        c.startActivity(mailClient);
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        //manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 15000, service);
        manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, service);
        //manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+60000, 60000, service);
    }

    /*@Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName (context, activcount_widget.class);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews( context.getPackageName(), R.layout.activcount_widget);
            Intent intent = new Intent (context, activcount_widget.class);
            intent.setAction (AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }*/

    /*@Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }*/

    @Override
    public void onEnabled(Context context) {
        // Set an Alarm to change the date everyday at 00:00
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, activcount_widget.class);
        intent.setAction(ACTION_DATE_ALARM);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        // Fire the alarm everyday at 00:00
        //alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /*@Override
    public IBinder onBind (Intent intent) {
        return null;
    }*/
}

