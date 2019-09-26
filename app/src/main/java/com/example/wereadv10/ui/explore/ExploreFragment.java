package com.example.wereadv10.ui.explore;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wereadv10.Club;
import com.example.wereadv10.ExploreBooksAdapter;
import com.example.wereadv10.ExploreClubsAdapter;
import com.example.wereadv10.ForgotPasswordActivity;
import com.example.wereadv10.R;
import com.example.wereadv10.ViewClubs;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.books.ViewBooks;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.categories.Category;
import com.example.wereadv10.ui.clubs.ClubsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {
    private static final String TAG = "ExploreFragment";

    private RecyclerView rvBooks; // H Recycler View for books
    private RecyclerView rvClubs; // V Recycler View for clubs

    private Button allBooks;
    private Button allClubs;

    private RecyclerView.Adapter rvBooks_adapter;
    private RecyclerView.Adapter rvClubs_adapter;

    private RecyclerView.LayoutManager rvBooks_LayoutManager;
    private RecyclerView.LayoutManager rvClubs_mLayoutManager;

    private List<Book> FiveBooks = new ArrayList<>();
    private List<Club> FiveClubs = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        rvBooks = (RecyclerView) root.findViewById(R.id.rvHorizontal);
        rvClubs = (RecyclerView) root.findViewById(R.id.rvVertical);

        rvBooks_LayoutManager = new LinearLayoutManager(ExploreFragment.this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvBooks.setLayoutManager ( rvBooks_LayoutManager );
        rvClubs.setLayoutManager ( new LinearLayoutManager(ExploreFragment.this.getContext()));

        rvBooks_adapter = new ExploreBooksAdapter(getParentFragment().getContext(), FiveBooks);
        rvClubs_adapter = new ExploreClubsAdapter(getParentFragment().getContext(), FiveClubs);

        rvBooks.setAdapter(rvBooks_adapter);
        rvClubs.setAdapter(rvClubs_adapter);

        dbSetUp = new dbSetUp();

        getFiveBooks();
        getFiveClubs();

        allBooks = root.findViewById(R.id.show_books);
        allBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ViewBooks.class);
                startActivity(i);
            }
        });

        allClubs = root.findViewById(R.id.show_clubs);
        allClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ViewClubs.class);
                startActivity(i);
            }
        });

        return root;
    }

    /*
            //rvBooks.setItemAnimator(new DefaultItemAnimator());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

      *  mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

      *  rvBooks_LayoutManager = new LinearLayoutManager(this);
      *  mRecyclerView.setLayoutManager(rvBooks_LayoutManager);

        mAdapter = new MessageAdapter(getBaseContext(), messageList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fillWithNonsenseText();
    }*/


    private List<Book> getFiveBooks(){

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

                                                FiveBooks.add(book);
                                                rvBooks_adapter.notifyDataSetChanged();

                                            }

                                        });
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return FiveBooks;
    }


    private List<Club> getFiveClubs(){

        CollectionReference clubRef = dbSetUp.db.collection("clubs");
        clubRef.limit(5).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Club club = new Club();

                                String club_name = document.get("club_name").toString();
                                String club_owner = document.get("club_owner").toString();
                                String club_description = document.get("club_description").toString();
                                String club_image = document.get("club_image").toString();

                                dbSetUp.storageRef.child("clubs_images/"+club_image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        club.setClub_image(uri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });

                                club.setClub_name(club_name);
                                club.setClub_owner(club_owner);
                                club.setClub_description(club_description);


                                FiveClubs.add(club);
                                rvClubs_adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return FiveClubs;
    }


    //??
    private void getBooksBasedOnCategory(String book_category){
        // book_category

        dbSetUp.db.collection("books")
                .whereEqualTo("book_category ", "/categories/Medicine")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData());
                                System.out.println("LAMA");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}