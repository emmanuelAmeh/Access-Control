package com.example.android.accesscontrol;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VisitorAdapter extends ListAdapter<Visitor, VisitorAdapter.VisitorHolder> {
    private static final DiffUtil.ItemCallback<Visitor> DIFF_CALLBACK = new DiffUtil.ItemCallback<Visitor>() {
        @Override
        public boolean areItemsTheSame(@NonNull Visitor oldItem, @NonNull Visitor newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Visitor oldItem, @NonNull Visitor newItem) {
            return oldItem.getFirstname().equals(newItem.getFirstname()) &&
                    oldItem.getSurname().equals(newItem.getSurname()) &&
                    oldItem.getAccessCode() == (newItem.getAccessCode()) &&
                    oldItem.getTimeGenerated() == (newItem.getTimeGenerated()) &&
                    oldItem.getTimeLoggedIn() == (newItem.getTimeLoggedIn()) &&
                    oldItem.getTimeLoggedOut() == (newItem.getTimeLoggedOut());
        }
    };
    private OnItemClickListener listener;

    protected VisitorAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public VisitorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor_layout, parent, false);
        return new VisitorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorHolder holder, int position) {
        Visitor currentVisitor = getItem(position);
        holder.visitorName.setText(currentVisitor.getFirstname() + " " + currentVisitor.getSurname());

        //make date from long
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentVisitor.getTimeGenerated());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss  MMM dd, YYYY");

        holder.timeGenerated.setText(dateFormat.format(calendar.getTime()));
        holder.accessCode.setText(String.valueOf(currentVisitor.getAccessCode()));

        //Check status
        if (currentVisitor.getTimeLoggedOut() > 100) {
            Calendar statusCal = Calendar.getInstance();
            statusCal.setTimeInMillis(currentVisitor.getTimeLoggedOut());
            holder.status.setText("Logged Out at " + dateFormat.format(statusCal.getTime()));
        } else if (currentVisitor.getTimeLoggedIn() > 100) {
            Calendar statusCal = Calendar.getInstance();
            statusCal.setTimeInMillis(currentVisitor.getTimeLoggedIn());
            holder.status.setText("Logged in at " + dateFormat.format(statusCal.getTime()));
        } else {
            holder.status.setText("Not yet Logged in.");
        }
    }

    public Visitor getVisitorAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    public interface OnItemClickListener {
        void onItemClick(Visitor visitor);
    }

    class VisitorHolder extends RecyclerView.ViewHolder {
        private TextView visitorName;
        private TextView timeGenerated;
        private TextView accessCode;
        private TextView status;
        private FloatingActionButton fabEditVisitor;

        public VisitorHolder(@NonNull View itemView) {
            super(itemView);
            visitorName = itemView.findViewById(R.id.visitor_name);
            timeGenerated = itemView.findViewById(R.id.time_generated);
            accessCode = itemView.findViewById(R.id.code);
            status = itemView.findViewById(R.id.status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
}
