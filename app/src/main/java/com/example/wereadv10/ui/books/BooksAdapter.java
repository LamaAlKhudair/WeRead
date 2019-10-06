package com.example.wereadv10.ui.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.oneBook.bookPage;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Book> booksList = new ArrayList<>();

    private static final String TAG = "BooksAdapter";
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView cardImg;

        public MyViewHolder(View view) {
            super(view);
            cardImg = (ImageView) view.findViewById(R.id.cardImg);
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

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public BooksAdapter(Context mContext, List<Book> l) {
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
        return booksList.size();
    }
    public void updateList(List<Book> list){
        if (list.isEmpty()){
            Toast.makeText(this.mContext, "No book found", Toast.LENGTH_SHORT).show();
            booksList = new ArrayList<Book>();
            notifyDataSetChanged();
        }else{
            booksList = new ArrayList<Book>();
            booksList.addAll(list);
            notifyDataSetChanged();
        }
    }
}


