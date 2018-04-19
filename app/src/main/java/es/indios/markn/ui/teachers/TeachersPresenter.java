package es.indios.markn.ui.teachers;

import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
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

public class TeachersPresenter extends BasePresenter<TeachersMvpView> implements SearchView.OnQueryTextListener {

    private final DataManager mDataManager;

    private ArrayList<Teacher> mTeacherList = new ArrayList<>();

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
                        mTeacherList = new ArrayList<Teacher>(teachers);
                        Collections.sort(mTeacherList, (teacher, t1) -> teacher.getName().compareTo(t1.getName()));
                        if (getMvpView()!=null)
                            getMvpView().setTeachers(mTeacherList);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Teacher> newTeachers = new ArrayList<>();
        for (Teacher teacher : mTeacherList){
            if(teacher.getName().toLowerCase().contains(newText.toLowerCase())){
                newTeachers.add(teacher);
            }
        }
        if (getMvpView()!=null)
            getMvpView().setTeachers(newTeachers);
        return true;
    }
}
