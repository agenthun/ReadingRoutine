package com.agenthun.readingroutine.transitionmanagers;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.readingroutine.transitionmanagers.lifecycle.IComponentContainer;
import com.agenthun.readingroutine.transitionmanagers.lifecycle.LifeCycleComponent;
import com.agenthun.readingroutine.transitionmanagers.lifecycle.LifeCycleComponentManager;

/**
 * Created by Agent Henry on 2015/5/16.
 */
public class TFragment extends Fragment implements ITFragment, IComponentContainer {

    protected Object mDataIn;
    private boolean mFirstResume = true;

    private LifeCycleComponentManager mLifeCycleComponentManager = new LifeCycleComponentManager();

    @Override
    public void addComponent(LifeCycleComponent component) {
        mLifeCycleComponentManager.addComponent(component);
    }

    public TFragmentActivity getContext() {
        return (TFragmentActivity) getActivity();
    }

    @Override
    public void onEnter(Object data) {
        mDataIn = data;
    }

    @Override
    public void onLeave() {
        mLifeCycleComponentManager.onBecomesTotallyInvisible();
    }

    @Override
    public void onBack() {
        mLifeCycleComponentManager.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public void onBackWithData(Object data) {
        mLifeCycleComponentManager.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public boolean processBackPressed() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFirstResume) {
            onBack();
        }
        if (mFirstResume) {
            mFirstResume = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onLeave();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifeCycleComponentManager.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setIsTrial(boolean isTrial) {
        getContext().getSharedPreferences(TFragmentActivity.GLOBAL_SETTINGS, 0)
                .edit().putBoolean(TFragmentActivity.IS_TRIAL, isTrial).commit();
    }

    public boolean getIsTrial() {
        return getContext().getSharedPreferences(TFragmentActivity.GLOBAL_SETTINGS, 0)
                .getBoolean(TFragmentActivity.IS_TRIAL, true);
    }
}
