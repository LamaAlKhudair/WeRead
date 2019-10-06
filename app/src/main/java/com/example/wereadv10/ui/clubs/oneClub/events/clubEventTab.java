package com.example.wereadv10.ui.clubs.oneClub.events;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.wereadv10.R;


public class clubEventTab extends Fragment {
    private TextView event;


    ////// public clubEventTab() {
    // Required empty public constructor
    ////// }


    ////@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.club_event_tab, container, false);
        //event=(TextView)view.findViewById(R.id.club_event);
        //event.setText(getActivity().getIntent().getExtras().getString("EVENT"));
        return view;
    }

}