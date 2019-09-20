package com.example.wereadv10.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wereadv10.LoginActivity;
import com.example.wereadv10.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Button signoutBtn;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        signoutBtn = root.findViewById(R.id.testPage_signoutBtn);
        signoutBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


        return root;
    }
    public void signOut() {
        // [START auth_sign_out]
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
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