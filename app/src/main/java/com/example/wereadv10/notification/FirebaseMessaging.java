package com.example.wereadv10.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.wereadv10.Constant;
import com.example.wereadv10.MainActivity;
import com.example.wereadv10.MySharedPreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessaging extends FirebaseMessagingService {
    private FirebaseAuth mAuth;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //get user id form SharedPreferences
        SharedPreferences sp = getSharedPreferences(Constant.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String savedCurrentUser = sp.getString("CURRENT_USERID","None");
        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");
        FirebaseUser  fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser !=null && sent.equals(fUser.getUid())){
            Log.d("notification","onResponse: ");

            if (!savedCurrentUser.equals(user)){
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                    sendOAndAboveNotification(remoteMessage);
                }
                else {
                    sendNormalNotification(remoteMessage);

                }
            }
        }
    }
    private void sendNormalNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification  notification = remoteMessage.getNotification();
        int i = Integer.parseInt((user.replaceAll("[\\D]","")));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int j = 0;
        if (i > 0){
            j = 0;
        }
        notificationManager.notify(j,builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification  notification = remoteMessage.getNotification();
        int i = Integer.parseInt((user.replaceAll("[\\D]","")));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification notification1 =new OreoAndAboveNotification(this);
        Notification.Builder builder = notification1.getONotifications(title,body,pIntent,defSoundUri,icon);

        int j=0;
        if (i>0){
            j=0;
        }
        notification1.getManager().notify(j,builder.build());


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //update user token
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            //signed in, update token
            updateToken(s);

        }
    }

    private void updateToken(String s) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(s);
        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken.getToken());
        // Set the "isCapital" field of the city 'DC'
        userTokenDR
                .update(tokenh)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error updating document", e);


                    }
                });
    }
}
