package es.indios.markn.data.sync;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import javax.inject.Inject;

import es.indios.markn.MarknApplication;
import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.markn.util.AndroidComponentUtil;
import es.indios.markn.util.NetworkUtil;
import es.indios.markn.util.RxUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TeachersSyncService extends Service {

    @Inject
    DataManager mDataManager;
    private Disposable mDisposable;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TeachersSyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, TeachersSyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MarknApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        RxUtil.dispose(mDisposable);

        mDataManager.syncTeachers()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Teacher>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Teacher teacher) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i(e, "Error SyncTeachers");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) mDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
