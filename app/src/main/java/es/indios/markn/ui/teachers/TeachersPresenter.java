package es.indios.markn.ui.teachers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.markn.ui.base.BasePresenter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class TeachersPresenter extends BasePresenter<TeachersMvpView>{

    private final DataManager mDataManager;

    @Inject
    public TeachersPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public void getTeachers() {
        mDataManager.getTeachers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Teacher>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Teacher> teachers) {
                        if (getMvpView()!=null)
                            getMvpView().setTeachers(new ArrayList<Teacher>(teachers));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("ERROR EN TEACHERS");
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
