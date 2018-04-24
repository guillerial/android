package es.indios.markn.ui.guide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Timer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.ui.base.BaseFragment;
import es.indios.markn.R;
import timber.log.Timber;

/**
 * Created by CristinaPosada on 22/03/2018.
 */

public class GuideFragment extends BaseFragment implements GuideMvpView,
        LocationAdapter.customRecyclerOnItemClickListener,
        GuideAdapter.customRecyclerOnItemClickListener{

    @Inject
    GuidePresenter mGuidePresenter;
    @Inject
    GuideAdapter mGuideAdapter;
    @Inject
    LocationAdapter mLocationAdapter;

    @BindView(R.id.search_location)                 SearchView mSearchView;
    @BindView(R.id.guide_indication_recycler)       RecyclerView mIndicationRecyclerView;
    @BindView(R.id.guide_location_recycler)         RecyclerView mLocationRecyclerView;
    @BindView(R.id.guide_indication_container)      RelativeLayout mIndicationContainer;
    @BindView(R.id.guide_location_container)        RelativeLayout mLocationContainer;
    @BindView(R.id.route_title_textview)            TextView mRouteTitleTextview;

    private AlertDialog mDialog;

    private AlertDialog mWrongDialog;

    private boolean guide = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        mGuidePresenter.attachView(this);
        mDialog = initialiseGPSDialog();

        Timber.i("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_guide, container, false);
        ButterKnife.bind(this, view);

        mIndicationRecyclerView.setAdapter(mGuideAdapter);
        mLocationRecyclerView.setAdapter(mLocationAdapter);
        RecyclerView.LayoutManager indicationLayoutManager = new LinearLayoutManager(getContext());
        mIndicationRecyclerView.setLayoutManager(indicationLayoutManager);
        mGuideAdapter.setManager(indicationLayoutManager);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Timber.i("Guide? "+guide);
        if(!guide) {
            mGuidePresenter.getLocations();
            mGuidePresenter.getIndicationsAndTopologies();
        }else{
            mIndicationContainer.setVisibility(View.VISIBLE);
            mLocationContainer.setVisibility(View.GONE);
        }
        mSearchView.setOnQueryTextListener(mGuidePresenter);
        mSearchView.setIconifiedByDefault(false);

        Timber.i("guidefragment onCreateView");

        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            initialiseBLEDialog().show();
        }
        mWrongDialog = initialiseWrongDirectionDialog();
        return view;
    }

    @Override
    public void onPause() {
        Timber.i("onPause");
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.i("onViewCreated guide?"+guide);
        if(guide){
            mIndicationContainer.setVisibility(View.VISIBLE);
            mLocationContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        Timber.i("onStart guide?"+guide);
        if(guide){
            mIndicationContainer.setVisibility(View.VISIBLE);
            mLocationContainer.setVisibility(View.GONE);
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        Timber.i("onResume guide?"+guide);
        super.onResume();
        if(guide){
            mIndicationContainer.setVisibility(View.VISIBLE);
            mLocationContainer.setVisibility(View.GONE);
        }
        mGuidePresenter.setWindowResume(true);
    }

    @Override
    public void onDestroy() {
        Timber.i("onDestroy");
        mGuidePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setLocationList(ArrayList<Location> locations) {
        if(guide){
            mIndicationContainer.setVisibility(View.VISIBLE);
            mLocationContainer.setVisibility(View.GONE);
        }else{
            mIndicationContainer.setVisibility(View.GONE);
            mLocationContainer.setVisibility(View.VISIBLE);
        }
        mLocationAdapter.setResources(getResources());
        mLocationAdapter.setListener(this);
        mLocationAdapter.setLocations(locations);
    }

    @Override
    public void setIndicationList(final ArrayList<Indication> indications, boolean first) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                guide = true;
                mGuideAdapter.setIndications(indications);
                mLocationContainer.setVisibility(View.GONE);
                mIndicationContainer.setVisibility(View.VISIBLE);
                mGuideAdapter.setListener(GuideFragment.this);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGuideAdapter.setIndicationBackground(0,
                                first?R.color.primary:R.color.red,
                                getResources()
                        );
                    }
                }, 500);
                if(!first){
                    if(!mWrongDialog.isShowing())
                        mWrongDialog.show();
                }
                if(mDialog.isShowing())
                    mDialog.cancel();
            }
        });
    }

    @Override
    public void scrollToIndication(String route, boolean colorear) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mGuideAdapter.getRoutePosition(route)!=-1){
                    mLocationContainer.setVisibility(View.GONE);
                    mIndicationContainer.setVisibility(View.VISIBLE);
                    if(colorear) {
                        mGuideAdapter.setIndicationBackground(
                                mGuideAdapter.getRoutePosition(route),
                                R.color.primary,
                                getResources()
                        );
                    }
                    mIndicationRecyclerView.smoothScrollToPosition(mGuideAdapter.getRoutePosition(route));
                }
            }
        });
    }

    private AlertDialog initialiseGPSDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=Escola+de+Enxeñaría+de+Telecomunicación");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                })
                .setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private AlertDialog initialiseBLEDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.ble_dialog_message)
                .setPositiveButton(R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private AlertDialog initialiseWrongDirectionDialog(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.wrong_dialog_message)
                .setPositiveButton(R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void askForGPS() {
        if(!mDialog.isShowing())
            mDialog.show();
    }

    @Override
    public void onLocationClick(Location location) {
        mGuidePresenter.getIndications(location);

        mRouteTitleTextview.setText(String.format("%s %s", getResources().getString(R.string.route_to), location.getName()));
    }

    public void onNewIndication(Indication indication) {
        mGuidePresenter.onNewIndication(indication);
    }

    public void setNearbyBeacon(Beacon nearbyBeacon) {
        mGuidePresenter.setNearbyBeacon(nearbyBeacon);
    }

    public void onShareButtonClick() {

    }

    public boolean reloadFragment(){
        if(mLocationContainer.getVisibility()!=View.VISIBLE){
            return true;
        }
        return false;
    }

    @Override
    public void onIndicationClick(Indication indication) {
        Intent indicationActivity = new Intent(getActivity(), ImageActivity.class);
        indicationActivity.putExtra(ImageActivity.IMAGE_URL,indication.getImage_url());
        indicationActivity.putExtra(ImageActivity.INDICATION, indication.getIndication());
        startActivity(indicationActivity);
    }
}
