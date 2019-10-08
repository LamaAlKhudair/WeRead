
package com.example.wereadv10.ui.clubs.oneClub.events;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private Context mcontext;
    private List<Event> EventsList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView event_name;
        private TextView event_date;
        private TextView event_desc;
        private TextView event_location;
        private TextView event_time;

        private LinearLayout card;


        public MyViewHolder(View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.event_name);
            event_date = itemView.findViewById(R.id.event_date);
            event_time = itemView.findViewById(R.id.event_time);
            event_desc = itemView.findViewById(R.id.event_description);
            event_location = itemView.findViewById(R.id.event_location);
            card = itemView.findViewById(R.id.card_club);
        }

/*
    @Override
    public void onClick(View view) {

        Intent intent = new Intent(view.getContext(), clubPage.class);
        intent.putExtra("NAME", clubsList.get(getAdapterPosition()).getClub_name());
        intent.putExtra("IMAGE", clubsList.get(getAdapterPosition()).getClub_image());
        intent.putExtra("OWNER", clubsList.get(getAdapterPosition()).getClub_owner());
        intent.putExtra("DESCRIPTION", clubsList.get(getAdapterPosition()).getClub_description());

        mContext.startActivity(intent);
    }*/

}//End MyViewHolder Class


    public EventsAdapter(Context context, List<Event> listData) {
        this.mcontext = context;
        this.EventsList = listData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Event event = EventsList.get(position);
        holder.event_name.setText(event.getEvent_name());
        holder.event_desc.setText(event.getEvent_desc());
        holder.event_date.setText(event.getEvent_date());
        holder.event_time.setText(event.getEvent_time());
        holder.event_location.setText(event.getEvent_location());
    }


    @Override
    public int getItemCount() {
        if (EventsList!=null)
            return EventsList.size();
        else return 0;
    }


}


