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
		views.setTextViewText(R.id.appwidget_text, widgetText);

		views.setImageViewResource(R.id.appwidget_text, R.mipmap.ic_logo);
		Intent intentWeb = new Intent (context, MainActivity.class);
		PendingIntent pendingWeb = PendingIntent.getActivity(context,appWidgetId,intentWeb,PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_5x2_logo, pendingWeb);

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

