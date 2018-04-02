package es.indios.markn.ui.init;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.Scanner;
import es.indios.markn.ui.base.BaseActivity;
import es.indios.markn.ui.guide.GuideFragment;
import es.indios.markn.ui.main.BeaconsAdapter;
import es.indios.markn.ui.scheduler.SchedulerFragment;
import es.indios.markn.ui.teachers.TeachersFragment;
import es.indios.ribot.androidboilerplate.R;

public class InitActivity extends BaseActivity implements InitMvpView, BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    @Inject
    InitPresenter mInitPresenter;
    @Inject
    BeaconsAdapter mBeaconsAdapter;

    private FragmentManager mFragmentManager;


    @BindView(R.id.bottom_navigation)   BottomNavigationView mBottomNavigation;
    @BindView(R.id.navigation_view)     NavigationView mNavigationView;
    @BindView(R.id.content_main)        FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mInitPresenter.attachView(this);
        Scanner.getInstance().subscribeListener(mInitPresenter);

        onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.action_scheduler));
    }

    @Override
    protected void onDestroy() {
        Scanner.getInstance().deleteListener(mInitPresenter);
        mInitPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        switch (item.getItemId()){
            case R.id.action_scheduler:
                transaction.replace(R.id.content_main, new SchedulerFragment(), "Scheduler");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                break;
            case R.id.action_teachers:
                transaction.replace(R.id.content_main, new TeachersFragment(), "Teachers");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                break;
            case R.id.action_guide:
                transaction.replace(R.id.content_main, new GuideFragment(), "Guide");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                break;
            case R.id.action_notifications:
                break;
            case R.id.action_settings:
                break;
            case R.id.action_help:
                break;
            default:
                return false;
        }
        return true;

    }

    @Override
    public FragmentManager getMFragmentManager() {
        return mFragmentManager;
    }
}