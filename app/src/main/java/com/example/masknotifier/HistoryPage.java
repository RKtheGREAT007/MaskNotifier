package com.example.masknotifier;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.ResourceDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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

        String uid = UserDetails.getUserInstance().getUid();

        Log.d(TAG, "onCreate: " + uid);

        progressBar.setVisibility(View.VISIBLE);

        db.collection("userHistory")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int index = Integer.parseInt((String) documentSnapshot.get("index"));
                        int cnt = 0;
                        while(cnt!=10&&index>=0){
                            if(documentSnapshot.contains("reply"+String.valueOf(index))){
                                HistoryData historyData = new HistoryData();
                                historyData.setReply((String) documentSnapshot.get("reply"+String.valueOf(index)));
                                historyData.setTimeStamp((String) documentSnapshot.get("timeStamp"+String.valueOf(index)));
                                cnt += 1;
                                historyDataList.add(historyData);
                            }
                            index -= 1;
                        }
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onSuccess: size " + historyDataList.size());
                        if(historyDataList.size() == 0){
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(HistoryPage.this, historyDataList);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(HistoryPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}