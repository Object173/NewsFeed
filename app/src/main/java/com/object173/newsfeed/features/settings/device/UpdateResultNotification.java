package com.object173.newsfeed.features.settings.device;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.feedlist.presentation.FeedListActivity;
import com.object173.newsfeed.features.settings.device.model.NotificationConfig;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

class UpdateResultNotification {

    private static final String TAG = "UpdateResultNotification";
    private static final int ID = 173;

    static void show(final Context context, int updatedFeed, int newNews) {
        final NotificationConfig config = getNotificationConfig(context);

        if(!config.enabled || newNews == 0) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationCompat.CATEGORY_MESSAGE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_result_update_title))
                .setContentText(context.getString(R.string.notification_result_update_content, updatedFeed, newNews))
                .setAutoCancel(true);

        switch (config.type) {
            case VIBRATION_ONLY:
                builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
                break;
            case SOUND_ONLY:
                builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                break;
            case SOUND_AND_VIBRATION:
                builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_SOUND);
                break;
        }

        Intent resultIntent = new Intent(context, FeedListActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(FeedListActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(TAG, ID, builder.build());
    }

    private static NotificationConfig getNotificationConfig(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = preferences.getBoolean(context.getString(R.string.pref_key_notification_enabled),
                context.getResources().getBoolean(R.bool.pref_notification_enabled_default));

        String notificationTypeStr = preferences.getString(context.getString(R.string.pref_key_notification_type),
                context.getString(R.string.pref_notification_type_default));

        NotificationConfig.Type notificationType = NotificationConfig.Type
                .values()[Integer.parseInt(notificationTypeStr)];

        return new NotificationConfig(isEnabled, notificationType);
    }
}
