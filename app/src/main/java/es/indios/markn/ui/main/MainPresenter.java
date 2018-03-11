package es.indios.markn.ui.main;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import javax.inject.Inject;

import es.indios.markn.blescanner.MarknListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import es.indios.markn.data.DataManager;
import es.indios.markn.injection.ConfigPersistent;
import es.indios.markn.ui.base.BasePresenter;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> implements MarknListener{

    private final DataManager mDataManager;
    private Disposable mDisposableBeacons;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposableBeacons != null) mDisposableBeacons.dispose();
    }


    @Override
    public void notifyBluetoothActivationRequired() {
        Timber.i("You need to activate bluetooth");
    }

    @Override
    public void onBeaconsDetected(ArrayList<Beacon> beacons) {
        if (!beacons.isEmpty()) {
            Observable.fromArray(beacons).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ArrayList<Beacon>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            if (mDisposableBeacons!=null && !mDisposableBeacons.isDisposed())
                                mDisposableBeacons.dispose();
                            mDisposableBeacons = d;
                        }

                        @Override
                        public void onNext(ArrayList<Beacon> beacons) {
                            getMvpView().showBeacons(beacons);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.i(e,"ERROR WHEN PRINTING BEACONS");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }
}
