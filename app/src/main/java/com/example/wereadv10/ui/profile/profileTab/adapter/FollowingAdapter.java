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

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder>{
private List<User>listData;
private     Context context;
    public FollowingAdapter(Context context, List<User> listData) {
this.context = context;
    this.listData = listData;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.following_card,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User ld=listData.get(position);
        holder.txtname.setText(ld.getName());

  //  Glide.with(context).load(video.getThumbnail()).into(holder.thumbnail);

}

@Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView txtname;
    public ViewHolder(View itemView) {
        super(itemView);
        txtname=(TextView)itemView.findViewById(R.id.following_name);
    }
}
}
