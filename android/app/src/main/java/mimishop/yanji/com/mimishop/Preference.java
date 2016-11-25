package mimishop.yanji.com.mimishop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

	private static final String FILE_NAME = "mimishop.pref";

    public static final String KEY_PUSH_ENVELOP = "key_push_envelop";
    public static final String KEY_PUSH_COMMENT = "key_push_comment";
    public static final String KEY_PUSH_TAG = "key_push_tag";

	private static Preference mInstance;

	public static Preference getInstance() {
		if (null == mInstance) {
			mInstance = new Preference();
		}
		return mInstance;
	}

	public void putSharedPreference(Context context, String key, String value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putSharedPreference(Context context, String key, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putSharedPreference(Context context, String key, int value) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public String getSharedPreference(Context context, String key,
			String defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getString(key, defaultValue);
	}

	public int getSharedPreference(Context context, String key, int defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getInt(key, defaultValue);
	}

	public boolean getSharedPreference(Context context, String key,
			boolean defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Activity.MODE_PRIVATE);
		return pref.getBoolean(key, defaultValue);
	}

}