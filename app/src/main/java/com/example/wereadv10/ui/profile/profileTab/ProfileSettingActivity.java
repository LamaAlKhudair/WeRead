package com.example.wereadv10.ui.profile.profileTab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wereadv10.LoginActivity;
import com.example.wereadv10.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileSettingActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout nameLL, passwordLL, logoutLL;
    TextView nameTV;
    private FirebaseAuth mAuth;

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

                break;

            case R.id.profile_setting_resetPasswordLL:

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
}//end class