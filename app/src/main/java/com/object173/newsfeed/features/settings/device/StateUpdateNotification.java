package com.object173.newsfeed.features.settings.device;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.CancelWorkerReceiver;

import androidx.core.app.NotificationCompat;

class StateUpdateNotification {

    private static final String TAG = "StateUpdateNotification";
    private static final int ID = 177;

    private NotificationCompat.Builder mNotification;
    private int mMaxProgress;


    StateUpdateNotification(final Context context, final int maxProgress) {
        mMaxProgress = maxProgress;

        mNotification = new NotificationCompat.Builder(context, NotificationCompat.CATEGORY_PROGRESS)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_update_progress_title))
                .setProgress(mMaxProgress, 0, false)
                .setDeleteIntent(createOnDismissedIntent(context));
    }

    void setCurrentProgress(Context context, int currentProgress) {
        mNotification.setProgress(mMaxProgress, currentProgress, false);
        show(context);
    }

    private void show(final Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(TAG, ID, mNotification.build());
    }

    static void hide(final Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(TAG, ID);
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = CancelWorkerReceiver.createMessage(context, AutoUpdateWorker.WORKER_TAG);
        return PendingIntent.getBroadcast(context.getApplicationContext(), ID, intent, 0);
    }
}
