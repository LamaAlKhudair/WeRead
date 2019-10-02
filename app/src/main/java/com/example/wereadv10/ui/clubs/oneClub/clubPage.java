package com.example.wereadv10.ui.clubs.oneClub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;


public class clubPage extends AppCompatActivity {

    public TextView clubName;
    public ImageView clubImage;
    public  TextView clubOwner;
    public TextView clubDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        clubName = findViewById(R.id.club_name);
        clubImage = findViewById(R.id.club_img);
        clubOwner = findViewById(R.id.club_owner);
        clubDescription = findViewById(R.id.club_description);

        getExtras();
    }

    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("NAME") != null)
                clubName.setText(intent.getExtras().getString("NAME"));
            if (intent.getExtras().getString("IMAGE") != null) {
             //   Glide.with(clubPage.this).load(intent.getExtras().getString("IMAGE")).into(clubImage);
            }else{
             //   Glide.with(clubPage.this).load(R.drawable.logo).into(clubImage);
            }
            if (intent.getExtras().getString("OWNER") != null)
             //   clubOwner.setText(intent.getExtras().getString("OWNER"));
            if (intent.getExtras().getString("DESCRIPTION") != null)
                clubDescription.setText(intent.getExtras().getString("DESCRIPTION"));

        }
    }
}
