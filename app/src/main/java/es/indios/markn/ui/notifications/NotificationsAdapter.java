package es.indios.markn.ui.notifications;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.R;
import es.indios.markn.data.model.user.MarknNotification;
import es.indios.markn.data.model.uvigo.Teacher;

/**
 * Created by guille on 11/03/18.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.LocationViewHolder> {
    private ArrayList<MarknNotification> mNotifications;
    customRecyclerOnItemClickListener mListener;


    @Inject
    public NotificationsAdapter() {
        mNotifications = new ArrayList<>();
    }

    public void setListener(customRecyclerOnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setNotifications(ArrayList<MarknNotification> notifications){
        mNotifications = notifications;
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        final MarknNotification notification = mNotifications.get(position);
        holder.mNotificationAuthor.setText(notification.getAuthor());
        holder.mNotificationTitle.setText(notification.getTitle());
        holder.mNotificationBody.setText(notification.getBody());
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification_author) TextView mNotificationAuthor;
        @BindView(R.id.notification_title) TextView mNotificationTitle;
        @BindView(R.id.notification_body) TextView mNotificationBody;

        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface customRecyclerOnItemClickListener {
        void onNotificationClick(MarknNotification notification);
    }

}
