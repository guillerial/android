package es.indios.markn.ui.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.ribot.androidboilerplate.R;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class GuideFragment extends BaseFragment implements GuideMvpView, LocationAdapter.customRecyclerOnItemClickListener {

    @Inject
    GuidePresenter mGuidePresenter;
    @Inject
    GuideAdapter mGuideAdapter;
    @Inject
    LocationAdapter mLocationAdapter;

    @BindView(R.id.search_location)                 SearchView mSearchView;
    @BindView(R.id.guide_indication_recycler)       RecyclerView mIndicationRecyclerView;
    @BindView(R.id.guide_location_recycler)         RecyclerView mLocationRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        mGuidePresenter.attachView(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_guide, container, false);
        ButterKnife.bind(this, view);

        mIndicationRecyclerView.setAdapter(mGuideAdapter);
        mLocationRecyclerView.setAdapter(mLocationAdapter);
        mIndicationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mGuidePresenter.getLocations();
        mGuidePresenter.getIndicationsAndTopologies();

        mSearchView.setOnQueryTextListener(mGuidePresenter);

        return view;
    }

    @Override
    public void setLocationList(ArrayList<Location> locations) {
        mIndicationRecyclerView.setVisibility(View.GONE);
        mLocationRecyclerView.setVisibility(View.VISIBLE);
        mLocationAdapter.setListener(this);
        mLocationAdapter.setLocations(locations);
    }

    @Override
    public void setIndicationList(ArrayList<Indication> indications) {
        mLocationRecyclerView.setVisibility(View.GONE);
        mIndicationRecyclerView.setVisibility(View.VISIBLE);
        mGuideAdapter.setIndications(indications);
    }

    @Override
    public void onLocationClick(Location location) {
        mGuidePresenter.getIndications(location);
    }

    public void onNewIndication(Indication indication) {
        mGuidePresenter.onNewIndication(indication);
    }

    public void setNearbyBeacon(Beacon nearbyBeacon) {
        mGuidePresenter.setNearbyBeacon(nearbyBeacon);
    }

    public void onShareButtonClick() {

    }
}
