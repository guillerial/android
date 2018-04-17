package es.indios.markn.ui.notifications;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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

public class NotificationsActivity extends BaseActivity implements NotificationsMvpView {

    @Inject
    NotificationsPresenter mNotificationsPresenter;
    @Inject
    NotificationsAdapter mNotificationAdapter;

    @BindView(R.id.notifications_recycler_view)
    RecyclerView mNotificationsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        mNotificationsRecycler.setAdapter(mNotificationAdapter);

        mNotificationsPresenter.getNotifications();

    }

    @Override
    public void setNotifications(ArrayList<MarknNotification> marknNotifications) {
        mNotificationAdapter.setNotifications(marknNotifications);
    }
}
