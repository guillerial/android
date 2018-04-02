package es.indios.markn.ui.scheduler;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.ui.base.BasePresenter;

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
}
