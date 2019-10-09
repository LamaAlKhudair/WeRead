package com.example.wereadv10.ui.books.oneBook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ratingPage extends AppCompatActivity {
    private ImageView cover;
    private RatingBar ratingBar;
    private Button ratebtn;
    private Button cancelbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_page);
        ratebtn=findViewById(R.id.RateBtn);
        ratingBar=findViewById(R.id.ratingBar);
        cover = findViewById(R.id.cover);
        Glide.with(ratingPage.this).load(getIntent().getExtras().getString("COVER_RATING")).into(cover);
        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ratebtn.setBackgroundColor();

                if(ratingBar.getRating()==0)
                return;
                else {
                    int Num = Math.round(ratingBar.getRating());
                    String s = String.valueOf(Num);
                    Intent i = new Intent(ratingPage.this, bookPage.class);
                    i.putExtra("RATING_VALUE", s);
                    startActivity(i);
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



}
