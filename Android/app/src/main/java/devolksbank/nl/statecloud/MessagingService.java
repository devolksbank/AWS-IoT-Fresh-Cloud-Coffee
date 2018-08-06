package devolksbank.nl.statecloud;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Handles incomming push notifications.
 */
public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM/MSG";

    /**
     * Event handler for when push notifications arrive.
     * @param remoteMessage the push notification
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            String messageBody = remoteMessage.getData().get("default");

            sendNotification(messageBody);
            sendDialogBroadcast(messageBody);
        }
    }

    /**
     * Sends notification in Android, even when the app has no focus.
     * @param messageBody the content of the push notification
     */
    private void sendNotification(String messageBody) {
        // Construct new intent
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Construct notification and add it to intent
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSound(defaultSoundUri).setSmallIcon(R.drawable.ic_stat)
                .setContentTitle(getString(R.string.update_notification))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Set channel for notifications if Android version demands it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        // Send notification
        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * Sends message to BroadcastReceiver with content of push notification, so it can be shown as a dialog when the app is open.
     * @param messageBody the content of the push notification
     */
    private void sendDialogBroadcast(String messageBody) {
        Intent alertIntent = new Intent("awsPush");
        alertIntent.putExtra("data", messageBody);
        LocalBroadcastManager.getInstance(this).sendBroadcast(alertIntent);
    }

}
