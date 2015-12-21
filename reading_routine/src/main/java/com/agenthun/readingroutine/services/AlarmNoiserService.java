package com.agenthun.readingroutine.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.db.BookDatabaseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class AlarmNoiserService extends Service {
    private static final String TAG = "AlarmNoiserService";

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BookInfo bookInfo = getNext();
        if (bookInfo != null) {
            try {
                final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
                long time = getAlarmTime(DATE_FORMAT.parse(bookInfo.getBookAlarmTime()));
                AlarmNoiser.startAlarmNoiserService(this, time, bookInfo.getBookName(), AlarmNoiserIntentService.class, AlarmNoiserIntentService.ACTION_NOTIFICATION);
                Log.d(TAG, "onStartCommand() returned: Alarm Time " + new Date(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onStartCommand() returned: start AlarmNoiserIntentService" + bookInfo.toString());
        } else {
            AlarmNoiser.stopAlarmNoiserService(this, AlarmNoiserIntentService.class, AlarmNoiserIntentService.ACTION_NOTIFICATION);
            Log.d(TAG, "onStartCommand() returned: stop AlarmNoiserIntentService");
        }
        return START_NOT_STICKY;
    }

    private long getAlarmTime(Date date) {
        long alarmTime;
//        alarmTime = date.getTime() - 3600000;
        alarmTime = date.getTime();
        return alarmTime;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() returned: ");
        BookDatabaseUtil.destory();
        super.onDestroy();
    }

    private BookInfo getNext() {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        ArrayList<BookInfo> mDataSet = BookDatabaseUtil.getInstance(getApplicationContext()).queryBookInfos();

        Set<BookInfo> queue = new TreeSet<>(new Comparator<BookInfo>() {
            @Override
            public int compare(BookInfo lhs, BookInfo rhs) {
                int result = 0;
                try {
                    long diff = DATE_FORMAT.parse(lhs.getBookAlarmTime()).getTime() - DATE_FORMAT.parse(rhs.getBookAlarmTime()).getTime();
                    if (diff > 0) return 1;
                    else if (diff < 0) return -1;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });

        for (BookInfo bookInfo :
                mDataSet) {
            try {
                if ((DATE_FORMAT.parse(bookInfo.getBookAlarmTime())).after(Calendar.getInstance().getTime())) {
                    queue.add(bookInfo);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (queue.iterator().hasNext()) {
            return queue.iterator().next();
        } else {
            return null;
        }
    }
}
