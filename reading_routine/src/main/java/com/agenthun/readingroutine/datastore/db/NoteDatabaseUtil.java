package com.agenthun.readingroutine.datastore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.agenthun.readingroutine.datastore.NoteInfo;
import com.agenthun.readingroutine.datastore.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Agent Henry on 2015/7/28.
 */
public class NoteDatabaseUtil {
    private static final String TAG = "NoteDatabaseUtil";
    public static final String DATABASE_NAME = "readingroutine_note.db";
    public static final int DATABASE_VERSION = 1;

    private static NoteDatabaseUtil instance;
    private NoteDBHelper noteDBHelper;
    private Context mContext;

    //单例模型
    private NoteDatabaseUtil(Context context) {
        noteDBHelper = new NoteDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public synchronized static NoteDatabaseUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (BookDatabaseUtil.class) {
                if (instance == null) {
                    instance = new NoteDatabaseUtil(context);
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
        if (noteDBHelper != null) {
            noteDBHelper.close();
            noteDBHelper = null;
        }
    }

    public void deleteNote(NoteInfo noteInfo) {
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        String where = NoteDBHelper.NoteTable.USER_ID + " = '" + userId
                + "' AND " + NoteDBHelper.NoteTable.OBJECT_ID + " = '" + noteInfo.getObjectId() + "'";
        cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            noteDBHelper.delete(NoteDBHelper.TABLE_NAME, where, null);
            Log.i(TAG, "delete success");
        }
        if (cursor == null) {
            where = NoteDBHelper.NoteTable.USER_ID + " = '" + userId
                    + "' AND " + NoteDBHelper.NoteTable.NOTE_TITLE + " = '" + noteInfo.getNoteTitle()
                    + "' AND " + NoteDBHelper.NoteTable.NOTE_COMPOSE + " = '" + noteInfo.getNoteCompose()
                    + "' AND " + NoteDBHelper.NoteTable.NOTE_CREATE_TIME + " = '" + noteInfo.getNoteCreateTime()
                    + "' AND " + NoteDBHelper.NoteTable.NOTE_COLOR + " = '" + noteInfo.getNoteColor() + "'";
            cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, where, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                noteDBHelper.delete(NoteDBHelper.TABLE_NAME, where, null);
                Log.i(TAG, "delete success");
            }
        }

        if (cursor != null) {
            cursor.close();
            noteDBHelper.close();
        }
    }

    public void deleteNote(NoteInfo noteInfo, boolean isOffline) {
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        if (userId == null) userId = BookDatabaseUtil.TRIAL_USER;
        String where = NoteDBHelper.NoteTable.USER_ID + " = '" + userId
                + "' AND " + NoteDBHelper.NoteTable.NOTE_TITLE + " = '" + noteInfo.getNoteTitle()
                + "' AND " + NoteDBHelper.NoteTable.NOTE_COMPOSE + " = '" + noteInfo.getNoteCompose()
                + "' AND " + NoteDBHelper.NoteTable.NOTE_CREATE_TIME + " = '" + noteInfo.getNoteCreateTime()
                + "' AND " + NoteDBHelper.NoteTable.NOTE_COLOR + " = '" + noteInfo.getNoteColor() + "'";
        cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            noteDBHelper.delete(NoteDBHelper.TABLE_NAME, where, null);
            Log.i(TAG, "delete success");
        }

        if (cursor != null) {
            cursor.close();
            noteDBHelper.close();
        }
    }

    public long insertNote(NoteInfo noteInfo) {
        long uri = 0;
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        String where = NoteDBHelper.NoteTable.USER_ID + " = '" + userId
                + "' AND " + NoteDBHelper.NoteTable.OBJECT_ID + " = '" + noteInfo.getObjectId() + "'";
        cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NoteDBHelper.NoteTable.NOTE_TITLE, noteInfo.getNoteTitle());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COMPOSE, noteInfo.getNoteCompose());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_CREATE_TIME, noteInfo.getNoteCreateTime());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COLOR, noteInfo.getNoteColor());
            noteDBHelper.update(NoteDBHelper.TABLE_NAME, contentValues, where, null);
            Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NoteDBHelper.NoteTable.USER_ID, userId);
            contentValues.put(NoteDBHelper.NoteTable.OBJECT_ID, noteInfo.getObjectId());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_TITLE, noteInfo.getNoteTitle());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COMPOSE, noteInfo.getNoteCompose());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_CREATE_TIME, noteInfo.getNoteCreateTime());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COLOR, noteInfo.getNoteColor());
            uri = noteDBHelper.insert(NoteDBHelper.TABLE_NAME, null, contentValues);
            Log.i(TAG, "insert");
        }
        if (cursor != null) {
            cursor.close();
            noteDBHelper.close();
        }
        return uri;
    }

    public long insertNote(NoteInfo noteInfo, NoteInfo noteInfoOld, boolean isOffline) {
        long uri = 0;
        Cursor cursor = null;
        String userId = (String) UserData.getObjectByKey(mContext, "objectId");
        if (userId == null) userId = BookDatabaseUtil.TRIAL_USER;
        String where = NoteDBHelper.NoteTable.USER_ID + " = '" + userId
                + "' AND " + NoteDBHelper.NoteTable.NOTE_TITLE + " = '" + noteInfoOld.getNoteTitle()
                + "' AND " + NoteDBHelper.NoteTable.NOTE_COMPOSE + " = '" + noteInfoOld.getNoteCompose()
                + "' AND " + NoteDBHelper.NoteTable.NOTE_CREATE_TIME + " = '" + noteInfoOld.getNoteCreateTime() + "'";
        cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, where, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NoteDBHelper.NoteTable.USER_ID, userId);
            contentValues.put(NoteDBHelper.NoteTable.OBJECT_ID, noteInfo.getObjectId());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_TITLE, noteInfo.getNoteTitle());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COMPOSE, noteInfo.getNoteCompose());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_CREATE_TIME, noteInfo.getNoteCreateTime());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COLOR, noteInfo.getNoteColor());
            noteDBHelper.update(NoteDBHelper.TABLE_NAME, contentValues, where, null);
            Log.i(TAG, "update");
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NoteDBHelper.NoteTable.USER_ID, userId);
            contentValues.put(NoteDBHelper.NoteTable.OBJECT_ID, noteInfo.getObjectId());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_TITLE, noteInfo.getNoteTitle());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COMPOSE, noteInfo.getNoteCompose());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_CREATE_TIME, noteInfo.getNoteCreateTime());
            contentValues.put(NoteDBHelper.NoteTable.NOTE_COLOR, noteInfo.getNoteColor());
            uri = noteDBHelper.insert(NoteDBHelper.TABLE_NAME, null, contentValues);
            Log.i(TAG, "insert");
        }
        if (cursor != null) {
            cursor.close();
            noteDBHelper.close();
        }
        return uri;
    }

    public ArrayList<NoteInfo> queryNoteInfos() {
        ArrayList<NoteInfo> nookInfos = null;
        Cursor cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, null, null, null, null, null);
        Log.i(TAG, cursor.getCount() + "");

        if (cursor == null) {
            return null;
        }
        nookInfos = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NoteInfo noteInfo = new NoteInfo();
            noteInfo.setObjectId(cursor.getString(cursor.getColumnIndex(NoteDBHelper.NoteTable.OBJECT_ID)));
            noteInfo.setNoteTitle(cursor.getString(3));
            noteInfo.setNoteCompose(cursor.getString(4));
            noteInfo.setNoteCreateTime(cursor.getString(5));
            noteInfo.setNoteColor(cursor.getInt(6));
            nookInfos.add(0, noteInfo);
        }
        if (cursor != null) {
            cursor.close();
        }
        return nookInfos;
    }

    public List<NoteInfo> setNotes(List<NoteInfo> lists) {
        Cursor cursor = null;
        if (lists != null && lists.size() > 0) {
            for (Iterator iterator = lists.iterator(); iterator.hasNext(); ) {
                NoteInfo noteInfo = (NoteInfo) iterator.next();
                insertNote(noteInfo);
            }
        }
        if (cursor != null) {
            cursor.close();
            noteDBHelper.close();
        }
        return lists;
    }

    public ArrayList<NoteInfo> queryInsertBatchNoteInfos() {
        ArrayList<NoteInfo> noteInfos = null;
        String where = NoteDBHelper.NoteTable.USER_ID + " = '" + UserData.getObjectByKey(mContext, "objectId")
                + "' AND " + NoteDBHelper.NoteTable.OBJECT_ID + " is null";
        Cursor cursor = noteDBHelper.query(NoteDBHelper.TABLE_NAME, null, where, null, null, null, null);
        Log.i(TAG, cursor.getCount() + "");

        if (cursor == null) {
            return null;
        }
        noteInfos = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NoteInfo noteInfo = new NoteInfo();
            noteInfo.setNoteTitle(cursor.getString(3));
            noteInfo.setNoteCompose(cursor.getString(4));
            noteInfo.setNoteCreateTime(cursor.getString(5));
            noteInfo.setNoteColor(cursor.getInt(6));
            noteInfos.add(0, noteInfo);
        }
        if (cursor != null) {
            cursor.close();
        }
        return noteInfos;
    }
}
