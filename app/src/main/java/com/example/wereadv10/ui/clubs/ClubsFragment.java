package com.example.wereadv10.ui.clubs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.wereadv10.R;


public class ClubsFragment extends Fragment implements View.OnClickListener{

    private Button createClub;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_clubs, container, false);

        createClub = root.findViewById(R.id.createClub);
        createClub.setOnClickListener(this);

        return root;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createClub:
                startActivity(new Intent(getContext(), CreateClub.class));
                break;
        }//end switch
    }

}