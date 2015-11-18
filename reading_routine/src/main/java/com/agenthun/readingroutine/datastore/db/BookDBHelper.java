package com.agenthun.readingroutine.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Agent Henry on 2015/7/28.
 */
public class BookDBHelper extends DBHelper {

    public static final String TABLE_NAME = "bookinfo";

    interface BookinfoTable {
        String _ID = "_id";
        String USER_ID = "userid";
        String OBJECT_ID = "objectid";
        String BOOK_NAME = "bookName";
        String BOOK_COLOR = "bookColor";
        String BOOK_ALARM_TIME = "bookAlarmTime";
    }

    public BookDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
}
