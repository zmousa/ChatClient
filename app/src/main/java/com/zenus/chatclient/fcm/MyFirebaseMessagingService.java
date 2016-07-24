package com.zenus.chatclient.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zenus.chatclient.R;
import com.zenus.chatclient.activities.ChatListActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static String NOTIFICATION_TITLE = "title";
    public static String NOTIFICATION_MESSAGE = "message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> map =  remoteMessage.getData();
        if (map != null && map.size() > 0) {
            // Handle sent message for show
            String message = map.get(NOTIFICATION_MESSAGE);
            String title = map.get(NOTIFICATION_TITLE);
            if (message != null && !"".equals(message))
                sendNotification(title == null? "" : title, message);
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, ChatListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}