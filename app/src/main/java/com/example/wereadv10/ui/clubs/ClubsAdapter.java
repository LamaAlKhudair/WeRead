package com.example.wereadv10.ui.clubs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.clubPage;

import java.util.ArrayList;
import java.util.List;

public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.MyViewHolder>{

        private Context mContext;
        private List<Club> clubsList = new ArrayList<>();


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView club_name;
            public TextView club_description;
            public ImageView club_image;
            private CardView card;

            public MyViewHolder(View view) {
                super(view);
                club_name =  view.findViewById(R.id.club_name);
                club_description = view.findViewById(R.id.club_description);
                club_image = view.findViewById(R.id.club_img);
                club_image.setOnClickListener(this);
                card = view.findViewById(R.id.club_card);
                card.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), clubPage.class);
            intent.putExtra("CLUB_ID", clubsList.get(getAdapterPosition()).getID());
            intent.putExtra("NAME", clubsList.get(getAdapterPosition()).getClub_name());
            intent.putExtra("IMAGE", clubsList.get(getAdapterPosition()).getClub_image());
            intent.putExtra("OWNER", clubsList.get(getAdapterPosition()).getClub_owner());
            intent.putExtra("DESCRIPTION", clubsList.get(getAdapterPosition()).getClub_description());

            mContext.startActivity(intent);
            }

        }//End MyViewHolder Class


        public ClubsAdapter(Context mContext, List<Club> l) {
            this.mContext = mContext;
            this.clubsList = l;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.club_card, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Club club = clubsList.get(position);
            Glide.with(mContext).load(club.getClub_image()).into(holder.club_image);
            holder.club_name.setText(club.getClub_name());
            holder.club_description.setText(club.getClub_description());
        }


        @Override
        public int getItemCount() {
            if (clubsList!=null)
                return clubsList.size();
            else return 0;
        }

    public void updateList(List<Club> list){
        if (list.isEmpty()){
            Toast.makeText(this.mContext, "No club found", Toast.LENGTH_SHORT).show();
            clubsList = new ArrayList<Club>();
            notifyDataSetChanged();
        }else{
            clubsList = new ArrayList<Club>();
            clubsList.addAll(list);
            notifyDataSetChanged();
        }
    }

}
