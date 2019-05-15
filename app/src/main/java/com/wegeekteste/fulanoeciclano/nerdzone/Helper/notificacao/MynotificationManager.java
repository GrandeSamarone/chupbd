package com.wegeekteste.fulanoeciclano.nerdzone.Helper.notificacao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

public class MynotificationManager {

    private Context ctx;
    private static MynotificationManager mystance;

    private MynotificationManager(Context context){
        ctx=context;
    }
    public static synchronized MynotificationManager getMystance(Context context){

        if(mystance==null){
         mystance = new MynotificationManager(context);
         }
         return mystance;
    }

    public void displaynotification(String title,String mensagem){
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.favicon)
                    .setContentTitle(title)
                    .setContentText(mensagem);

            Intent intent = new Intent(ctx, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mnotificationmanager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            if (mnotificationmanager != null) {
                mnotificationmanager.notify(1, mBuilder.build());
            }
        }else{
            Intent notificationIntent = new Intent(ctx,MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(ctx,
                    0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager nm = (NotificationManager) ctx
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Resources res = ctx.getResources();
            Notification.Builder builder = new Notification.Builder(ctx);

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.favicon)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.favicon))
                    .setContentTitle(title)
                    .setContentText(mensagem);
            Notification n = builder.build();


            nm.notify(1, n);
        }
    }
}
