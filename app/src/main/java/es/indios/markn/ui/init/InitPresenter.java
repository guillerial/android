package es.indios.markn.ui.init;

import android.support.v4.app.Fragment;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import javax.inject.Inject;

import es.indios.markn.blescanner.MarknListener;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.DataManager;
import es.indios.markn.injection.ConfigPersistent;
import es.indios.markn.ui.base.BasePresenter;
import es.indios.markn.ui.guide.GuideFragment;

@ConfigPersistent
public class InitPresenter extends BasePresenter<InitMvpView> implements MarknListener {

    private final DataManager mDataManager;

    @Inject
    public InitPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public GuideFragment getGuideFragment(){
        if (getMvpView()!=null) {
            Fragment fragment = getMvpView().getMFragmentManager().findFragmentByTag("Guide");
            if (fragment != null && fragment.isVisible())
                return ((GuideFragment) fragment);
        }
        return null;

    };


    @Override
    public void notifyBluetoothActivationRequired() {

    }

    @Override
    public void onBeaconsDetected(ArrayList<Beacon> beacons) {
        if (getGuideFragment()!=null && !beacons.isEmpty())
            getGuideFragment().setNearbyBeacon(beacons.get(0));
    }

    @Override
    public void onNewIndication(Indication indication) {
        if (getGuideFragment()!=null)
            getGuideFragment().onNewIndication(indication);
    }

    public void logout() {
        mDataManager.logout();
    }
}
