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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AppLauncherWidget_1x1 extends AppWidgetProvider {

	static void updateAppWidget (Context context, AppWidgetManager appWidgetManager,
								 int appWidgetId) {

		CharSequence widgetText = context.getString(R.string.appwidget_text);
		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_launcher_widget_1x1);
		//views.setTextViewText(R.id.appwidget_logo, widgetText);

		views.setImageViewResource(R.id.appwidget_1x1_logo, R.mipmap.ic_logo);
		Intent intentWeb = new Intent (context, MainActivity.class);
		PendingIntent pendingWeb = PendingIntent.getActivity(context,appWidgetId,intentWeb,PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.appwidget_1x1_logo, pendingWeb);

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onEnabled (Context context) {
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled (Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}

	public void launch_web(Context c) {
		Intent mailClient = new Intent(Intent.ACTION_VIEW);
		c.startActivity(mailClient);
	}
}

