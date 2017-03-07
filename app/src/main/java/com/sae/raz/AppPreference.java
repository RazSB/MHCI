package com.sae.raz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class AppPreference {
	public static final String PREF_NAME = "VEEW_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;

	public static class KEY {
		// sign in
		public static final String SIGN_IN_AUTO = "SIGN_IN_AUTO";

		public static final String SIGN_IN_EMAIL = "SIGN_IN_EMAIL";
		public static final String SIGN_IN_PASSWORD = "SIGN_IN_PASSWORD";
		public static final String SIGN_IN_FULLNAME = "SIGN_IN_FULLNAME";
	}

	public static void writeBoolean(String key, boolean value) {
		getEditor().putBoolean(key, value).commit();
	}

	public static boolean readBoolean(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	public static void writeInteger(String key, int value) {
		getEditor().putInt(key, value).commit();

	}

	public static int readInteger(String key, int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	public static void writeString(String key, String value) {
		getEditor().putString(key, value).commit();

	}

	public static String readString(String key, String defValue) {
		return getPreferences().getString(key, defValue);
	}

	public static void writeFloat(String key, float value) {
		getEditor().putFloat(key, value).commit();
	}

	public static float readFloat(String key, float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	public static void writeLong(String key, long value) {
		getEditor().putLong(key, value).commit();
	}

	public static long readLong(String key, long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	public static SharedPreferences getPreferences() {
		Context context = GymTrackApp.getContext();
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor() {
		return getPreferences().edit();
	}

	public static void removeKey(String strKey) {
		getEditor().remove(strKey);
		getEditor().commit();
	}
}