package com.example.wereadv10.ui.clubs.oneClub.votes;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;


public class VotesAdapter extends RecyclerView.Adapter<VotesAdapter.ViewHolder> {

    private List<Vote> VotesList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView voteTitle;
        private TextView voteDesc;
        private Button voteOneBtn, voteTwoBtn;

        private TextView voteOneRslt;
        private TextView voteOneRsltName;
        private TextView voteTwoRslt;
        private TextView voteTwoRsltName;
        private TextView totVotesrslt;
        private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

        private ProgressBar voteOnePrg, voteTwoPrg;

        private String counter_op1;
        private String counter_op2;
        private String counter_tot;

        float op1, op2, tot;


        public ViewHolder(View itemView) {

            super(itemView);

            voteTitle = itemView.findViewById(R.id.vote_title);
            voteDesc = itemView.findViewById(R.id.vote_desc);
            voteOneBtn = itemView.findViewById(R.id.option_one_button);
            voteTwoBtn = itemView.findViewById(R.id.option_two_button);

            totVotesrslt = itemView.findViewById(R.id.tv_total_num);

            voteOneRsltName = itemView.findViewById(R.id.tv_op1_res);
            voteTwoRsltName = itemView.findViewById(R.id.tv_op2_res);

            voteOneRslt = itemView.findViewById(R.id.tv_op1_count);
            voteTwoRslt = itemView.findViewById(R.id.tv_op2_count);

            voteOnePrg = itemView.findViewById(R.id.op1_PB);
            voteTwoPrg = itemView.findViewById(R.id.op2_PB);

            voteOneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateOpt1(VotesList.get(getAdapterPosition()).getVote_id());
                }
            });

            voteTwoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateOpt2(VotesList.get(getAdapterPosition()).getVote_id());
                }
            });


        }


        private String getUserID() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String userID="";
            if (user != null) {
                userID = user.getUid();
            }
            return userID;
        }

        private void updateOpt1(final String voteId ){

            CollectionReference voteRef = dbSetUp.db.collection("votes");
            voteRef.whereEqualTo("vote_id", voteId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getData();

                                    counter_op1 = document.get("counter_op1").toString();
                                    counter_op2 = document.get("counter_op2").toString();
                                    counter_tot = document.get("counter_tot").toString();

                                    op1 = Float.parseFloat(counter_op1);
                                    op2 = Float.parseFloat(counter_op2);
                                    tot = Float.parseFloat(counter_tot);


                                    tot++;
                                    counter_tot = String.valueOf((int) tot);
                                    totVotesrslt.setText(counter_tot);


                                    op1++;
                                    counter_op1 = String.valueOf((int) op1);
                                    float op1_percentage = (op1/tot)*100;
                                    voteOneRslt.setText(roundOffTo2DecPlaces(op1_percentage));

                                    float op2_percentage = (op2/tot)*100;
                                    voteTwoRslt.setText(roundOffTo2DecPlaces(op2_percentage));


                                    voteOnePrg.setMax((int) tot);
                                    voteTwoPrg.setMax((int) tot);
                                    voteOnePrg.setProgress((int) op1);



                                    final Map<String, Object> vote = new HashMap<>();
                                    vote.put("counter_op1", counter_op1);
                                    vote.put("counter_tot", counter_tot);

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

                                }

                            } else Log.w(TAG, "Error getting documents.", task.getException());

                        }
                    });


            addVote(voteId);
            hideButtons();
        }

        private void updateOpt2(final String voteId){

            CollectionReference voteRef = dbSetUp.db.collection("votes");
            voteRef.whereEqualTo("vote_id", voteId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getData();

                                    counter_op1 = document.get("counter_op1").toString();
                                    counter_op2 = document.get("counter_op2").toString();
                                    counter_tot = document.get("counter_tot").toString();

                                    op1 = Float.parseFloat(counter_op1);
                                    op2 = Float.parseFloat(counter_op2);
                                    tot = Float.parseFloat(counter_tot);


                                    tot++;
                                    counter_tot = String.valueOf((int) tot);
                                    totVotesrslt.setText(counter_tot);

                                    op2++;
                                    counter_op2 = String.valueOf((int) op2);
                                    float op2_percentage = (op2/tot)*100;
                                    voteTwoRslt.setText(roundOffTo2DecPlaces(op2_percentage));

                                    float op1_percentage = (op1/tot)*100;
                                    voteOneRslt.setText(roundOffTo2DecPlaces(op1_percentage));

                                    voteOnePrg.setMax((int) tot );
                                    voteTwoPrg.setMax((int) tot );
                                    voteTwoPrg.setProgress((int) op2);


                                    final Map<String, Object> vote = new HashMap<>();
                                    vote.put("counter_op2", counter_op2);
                                    vote.put("counter_tot", counter_tot);


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

                                }

                            }
                            else Log.w(TAG, "Error getting documents.", task.getException());

                        }

                    });

            addVote(voteId);
            hideButtons();

        }

        private void addVote(String voteId){

            final Map<String, Object> addVote = new HashMap<>();
            addVote.put("member_id", getUserID());
            addVote.put("vote_id", voteId);

            dbSetUp.db.collection("vote_participation")
                    .document(getRandom())
                    .set(addVote).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error writing document", e);
                        }
                    });

        }

        private String roundOffTo2DecPlaces(float val)
        {
            return String.format("%.1f", val);
        }

        private String getRandom() {

            return UUID.randomUUID().toString();
        }

        private void hideButtons() {
            voteOneBtn.setVisibility(View.GONE);
            voteTwoBtn.setVisibility(View.GONE);
        }

    }


    public VotesAdapter(Context context, List<Vote> listData ) {//,OnButtonListener onButtonListener
        this.context = context;
        this.VotesList = listData;
        //this.mOnButtonListener = onButtonListener;
    }

    @NonNull
    @Override
    public VotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voting_card,parent,false);
        return new VotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Vote vote = VotesList.get(position);

        holder.voteTitle.setText( vote.getVote_title() );
        holder.voteDesc.setText( vote.getVote_desc() );

        holder.voteOneBtn.setText( vote.getOption1() );
        holder.voteTwoBtn.setText( vote.getOption2() );

        //Visibility
        holder.voteOneBtn.setVisibility(View.GONE);
        holder.voteTwoBtn.setVisibility(View.GONE);

        holder.totVotesrslt.setText( vote.getCounter_tot() );

        if(Integer.parseInt(vote.getCounter_tot()) != 0) {

            float percentageOp1 = (Float.parseFloat(vote.getCounter_op1()) / Float.parseFloat(vote.getCounter_tot())) * 100;
            holder.voteOneRslt.setText( roundOffTo2DecPlaces(percentageOp1) );

            float percentageOp2 = (Float.parseFloat(vote.getCounter_op2()) / Float.parseFloat(vote.getCounter_tot())) * 100;
            holder.voteTwoRslt.setText( roundOffTo2DecPlaces(percentageOp2) );

        }else{
            holder.voteOneRslt.setText("0");
            holder.voteTwoRslt.setText("0");
        }

        holder.voteOneRsltName.setText( vote.getOption1() );
        holder.voteTwoRsltName.setText( vote.getOption2() );

        holder.voteOnePrg.setMax( (int) Float.parseFloat(vote.getCounter_tot()) );
        holder.voteTwoPrg.setMax( (int) Float.parseFloat(vote.getCounter_tot()) );
        holder.voteOnePrg.setProgress( (int) Float.parseFloat(vote.getCounter_op1()) );
        holder.voteTwoPrg.setProgress( (int) Float.parseFloat(vote.getCounter_op2()) );


        // show option buttons when user member in the club
        CollectionReference clubMember = dbSetUp.db.collection("club_members");
        clubMember.whereEqualTo("club_id", vote.getClub_id()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            String userNow = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            boolean member = false;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String member_id = document.get("member_id").toString();
                                if (member_id.equalsIgnoreCase(userNow)) {
                                    holder.voteOneBtn.setVisibility(View.VISIBLE);
                                    holder.voteTwoBtn.setVisibility(View.VISIBLE);
                                    member = true;
                                    break;
                                }
                            }
                            if(member){ //is member

                                // hide option buttons when member already voted
                                CollectionReference VotePart = dbSetUp.db.collection("vote_participation");
                                VotePart.whereEqualTo("vote_id", VotesList.get(position).getVote_id()).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        String member_id = document.get("member_id").toString();
                                                        if (member_id.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                            holder.voteOneBtn.setVisibility(View.GONE);
                                                            holder.voteTwoBtn.setVisibility(View.GONE);                                }
                                                    }


                                                } else Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        });

                            }//end if member

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });

    }

    private String roundOffTo2DecPlaces(float val)
    {
        return String.format("%.1f", val);
    }


    @Override
    public int getItemCount() {
        return VotesList.size();
    }

    public void clear(){
        VotesList.clear();
    }

}
