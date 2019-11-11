package com.example.wereadv10.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.ui.clubs.oneClub.events.Event;
import com.example.wereadv10.ui.clubs.oneClub.events.EventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class eventsTabFragment extends Fragment {
    private RecyclerView rvEvents;
    private EventsAdapter eventsAdapter;
    private List<Event> events = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_tab, container, false);
        rvEvents = view.findViewById(R.id.comingEvents_rv);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;


    }

//DB functions here


}