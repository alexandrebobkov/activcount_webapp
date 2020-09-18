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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
    private static Calendar time;


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
        //Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        time = Calendar.getInstance();
        int hr = time.get(time.DAY_OF_MONTH);
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
        // Date format NN MMM, YYYY
        //SimpleDateFormat date_format = new SimpleDateFormat("MMM d, yyyy");
        SimpleDateFormat date_format = new SimpleDateFormat("EE, MMMM");
        //SimpleDateFormat date_format = new SimpleDateFormat("d MMM. EEEE");
        // Week day format
        SimpleDateFormat day_format = new SimpleDateFormat("EEEE");
        //SimpleDateFormat day_format = new SimpleDateFormat("yyyy");

        views.setOnClickPendingIntent(R.id.widget_5x2_img_date, pendingUpdate);
        // Display date
        views.setImageViewBitmap(R.id.widget_5x2_img_date, BuildUpdate(date_format.format(new Date()), path_font_fff_tusj, 90f, context));
        // Display weekday
        //views.setImageViewBitmap(R.id.widget_5x2_img_weekday, BuildUpdate(day_format.format(new Date()), path_font_fff_tusj, 120f, context));
        views.setImageViewBitmap(R.id.widget_5x2_img_weekday, BuildUpdate(numToString(hr), path_font_fff_tusj, 120f, context));
        views.setImageViewResource(R.id.widget_5x2_logo, R.mipmap.ic_logo);
        views.setOnClickPendingIntent(R.id.widget_5x2_logo, pendingWeb);
        views.setImageViewBitmap(R.id.widget5x2_img_business_name, BuildUpdate("Alexander Specialised Accounting Services", path_font_comfortaa,80f, context));
        views.setImageViewBitmap(R.id.widget5x2_img_txt_contact, BuildUpdate("+1 (343) 202 - 2043     [RC]", path_font_comfortaa, 40f, context));

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
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private String numToString (int num) {
        String s = "";

        if (num == 0) s = "zero";
        else if (num == 1) s = "one";
        else if (num == 2) s = "two";
        else if (num == 3) s = "three";
        else if (num == 4) s = "four";
        else if (num == 5) s = "five";
        else if (num == 6) s = "six";
        else if (num == 7) s = "seven";
        else if (num == 8) s = "eight";
        else if (num == 9) s = "nine";
        else if (num == 10) s = "ten";
        else if (num == 11) s = "eleven";
        else if (num == 12) s = "twelve";
        else if (num == 13) s = "thirteen";
        else if (num == 14) s = "fourteen";
        else if (num == 15) s = "fifteen";
        else if (num == 16) s = "sixteen";
        else if (num == 17) s = "seventeen";
        else if (num == 18) s = "eighteen";
        else if (num == 19) s = "nineteen";
        else if (num == 20) s = "twenty";
        else if (num == 21) s = "twenty one";
        else if (num == 22) s = "twenty two";
        else if (num == 23) s = "twenty three";
        else if (num == 24) s = "twenty four";
        else if (num == 25) s = "twenty five";
        else if (num == 26) s = "twenty six";
        else if (num == 27) s = "twenty seven";
        else if (num == 28) s = "twenty eight";
        else if (num == 29) s = "twenty nine";
        else if (num == 30) s = "thirty";
        else if (num == 31) s = "thirty one";
        else if (num == 32) s = "thirty two";
        else if (num == 33) s = "thirty three";
        else if (num == 34) s = "thirty four";
        else if (num == 35) s = "thirty five";
        else if (num == 36) s = "thirty six";
        else if (num == 37) s = "thirty seven";
        else if (num == 38) s = "thirty eight";
        else if (num == 39) s = "thirty nine";
        else if (num == 40) s = "forty";
        else if (num == 41) s = "forty one";
        else if (num == 42) s = "forty two";
        else if (num == 43) s = "forty three";
        else if (num == 44) s = "forty four";
        else if (num == 45) s = "forty five";
        else if (num == 46) s = "forty six";
        else if (num == 47) s = "forty seven";
        else if (num == 48) s = "forty eight";
        else if (num == 49) s = "forty nine";
        else if (num == 50) s = "fifty";
        else if (num == 51) s = "fifty one";
        else if (num == 52) s = "fifty two";
        else if (num == 53) s = "fifty three";
        else if (num == 54) s = "fifty four";
        else if (num == 55) s = "fifty five";
        else if (num == 56) s = "fifty six";
        else if (num == 57) s = "fifty seven";
        else if (num == 58) s = "fifty eight";
        else if (num == 59) s = "fifty nine";
        else if (num == 60) s = "sixty";

        return s;
    }
}
