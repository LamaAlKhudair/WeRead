package com.example.wereadv10.ui.explore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wereadv10.ForgotPasswordActivity;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {
    private Button getCat;
    private TextView catHere, bookHere;
    private com.example.wereadv10.dbSetUp dbSetUp;
    TextView test ;
    private static final String TAG = "ExploreFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_explore, container, false);


        test = root.findViewById(R.id.textView_books);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        dbSetUp = new dbSetUp();

        catHere = root.findViewById(R.id.catHere);
        bookHere=root.findViewById(R.id.bookHere);

        //getCategories();
        getCat = root.findViewById(R.id.getCat);
        return root;
    }
    private  void getFiveBooks(){
        CollectionReference bookRef = dbSetUp.db.collection("books");
        bookRef.limit(5).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData().toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    private void getCategories() {
        // this function retrive all the categories in the database
        // and save them in categoies list
        dbSetUp.db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> categoies = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categoies.add(document.getData().get("category_name").toString());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void getBooks() {
        // this function retrive all the books in the system and
        // save it in books list
        dbSetUp.db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> books = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                books.add(document.getData().toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    private void getOneBook(String bookName) {
        // this function retrive one book based on BookName == book_title
        // and print it.

        dbSetUp.db.collection("books")
                .whereEqualTo("book_title", bookName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    private void findCat(String documentReference){
        /*dbSetUp.db.document("categories/\("+documentReference+")")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData());
                            }
                        }
                    }
                });*/
    }
    private void getClubs(){
        // this function retrive all the clubs in the system and
        // save them in clubs List
        dbSetUp.db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> clubs = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                clubs.add(document.getData().toString());
                                System.out.println(document.getData().toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
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