package com.example.masknotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.masknotifier.model.UserDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton nextButton, prevButton;
    private Button letsGetStartedButton;
    private TextView titleTextView, descriptionTextView;
    private int currentPage = 0;
    private int[] title = new int[3];
    private int[] description = new int[3];

    public static final String MASK_NOTIFIER_PREFERENCE = "MaskNotifierPreference";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.previous_button);
        titleTextView = findViewById(R.id.title_textView);
        letsGetStartedButton = findViewById(R.id.get_started_button);
        descriptionTextView = findViewById(R.id.description_textView);

        sharedPreferences = getSharedPreferences(MASK_NOTIFIER_PREFERENCE, MODE_PRIVATE);

        if(sharedPreferences.contains("radius")) {
            startActivity(new Intent(this, HomePage.class));
        }

        title[0] = R.string.title_1;
        title[1] = R.string.title_2;
        title[2] = R.string.title_3;
        description[0] = R.string.description_1;
        description[1] = R.string.description_2;
        description[2] = R.string.description_3;
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        letsGetStartedButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.next_button:
                titleTextView.setText(title[currentPage+1]);
                descriptionTextView.setText(description[currentPage+1]);
                currentPage++;
                if(currentPage==2){
                    nextButton.setVisibility(GONE);
                    letsGetStartedButton.setVisibility(View.VISIBLE);
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
                    letsGetStartedButton.setVisibility(GONE);
                }
                else if(currentPage==0){
                    prevButton.setVisibility(GONE);
                }
                break;
            case R.id.get_started_button:
                //todo: goto next activity
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}