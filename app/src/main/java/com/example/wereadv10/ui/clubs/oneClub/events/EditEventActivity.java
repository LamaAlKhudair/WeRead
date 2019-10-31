package com.example.wereadv10.ui.clubs.oneClub.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wereadv10.R;
import com.example.wereadv10.dbSetUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener  {
    private Button editBtn,selectTime,selectDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText DateEt,eventTimeEt,EventNameEt,EventdesEt,eventLocationEt;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    private String eventID ,am_pm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        setTitle("Edit Event");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editBtn = findViewById(R.id.edit_event_button);
        DateEt = findViewById(R.id.DateEt);
        eventLocationEt= findViewById(R.id.eventLocationEt);
        EventNameEt = findViewById(R.id.EventNameEt);
        EventdesEt= findViewById(R.id.EventdesEt);
        eventTimeEt = findViewById(R.id.eventTimeEt);
        selectDate = findViewById(R.id.btn_date);
        selectTime = findViewById(R.id.btn_time);
        getExtra();
        editBtn.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        selectDate.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.edit_event_button:
                editEvent();
                break;


            case R.id.btn_date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                DateEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;


            case R.id.btn_time:
                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Calendar datetime = Calendar.getInstance();
                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";
                                eventTimeEt.setText(hourOfDay + ":" + minute +" " + am_pm );
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();


                break;

            default: break;

        }
    }
    private void getExtra(){
        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            if (intent.getExtras().getString("Event_name") != null)
                EventNameEt.setText(intent.getExtras().getString("Event_name"));
            if (intent.getExtras().getString("Event_date") != null)
                DateEt.setText(intent.getExtras().getString("Event_date"));
            if (intent.getExtras().getString("Event_time") != null)
                eventTimeEt.setText(intent.getExtras().getString("Event_time"));
            if (intent.getExtras().getString("Event_desc") != null)
                EventdesEt.setText(intent.getExtras().getString("Event_desc"));
            if (intent.getExtras().getString("Event_location") != null)
                eventLocationEt.setText(intent.getExtras().getString("Event_location"));
            if (intent.getExtras().getString("EventID") != null ){
                eventID = intent.getExtras().getString("EventID");
            }
        }
    }
    private void editEvent(){
        if(!EventNameEt.getText().toString().equals("")&&!EventdesEt.getText().toString().equals("")&&!DateEt.getText().toString().equals("")
                &&!eventTimeEt.getText().toString().equals("")&&!eventLocationEt.getText().toString().equals(""))
        {
            final Map<String, Object> event = new HashMap<>();
            event.put("event_name", EventNameEt.getText().toString());
            event.put("event_desc", EventdesEt.getText().toString());
            event.put("event_date", DateEt.getText().toString());
            event.put("event_time", eventTimeEt.getText().toString());
            event.put("event_location", eventLocationEt.getText().toString());

            dbSetUp.db.collection("events").whereEqualTo("event_id", eventID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            dbSetUp.db
                                    .collection("events")
                                    .document(id).update(event);
                        }
                        Toast.makeText(getApplicationContext(),"Edit event information completed",Toast.LENGTH_SHORT).show();


                    } else {
                        System.out.println( "Error getting documents: ");
                    }
                }
            });

            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"You Cannot Leave This Empty!",Toast.LENGTH_SHORT).show();
        }
    }
}
