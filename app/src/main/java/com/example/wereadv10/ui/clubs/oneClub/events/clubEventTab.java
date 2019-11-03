package com.example.wereadv10.ui.clubs.oneClub.events;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class clubEventTab extends Fragment {
    private static final String TAG = "Event fragment";

    private String clubID ;

    private RecyclerView rvEvents;
    private EventsAdapter rvEvents_adapter;
    private RecyclerView.LayoutManager rvEvents_LayoutManager;

    private List<Event> AllEvents = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp;


        public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.club_event_tab, container, false);
        clubID = getActivity().getIntent().getExtras().getString("CLUB_ID");

        rvEvents = root.findViewById(R.id.events_rv);
        rvEvents_LayoutManager = new LinearLayoutManager(clubEventTab.this.getContext());
        rvEvents.setLayoutManager ( rvEvents_LayoutManager );
        rvEvents_adapter = new EventsAdapter(getContext(), AllEvents);
        rvEvents.setAdapter(rvEvents_adapter);
        dbSetUp = new dbSetUp();



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AllEvents.clear();
        getAllEvent();
    }

    private List<Event> getAllEvent() {

        System.out.println("In all events "+ AllEvents.isEmpty());
        final CollectionReference eventRef = dbSetUp.db.collection("events");
        eventRef.whereEqualTo("club_id", clubID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Event event = new Event();

                                String event_id = document.get("event_id").toString();
                                String event_name = document.get("event_name").toString();
                                String event_date = document.get("event_date").toString();
                                String event_desc = document.get("event_desc").toString();
                                String event_location = document.get("event_location").toString();
                                String event_time = document.get("event_time").toString();


                                event.setEvent_id(event_id);
                                event.setEvent_name(event_name);
                                event.setEvent_date(event_date);
                                event.setEvent_location(event_location);
                                event.setEvent_time(event_time);
                                event.setEvent_desc(event_desc);
                                event.setClub_id(clubID);

                                AllEvents.add(event);

                                rvEvents_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        return AllEvents;
    }
}




















