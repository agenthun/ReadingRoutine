package com.agenthun.readingroutine.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activities.MainActivity;

public class AlarmNoiserService extends Service {
    private static final String TAG = "AlarmNoiserService";
    public static final String ACTION = "com.agenthun.readingroutine.services.AlarmNoiserService";

    NotificationCompat.Builder mBuilder;

    public AlarmNoiserService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "AlarmNoiserService onCreate() returned: ");
        super.onCreate();
    }

    private void initNotifiManager() {
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_reading_routine_white)
                .setContentTitle("阅读提醒")
                .setContentText("你的书该看了");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    int count = 0;

    class AlarmNoiserThread extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "run() returned: ");
            count++;
            if (count % 5 == 0) {
                showNotification();
                Log.d(TAG, "run() returned: new message");
            }
        }
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
        Log.d(TAG, "showNotification() returned: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() returned: ");
    }
}
