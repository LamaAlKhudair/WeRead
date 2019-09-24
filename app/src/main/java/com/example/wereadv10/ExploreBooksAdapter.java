package com.example.wereadv10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ExploreBooksAdapter extends RecyclerView.Adapter<ExploreBooksAdapter.MyViewHolder> implements ItemClickListener {
    private Context mContext;
    private List<Book> booksList = new ArrayList<>();
    private static final String TAG = "ExploreBooksAdapter";
    private dbSetUp dbSetUp = new dbSetUp();
    OnItemClickListener mItemClickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView cardImg;
        private CardView card;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            cardImg = (ImageView) view.findViewById(R.id.cardImg);
            title = (TextView) view.findViewById(R.id.Title);
            card = (CardView) view.findViewById(R.id.card_view);
            card.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), BookInfoFragment.class);
            intent.putExtra("TITLE", booksList.get(getAdapterPosition()).getBook_title());
            //  intent.putExtra("COVER", booksList.get(getAdapterPosition()).getCover());
            intent.putExtra("AUTHOR", booksList.get(getAdapterPosition()).getAuthor());
            Category cat=booksList.get(getAdapterPosition()).getBook_category();
            intent.putExtra("CATEGORY", cat.toString());

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class

    public interface OnItemClickListener{
        public void onItemClick(View view, int Position);
    }

    public void SetOnItemClickListner(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public ExploreBooksAdapter(Context mContext, List<Book> l) {
        this.mContext = mContext;
        this.booksList = l;
       // System.out.println("BOOKLIISST"+booksList.isEmpty());
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
        holder.title.setText(book.getBook_title());
        Glide.with(mContext).load(booksList.get(position).getCover()).into(holder.cardImg);


    }


    @Override
    public int getItemCount() {
        if (booksList!=null)
            return booksList.size();
        else return 0;
    }

    @Override
    public void onItemClick(View v, int pos) {

    }

}

