package com.example.wereadv10.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wereadv10.LoginActivity;
import com.example.wereadv10.MySharedPreference;
import com.example.wereadv10.R;
import com.example.wereadv10.SignUp;
import com.example.wereadv10.ui.profile.profileTab.BookTabFragment;
import com.example.wereadv10.ui.profile.profileTab.FollowingTabFragment;
import com.example.wereadv10.ui.profile.profileTab.ProfileSettingActivity;
import com.example.wereadv10.ui.profile.profileTab.adapter.FragmentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    private BookTabFragment bookTabFragment;
    private FollowingTabFragment followingTabFragment;
    FragmentAdapter fragmentAdapter;
    //   private Button signoutBtn;
    private ImageView profileSettingImg;
    private FirebaseAuth mAuth;
    private TextView nameTV;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = ProfileFragment.class.getSimpleName();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTV = root.findViewById(R.id.profile_name);
        mAuth = FirebaseAuth.getInstance();
        //to display the name
        displayName();

//for tabs
        viewPager = root.findViewById(R.id.profile_viewPager);
        TabLayout tabLayout = root.findViewById(R.id.profile_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
// creating and adding the fragment to the book_tab_card

        bookTabFragment = new BookTabFragment();
        followingTabFragment = new FollowingTabFragment();

        fragmentAdapter = new FragmentAdapter(getFragmentManager(), tabLayout.getTabCount());
        fragmentAdapter.addFragment(bookTabFragment, "My Library");
        fragmentAdapter.addFragment(followingTabFragment, "Following");

        //
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        profileSettingImg = root.findViewById(R.id.profile_settingImg);
        profileSettingImg.setOnClickListener(this);


        return root;
    }

    private void displayName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            // If the above were null, iterate the provider data
            // and set with the first non null data
            for (UserInfo userInfo : user.getProviderData()) {
                if (name == null && userInfo.getDisplayName() != null) {
                    name = userInfo.getDisplayName();
                    MySharedPreference.putString(getContext(),"userName",name);

                }
            }
            nameTV.setText(name);

        }//end if

/*        final DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        Source source = Source.CACHE;
// Get the document, forcing the SDK to use the offline cache
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    String name = document.get("name").toString();
                    nameTV.setText(name);
                    Log.d(TAG, "Cached document data: " + document.getData());
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });*/
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_settingImg:
                startActivity(new Intent(getContext(), ProfileSettingActivity.class));
                break;
        }//end switch
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MySharedPreference.getString(getContext(),"userName","").equals("")){
            nameTV.setText(MySharedPreference.getString(getContext(),"userName",""));
        }
        else
            nameTV.setText("");
    }

}