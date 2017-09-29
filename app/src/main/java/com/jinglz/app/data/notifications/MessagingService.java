package com.jinglz.app.data.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jinglz.app.R;
import com.jinglz.app.ui.splash.SplashActivity;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private final String NOTIFICATION_TITLE = "title";
    private final String NOTIFICATION_MESSAGE = "message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        final Map<String, String> data = remoteMessage.getData();
        createNotification(this, data);
    }

    /**
     * This method is used to retrieve notification data from {@code data}.
     * title and message will retrieved from {@link Map#get(Object)}.
     * this method is used to push notification by constructing new {@link NotificationCompat.Builder}.
     * it will open {@link SplashActivity} on notification click.
     *
     * @param context to use application specific resources.
     * @param data Map data for retrieving
     */
    private void createNotification(Context context, Map<String, String> data) {
        final String title = data.get(NOTIFICATION_TITLE);
        final String message = data.get(NOTIFICATION_MESSAGE);

        final Intent intent = new Intent(context, SplashActivity.class);
        final PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .setSound(ringtoneUri)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(-1, builder.build());
    }
}
