package com.sae.raz.model;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneTLM;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneURL;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;
import com.sae.raz.AppConstant;
import com.sae.raz.database.DBManager;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BeaconModel {
	public static final int TYPE_MULTI = 1;
	public static final int TYPE_UPPER = 2;
	public static final int TYPE_LOWER = 3;
	public static final int TYPE_ARMS = 4;


	public int beaconType;
	public String macAddress = "";
	public String uuid = "";
	public int major;
	public int minor;

	public int equType;
	public int beaconNumber;
	public String beaconTitle;
	public String webUrl;
	public long lastTime;
	public int xPosition;
	public int yPosition;

	public int txPower;
	public int rssi;
	public double distance;

	public BeaconModel() {
		beaconType = AppConstant.TYPE_IBEACON;
		macAddress = "";
		uuid = "";
		major = 0;
		minor = 0;

		equType = 0;
		beaconNumber = -1;
		beaconTitle = "";
		webUrl = "";
		lastTime = -1;
		xPosition = 0;
		yPosition = 0;

		txPower = 0;
		rssi = 0;
		distance = 1000;
	}

	public static BeaconModel makeNewModel(ScanResult newResult) {
		final BeaconModel newModel = new BeaconModel();

		newModel.macAddress = newResult.getDevice().getAddress();
		newModel.rssi = newResult.getRssi();

		List<ADStructure> structures = ADPayloadParser.getInstance().parse(newResult.getScanRecord().getBytes());
		for (ADStructure structure : structures)
		{
			if (structure instanceof EddystoneUID)
			{
				newModel.beaconType = AppConstant.TYPE_EDDYSTONE_UID;
			}
			else if (structure instanceof EddystoneURL)
			{
				newModel.beaconType = AppConstant.TYPE_EDDYSTONE_URL;
			}
			else if (structure instanceof EddystoneTLM)
			{
				newModel.beaconType = AppConstant.TYPE_EDDYSTONE_TLM;
			}
			else if (structure instanceof IBeacon)
			{
				newModel.beaconType = AppConstant.TYPE_IBEACON;
				// iBeacon
				IBeacon iBeacon = (IBeacon)structure;
				// (1) Proximity UUID
				newModel.uuid = iBeacon.getUUID().toString();
				// (2) Major number
				newModel.major = iBeacon.getMajor();
				// (3) Minor number
				newModel.minor = iBeacon.getMinor();
				// (4) Tx Power
				newModel.txPower = iBeacon.getPower();
			}
		}

		newModel.lastTime = Calendar.getInstance().getTimeInMillis();
		if (newModel.beaconType == AppConstant.TYPE_IBEACON) {

			if (DBManager.getInstance().isBeaconExist(newModel)) {
				newModel.equType = DBManager.getInstance().getBeaconType(newModel);
				newModel.beaconNumber = DBManager.getInstance().getBeaconNumber(newModel);
				newModel.xPosition = DBManager.getInstance().getXposition(newModel);
				newModel.yPosition = DBManager.getInstance().getYposition(newModel);
				newModel.beaconTitle = DBManager.getInstance().getBeaconTitle(newModel);
				newModel.webUrl = DBManager.getInstance().getWebUrl(newModel);
				newModel.lastTime = DBManager.getInstance().getLastTime(newModel);

				newModel.distance = calculateDistance(newModel.rssi, newModel.txPower);
				return newModel;
			}
		}

		return null;
	}

	private static double calculateDistance(int rssi, int measuredPower) {
		if(rssi == 0) {
			return -1.0D;
		} else {
			double ratio = (double)rssi / (double)measuredPower;
			double rssiCorrection = 0.96D + Math.pow((double)Math.abs(rssi), 3.0D) % 10.0D / 150.0D;
			return ratio <= 1.0D?Math.pow(ratio, 9.98D) * rssiCorrection:(0.103D + 0.89978D * Math.pow(ratio, 7.71D)) * rssiCorrection;
		}
	}

	public static void QuickSort(ArrayList<BeaconModel> modelList, int nStart, int nEnd)
	{
		if(nStart < 0)
			return;
		if(nEnd <= nStart)
			return;
		if(modelList.size() < nEnd)
			return;

		BeaconModel model_Base = modelList.get(nStart);
		BeaconModel model_Left = null;
		BeaconModel model_Right = null;
		int iLeft = nStart;
		int iRight = nEnd;

		while(iRight > iLeft)
		{
			do
			{
				iLeft++;
				if(iLeft == modelList.size())
					break;
				model_Left = modelList.get(iLeft);
			} while(model_Base.distance > model_Left.distance);

			do
			{
				iRight--;
				if(iRight < 0)	break;
				model_Right = modelList.get(iRight);
			} while( model_Right.distance > model_Base.distance);

			if(iRight > iLeft)
			{
				modelList.set(iLeft, model_Right);
				modelList.set(iRight, model_Left);
			}
			else
			{
				modelList.set(nStart, model_Right);
				modelList.set(iRight, model_Base);

				QuickSort(modelList, nStart, iRight);
				QuickSort(modelList, iRight+1, nEnd);
			}
		}
	}
}
