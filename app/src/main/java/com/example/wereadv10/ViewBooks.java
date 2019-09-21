package com.example.wereadv10;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.viewbooks);
            recyclerView = (RecyclerView) findViewById(R.id.ViewBooksRecycler);

            String [] b1= getOneBook("Grant");
            String [] b2= getOneBook("The Making of a Manager");
            String [] b3= getOneBook("Information Security");
            String [] b4= getOneBook("My Dear Hamilton: A Novel of Eliza Schuyler Hamilton");
            String [] b5= getOneBook("Why We Sleep: Unlocking the Power of Sleep and Dreams");
            Book book1 = new Book(b1[0],b1[1],b1[2],b1[3]);
            Book book2 = new Book(b2[0],b2[1],b2[2],b2[3]);
            Book book3 = new Book(b3[0],b3[1],b3[2],b3[3]);
            Book book4 = new Book(b4[0],b4[1],b4[2],b4[3]);
            Book book5 = new Book(b5[0],b5[1],b5[2],b5[3]);
            bookList.add(0,book1);
            bookList.add(1,book2);
            bookList.add(2,book3);
            bookList.add(3,book4);
            bookList.add(4,book5);


            adapter = new BooksAdapter(this, bookList); //should pass a book list to the adapter
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }




        private String [] getOneBook(String bookName) {
            // this function retrieve one book based on BookName == book_title
            // and print it.
            final String [] book= new String[4];

            dbSetUp.db.collection("books")
                    .whereEqualTo("book_title", bookName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    System.out.println(document.getData().toString());
                                    book[0]=document.getString("author");
                                    book[1]=document.getString("book_category");
                                    book[2]=document.getString("book_title");
                                    book[3]=document.getString("summary");

                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });



            return book;

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

    }


