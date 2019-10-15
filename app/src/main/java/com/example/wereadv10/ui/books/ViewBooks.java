package com.example.wereadv10.ui.books;

import android.content.res.Resources;
import android.graphics.Rect;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.CategoryAdapter;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.categories.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.firebase.firestore.Query.Direction;

public class ViewBooks extends AppCompatActivity implements SearchView.OnQueryTextListener, PopupMenu.OnMenuItemClickListener {
    private RecyclerView recyclerView, catRecyclerView;
    private BooksAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private   List<Book> bookList;
    private List<Book> RatedBooks;
    public ImageView filter;
    private List<Category> categoryList;
    private static final String TAG = "ViewBooks";
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    int placeCurrent =0;
    int placeComplate =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewbooks);
        setTitle("Books");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        filter=findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ViewBooks.this, view);
                popup.setOnMenuItemClickListener(ViewBooks.this);
                popup.inflate(R.menu.popup_menu2);
                popup.show();
            }
        });
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.viewBooksRec);
        catRecyclerView = (RecyclerView) findViewById(R.id.category_rv);

        bookList = new ArrayList<Book>();

        categoryList = new ArrayList<Category>();

        adapter = new BooksAdapter(this,getBooks()) ;//should pass a book list to the adapter
        categoryAdapter = new CategoryAdapter(this, getCategory());

    }//End onCreate()
    
    private void filterBookHighToLow(){
        Collections.sort(bookList, new Comparator<Book>() {
            @Override
            public int compare(Book bookData, Book t1) {
                Long idea1 = new Long(bookData.getRate());// here pass rating value.
                Long idea2 = new Long(t1.getRate());// here pass rating value.
                return idea2.compareTo(idea1);
            }
        });
        if (adapter != null)
            adapter.notifyDataSetChanged();


    }
    private void filterBookLowToHigh(){
        Collections.sort(bookList, new Comparator<Book>() {
            @Override
            public int compare(Book bookData, Book t1) {
                Long idea1 = new Long(bookData.getRate());// here pass rating value.
                Long idea2 = new Long(t1.getRate());// here pass rating value.
                return idea1.compareTo(idea2);
            }
        });
        if (adapter != null)
            adapter.notifyDataSetChanged();

    }
    private void filterCurrentRead(){
        final Map<String, Integer> currentRead =new HashMap<>();
        dbSetUp.db.collection("current_read_book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String book_id = document.getString("bookID");

                                if (currentRead.containsKey(book_id)){
                                    currentRead.put(book_id, currentRead.get(book_id) + 1);

                                }else{
                                    currentRead.put(book_id, 1);

                                }
                            }

                            Object[] a2 = currentRead.entrySet().toArray();
                            Arrays.sort(a2, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    return ((Map.Entry<String, Integer>) o2).getValue()
                                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                                }
                            });
//                            for(int i=currentRead.size()-1; i>=0;i--) {
                            for(int i=0; i<currentRead.size();i++) {
                                System.out.println(a2[i]+"\t LAMMA");
                                String bb_id = a2[i].toString().substring(0, a2[i].toString().indexOf("="));
                                System.out.println(bb_id);
                                sortBooksBasedOnId(bb_id, placeCurrent++);
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                });

    }

    private void filterComplatedBooks(){
        final Map<String, Integer> complatedBook =new HashMap<>();
        dbSetUp.db.collection("complete_read_book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String book_id = document.getString("bookID");

                                if (complatedBook.containsKey(book_id)){
                                   complatedBook.put(book_id, complatedBook.get(book_id) + 1);

                                }else{
                                    complatedBook.put(book_id, 1);

                                }
                            }

                            Object[] a = complatedBook.entrySet().toArray();
                            Arrays.sort(a, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    return ((Map.Entry<String, Integer>) o2).getValue()
                                            .compareTo(((Map.Entry<String, Integer>) o1).getValue());
                                }
                            });
                            //ArrayList<String> keys = new ArrayList<String>(a.keySet());

                            for(int i=0; i<complatedBook.size();i++) {
                                System.out.println(a[i]+"\t LAMMA");
                                String bb_id = a[i].toString().substring(0, a[i].toString().indexOf("="));
                                System.out.println(bb_id);
                                sortBooksBasedOnId(bb_id, placeComplate++);
                            }
//
                            adapter.notifyDataSetChanged();

                        }
                    }
                });

    }


    private void sortBooksBasedOnId(String id, int place){

        int pos =0;
        for (Book book : bookList){
            String book_id = book.getID();
            if (book_id.equalsIgnoreCase(id)){
                pos = bookList.indexOf(book);
                break;
            }
        }
//        for (int i = 0; i < bookList.size(); i++) {
//            if (i < bookList.size() - 1) {
//                Book j = bookList.get(i+1);
//                bookList.remove(i);
//                bookList.add(i, bookList.get(i));
//                bookList.remove(i + 1);
//                bookList.add(j);
//            }
//        }
        Collections.swap(bookList, pos, place);
        adapter.notifyDataSetChanged();



    }
    private List<Category> getCategory(){
        dbSetUp.db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count =3;
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ViewBooks.this, LinearLayoutManager.HORIZONTAL, false);
                            catRecyclerView.setLayoutManager(mLayoutManager);
                            catRecyclerView.setAdapter(categoryAdapter);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Category category = new Category();
                                String category_name = document.get("category_name").toString();
                                category.setCategory_name(category_name);
                                categoryList.add(category);
                                count = count+1;
                                categoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return categoryList;
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
        adapter.updateList(newList);
        adapter.notifyDataSetChanged();
        return true;
    }

    private List<Book> getBooks() {

        dbSetUp.db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ViewBooks.this, 2);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
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
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                                bookList.add(book);
                                                adapter.notifyDataSetChanged();
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



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.rateHigh) {
            filterBookHighToLow();
            return true;
        }

        if (menuItem.getItemId() == R.id.rateLow) {
            filterBookLowToHigh();
            return true;
        }
        if ( menuItem.getItemId() == R.id.completedBook){
            filterComplatedBooks();
            return true;
        }
        if (menuItem.getItemId() == R.id.currently_Reading){
            filterCurrentRead();
            return true;
        }
        else
        return false;
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}


