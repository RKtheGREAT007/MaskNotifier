package com.example.masknotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "NotificationBroadcastReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String whichAction = intent.getAction();

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        Log.d(TAG, "onReceive: " + currentUser.getEmail());

        if ("quit_action".equals(whichAction)) {
            db.collection("userHistory")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, Object> mp = documentSnapshot.getData();
                            int index = Integer.parseInt((String) Objects.requireNonNull(mp.get("index")));
                            Map<String, Object> obj = new HashMap<>();
                            obj.put("reply" + String.valueOf(index), "yes");
                            obj.put("timeStamp" + String.valueOf(index), Calendar.getInstance().getTime().toString());
                            obj.put("index", String.valueOf(index+1));
                            db.collection("userHistory")
                                    .document(currentUser.getUid())
                                    .update(obj)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Reply noted", Toast.LENGTH_SHORT).show();
                                            NotificationManagerCompat.from(context).cancel(1000);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.getMessage());
                                        }
                                    });
                        }
                    });
        }

//        Log.d(TAG, "onReceive: " + Objects.requireNonNull(intent.getExtras()).get("id"));
//        NotificationManagerCompat.from(context).cancel((int) Objects.requireNonNull(intent.getExtras()).get("id"));

    }
}
