package es.indios.markn.ui.teachers;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.data.model.uvigo.Teacher;
import es.indios.ribot.androidboilerplate.R;

/**
 * Created by guille on 11/03/18.
 */

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.LocationViewHolder> {
    private ArrayList<Teacher> mTeachers;
    customRecyclerOnItemClickListener mListener;
    Random rnd;


    @Inject
    public TeachersAdapter() {
        mTeachers = new ArrayList<>();
        rnd = new Random();
    }

    public void setListener(customRecyclerOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setTeachers(ArrayList<Teacher> teachers){
        mTeachers = teachers;
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        final Teacher teacher = mTeachers.get(position);
        holder.mTeacherName.setText(teacher.getName());
        holder.color.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        holder.mTeacherEmail.setText(teacher.getEmail());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null)
                    mListener.onTeacherClick(teacher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTeachers.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_hex_color) View color;
        @BindView(R.id.text_name) TextView mTeacherName;
        @BindView(R.id.text_email) TextView mTeacherEmail;
        @BindView(R.id.teacher_card_view) CardView mCardView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface customRecyclerOnItemClickListener {
        void onTeacherClick(Teacher teacher);
    }

}
