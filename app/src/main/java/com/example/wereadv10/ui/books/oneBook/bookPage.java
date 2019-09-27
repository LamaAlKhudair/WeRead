package com.example.wereadv10.ui.books.oneBook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
    private ViewPager viewPager;
    private Bitmap bitmap;
    private bookInfoTab infoFragment=new bookInfoTab();;
    private ReviewsTab reviewsTab = new ReviewsTab();
    TabsAdapter tabsAdapter;
    private Context mContext;


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
            if (intent.getExtras().getString("COVER") != null) {
                Glide.with(bookPage.this).load(intent.getExtras().getString("COVER")).into(bookCover);


            }else{
                Glide.with(bookPage.this).load(R.drawable.logo).into(bookCover);

            }


        }
    }



    public int getImageId(Context context, String imageName) {
        Intent intent = getIntent();
        return context.getResources().getIdentifier(intent.getExtras().getString("COVER"), null, context.getPackageName());
    }


//    public void pickImage(View View) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
//            try {
//                // We need to recyle unused bitmaps
//                if (bitmap != null) {
//                    bitmap.recycle();
//                }
//                InputStream stream = getContentResolver().openInputStream(
//                        data.getData());
//                bitmap = BitmapFactory.decodeStream(stream);
//                stream.close();
//                bookCover.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

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

}

