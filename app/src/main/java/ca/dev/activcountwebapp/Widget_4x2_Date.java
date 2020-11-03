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
*  Last time updated:   November 1, 2020
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
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Widget_4x2_Date extends AppWidgetProvider {

    private PendingIntent service;

    /** FONTS **/
    private static String path_font_archistico  = "assets/fonts/archistico_simple.ttf";
    /*
    private static String path_font_comfortaa   = "font/comfortaa.ttf";
    private static String path_font_fff_tusj    = "font/fff_tusj.ttf";
    private static String path_font_jura_light  = "assets/fonts/jura.ttf";
    */
    private static Calendar time, calendar;
    private int build;
    private static String contact_phone         = "(343) 202 - 2043 | books@activcount.ca";
    private static String biz_name              = "Alexander Specialised Accounting Services";


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        MainActivity.setWebPageUrl("https://mobile.activcount.ca");

        time = Calendar.getInstance();
        calendar = Calendar.getInstance();
        build = PackageManager.VERSION_CODE_HIGHEST;

        int hr = time.get(time.DAY_OF_MONTH);
        // Setup update button to send an update request as a pending intent.
        Intent intentUpdate = new Intent (context, Widget_4x2_Date.class);
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
        Intent contactsWeb = new Intent (context, ContactInfoWebPage.class);

        // Wrap it all in a pending intent to send a broadcast.
        // Use the app widget ID as the request code (third argument) so that
        // each intent is unique.
        PendingIntent pendingWeb = PendingIntent.getActivity(context,appWidgetId,intentWeb,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingContactsWebPage = PendingIntent.getActivity(context,appWidgetId,contactsWeb,PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activcount_widget_4x2);
        // Date format NN MMM, YYYY
        SimpleDateFormat date_format = new SimpleDateFormat("EEEE");
        // Other formats: "MMMM" "d MMM. EEEE" "yyyy"
        // Week day format
        SimpleDateFormat day_format = new SimpleDateFormat("MMMM d");

        // Display week day
        views.setTextViewText(R.id.widget_4x2_body_weekday, date_format.format(new Date()));
        // Display date
        views.setTextViewText(R.id.widget_4x2_body_day, day_format.format(new Date()));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.jura);
        // Display Contact information
        views.setTextViewText(R.id.widget_4x2_footer_contact, contact_phone);
        // Display company name
        views.setTextViewText(R.id.widget_4x2_footer_bizname, biz_name);

        // Display logo
        views.setImageViewResource(R.id.widget_4x2_logo, R.mipmap.ic_logo);
        // Launch website inside WebvVew if user clicked on a logo
        views.setOnClickPendingIntent(R.id.widget_4x2_logo, pendingWeb);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent (context, UpdateWidgetService.class);

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
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
