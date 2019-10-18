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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.events.clubEventTab;
import com.example.wereadv10.ui.clubs.oneClub.votes.clubVotingTab;
import com.example.wereadv10.ui.profile.profileTab.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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
    public TextView clubDescription;
    private Button joinBtn;
    private String userID;
    private ImageView Share;

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
    private int[] sampleImages = new int[5];


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
        joinBtn = findViewById(R.id.join_button);
        joinBtn.setOnClickListener(this);
        membersNum = findViewById(R.id.membersNum);
        Share=findViewById(R.id.shareIcon);
        Share.setOnClickListener(this);

        getExtras();

        // Members recycler view
        sampleImages[0] = R.drawable.man ;
        sampleImages[1] = R.drawable.girl ;
        rvMembers = findViewById(R.id.rvMembers);

        Members_LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMembers.setLayoutManager ( Members_LayoutManager );

        getMembers();
        getUserID();

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

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_button:
                if (joinBtn.getText().toString().equalsIgnoreCase("join club")){
                    joinClub();
                    joinBtn.setText("Leave club");
                }else {
                    leaveClub();
                }
                break;

            case R.id.shareIcon:
                initdialog();
                break;

            default:
                break;
        }//end switch
    }//end onClick





    private void initdialog() {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(clubPage.this).create();
        LayoutInflater inflater =getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.inviteEmail);
        final EditText editText2 = (EditText) dialogView.findViewById(R.id.inviteMssg);
        editText2.setText("Hey there!\nJoin us at "+getIntent().getExtras().getString("NAME")+" after downloading weRead App..\nLooking forward to see you there <3");
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.putExtra(Intent.EXTRA_EMAIL, new String[]{editText.getText().toString()});
                it.putExtra(Intent.EXTRA_TEXT,editText2.getText());
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


    }



    private void leaveClub(){
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

                        Toast.makeText(getApplicationContext(),"You left the club now :( ",Toast.LENGTH_SHORT).show();
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
    private void joinClub(){

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
                Toast.makeText(getApplicationContext(),"Welcome with us in "+clubName.getText().toString()+"!",Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error writing document", e);
                        Toast.makeText(getApplicationContext(),"You Cannot writing This Empty!",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String getRandom(){
        return UUID.randomUUID().toString();
    }

    private void getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();

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
                                if(member_id.equalsIgnoreCase(userID)){
                                    joinBtn.setText("Leave Club");
                                }
                                member.setId(member_id);

                                int random ;
                                if(Math.random() < 0.5)
                                    random = 0;
                                else random = 1;

                                int member_image = sampleImages[random] ;
                                member.setImage(member_image);

                                // Get members info from users collection
                                DocumentReference userRef = dbSetUp.db.collection("users").document(member_id);
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            member.setName(doc.get("name").toString());
                                            member.setEmail(doc.get("email").toString());
                                            Members_adapter.notifyDataSetChanged();

                                        }
                                    }
                                });

                                Members.add(member);

                                Members_adapter.notifyDataSetChanged();
                                membersNum.setText("Members("+numOfMember+")");

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
