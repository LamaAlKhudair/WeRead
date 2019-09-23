package com.example.wereadv10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BookInfoFragment extends Fragment {
    public TextView bookTitle;
    public TextView add;
    public ImageView bookCover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookinfo, container, false);
        getExtras();
        bookTitle=view.findViewById(R.id.bookName);
        add=view.findViewById(R.id.addToShelf);

        return view;
    }



    private void getExtras() {
        Bundle bundle = getArguments();
        if (bundle != null) {
       // System.out.println("From fraq"+bundle.getString("Title"));


            bookTitle.setText(bundle.getString("TITLE"));
        }else{
           // System.out.println("bandel is null!!!!!");
        }
    }






}
