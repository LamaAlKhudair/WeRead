package com.example.wereadv10.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.events.Event;
import com.example.wereadv10.ui.clubs.oneClub.events.EventsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class eventsTabFragment extends Fragment {
    private RecyclerView rvEvents;
    private EventsAdapter eventsAdapter;
    private List<Event> AllEvent = new ArrayList<>();
    private FirebaseUser user;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    private String TAG = eventsTabFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_tab, container, false);
        rvEvents = view.findViewById(R.id.comingEvents_rv);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        eventsAdapter = new EventsAdapter(getContext(), AllEvent);
        rvEvents.setAdapter(eventsAdapter);
        user = FirebaseAuth.getInstance().getCurrentUser();

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        AllEvent.clear();
        getUserEvent();
    }

    private void getUserEvent(){
    String userID = user.getUid();
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    Date today = new Date();
    Date todayWithZeroTime;
    try{
        todayWithZeroTime = formatter.parse(formatter.format(today));}
    catch (ParseException e){
        todayWithZeroTime = new Date();
    }

    final Date finalTodayWithZeroTime = todayWithZeroTime;
    dbSetUp.db.collection("event_attendees").whereEqualTo("member_id", userID).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            final Event event = new Event();
                            final String eventID = document.get("event_id").toString();
                            final CollectionReference eventRef = dbSetUp.db.collection("events");
                            eventRef.whereEqualTo("event_id", eventID).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String updateEvent_name = document.get("event_name").toString();
                                                    String updateEvent_date = document.get("event_date").toString();
                                                    String updateEvent_desc = document.get("event_desc").toString();
                                                    String updateEvent_location = document.get("event_location").toString();
                                                    String updateEvent_time = document.get("event_time").toString();
                                                    String ClubId = document.get("club_id").toString();

                                                    event.setEvent_name(updateEvent_name);
                                                    event.setEvent_date(updateEvent_date);
                                                    event.setEvent_time(updateEvent_time);
                                                    event.setEvent_desc(updateEvent_desc);
                                                    event.setEvent_location(updateEvent_location);
                                                    event.setClub_id(ClubId);
                                                    event.setEvent_id(eventID);

                                                    try{
                                                        Date strDate = sdf.parse(updateEvent_date);
                                                        if (finalTodayWithZeroTime.after(strDate) ) {
                                                            // ended event
                                                        }else if (finalTodayWithZeroTime.equals(strDate)){
                                                            AllEvent.add(event);
                                                        }
                                                        else{
                                                            AllEvent.add(event);
                                                        }

                                                    } catch (ParseException e) {
                                                        System.out.println("ParseException");
                                                        System.out.println(e);
                                                    }
                                                }

                                            } else ;
                                            sortFunc();
                                            eventsAdapter.notifyDataSetChanged();
                                        }

                                    });

                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }

                }

            });
    }
    private void sortFunc(){
        Collections.sort(AllEvent, new Comparator<Event>() {
            @Override
            public int compare(Event eventDate, Event t1) {
                Date idea1 = new Date(eventDate.getEvent_date());// here pass rating value.
                Date idea2 = new Date(t1.getEvent_date());// here pass rating value.
                return idea1.compareTo(idea2);
            }
        });
    }
}