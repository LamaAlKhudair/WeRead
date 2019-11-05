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
   // private OnButtonListener mOnButtonListener;

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

/*            this.onButtonListener = onButtonListener;
            voteOneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonListener.onButtonOneClick(VotesList.get(getAdapterPosition()).getVote_id(),getAdapterPosition());
                }
            });*/

        }


        private String getUserID() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String userID="";
            if (user != null) {
                userID = user.getUid();
            }
            return userID;
        }

        private void updateOpt1(final String voteId){

            String counter_op1, counter_tot;
            counter_op1 = voteOneRslt.getText().toString();
            counter_tot = totVotesrslt.getText().toString();

            int op1, tot;
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
            addVote(voteId);
            hideButtons();
        }

        private void updateOpt2(String voteId){

            String counter_op2, counter_tot;
            counter_op2 = voteTwoRslt.getText().toString();
            counter_tot = totVotesrslt.getText().toString();


            int op2, tot;
            op2 = Integer.parseInt(counter_op2);
            tot = Integer.parseInt(counter_tot);


            op2++;
            counter_op2 = String.valueOf(op2);
            voteTwoRslt.setText(counter_op2);

            tot++;
            counter_tot = String.valueOf(tot);
            totVotesrslt.setText(counter_tot);

            voteTwoPrg.setProgress(op2);
            voteOnePrg.setMax(tot);
            voteTwoPrg.setMax(tot);


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
        return new VotesAdapter.ViewHolder(view);//,mOnButtonListener
    }

    @Override
    public void onBindViewHolder(@NonNull final VotesAdapter.ViewHolder holder, final int position) {
        Vote vote = VotesList.get(position);

        holder.voteTitle.setText( vote.getVote_title() );
        holder.voteDesc.setText( vote.getVote_desc() );
        holder.voteOneBtn.setText( vote.getOption1() );
        holder.voteTwoBtn.setText( vote.getOption2() );
        holder.totVotesrslt.setText( vote.getCounter_tot() );
        holder.voteOneRsltName.setText( vote.getOption1() );
        holder.voteTwoRsltName.setText( vote.getOption2() );
        holder.voteOneRslt.setText( vote.getCounter_op1() );
        holder.voteTwoRslt.setText( vote.getCounter_op2() );
        holder.voteOnePrg.setMax( Integer.parseInt(vote.getCounter_tot()) );
        holder.voteTwoPrg.setMax( Integer.parseInt(vote.getCounter_tot()) );
        holder.voteOnePrg.setProgress( Integer.parseInt(vote.getCounter_op1()) );
        holder.voteTwoPrg.setProgress( Integer.parseInt(vote.getCounter_op2()) );

        // Hide option buttons when member already voted
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
                                    holder.voteTwoBtn.setVisibility(View.GONE);
                                }
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });


        holder.voteOneBtn.setVisibility(View.GONE);
        holder.voteTwoBtn.setVisibility(View.GONE);

        // Hide option buttons when not member in the club
        CollectionReference clubMember = dbSetUp.db.collection("club_members");
        clubMember.whereEqualTo("club_id", vote.getClub_id()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userNow = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String member_id = document.get("member_id").toString();
                                if (member_id.equalsIgnoreCase(userNow)) {
                                    holder.voteOneBtn.setVisibility(View.VISIBLE);
                                    holder.voteTwoBtn.setVisibility(View.VISIBLE);
                                }
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });

    }


    @Override
    public int getItemCount() {
        return VotesList.size();
    }

    public void clear(){
        VotesList.clear();
    }

}
