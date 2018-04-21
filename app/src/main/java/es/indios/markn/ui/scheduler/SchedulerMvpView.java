package es.indios.markn.ui.scheduler;

import android.text.Spanned;

import es.indios.markn.ui.base.MvpView;

/**
 * Created by guille on 21/04/18.
 */

public interface SchedulerMvpView extends MvpView {
    void goShareSchedules(String shareString);
}
