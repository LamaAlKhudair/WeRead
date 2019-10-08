package com.example.wereadv10.ui.clubs.oneClub;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.wereadv10.ui.clubs.oneClub.events.clubEventTab;
import com.example.wereadv10.ui.clubs.oneClub.votes.clubVotingTab;

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
                clubEventTab eventTab = new clubEventTab();
                return eventTab;
            case 1:
                clubVotingTab votingTab = new clubVotingTab();
                return votingTab;
            default:
                return null;
        } //switch
    }

    @Override
    public int getCount() {
        return cNumOfTabs;
    }//getCount


    public void addFragment(Fragment fragment, String tiltle) {
        cFragmentList.add(fragment);
        cFragmentTitleList.add(tiltle);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return cFragmentTitleList.get(position);
    }
}
