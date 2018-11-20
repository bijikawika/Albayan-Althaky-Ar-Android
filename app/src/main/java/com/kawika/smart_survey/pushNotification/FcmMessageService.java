package com.kawika.smart_survey.pushNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kawika.smart_survey.R;
import com.kawika.smart_survey.views.NotificationsActivity;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Senthil on 13-March-18.
 */

public class FcmMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("remoteMessage = " + remoteMessage);
        String title ="", body = "", push_type= "";
        try {
            Map<String, String> datas = remoteMessage.getData();
            JSONObject jsonObject = new JSONObject(datas);

            if (jsonObject.has("title")) {
                title = jsonObject.getString("title");

            }
            if (jsonObject.has("push_type")) {
                push_type = jsonObject.getString("push_type");

            }
            if (jsonObject.has("body")) {
                body = jsonObject.getString("body");

            }
            showNotification(title, body, push_type);
        }catch (Exception e){
            
        }
    }

    public void  showNotification(String title, String body, String push_type){
        Intent myIntent = new Intent(this, NotificationsActivity.class);
//        myIntent.putExtra("code_received",push_type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, myIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }
}
