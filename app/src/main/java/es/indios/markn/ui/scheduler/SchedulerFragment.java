package es.indios.markn.ui.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.markn.R;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class SchedulerFragment extends BaseFragment {

    @BindView(R.id.pager)               ViewPager mViewPager;
    @BindView(R.id.pager_tab_strip)     PagerTabStrip mPagerTabStrip;

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
    }

    public void onShareButtonClick() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Your shearing message goes here";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        getActivity().startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    public class SchedulesPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = getResources().getStringArray(R.array.scheduler_fragment_tabs);

        public SchedulesPagerAdapter(FragmentManager fm) {
            super(fm);
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

            switch (position) {
                case 0:
                    return new DayFragment(
                            DayFragment.MONDAY);
                case 1:
                    return new DayFragment(
                            DayFragment.TUESDAY);
                case 2:
                    return new DayFragment(
                            DayFragment.WEDNESDAY);
                case 3:
                    return new DayFragment(
                            DayFragment.THURSDAY);
                case 4:
                    return new DayFragment(
                            DayFragment.FRIDAY);
            }

            return null;
        }
    }
}
