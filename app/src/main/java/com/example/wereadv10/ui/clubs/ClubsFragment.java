package com.example.wereadv10.ui.clubs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.books.Book;
import com.example.wereadv10.ui.explore.ExploreClubsAdapter;
import com.example.wereadv10.ui.explore.ExploreFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ClubsFragment extends Fragment implements View.OnClickListener{

    private Button createClub;
    private RecyclerView rvClubs, rvMyClubs; // V Recycler View for clubs
    private ExploreClubsAdapter rvClubs_adapter, myClubs_adapter;
    private RecyclerView.LayoutManager rvClubs_mLayoutManager, rvMyClubs_mLayoutManager;

    private List<Club> myClubs = new ArrayList<>();
    private List<Club> myOwnClubs = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_clubs, container, false);
        rvClubs = root.findViewById(R.id.rvVertical);
        rvMyClubs = root.findViewById(R.id.rv2Vertical);




        createClub = root.findViewById(R.id.createClub);
        createClub.setOnClickListener(this);

        return root;

    }
    private void getMyOwnClubs(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        dbSetUp.db.collection("clubs")
                .whereEqualTo("club_owner", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            rvMyClubs_mLayoutManager = new LinearLayoutManager(ClubsFragment.this.getContext());
                            rvMyClubs.setLayoutManager ( rvMyClubs_mLayoutManager );
                            myClubs_adapter = new ExploreClubsAdapter(getParentFragment().getContext(), myOwnClubs);

                            rvMyClubs.setAdapter(myClubs_adapter);

                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                dbSetUp.db.collection("clubs")
                                        .whereEqualTo("club_id", document2.get("club_id"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        final Club club = new Club();
                                                        String club_id = document.get("club_id").toString();
                                                        String club_name = document.get("club_name").toString();
                                                        String club_owner = document.get("club_owner").toString();
                                                        String club_description = document.get("club_description").toString();
                                                        String club_image = document.get("club_image").toString();


                                                        club.setOwnerID(club_owner);

                                                        //get club owner name
                                                        DocumentReference userRef = dbSetUp.db.collection("users").document(club_owner);
                                                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot doc = task.getResult();
                                                                    club.setClub_owner(doc.get("name").toString());
                                                                }
                                                            }
                                                        });
                                                        club.setID(club_id);
                                                        club.setClub_name(club_name);
                                                        club.setClub_description(club_description);
                                                        club.setClub_image(club_image);

                                                        myOwnClubs.add(club);
                                                        myClubs_adapter.notifyDataSetChanged();
                                                    }

                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
    private void getMyClubs(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

         dbSetUp.db.collection("club_members")
                    .whereEqualTo("member_id", user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                rvClubs_mLayoutManager = new LinearLayoutManager(ClubsFragment.this.getContext());
                                rvClubs.setLayoutManager ( rvClubs_mLayoutManager );
                                rvClubs_adapter = new ExploreClubsAdapter(getParentFragment().getContext(), myClubs);

                                rvClubs.setAdapter(rvClubs_adapter);

                                for (QueryDocumentSnapshot document2 : task.getResult()) {
                                    dbSetUp.db.collection("clubs")
                                            .whereEqualTo("club_id", document2.get("club_id"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            final Club club = new Club();
                                                            String club_id = document.get("club_id").toString();
                                                            String club_name = document.get("club_name").toString();
                                                            String club_owner = document.get("club_owner").toString();
                                                            String club_description = document.get("club_description").toString();
                                                            String club_image = document.get("club_image").toString();

                                                            club.setOwnerID(club_owner);

                                                            //get club owner name
                                                            DocumentReference userRef = dbSetUp.db.collection("users").document(club_owner);
                                                            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot doc = task.getResult();
                                                                        club.setClub_owner(doc.get("name").toString());
                                                                    }
                                                                }
                                                            });
                                                            club.setID(club_id);
                                                            club.setClub_name(club_name);
                                                            club.setClub_description(club_description);
                                                            club.setClub_image(club_image);
                                                            if(userID.equalsIgnoreCase(club_owner));
                                                            else{
                                                            myClubs.add(club);
                                                            rvClubs_adapter.notifyDataSetChanged();
                                                            }
                                                        }

                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createClub:
                startActivity(new Intent(getContext(), CreateClub.class));
                break;
        }//end switch
    }

    @Override
    public void onResume() {
        super.onResume();
        myClubs.clear();
        myOwnClubs.clear();

        getMyClubs();
        getMyOwnClubs();
    }
}