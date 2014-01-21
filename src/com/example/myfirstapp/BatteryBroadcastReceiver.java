package com.example.myfirstapp;

import com.example.myfirstapp.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Vibrator;

public class BatteryBroadcastReceiver extends BroadcastReceiver {
	public BatteryBroadcastReceiver() {
	}

	@Override
    public void onReceive(Context context, Intent intent) {
		String highLimitField = context.getResources().getString(R.string.high_level);
		String lowLimitField = context.getResources().getString(R.string.low_level);
		String limitFileName = context.getResources().getString(R.string.limit_file_name);
	    SharedPreferences settings = context.getSharedPreferences(limitFileName, 0);

		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
	    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	    int batteryPct = (int) (100 * level / (float) scale);
		int highLimit = settings.getInt(highLimitField, 100);
		int lowLimit = settings.getInt(lowLimitField, 0);

        int plugged = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPluggedIn = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        if(((batteryPct > highLimit) && isPluggedIn) || (batteryPct < lowLimit)){
		    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
	    }

		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
		    Intent intent2 = new Intent(context, BatteryBroadcastReceiver.class);
		    intent2.setAction("com.myapp.ALARM_EVENT");
		    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
		    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
			        AlarmManager.INTERVAL_FIFTEEN_MINUTES / 3,
			        AlarmManager.INTERVAL_FIFTEEN_MINUTES / 3,
			        alarmIntent);
		}
    }
}
