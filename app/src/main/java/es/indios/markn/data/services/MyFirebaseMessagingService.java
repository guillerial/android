package es.indios.markn.data.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import javax.inject.Inject;

import es.indios.markn.MarknApplication;
import es.indios.markn.R;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.user.MarknNotification;
import es.indios.markn.ui.init.InitActivity;
import es.indios.markn.util.AndroidComponentUtil;
import es.indios.markn.util.NetworkUtil;
import es.indios.markn.util.RxUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    DataManager mDataManager;
    private Disposable mDisposable;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MyFirebaseMessagingService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, MyFirebaseMessagingService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MarknApplication.get(this).getComponent().inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.i("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Timber.i("Message data payload: " + remoteMessage.getData());

            MarknNotification notification = handleNow(remoteMessage);
            if (notification != null) {
                sendNotification(notification);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder().build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private MarknNotification handleNow(RemoteMessage remoteMessage) {

        String title = "";
        String body = "";
        String author = "";
        if(remoteMessage.getData()!=null) {
            if(remoteMessage.getData().containsKey("author"))
                author = remoteMessage.getData().get("author");
            if(remoteMessage.getData().containsKey("title"))
                title = remoteMessage.getData().get("title");
            if(remoteMessage.getData().containsKey("body"))
                body = remoteMessage.getData().get("body");
            Date date = new Date();
            MarknNotification notification = new MarknNotification(author, title, body, date);
            Timber.i("Notificacion creada: "+notification.getBody());
            mDataManager.saveNotification(notification).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MarknNotification>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MarknNotification notification) {
                            Timber.i("Notification received"+notification.getBody());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.i(e, "Error saving notification");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            return notification;
        }
        return null;
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param notification FCM message body received.
     */
    private void sendNotification(MarknNotification notification) {
        Intent intent = new Intent(this, InitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logofinal)
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setContentInfo(notification.getAuthor())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) mDisposable.dispose();
        super.onDestroy();
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }

}
