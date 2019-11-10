package com.example.wereadv10.ui.clubs.oneClub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wereadv10.MySharedPreference;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.EditClubInfoActivity;
import com.example.wereadv10.ui.clubs.oneClub.events.clubEventTab;
import com.example.wereadv10.ui.clubs.oneClub.votes.clubVotingTab;
import com.example.wereadv10.ui.profile.profileTab.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class clubPage extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "clubPage";

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    public String clubID;
    public TextView clubName, membersNum;
    public ImageView clubImage;
    public TextView clubOwner;
    public String clubOwnerID;
    public TextView clubDescription;
    private Button joinBtn;
    private String userID;
    private String clubNameString, clubDesc, clubImg;
    private String userEmail;
    private ImageView Share, settingImg;
    private boolean isFABOpen;
    FloatingActionButton addButton, addEvent_button, addVote_button;
    TextView TV_addEvent, TV_addVote;
    private Animation fab_clock, fab_anticlock;

    // Members recycler view
    private RecyclerView rvMembers;
    private MembersAdapter Members_adapter;
    private RecyclerView.LayoutManager Members_LayoutManager;
    private List<User> Members = new ArrayList<>();

    // Events and Votes
    private ViewPager BodyViewPager;
    private clubEventTab eventFragment = new clubEventTab();
    ;
    private clubVotingTab votingTab = new clubVotingTab();
    private clubTabsAdapter clubTabsAdapter;
    private int[] sampleImages = new int[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        dbSetUp = new dbSetUp();

        clubName = findViewById(R.id.club_name);
        clubImage = findViewById(R.id.club_image);
        clubOwner = findViewById(R.id.club_owner);
        clubDescription = findViewById(R.id.club_description);
        joinBtn = findViewById(R.id.join_button);
        joinBtn.setOnClickListener(this);
        membersNum = findViewById(R.id.membersNum);
        Share = findViewById(R.id.shareIcon);
        Share.setOnClickListener(this);
        settingImg = findViewById(R.id.club_settingImg);
        settingImg.setOnClickListener(this);
        addButton = findViewById(R.id.AddButton);
        addEvent_button = findViewById(R.id.addEvent_button);
        addVote_button = findViewById(R.id.addVote_button);
        TV_addEvent = findViewById(R.id.TV_addEvent);
        TV_addVote = findViewById(R.id.TV_addVote);
        isFABOpen = false;
        addButton.setOnClickListener(this);
        addEvent_button.setOnClickListener(this);
        addVote_button.setOnClickListener(this);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);


        getExtras();
        getUserID();

        /*Hide join/leave button to club owner
         *Display (add event) and (add vote) buttons to club owner*/
        ownerView();


        // Members recycler view
        sampleImages[0] = R.drawable.man;
        sampleImages[1] = R.drawable.girl;
        rvMembers = findViewById(R.id.rvMembers);

        Members_LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMembers.setLayoutManager(Members_LayoutManager);

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

        initCollapsingToolbar();
        initToolBar();
    }

    private void ownerView() {

        if (clubOwnerID.equals(userID)) {
            joinBtn.setVisibility(View.GONE);

            addButton.show();
            addEvent_button.show();
            addVote_button.show();

        }
        if (!clubOwnerID.equals(userID)) {
            settingImg.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_button:
                if (joinBtn.getText().toString().equalsIgnoreCase("Join club")) {
                    joinClub();
                    joinBtn.setText("Leave club");
                } else {
                    leaveClub();
                }
                break;

            case R.id.shareIcon:
                initdialog();
                break;

            case R.id.AddButton:
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;

            case R.id.addEvent_button:
                Intent i = new Intent(this, createEvent.class);
                i.putExtra("CLUB_ID", getIntent().getExtras().getString("CLUB_ID"));
                startActivity(i);
                break;

            case R.id.addVote_button:
                Intent intent = new Intent(this, createVote.class);
                intent.putExtra("CLUB_ID", getIntent().getExtras().getString("CLUB_ID"));
                startActivity(intent);
                break;
            case R.id.club_settingImg:
                Intent intentEdit = new Intent(this, EditClubInfoActivity.class);
                //clubNameString, clubDesc,clubImg
                intentEdit.putExtra("CLUB_ID", getIntent().getExtras().getString("CLUB_ID"));
                intentEdit.putExtra("NAME", clubNameString);
                intentEdit.putExtra("OWNER", getIntent().getExtras().getString("OWNER"));
                intentEdit.putExtra("OWNER_ID", getIntent().getExtras().getString("OWNER_ID"));
                intentEdit.putExtra("DESCRIPTION", clubDesc);
                intentEdit.putExtra("IMAGE", clubImg);

                startActivity(intentEdit);
                break;
            default:
                break;
        }//end switch
    }//end onClick


    private void showFABMenu() {
        isFABOpen = true;
        addButton.startAnimation(fab_clock);
        addEvent_button.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        addVote_button.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        TV_addEvent.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        TV_addVote.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        TV_addEvent.setVisibility(View.VISIBLE);
        TV_addVote.setVisibility(View.VISIBLE);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        addButton.startAnimation(fab_anticlock);
        TV_addEvent.setVisibility(View.INVISIBLE);
        TV_addVote.setVisibility(View.INVISIBLE);
        addEvent_button.animate().translationY(0);
        addVote_button.animate().translationY(0);
        TV_addEvent.animate().translationY(0);
        TV_addVote.animate().translationY(0);
    }

    private void initdialog() {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(clubPage.this).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText receiverEmailEditText = dialogView.findViewById(R.id.inviteEmail);
        final EditText messageEditText = dialogView.findViewById(R.id.inviteMssg);
        messageEditText.setText("Hey there!\nJoin us at " + getIntent().getExtras().getString("NAME") + " after downloading WeRead App..\nLooking forward to see you there <3");
        Button button1 = dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "Join Club  WeRead application" + "&body=" + (messageEditText.getText().toString()) + "&to=" + receiverEmailEditText.getText().toString());
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
                finish();
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


    }

    private void leaveClub() {
        dbSetUp.db.collection("club_members")
                .whereEqualTo("member_id", userID)
                .whereEqualTo("club_id", clubID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docId = document.getId();
                                deleteDoc(docId);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void deleteDoc(String id) {
        dbSetUp.db.collection("club_members").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        joinBtn.setText("JOIN CLUB");

                        Toast.makeText(getApplicationContext(), "You left the club now :( ", Toast.LENGTH_SHORT).show();
                        Members.clear();
                        getMembers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(clubPage.this, "please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void joinClub() {

        final Map<String, Object> joinMember = new HashMap<>();
        joinMember.put("member_id", userID);
        joinMember.put("club_id", clubID);
        dbSetUp.db.collection("club_members")
                .document(getRandom())
                .set(joinMember).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Members.clear();
                getMembers();
                Toast.makeText(getApplicationContext(), "Welcome with us in " + clubName.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                        Toast.makeText(getApplicationContext(), "You Cannot writing This Empty!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private String getRandom() {

        return UUID.randomUUID().toString();
    }


    private void getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            userEmail = user.getEmail();
        }
    }


    private List<User> getMembers() {

        CollectionReference MemberRef = dbSetUp.db.collection("club_members");
        MemberRef.whereEqualTo("club_id", clubID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int numOfMember = 0;
                            Members_adapter = new MembersAdapter(clubPage.this, Members);
                            rvMembers.setAdapter(Members_adapter);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final User member = new User();
                                numOfMember++;
                                String member_id = document.get("member_id").toString();
                                if (member_id.equalsIgnoreCase(userID)) {
                                    joinBtn.setText("Leave Club");
                                }
                                member.setId(member_id);

                                int random;
                                if (Math.random() < 0.5)
                                    random = 0;
                                else random = 1;

                                int member_image = sampleImages[random];
                                member.setImage(member_image);

                                // Get members info from users collection
                                DocumentReference userRef = dbSetUp.db.collection("users").document(member_id);
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            DocumentSnapshot doc = task.getResult();
                                            member.setName(doc.get("name").toString());
                                            member.setEmail(doc.get("email").toString());
                                            Members_adapter.notifyDataSetChanged();

                                        }
                                    }
                                });

                                Members.add(member);

                                Members_adapter.notifyDataSetChanged();
                                membersNum.setText("Members (" + numOfMember + ")");

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
            if (intent.getExtras().getString("NAME") != null) {
                clubNameString = intent.getExtras().getString("NAME");
                clubName.setText(clubNameString);
            }
            if (intent.getExtras().getString("OWNER") != null)
                clubOwner.setText(intent.getExtras().getString("OWNER"));
            if (intent.getExtras().getString("OWNER_ID") != null)
                clubOwnerID = intent.getExtras().getString("OWNER_ID");
            if (intent.getExtras().getString("DESCRIPTION") != null) {
                clubDesc = intent.getExtras().getString("DESCRIPTION");
                clubDescription.setText(clubDesc);
            }
            if (intent.getExtras().getString("IMAGE") != null) {
                clubImg = intent.getExtras().getString("IMAGE");
                Glide.with(clubPage.this).load(clubImg).into(clubImage);

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initToolBar() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            setTitle(clubNameString);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //get information from MySharedPreference after edit information
        if (!MySharedPreference.getString(clubPage.this,"clubName","").equals("")) {
            clubNameString= MySharedPreference.getString(clubPage.this, "clubName", "");
            clubName.setText(clubNameString);
            MySharedPreference.clearValue(clubPage.this,"clubName");
        }
        if (!MySharedPreference.getString(clubPage.this,"clubDescription","").equals("")) {
            clubDesc= MySharedPreference.getString(clubPage.this, "clubDescription", "");
            clubDescription.setText(clubDesc);
            MySharedPreference.clearValue(clubPage.this,"clubDescription");
        }
        if (!MySharedPreference.getString(clubPage.this,"clubImg","").equals("")) {
            clubImg= MySharedPreference.getString(clubPage.this, "clubImg", "");
            Glide.with(clubPage.this).load(clubImg).into(clubImage);
            MySharedPreference.clearValue(clubPage.this,"clubImg");
        }

    }//end onResume()
}//end class
