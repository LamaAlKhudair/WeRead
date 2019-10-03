package com.example.wereadv10.ui.profile.profileTab.bookTab;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.books.oneBook.bookPage;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Book> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(Book item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.book_tab_card, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        //move to book page
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), bookPage.class);
                intent.putExtra("TITLE", mData.get(position).getBook_title());
                intent.putExtra("COVER", mData.get(position).getCover());
                intent.putExtra("AUTHOR", mData.get(position).getAuthor());
                intent.putExtra("CATEGORY", mData.get(position).getBook_category().getCategory_name()); //generate an error
                intent.putExtra("SUMMARY", mData.get(position).getSummary());
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Book item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        ImageView bookImg = view.findViewById(R.id.book_tab_card_img);
        Glide.with(view.getContext()).load(item.getCover()).into(bookImg);

        //   TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
       titleTextView.setText(item.getBook_title());
     //   contentTextView.setText(item.getText());
    }

}
