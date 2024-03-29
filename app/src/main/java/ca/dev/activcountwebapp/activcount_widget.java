/*
 * MIT License
 *
 * Copyright (c) 2020 Alexandre Comptabilite Specialise Ltee
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 *
 *  Date Created:        August 30, 2020
 *  Last time updated:   September 7, 2020
 *  Revision:
 *
 *  Author:              Alexandre Bobkov
 *  Company:             Alexandre Comptabilite Specialise Ltee.
 *
 *  Program description: application with launcher widget.
 *
 */

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

public class activcount_widget extends AppWidgetProvider {

    private WebView web_view;
    static private IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private PendingIntent service;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private String ACTION_DATE_ALARM = "WidgetDateAlarm";

    /** FONTS **/
    private static String path_font_comfortaa   = "fonts/comfortaa.ttf";
    private static String path_font_archistico  = "font/archistico_simple.ttf";
    private static String path_font_fff_tusj    = "fonts/fff_tusj.ttf";
    private static String path_font_jura_light  = "fonts/jura_light.ttf";


    public static Bitmap BuildUpdate (String text, String font_path, float size, Context context) {
        Paint paint = new Paint();
        paint.setTextSize(size);
        Typeface ourCustomTypeface = Typeface.createFromAsset(context.getAssets(), font_path);
        paint.setTypeface(ourCustomTypeface);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.5f);
        int height = (int) (baseline+paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Intent batteryStatus = context.registerReceiver(null, ifilter);

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

        SimpleDateFormat date_format = new SimpleDateFormat("E d MMM yyyy");
        SimpleDateFormat time_format = new SimpleDateFormat("HH : mm");

        views.setImageViewBitmap(R.id.img_time, BuildUpdate(time_format.format(new Date()), path_font_comfortaa, 70f, context));
        views.setImageViewBitmap(R.id.img_date, BuildUpdate(date_format.format(new Date()), path_font_comfortaa, 80f, context));
        views.setImageViewBitmap(R.id.img_business_name, BuildUpdate("Alexander Specialised Accounting Services", path_font_comfortaa,80f, context));
        views.setImageViewBitmap(R.id.appwidget_imgtxt_contact, BuildUpdate("+1 (343) 202 - 2043", path_font_comfortaa, 40f, context));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public void launch_web(Context c) {
        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        c.startActivity(mailClient);
    }

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent (context, UpdateService.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 0);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, service);
    }

    @Override
    public void onEnabled(Context context) {
        // Set an Alarm to change the date everyday at 00:00
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, activcount_widget.class);
        intent.setAction(ACTION_DATE_ALARM);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 0);

        // Fire the alarm everyday at 00:00
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

