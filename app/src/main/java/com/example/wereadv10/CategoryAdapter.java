package com.example.wereadv10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.books.oneBook.bookPage;
import com.example.wereadv10.ui.categories.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Category> categoriesList = new ArrayList<>();
    private static final String TAG = "CategoryAdapter";
    private dbSetUp dbSetUp = new dbSetUp();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView category_name;
        private CardView card;

        public MyViewHolder(View view) {
            super(view);
            category_name = (TextView) view.findViewById(R.id.category_name);
            card = (CardView) view.findViewById(R.id.category_card);
            card.getLayoutParams().height = 500;
            card.getLayoutParams().width = 420;
            card.setCardBackgroundColor(Color.TRANSPARENT);

            card.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), categoryPage.class);
            intent.putExtra("TITLE", categoriesList.get(getAdapterPosition()).getCategory_name());

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public CategoryAdapter(Context mContext, List<Category> l) {
        this.mContext = mContext;
        this.categoriesList = l;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card, parent, false);

        return new CategoryAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {
        Category category = categoriesList.get(position);
        holder.category_name.setText(category.getCategory_name());
    }


    @Override
    public int getItemCount() {
        if (categoriesList!=null)
            return categoriesList.size();
        else return 0;
    }

}
