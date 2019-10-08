package com.example.wereadv10.ui.clubs.oneClub.votes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



        }


        @Override
        public int getItemCount() {
            return VotesList.size();
        }
        public void clear(){
            VotesList.clear();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView optionOne;
            private TextView optionTwo;
            private TextView voteTitle;
            private TextView voteDesc;
            public ViewHolder(View itemView) {
                super(itemView);
                voteTitle = itemView.findViewById(R.id.vote_title);
                optionOne = itemView.findViewById(R.id.option_one);
                optionTwo = itemView.findViewById(R.id.option_two);
                voteDesc = itemView.findViewById(R.id.vote_desc);

            }
        }



    }
