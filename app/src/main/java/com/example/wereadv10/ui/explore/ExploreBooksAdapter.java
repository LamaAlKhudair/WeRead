package com.example.wereadv10.ui.explore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.books.oneBook.bookPage;

import java.util.ArrayList;
import java.util.List;

public class ExploreBooksAdapter extends RecyclerView.Adapter<ExploreBooksAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Book> booksList = new ArrayList<>();
    private static final String TAG = "ExploreBooksAdapter";


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView cardImg;
        private CardView card;

        public MyViewHolder(View view) {
            super(view);
            cardImg = view.findViewById(R.id.cardImg);
            card = view.findViewById(R.id.card_view);
            card.getLayoutParams().height = 500;
            card.getLayoutParams().width = 420;
            cardImg.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), bookPage.class);
            intent.putExtra("TITLE", booksList.get(getAdapterPosition()).getBook_title());
            intent.putExtra("COVER", booksList.get(getAdapterPosition()).getCover());
            intent.putExtra("AUTHOR", booksList.get(getAdapterPosition()).getAuthor());
            intent.putExtra("CATEGORY", booksList.get(getAdapterPosition()).getBook_category().getCategory_name()); //generate an error
            intent.putExtra("SUMMARY", booksList.get(getAdapterPosition()).getSummary());
            intent.putExtra("BOOK_ID", booksList.get(getAdapterPosition()).getID());
            intent.putExtra("BOOK_RATE", booksList.get(getAdapterPosition()).getRate()+" ");

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public ExploreBooksAdapter(Context mContext, List<Book> l) {
        this.mContext = mContext;
        this.booksList = l;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book book = booksList.get(position);
        Glide.with(mContext).load(book.getCover()).into(holder.cardImg);
    }


    @Override
    public int getItemCount() {
        if (booksList!=null)
            return booksList.size();
        else return 0;
    }



}


