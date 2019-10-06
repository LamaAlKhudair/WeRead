package com.example.wereadv10.ui.books.oneBook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.books.oneBook.reviews.ReviewsTab;
import com.google.android.material.tabs.TabLayout;

public class bookPage extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener {
    public TextView bookTitle;
    public TextView add;
    public ImageView bookCover;
    public ImageView star;
    private ViewPager viewPager;
    TextView yourRating;
    private bookInfoTab infoFragment=new bookInfoTab();;
    private ReviewsTab reviewsTab = new ReviewsTab();
    private String book_id;
    String Cover;
    TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_page);
        bookTitle=findViewById(R.id.bookName);
        bookCover=findViewById(R.id.bookCover);
        add=findViewById(R.id.addToShelf);
        add.setOnClickListener(this);
        star=findViewById(R.id.ratingstar);
        yourRating=findViewById(R.id.rateThis);
        if (getIntent().getExtras().getString("RATING_VALUE")!=null)
        yourRating.setText(getIntent().getExtras().getString("RATING_VALUE")+"/5");
        setTitle("Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.bookInfo_viewPager);
        TabLayout tabLayout = findViewById(R.id.bookInfo_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabsAdapter = new TabsAdapter(this.getSupportFragmentManager(), tabLayout.getTabCount());
        tabsAdapter.addFragment(infoFragment, "Book Information");
        tabsAdapter.addFragment(reviewsTab, "Reviews");
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getExtras();
        star.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent i = new Intent(bookPage.this,ratingPage.class);
                i.putExtra("COVER_RATING",Cover);
                startActivity(i);
            }

        });



    }


    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("TITLE") != null)
                bookTitle.setText(intent.getExtras().getString("TITLE"));
            if (intent.getExtras().getString("COVER") != null) {
                Glide.with(bookPage.this).load(intent.getExtras().getString("COVER")).into(bookCover);
                Cover = intent.getExtras().getString("COVER");

            }else{
                Glide.with(bookPage.this).load(R.drawable.logo).into(bookCover);

            }


        }
    }




    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()!= R.id.cancel) {
            Toast.makeText(this, "The book has been added successfully", Toast.LENGTH_SHORT).show(); //todo enhance the behavior
            return true;


        }
            else
            return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        PopupMenu popup = new PopupMenu(bookPage.this, view);
        popup.setOnMenuItemClickListener(bookPage.this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    private boolean addToCurrent(){
            return true;
    }
    private boolean addToRead(){
        return true;
    }
    private boolean addToComplate(){
        return true;
    }

}

