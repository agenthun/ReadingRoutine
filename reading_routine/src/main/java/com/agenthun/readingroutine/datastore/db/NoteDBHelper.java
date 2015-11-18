package com.agenthun.readingroutine.datastore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Agent Henry on 2015/7/28.
 */
public class NoteDBHelper extends DBHelper {

    public static final String TABLE_NAME = "notes";

    interface NoteTable {
        String _ID = "_id";
        String USER_ID = "userid";
        String OBJECT_ID = "objectid";
        String NOTE_TITLE = "noteTitle";
        String NOTE_COMPOSE = "noteCompose";
        String NOTE_CREATE_TIME = "noteCreateTime";
        String NOTE_COLOR = "noteColor";
    }

    public NoteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append(" ( ").append(NoteTable._ID).append(" INTEGER PRIMARY KEY,")
                .append(NoteTable.USER_ID).append(" varchar(100),")
                .append(NoteTable.OBJECT_ID).append(" varchar(20),")
                .append(NoteTable.NOTE_TITLE).append(" varchar(20),")
                .append(NoteTable.NOTE_COMPOSE).append(" varchar(65536),")
                .append(NoteTable.NOTE_CREATE_TIME).append(" varchar(20),")
                .append(NoteTable.NOTE_COLOR).append(" Integer);");
        db.execSQL(sb.toString());
    }
}
