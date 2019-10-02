package com.example.wereadv10.ui.books.oneBook.reviews;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.oneBook.bookPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class OneReview extends AppCompatActivity {
    private Review review;
    private TextView username;
    private EditText revTitle;
    private EditText body;
    private Button ButtonAdd, cancel;
    private String book_title, userEmail;
    private List<Review> RevList;
    private ReviewsTab reviewsTab;
    private ReviewsAdapter adapter;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_review);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        book_title = "";
        userEmail = "";
        setTitle("Add Review");
        RevList=new ArrayList<>();
        username=findViewById(R.id.User);
        revTitle=findViewById(R.id.revTitle);
        revTitle.setSelection(0);
        body=findViewById(R.id.body);
        body.setSelection(0);
        ButtonAdd=findViewById(R.id.Add2);
        cancel=findViewById(R.id.cancelRev);
        getExtras();
        ButtonAdd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){

                    addReview();
            }

        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(OneReview.this).create();
                alertDialog.setMessage("Discard this review?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
    private void addReview(){
        if(!revTitle.getText().toString().equals("")&&!body.getText().toString().equals(""))
                {
        final Map<String, Object> rev1 = new HashMap<>();
        rev1.put("text", body.getText().toString());
        rev1.put("review_title", revTitle.getText().toString());
        rev1.put("user_name", userEmail);
        rev1.put("book", book_title);
        dbSetUp.db.collection("reviews").document(getRandom())
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
                                                Log.w("Error writing document", e);
                                            }
                                        });
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"You Cannot Leave This Empty!",Toast.LENGTH_SHORT).show();
            }

    }

    private String getRandom(){
        return UUID.randomUUID().toString();
    }
    private void getExtras(){
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("BOOK_TITLE") != null)
                book_title=intent.getExtras().getString("BOOK_TITLE");
            if (intent.getExtras().getString("USER")!= null){
                userEmail=intent.getExtras().getString("USER");
                username.setText(userEmail);
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

