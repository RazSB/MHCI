package com.sae.raz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sae.raz.service.BeaconMonitoringService;


public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BootCompleteReceiver", "call onReceive()");
		
        if ((intent.getAction() != null) && (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)))
        {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

			Intent beaconService = new Intent(context, BeaconMonitoringService.class);
			context.startService(beaconService);
        }
	}
}
