package com.agenthun.readingroutine.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/13 上午3:01.
 */
public class TransitionHelper {
    public TransitionHelper() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                        boolean includeStatusBar,
                                                                        @Nullable Pair... otherParticipants) {
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        if (otherParticipants != null && !(otherParticipants.length == 1) && otherParticipants[0] == null) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }
}
