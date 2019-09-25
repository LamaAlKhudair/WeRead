package com.example.wereadv10.ui.books.oneBook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.books.oneBook.reviews.ReviewsTab;
import com.google.android.material.tabs.TabLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class bookPage extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener {
    public TextView bookTitle;
    public TextView add;
    public ImageView bookCover;
    private ViewPager viewPager;
    private bookInfoTab infoFragment=new bookInfoTab();;
    private ReviewsTab reviewsTab = new ReviewsTab();
    TabsAdapter tabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_page);
        bookTitle=findViewById(R.id.bookName);
        bookCover=findViewById(R.id.bookCover);
        add=findViewById(R.id.addToShelf);
        add.setOnClickListener(this);
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



    }


    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("TITLE") != null)
                bookTitle.setText(intent.getExtras().getString("TITLE"));
            if (intent.getExtras().getString("COVER") != null)
                bookCover.setImageResource(getImageId(this, intent.getExtras().getString("COVER")));

        }
    }

    public int getImageId(Context context, String imageName) {
        Intent intent = getIntent();
        return context.getResources().getIdentifier(intent.getExtras().getString("COVER"), null, context.getPackageName());
    }




    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()!=R.id.cancel) {
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

}

