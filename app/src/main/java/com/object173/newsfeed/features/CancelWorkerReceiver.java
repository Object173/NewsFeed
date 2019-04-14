package com.object173.newsfeed.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.object173.newsfeed.features.settings.device.AutoUpdateWorker;
import com.object173.newsfeed.libs.log.LoggerFactory;

import androidx.work.WorkManager;

public class CancelWorkerReceiver extends BroadcastReceiver {

    private static final String ACTION = "com.object173.newsfeed.cancel_worker";
    private static final String KEY_WORKER_TAG = "worker_tag";

    public static Intent createMessage(Context context, String tag) {
        Intent intent = new Intent(context, CancelWorkerReceiver.class);
        intent.setAction(ACTION);
        intent.putExtra(KEY_WORKER_TAG, tag);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!ACTION.equals(intent.getAction())) {
            return;
        }
        String tag = intent.getExtras().getString(KEY_WORKER_TAG);
        LoggerFactory.get(AutoUpdateWorker.class).info("OnReceive " + tag);
        WorkManager.getInstance().cancelAllWorkByTag(tag);
    }
}
