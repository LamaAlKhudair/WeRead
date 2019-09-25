package com.example.wereadv10.ui.books.oneBook.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewsTab extends Fragment {
    private RecyclerView rv;
    private List<Review> RevList;
    private ReviewsAdapter reviewsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_tab, container, false);
        rv=view.findViewById(R.id.review_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        RevList=new ArrayList<>();
//        reviewsAdapter = new BooksAdapter(this,getReviews()) ;
//        rv.setItemAnimator(new DefaultItemAnimator());
//        rv.setAdapter(reviewsAdapter);
//        reviewsAdapter.notifyDataSetChanged();
        return view;




    }
}
