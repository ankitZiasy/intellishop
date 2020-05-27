package com.ziasy.haanbaba.intellishopping.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ziasy.haanbaba.intellishopping.Activity.MainActivity;
import com.ziasy.haanbaba.intellishopping.Activity.NotificationActivity;
import com.ziasy.haanbaba.intellishopping.R;


/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String title=null,message=null;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

       // Log.d("data", "From: " + remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d("notification", "From: " + remoteMessage.getData());
        title=remoteMessage.getData().get("title").trim();
        message=remoteMessage.getData().get("message").trim();
        if (message!=null){
            sendNotification(title,message);
        }
    }


    private void sendNotification(String title,String messageBody) {
        Log.e("Message Body", messageBody.toString());
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notification);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(bm)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}