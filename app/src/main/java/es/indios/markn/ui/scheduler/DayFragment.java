package es.indios.markn.ui.scheduler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.ribot.androidboilerplate.R;

/**
 * Created by guille on 2/04/18.
 */

public class DayFragment extends BaseFragment implements DayMvpView {

    @Inject
    DayPresenter mDayPresenter;

    public static final int MONDAY      = 0;
    public static final int TUESDAY     = 1;
    public static final int WEDNESDAY   = 2;
    public static final int THURSDAY    = 3;
    public static final int FRIDAY      = 4;

    private int mDay;

    public DayFragment() {
    }

    @SuppressLint("ValidFragment")
    public DayFragment(int day) {
        mDay = day;
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
        return view;
    }
}
