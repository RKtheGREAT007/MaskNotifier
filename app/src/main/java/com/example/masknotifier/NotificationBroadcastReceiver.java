package com.example.masknotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private DBHelper dbHelper;
    private static final String TAG = "NotificationBroadcastReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String whichAction = intent.getAction();

        dbHelper = new DBHelper(context);
        if ("okay".equals(whichAction)||"forgot".equals(whichAction)) {

            if("okay".equals(whichAction)) {

//                dbHelper.deleteLast();
                if(!dbHelper.insertHistory("Okay", Calendar.getInstance().getTime().toString())) {
                    Log.d(TAG, "sendHighPriorityNotification: Failed to add data");
                }
            }
            else{
//                dbHelper.deleteLast();
                if(!dbHelper.insertHistory("Forgot", Calendar.getInstance().getTime().toString())) {
                    Log.d(TAG, "sendHighPriorityNotification: Failed to add data");
                }
            }

            NotificationManagerCompat.from(context).cancel(1000);

        }

    }
}
