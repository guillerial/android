package es.indios.markn.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.R;

/**
 * Created by guille on 11/03/18.
 */

public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.BeaconViewHolder> {
    private ArrayList<Beacon> mBeacons;

    @Inject
    public BeaconsAdapter() {
        mBeacons = new ArrayList<>();
    }

    public void setBeacons(ArrayList<Beacon> beacons){
        mBeacons = beacons;
    }

    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new BeaconViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BeaconViewHolder holder, int position) {
        Beacon beacon = mBeacons.get(position);
        holder.nameTextView.setText(String.format("%s",
                beacon.getId1()));
        holder.emailTextView.setText(String.format("MAJOR: %s MINOR: %s DISTANCE: %S",beacon.getId2(), beacon.getId3(), beacon.getDistance()));
    }

    @Override
    public int getItemCount() {
        return mBeacons.size();
    }

    class BeaconViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.text_name) TextView nameTextView;
        @BindView(R.id.text_email) TextView emailTextView;

        public BeaconViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
