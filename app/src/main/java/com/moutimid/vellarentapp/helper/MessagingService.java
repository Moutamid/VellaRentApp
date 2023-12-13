package com.moutimid.vellarentapp.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fxn.stash.Stash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moutamid.dantlicorp.MainActivity;
import com.moutamid.dantlicorp.Model.NotificationModel;
import com.moutamid.dantlicorp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MessagingService  extends FirebaseMessagingService {

    NotificationModel notificationModel;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
//        Config.AdminToken.setValue(token);
//        getSharedPreferences("SERVICETOKEN", MODE_PRIVATE).edit().putString("token", token).apply();
    }

    private final String ADMIN_CHANNEL_ID = "admin_channel";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        notificationModel = new NotificationModel();
        notificationModel.type = remoteMessage.getData().get("type");
        notificationModel.title = remoteMessage.getData().get("data");
        notificationModel.message = remoteMessage.getData().get("message");
        notificationModel.date = currentDate +" at "+currentTime ;
        Log.d("Notification", notificationModel.type+" | "+notificationModel.title+" | "+notificationModel.message+" notification");
        ArrayList<NotificationModel> notificationModelArrayList = Stash.getArrayList("Notification", NotificationModel.class);
        notificationModelArrayList.add(notificationModel);
        Stash.put("Notification", notificationModelArrayList);
        final Intent intent = new Intent(MessagingService.this, MainActivity.class);
        intent.putExtra("type", remoteMessage.getData().get("type"));
        intent.putExtra("data", remoteMessage.getData().get("data"));
        intent.putExtra("message", remoteMessage.getData().get("message"));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MessagingService.this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_foreground);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (remoteMessage.getData().get("data") != null) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MessagingService.this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message") + "\n" + remoteMessage.getData().get("data"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);

            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(getResources().getColor(R.color.app_color));
            }

            notificationManager.notify(notificationID, notificationBuilder.build());
        }
        else
        {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MessagingService.this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message") + "\n" + remoteMessage.getData().get("data"))
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);

            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(getResources().getColor(R.color.app_color));
            }

            notificationManager.notify(notificationID, notificationBuilder.build());

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "message";
        String adminChannelDescription = "message received";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
//        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
