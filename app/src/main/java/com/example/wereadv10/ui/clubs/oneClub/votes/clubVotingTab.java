package com.example.wereadv10.ui.clubs.oneClub.votes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class clubVotingTab extends Fragment{// implements VotesAdapter.OnButtonListener
    private static final String TAG = "Vote fragment";

    private RecyclerView rvVotes;
    private VotesAdapter rvVotes_adapter;
    private RecyclerView.LayoutManager rvVotes_LayoutManager;

    private List<Vote> AllVotes = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp;
    private String clubID ;

    private Button voteOneBtn, voteTwoBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.club_voting_tab, container, false);
        clubID = getActivity().getIntent().getExtras().getString("CLUB_ID");


        rvVotes = root.findViewById(R.id.votes_rv);
        rvVotes_LayoutManager = new LinearLayoutManager(clubVotingTab.this.getContext());
        rvVotes.setLayoutManager ( rvVotes_LayoutManager );

        rvVotes_adapter = new VotesAdapter (getContext(), AllVotes);

        rvVotes.setAdapter(rvVotes_adapter);

        dbSetUp = new dbSetUp();

        voteOneBtn = root.findViewById(R.id.option_one_button);
        voteTwoBtn = root.findViewById(R.id.option_two_button);


        //getAllVotes();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AllVotes.clear();
        getAllVotes();
    }
    private List<Vote> getAllVotes() {

        CollectionReference voteRef = dbSetUp.db.collection("votes");
        voteRef.whereEqualTo("club_id", clubID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Vote vote = new Vote();

                                String option_one = document.get("option_one").toString();
                                String option_two = document.get("option_two").toString();
                                String vote_desc = document.get("vote_desc").toString();
                                String vote_title = document.get("vote_title").toString();
                                String counter_op1 = document.get("counter_op1").toString();
                                String counter_op2 = document.get("counter_op2").toString();
                                String counter_tot = document.get("counter_tot").toString();
                                String vote_id = document.get("vote_id").toString();
                                String club_id = document.get("club_id").toString();


                                vote.setClub_id(club_id);
                                vote.setVote_id(vote_id);
                                vote.setCounter_tot(counter_tot);
                                vote.setCounter_op1(counter_op1);
                                vote.setCounter_op2(counter_op2);
                                vote.setOption1(option_one);
                                vote.setOption2(option_two);
                                vote.setVote_desc(vote_desc);
                                vote.setVote_title(vote_title);

                                AllVotes.add(vote);

                                rvVotes_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        return AllVotes;
    }


}