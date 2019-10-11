package com.example.wereadv10.ui.otherProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wereadv10.R;
import com.example.wereadv10.SignUp;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.profile.ProfileFragment;
import com.example.wereadv10.ui.profile.profileTab.BookTabFragment;
import com.example.wereadv10.ui.profile.profileTab.FollowingTabFragment;
import com.example.wereadv10.ui.profile.profileTab.adapter.FollowingAdapter;
import com.example.wereadv10.ui.profile.profileTab.adapter.FragmentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private OtherProfileBookTabFragment bookTabFragment;
    private OtherProfileFollowingTabFragment followingTabFragment;
    OtherProfileFragmentAdapter fragmentAdapter;
    private TextView nameTV;
    Button followBtn;
    private String TAG = ProfileFragment.class.getSimpleName();
    private String otherUserEmail = "";
    private String otherUserID = "";
    private com.example.wereadv10.dbSetUp dbSetUp;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        getExtras();

        dbSetUp = new dbSetUp();

        nameTV = findViewById(R.id.other_profile_name);

        //to display the name
        displayName();
        initToolBar();
//for tabs
        viewPager = findViewById(R.id.other_profile_viewPager);
        TabLayout tabLayout = findViewById(R.id.other_profile_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
// creating and adding the fragment to the book_tab_card

        bookTabFragment = new OtherProfileBookTabFragment();
        followingTabFragment = new OtherProfileFollowingTabFragment();

        fragmentAdapter = new OtherProfileFragmentAdapter(this.getSupportFragmentManager(), tabLayout.getTabCount());
        fragmentAdapter.addFragment(bookTabFragment, name + " Library");
        fragmentAdapter.addFragment(followingTabFragment, "Following");

        //
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
//
        followBtn = findViewById(R.id.other_profile_followBtn);
        followBtn.setOnClickListener(this);
    }//end onCreate()

    private void displayName() {
        Source source = Source.CACHE;
        dbSetUp.db.collection("users")
                .whereEqualTo("email", otherUserEmail)
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get user id and name
                                otherUserID = document.getId();
                                name = document.get("name").toString();

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        nameTV.setText(name);
                    }
                });
    }

    private void getExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            if (intent.getExtras().getString("otherUserEmail") != null)
                otherUserEmail = intent.getExtras().getString("otherUserEmail");
            if (intent.getExtras().getString("otherUserID") != null)
                otherUserID = intent.getExtras().getString("otherUserID");
        }
    }//end getExtras()


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp

    private void initToolBar() {
        setTitle(name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_profile_followBtn:
                if (followBtn.getText().toString().equals("Follow"))
                    followUser();
                else {
                    Toast.makeText(OtherProfileActivity.this, "unFollow", Toast.LENGTH_SHORT).show();
                    unFollowUser();
                }
                break;
        }//end switch
    }//end onClick

    private void followUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final Map<String, Object> follow = new HashMap<>();
        follow.put("followed_by_id", user.getUid());//uerId
        follow.put("followed_id", otherUserID);//otherUser
        dbSetUp.db.collection("following")
                .add(follow)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        followBtn.setText("UnFollow");
//followBtn.setBackgroundColor();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }//end followUser()

    private void unFollowUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbSetUp.db.collection("following")
                .whereEqualTo("followed_id", otherUserID)
                .whereEqualTo("followed_by_id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //get book id
                                String docId = document.getId();
                                deleteDoc(docId);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }//end unFollowUser()

    private void deleteDoc(String id) {
        dbSetUp.db.collection("following").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        followBtn.setText("Follow");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OtherProfileActivity.this, "pleas try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}//end class
