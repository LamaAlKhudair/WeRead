package com.example.wereadv10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TestPageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
        signoutBtn = findViewById(R.id.testPage_signoutBtn);
        signoutBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    public void signOut() {
        // [START auth_sign_out]
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(TestPageActivity.this,LoginActivity.class);
            startActivity(intent);        }
        // [END auth_sign_out]
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.testPage_signoutBtn:
                signOut();
                break;
        }//end switch
    }
}
