
package com.example.wereadv10.ui.clubs.oneClub.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.events.Event;
import com.example.wereadv10.ui.clubs.oneClub.events.EventsAdapter;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> EventsList;
    private Context context;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    public EventsAdapter(Context context, List<Event> listData) {
        this.context = context;
        this.EventsList = listData;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {
        Event event = EventsList.get(position);
        holder.clubTime.setText(event.getEvent_time());
        holder.clubDate.setText(event.getEvent_date());
        holder.eventLocation.setText(event.getEvent_location());
        holder.eventName.setText(event.getEvent_name());



    }


    @Override
    public int getItemCount() {
        return EventsList.size();
    }
    public void clear(){
        EventsList.clear();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView clubTime;
        private TextView clubDate;
        private TextView eventLocation;
        private TextView eventName;
        public ViewHolder(View itemView) {
            super(itemView);
            clubTime = itemView.findViewById(R.id.club_time);
            clubDate = itemView.findViewById(R.id.club_date);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventName = itemView.findViewById(R.id.event_name);
        }
    }




}


