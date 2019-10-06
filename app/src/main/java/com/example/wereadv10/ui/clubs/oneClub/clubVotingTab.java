package com.example.wereadv10.ui.clubs.oneClub;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.wereadv10.R;


public class clubVotingTab extends Fragment {
    private TextView voting;


    ////// public clubVotingTab() {
    // Required empty public constructor
    ////// }


    ////@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.club_voting_tab, container, false);
        voting =(TextView)view.findViewById(R.id.club_voting);
        voting.setText(getActivity().getIntent().getExtras().getString("VOTING"));
        return view;
    }

}

