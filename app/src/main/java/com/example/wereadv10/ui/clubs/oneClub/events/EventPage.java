package com.example.wereadv10.ui.clubs.oneClub.events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EventPage extends AppCompatActivity implements View.OnClickListener{

    private TextView event_name;
    private TextView event_date;
    private TextView event_desc;
    private TextView event_location;
    private TextView event_time;
    private Button joinEventBtn, editEventBtn;
    private String clubOwnerID, userID, clubID;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        setTitle("Event's details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event_name = findViewById(R.id.Title);
        event_date = findViewById(R.id.Date);
        event_desc = findViewById(R.id.EventDesc);
        event_location = findViewById(R.id.Location);
        event_time = findViewById(R.id.Time);
        joinEventBtn = findViewById(R.id.join_event_btn);
        joinEventBtn.setOnClickListener(this);
        editEventBtn = findViewById(R.id.edit_event_btn);
        editEventBtn.setOnClickListener(this);
        getUserID();
        getExtras();



    }
    private void getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.join_event_btn:
                //add code here
                break;
            case R.id.edit_event_btn:
                System.out.println("EDIT CLICKED ");
                // edit
                break;
            default:
                break;
        }//end switch
    }//end onclick

    private void ownerView() {

        if ( clubOwnerID.equals(userID) ){
            joinEventBtn.setVisibility(View.GONE);
            editEventBtn.setVisibility(View.VISIBLE);
        }else{
            editEventBtn.setVisibility(View.GONE);

        }

    }
    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            if (intent.getExtras().getString("Event_name") != null)
                event_name.setText(intent.getExtras().getString("Event_name"));
            if (intent.getExtras().getString("Event_date") != null)
                event_date.setText(intent.getExtras().getString("Event_date"));
            if (intent.getExtras().getString("Event_time") != null)
                event_time.setText(intent.getExtras().getString("Event_time"));
            if (intent.getExtras().getString("Event_desc") != null)
                event_desc.setText(intent.getExtras().getString("Event_desc"));
            if (intent.getExtras().getString("Event_location") != null)
                event_location.setText(intent.getExtras().getString("Event_location"));
            if (intent.getExtras().getString("ClubID") != null ){
                clubID = intent.getExtras().getString("ClubID");
                getOwnerID();
            }
        }
    }

    private void getOwnerID(){
        dbSetUp.db.collection("clubs").whereEqualTo("club_id",clubID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                clubOwnerID = document.get("club_owner").toString();
                            }

                        } else Log.w( "Error getting documents.", task.getException());
                        ownerView();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
