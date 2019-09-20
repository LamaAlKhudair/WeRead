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
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> implements ItemClickListener {
    private Context mContext;
    private List<Book> booksList;
    private static final String TAG = "BooksAdapter";
    private dbSetUp dbSetUp;
    private List<Book> bookList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView cardImg;
        private RelativeLayout card;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            cardImg = (ImageView) view.findViewById(R.id.cardImg);
            card = (RelativeLayout) view.findViewById(R.id.card_view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ViewBooks.class);
                    intent.putExtra("TITLE", booksList.get(getAdapterPosition()).getBook_title());
                    intent.putExtra("COVER", booksList.get(getAdapterPosition()).getCover());
                    intent.putExtra("AUTHOR", booksList.get(getAdapterPosition()).getAuthor());
                    intent.putExtra("CATEGORY", booksList.get(getAdapterPosition()).getBook_category());
                    view.getContext().startActivity(intent);
                }//end onClick()
            });
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View view) {

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
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                holder.title.setText( booksList.get(position).getBook_title());
                //i.putExtra("COVER", booksList.get(position).getCover());
//                if (pos ==0||pos ==1||pos ==2||pos ==3){
//                    i.putExtra("COVER", booksList.get(position).getCover());
//                    i.putExtra("AUTHOR", booksList.get(position).getAuthor());
//                    i.putExtra("CATEGORY", booksList.get(position).getBook_category());
//                    i.putExtra("TITLE", booksList.get(position).getBook_title());
//                    i.putExtra("SUMMARY", booksList.get(position).getSummary());
//                }
                Glide.with(mContext).load(booksList.get(position).getCover()).into(holder.cardImg);

                holder.cardImg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, BookInfoFragment.class);
                        mContext.startActivity(i);
                    }

                });



            }
        });

    }


    @Override
    public int getItemCount() {
        return booksList.size();
    }

    @Override
    public void onItemClick(View v, int pos) {

    }

}


