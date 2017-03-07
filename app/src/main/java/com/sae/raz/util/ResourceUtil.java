package com.sae.raz.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Surface;

import com.sae.raz.GymTrackApp;


@SuppressWarnings("deprecation")
public class ResourceUtil {
	public static String RES_DIRECTORY = Environment.getExternalStorageDirectory() + "/zapporoo/Veew/";
	public static String FILE_EXTENSION = ".veew";
	
	/*
	 * Resource directory
	 */
	public static String getResourceDirectory() {
		String tempDirPath = RES_DIRECTORY;
		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();

		return tempDirPath;
	}
	
	public static String getDownloadDirectory() {
		String tempDirPath = RES_DIRECTORY + "Donwload/";
		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();

		return tempDirPath;
	}
	
	public static String getDownloadFilePath(String fileName) {
		return getDownloadDirectory() + fileName + FILE_EXTENSION;
	}
	
	/*
	 * File
	 */
	public static String getCapturedImageFilePath() {
		String tempDirPath = RES_DIRECTORY;
		String tempFileName = "camera.jpg";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + tempFileName;
	}
	public static String getAvatarFilePath() {
		String tempDirPath = RES_DIRECTORY;
		String tempFileName = "avatar.jpg";

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + tempFileName;
	}
	
	/*
	 * Post
	 */
	// photo
	private static String mPhotoFileExtension = "jpg";
	public static void setPhotoExtension(String fileExtension) {
		mPhotoFileExtension = fileExtension;
	}
	public static String getCaptureImageFilePath(Context context) {
		return getResourceDirectory() + "post_image."+mPhotoFileExtension;
	}

	// video
	private static String mVideoFileExtension = "mp4";
	public static void setVideoExtension(String fileExtension) {
		mVideoFileExtension = fileExtension;
	}
	
	public static String getVideoExtension() {
		return mVideoFileExtension;
	}

	public static String getCaptureVideoFilePath(Context context) {
		return getResourceDirectory() + "post_video." + mVideoFileExtension;
	}

	public static String getTrimedVideoFilePath(Context context) {
		return getResourceDirectory() + "post_trimed_video." + mVideoFileExtension;
	}
	
	public static String getVideoThumbnailFilePath(Context context) {
		return getResourceDirectory() + "video_thumbnail.bmp";
	}

	/*
	 * Device orientation
	 */
	public static int getRotationAngle(Activity mContext, int cameraId) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		return result;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		BitmapFactory.decodeFile(path, options);
		int reqHeight = reqWidth * options.outHeight / options.outWidth;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width <= reqWidth && height <= reqHeight)
			return 1;

		float widthRatio = (float)width / reqWidth;
		float heightRatio = (float)height / reqHeight;
		float maxRatio = Math.max(widthRatio, heightRatio);
		inSampleSize = (int)(maxRatio + 0.5);
		return inSampleSize;
	}

	public static Bitmap decodeUri(Context context, Uri selectedImage, int reqSize) throws FileNotFoundException {
		// Decode image size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = options.outWidth, height_tmp = options.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < reqSize
					|| height_tmp / 2 < reqSize) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		options = new BitmapFactory.Options();
		options.inSampleSize = scale;
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
	}
	/*
    * Save bitmap on given file path on sdcard
    */
	public static void saveBitmapToSdcard(Bitmap bitmap, String dirPath) {
		File tempFile = new File(dirPath);
		if (tempFile.exists())
			tempFile.delete();

		try {
			FileOutputStream fOut = new FileOutputStream(tempFile);

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap resizeIcon(int resId, int dpMaxSize) {
		int maxSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpMaxSize, GymTrackApp.getContext().getResources().getDisplayMetrics());
		Bitmap orgBitmap = BitmapFactory.decodeResource(GymTrackApp.getContext().getResources(), resId);
		Bitmap resizedBitmap = getScaledBitmap(orgBitmap, maxSize);
		orgBitmap.recycle();
		return resizedBitmap;
	}

	private static Bitmap getScaledBitmap(Bitmap orgBmp, int maxSize) {
		float width = orgBmp.getWidth();
		float height = orgBmp.getHeight();
		if (width > height) {
			height = maxSize * (height / width);
			width = maxSize;
		} else {
			width = maxSize * (width / height);
			height = maxSize;
		}
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(orgBmp, (int)width, (int)height, false);
		return resizedBitmap;
	}

	/**
	 * Checks whether the Bluetooth adapter is enabled.
	 */
	public static boolean isBluetoothEnabled() {
		BluetoothAdapter bluetoothAdapter = null;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
			BluetoothManager bluetoothManager = (BluetoothManager) GymTrackApp.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
			bluetoothAdapter = bluetoothManager.getAdapter();
		} else{
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
	}
}
