package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.books.BooksAdapter;
import com.example.wereadv10.ui.books.ViewBooks;
import com.example.wereadv10.ui.categories.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class categoryPage extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView catRecyclerView;
    private List<Category> categoryList;

    private   List<Book> bookList;
    private String category;
    private BooksAdapter booksAdapter;
    private dbSetUp dbSetUp = new dbSetUp();
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private static final String TAG = "categoryPage";
    private String userEmail, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        category = " ";
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        getExtras();
        recyclerView = (RecyclerView) findViewById(R.id.viewBooksRec);


        bookList = new ArrayList<Book>();
        booksAdapter = new BooksAdapter(categoryPage.this,getBooks()) ;
    }



    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("TITLE") != null) {
                category = new String(intent.getExtras().getString("TITLE"));
                setTitle(category);
            }
        }
    }
    private void getUserEmail() {
        //to display the name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail();
            userID = user.getUid();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<Book> getBooks() {
        dbSetUp.db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(categoryPage.this, 2);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.addItemDecoration(new categoryPage.GridSpacingItemDecoration(2, dpToPx(10), true));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(booksAdapter);
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final Book book = new Book();
                                String book_title = document.get("book_title").toString();
                                String summary = document.get("summary").toString();
                                String author = document.get("author").toString();
                                String bookCover = document.get("book_cover").toString();
                                String book_id = document.getString("bookID");

                                long rate = (long) document.get("book_rate");
                                book.setRate(rate);

                                book.setID(book_id);

                                book.setBook_title(book_title);
                                book.setSummary(summary);
                                book.setAuthor(author);
                                book.setCover(bookCover);

                                DocumentReference doc = document.getDocumentReference("book_category");
                                String path = doc.getPath();
                                String col = path.substring(0, path.indexOf("/"));
                                String doc3 = path.substring(path.indexOf("/")+1);
                                dbSetUp.db.collection(col).whereEqualTo("category_name", doc3)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                        Category book_cat = document2.toObject(Category.class);
                                                        book.setBook_category(book_cat);

                                                        if (book_cat.getCategory_name().equals(category)) {
                                                            bookList.add(book);
                                                        }
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }

                                                booksAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return bookList;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

    }



    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s){

        List<Book> newList = new ArrayList<>();
        s=s.toLowerCase();
        for(int i=0; i<bookList.size(); i++){
            String book_name = bookList.get(i).getBook_title().toLowerCase();
            String book_author = bookList.get(i).getAuthor().toLowerCase();
            if(book_name.contains(s) || book_author.contains(s) ){
                newList.add(bookList.get(i));
            }
        }
        booksAdapter.updateList(newList);
        booksAdapter.notifyDataSetChanged();
        return true;
    }

}
