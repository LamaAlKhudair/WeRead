package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
EditText emailEt;
Button restPasswordBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initToolBar();

        mAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.forgotPassword_emailET);
        restPasswordBtn = findViewById(R.id.forgotPassword_restPassBtn);
        restPasswordBtn.setOnClickListener(this);
    }

    private void initToolBar() {
        setTitle("Forgot Password");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgotPassword_restPassBtn:
                String email = emailEt.getText().toString();

                if (email.equals("")) {
                    Toast.makeText(getApplication(), "Enter your email id please ", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "We have sent you email instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp
}
