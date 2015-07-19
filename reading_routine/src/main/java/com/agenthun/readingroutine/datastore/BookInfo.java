package com.agenthun.readingroutine.datastore;

import cn.bmob.v3.BmobObject;

/**
 * Created by Agent Henry on 2015/7/19.
 */
public class BookInfo extends BmobObject {
    private UserData userData;
    private String bookName;
    private Integer bookColor;
    private String bookAlarmTime;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getBookColor() {
        return bookColor;
    }

    public void setBookColor(Integer bookColor) {
        this.bookColor = bookColor;
    }

    public String getBookAlarmTime() {
        return bookAlarmTime;
    }

    public void setBookAlarmTime(String bookAlarmTime) {
        this.bookAlarmTime = bookAlarmTime;
    }
}
