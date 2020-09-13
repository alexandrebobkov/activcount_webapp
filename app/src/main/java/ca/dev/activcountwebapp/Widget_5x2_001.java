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

public class Widget_5x2_001 extends AppWidgetProvider {

    private PendingIntent service;

    /** FONTS **/
    private static String path_font_comfortaa   = "fonts/Comfortaa-Regular.ttf";
    private static String path_font_archistico  = "fonts/Archistico_Simple.ttf";
    private static String path_font_fff_tusj    = "fonts/FFF_Tusj.ttf";
    private static String path_font_jura_light  = "fonts/Jura-Light.ttf";


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

        // Setup update button to send an update request as a pending intent.
        Intent intentUpdate = new Intent (context, Widget_5x2_001.class);
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

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activcount_widget_5x2);
        SimpleDateFormat date_format = new SimpleDateFormat("d MMM, yyyy");

        views.setOnClickPendingIntent(R.id.widget_5x2_img_date, pendingUpdate);
        views.setImageViewBitmap(R.id.widget_5x2_img_date, BuildUpdate(date_format.format(new Date()), path_font_fff_tusj, 80f, context));
        views.setImageViewResource(R.id.widget_5x2_logo, R.mipmap.ic_logo);
        views.setOnClickPendingIntent(R.id.widget_5x2_logo, pendingWeb);
        views.setImageViewBitmap(R.id.widget5x2_img_business_name, BuildUpdate("Alexander Specialised Accounting Services", path_font_comfortaa,80f, context));
        views.setImageViewBitmap(R.id.widget5x2_img_txt_contact, BuildUpdate("+1 (343) 202 - 2043", path_font_comfortaa, 40f, context));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public void launch_web(Context c) {
        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        //mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
        c.startActivity(mailClient);
    }

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

    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
