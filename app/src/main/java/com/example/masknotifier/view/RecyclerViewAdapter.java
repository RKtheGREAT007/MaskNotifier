package com.example.masknotifier.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masknotifier.R;
import com.example.masknotifier.model.HistoryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<HistoryData> historyDataList;

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
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final HistoryData historyData = historyDataList.get(position);

        holder.reply.setText(historyData.getReply());
        holder.timeStamp.setText(historyData.getTimeStamp());

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

            //Todo: del element

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
