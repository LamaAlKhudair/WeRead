package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailET, passwordET;
    private TextView createAccountTV, forgetPasswordTV;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private String TAG = LoginActivity.class.getSimpleName();
private TextInputLayout emailIL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        emailET = findViewById(R.id.login_emailET);
        passwordET = findViewById(R.id.login_passwordET);
        createAccountTV = findViewById(R.id.login_createAccountTV);
        createAccountTV.setOnClickListener(this);
        forgetPasswordTV = findViewById(R.id.login_forgotPasswordTV);
        forgetPasswordTV.setOnClickListener(this);
        loginBtn = findViewById(R.id.login_loginBtn);
        loginBtn.setOnClickListener(this);
        emailIL = findViewById(R.id.login_emailIL);
        mAuth = FirebaseAuth.getInstance();
        //Disable Button When EditText Is Empty
        //loginBtn.setEnabled(false);
        emailET.addTextChangedListener(loginTextWatcher);
        passwordET.addTextChangedListener(loginTextWatcher);
        checkUserState();

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String emailInput = emailET.getText().toString().trim();
            String passwordInput = passwordET.getText().toString().trim();
            loginBtn.setEnabled(!emailInput.equals("") && !passwordInput.equals(""));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void checkUserState() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.login_loginBtn:
/*                if (emailET.getText().toString().equals("") || passwordET.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "the email or password is wrong",
                            Toast.LENGTH_LONG).show();
                }*/
                loginUser(emailET.getText().toString(), passwordET.getText().toString());
                break;
            case R.id.login_createAccountTV:
                intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                break;
            case R.id.login_forgotPasswordTV:
                intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
        }//end switch
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                //there is'n user with this Email
                                emailIL.setError("the email or password is wrong");
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //the password is wrong
                                emailIL.setError("the email or password is wrong");

                            } else {
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

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LoginActivity.this, "check the email link to verify account", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }
}
