package com.example.wereadv10.ui.clubs.oneClub.events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.ui.clubs.oneClub.createEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventPage extends AppCompatActivity implements View.OnClickListener{

    private TextView event_name;
    private TextView event_date;
    private TextView event_desc;
    private TextView event_location;
    private TextView event_time;
    private Button joinEventBtn, editEventBtn;
    private String clubOwnerID, userID, clubID, event_id;
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
        joinEventBtn.setVisibility(View.GONE);
        joinEventBtn.setOnClickListener(this);
        editEventBtn = findViewById(R.id.edit_event_btn);
        editEventBtn.setVisibility(View.GONE);
        editEventBtn.setOnClickListener(this);
        getUserID();
        getExtras();
        memberView();
        attendeeView(); // display leave button when member already joined the event



    }

    private void memberView() {

        CollectionReference MemberRef = dbSetUp.db.collection("club_members");
        MemberRef.whereEqualTo("club_id", clubID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String member_id = document.get("member_id").toString();
                                if (member_id.equalsIgnoreCase(userID)) {
                                    joinEventBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
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
                if (joinEventBtn.getText().toString().equalsIgnoreCase("Join event")){
                    joinEvent();
                    joinEventBtn.setText("Leave event");
                }else {
                    leaveEvent();
                }
                break;
            case R.id.edit_event_btn:
                editFunc();
                // edit
                break;
            default:
                break;
        }//end switch
    }//end onclick

    private void editFunc() {
        Intent i = new Intent(this, EditEventActivity.class);
        i.putExtra("EventID", event_id);
        i.putExtra("Event_name", event_name.getText());
        i.putExtra("Event_location", event_location.getText());
        i.putExtra("Event_desc", event_desc.getText());
        i.putExtra("Event_time", event_time.getText());
        i.putExtra("Event_date", event_date.getText());
        i.putExtra("CLUB_ID", clubID);
        startActivity(i);
        finish();
    }


    private void joinEvent(){

        final Map<String, Object> joinMember = new HashMap<>();
        joinMember.put("member_id", userID);
        joinMember.put("event_id", event_id);
        dbSetUp.db.collection("event_attendees")
                .document(getRandom())
                .set(joinMember).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
/*                Members.clear();
                getMembers();*/
                Toast.makeText(getApplicationContext(),"See you there!",Toast.LENGTH_SHORT).show();
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

    private void leaveEvent(){
        dbSetUp.db.collection("event_attendees")
                .whereEqualTo("member_id", userID)
                .whereEqualTo("event_id", event_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docId = document.getId();
                                deleteDoc(docId);
                            }

                        }

                    }
                });
    }

    private void deleteDoc(String id) {
        dbSetUp.db.collection("event_attendees").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        joinEventBtn.setText("JOIN EVENT");

                        Toast.makeText(getApplicationContext(),"You left the event",Toast.LENGTH_SHORT).show();
/*                        Members.clear();
                        getMembers();*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getRandom(){

        return UUID.randomUUID().toString();
    }

    private void attendeeView(){

        CollectionReference MemberRef = dbSetUp.db.collection("event_attendees");
        MemberRef.whereEqualTo("event_id", event_id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String member_id = document.get("member_id").toString();
                                if (member_id.equalsIgnoreCase(userID)) {
                                    joinEventBtn.setText("Leave event");
                                }
                            }
                        }
                    }
                });
    }

    private void ownerView() {

        if ( clubOwnerID.equals(userID) ){
            joinEventBtn.setVisibility(View.GONE);
            editEventBtn.setVisibility(View.VISIBLE);
        }else{
            editEventBtn.setVisibility(View.GONE);

        }

    }

    private void getExtras(){

            Intent intent = getIntent();
            if (intent.getExtras() != null) {

                if (intent.getExtras().getString("Event_id") != null)
                    event_id = intent.getExtras().getString("Event_id");
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
                if (intent.getExtras().getString("ClubID") != null) {
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
