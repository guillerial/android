package es.indios.markn.ui.guide;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.ribot.androidboilerplate.R;

/**
 * Created by guille on 11/03/18.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private ArrayList<Location> mLocations;
    customRecyclerOnItemClickListener mListener;


    @Inject
    public LocationAdapter() {
        mLocations = new ArrayList<>();
    }

    public void setListener(customRecyclerOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setLocations(ArrayList<Location> locations){
        mLocations = locations;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        final Location location = mLocations.get(position);
        holder.locationText.setText(location.getName());
        holder.color.setVisibility(View.GONE);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null)
                    mListener.onLocationClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_hex_color) ImageView color;
        @BindView(R.id.text_name) TextView locationText;
        @BindView(R.id.teacher_card_view) CardView mCardView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface customRecyclerOnItemClickListener {
        void onLocationClick(Location location);
    }

}
