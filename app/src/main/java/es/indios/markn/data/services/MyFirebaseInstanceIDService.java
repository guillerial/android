package es.indios.markn.data.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import es.indios.markn.MarknApplication;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.user.TokenResponse;
import es.indios.markn.util.AndroidComponentUtil;
import es.indios.markn.util.NetworkUtil;
import es.indios.markn.util.RxUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Inject
    DataManager mDataManager;

    private Disposable mDisposable;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MyFirebaseInstanceIDService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, MyFirebaseInstanceIDService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MarknApplication.get(this).getComponent().inject(this);
        onTokenRefresh();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (mDataManager.isLoggedIn()) {
            Timber.i("Refreshed token: " + refreshedToken);
            mDataManager.sendFirebaseToken(refreshedToken).subscribeOn(Schedulers.io()).subscribe(new Observer<TokenResponse>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(TokenResponse tokenResponse) {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
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
