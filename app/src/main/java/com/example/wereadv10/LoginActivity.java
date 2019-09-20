package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailET, passwordET;
    private TextView createAccountTV, forgetPasswordTV;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.login_emailET);
        passwordET = findViewById(R.id.login_passwordET);
        createAccountTV = findViewById(R.id.login_createAccountTV);
        createAccountTV.setOnClickListener(this);
        forgetPasswordTV = findViewById(R.id.login_forgotPasswordTV);
        forgetPasswordTV.setOnClickListener(this);
        loginBtn = findViewById(R.id.login_loginBtn);
        loginBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
loginBtn.setEnabled(false);
        checkUserState();

    }

    private void checkUserState() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, TestPageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_loginBtn:
                if (emailET.getText().toString().equals("")|| passwordET.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "the email or password is wrong",
                            Toast.LENGTH_LONG).show();
                }
                loginUser(emailET.getText().toString(), passwordET.getText().toString());
                break;
            case R.id.login_createAccountTV:
                 intent = new Intent(LoginActivity.this, Signup.class);
                startActivity(intent);
                break;
            case R.id.login_forgotPasswordTV:
                intent= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
        }//end switch
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                                //there is'n user with this Email
                                Toast.makeText(getApplicationContext(), "the email or password is wrong",
                                        Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //the password is wrong
                                Toast.makeText(getApplicationContext(), "the email or password is wrong",
                                        Toast.LENGTH_LONG).show();
                            }else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        // ...
                    }
                });
    }
}
