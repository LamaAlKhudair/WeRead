package com.example.wereadv10.ui.otherProfile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.categories.Category;
import com.example.wereadv10.ui.profile.profileTab.bookTab.CardPagerAdapter;
import com.example.wereadv10.ui.profile.profileTab.bookTab.ShadowTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OtherProfileBookTabFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ViewPager mCurrentlyBookReadViewPager;
    private ViewPager mToReadViewPager;
    private ViewPager mCompleteBookViewPager;

    private CardPagerAdapter mCurrentlyBookReadCardAdapter;
    private CardPagerAdapter mToReadAdapter;
    private CardPagerAdapter mCompleteBookCardAdapter;

    private ShadowTransformer mCurrentlyBookReadCardShadowTransformer;
    private ShadowTransformer mToReadCardShadowTransformer;
    private ShadowTransformer mCompleteBookCardShadowTransformer;
    private com.example.wereadv10.dbSetUp dbSetUp;
    private String TAG = OtherProfileBookTabFragment.class.getSimpleName();





    private ShadowTransformer mFragmentCardShadowTransformer;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile_book_tab, container, false);
        mCurrentlyBookReadViewPager = view.findViewById(R.id.fragment_book_tab_currently_book_read_viewPager);
        dbSetUp = new dbSetUp();
        mCurrentlyBookReadCardAdapter = new CardPagerAdapter();
       getCurrentReadBook();

        mCurrentlyBookReadCardShadowTransformer = new ShadowTransformer(mCurrentlyBookReadViewPager, mCurrentlyBookReadCardAdapter);

        mCurrentlyBookReadViewPager.setAdapter(mCurrentlyBookReadCardAdapter);
        mCurrentlyBookReadViewPager.setPageTransformer(false, mCurrentlyBookReadCardShadowTransformer);
        mCurrentlyBookReadViewPager.setOffscreenPageLimit(3);
//2
        mToReadViewPager = view.findViewById(R.id.fragment_book_tab_to_read_viewPager);


        mToReadAdapter = new CardPagerAdapter();
        getToReadBook();


        mToReadCardShadowTransformer = new ShadowTransformer(mToReadViewPager, mToReadAdapter);

        mToReadViewPager.setAdapter(mToReadAdapter);
        mToReadViewPager.setPageTransformer(false, mToReadCardShadowTransformer);
        mToReadViewPager.setOffscreenPageLimit(3);
        //3
        mCompleteBookViewPager = (ViewPager) view.findViewById(R.id.fragment_book_tab_complete_book_viewPager);



        mCompleteBookCardAdapter = new CardPagerAdapter();
        getCompleteReadBook();

        mCompleteBookCardShadowTransformer = new ShadowTransformer(mCompleteBookViewPager, mCompleteBookCardAdapter);

        mCompleteBookViewPager.setAdapter(mCompleteBookCardAdapter);
        mCompleteBookViewPager.setPageTransformer(false, mCompleteBookCardShadowTransformer);
        mCompleteBookViewPager.setOffscreenPageLimit(3);
        return view;

    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCurrentlyBookReadCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }

    @Override
    public void onClick(View view) {

    }

//toRead Book
    private void getToReadBook() {

        String otherUserID = "";
        if (getActivity().getIntent().getExtras().getString("otherUserID")!=null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");
        dbSetUp.db.collection("to_read_book")
                .whereEqualTo("userID", otherUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id
                                String bookID = document.get("bookID").toString();
                                getBooks(bookID);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        ////

    }//end getToReadBook()

    private void getBooks(String bookId) {

        dbSetUp.db.collection("books").whereEqualTo("bookID", bookId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Book book = new Book();
                                String book_title = document.get("book_title").toString();
                                String summary = document.get("summary").toString();
                                String author = document.get("author").toString();
                                String bookCover = document.get("book_cover").toString();

                                book.setBook_title(book_title);
                                book.setSummary(summary);
                                book.setAuthor(author);
                                book.setCover(bookCover);

                                DocumentReference doc = document.getDocumentReference("book_category");
                                String path = doc.getPath();
                                String col = path.substring(0, path.indexOf("/"));
                                String doc3 = path.substring(path.indexOf("/") + 1);
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
                                mToReadAdapter.addCardItem(book);
                                mToReadAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        //mToReadAdapter.notifyDataSetChanged();

                    }
                });


    }
   // complete read book
    private void getCompleteReadBook() {

        String otherUserID = "";
        if (getActivity().getIntent().getExtras().getString("otherUserID")!=null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");
        dbSetUp.db.collection("complete_read_book")
                .whereEqualTo("userID", otherUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id
                                String bookID = document.get("bookID").toString();
                                getCompleteReadBooks(bookID);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        ////

    }//end getToReadBook()

    private void getCompleteReadBooks(String bookId) {

        dbSetUp.db.collection("books").whereEqualTo("bookID", bookId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Book book = new Book();
                                String book_title = document.get("book_title").toString();
                                String summary = document.get("summary").toString();
                                String author = document.get("author").toString();
                                String bookCover = document.get("book_cover").toString();


                                book.setBook_title(book_title);
                                book.setSummary(summary);
                                book.setAuthor(author);
                                book.setCover(bookCover);

                                DocumentReference doc = document.getDocumentReference("book_category");
                                String path = doc.getPath();
                                String col = path.substring(0, path.indexOf("/"));
                                String doc3 = path.substring(path.indexOf("/") + 1);
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
                                mCompleteBookCardAdapter.addCardItem(book);
                                mCompleteBookCardAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    // current read book
    private void getCurrentReadBook() {

        String otherUserID = "";
        if (getActivity().getIntent().getExtras().getString("otherUserID")!=null)
            otherUserID = getActivity().getIntent().getExtras().getString("otherUserID");
        dbSetUp.db.collection("current_read_book")
                .whereEqualTo("userID", otherUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id
                                String bookID = document.get("bookID").toString();
                                getCurrentReadBooks(bookID);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        ////

    }//end getToReadBook()

    private void getCurrentReadBooks(String bookId) {

        dbSetUp.db.collection("books").whereEqualTo("bookID", bookId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Book book = new Book();
                                String book_title = document.get("book_title").toString();
                                String summary = document.get("summary").toString();
                                String author = document.get("author").toString();
                                String bookCover = document.get("book_cover").toString();


                                book.setBook_title(book_title);
                                book.setSummary(summary);
                                book.setAuthor(author);
                                book.setCover(bookCover);

                                DocumentReference doc = document.getDocumentReference("book_category");
                                String path = doc.getPath();
                                String col = path.substring(0, path.indexOf("/"));
                                String doc3 = path.substring(path.indexOf("/") + 1);
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
                                mCurrentlyBookReadCardAdapter.addCardItem(book);
                                mCurrentlyBookReadCardAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

}
