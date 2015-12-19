package com.agenthun.readingroutine.transitionmanagers;

import android.content.Intent;
import android.os.Bundle;

import com.agenthun.readingroutine.services.AlarmNoiserReciever;
import com.agenthun.readingroutine.transitionmanagers.lifecycle.IComponentContainer;
import com.agenthun.readingroutine.transitionmanagers.lifecycle.LifeCycleComponent;
import com.agenthun.readingroutine.transitionmanagers.lifecycle.LifeCycleComponentManager;

/**
 * Created by Agent Henry on 2015/5/16.
 */
public abstract class TActivity extends TFragmentActivity implements IComponentContainer {
    private LifeCycleComponentManager mLifeCycleComponentManager = new LifeCycleComponentManager();

    @Override
    public void addComponent(LifeCycleComponent component) {
        mLifeCycleComponentManager.addComponent(component);
    }

    @Override
    protected String getCloseWarning() {
        return "再按一次，退出程序";
    }


    @Override
    protected void onStart() {
        super.onStart();
        mLifeCycleComponentManager.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifeCycleComponentManager.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifeCycleComponentManager.onBecomesPartiallyInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifeCycleComponentManager.onBecomesVisibleFromPartiallyInvisible();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifeCycleComponentManager.onBecomesTotallyInvisible();
    }

    protected void callRoutineService() {
        Intent serviceIntent = new Intent(this, AlarmNoiserReciever.class);
        sendBroadcast(serviceIntent, null);
    }
}
