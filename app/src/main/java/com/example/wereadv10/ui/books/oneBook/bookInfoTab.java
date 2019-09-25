package com.example.wereadv10.ui.books.oneBook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wereadv10.R;

public class bookInfoTab extends Fragment {
    private TextView author;
    private TextView category;
    private TextView summary;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookinfo_tab, container, false);
        author=(TextView)view.findViewById(R.id.book_author);
        category=(TextView)view.findViewById(R.id.book_category);
        summary=(TextView)view.findViewById(R.id.book_summary);
        author.setText(getActivity().getIntent().getExtras().getString("AUTHOR"));
        category.setText(getActivity().getIntent().getExtras().getString("CATEGORY"));
        summary.setText(getActivity().getIntent().getExtras().getString("SUMMARY"));
        return view;

    }


}
