package com.agenthun.readingroutine.datastore;

import cn.bmob.v3.BmobObject;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/19 上午1:23.
 */
public class NoteInfo extends BmobObject {
    private static final long serialVersionUID = 1L;

    private UserData userData;
    private String noteTitle;
    private String noteCompose;
    private String noteCreateTime;
    private Integer noteColor;

    public NoteInfo() {
    }

    public NoteInfo(UserData userData, String noteTitle, String noteCompose, String noteCreateTime, Integer noteColor) {
        this.userData = userData;
        this.noteTitle = noteTitle;
        this.noteCompose = noteCompose;
        this.noteCreateTime = noteCreateTime;
        this.noteColor = noteColor;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteCompose() {
        return noteCompose;
    }

    public void setNoteCompose(String noteCompose) {
        this.noteCompose = noteCompose;
    }

    public String getNoteCreateTime() {
        return noteCreateTime;
    }

    public void setNoteCreateTime(String noteCreateTime) {
        this.noteCreateTime = noteCreateTime;
    }

    public Integer getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(Integer noteColor) {
        this.noteColor = noteColor;
    }

    @Override
    public String toString() {
        return "NoteInfo{" +
                "ObjectId=" + getObjectId() + '\'' +
                "userData=" + userData +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteCompose='" + noteCompose + '\'' +
                ", noteCreateTime='" + noteCreateTime + '\'' +
                ", noteColor=" + noteColor +
                '}';
    }
}
