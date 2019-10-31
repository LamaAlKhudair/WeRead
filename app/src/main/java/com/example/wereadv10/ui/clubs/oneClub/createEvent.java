package com.example.wereadv10.ui.clubs.oneClub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.wereadv10.dbSetUp;
import com.example.wereadv10.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class createEvent extends AppCompatActivity implements View.OnClickListener {

    private Button create,selectTime,selectDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText DateEt,eventTimeEt,EventNameEt,EventdesEt,eventLocationEt;
    private com.example.wereadv10.dbSetUp dbSetUp = new dbSetUp();
    private String am_pm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_event);
        setTitle("Create Event");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        create = findViewById(R.id.create_event_button);
        DateEt = findViewById(R.id.DateEt);
        eventLocationEt= findViewById(R.id.eventLocationEt);
        EventNameEt = findViewById(R.id.EventNameEt);
        EventdesEt= findViewById(R.id.EventdesEt);
        eventTimeEt = findViewById(R.id.eventTimeEt);
        selectDate = findViewById(R.id.btn_date);
        selectTime = findViewById(R.id.btn_time);
        create.setOnClickListener(this);
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

            case R.id.create_event_button:
                addEvent();
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

    private void addEvent() {
        if(!EventNameEt.getText().toString().equals("")&&!EventdesEt.getText().toString().equals("")&&!DateEt.getText().toString().equals("")
        &&!eventTimeEt.getText().toString().equals("")&&!eventLocationEt.getText().toString().equals(""))
        {
            final Map<String, Object> event = new HashMap<>();
            event.put("event_name", EventNameEt.getText().toString());
            event.put("event_desc", EventdesEt.getText().toString());
            event.put("event_date", DateEt.getText().toString());
            event.put("event_time", eventTimeEt.getText().toString());
            event.put("event_location", eventLocationEt.getText().toString());
            event.put("club_id",getIntent().getExtras().getString("CLUB_ID"));
            event.put("event_id",getRandom());

            dbSetUp.db.collection("events").document(getRandom())
                    .set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("DocumentSnapshot successfully written!");


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error writing document", e);
                        }
                    });
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"You Cannot Leave This Empty!",Toast.LENGTH_SHORT).show();
        }

        }


    private String getRandom(){
        return UUID.randomUUID().toString();
    }


}

