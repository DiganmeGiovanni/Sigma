package org.assistant.sigma.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.assistant.sigma.R;

/**
 * Created by giovanni on 10/10/17.
 *
 */
public class ScheduledTransactionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received", Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.money)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.category_description_restaurants));
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, mBuilder.build());

    }
}
