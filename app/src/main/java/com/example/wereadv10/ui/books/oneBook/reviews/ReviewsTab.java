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
    private Button ButtonAdd;
    private Button ButtonAdd2;

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_tab, container, false);
        rv=view.findViewById(R.id.review_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        RevList=new ArrayList<>();
        book_title = getActivity().getIntent().getExtras().getString("TITLE");
        ButtonAdd=view.findViewById(R.id.addButton);
        ButtonAdd2=view.findViewById(R.id.Add2);
        getRevList();

        //new added review
//        if(getActivity().getIntent()!=null) {
//            Review review = new Review(getActivity().getIntent().getExtras().getString("username"), getActivity().getIntent().getExtras().getString("revTitle"),
//                    getActivity().getIntent().getExtras().getString("body"));
//            if (RevList.isEmpty()){} else {
//                RevList.add(RevList.size() - 1, review);// لأن الاراي فاضيه ماراح نستخدم هذا حالياً
//            RevList.add(0, review);
//            reviewsAdapter.notifyDataSetChanged();}
//
//        }

        ButtonAdd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent i = new Intent(getContext(),OneReview.class);
                startActivity(i);

            }

        });

        return view;

    }



    private List<Review> getRevList(){
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
                                    DocumentReference doc_user = document.getDocumentReference("user_name");
                                    String user_name1 = doc_user.getPath();
                                    String doc33 = user_name1.substring(user_name1.indexOf("/") + 1);
                                    String user_name = doc33;
                                    review.setUserName(user_name);
                                    //getBook(doc);
                                    RevList.add(review);
                                    reviewsAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        for (Review review2 : RevList){
                            System.out.println("LAMA"+review2.getRevTitle());
                            System.out.println("LAMA2 \t"+ review2.getText());
                        }
                    }

                });
        return RevList;
    }
   /* private void getBook(DocumentReference doc){
        dbSetUp.db.collection("books").document(doc.getPath()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot book_document : task2.getResult()) {
                                String doc3 = book_document.get("book_title").toString();
                                if (doc3.equalsIgnoreCase(book_title)){
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
                                    RevList.add(review);
                                }
                            }
                        }
                    }
                });
    }

    */
    private void addReview(){
        final Map<String, Object> rev1 = new HashMap<>();
        rev1.put("text", "Los Angeles");
        rev1.put("review_title", "CA");
       dbSetUp.db.collection("books").whereEqualTo("book_title",book_title).get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {

                               DocumentReference doc = document.getReference();
                               rev1.put("book", doc);
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
                                       });
                           }
                       } else {
                           Log.w(TAG, "Error getting documents.", task.getException());
                       }
                   }
               });

          }

}

