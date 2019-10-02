package com.example.wereadv10.ui.books.oneBook.reviews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wereadv10.R;
import com.example.wereadv10.ui.books.oneBook.bookPage;

import java.util.List;

public class OneReview extends AppCompatActivity {
    private Review review;
    private TextView username;
    private EditText revTitle;
    private EditText body;
    private Button ButtonAdd , cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_review);
        setTitle("Add Review");
        username=findViewById(R.id.User);
        revTitle=findViewById(R.id.revTitle);
        body=findViewById(R.id.body);
        ButtonAdd=findViewById(R.id.Add2);
        cancel= findViewById(R.id.CancelRev);
        ButtonAdd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){

                if(!revTitle.getText().toString().equals("")&&!body.getText().toString().equals(""))//or null?
                {
                    Intent intent = new Intent(OneReview.this,bookPage.class);
                    intent.putExtra("username", username.getText().toString());
                    intent.putExtra("revTitle", revTitle.getText().toString());
                    intent.putExtra("body", body.getText().toString());
                    startActivity(intent);
                    finish();
                }
                else

                    Toast.makeText(getApplicationContext(),"You Cannot Leave This Empty!",Toast.LENGTH_SHORT).show();
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




}

