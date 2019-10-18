package com.example.wereadv10.ui.clubs.oneClub;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.otherProfile.OtherProfileActivity;
import com.example.wereadv10.ui.profile.profileTab.User;

import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder>{

    private Context mContext;
    private List<User> membersList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView member_name;
        public ImageView member_image;
        private CardView card;

        public MyViewHolder(View view) {
            super(view);
            member_name  = view.findViewById(R.id.member_name);
            member_image = view.findViewById(R.id.member_imageView);
            member_image.setOnClickListener(this);
            card = view.findViewById(R.id.member_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), OtherProfileActivity.class);
            intent.putExtra("otherUserID", membersList.get(getAdapterPosition()).getId());
            intent.putExtra("otherUserEmail", membersList.get(getAdapterPosition()).getEmail());

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public MembersAdapter(Context mContext, List<User> l) {
        this.mContext = mContext;
        this.membersList = l;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        User member = membersList.get(position);
        //Glide.with(mContext).load(member.getImage()).into(holder.member_image);
        holder.member_name.setText(member.getName());
        holder.member_image.setImageResource(member.getImage());
    }


    @Override
    public int getItemCount() {
        if (membersList!=null)
            return membersList.size();
        else return 0;
    }

    public void updateList(List<User> list){
        if (list.isEmpty()){
            Toast.makeText(this.mContext, "No readers join the club yet.", Toast.LENGTH_SHORT).show();
            membersList = new ArrayList<User>();
            notifyDataSetChanged();
        }else{
            membersList = new ArrayList<User>();
            membersList.addAll(list);
            notifyDataSetChanged();
        }
    }

}