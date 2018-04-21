package es.indios.markn.ui.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.markn.R;
import timber.log.Timber;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class SchedulerFragment extends BaseFragment implements SchedulerMvpView{

    @BindView(R.id.pager)               ViewPager mViewPager;
    @BindView(R.id.pager_tab_strip)     PagerTabStrip mPagerTabStrip;
    @BindView(R.id.quarter_switch)      Switch mQuarterSwitch;

    @Inject
    SchedulerPresenter mSchedulerPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scheduler, container, false);
        ButterKnife.bind(this, view);
        mSchedulerPresenter.attachView(this);

        mSchedulerPresenter.getSchedules();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewPager();
    }

    private void configureViewPager() {

        SchedulesPagerAdapter adapter = new SchedulesPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mQuarterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                adapter.changeQuarter(b);
            }
        });
    }

    public void onShareButtonClick() {
        mSchedulerPresenter.getShareableText(getContext(), mQuarterSwitch.isChecked());
    }

    @Override
    public void goShareSchedules(String shareString) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareString);
        getActivity().startActivity(Intent.createChooser(intent, ""));
    }

    public class SchedulesPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = getResources().getStringArray(R.array.scheduler_fragment_tabs);
        private FragmentManager fragmentManager;
        private ArrayList<DayFragment> mDayFragmentList;

        public SchedulesPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mDayFragmentList = new ArrayList<>();
            mDayFragmentList.add(0,new DayFragment(DayFragment.MONDAY, mQuarterSwitch.isChecked()));
            mDayFragmentList.add(1,new DayFragment(DayFragment.TUESDAY, mQuarterSwitch.isChecked()));
            mDayFragmentList.add(2,new DayFragment(DayFragment.WEDNESDAY, mQuarterSwitch.isChecked()));
            mDayFragmentList.add(3,new DayFragment(DayFragment.THURSDAY, mQuarterSwitch.isChecked()));
            mDayFragmentList.add(4,new DayFragment(DayFragment.FRIDAY, mQuarterSwitch.isChecked()));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mDayFragmentList.get(position);
        }

        public void changeQuarter(boolean b) {
            for(DayFragment fragment : mDayFragmentList){
                fragment.changeQuarter(b);
            }
        }
    }
}
