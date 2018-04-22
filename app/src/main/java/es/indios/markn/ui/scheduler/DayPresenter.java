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
import timber.log.Timber;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class DayPresenter extends BasePresenter<DayMvpView> {

    private final DataManager mDataManager;

    private int mDay;
    private ArrayList<Schedule> mFirstQuarterSchedules = new ArrayList<>();
    private ArrayList<Schedule> mSecondQuarterSchedules = new ArrayList<>();

    @Inject
    public DayPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public void setDay(int day) {
        this.mDay = day;
    }

    public void getSchedules(boolean secondQuarter) {
        mDataManager.getSchedulesByDay(mDay).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Schedule>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Schedule> schedules) {
                        mFirstQuarterSchedules = new ArrayList<>();
                        mSecondQuarterSchedules = new ArrayList<>();
                        for(Schedule schedule : schedules){
                            if(schedule.getGroup()!=null) {
                                int quarter = Character.getNumericValue(schedule.getGroup().getCode().trim().charAt(4));
                                if (quarter % 2 == 0) {
                                    mSecondQuarterSchedules.add(schedule);
                                } else {
                                    mFirstQuarterSchedules.add(schedule);
                                }
                            }
                        }
                        if(getMvpView()!=null)
                            if(secondQuarter){
                                getMvpView().setSchedules(mSecondQuarterSchedules);
                            }else{
                                getMvpView().setSchedules(mFirstQuarterSchedules);
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("ERROR EN DAY: "+mDay);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void setQuarter(boolean secondQuarter){
        if(getMvpView()!=null)
            if(secondQuarter){
                getMvpView().setSchedules(mSecondQuarterSchedules);
            }else{
                getMvpView().setSchedules(mFirstQuarterSchedules);
            }
    }
}
