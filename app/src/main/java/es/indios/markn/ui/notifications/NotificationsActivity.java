package es.indios.markn.ui.notifications;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.R;
import es.indios.markn.data.model.user.MarknNotification;
import es.indios.markn.ui.base.BaseActivity;

/**
 * Created by imasdetres on 17/04/18.
 */

public class NotificationsActivity extends BaseActivity implements NotificationsMvpView, NotificationsAdapter.customRecyclerOnItemClickListener {

    @Inject
    NotificationsPresenter mNotificationsPresenter;
    @Inject
    NotificationsAdapter mNotificationAdapter;

    @BindView(R.id.notifications_recycler_view)
    RecyclerView mNotificationsRecycler;
    @BindView(R.id.notifications_toolbar)
    Toolbar mNotificationToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        mNotificationsPresenter.attachView(this);
        setSupportActionBar(mNotificationToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mNotificationsRecycler.setAdapter(mNotificationAdapter);
        mNotificationsRecycler.setLayoutManager(new LinearLayoutManager(this));

        mNotificationsPresenter.getNotifications();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void setNotifications(ArrayList<MarknNotification> marknNotifications) {
        mNotificationAdapter.setNotifications(marknNotifications);
    }

    @Override
    public void onNotificationClick(MarknNotification notification) {

    }

    @Override
    protected void onDestroy() {
        mNotificationsPresenter.detachView();
        super.onDestroy();
    }
}
