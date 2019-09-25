package com.example.wereadv10.ui.books.oneBook.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wereadv10.R;

import java.util.List;

public class ReviewsAdapter  extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{
    private List<Review> ReviewsList;
    private Context context;

    public ReviewsAdapter(Context context, List<Review> listData) {
        this.context = context;
        this.ReviewsList = listData;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review=ReviewsList.get(position);
        holder.revTxt.setText(review.getText());
        holder.userName.setText(review.getUserName());



    }


    @Override
    public int getItemCount() {
        return ReviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView revTxt;
        private TextView userName;
        public ViewHolder(View itemView) {
            super(itemView);
            revTxt=itemView.findViewById(R.id.text_review);
            userName=itemView.findViewById(R.id.userName);
        }
    }



}
