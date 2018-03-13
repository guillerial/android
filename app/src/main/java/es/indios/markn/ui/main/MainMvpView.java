package es.indios.markn.ui.main;

import org.altbeacon.beacon.Beacon;


import java.util.ArrayList;

import es.indios.markn.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showBeacons(ArrayList<Beacon> beacons);
}
