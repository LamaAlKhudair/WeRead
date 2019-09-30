package com.example.wereadv10.ui.books.oneBook.reviews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.profile.profileTab.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsTab extends Fragment {
    private static final String TAG = "ReviewsTab";
    private RecyclerView rv;
    private List<Review> RevList;
    private ReviewsAdapter reviewsAdapter;
    private String book_title;
    private String book_id;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_tab, container, false);
        rv=view.findViewById(R.id.review_rv);
//        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        RevList=new ArrayList<>();
        book_title = getActivity().getIntent().getExtras().getString("TITLE");
        book_id = getActivity().getIntent().getExtras().getString("BOOK_ID");
        System.out.println("LAMA"+book_id);
        getRevList();
//        reviewsAdapter = new BooksAdapter(this,getReviews()) ;
//        rv.setItemAnimator(new DefaultItemAnimator());
//        rv.setAdapter(reviewsAdapter);
//        reviewsAdapter.notifyDataSetChanged();
        //addReview();
        return view;

    }
    private List<Review> getRevList(){
        dbSetUp.db.collection("reviews") //review_title
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ViewBooks.this, 2);
//                            recyclerView.setLayoutManager(mLayoutManager);
//                            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.setAdapter(adapter);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Review review = new Review();
                                DocumentReference doc = document.getDocumentReference("book");
                                String path = doc.getPath();
                                String col = path.substring(0, path.indexOf("/"));
                                String doc3 = path.substring(path.indexOf("/")+1);
                                if (doc3.equalsIgnoreCase(book_title)){
                                    String rev_title = document.get("review_title").toString();
                                    String rev_text = document.get("text").toString();
                                    review.setRevTitle(rev_title);
                                    review.setText(rev_text);
                                    // USER
                                    DocumentReference doc_user = document.getDocumentReference("user_name");
                                    String user_name = doc_user.getPath();
                                    String doc33 = user_name.substring(user_name.indexOf("/")+1);
                                    user_name = doc33;
                                    // Convert to User Object
//                                    dbSetUp.db.collection("users")
//                                            .whereEqualTo("name", user_name)
//                                            .get()
//                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
//                                                    if (task2.isSuccessful()) {
//                                                        for (QueryDocumentSnapshot user_document : task2.getResult()) {
//                                                            User user = user_document.toObject(User.class);
//                                                        }
//                                                    }
//                                            }
//                                                                       });

                                    review.setUserName(user_name);
                                    RevList.add(review);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return RevList;
    }

    private void addReview(){
        Map<String, Object> rev1 = new HashMap<>();
        rev1.put("text", "Los Angeles");
        rev1.put("review_title", "CA");

        rev1.put("book","kk" );
        rev1.put("user_name", "users/lama");
        dbSetUp.db.collection("reviews").document("LA")
                .set(rev1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });    }
}
