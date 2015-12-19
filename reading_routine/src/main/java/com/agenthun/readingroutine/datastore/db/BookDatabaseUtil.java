package com.agenthun.readingroutine.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Agent Henry on 2015/7/28.
 */
public class BookDatabaseUtil {
    private static final String TAG = "BookDatabaseUtil";
    protected static final String TRIAL_USER = "trial_user";
    public static final String DATABASE_NAME = "readingroutine_book.db";
    public static final int DATABASE_VERSION = 1;

    private static BookDatabaseUtil instance;
    private BookDBHelper bookDBHelper;
    private Context mContext;

    //单例模型
    private BookDatabaseUtil(Context context) {
        bookDBHelper = new BookDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

/*    public synchronized static BookDatabaseUtil getInstance(Context context) {
        if (instance == null) {
            instance = new BookDatabaseUtil(context);
        }
        return instance;
    }*/

    public static BookDatabaseUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (BookDatabaseUtil.class) {
                if (instance == null) {
                    instance = new BookDatabaseUtil(context);
                }
            }
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
        if (bookDBHelper != null) {
            bookDBHelper.close();
            bookDBHelper = null;
        }
    }

    public void deleteBookInfo(BookInfo bookInfo) {
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + userId
                + "' AND " + BookDBHelper.BookinfoTable.OBJECT_ID + " = '" + bookInfo.getObjectId() + "'";
        cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            bookDBHelper.delete(BookDBHelper.TABLE_NAME, where, null);
            //Log.i(TAG, "delete success");
        }
        if (cursor == null) {
            where = BookDBHelper.BookinfoTable.USER_ID + " = '" + userId
                    + "' AND " + BookDBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfo.getBookName()
                    + "' AND " + BookDBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfo.getBookColor()
                    + "' AND " + BookDBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfo.getBookAlarmTime() + "'";
            cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                bookDBHelper.delete(BookDBHelper.TABLE_NAME, where, null);
                //Log.i(TAG, "delete success");
            }
        }

        if (cursor != null) {
            cursor.close();
            bookDBHelper.close();
        }
    }

    public void deleteBookInfo(BookInfo bookInfo, boolean isOffline) {
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        if (userId == null) userId = TRIAL_USER;
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + userId
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfo.getBookName()
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfo.getBookColor()
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfo.getBookAlarmTime() + "'";
        cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            bookDBHelper.delete(BookDBHelper.TABLE_NAME, where, null);
            //Log.i(TAG, "delete success");
        }

        if (cursor != null) {
            cursor.close();
            bookDBHelper.close();
        }
    }

    public long insertBookInfo(BookInfo bookInfo) {
        long uri = 0;
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + userId
                + "' AND " + BookDBHelper.BookinfoTable.OBJECT_ID + " = '" + bookInfo.getObjectId() + "'";
        cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookDBHelper.BookinfoTable.USER_ID, userId);
            contentValues.put(BookDBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            bookDBHelper.update(BookDBHelper.TABLE_NAME, contentValues, where, null);
            //Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookDBHelper.BookinfoTable.USER_ID, userId);
            contentValues.put(BookDBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            uri = bookDBHelper.insert(BookDBHelper.TABLE_NAME, null, contentValues);
            //Log.i(TAG, "insert");
        }
        if (cursor != null) {
            cursor.close();
            bookDBHelper.close();
        }
        return uri;
    }

    public long insertBookInfo(BookInfo bookInfo, BookInfo bookInfoOld, boolean isOffline) {
        long uri = 0;
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        if (userId == null) userId = TRIAL_USER;
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + userId
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfoOld.getBookName()
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfoOld.getBookColor()
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfoOld.getBookAlarmTime() + "'";
        cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookDBHelper.BookinfoTable.USER_ID, userId);
            contentValues.put(BookDBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            bookDBHelper.update(BookDBHelper.TABLE_NAME, contentValues, where, null);
            //Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookDBHelper.BookinfoTable.USER_ID, userId);
            contentValues.put(BookDBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            uri = bookDBHelper.insert(BookDBHelper.TABLE_NAME, null, contentValues);
            //Log.i(TAG, "insert");
        }

/*        //version1 for trial user and sign in user with online
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        if (userId == null) userId = TRIAL_USER;
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + userId
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_NAME + " = '" + bookInfoOld.getBookName()
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_COLOR + " = '" + bookInfoOld.getBookColor()
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_ALARM_TIME + " = '" + bookInfoOld.getBookAlarmTime() + "'";
        cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookDBHelper.BookinfoTable.USER_ID, userId);
            contentValues.put(BookDBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            bookDBHelper.update(BookDBHelper.TABLE_NAME, contentValues, where, null);
            //Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BookDBHelper.BookinfoTable.USER_ID, userId);
            contentValues.put(BookDBHelper.BookinfoTable.OBJECT_ID, bookInfo.getObjectId());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_NAME, bookInfo.getBookName());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_COLOR, bookInfo.getBookColor());
            contentValues.put(BookDBHelper.BookinfoTable.BOOK_ALARM_TIME, bookInfo.getBookAlarmTime());
            uri = bookDBHelper.insert(BookDBHelper.TABLE_NAME, null, contentValues);
            //Log.i(TAG, "insert");
        }*/

        if (cursor != null) {
            cursor.close();
            bookDBHelper.close();
        }
        return uri;
    }

    public boolean queryHasBookInfo(String bookName) {
        boolean hasTheBook = false;
        Cursor cursor = null;
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + UserData.getObjectByKey(mContext, "objectId")
                + "' AND " + BookDBHelper.BookinfoTable.BOOK_NAME + " = '" + bookName + "'";
        cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            hasTheBook = true;
            cursor.close();
        } else {
            hasTheBook = false;
        }
        return hasTheBook;
    }

    public ArrayList<BookInfo> queryBookInfos() {
        ArrayList<BookInfo> bookInfos = null;
        Cursor cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, null, null, null, null, null);
        //Log.i(TAG, cursor.getCount() + "");

        if (cursor == null) {
            return null;
        }
        bookInfos = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setObjectId(cursor.getString(cursor.getColumnIndex(BookDBHelper.BookinfoTable.OBJECT_ID)));
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
        if (lists != null && lists.size() > 0) {
            for (Iterator iterator = lists.iterator(); iterator.hasNext(); ) {
                BookInfo bookInfo = (BookInfo) iterator.next();
                insertBookInfo(bookInfo);
            }
        }
        if (cursor != null) {
            cursor.close();
            bookDBHelper.close();
        }
        return lists;
    }

    public ArrayList<BookInfo> queryInsertBatchBookInfos() {
        ArrayList<BookInfo> bookInfos = null;
        String where = BookDBHelper.BookinfoTable.USER_ID + " = '" + UserData.getObjectByKey(mContext, "objectId")
                + "' AND " + BookDBHelper.BookinfoTable.OBJECT_ID + " is null";
        Cursor cursor = bookDBHelper.query(BookDBHelper.TABLE_NAME, null, where, null, null, null, null);
        //Log.i(TAG, cursor.getCount() + "");

        if (cursor == null) {
            return null;
        }
        bookInfos = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BookInfo bookInfo = new BookInfo();
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
}
