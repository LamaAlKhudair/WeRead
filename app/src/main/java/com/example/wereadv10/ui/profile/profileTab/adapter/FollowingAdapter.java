package com.example.wereadv10.ui.profile.profileTab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.profile.profileTab.User;

import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    private List<User> listData;
    private Context context;
    private OnFollowListener onFollowListener;

    public FollowingAdapter(Context context, List<User> listData, OnFollowListener onFollowListener) {
        this.context = context;
        this.listData = listData;
        this.onFollowListener = onFollowListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_card, parent, false);
        return new ViewHolder(view, onFollowListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User ld = listData.get(position);
        holder.txtname.setText(ld.getName());

        //  Glide.with(context).load(video.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtname;
        private OnFollowListener onFollowListener;

        public ViewHolder(View itemView, OnFollowListener onFollowListener) {
            super(itemView);
            txtname = itemView.findViewById(R.id.following_name);
            this.onFollowListener = onFollowListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String email = listData.get(getAdapterPosition()).getEmail();
            String userID = listData.get(getAdapterPosition()).getId();
            //  onFollowListener.onDepartmentClick(name, departmentList.get(getAdapterPosition()).getId());
            onFollowListener.onFollowUserClick(userID,email);
        }//end onClick
    }

    public interface OnFollowListener {
        void onFollowUserClick(String userID,String userEmail);
    }//end interface
}
