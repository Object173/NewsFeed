package com.object173.newsfeed.features.base.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.model.pref.CacheConfig;
import com.object173.newsfeed.features.base.model.pref.NotificationConfig;

public class PreferenceDataSourceImpl implements PreferenceDataSource {

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    private static Integer mCurrentTheme;

    public PreferenceDataSourceImpl(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public AutoUpdateConfig getAutoUpdateConfig() {
        boolean isEnabled = mSharedPreferences.getBoolean(mContext.getString(R.string.pref_key_auto_refresh_enabled),
                mContext.getResources().getBoolean(R.bool.pref_auto_refresh_default));

        int updateInterval = mSharedPreferences.getInt(mContext.getString(R.string.pref_key_refresh_period),
                mContext.getResources().getInteger(R.integer.cache_frequency_default));

        boolean isWifiOnly = mSharedPreferences.getBoolean(mContext.getString(R.string.pref_key_wifi_only),
                mContext.getResources().getBoolean(R.bool.pref_wifi_only_default));

        return new AutoUpdateConfig(isEnabled, updateInterval, isWifiOnly);
    }

    @Override
    public NotificationConfig getNotificationConfig() {
        boolean isEnabled = mSharedPreferences.getBoolean(mContext.getString(R.string.pref_key_notification_enabled),
                mContext.getResources().getBoolean(R.bool.pref_notification_enabled_default));

        String notificationTypeStr = mSharedPreferences.getString(mContext.getString(R.string.pref_key_notification_type),
                mContext.getString(R.string.pref_notification_type_default));

        NotificationConfig.Type notificationType = NotificationConfig.Type
                .values()[Integer.parseInt(notificationTypeStr)];

        return new NotificationConfig(isEnabled, notificationType);
    }

    @Override
    public CacheConfig getCacheConfig() {
        int cacheSize = mSharedPreferences.getInt(mContext.getString(R.string.pref_key_cache_size),
                mContext.getResources().getInteger(R.integer.cache_size_default));
        int cacheFrequency = mSharedPreferences.getInt(mContext.getString(R.string.pref_key_cache_frequency),
                mContext.getResources().getInteger(R.integer.cache_frequency_default));

        return new CacheConfig(cacheSize, cacheFrequency);
    }

    @Override
    public int getCurrentThemeId() {
        if(mCurrentTheme == null) {
            return updateCurrentThemeId();
        }

        return mCurrentTheme;
    }

    @Override
    public int updateCurrentThemeId() {
        String theme = mSharedPreferences.getString(mContext.getString(R.string.pref_key_current_theme),
                mContext.getString(R.string.current_theme_main));

        if(theme.equals(mContext.getString(R.string.current_theme_dark))) {
            mCurrentTheme = R.style.DarkTheme;
        }
        else {
            mCurrentTheme = R.style.MainTheme;
        }
        return mCurrentTheme;
    }
}
