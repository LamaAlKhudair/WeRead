package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewBooks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private static final String TAG = "ViewBooks";
    private dbSetUp dbSetUp = new dbSetUp();
    private List<Book> bookList = new ArrayList<>();
//    private FirestoreRecyclerAdapter<Book, BookViewHolder> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewbooks);
        recyclerView = (RecyclerView) findViewById(R.id.viewBooksRec);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //sol. 1

        List<Book> books = getBooks();
        adapter = new BooksAdapter(this, books); //should pass a book list to the adapter
        recyclerView.setAdapter(adapter);
        //sol. 2

//        Query query =dbSetUp.db.collection("books")
//                .orderBy("book_title", Query.Direction.ASCENDING) ;
//
//        FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>()
//                .setQuery(query, Book.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<Book, BookViewHolder>(options) {
//
//            @NonNull
//            @Override
//            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
//                return new BookViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i, @NonNull Book book) {
//                bookViewHolder.setBookName(book.getBook_title());
//            }
//        };
//
//        recyclerView.setAdapter(adapter);

    }//End onCreate()







    //sol. 1

    private List<Book> getBooks() {
        dbSetUp.db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                bookList.add(document.toObject(Book.class));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return bookList;
    }


    //sol. 2

//    private class BookViewHolder extends RecyclerView.ViewHolder {
//        private View view;
//
//        BookViewHolder(View itemView) {
//            super(itemView);
//            view = itemView;
//        }
//
//        void setBookName(String title) {
//            TextView textView = view.findViewById(R.id.bookName);
//            textView.setText(title);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if (adapter != null) {
//            adapter.stopListening();
//        }
//    }



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


}

