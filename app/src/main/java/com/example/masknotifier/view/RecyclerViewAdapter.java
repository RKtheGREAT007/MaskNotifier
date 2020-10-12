package com.example.masknotifier.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masknotifier.R;
import com.example.masknotifier.model.HistoryData;
import com.example.masknotifier.model.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<HistoryData> historyDataList;

    private static final String TAG = "RecyclerViewAdapter";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RecyclerViewAdapter(Context context, List<HistoryData> historyDataList) {
        this.context = context;
        this.historyDataList = historyDataList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, final int position) {
        final HistoryData historyData = historyDataList.get(position);

        holder.reply.setText(historyData.getReply());
        holder.timeStamp.setText(historyData.getTimeStamp());

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("userHistory")
                        .document(UserDetails.getUserInstance().getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> mp = new HashMap<>();
                                int ind = 0, cnt = 0;
                                int lst = Integer.parseInt((String) documentSnapshot.get("index"));
                                while(ind != lst){
                                    if(cnt==position){
                                        ind++;
                                        cnt++;
                                        continue;
                                    }
                                    if(documentSnapshot.contains("reply"+String.valueOf(ind))){
                                        mp.put("reply"+String.valueOf(ind),documentSnapshot.get("reply"+String.valueOf(ind)));
                                        mp.put("timeStamp"+String.valueOf(ind),documentSnapshot.get("timeStamp"+String.valueOf(ind)));
                                        cnt++;
                                    }
                                    ind++;
                                }
                                mp.put("index",String.valueOf(lst));
                                Log.d(TAG, "onSuccess: " + mp);
                                historyDataList.remove(position);
                                db.collection("userHistory")
                                        .document(UserDetails.getUserInstance().getUid())
                                        .set(mp)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Content Deleted!", Toast.LENGTH_SHORT).show();
                                                notifyItemRemoved(position);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView cancelButton;
        private TextView reply,timeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cancelButton = itemView.findViewById(R.id.row_cancel_imageView);
            reply = itemView.findViewById(R.id.row_reply_textview);
            timeStamp = itemView.findViewById(R.id.row_timeStamp_textView);
        }
    }
}
