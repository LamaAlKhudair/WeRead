package com.example.wereadv10.ui.profile.profileTab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.categories.Category;
import com.example.wereadv10.ui.profile.ProfileFragment;
import com.example.wereadv10.ui.profile.profileTab.bookTab.CardPagerAdapter;
import com.example.wereadv10.ui.profile.profileTab.bookTab.ShadowTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class BookTabFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private ViewPager mViewPager;
    private ViewPager mViewPager2;
    private ViewPager mViewPager3;

    private CardPagerAdapter mCardAdapter;
    private CardPagerAdapter mCardAdapter2;
    private CardPagerAdapter mCardAdapter3;

    private ShadowTransformer mCardShadowTransformer;
    private ShadowTransformer mCardShadowTransformer2;
    private ShadowTransformer mCardShadowTransformer3;
    private com.example.wereadv10.dbSetUp dbSetUp;
    private String TAG = BookTabFragment.class.getSimpleName();

    private ShadowTransformer mFragmentCardShadowTransformer;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_tab, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        dbSetUp = new dbSetUp();


        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new Book("auth1",new Category("kjgk","book1"),"book1","gkjg"));
        mCardAdapter.addCardItem(new Book("auth2",new Category("kjgk","book2"),"book2","gkjg"));
        mCardAdapter.addCardItem(new Book("auth3",new Category("kjgk","book3"),"book3","gkjg"));
        mCardAdapter.addCardItem(new Book("auth4",new Category("kjgk","book4"),"book4","gkjg"));


        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
//2
        mViewPager2 = (ViewPager) view.findViewById(R.id.viewPager2);


        mCardAdapter2 = new CardPagerAdapter();
        getFiveBooks();
/*
        mCardAdapter2.addCardItem(new Book("auth1",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
        mCardAdapter2.addCardItem(new Book("auth2",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
        mCardAdapter2.addCardItem(new Book("auth3",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
        mCardAdapter2.addCardItem(new Book("auth4",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
*/


        mCardShadowTransformer2 = new ShadowTransformer(mViewPager2, mCardAdapter2);

        mViewPager2.setAdapter(mCardAdapter2);
        mViewPager2.setPageTransformer(false, mCardShadowTransformer2);
        mViewPager2.setOffscreenPageLimit(3);
        //3
        mViewPager3 = (ViewPager) view.findViewById(R.id.viewPager3);


        mCardAdapter3 = new CardPagerAdapter();
        mCardAdapter3.addCardItem(new Book("auth1",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
        mCardAdapter3.addCardItem(new Book("auth2",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
        mCardAdapter3.addCardItem(new Book("auth3",new Category("kjgk","jkhjk"),"fgjk","gkjg"));
        mCardAdapter3.addCardItem(new Book("auth4",new Category("kjgk","jkhjk"),"fgjk","gkjg"));


        mCardShadowTransformer3 = new ShadowTransformer(mViewPager3, mCardAdapter3);

        mViewPager3.setAdapter(mCardAdapter3);
        mViewPager3.setPageTransformer(false, mCardShadowTransformer3);
        mViewPager3.setOffscreenPageLimit(3);
        return view;

    }
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }

    @Override
    public void onClick(View view) {

    }

    private void getFiveBooks(){

        CollectionReference bookRef = dbSetUp.db.collection("books");
        bookRef.limit(5).get()
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

                                dbSetUp.storageRef.child("books_covers/"+bookCover).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        book.setCover(uri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });

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
                                                mCardAdapter2.addCardItem(book);
                                               // FiveBooks.add(book);
                                                mCardAdapter2.notifyDataSetChanged();
                                            }

                                        });
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        //return FiveBooks;
    }
}
