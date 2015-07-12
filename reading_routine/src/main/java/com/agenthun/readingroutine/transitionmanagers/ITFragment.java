package com.agenthun.readingroutine.transitionmanagers;

/**
 * Created by Agent Henry on 2015/5/16.
 */
public interface ITFragment {

    void onEnter(Object data);

    void onLeave();

    void onBack();

    void onBackWithData(Object data);

    boolean processBackPressed();
}
