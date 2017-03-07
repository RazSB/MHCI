package com.sae.raz.ui.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jiahuan.svgmapview.SVGMapView;
import com.jiahuan.svgmapview.SVGMapViewListener;
import com.sae.raz.AppConstant;
import com.sae.raz.GymTrackApp;
import com.sae.raz.R;
import com.sae.raz.database.DBManager;
import com.sae.raz.helper.AssetsHelper;
import com.sae.raz.model.BeaconModel;
import com.sae.raz.overlay.BeaconMarker;
import com.sae.raz.overlay.UserMarker;
import com.sae.raz.ui.activity.MainActivity;
import com.sae.raz.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;


public class DiscoverFragment extends BaseFragment {

    private final static int REQUEST_PERMISSION_REQ_CODE = 76; // any 8-bit number
    private static final int REQUEST_ENABLE_BLUETOOTH = 77;

    private static final String TAG = DiscoverFragment.class.getSimpleName();
    private static DiscoverFragment fragment = null;

    // UI
    private SVGMapView mapView;

    // Data
    ArrayList<BeaconModel> mBeaconModelList = new ArrayList<BeaconModel>();

    private Handler mHandler = new Handler();
    private boolean mIsScanning = false;


    public static boolean mIsOpened = false;

    public static DiscoverFragment getInstance() {
        if (fragment == null)
            fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mapView = (SVGMapView) view.findViewById(R.id.svg_mapview);
        mapView.registerMapViewListener(new SVGMapViewListener() {
            @Override
            public void onMapLoadComplete() {
                mapView.getController().setRotationGestureEnabled(false);
                mapView.refresh();
            }

            @Override
            public void onMapLoadError() {

            }

            @Override
            public void onGetCurrentMap(Bitmap bitmap) {

            }
        });

        mapView.loadMap(AssetsHelper.getContent(mActivity, "gym_map.svg"));
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsOpened = true;
        mapView.onResume();

        if (ResourceUtil.isBluetoothEnabled()) {
            scanPeriodically();
        } else {
            enableBluetooth();
        }
    }

    @Override
    public void onPause() {
        mIsOpened = false;
        stopScan();
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Tries to start Bluetooth adapter.
     */
    private void enableBluetooth() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    private void showPermissionWarning() {
        Toast.makeText(mActivity, R.string.permission_denied, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                mIsOpened = true;
                if (resultCode == RESULT_OK) {
                    UpdateUI();
                    scanPeriodically();
                } else
                    mActivity.finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have been granted the Manifest.permission.ACCESS_COARSE_LOCATION permission. Now we may proceed with scanning.
                    scanPeriodically();
                } else {
                    showPermissionWarning();
                }
                break;
            }
        }
    }


    private void scanPeriodically() {
        if (mIsScanning || !mIsOpened)
            return;
        Log.e("BeaconAttitude", "HomeFragment::scanPeriodically0");
        startScan();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("BeaconAttitude", "HomeFragment::scanPeriodically1");
                if (mIsScanning) {
                    Log.e("BeaconAttitude", "HomeFragment::scanPeriodically2");
                    stopScan();
                    long sleepTimeout = AppConstant.SCAN_HOME_PERIOD - AppConstant.SCAN_DURATION;
                    if (sleepTimeout < 0)
                        sleepTimeout = 100;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("BeaconAttitude", "HomeFragment::scanPeriodically3");
                            scanPeriodically();
                        }
                    }, sleepTimeout);
                }
            }
        }, AppConstant.SCAN_DURATION);
    }

    private void startScan() {
        Log.i(TAG, "Inside srart running");
        // Since Android 6.0 we need to obtain either Manifest.permission.ACCESS_COARSE_LOCATION or Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
        // Bluetooth LE devices. This is related to beacons as proximity devices.
        // On API older than Marshmallow the following code does nothing.
        if (ContextCompat.checkSelfPermission(GymTrackApp.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // When user pressed Deny and still wants to use this functionality, show the rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                return;
            }

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }

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
            mBeaconModelList.clear();
            for (ScanResult scanResult : beaconResults) {
                BeaconModel newModel = BeaconModel.makeNewModel(scanResult);
                if (newModel != null)
                    mBeaconModelList.add(newModel);
            }

            UpdateUI();
        }

        @Override
        public void onScanFailed(final int errorCode) {
            // should never be called
        }
    };

    private void UpdateUI() {
        // sort by distance
        BeaconModel.QuickSort(mBeaconModelList, 0, mBeaconModelList.size());

        int count = mapView.getOverLays().size();
        if (count == 0)
            return;
        for (int i = count - 1; i > 0; i--)
            mapView.getOverLays().remove(i);

        if (mBeaconModelList.size() > 0) {
            if (mBeaconModelList.size() >= 3) {
                Point pointA = new Point(mBeaconModelList.get(0).xPosition, mBeaconModelList.get(0).yPosition);
                Point pointB = new Point(mBeaconModelList.get(1).xPosition, mBeaconModelList.get(1).yPosition);
                Point pointC = new Point(mBeaconModelList.get(2).xPosition, mBeaconModelList.get(2).yPosition);

                Point point = getUserLocation(pointA, pointB, pointC,
                        mBeaconModelList.get(0).distance * AppConstant.RATIO_DIS,
                        mBeaconModelList.get(1).distance * AppConstant.RATIO_DIS,
                        mBeaconModelList.get(2).distance * AppConstant.RATIO_DIS);
                mapView.getOverLays().add(new UserMarker(mapView, point.x, point.y));
            }
        }

        for (BeaconModel model : MainActivity.allBeacons) {
            if (isDetected(model))
                mapView.getOverLays().add(new BeaconMarker(mapView, model, true, mActivity));
            else
                mapView.getOverLays().add(new BeaconMarker(mapView, model, false, mActivity));
        }

        mapView.refresh();
    }

    private boolean isDetected(BeaconModel beaconModel) {
        for (BeaconModel model : mBeaconModelList) {
            if (beaconModel.beaconNumber == model.beaconNumber)
                return true;
        }

        return false;
    }

    private Point getUserLocation (Point locationA, Point locationB, Point locationC, double distanceA, double distanceB, double distanceC) {
        // Location of user.
        double xUser, yUser;

        // Calculate location of user using following trilateration algorithm.
        // xA/xB/xC and disA/disB/disC are each location of beacon A/B/C and distance from user to each beacon.
        // pow((xUser - xA), 2) + pow((yUser - yA), 2) = pow(distA, 2)
        // pow((xUser - xB), 2) + pow((yUser - yB), 2) = pow(distB, 2)
        // pow((xUser - xC), 2) + pow((yUser - yC), 2) = pow(distC, 2)

        double temp1, temp2, temp3;
        temp1 = Math.pow(distanceA, 2) - Math.pow(distanceB, 2) - Math.pow(locationA.x, 2) - Math.pow(locationA.y, 2) + Math.pow(locationB.x, 2) + Math.pow(locationB.y, 2);
        temp2 = Math.pow(distanceB, 2) - Math.pow(distanceC, 2) - Math.pow(locationB.x, 2) - Math.pow(locationB.y, 2) + Math.pow(locationC.x, 2) + Math.pow(locationC.y, 2);

        xUser = (temp1 * (locationC.y - locationB.y) - temp2 * (locationB.y - locationA.y)) / (2 * ((locationB.x - locationA.x) * (locationC.y - locationB.y) - (locationC.x - locationB.x) * (locationB.y - locationA.y)));
        yUser = (temp1 - 2 * xUser * (locationB.x - locationA.x)) / (2 * (locationB.y - locationA.y));

        temp3 = (temp2 - 2 * xUser * (locationC.x - locationB.x)) / (2 * (locationC.y - locationB.y));

        yUser = (yUser + temp3) / 2;

        return new Point((int)xUser, (int)yUser);
    }
}
