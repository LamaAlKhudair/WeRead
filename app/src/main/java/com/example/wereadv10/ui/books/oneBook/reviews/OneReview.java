package com.example.wereadv10.ui.books.oneBook.reviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wereadv10.R;
import com.example.wereadv10.ui.books.oneBook.bookPage;

import java.util.List;

public class OneReview extends AppCompatActivity {
    private Review review;
    private EditText username;
    private EditText revTitle;
    private EditText body;
    private Button ButtonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_review);
        username=findViewById(R.id.User);
        revTitle=findViewById(R.id.revTitle);
        body=findViewById(R.id.body);
        ButtonAdd=findViewById(R.id.Add2);
        ButtonAdd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(OneReview.this,bookPage.class);
                intent.putExtra("username",username.getText().toString());
                intent.putExtra("revTitle",revTitle.getText().toString());
                intent.putExtra("body",body.getText().toString());
                startActivity(intent);

            }

        });





    }


}
