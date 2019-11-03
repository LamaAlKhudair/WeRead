package com.example.wereadv10.ui.clubs.oneClub.votes;

import android.content.Context;

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


import java.util.ArrayList;
import java.util.List;


public class VotesAdapter extends RecyclerView.Adapter<VotesAdapter.ViewHolder> {

    private List<Vote> VotesList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView voteTitle;
        private TextView voteDesc;
        private Button voteOneBtn, voteTwoBtn;

        private TextView voteOneRslt;
        private TextView voteOneRsltName;
        private TextView voteTwoRslt;
        private TextView voteTwoRsltName;
        private TextView totVotesrslt;

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

        }

    }


    public VotesAdapter(Context context, List<Vote> listData) {
        this.context = context;
        this.VotesList = listData;
    }

    @NonNull
    @Override
    public VotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voting_card,parent,false);
        return new VotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.wereadv10.ui.clubs.oneClub.votes.VotesAdapter.ViewHolder holder, int position) {
        Vote vote = VotesList.get(position);

        holder.voteTitle.setText(vote.getVote_title());
        holder.voteDesc.setText(vote.getVote_desc());
        holder.voteOneBtn.setText(vote.getOption1());
        holder.voteTwoBtn.setText(vote.getOption2());
        holder.totVotesrslt.setText(vote.getCounter_tot());
        holder.voteOneRsltName.setText(vote.getOption1());
        holder.voteTwoRsltName.setText(vote.getOption2());
        holder.voteOneRslt.setText(vote.getCounter_op1());
        holder.voteTwoRslt.setText(vote.getCounter_op2());
        holder.voteOnePrg.setMax( Integer.parseInt(vote.getCounter_tot()) );
        holder.voteTwoPrg.setMax( Integer.parseInt(vote.getCounter_tot()) );
        holder.voteOnePrg.setProgress( Integer.parseInt(vote.getCounter_op1()) );
        holder.voteTwoPrg.setProgress( Integer.parseInt(vote.getCounter_op2()) );

    }

    @Override
    public int getItemCount() {
        return VotesList.size();
    }

    public void clear(){
        VotesList.clear();
    }

}
