package fingertip.android.com.fingertip.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import fingertip.android.com.fingertip.LoginActivity;
import fingertip.android.com.fingertip.R;

/**
 * Created by nilnayal on 3/16/2017.
 */

public class QuoteWidgetProvider extends AppWidgetProvider {

    public static final String SYNC_CLICKED= "com.sam_chordas.android.stockhawk.SYNC_CLICKED";
    private static final String TAG = "Widget: ";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_collection);

        final Intent refreshIntent = new Intent(context, QuoteWidgetProvider.class);
        refreshIntent.setAction(QuoteWidgetProvider.SYNC_CLICKED);
        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0,
                refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.update, refreshPendingIntent);

        views.setEmptyView(R.id.widget_list, R.id.empty_view);

        final Intent openAppIntent = new Intent(context, LoginActivity.class);
        PendingIntent openAppPendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(openAppIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, openAppPendingIntent);

        // Set up the collection

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }



    @Override
    public void onReceive(Context context, Intent intent) {


        super.onReceive(context, intent);

        if (intent.getAction().equals(SYNC_CLICKED)) {

            //  Log.v(TAG, "Received");
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            final ComponentName componentName = new ComponentName(context, QuoteWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widget_list);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */



    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, QuoteWidgetRemoteViewsService.class));
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, QuoteWidgetRemoteViewsService.class));
    }

}
