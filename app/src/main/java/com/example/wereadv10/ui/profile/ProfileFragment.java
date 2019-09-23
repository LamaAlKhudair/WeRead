package com.example.wereadv10.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.wereadv10.LoginActivity;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.profile.profileTab.BookTabFragment;
import com.example.wereadv10.ui.profile.profileTab.FollowingTabFragment;
import com.example.wereadv10.ui.profile.profileTab.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    private BookTabFragment bookTabFragment;
    private FollowingTabFragment followingTabFragment;
    FragmentAdapter fragmentAdapter;
 //   private Button signoutBtn;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

//for tabs
        viewPager = root.findViewById(R.id.profile_viewPager);
        TabLayout tabLayout = root.findViewById(R.id.profile_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
// creating and adding the fragment to the adapter

        bookTabFragment = new BookTabFragment();
        followingTabFragment = new FollowingTabFragment();

        fragmentAdapter = new FragmentAdapter(getFragmentManager(), tabLayout.getTabCount());
        fragmentAdapter.addFragment(bookTabFragment, "Book");
        fragmentAdapter.addFragment(followingTabFragment, "Following");

        //
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);


/*        signoutBtn = root.findViewById(R.id.testPage_signoutBtn);
        signoutBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();*/


        return root;
    }

    public void signOut() {
        // [START auth_sign_out]
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
        // [END auth_sign_out]
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
/*            case R.id.testPage_signoutBtn:
                signOut();
                break;*/
        }//end switch
    }


}