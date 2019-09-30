package com.example.wereadv10.ui.clubs;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wereadv10.ExploreClubsAdapter;
import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.clubPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewClubs extends AppCompatActivity {

    private static final String TAG = "ViewClubs";

    private RecyclerView rvClubs;

    private RecyclerView.Adapter Clubs_adapter;

    private RecyclerView.LayoutManager Clubs_LayoutManager;

    private CardView clubCard;

    private List<Club> Clubs = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clubs);
        setTitle("Clubs");

        SearchView searchView = findViewById(R.id.search_club);

        rvClubs = (RecyclerView) findViewById(R.id.viewClubsRec);

        Clubs_LayoutManager = new LinearLayoutManager(this);
        rvClubs.setLayoutManager ( Clubs_LayoutManager );

        Clubs_adapter = new ClubsAdapter(this, Clubs);

        rvClubs.setAdapter(Clubs_adapter);

        dbSetUp = new dbSetUp();

        getClubs();



    }


    private List<Club> getClubs(){

        CollectionReference clubRef = dbSetUp.db.collection("clubs");
        clubRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Club club = new Club();

                                String club_name = document.get("club_name").toString();
                                String club_owner = document.get("club_owner").toString();
                                String club_description = document.get("club_description").toString();
                                String club_image = document.get("club_image").toString();

                                dbSetUp.storageRef.child("clubs_images/"+club_image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        club.setClub_image(uri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });

                                club.setClub_name(club_name);
                                club.setClub_owner(club_owner);
                                club.setClub_description(club_description);

                                Clubs.add(club);
                                Clubs_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        return Clubs;
    }


}
