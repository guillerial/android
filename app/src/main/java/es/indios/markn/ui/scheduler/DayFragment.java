package es.indios.markn.ui.scheduler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.markn.R;

/**
 * Created by guille on 2/04/18.
 */

public class DayFragment extends BaseFragment implements DayMvpView {

    @Inject
    DayPresenter mDayPresenter;
    @Inject
    DayAdapter mDayAdapter;

    @BindView(R.id.day_recycler_view)
    RecyclerView mDayRecyclerView;

    public static final int MONDAY      = 1;
    public static final int TUESDAY     = 2;
    public static final int WEDNESDAY   = 3;
    public static final int THURSDAY    = 4;
    public static final int FRIDAY      = 5;

    private int mDay;
    private boolean mQuarter;

    public DayFragment() {
    }

    @SuppressLint("ValidFragment")
    public DayFragment(int day, boolean checked) {
        mDay = day;
        mQuarter = checked;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);

        mDayPresenter.attachView(this);
        mDayPresenter.setDay(mDay);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_item, container, false);
        ButterKnife.bind(this, view);

        mDayRecyclerView.setAdapter(mDayAdapter);
        mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mDayPresenter.getSchedules(mQuarter);
        return view;
    }

    @Override
    public void setSchedules(ArrayList<Schedule> schedules) {
        mDayAdapter.setSchedules(schedules);
    }

    public void changeQuarter(boolean b) {
        mQuarter = b;
        if (mDayPresenter!=null)
            mDayPresenter.setQuarter(mQuarter);
    }
}
