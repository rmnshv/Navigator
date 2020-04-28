package icst.spbstu.ru.navigatoricst.data.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;

public class AppPreference {

    private static Context mContext;

    private static AppPreference mAppPreference = null;
    private SharedPreferences mSharedPreferences, mSettingsPreferences;
    private SharedPreferences.Editor mEditor;

    public static AppPreference getInstance(Context context){
        if (mAppPreference == null){
            mContext = context;
            mAppPreference = new AppPreference();
        }
        return mAppPreference;
    }

    @SuppressLint("CommitPrefEdits")
    private AppPreference() {
        mSharedPreferences = mContext.getSharedPreferences(AppConstants.APP_PREF_NAME, Context.MODE_PRIVATE);
        mSettingsPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
    }



    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public void setString(String key, String value){
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }


    public boolean isNotificationOn() {
        return mSettingsPreferences.getBoolean(AppConstants.PREF_NOTIFICATION, true);
    }

    public String getTextSize() {
        return mSettingsPreferences.getString(AppConstants.PREF_FONT_SIZE, mContext.getResources().getString(R.string.default_text));
    }

}
