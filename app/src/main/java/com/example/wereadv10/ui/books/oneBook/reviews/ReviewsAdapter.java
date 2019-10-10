package com.example.wereadv10.ui.books.oneBook.reviews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.otherProfile.OtherProfileActivity;

import java.util.List;

public class ReviewsAdapter  extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{
    private List<Review> ReviewsList;
    private Context context;
    private dbSetUp dbSetUp = new dbSetUp();


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
         holder.revTitle.setText(review.getRevTitle());
        holder.userName.setText(review.getUserName());



    }


    @Override
    public int getItemCount() {
        return ReviewsList.size();
    }
    public void clear(){
        ReviewsList.clear();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView revTxt;
        private TextView revTitle;
        private TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);
            revTitle = itemView.findViewById(R.id.reviewTitle);
            revTxt=itemView.findViewById(R.id.text_review);
            userName=itemView.findViewById(R.id.userName);
            userName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, OtherProfileActivity.class);
            intent.putExtra("otherUserEmail", ReviewsList.get(getAdapterPosition()).getUserEmail());
            intent.putExtra("otherUserID", ReviewsList.get(getAdapterPosition()).getUserID());

            context.startActivity(intent);
        }
    }



}
