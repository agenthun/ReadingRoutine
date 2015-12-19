package com.agenthun.readingroutine.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmNoiserReciever extends BroadcastReceiver {
    private static final String TAG = "AlarmNoiserReciever";

    public AlarmNoiserReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent serviceIntent = new Intent(context, AlarmNoiserService.class);
        context.startService(serviceIntent);
    }
}
