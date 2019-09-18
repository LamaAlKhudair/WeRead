package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
private EditText nameET, emailET, passwordET, confirmPasswordET;
private Button signupBtn;
private String TAG = Signup.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.singup_nameET);
        emailET = findViewById(R.id.singup_emailET);
        passwordET = findViewById(R.id.singup_passwordET);
        confirmPasswordET = findViewById(R.id.singup_confirmPasswordET);
        signupBtn = findViewById(R.id.singup_registerBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserAccount(emailET.getText().toString(), passwordET.getText().toString(), confirmPasswordET.getText().toString());
            }
        });


    }//end onCreate()

    public void createUserAccount(String email,String password,String confirmPassword){
        //if the passwordET doesn't match show dialog otherwise create account
        if (!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(), "the passwordET does't match, please try again",
                    Toast.LENGTH_SHORT).show();
            passwordET.setText("");
            confirmPasswordET.setText("");
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Signup.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User with Email id already exists",
                                        Toast.LENGTH_LONG).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "incorrect emailET format ",
                                        Toast.LENGTH_LONG).show();
                            }
                            else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(getApplicationContext(), "Password is weak, passwordET must a least 6 characters",
                                        Toast.LENGTH_LONG).show();
                                passwordET.setText("");
                                confirmPasswordET.setText("");
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Signup.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }

                        // ...
                    }
                });
    }//end createUserAccount()

}//end class
