package com.example.wereadv10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> implements ItemClickListener {
    private Context mContext;
    private List<Book> booksList = new ArrayList<>();
    private static final String TAG = "BooksAdapter";
    private dbSetUp dbSetUp;
    OnItemClickListener mItemClickListener;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView cardImg;
        private RelativeLayout card;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            cardImg = (ImageView) view.findViewById(R.id.cardImg);
            card = (RelativeLayout) view.findViewById(R.id.card_view);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ViewBooks.class);
            intent.putExtra("TITLE", booksList.get(getAdapterPosition()).getBook_title());
                    intent.putExtra("COVER", booksList.get(getAdapterPosition()).getCover());
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

    public BooksAdapter(Context mContext, List<Book> l) {
        this.mContext = mContext;
        this.booksList = l;
//        System.out.println("ftom adapter:"+l.isEmpty());
    }
//    public void updateBooksList(List<Book> newlist) {
//        booksList.clear(); // when u delete this line (بياض لا نهائي في الانترفيس )
//        System.out.println("From updateReceiptsList"+booksList.isEmpty());
//        booksList.addAll(newlist);
//        System.out.println("From updateReceiptsList2"+booksList.isEmpty());
//        this.notifyDataSetChanged();
//
//    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }

   /* public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
        System.out.println("from adapter "+booksList.isEmpty());
    }
*/
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Book book = booksList.get(position);
        Glide.with(mContext).load(booksList.get(position).getCover()).into(holder.cardImg);


    }


    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onItemClick(View v, int pos) {

    }

}


