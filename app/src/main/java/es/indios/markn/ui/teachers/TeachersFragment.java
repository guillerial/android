package es.indios.markn.ui.teachers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.markn.R;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class TeachersFragment extends BaseFragment implements TeachersMvpView, TeachersAdapter.customRecyclerOnItemClickListener {

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
        mSearchView.setOnQueryTextListener(mTeachersPresenter);

        mTeachersAdapter.setListener(this);

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

    @Override
    public void onTeacherClick(Teacher teacher) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + teacher.getEmail()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.email_message_body));

        try {
            startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.email_message_chooser)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), getResources().getString(R.string.email_message_error), Toast.LENGTH_SHORT).show();
        }
    }
}
