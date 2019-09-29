package com.example.wereadv10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView loginTV;
    private TextInputLayout  emailIL, passwordIL;
    private EditText nameET, emailET, passwordET, confirmPasswordET;
    private Button signUpBtn;
    private String TAG = SignUp.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initToolBar();
        mAuth = FirebaseAuth.getInstance();

        loginTV = findViewById(R.id.singup_logIn);
        nameET = findViewById(R.id.singup_nameET);
        emailET = findViewById(R.id.singup_emailET);
        passwordET = findViewById(R.id.singup_passwordET);
        confirmPasswordET = findViewById(R.id.singup_confirmPasswordET);
        signUpBtn = findViewById(R.id.singup_registerBtn);
        emailIL = findViewById(R.id.singup_emailInputLayout);
        passwordIL = findViewById(R.id.singup_passwordInputLayout);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserAccount(emailET.getText().toString(), passwordET.getText().toString(), confirmPasswordET.getText().toString(), nameET.getText().toString());
            }
        });

        nameET.addTextChangedListener(signUpTextWatcher);
        emailET.addTextChangedListener(signUpTextWatcher);
        passwordET.addTextChangedListener(signUpTextWatcher);
        confirmPasswordET.addTextChangedListener(signUpTextWatcher);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }//end onCreate()

    private TextWatcher signUpTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nameInput = nameET.getText().toString().trim();
            String emailInput = emailET.getText().toString().trim();
            String passwordInput = passwordET.getText().toString().trim();
            String confirmPasswordInput = confirmPasswordET.getText().toString().trim();

            signUpBtn.setEnabled(!nameInput.equals("") & !emailInput.equals("") & !passwordInput.equals("") & !confirmPasswordInput.equals(""));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void createUserAccount(final String email, String password, String confirmPassword, final String name) {
        //if the passwordET doesn't match show dialog otherwise create account
        if (!password.equals(confirmPassword)) {
            passwordIL.setError("the password  does't match, please try again");
            passwordET.setText("");
            confirmPasswordET.setText("");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userf = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            userf.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            userf.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated.");
                                            }
                                        }
                                    });
                           // Create a new user with a first and last name
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", name);

                        // Add a new document with a generated ID
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                            sendVerificationEmail();

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                emailIL.setError("User with Email id already exists");
                                passwordIL.setError(null);
                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                passwordIL.setError("Password is weak, password must a least 6 characters");
                                passwordET.setText("");
                                confirmPasswordET.setText("");
                                emailIL.setError(null);

                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                emailIL.setError("incorrect email format ");
                                passwordIL.setError(null);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }

                        // ...
                    }
                });
    }//end createUserAccount()

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            showDialog("Successfully create account, we have send to your email verification link to active your account. ");
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Log.d(TAG, "there is problem the email doesn't send: ");

                        }
                    }
                });
    }

    public void showDialog(String msg) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // after email is sent just logout the user and finish this activity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SignUp.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.show();
    }//end showDialog()

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp

    private void initToolBar() {
        setTitle("Create Account");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()
}//end class
