package com.adivid.zpattendanceadmin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        edit = pref.edit();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        edit.putString("Notification", "yes");
        edit.commit();
        sendNotification(remoteMessage.getNotification().getBody());
    }

    @Override
    public void onNewToken(String s) {
        edit.putString("FCM", s);
        edit.commit();
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SplashScreen.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Third I Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

}
