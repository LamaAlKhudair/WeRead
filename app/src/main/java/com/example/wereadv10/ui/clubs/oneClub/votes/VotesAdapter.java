package com.example.wereadv10.ui.clubs.oneClub.votes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.votes.Vote;
import com.example.wereadv10.ui.clubs.oneClub.votes.VotesAdapter;

import java.util.List;



public class VotesAdapter extends RecyclerView.Adapter<com.example.wereadv10.ui.clubs.oneClub.votes.VotesAdapter.ViewHolder>{


    private List<Vote> VotesList;
    private Context context;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    public static int countTot=0;



    public VotesAdapter(Context context, List<Vote> listData) {
        this.context = context;
        this.VotesList = listData;
    }

    @NonNull
    @Override
    public com.example.wereadv10.ui.clubs.oneClub.votes.VotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.voting_card,parent,false);
        return new com.example.wereadv10.ui.clubs.oneClub.votes.VotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.wereadv10.ui.clubs.oneClub.votes.VotesAdapter.ViewHolder holder, int position) {
        Vote vote = VotesList.get(position);
        holder.voteTitle.setText(vote.getVote_title());
        holder.optionOne.setText(vote.getOption1());
        holder.voteDesc.setText(vote.getVote_desc());
        holder.optionTwo.setText(vote.getOption2());

        holder.voteOneRsltName.setText(vote.getOption1());
        holder.voteTwoRsltName.setText(vote.getOption2());

    }


    @Override
    public int getItemCount() {
        return VotesList.size();
    }
    public void clear(){
        VotesList.clear();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView optionOne;
        private TextView optionTwo;
        private TextView voteTitle;
        private TextView voteDesc;


        private TextView voteOneRslt, voteOneRsltName;
        private TextView voteTwoRslt, voteTwoRsltName;
        private TextView totVotesrslt;
        private Button voteOneBtn, voteTwoBtn;
        private ProgressBar voteOnePrg, voteTwoPrg;

        public ViewHolder(View itemView) {
            super(itemView);
            voteTitle = itemView.findViewById(R.id.vote_title);
            optionOne = itemView.findViewById(R.id.option_one);
            optionTwo = itemView.findViewById(R.id.option_two);
            voteDesc = itemView.findViewById(R.id.vote_desc);

            voteOneRslt = itemView.findViewById(R.id.tv_op1_count);
            voteTwoRslt = itemView.findViewById(R.id.tv_op2_count);
            totVotesrslt = itemView.findViewById(R.id.tv_total_num);
            voteOneRsltName = itemView.findViewById(R.id.tv_op1_res);
            voteTwoRsltName = itemView.findViewById(R.id.tv_op2_res);

            voteOnePrg = itemView.findViewById(R.id.op1_PB);
            voteTwoPrg = itemView.findViewById(R.id.op2_PB);

            voteOneBtn = itemView.findViewById(R.id.option_one);
            voteTwoBtn = itemView.findViewById(R.id.option_two);


            voteOneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vote1Count  =  voteOneRslt.getText().toString().trim();
                    int countO = Integer.parseInt(vote1Count); //converting string into int
                    countO++;

                    if(countO == 1){
                        voteOneBtn .setVisibility(View.GONE);
                        voteTwoBtn .setVisibility(View.GONE);
                        voteOneBtn.setEnabled(false);
                       // voteOneBtn.setClickable(false);
                        voteTwoBtn.setEnabled(false);
                        //voteTwoBtn.setClickable(false);
                    }

                    countTot++;
                    voteOneRslt.setText(String.valueOf(countO)); //setting the test back (updated)
                    totVotesrslt.setText(String.valueOf(countTot));
                    voteOnePrg.setProgress(countO);
                }
            });

            voteTwoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vote2Count  =  voteTwoRslt.getText().toString().trim();
                    int countT = Integer.parseInt(vote2Count); //converting string into int
                    countT++;

                    if(countT == 1){
                        voteTwoBtn .setVisibility(View.GONE);
                        voteOneBtn .setVisibility(View.GONE);
                        voteTwoBtn.setEnabled(false);
                        //voteTwoBtn.setClickable(false);
                        voteOneBtn.setEnabled(false);
                        //voteOneBtn.setClickable(false);
                    }

                    countTot++;
                    voteTwoRslt.setText(String.valueOf(countT)); //setting the test back (updated)
                    totVotesrslt.setText(String.valueOf(countTot));
                    voteTwoPrg.setProgress(countT);

                }
            });
        }
    }
}
