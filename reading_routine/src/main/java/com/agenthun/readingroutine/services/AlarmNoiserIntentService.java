package com.agenthun.readingroutine.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activities.MainActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmNoiserIntentService extends IntentService {
    private static final String TAG = "ANIntentService";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_NOTIFICATION = "com.agenthun.readingroutine.services.action.NOTIFICATION";
    public static final String ACTION_BAZ = "com.agenthun.readingroutine.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.agenthun.readingroutine.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.agenthun.readingroutine.services.extra.PARAM2";

    public AlarmNoiserIntentService() {
        super("AlarmNoiserIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionNotification(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AlarmNoiserIntentService.class);
        intent.setAction(ACTION_NOTIFICATION);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
        Log.d(TAG, "startActionNotification");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AlarmNoiserIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFICATION.equals(action)) {
                final String param1 = intent.getStringExtra(AlarmNoiser.EXTRA_CONTENT_TEXT);
//                Log.d(TAG, "onHandleIntent() returned: " + param1);
                handleActionNotification(param1);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionNotification(String param1) {
        // TODO: Handle action Foo
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_reading_routine_white_no_annulus_48dp)
                .setContentTitle(getString(R.string.text_notification_title))
                .setContentText("亲, 您的<<" + param1 + ">>是否按计划完成了?")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
//        .setStyle(new NotificationCompat.BigTextStyle().bigText(param1));

        Intent intent = new Intent(this, AlarmNoiserIntentService.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

        Intent serviceIntent = new Intent(this, AlarmNoiserReciever.class);
        sendBroadcast(serviceIntent, null);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        Log.d(TAG, "handleActionBaz() returned: ");
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "AlarmNoiserIntentService onDestroy()");
        super.onDestroy();
    }
}
