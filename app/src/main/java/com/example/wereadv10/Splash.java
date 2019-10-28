package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wereadv10.notification.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity implements View.OnClickListener {
    Timer timer;
    boolean click = false;
    private FirebaseAuth mAuth;
    private String TAG = Splash.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LinearLayout splashlin = findViewById(R.id.splash);
        splashlin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (click)
                    return;
                if (checkUserLogin()) {
                    //user is login
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash:
                if (checkUserLogin()) {
                    //user is login
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(Splash.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }

    }

    private boolean checkUserLogin() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            return true;
        } else
            return false;
    }


}
