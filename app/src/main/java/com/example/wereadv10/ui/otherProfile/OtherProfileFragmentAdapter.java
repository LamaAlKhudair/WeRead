package com.example.wereadv10.ui.otherProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.wereadv10.ui.profile.profileTab.BookTabFragment;
import com.example.wereadv10.ui.profile.profileTab.FollowingTabFragment;

import java.util.ArrayList;
import java.util.List;


public class OtherProfileFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;



    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public OtherProfileFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OtherProfileBookTabFragment book_fragment = new OtherProfileBookTabFragment();
                return book_fragment;
            case 1:
                OtherProfileFollowingTabFragment following_fragment = new OtherProfileFollowingTabFragment();
                return following_fragment;
            default:
                return null;
        } //switch
    }//getItem

    @Override
    public int getCount() {
        return mNumOfTabs;
    }//getCount


    public void addFragment(Fragment fragment, String tiltle) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(tiltle);
    }//addFragment


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
} // class
