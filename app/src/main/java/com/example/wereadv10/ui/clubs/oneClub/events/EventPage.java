package com.example.wereadv10.ui.clubs.oneClub.events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wereadv10.R;

public class EventPage extends AppCompatActivity implements View.OnClickListener{

    private TextView event_name;
    private TextView event_date;
    private TextView event_desc;
    private TextView event_location;
    private TextView event_time;
    private Button joinEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);


        event_name = findViewById(R.id.Title);
        event_date = findViewById(R.id.Date);
        event_desc = findViewById(R.id.EventDesc);
        event_location = findViewById(R.id.Location);
        event_time = findViewById(R.id.Time);
        joinEventBtn = findViewById(R.id.join_event_btn);
        joinEventBtn.setOnClickListener(this);

        getExtras();

    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.join_event_btn:
                //add code here
                break;
            default:
                break;
        }//end switch
    }//end onclick


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

        }
    }

}
