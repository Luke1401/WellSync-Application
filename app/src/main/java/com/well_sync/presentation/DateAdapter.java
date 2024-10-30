package com.well_sync.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.well_sync.R;

import java.util.Date;
import java.util.List;


public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private final List<Date> dateList;
    protected static PatientAdapter.OnItemClickListener onItemClickListener;

    public DateAdapter(Context context, List<Date> dateList) {
        this.dateList = dateList;
    }
    public void setOnItemClickListener(PatientAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    @NonNull
    @Override
    public DateAdapter.DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.DateViewHolder holder, int position) {
        Date date = dateList.get(position);
        holder.logDate.setText(date.toString());
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView logDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            logDate = itemView.findViewById(R.id.patient_date);

            // set click listener for the item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }
}
