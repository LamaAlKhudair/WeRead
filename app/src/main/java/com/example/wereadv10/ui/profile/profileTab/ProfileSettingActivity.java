package com.example.wereadv10.ui.profile.profileTab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wereadv10.LoginActivity;
import com.example.wereadv10.MySharedPreference;
import com.example.wereadv10.R;
import com.example.wereadv10.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout nameLL, passwordLL, logoutLL;
    private TextView nameTV;
    private FirebaseAuth mAuth;
    private String TAG = ProfileSettingActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        nameLL = findViewById(R.id.profile_setting_nameLL);
        passwordLL = findViewById(R.id.profile_setting_resetPasswordLL);
        logoutLL = findViewById(R.id.profile_setting_logoutLL);
        nameTV = findViewById(R.id.profile_setting_nameTV);
        mAuth = FirebaseAuth.getInstance();


        nameTV.setOnClickListener(this);
        nameLL.setOnClickListener(this);
        passwordLL.setOnClickListener(this);
        logoutLL.setOnClickListener(this);
        initToolBar();

displayName();

    }

    private void displayName() {
        //to display the name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            // If the above were null, iterate the provider data
            // and set with the first non null data
            for (UserInfo userInfo : user.getProviderData()) {
                if (name == null && userInfo.getDisplayName() != null) {
                    name = userInfo.getDisplayName();
                }
            }
            nameTV.setText(name);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp

    private void initToolBar() {
        setTitle("Setting");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_setting_nameLL:
                updateNameDialog();
                break;

            case R.id.profile_setting_resetPasswordLL:
                Intent intent = new Intent(ProfileSettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.profile_setting_logoutLL:
                logoutDialog();
                break;
            case R.id.profile_setting_nameTV:

                break;
        }//end switch
    }//end onClick

    public void signOut() {
        // [START auth_sign_out]
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        // [END auth_sign_out]
    }

    public void logoutDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileSettingActivity.this);
        //set dialog msg
        alertDialog.setMessage("Are you sure you want to logout?");
        //set Yes Btn
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        signOut();
                    }//end of OnClick
                }//end of OnClickListener
        );//end setPositiveButton

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }//end onClick
                }//end OnClickListener
        );//end setNegativeButton

        //show dialog
        alertDialog.show();
    }//end logoutDialog



    private void updateNameDialog() {
        android.app.AlertDialog.Builder updateNameDialog = new android.app.AlertDialog.Builder(ProfileSettingActivity.this);//,R.style.AlertDialogStyle
        updateNameDialog.setTitle("Update Name");
        final EditText forgetEmilET = new EditText(ProfileSettingActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        forgetEmilET.setLayoutParams(lp);
        updateNameDialog.setView(forgetEmilET);
        updateNameDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(ProfileSettingActivity.this);
                if (forgetEmilET.getText().toString().equals("")) {
                    dialog.setMessage("enter the name");
                    dialog.setPositiveButton("ok", null);
                    dialog.show();
                }else {
                    //TODO: return boolean if true show update message else pleas try again
                    boolean check ;
                    check = updateName(forgetEmilET.getText().toString());
                    if (check) {
                        dialog.setMessage("the name is update");
                        dialog.setPositiveButton("ok", null);
                        dialog.show();
                    }else {
                        dialog.setMessage("can't update the name now please try again");
                        dialog.setPositiveButton("ok", null);
                        dialog.show();
                    }
                }//end else
            }//end onClick
        });//end setPositiveButton
        updateNameDialog.show();
    }
    public boolean updateName(final String name) {
        DocumentReference userName = db.collection("users").document(mAuth.getUid());

// Set the "isCapital" field of the city 'DC'
        userName
                .update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nameTV.setText(name);
                        //update FirebaseUser profile
                        FirebaseUser userf = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        userf.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                            MySharedPreference.putString(ProfileSettingActivity.this,"userName",name);
                                            update = true;
                                        }else {
                                            update = false;
                                        }
                                    }
                                });
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        update = false;

                    }
                });
        return update;
    }// updateName()
}//end class
