package es.indios.markn.ui.init;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import javax.inject.Inject;

import es.indios.markn.MarknApplication;
import es.indios.markn.blescanner.MarknListener;
import es.indios.markn.blescanner.Scanner;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.DataManager;
import es.indios.markn.injection.ConfigPersistent;
import es.indios.markn.ui.base.BasePresenter;
import es.indios.markn.ui.guide.GuideFragment;
import timber.log.Timber;

@ConfigPersistent
public class InitPresenter extends BasePresenter<InitMvpView> implements MarknListener {

    private final DataManager mDataManager;
    static Handler mHandler = new Handler();

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

    public void initUser() {
        if(mDataManager.isLoggedIn()){
            if(getMvpView()!=null)
            getMvpView().setName(mDataManager.getName());
        }
    }

    public void setBluetooth(Context context, boolean enable){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = mBluetoothAdapter.isEnabled();
        if(enable){
            if (!isEnabled) {
                Scanner.getInstance().enableBluetooth();
            }else{
                if(mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_CONNECTED
                        || mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothProfile.STATE_CONNECTED){
                    Timber.i("DEVICE: HEADSET ---> "+mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET));
                    Timber.i("DEVICE: A2DP ---> "+mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP));
                }else{
                    Timber.i("RESTARTING BLE.");
                    restartBle(context);
                }
            }
        }else{
            if(mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_CONNECTED
                    || mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothProfile.STATE_CONNECTED){
                Scanner.getInstance().stopDiscovery();
            }else{
                Scanner.getInstance().disable();
            }
        }
    }

    public static void restartBle(Context context) {
        final BluetoothManager mgr = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adp = mgr.getAdapter();
        if (null != adp) {
            if (adp.isEnabled()) {
                adp.disable();
                Timber.i("DISABLING BLE");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Timber.i("ENABLING BLE");
                        if (!adp.isEnabled()) {
                            adp.enable();
                        } else {
                            mHandler.postDelayed(this, 1000);
                        }
                    }
                }, 2500);
            }
        }
    }

}
