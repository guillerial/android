package es.indios.markn.ui.scheduler;

import java.util.ArrayList;

import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.ui.base.MvpView;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public interface DayMvpView extends MvpView {
    void setSchedules(ArrayList<Schedule> schedules);
}
