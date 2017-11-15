package com.teomanyaman.radio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, Radios.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
