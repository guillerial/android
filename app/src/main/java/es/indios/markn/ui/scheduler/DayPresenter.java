package es.indios.markn.ui.scheduler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.ui.base.BasePresenter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class DayPresenter extends BasePresenter<DayMvpView> {

    private final DataManager mDataManager;

    private int mDay;

    @Inject
    public DayPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public void setDay(int day) {
        this.mDay = day;
    }

    public void getSchedules() {
        mDataManager.getSchedules().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Schedule>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Schedule> schedules) {
                        if(getMvpView()!=null)
                            getMvpView().setSchedules(new ArrayList<>(schedules));
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
