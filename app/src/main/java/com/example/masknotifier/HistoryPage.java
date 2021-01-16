package com.example.masknotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masknotifier.model.HistoryData;
import com.example.masknotifier.model.UserDetails;
import com.example.masknotifier.view.RecyclerViewAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryPage extends AppCompatActivity {

    private static final String TAG = "History Page";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ProgressBar progressBar;
    private TextView emptyText;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private DBHelper dbHelper;

    private List<HistoryData> historyDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        progressBar = findViewById(R.id.history_progress_bar);
        emptyText = findViewById(R.id.empty_textView);
        recyclerView = findViewById(R.id.history_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        String uid = UserDetails.getUserInstance().getUid();

        Log.d(TAG, "onCreate: " + uid);

        progressBar.setVisibility(View.VISIBLE);

        List<HistoryData> arrayList = dbHelper.getHistory();

        int cnt = 0;
        for(int i = 0; i < arrayList.size(); i++) {
            if(cnt < 10) {
                historyDataList.add(arrayList.get(i));
                cnt++;
            }
            else {
                dbHelper.deleteHistory(arrayList.get(i).getTimeStamp());
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "size is: " + historyDataList.size(), Toast.LENGTH_SHORT).show();
        if(historyDataList.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(HistoryPage.this, historyDataList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}