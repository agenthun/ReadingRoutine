package com.agenthun.readingroutine.datastore;

import cn.bmob.v3.BmobObject;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/22 下午11:36.
 */
public class Feedback extends BmobObject {
    private String contacts;
    private String content;

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
