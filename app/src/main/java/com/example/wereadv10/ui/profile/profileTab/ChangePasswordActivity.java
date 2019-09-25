package com.example.wereadv10.ui.profile.profileTab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wereadv10.ForgotPasswordActivity;
import com.example.wereadv10.R;
import com.example.wereadv10.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView forgotPasswordTV;
    private TextInputLayout oldPasswordIL, newPasswordIL, confirmNwePasswordIL;
    private EditText oldPasswordET, newPasswordET, confirmNewPasswordET;
    private Button changePasswordBtn;
    private String TAG = ChangePasswordActivity.class.getSimpleName();
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        forgotPasswordTV = findViewById(R.id.change_password_forgot_passwordTV);
        oldPasswordET = findViewById(R.id.change_password_old_passwordET);
        newPasswordET = findViewById(R.id.change_password_newPasswordET);
        confirmNewPasswordET = findViewById(R.id.change_password_confirmNewPasswordET);
        oldPasswordIL = findViewById(R.id.change_password_oldPasswordInputLayout);
        newPasswordIL = findViewById(R.id.change_password_newPasswordInputLayout);
        confirmNwePasswordIL = findViewById(R.id.change_password_confirmNewPasswordInputLayout);
        changePasswordBtn = findViewById(R.id.change_password_restPassBtn);

        oldPasswordET.addTextChangedListener(changePasswordTextWatcher);
        newPasswordET.addTextChangedListener(changePasswordTextWatcher);
        confirmNewPasswordET.addTextChangedListener(changePasswordTextWatcher);

        forgotPasswordTV.setOnClickListener(this);
        changePasswordBtn.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        initToolBar();
    }//end onCreate()

    private TextWatcher changePasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String oldPasswordInput = oldPasswordET.getText().toString().trim();
            String newPasswordInput = newPasswordET.getText().toString().trim();
            String confirmNewPasswordInput = confirmNewPasswordET.getText().toString().trim();

            changePasswordBtn.setEnabled(!oldPasswordInput.equals("") & !newPasswordInput.equals("") & !confirmNewPasswordInput.equals(""));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };//end TextWatcher()

    public void changePassword(String oldPassword, final String newPassword, String confirmNewPassword) {
        //if the new password doesn't match show message otherwise change password
        if (!newPassword.equals(confirmNewPassword)) {
            newPasswordIL.setError("the password  does't match, please try again");
            newPasswordET.setText("");
            confirmNewPasswordET.setText("");
            return;
        }//end if

        // get user email from FireBase
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }
        //change password

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPassword);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });

    }//end changePassword()


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password_forgot_passwordTV:
forgotPassword();
                break;

            case R.id.change_password_restPassBtn:
                changePassword(oldPasswordET.getText().toString(),newPasswordET.getText().toString(),confirmNewPasswordET.getText().toString());
                break;
        }//end switch
    }

    private void forgotPassword() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }
        //send email
        mAuth.sendPasswordResetEmail(email)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePasswordActivity.this, "We have sent you email instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initToolBar() {
        setTitle("Change Password");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()
    @Override

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp
}//end class
