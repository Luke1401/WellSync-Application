package com.well_sync.presentation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.well_sync.objects.*;
import com.well_sync.R;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    private final List<Patient> patientList;
    protected static OnItemClickListener onItemClickListener;

    public PatientAdapter(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient patient = patientList.get(position);

        holder.nameTextView.setText(patient.getName());
        holder.emailTextView.setText(patient.getEmail());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.patient_name);
            emailTextView = itemView.findViewById(R.id.patient_email);

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
