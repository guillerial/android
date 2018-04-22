package es.indios.markn.ui.guide;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.blescanner.models.Topology.Indication;
import es.indios.markn.R;

/**
 * Created by guille on 11/03/18.
 */

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.IndicationViewHolder> {
    private ArrayList<Indication> mIndications;
    customRecyclerOnItemClickListener mListener;
    private RecyclerView.LayoutManager mLayoutManager;

    @Inject
    public GuideAdapter() {
        mIndications = new ArrayList<>();
    }

    public void setIndications(ArrayList<Indication> indications){
        mIndications = indications;
        notifyDataSetChanged();
    }

    @Override
    public IndicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guide_item, parent, false);
        return new IndicationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IndicationViewHolder holder, int position) {
        Indication indication = mIndications.get(position);
        if(!indication.getImage_url().equals("")) {
            Picasso.get().load(indication.getImage_url()).into(holder.indicationImage);
            holder.mGuideCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null)
                        mListener.onIndicationClick(indication);
                }
            });
        }else {
            holder.indicationImage.setVisibility(View.GONE);
        }
        holder.indicationText.setText(indication.getIndication());
    }

    @Override
    public int getItemCount() {
        return mIndications.size();
    }


    public void setListener(customRecyclerOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public int getRoutePosition(String route) {
        for(int i=0; i<mIndications.size(); i++){
            if(mIndications.get(i).getRoute().equals(route))
                return i;
        }
        return -1;
    }

    public void setManager(RecyclerView.LayoutManager indicationLayoutManager) {
        mLayoutManager = indicationLayoutManager;
    }

    class IndicationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.guide_cardview_item) CardView mGuideCardView;
        @BindView(R.id.indication_image) ImageView indicationImage;
        @BindView(R.id.indication_text) TextView indicationText;

        public IndicationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface customRecyclerOnItemClickListener {
        void onIndicationClick(Indication indication);
    }
}
