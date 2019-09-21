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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class ViewBooks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private static final String TAG = "ViewBooks";
    private dbSetUp dbSetUp = new dbSetUp();
    private List<Book> bookList ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewbooks);
        bookList = new ArrayList<Book>();
        adapter = new BooksAdapter(this, bookList) ;//should pass a book list to the adapter

        recyclerView = (RecyclerView) findViewById(R.id.viewBooksRec);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //sol. 1


        getBooks();

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

    private void getBooks() {
        dbSetUp.db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String book_title = document.get("book_title").toString();
                                 String summary = document.get("summary").toString();
                                 String author = document.get("author").toString();
                                final Book book = new Book();
                                book.setBook_title(book_title);
                                book.setSummary(summary);
                                book.setAuthor(author);
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
                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });
                                bookList.add(book);

                            }
                            System.out.println("bookList is empty fromm inside!!!"+bookList.isEmpty());
                            adapter.updateReceiptsList(bookList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        System.out.println("bookList is empty??"+bookList.isEmpty());
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

