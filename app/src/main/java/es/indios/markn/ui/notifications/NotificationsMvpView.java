package es.indios.markn.ui.notifications;

import java.util.ArrayList;
import java.util.List;

import es.indios.markn.data.model.user.MarknNotification;
import es.indios.markn.ui.base.MvpView;

/**
 * Created by imasdetres on 17/04/18.
 */

public interface NotificationsMvpView extends MvpView {
    void setNotifications(ArrayList<MarknNotification> marknNotifications);
}
