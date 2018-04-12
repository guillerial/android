package es.indios.markn.ui.scheduler;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Schedule;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.ribot.androidboilerplate.R;

/**
 * Created by guille on 11/03/18.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.LocationViewHolder> {
    private ArrayList<Schedule> mSchedules;
    customRecyclerOnItemClickListener mListener;


    @Inject
    public DayAdapter() {
        mSchedules = new ArrayList<>();
    }

    public void setListener(customRecyclerOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setData(ArrayList<Schedule> schedules){
        mSchedules = schedules;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        final Schedule schedule = mSchedules.get(position);
        holder.mScheduleHour.setText(schedule.getStart_hour()+":00 - "+schedule.getFinish_hour()+":00");
        holder.mScheduleGroup.setText(schedule.getGroup().getSubject_name()+" "+schedule.getGroup().getNumber());
        holder.mScheduleClassroom.setText(schedule.getGroup().getClassroom().getName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null)
                    mListener.onScheduleClick(schedule);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.schedule_card_view) CardView mCardView;
        @BindView(R.id.scheduler_hour) TextView mScheduleHour;
        @BindView(R.id.scheduler_group) TextView mScheduleGroup;
        @BindView(R.id.scheduler_classroom) TextView mScheduleClassroom;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface customRecyclerOnItemClickListener {
        void onScheduleClick(Schedule schedule);
    }

}
