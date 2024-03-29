package com.example.wereadv10.ui.books.oneBook.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReviewsTab extends Fragment {
    private static final String TAG = "ReviewsTab";
    private RecyclerView rv;
    private List<Review> RevList=new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private String book_title, userEmail, userName;
   // private Button ButtonAdd;
   private FloatingActionButton addFloatBtn;

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_tab, container, false);
        rv=view.findViewById(R.id.review_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        RevList=new ArrayList<>();
        getUserEmail();
        book_title = getActivity().getIntent().getExtras().getString("TITLE");
//        ButtonAdd=view.findViewById(R.id.addButton);
        addFloatBtn = view.findViewById(R.id.fab_T);
  //     RevList = getRevList() ;
        addFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),OneReview.class);
                i.putExtra("BOOK_TITLE", getActivity().getIntent().getExtras().getString("TITLE"));
                i.putExtra("USER",userEmail );
                startActivity(i);
            }
        });
/*        ButtonAdd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent i = new Intent(getContext(),OneReview.class);
                i.putExtra("BOOK_TITLE", getActivity().getIntent().getExtras().getString("TITLE"));
                i.putExtra("USER",userEmail );
                startActivity(i);

            }

        });*/
        return view;

    }
    private void getUserEmail() {
        //to display the name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
             userEmail = user.getEmail();

        }
    }



    public List<Review> getRevList(){
        dbSetUp.db.collection("reviews") //review_title
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reviewsAdapter = new ReviewsAdapter(ReviewsTab.this.getContext(), RevList) ;
                            rv.setItemAnimator(new DefaultItemAnimator());
                            rv.setAdapter(reviewsAdapter);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Review review = new Review();
                                String doc_book_title = document.getString("book");
                                if (doc_book_title.equalsIgnoreCase(book_title)) {
                                    String rev_title = document.get("review_title").toString();
                                    String rev_text = document.get("text").toString();
                                    review.setRevTitle(rev_title);
                                    review.setText(rev_text);
                                    // USER
                                    String doc_user = document.getString("user_name");
                                    dbSetUp.db.collection("users").whereEqualTo("email", doc_user)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                    if (task2.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                                            userName = document2.get("name").toString();
                                                            review.setUserName(document2.get("name").toString());
                                                            review.setUserEmail(document2.get("email").toString());
                                                            review.setUserID(document2.getId());
                                                            RevList.add(review);
                                                            reviewsAdapter.notifyDataSetChanged();
                                                        }
                                                    } else {
                                                        Log.w(TAG, "Error getting documents.", task2.getException());
                                                    }
                                                }
                                            });

                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }

                });
        return RevList;
    }

    
    @Override
    public void onResume() {
        super.onResume();
        RevList.clear();
        getRevList();
/*        dbSetUp.db.collection("reviews")
                .whereEqualTo("book", book_title)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    dc.getDocument().getData().
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });*/
    }
}
