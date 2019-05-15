package com.wegeekteste.fulanoeciclano.nerdzone.Helper.notificacao;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    String title=remoteMessage.getNotification().getTitle();
    String mensagem = remoteMessage.getNotification().getBody();

    MynotificationManager.getMystance(getApplicationContext()).displaynotification(title,mensagem   );

    }



}

