package com.nilanshi.nigam.personalassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

/**
 * Created by HP on 29-Sep-17.
 */

public class MyIntent {
    Context context;

    public MyIntent(Context context) {
        this.context = context;
    }

/*context is used as intent works for present class*/
    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
