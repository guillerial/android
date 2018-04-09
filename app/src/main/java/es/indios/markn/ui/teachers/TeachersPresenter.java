package es.indios.markn.ui.teachers;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.ui.base.BasePresenter;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class TeachersPresenter extends BasePresenter<TeachersMvpView>{

    private final DataManager mDataManager;

    @Inject
    public TeachersPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }




}
