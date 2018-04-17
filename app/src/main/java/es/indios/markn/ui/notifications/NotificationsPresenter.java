package es.indios.markn.ui.notifications;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.user.MarknNotification;
import es.indios.markn.ui.base.BasePresenter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by imasdetres on 17/04/18.
 */

public class NotificationsPresenter extends BasePresenter<NotificationsMvpView> {

    private final DataManager mDataManager;

    @Inject
    NotificationsPresenter(DataManager dataManager){mDataManager = dataManager;}



    public void getNotifications() {
        mDataManager.getNotifications().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<MarknNotification>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<MarknNotification> marknNotifications) {
                if(getMvpView()!=null)
                    getMvpView().setNotifications(new ArrayList<>(marknNotifications));
            }

            @Override
            public void onError(Throwable e) {
                Timber.i(e, "Error obteniendo notificaciones");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
