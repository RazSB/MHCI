package com.sae.raz.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sae.raz.AppConstant;
import com.sae.raz.GymTrackApp;
import com.sae.raz.R;
import com.sae.raz.database.DBManager;
import com.sae.raz.model.BeaconModel;
import com.sae.raz.ui.activity.MainActivity;
import com.sae.raz.ui.fragment.DiscoverFragment;
import com.sae.raz.util.ResourceUtil;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;


public class BeaconMonitoringService extends Service {
	private static String TAG = "BeaconMonitoringService";

	private Handler mHandler = new Handler();
	private boolean mIsScanning = false;

	ArrayList<BeaconModel> mNewModelList = new ArrayList<BeaconModel>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand()");

		Log.e("BeaconAttitude", "BeaconMonitoringService::onStartCommand");
		scanPeriodically();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy()");
		startScan();
		super.onDestroy();
	}

	private void scanPeriodically() {
		if (mIsScanning)
			return;
		Log.e("BeaconAttitude", "BeaconMonitoringService::scanPeriodically");
		startScan();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mIsScanning) {
					stopScan();
					long sleepTimeout = AppConstant.SCAN_NOTIFICATION_PERIOD - AppConstant.SCAN_DURATION;
					if (sleepTimeout < 0)
						sleepTimeout = 100;

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							scanPeriodically();
						}
					}, sleepTimeout);
				}
			}
		}, AppConstant.SCAN_DURATION);
	}

	private void startScan() {
		Log.i(TAG, "Inside srart running");
		final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
		final ScanSettings settings = new ScanSettings.Builder()
				.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(1000).setUseHardwareBatchingIfSupported(false).build();
		final List<ScanFilter> filters = new ArrayList<>();
		filters.add(new ScanFilter.Builder().setServiceUuid(null).build());
		if (ResourceUtil.isBluetoothEnabled())
			scanner.startScan(filters, settings, scanCallback);

		mIsScanning = true;
	}

	/**
	 * Stop scan if user tap Cancel button
	 */
	private void stopScan() {
		mHandler.removeCallbacksAndMessages(null);
		final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
		if (scanner != null)
			scanner.stopScan(scanCallback);

		mIsScanning = false;
	}

	private ScanCallback scanCallback = new ScanCallback() {
		@Override
		public void onScanResult(final int callbackType, final ScanResult result) {
			// do nothing
		}

		@Override
		public void onBatchScanResults(final List<ScanResult> beaconResults) {
			if (DiscoverFragment.mIsOpened)
				return;

			mNewModelList.clear();
			for (ScanResult scanResult : beaconResults) {
				final BeaconModel newModel = BeaconModel.makeNewModel(scanResult);
				if (newModel != null) {
					long lastTime = DBManager.getInstance().getLastTime(newModel);
					long curTime = Calendar.getInstance().getTimeInMillis();
					long elapsedTime = curTime - lastTime;
					if (elapsedTime > AppConstant.BEACON_ELAPSED_TIME && !DiscoverFragment.mIsOpened) {
						showBeaconNotification(newModel);
						DBManager.getInstance().updateLastTime(newModel);
					}
				}
			}
		}

		@Override
		public void onScanFailed(final int errorCode) {
			// should never be called
		}
	};

	public void showBeaconNotification(BeaconModel beaconModel) {

		// get notification sound
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// vibrate
		long[] pattern = {80, 80, 80};

		NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(GymTrackApp.getContext())
				.setSmallIcon(R.mipmap.noti_icon)
				.setContentTitle(GymTrackApp.getContext().getString(R.string.app_name))
				.setContentText(beaconModel.beaconTitle)
				.setVibrate(pattern)
				.setSound(null)
				.setAutoCancel(true);

		// open the beacon detail page when click the notification
		Intent resultIntent = new Intent(GymTrackApp.getContext(), MainActivity.class);
		resultIntent.putExtra(AppConstant.EK_FROM_NOTIFICATION, true);
		resultIntent.putExtra(AppConstant.EK_BEACON_NUMBER, beaconModel.beaconNumber);
		resultIntent.putExtra(AppConstant.EK_BEACON_TITLE, beaconModel.beaconTitle);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


		if (resultIntent != null) {
			PendingIntent resultPendingIntent = PendingIntent.getActivity(
					GymTrackApp.getContext(),
					beaconModel.beaconNumber,
					resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			notiBuilder.setContentIntent(resultPendingIntent);
		}

		// show notification
		NotificationManager notificationManager = (NotificationManager) GymTrackApp.getContext().getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(beaconModel.beaconNumber, notiBuilder.build());
	}
}
