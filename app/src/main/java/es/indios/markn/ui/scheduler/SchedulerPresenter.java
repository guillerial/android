package es.indios.markn.ui.scheduler;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import es.indios.markn.MarknApplication;
import es.indios.markn.R;
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

public class SchedulerPresenter extends BasePresenter<SchedulerMvpView> {

    private final DataManager mDataManager;

    private ArrayList<Schedule> mFirstQuarterSchedules = new ArrayList<>();
    private ArrayList<Schedule> mSecondQuarterSchedules = new ArrayList<>();

    @Inject
    public SchedulerPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void getSchedules() {
        mDataManager.getSchedules().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Schedule>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Schedule> schedules) {
                        Collections.sort(schedules, new Comparator<Schedule>() {
                            @Override
                            public int compare(Schedule schedule, Schedule t1) {
                                return schedule.getDay()-t1.getDay();
                            }
                        });
                        mFirstQuarterSchedules = new ArrayList<>();
                        mSecondQuarterSchedules = new ArrayList<>();
                        for(Schedule schedule : schedules){
                            int quarter = Character.getNumericValue(schedule.getGroup().getCode().trim().charAt(4));
                            if(quarter%2==0){
                                mSecondQuarterSchedules.add(schedule);
                            }else{
                                mFirstQuarterSchedules.add(schedule);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("ERROR EN SchedulerPresenter");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getShareableText(Context context, boolean secondQuarter){
        StringBuilder shareString = new StringBuilder();
        ArrayList<Schedule> schedules;
        if(secondQuarter){
            schedules = mSecondQuarterSchedules;
        }else{
            schedules = mFirstQuarterSchedules;
        }
        int day = -1;
        for (Schedule schedule : schedules){
            if (day == -1 || schedule.getDay() != day) {
                day = schedule.getDay();
                shareString.append("*" + parseDay(context, schedule.getDay()) + "* \n");
            }else{
                shareString.append("\t\t\t- - - - - - - - -\n");
            }
            shareString.append("\t" + schedule.getStart_hour() + ":00 - " + schedule.getFinish_hour() + ":00 \n");
            shareString.append("\t" + schedule.getGroup().getSubject_name() + " " +
                    (schedule.getGroup().getNumber() != 0 ? "B" + schedule.getGroup().getNumber() : "A" + schedule.getGroup().getNumber()) +
                    " -- " + schedule.getGroup().getClassroom().getName() + "\n");
        }
        if(getMvpView()!=null) {
            getMvpView().goShareSchedules(shareString.toString());
        }
    }

    private String parseDay(Context context, int i){
        String[] days = context.getResources().getStringArray(R.array.scheduler_fragment_tabs);
        switch (i){
            case 1:
                return days[0];
            case 2:
                return days[1];
            case 3:
                return days[2];
            case 4:
                return days[3];
            case 5:
                return days[4];
            default:
                return "";
        }
    }
}
