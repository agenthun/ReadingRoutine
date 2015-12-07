package com.agenthun.readingroutine.transitionmanagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by Agent Henry on 2015/5/16.
 */
public abstract class TFragmentActivity extends AppCompatActivity {

    protected static final String GLOBAL_SETTINGS = "MR_GLOBAL_SETTINGS";
    protected static final String IS_TRIAL = "IS_TRIAL";

    protected TFragment mCurrentTFragment;
    private boolean mCloseWarned;

    protected abstract String getCloseWarning();

    protected abstract int getFragmentContainerId();

    public void pushFragmentToBackStack(Class<?> cls, Object data) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        param.data = data;
        goToThisFragment(param);
    }

    private void goToThisFragment(FragmentParam param) {
        int containerId = getFragmentContainerId();
        Class<?> cls = param.cls;
        if (cls == null) {
            return;
        }

        try {
            String fragmentTag = getFragmentTag(param);
            FragmentManager fm = getSupportFragmentManager();
            TFragment fragment = (TFragment) fm.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                fragment = (TFragment) cls.newInstance();
            }
            if (mCurrentTFragment != null && mCurrentTFragment != fragment) {
                mCurrentTFragment.onLeave();
            }
            fragment.onEnter(param.data);

            FragmentTransaction ft = fm.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out,
                    android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(containerId, fragment, fragmentTag);
            }
            mCurrentTFragment = fragment;

            ft.addToBackStack(fragmentTag);
            ft.commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mCloseWarned = false;
    }

    protected String getFragmentTag(FragmentParam param) {
        StringBuilder sb = new StringBuilder(param.cls.toString());
        return sb.toString();
    }

    public void goToFragment(Class<?> cls, Object data) {
        if (cls == null) {
            return;
        }
        TFragment fragment = (TFragment) getSupportFragmentManager().findFragmentByTag(cls.toString());
        if (fragment != null) {
            mCurrentTFragment = fragment;
            fragment.onBackWithData(data);
        }
        getSupportFragmentManager().popBackStackImmediate(cls.toString(), 0);
    }

    public void popTopFragment(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
        if (tryToUpdateCurrentAfterPop() && mCurrentTFragment != null) {
            mCurrentTFragment.onBackWithData(data);
        }
    }

    public void popToRoot(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        }
        popTopFragment(data);
    }

    protected boolean processBackPressed() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (processBackPressed()) {
            return;
        }

        boolean enableBackPressed = true;
        if (mCurrentTFragment != null) {
            enableBackPressed = !mCurrentTFragment.processBackPressed();
        }
        if (enableBackPressed) {
            int cnt = getSupportFragmentManager().getBackStackEntryCount();
            if (cnt <= 1 && isTaskRoot()) {
                String closeWarningHint = getCloseWarning();
                if (!mCloseWarned && !TextUtils.isEmpty(closeWarningHint)) {
                    Toast toast = Toast.makeText(this, closeWarningHint, Toast.LENGTH_SHORT);
                    toast.show();
                    mCloseWarned = true;
                } else {
                    doReturnBack();
                }
            } else {
                mCloseWarned = false;
                doReturnBack();
            }
        }
    }

    public int getCountBackStackEntryAt() {
        int cnt = getSupportFragmentManager().getBackStackEntryCount();
        if (cnt > 1) {
            doReturnBack();
        }
        return cnt;
    }

    private boolean tryToUpdateCurrentAfterPop() {
        FragmentManager fm = getSupportFragmentManager();
        int cnt = fm.getBackStackEntryCount();
        if (cnt > 0) {
            String name = fm.getBackStackEntryAt(cnt - 1).getName();
            Fragment fragment = fm.findFragmentByTag(name);
            if (fragment != null && fragment instanceof TFragment) {
                mCurrentTFragment = (TFragment) fragment;
            }
            return true;
        }
        return false;
    }

    protected void doReturnBack() {
        int cnt = getSupportFragmentManager().getBackStackEntryCount();
        if (cnt <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && mCurrentTFragment != null) {
                mCurrentTFragment.onBack();
            }
        }
    }

    public void hideKeyboardForCurrentFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyboardAtView(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected void exitFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void setIsTrial(boolean isTrial) {
        getApplicationContext().getSharedPreferences(GLOBAL_SETTINGS, 0)
                .edit().putBoolean(IS_TRIAL, isTrial).commit();
    }

    public boolean getIsTrial() {
        return getApplicationContext().getSharedPreferences(GLOBAL_SETTINGS, 0)
                .getBoolean(IS_TRIAL, true);
    }
}
