package com.example.wereadv10.ui.clubs.oneClub.votes;


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


public class clubVotingTab extends Fragment {
    private static final String TAG = "Vote fragment";

    private RecyclerView rvVotes;
    private VotesAdapter rvVotes_adapter;
    private RecyclerView.LayoutManager rvVotes_LayoutManager;

    private List<Vote> AllVotes = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.club_voting_tab, container, false);

        rvVotes = root.findViewById(R.id.votes_rv);
        rvVotes_LayoutManager = new LinearLayoutManager(clubVotingTab.this.getContext());
        rvVotes.setLayoutManager ( rvVotes_LayoutManager );

        rvVotes_adapter = new VotesAdapter(getContext(), AllVotes);

        rvVotes.setAdapter(rvVotes_adapter);

        dbSetUp = new dbSetUp();

        getAllVotes();

        return root;
    }

    private List<Vote> getAllVotes() {


        CollectionReference voteRef = dbSetUp.db.collection("votes");
        voteRef.get()
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


                                vote.setOption1(option_one);
                                vote.setOption2(option_two);
                                vote.setVote_desc(vote_desc);
                                vote.setVote_title(vote_title);

                                //  if (club id == 212131)
                                AllVotes.add(vote);

                                rvVotes_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        return AllVotes;

    }
}