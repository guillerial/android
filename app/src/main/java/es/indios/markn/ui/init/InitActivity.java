package es.indios.markn.ui.init;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.Scanner;
import es.indios.markn.blescanner.models.Topology.IndicationsTopologyWrapper;
import es.indios.markn.data.sync.IndicationsSyncService;
import es.indios.markn.data.sync.LocationsSyncService;
import es.indios.markn.data.sync.TopologySyncService;
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
    @BindView(R.id.drawer_layout)       DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)     NavigationView mNavigationView;
    @BindView(R.id.content_main)        FrameLayout mFragmentContainer;
    @BindView(R.id.toolbar)             Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        mFragmentManager = getSupportFragmentManager();
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mInitPresenter.attachView(this);
        Scanner.getInstance().subscribeListener(mInitPresenter);

        onNavigationItemSelected(mBottomNavigation.getMenu().findItem(R.id.action_scheduler));

        syncThings();
    }

    private void syncThings() {
        startService(LocationsSyncService.getStartIntent(this));
        startService(TopologySyncService.getStartIntent(this));
        startService(IndicationsSyncService.getStartIntent(this));
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
            case R.id.action_help:

                break;
            case R.id.action_logout:

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