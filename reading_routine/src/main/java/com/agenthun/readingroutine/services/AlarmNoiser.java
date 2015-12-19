package com.agenthun.readingroutine.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/16 下午8:30.
 */
public class AlarmNoiser {
    public static final String EXTRA_CONTENT_TEXT = "EXTRA_CONTENT_TEXT";

    public static void startAlarmNoiserService(Context context, long triggerAlarmTimeAtMillis, String contentText, Class<?> cls, String action) {
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        intent.putExtra(EXTRA_CONTENT_TEXT, contentText);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, triggerAlarmTimeAtMillis, pendingIntent);
    }

    public static void stopAlarmNoiserService(Context context, Class<?> cls, String action) {
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
}
