package es.indios.markn.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.Scanner;
import es.indios.ribot.androidboilerplate.R;
import es.indios.markn.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainMvpView {
    @Inject MainPresenter mMainPresenter;
    @Inject BeaconsAdapter mBeaconsAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.main_beacon_textview)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mBeaconsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainPresenter.attachView(this);
        Scanner.getInstance().subscribeListener(mMainPresenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Scanner.getInstance().deleteListener(mMainPresenter);
        mMainPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showBeacons(ArrayList<Beacon> beacons) {
        mBeaconsAdapter.setBeacons(beacons);
        mBeaconsAdapter.notifyDataSetChanged();
    }

}
