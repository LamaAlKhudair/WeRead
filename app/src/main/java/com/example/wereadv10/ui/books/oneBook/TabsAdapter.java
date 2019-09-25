package com.example.wereadv10.ui.books.oneBook;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.wereadv10.ui.books.oneBook.reviews.ReviewsTab;
import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;



    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mNumOfTabs = behavior;
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                bookInfoTab book_fragment = new bookInfoTab();
                return book_fragment;
            case 1:
                ReviewsTab reviewsTab = new ReviewsTab();
                return reviewsTab;
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
