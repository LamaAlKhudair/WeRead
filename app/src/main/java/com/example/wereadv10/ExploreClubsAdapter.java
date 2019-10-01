package com.example.wereadv10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.ui.clubs.Club;
import com.example.wereadv10.ui.clubs.oneClub.clubPage;

import java.util.ArrayList;
import java.util.List;

public class ExploreClubsAdapter extends RecyclerView.Adapter<ExploreClubsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Club> clubsList ;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView club_name;
        public TextView club_description;
        public ImageView club_image;
        private LinearLayout card;

        public MyViewHolder(View view) {
            super(view);
            club_name = view.findViewById(R.id.club_name);
            club_description = view.findViewById(R.id.club_description);

            club_image = view.findViewById(R.id.club_img);
            club_image.setOnClickListener(this);
            card = view.findViewById(R.id.card_club);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

          Intent intent = new Intent(view.getContext(), clubPage.class);
            intent.putExtra("NAME", clubsList.get(getAdapterPosition()).getClub_name());
            intent.putExtra("IMAGE", clubsList.get(getAdapterPosition()).getClub_image());
            intent.putExtra("OWNER", clubsList.get(getAdapterPosition()).getClub_owner());
            intent.putExtra("DESCRIPTION", clubsList.get(getAdapterPosition()).getClub_description());

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public ExploreClubsAdapter(Context mContext, List<Club> l) {
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

}
