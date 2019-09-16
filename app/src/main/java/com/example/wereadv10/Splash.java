package com.example.wereadv10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Splash extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LinearLayout splashlin = findViewById(R.id.splash);
        splashlin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Splash.this, LoginActivity.class);
        startActivity(intent);
    }





}
