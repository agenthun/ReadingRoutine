package com.agenthun.readingroutine.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Agent Henry on 2015/7/28.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "bookinfo";

    private SQLiteDatabase mDb;

    interface BookinfoTable {
        String _ID = "_id";
        String USER_ID = "userid";
        String OBJECT_ID = "objectid";
        String BOOK_NAME = "bookName";
        String BOOK_COLOR = "bookColor";
        String BOOK_ALARM_TIME = "bookAlarmTime";
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append(" ( ").append(BookinfoTable._ID).append(" INTEGER PRIMARY KEY,")
                .append(BookinfoTable.USER_ID).append(" varchar(100),")
                .append(BookinfoTable.OBJECT_ID).append(" varchar(20),")
                .append(BookinfoTable.BOOK_NAME).append(" varchar(20),")
                .append(BookinfoTable.BOOK_COLOR).append(" Integer,")
                .append(BookinfoTable.BOOK_ALARM_TIME).append(" varchar(20));");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //获取数据库操作对象
    public synchronized SQLiteDatabase getDatabase(boolean isWrite) {
        if (mDb == null || !mDb.isOpen()) {
            if (isWrite) {
                try {
                    mDb = getWritableDatabase();
                } catch (Exception e) {
                    mDb = getReadableDatabase();
                    return mDb;
                }
            } else {
                mDb = getReadableDatabase();
            }
        }
        return mDb;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        getDatabase(true);
        return mDb.insertOrThrow(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.update(table, values, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        getDatabase(false);
        return mDb.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql) {
        getDatabase(true);
        mDb.execSQL(sql);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, final String orderBy) {
        getDatabase(true);
        return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }
}
