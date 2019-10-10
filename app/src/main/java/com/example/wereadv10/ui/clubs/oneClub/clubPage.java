package com.example.wereadv10.ui.clubs.oneClub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.events.clubEventTab;
import com.example.wereadv10.ui.clubs.oneClub.votes.clubVotingTab;
import com.example.wereadv10.ui.profile.profileTab.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class clubPage extends AppCompatActivity {

    private static final String TAG = "Members";

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    public static String clubID;
    public TextView clubName;
    public ImageView clubImage;
    public TextView clubOwner;
    public TextView clubDescription;

    // Members recycler view
    private RecyclerView rvMembers;
    private MembersAdapter Members_adapter;
    private RecyclerView.LayoutManager Members_LayoutManager;
    private List<User> Members = new ArrayList<>();

    // Events and Votes
    private ViewPager BodyViewPager;
    private clubEventTab eventFragment = new clubEventTab();;
    private clubVotingTab votingTab = new clubVotingTab();
    private clubTabsAdapter clubTabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);
        initToolBar();
        dbSetUp = new dbSetUp();


        clubName = findViewById(R.id.club_name);
        clubImage = findViewById(R.id.club_image);
        clubOwner = findViewById(R.id.club_owner);
        clubDescription = findViewById(R.id.club_description);

        // Members recycler view
        rvMembers = findViewById(R.id.rvMembers);
        Members_LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMembers.setLayoutManager ( Members_LayoutManager );
        Members_adapter = new MembersAdapter(this, Members);
        rvMembers.setAdapter(Members_adapter);
        getMembers();

        // Events and Votes
        BodyViewPager = findViewById(R.id.clubEvent_viewPager);
        TabLayout tabLayout = findViewById(R.id.clubEvent_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        clubTabsAdapter = new clubTabsAdapter(this.getSupportFragmentManager(), tabLayout.getTabCount());
        clubTabsAdapter.addFragment(eventFragment, "Club Events");
        clubTabsAdapter.addFragment(votingTab, "Club Votes");
        BodyViewPager.setAdapter(clubTabsAdapter);
        tabLayout.setupWithViewPager(BodyViewPager);

        getExtras();
        initCollapsingToolbar();

    }

    private List<User> getMembers() {

        CollectionReference MemberRef = dbSetUp.db.collection("club_members");
        MemberRef.whereEqualTo("club_id", clubID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final User member = new User();

                                String member_id = document.get("member_id").toString();

                                // Get members info from users collection
                                DocumentReference userRef = dbSetUp.db.collection("users").document(member_id);
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            member.setName(doc.get("name").toString());

                                        }
                                    }
                                });

                                member.setId(member_id);

                                Members.add(member);

                                Members_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        return Members;

    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
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
                System.out.println("CLUD _ID "+ clubID);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initToolBar() {
        setTitle("Club");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
