package es.indios.markn.ui.guide;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Location;
import es.indios.markn.R;

/**
 * Created by guille on 11/03/18.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private ArrayList<Location> mLocations;
    customRecyclerOnItemClickListener mListener;
    private Resources mResources;

    private HashMap<String, Integer> mLocationIconsMap;


    @Inject
    public LocationAdapter() {
        mLocations = new ArrayList<>();
        if(mLocationIconsMap==null){ setLocationIconsMap();}
    }

    private void setLocationIconsMap() {
        mLocationIconsMap = new HashMap<>();
        mLocationIconsMap.put("biblioteca", R.drawable.biblioteca);
        mLocationIconsMap.put("cafeteria", R.drawable.cafeteria);
        mLocationIconsMap.put("clase", R.drawable.clase);
        mLocationIconsMap.put("conserjeria", R.drawable.conserjeria);
        mLocationIconsMap.put("dat", R.drawable.dat);
        mLocationIconsMap.put("despacho", R.drawable.despacho);
        mLocationIconsMap.put("despacho2", R.drawable.despacho2);
        mLocationIconsMap.put("informatica", R.drawable.informatica);
        mLocationIconsMap.put("seminario", R.drawable.seminario);
    }

    public void setListener(customRecyclerOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setLocations(ArrayList<Location> locations){
        mLocations = locations;
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        final Location location = mLocations.get(position);
        holder.locationText.setText(location.getName());
        if(location.getName().contains("Despacho")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("despacho2")));
        }else if(location.getName().contains("Biblioteca")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("biblioteca")));
        }else if(location.getName().contains("Cafetería")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("cafeteria")));
        }else if(location.getName().contains("Conserjería")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("conserjeria")));
        }else if(location.getName().contains("DAT")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("dat")));
        }else if(location.getName().contains("informática")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("informatica")));
        }else if(location.getName().contains("Seminario")){
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("seminario")));
        }else{
            holder.image.setImageDrawable(mResources.getDrawable(mLocationIconsMap.get("clase")));
        }
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

    public void setResources(Resources resources) {
        this.mResources = resources;
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_hex_color) ImageView image;
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
