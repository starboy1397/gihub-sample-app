package com.ravi.github;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";


    //receives notification messages
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        Log.v(TAG, "From: " + message.getFrom());

        //check for data payload in the message
        if (!message.getData().isEmpty()) {
            Log.v(TAG, "Message data payload: " + message.getData());
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        //check for notification payload in the message
        if (message.getNotification() != null) {
            Log.v(TAG, "Notification Title: " + message.getNotification().getTitle());
            Log.v(TAG, "Notification Body: " + message.getNotification().getBody());
            sendNotification(message.getNotification().getBody());
        }


    }




    // to retrieve a new registration token whenever its generated
    @Override
    public void onNewToken(@NonNull String token) {

        Log.d(TAG, "onNewToken: " + token);
        sendRegistrationToServer(token);
    }



    private void scheduleJob() {

        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();

    }

    private void handleNow() {

        Log.v(TAG, "Short lived task is done!");
    }

    private void sendRegistrationToServer(String token) {

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, RepositoryListActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.github_notification_logo)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel Human Readable Title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());


    }
}
