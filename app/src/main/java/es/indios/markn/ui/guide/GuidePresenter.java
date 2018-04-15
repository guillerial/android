package es.indios.markn.ui.guide;

import android.support.v7.widget.SearchView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.blescanner.models.Topology.IndicationsTopologyWrapper;
import es.indios.markn.blescanner.models.Topology.Route;
import es.indios.markn.data.DataManager;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.ui.base.BasePresenter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class GuidePresenter extends BasePresenter<GuideMvpView> implements SearchView.OnQueryTextListener{

    private final DataManager mDataManager;

    private ArrayList<Location> mLocations;

    private HashMap<String, Route> mTopology;
    private HashMap<String, Indication> mIndications;

    private Indication mActualIndication;
    private Location mActualDestination;
    private Beacon mActualBeacon;

    @Inject
    public GuidePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void getLocations(){
        mDataManager.getLocations().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Location>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Location> locations) {
                mLocations = new ArrayList<Location>(locations);
                if(getMvpView()!=null)
                    getMvpView().setLocationList(mLocations);

            }

            @Override
            public void onError(Throwable e) {
                Timber.i(e,"Error GuidePresenter");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getIndicationsAndTopologies(){
        mDataManager.getIndicationsAndTopology().subscribe(new Observer<IndicationsTopologyWrapper>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(IndicationsTopologyWrapper indicationsTopologyWrapper) {
                mTopology = new HashMap<>();
                for (Route route : indicationsTopologyWrapper.mRoutes){
                    Timber.i(route.getRoute());
                    mTopology.put(route.getRoute(), route);
                }

                mIndications = new HashMap<>();
                for (Indication indication : indicationsTopologyWrapper.mIndications) {
                    Timber.i(indication.getRoute());
                    mIndications.put(indication.getRoute(), indication);
                }
            }

            @Override
            public void onError(Throwable e) {
                Timber.i(e,"Error GuidePresenter");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getIndications(Location location) {
        boolean changes = false;
        if(mActualDestination == null || !mActualDestination.getName().equals(location.getName())) {
            mActualDestination = location;
            changes = true;
        }
        if(changes && mActualBeacon!=null && mActualDestination!=null){
            generatePathIndicationList();
        }
    }

    public void setNearbyBeacon(Beacon nearbyBeacon) {
        boolean changes = false;
        if(mActualBeacon == null || !mActualBeacon.getId2().equals(nearbyBeacon.getId2()) || !mActualBeacon.getId3().equals(nearbyBeacon.getId3())) {
            mActualBeacon = nearbyBeacon;
            changes = true;
        }
        if(changes && mActualBeacon!=null && mActualDestination!=null){
            generatePathIndicationList();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Location> newLocations = new ArrayList<>();
        for (Location location : mLocations){
            if(location.getName().contains(newText)){
                newLocations.add(location);
            }
        }
        if (getMvpView()!=null)
            getMvpView().setLocationList(newLocations);
        return true;
    }

    public void onNewIndication(Indication indication) {
        boolean changes = false;
        if(mActualIndication == null || !mActualIndication.getRoute().equals(indication.getRoute())) {
            mActualIndication = indication;
            changes = true;
        }
        if (changes && mActualBeacon!=null && mActualDestination!=null) {
            generatePathIndicationList();
        }
    }

    public void generatePathIndicationList(){
        ArrayList<Indication> indications = new ArrayList<>();

        String actualBeacon = new StringBuilder().append(mActualBeacon.getId3()).toString();
        Timber.i("Beacon actual:"+actualBeacon+"Beacon destino:"+mActualDestination.getNearby_beacon());
        Route route = mTopology.get(actualBeacon+"-"+mActualDestination.getNearby_beacon());
        if(actualBeacon.equals(mActualDestination.getNearby_beacon())){
            //FIXME: añadir que he llegado al destino

        }else {
            do {
                indications.add(mIndications.get(actualBeacon+"-"+route.getNext()));

                actualBeacon = route.getNext();
                if(!route.getNext().equals(mActualDestination.getNearby_beacon())){
                    route = mTopology.get(route.getNext()+"-"+mActualDestination.getNearby_beacon());
                }
            } while (!actualBeacon.equals(mActualDestination.getNearby_beacon()));
        }

        if(getMvpView()!=null){
            getMvpView().setIndicationList(indications);
        }
    }

}
