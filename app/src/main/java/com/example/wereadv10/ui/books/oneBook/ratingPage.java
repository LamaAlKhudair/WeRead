package com.example.wereadv10.ui.books.oneBook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ratingPage extends AppCompatActivity {
    private ImageView cover;
    private RatingBar ratingBar;
    private Button ratebtn;
    private Button cancelbtn;
    private String userID, book_id;
    private boolean hasRate;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_page);
        ratebtn=findViewById(R.id.RateBtn);
        ratingBar=findViewById(R.id.ratingBar);
        initToolBar();
        cover = findViewById(R.id.cover);
        Glide.with(ratingPage.this).load(getIntent().getExtras().getString("COVER_RATING")).into(cover);
        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID=book_id ="";
                getExtas();
                if(ratingBar.getRating()==0)
                return;
                else {
                    int num = Math.round(ratingBar.getRating());
                    System.out.println("HASRATe in close "+hasRate);
                    if(hasRate){
                        updateUserRate(num);
                    }else{
                        rateBook(num);
                    }
                    finish();
                }

            }
        });
        cancelbtn=(Button)findViewById(R.id.cancell);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(ratingPage.this).create();
                alertDialog.setMessage("Discard this rating?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.show();
            }
        });
    }
    private void updateUserRate(int rateInt){
        final Map<String, Object> rev1 = new HashMap<>();
        rev1.put("rate",rateInt);
        //rev1.put("userID",userID);
        final String[] id = new String[1];
         dbSetUp.db
                .collection("books").document(book_id).collection("rates")
                .whereEqualTo("userID",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if (task.isSuccessful()) {
                     for (DocumentSnapshot document : task.getResult()) {
                        id[0] = document.getId();
                     }
                     dbSetUp.db
                             .collection("books").document(book_id).collection("rates")
                             .document(id[0]).update(rev1);
                     updateBookRate();
                 } else {
                     System.out.println( "Error getting documents: ");
                 }
             }
         });;

    }
    private String getRandom(){
        return UUID.randomUUID().toString();
    }
    private void getExtas() {
        if (getIntent().getExtras().getString("USER_ID") != null){
            userID =getIntent().getExtras().getString("USER_ID");
        }
        if (getIntent().getExtras().getString("BOOK_ID") != null){
            book_id =getIntent().getExtras().getString("BOOK_ID");
        }else {
            System.out.println("No intent ");
        }

        if (getIntent().getExtras().getBoolean("HAS_RATE") != false){
                hasRate = getIntent().getExtras().getBoolean("HAS_RATE");
                System.out.println("HASRATe in the"+hasRate);
        }else{
            hasRate = false;
            System.out.println("In else");
        }
    }
    private void rateBook(int rateInt){
        final Map<String, Object> rev1 = new HashMap<>();
        rev1.put("rate",rateInt);
        rev1.put("userID",userID);
        dbSetUp.db
                .collection("books").document(book_id)
                .collection("rates").document(getRandom()).set(rev1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");
                        updateBookRate();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                    }
                });

    }
    private void updateBookRate(){
        dbSetUp.db
                .collection("books").document(book_id)
                .collection("rates").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    Long ave = Long.valueOf(0);
                    for (DocumentSnapshot document : task.getResult()) {
                        count++;
                        ave = ave+document.getLong("rate");

                    }
                    final Map<String, Object> rev1 = new HashMap<>();
                    rev1.put("book_rate",(ave/count));
                    dbSetUp.db
                            .collection("books").document(book_id).update(rev1);
                } else {
                    System.out.println( "Error getting documents: ");
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp

    private void initToolBar() {
        setTitle("Add Rating");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()

}
