package com.example.wereadv10.ui.clubs.oneClub;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class clubTabsAdapter extends FragmentStatePagerAdapter{
    int cNumOfTabs;



    private final List<Fragment> cFragmentList = new ArrayList<>();
    private final List<String> cFragmentTitleList = new ArrayList<>();

    public clubTabsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.cNumOfTabs = behavior;
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                clubEventTab event_fragment = new clubEventTab();
                return event_fragment;
            case 1:
                clubVotingTab votingTab = new clubVotingTab();
                return votingTab;
            default:
                return null;
        } //switch
    }//getItem

    @Override
    public int getCount() {
        return cNumOfTabs;
    }//getCount


    public void addFragment(Fragment fragment, String tiltle) {
        cFragmentList.add(fragment);
        cFragmentTitleList.add(tiltle);
    }//addFragment


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return cFragmentTitleList.get(position);
    }
}
