package com.example.wereadv10.ui.profile.profileTab.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.wereadv10.ui.profile.profileTab.BookTabFragment;
import com.example.wereadv10.ui.profile.profileTab.FollowingTabFragment;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;



    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BookTabFragment book_fragment = new BookTabFragment();
                return book_fragment;
            case 1:
                FollowingTabFragment following_fragment = new FollowingTabFragment();
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
