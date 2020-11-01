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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Widget_4x2_Date extends AppWidgetProvider {

    private PendingIntent service;

    /** FONTS **/
    private static String path_font_archistico  = "assets/fonts/Archistico_Simple.ttf";
    /*
    private static String path_font_comfortaa   = "font/comfortaa.ttf";
    private static String path_font_fff_tusj    = "font/fff_tusj.ttf";

    private static String path_font_jura_light  = "assets/fonts/jura.ttf";
     */
    private static Calendar time, calendar;
    private int build;
    private static String contact_phone         = "(343) 202 - 2043 | books@activcount.ca";
    private static String biz_name              = "Alexander Specialised Accounting Services";


    /*public static Bitmap BuildUpdate (String text, String font_path, float size, Context context) {

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
        //Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }*/

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
        SimpleDateFormat date_format = new SimpleDateFormat("EEEE, MMM d, yyyy");
        // Other formats: "MMMM" "d MMM. EEEE" "yyyy"
        // Week day format
        SimpleDateFormat day_format = new SimpleDateFormat("EEEE");



        // Display full date
        views.setTextViewText(R.id.widget_4x2_body_weekday, date_format.format(new Date()));

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

    /*public void launch_web(Context c) {
        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        c.startActivity(mailClient);
    }*/

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

    // Spells out digit
    private String dateToString (int num) {
        String s = "";

        if (num == 0) s = "zero";
        else if (num == 1) s = "First";
        else if (num == 2) s = "Second";
        else if (num == 3) s = "Third";
        else if (num == 4) s = "Fourth";
        else if (num == 5) s = "Fifth";
        else if (num == 6) s = "Sixth";
        else if (num == 7) s = "Seventh";
        else if (num == 8) s = "Eighth";
        else if (num == 9) s = "Ninth";
        else if (num == 10) s = "Tenth";
        else if (num == 11) s = "Eleventh";
        else if (num == 12) s = "Twelveth";
        else if (num == 13) s = "Thirteenth";
        else if (num == 14) s = "Fourteenth";
        else if (num == 15) s = "Fifteenth";
        else if (num == 16) s = "Sixteenth";
        else if (num == 17) s = "Seventeenth";
        else if (num == 18) s = "Eighteenth";
        else if (num == 19) s = "Nineteenth";
        else if (num == 20) s = "Twentiest";
        else if (num == 21) s = "Twenty First";
        else if (num == 22) s = "Twenty Second";
        else if (num == 23) s = "Twenty Third";
        else if (num == 24) s = "Twenty Fourth";
        else if (num == 25) s = "Twenty Fifth";
        else if (num == 26) s = "Twenty Sixth";
        else if (num == 27) s = "Twenty Seventh";
        else if (num == 28) s = "Twenty Eighth";
        else if (num == 29) s = "Twenty Nineth";
        else if (num == 30) s = "Thirtiest";
        else if (num == 31) s = "Thirty First";

        return s;
    }
}
