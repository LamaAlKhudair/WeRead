package com.example.wereadv10.ui.books.oneBook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.oneBook.reviews.ReviewsTab;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    String Cover, userEmail, userID;
    TabsAdapter tabsAdapter;
    private dbSetUp dbSetUp = new dbSetUp();

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
        userEmail = "";
        userID = "";
        getUserEmail();
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
        if (item.getItemId() == R.id.Currently_Reading) {
            if( addToCurrent())
            Toast.makeText(this, "The book has been added successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "The book has not been added,try again", Toast.LENGTH_SHORT).show();

            return true;
        }

        if (item.getItemId() == R.id.to_read) {
            if(addToRead())
            Toast.makeText(this, "The book has been added successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "The book has not been added,try again", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == R.id.completed) {
            if(addToComplate())
            Toast.makeText(this, "The book has been added successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "The book has not been added,try again", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.cancel) {
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
        final Map<String, Object> addBook = new HashMap<>();
        addBook.put("userID", userID);
        addBook.put("bookID", book_id);
        dbSetUp.db.collection("current_read_book").document(getRandom())
                .set(addBook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                    }
                });
            return true;
    }
    private boolean addToRead(){
        final Map<String, Object> addBook = new HashMap<>();
        addBook.put("userID", userID);
        addBook.put("bookID", book_id);
        dbSetUp.db.collection("to_read_book").document(getRandom())
                .set(addBook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                    }
                });
        return true;
    }
    //complete_read_book
    private boolean addToComplate(){
        final Map<String, Object> addBook = new HashMap<>();
        addBook.put("userID", userID);
        addBook.put("bookID", book_id);
        dbSetUp.db.collection("complete_read_book").document(getRandom())
                .set(addBook)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                    }
                });
        return true;    }
    private void getUserEmail() {
        //to display the name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail();
            userID = user.getUid();
        }
    }
    private String getRandom(){
        return UUID.randomUUID().toString();
    }

}

