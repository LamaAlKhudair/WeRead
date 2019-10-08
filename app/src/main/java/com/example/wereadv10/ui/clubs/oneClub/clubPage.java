package com.example.wereadv10.ui.clubs.oneClub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.ui.clubs.oneClub.events.clubEventTab;
import com.example.wereadv10.ui.clubs.oneClub.votes.clubVotingTab;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;


public class clubPage extends AppCompatActivity {

    public static String clubID;
    public TextView clubName;
    public ImageView clubImage;
    public TextView clubOwner;
    public TextView clubDescription;

    private ViewPager viewPager;
    private clubEventTab eventFragment=new clubEventTab();;
    private clubVotingTab votingTab = new clubVotingTab();
    clubTabsAdapter ctabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        clubName = findViewById(R.id.club_name);
        clubImage = findViewById(R.id.club_image);
        clubOwner = findViewById(R.id.club_owner);
        clubDescription = findViewById(R.id.club_description);


        viewPager = findViewById(R.id.clubEvent_viewPager);
        TabLayout tabLayout = findViewById(R.id.clubEvent_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        ctabsAdapter = new clubTabsAdapter(this.getSupportFragmentManager(), tabLayout.getTabCount());
        ctabsAdapter.addFragment(eventFragment, "Club events");
        ctabsAdapter.addFragment(votingTab, "Club voting");
        viewPager.setAdapter(ctabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getExtras();
        initCollapsingToolbar();

    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            Intent intent = getIntent();

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(intent.getExtras().getString("NAME"));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }//end initCollapsingToolbar


    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

                clubID = intent.getExtras().getString("CLUB_ID");

            if (intent.getExtras().getString("NAME") != null)
                clubName.setText(intent.getExtras().getString("NAME"));
            if (intent.getExtras().getString("OWNER") != null)
                clubOwner.setText(intent.getExtras().getString("OWNER"));
            if (intent.getExtras().getString("DESCRIPTION") != null)
                clubDescription.setText(intent.getExtras().getString("DESCRIPTION"));
            if (intent.getExtras().getString("IMAGE") != null)
                Glide.with(clubPage.this).load(intent.getExtras().getString("IMAGE")).into(clubImage);

        }
    }


}
