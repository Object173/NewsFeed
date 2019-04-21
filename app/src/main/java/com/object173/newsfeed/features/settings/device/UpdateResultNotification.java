package com.object173.newsfeed.features.settings.device;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.base.domain.model.pref.NotificationConfig;
import com.object173.newsfeed.features.main.presentation.MainActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

class UpdateResultNotification {

    private static final String TAG = "UpdateResultNotification";
    private static final int ID = 173;

    static void show(final Context context, NotificationConfig config,  int updatedFeed, int newNews) {

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

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(TAG, ID, builder.build());
    }
}
