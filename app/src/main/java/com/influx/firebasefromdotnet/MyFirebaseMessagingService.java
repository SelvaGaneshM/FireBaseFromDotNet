package com.influx.firebasefromdotnet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by Selva Ganesh on 28-11-2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        String tittle_get = remoteMessage.getData().get("title");
        String message_get = remoteMessage.getData().get("message");
        String image_get = remoteMessage.getData().get("image");

        shownotification(tittle_get, message_get, image_get);
    }

    private void shownotification(String tittle_get, String message_get, String image_get) {

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (image_get != null && !image_get.equalsIgnoreCase("")) {


            Bitmap bitmap = getBitmapFromURL(image_get);

            NotificationCompat.Builder notificationBuilder1 = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(tittle_get)
                    .setContentText(message_get)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setLights(Color.BLUE, 1, 1)
                    .setSound(defaultSoundUri)
                    .setLargeIcon(bitmap)
                    .setContentIntent(pendingIntent);


            NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
            s.setSummaryText(tittle_get);
            notificationBuilder1.setStyle(s);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m /* ID of notification */, notificationBuilder1.build());

        } else {
            NotificationCompat.Builder notificationBuilder2 = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(tittle_get)
                    .setContentText(message_get)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setLights(Color.BLUE, 1, 1)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m /* ID of notification */, notificationBuilder2.build());
        }


    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
