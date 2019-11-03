package com.example.wereadv10.ui.clubs.oneClub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class createVote extends AppCompatActivity {
    private Button addVoteBtn;
    private EditText VoteNameEt,VotedesEt,optionOneEt,optionTwoEt;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vote);
        setTitle("Add Vote");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        VoteNameEt = findViewById(R.id.VoteNameEt);
        VotedesEt = findViewById(R.id.VotedesEt);
        optionOneEt = findViewById(R.id.optionOneEt);
        optionTwoEt = findViewById(R.id.optionTwoEt);


        addVoteBtn = findViewById(R.id.create_vote_button);
        addVoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVote();
            }
        });
    }



    private void addVote() {
        if(!VoteNameEt.getText().toString().equals("")&&!VotedesEt.getText().toString().equals("")&&!optionOneEt.getText().toString().equals("")
                &&!optionTwoEt.getText().toString().equals(""))
        {
            final Map<String, Object> vote = new HashMap<>();
            vote.put("vote_title", VoteNameEt.getText().toString());
            vote.put("vote_desc", VotedesEt.getText().toString());
            vote.put("option_one", optionOneEt.getText().toString());
            vote.put("option_two", optionTwoEt.getText().toString());
            vote.put("club_id",getIntent().getExtras().getString("CLUB_ID"));

            dbSetUp.db.collection("votes").document(getRandom())
                    .set(vote)
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
