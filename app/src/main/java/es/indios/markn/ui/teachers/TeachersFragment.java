package es.indios.markn.ui.teachers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.ribot.androidboilerplate.R;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class TeachersFragment extends BaseFragment implements TeachersMvpView {

    @BindView(R.id.search_teacher) SearchView mSearchView;
    @BindView(R.id.teachers_recycler_view) RecyclerView mTeachersRecyclerView;

    @Inject
    TeachersPresenter mTeachersPresenter;
    @Inject
    TeachersAdapter mTeachersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);

        mTeachersPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_teachers, container, false);
        ButterKnife.bind(this, view);

        mTeachersRecyclerView.setAdapter(mTeachersAdapter);
        mTeachersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mTeachersPresenter.getTeachers();
        mSearchView.setIconifiedByDefault(false);

        return view;
    }

    @Override
    public void onDestroy() {
        mTeachersPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setTeachers(ArrayList<Teacher> teachers) {
        mTeachersAdapter.setTeachers(teachers);
    }

    public void onShareButtonClick() {

    }
}
