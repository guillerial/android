package es.indios.markn.ui.init;

import android.app.Activity;
import android.content.Intent;
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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.Scanner;
import es.indios.markn.data.services.IndicationsSyncService;
import es.indios.markn.data.services.LocationsSyncService;
import es.indios.markn.data.services.SchedulesSyncService;
import es.indios.markn.data.services.TeachersSyncService;
import es.indios.markn.data.services.TopologySyncService;
import es.indios.markn.ui.base.BaseActivity;
import es.indios.markn.ui.guide.GuideFragment;
import es.indios.markn.ui.help.HelpActivity;
import es.indios.markn.ui.login.LoginActivity;
import es.indios.markn.ui.main.BeaconsAdapter;
import es.indios.markn.ui.scheduler.SchedulerFragment;
import es.indios.markn.ui.teachers.TeachersFragment;
import es.indios.markn.R;

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
    @BindView(R.id.share_button)        ImageButton mShareButton;

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
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.orange));
        mDrawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        if(!isGooglePlayServicesAvailable(this)){
            finish();
        }

        mFragmentManager = getSupportFragmentManager();
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mInitPresenter.attachView(this);
        Scanner.getInstance().subscribeListener(mInitPresenter);

        onNavigationItemSelected(mBottomNavigation.getMenu().findItem(R.id.action_scheduler));

        mShareButton.setOnClickListener(this::onShareButtonClick);
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.makeGooglePlayServicesAvailable(activity);
            }
            return false;
        }
        return true;
    }

    private void syncThings() {
        startService(LocationsSyncService.getStartIntent(this));
        startService(TopologySyncService.getStartIntent(this));
        startService(IndicationsSyncService.getStartIntent(this));
        startService(SchedulesSyncService.getStartIntent(this));
        startService(TeachersSyncService.getStartIntent(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isGooglePlayServicesAvailable(this)){
            finish();
        }
        syncThings();
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
                Intent intent_help = new Intent(this, HelpActivity.class);
                startActivity(intent_help);
                break;
            case R.id.action_logout:
                mInitPresenter.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return false;
        }
        return true;

    }

    public void onShareButtonClick(View v){
        ImageButton shareButton = (ImageButton) v;
        SchedulerFragment scheduler = (SchedulerFragment) mFragmentManager.findFragmentByTag("Scheduler");
        TeachersFragment teachers = (TeachersFragment) mFragmentManager.findFragmentByTag("Teachers");
        GuideFragment guide = (GuideFragment) mFragmentManager.findFragmentByTag("Guide");

        if(scheduler!= null && scheduler.isVisible()){
            scheduler.onShareButtonClick();
        }
        if(teachers!= null && teachers.isVisible()){
            teachers.onShareButtonClick();
        }
        if(guide!= null && guide.isVisible()){
            guide.onShareButtonClick();
        }

    }

    @Override
    public FragmentManager getMFragmentManager() {
        return mFragmentManager;
    }
}