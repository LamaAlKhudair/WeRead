package com.example.wereadv10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.ui.categories.Category;

import java.util.List;

//todo display books based on category
public class categoryPage extends AppCompatActivity {
    private RecyclerView catRecyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getExtras();
    }



    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("TITLE") != null)
                setTitle(intent.getExtras().getString("TITLE"));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





}
