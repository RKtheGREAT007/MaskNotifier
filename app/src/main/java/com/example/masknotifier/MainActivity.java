package com.example.masknotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton nextButton, prevButton;
    private Button setGeoFenceButton;
    private FloatingActionButton fab;
    private TextView titleTextView, descriptionTextView;
    private int currentPage = 0;
    private int[] title = new int[3];
    private int[] description = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.previous_button);
        setGeoFenceButton = findViewById(R.id.set_geo_fence_button);
        fab = findViewById(R.id.fab_next);
        titleTextView = findViewById(R.id.title_textView);
        descriptionTextView = findViewById(R.id.description_textView);

        title[0] = R.string.title_1;
        title[1] = R.string.title_2;
        title[2] = R.string.title_3;
        description[0] = R.string.description_1;
        description[1] = R.string.description_2;
        description[2] = R.string.description_3;
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        setGeoFenceButton.setOnClickListener(this);
        fab.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.next_button:
            case R.id.fab_next:
                titleTextView.setText(title[currentPage+1]);
                descriptionTextView.setText(description[currentPage+1]);
                currentPage++;
                if(currentPage==2){
                    nextButton.setVisibility(GONE);
                    fab.setVisibility(GONE);
                    setGeoFenceButton.setVisibility(View.VISIBLE);
                }
                else if(currentPage==1){
                    prevButton.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.previous_button:
                titleTextView.setText(title[currentPage-1]);
                descriptionTextView.setText(description[currentPage-1]);
                currentPage--;
                if(currentPage==1){
                    nextButton.setVisibility(View.VISIBLE);
                    setGeoFenceButton.setVisibility(GONE);
                    fab.setVisibility(View.VISIBLE);
                }
                else if(currentPage==0){
                    prevButton.setVisibility(GONE);
                }
                break;
            case R.id.set_geo_fence_button:
                //todo: goto next activity
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
    }

}