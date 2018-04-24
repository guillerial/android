package es.indios.markn.ui.guide;

import java.util.ArrayList;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.ui.base.MvpView;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public interface GuideMvpView extends MvpView {
    void setLocationList(ArrayList<Location> locations);

    void setIndicationList(ArrayList<Indication> indications, boolean first);

    void scrollToIndication(String route, boolean colorear);

    void askForGPS();
}
