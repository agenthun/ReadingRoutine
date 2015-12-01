package com.agenthun.readingroutine.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activities.ReadingActivity;
import com.agenthun.readingroutine.activities.ShoppingActivity;
import com.agenthun.readingroutine.transitionmanagers.TFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends TFragment {
    public static final int READING_FRAGMENT = 1;
    public static final int ROUTINES_FRAGMENT = 2;
    public static final int SHOPPING_FRAGMENT = 3;
    public static final int ABOUT_FRAGMENT = 4;
    public static final int NOTES_FRAGMENT = 5;
    public static final int SETTINGS_FRAGMENT = 6;

    @InjectView(R.id.ic_management)
    ImageView managementBadge;
    @InjectView(R.id.ic_routines)
    ImageView routinesBadge;
    @InjectView(R.id.ic_shopping)
    ImageView shoppingBadge;
    @InjectView(R.id.ic_about)
    ImageView aboutBadge;
    @InjectView(R.id.ic_notes)
    ImageView notesBadge;
    @InjectView(R.id.ic_settings)
    ImageView settingsBadge;

    private OnMenuInteractionListener mListener;
    private boolean pendingIntroAnimation;

    public MenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMenuInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMenuInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLeave() {
        super.onLeave();
        mListener = null;
    }

    @Override
    public void onBack() {
        super.onBack();
        onAttach(getActivity());
    }

    @OnClick(R.id.ic_management)
    public void onReadingClick() {
        if (mListener != null) {
            mListener.onFragmentInteraction(READING_FRAGMENT);
            startActivity(new Intent(getContext(), ReadingActivity.class));
        }
    }

    @OnClick(R.id.ic_routines)
    public void onRoutinesClick() {
        if (mListener != null) {
            mListener.onFragmentInteraction(ROUTINES_FRAGMENT);
            getContext().pushFragmentToBackStack(RoutinesFragment.class, null);
        }
    }

    @OnClick(R.id.ic_shopping)
    public void onShoppingClick() {
        if (mListener != null) {
            mListener.onFragmentInteraction(SHOPPING_FRAGMENT);
            startActivity(new Intent(getContext(), ShoppingActivity.class));
        }
    }

    @OnClick(R.id.ic_about)
    public void onAboutClick() {
        if (mListener != null) {
            mListener.onFragmentInteraction(ABOUT_FRAGMENT);
            getContext().pushFragmentToBackStack(AboutFragment.class, null);
        }
    }

    @OnClick(R.id.ic_notes)
    public void onNotesClick() {
        if (mListener != null) {
            mListener.onFragmentInteraction(NOTES_FRAGMENT);
            getContext().pushFragmentToBackStack(NotesFragment.class, null);
        }
    }

    @OnClick(R.id.ic_settings)
    public void onSettingsClick() {
        if (mListener != null) {
            mListener.onFragmentInteraction(SETTINGS_FRAGMENT);
            getContext().pushFragmentToBackStack(SettingsFragment.class, null);
        }
    }

    public interface OnMenuInteractionListener {
        void onFragmentInteraction(int fragmentIndex);
    }
}
