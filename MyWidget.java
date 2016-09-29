package com.niit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.niit.iweather.R;
import com.niit.iweather.Welcome;


public class MyWidget extends AppWidgetProvider {
	RemoteViews rv;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	private void initData(Context context) {

		rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		// 完成views中各个组件的监听
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				new Intent(context, Welcome.class), 0);

		rv.setOnClickPendingIntent(R.id.widget_linearlayout, intent);
	}
	@Override
	public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		updateMywidget(context, appWidgetManager);
	}

	private void updateMywidget(Context context, AppWidgetManager appWidgetManager) {
		initData(context);
		ComponentName componentName = new ComponentName(context, MyWidget.class);
		appWidgetManager.updateAppWidget(componentName, rv);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		updateMywidget(context, appWidgetManager);		

	}


}

