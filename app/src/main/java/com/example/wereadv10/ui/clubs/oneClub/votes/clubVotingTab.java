package com.example.wereadv10.ui.clubs.oneClub.votes;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class clubVotingTab extends Fragment{// implements VotesAdapter.OnButtonListener
    private static final String TAG = "Vote fragment";

    private RecyclerView rvVotes;
    private VotesAdapter rvVotes_adapter;
    private RecyclerView.LayoutManager rvVotes_LayoutManager;

    private List<Vote> AllVotes = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp;
    private String clubID ;

/*    private Button voteOneBtn, voteTwoBtn;
    private TextView voteOneRslt;
    private TextView voteTwoRslt;
    private ProgressBar voteOnePrg, voteTwoPrg;
    private TextView totVotesrslt;*/



    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.club_voting_tab, container, false);
        clubID = getActivity().getIntent().getExtras().getString("CLUB_ID");


        rvVotes = root.findViewById(R.id.votes_rv);
        rvVotes_LayoutManager = new LinearLayoutManager(clubVotingTab.this.getContext());
        rvVotes.setLayoutManager ( rvVotes_LayoutManager );

        rvVotes_adapter = new VotesAdapter (getContext(), AllVotes);//,this

        rvVotes.setAdapter(rvVotes_adapter);


        dbSetUp = new dbSetUp();


/*        voteOneBtn = root.findViewById(R.id.option_one_button);
        voteTwoBtn = root.findViewById(R.id.option_two_button);
        totVotesrslt = root.findViewById(R.id.tv_total_num);
        voteOneRslt = root.findViewById(R.id.tv_op1_count);
        voteTwoRslt = root.findViewById(R.id.tv_op2_count);
        voteOnePrg = root.findViewById(R.id.op1_PB);
        voteTwoPrg = root.findViewById(R.id.op2_PB);*/


        getAllVotes();

        return root;
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


/*    private void updateOpt1(String voteId){

        String counter_op1, counter_tot;
        counter_op1 = voteOneRslt.getText().toString();
        counter_tot = totVotesrslt.getText().toString();


        int op1, op2, tot;
        op1 = Integer.parseInt(counter_op1);
        tot = Integer.parseInt(counter_tot);


        op1++;
        counter_op1 = String.valueOf(op1);
        voteOneRslt.setText(counter_op1);

        tot++;
        counter_tot = String.valueOf(tot);
        totVotesrslt.setText(counter_tot);

        voteOnePrg.setProgress(op1);
        voteOnePrg.setMax(tot);
        voteTwoPrg.setMax(tot);


        final Map<String, Object> vote = new HashMap<>();
        vote.put("counter_op1", counter_op1);
        vote.put("counter_tot", counter_tot);

        //in the next line I want to use the vote_id to edit the document
        //dbSetUp.db.collection("votes").whereEqualTo("vote_id", vote_id)
        dbSetUp.db.collection("votes").whereEqualTo("vote_id", voteId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();

                                dbSetUp.db
                                        .collection("votes")
                                        .document(id).update(vote);
                            }

                        } else {
                            System.out.println( "Error getting documents: ");
                        }
                    }
                });

        hideButtons();

    }*/

/*    private void updateOpt2(){

        String counter_op2, counter_tot;
        counter_op2 = voteTwoRslt.getText().toString();
        counter_tot = totVotesrslt.getText().toString();


        int op2, tot;
        op2 = Integer.parseInt(counter_op2);
        tot = Integer.parseInt(counter_tot);


        op2++;
        counter_op2 = String.valueOf(op2);
        voteOneRslt.setText(counter_op2);

        tot++;
        counter_tot = String.valueOf(tot);
        totVotesrslt.setText(counter_tot);

        voteTwoPrg.setProgress(op2);
        voteOnePrg.setMax(tot);
        voteTwoPrg.setMax(tot);


        final Map<String, Object> vote = new HashMap<>();
        vote.put("counter_op2", counter_op2);
        vote.put("counter_tot", counter_tot);

        //in the next line I want to use the vote_id to edit the document
        //dbSetUp.db.collection("votes").whereEqualTo("vote_id", vote_id)
        dbSetUp.db.collection("votes").whereEqualTo("vote_id", "**HERE**")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();

                                dbSetUp.db
                                        .collection("votes")
                                        .document(id).update(vote);
                            }

                        } else {
                            System.out.println( "Error getting documents: ");
                        }
                    }
                });

        hideButtons();

    }*/


/*    private void hideButtons(){
        voteOneBtn.setVisibility(View.GONE);
        voteTwoBtn.setVisibility(View.GONE);

    }*/


/*    @Override
    public void onButtonOneClick(String voteId,int position) {
       updateOpt1(voteId);
        Toast.makeText(getContext(), "vote Id is" + voteId + "!", Toast.LENGTH_SHORT).show();

    }*/
}