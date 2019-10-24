package com.example.wereadv10.ui.clubs;


import android.os.Bundle;
import android.util.Log;

import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewClubs extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "ViewClubs";

    private RecyclerView rvClubs;

    private ClubsAdapter Clubs_adapter;

    private RecyclerView.LayoutManager Clubs_LayoutManager;

    private List<Club> Clubs = new ArrayList<>();

    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clubs);

        SearchView searchView = findViewById(R.id.search_club);
        searchView.setOnQueryTextListener(this);

        rvClubs = findViewById(R.id.viewClubsRec);

        Clubs_LayoutManager = new LinearLayoutManager(this);
        rvClubs.setLayoutManager ( Clubs_LayoutManager );

        Clubs_adapter = new ClubsAdapter(this, Clubs);

        rvClubs.setAdapter(Clubs_adapter);

        dbSetUp = new dbSetUp();
        initToolBar();
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

                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            club.setClub_owner( doc.get("name").toString() );
                                        }
                                    }
                                });

                                club.setID(club_id);
                                club.setClub_name(club_name);
                                club.setClub_description(club_description);
                                club.setClub_image(club_image);

                                Clubs.add(club);
                                Clubs_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
        return Clubs;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s){

        List<Club> newList = new ArrayList<>();
        s=s.toLowerCase();
        for(int i=0; i<Clubs.size(); i++){
            String club_name = Clubs.get(i).getClub_name().toLowerCase();
            if(club_name.contains(s)  ){
                newList.add(Clubs.get(i));
            }
        }

        Clubs_adapter.updateList(newList);
        Clubs_adapter.notifyDataSetChanged();
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp

    private void initToolBar() {
        setTitle("Clubs");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()
}
