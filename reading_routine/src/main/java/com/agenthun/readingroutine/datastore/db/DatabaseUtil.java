package com.agenthun.readingroutine.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.agenthun.readingroutine.activities.LoginActivity;
import com.agenthun.readingroutine.datastore.BookInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Agent Henry on 2015/7/28.
 */
public class DatabaseUtil {
    private static final String TAG = "DatabaseUtil";
    public static final String DATABASE_NAME = "readingroutine.db";
    public static final int DATABASE_VERSION = 1;

    private static DatabaseUtil instance;
    private DBHelper dbHelper;

    //单例模型
    private DatabaseUtil(Context context) {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public synchronized static DatabaseUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseUtil(context);
        }
        return instance;
    }

    public static void destory() {
        if (instance != null) {
            instance.onDestory();
        }
    }

    public void onDestory() {
        instance = null;
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }

    public void deleteBookInfo(BookInfo bookInfo) {
        Cursor cursor = null;
        String where = DBHelper.BookinfoTable.USER_ID + " = '" + LoginActivity.userData.getObjectId()
                + "' AND " + DBHelper.BookinfoTable.OBJECT_ID + " = '" + bookInfo.getObjectId() + "'";
        cursor = dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
/*            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.BookinfoTable.BOOK_NAME));
                if (name == bookInfo.getBookName()) {*/
            dbHelper.delete(DBHelper.TABLE_NAME, where, null);
            Log.i(TAG, "delete success");
/*                }
            }*/
        }
        if (cursor == null) {
            where = DBHelper.BookinfoTable.USER_ID + " = '" + LoginActivity.userData.getObjectId()
                    + "' AND " + DBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfo.getBookName() + "'"
                    + "' AND " + DBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfo.getBookColor() + "'"
                    + "' AND " + DBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfo.getBookAlarmTime() + "'";
            cursor = dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                dbHelper.delete(DBHelper.TABLE_NAME, where, null);
                Log.i(TAG, "delete success");
            }
        }

        if (cursor != null) {
            cursor.close();
            dbHelper.close();
        }
    }

    public void deleteBookInfo(BookInfo bookInfo, boolean isOffline) {
        Cursor cursor = null;
        String where = DBHelper.BookinfoTable.USER_ID + " = '" + LoginActivity.userData.getObjectId()
                + "' AND " + DBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfo.getBookName()
                + "' AND " + DBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfo.getBookColor()
                + "' AND " + DBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfo.getBookAlarmTime() + "'";
        cursor = dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
/*            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.BookinfoTable.BOOK_NAME));
                if (name == bookInfo.getBookName()) {*/
            dbHelper.delete(DBHelper.TABLE_NAME, where, null);
            Log.i(TAG, "delete success");
/*                }
            }*/
        }

        if (cursor != null) {
            cursor.close();
            dbHelper.close();
        }
    }

    public long insertBookInfo(BookInfo bookInfo) {
        long uri = 0;
        Cursor cursor = null;
        String where = DBHelper.BookinfoTable.USER_ID + " = '" + LoginActivity.userData.getObjectId()
                + "' AND " + DBHelper.BookinfoTable.OBJECT_ID + " = '" + bookInfo.getObjectId() + "'";
        cursor = dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(DBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(DBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            dbHelper.update(DBHelper.TABLE_NAME, contentValues, where, null);
            Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.BookinfoTable.USER_ID, LoginActivity.userData.getObjectId());
            contentValues.put(DBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(DBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(DBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(DBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            uri = dbHelper.insert(DBHelper.TABLE_NAME, null, contentValues);
            Log.i(TAG, "insert");
        }
        if (cursor != null) {
            cursor.close();
            dbHelper.close();
        }
        return uri;
    }

    public long insertBookInfo(BookInfo bookInfo, BookInfo bookInfoOld, boolean isOffline) {
        long uri = 0;
        Cursor cursor = null;
        String where = DBHelper.BookinfoTable.USER_ID + " = '" + LoginActivity.userData.getObjectId()
                + "' AND " + DBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfoOld.getBookName()
                + "' AND " + DBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfoOld.getBookColor()
                + "' AND " + DBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfoOld.getBookAlarmTime() + "'";
        cursor = dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.BookinfoTable.USER_ID, LoginActivity.userData.getObjectId());
            contentValues.put(DBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(DBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(DBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(DBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            dbHelper.update(DBHelper.TABLE_NAME, contentValues, where, null);
            Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.BookinfoTable.USER_ID, LoginActivity.userData.getObjectId());
            contentValues.put(DBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(DBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(DBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(DBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            uri = dbHelper.insert(DBHelper.TABLE_NAME, null, contentValues);
            Log.i(TAG, "insert");
        }
        if (cursor != null) {
            cursor.close();
            dbHelper.close();
        }
        return uri;
    }

    public ArrayList<BookInfo> queryBookInfos() {
        ArrayList<BookInfo> bookInfos = null;
        Cursor cursor = dbHelper.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        Log.i(TAG, cursor.getCount() + "");

        if (cursor == null) {
            return null;
        }
        bookInfos = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setObjectId(cursor.getString(cursor.getColumnIndex(DBHelper.BookinfoTable.OBJECT_ID)));
            bookInfo.setBookName(cursor.getString(3));
            bookInfo.setBookColor(cursor.getInt(4));
            bookInfo.setBookAlarmTime(cursor.getString(5));
            bookInfos.add(0, bookInfo);
        }
        if (cursor != null) {
            cursor.close();
        }
        return bookInfos;
    }

    public List<BookInfo> setBookInfos(List<BookInfo> lists) {
        Cursor cursor = null;
        if (lists != null & lists.size() > 0) {
            for (Iterator iterator = lists.iterator(); iterator.hasNext(); ) {
                BookInfo bookInfo = (BookInfo) iterator.next();
                insertBookInfo(bookInfo);
            }
        }
        if (cursor != null) {
            cursor.close();
            dbHelper.close();
        }
        return lists;
    }
}
