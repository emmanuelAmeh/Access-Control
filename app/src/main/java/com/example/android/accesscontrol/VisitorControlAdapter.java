package com.example.android.accesscontrol;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VisitorControlAdapter extends RecyclerView.Adapter<VisitorControlAdapter.VisitorHolder> {
    private List<Visitor> visitors = new ArrayList<>();
    private VisitorControlAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public VisitorControlAdapter.VisitorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor_control_layout, parent, false);
        return new VisitorControlAdapter.VisitorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorControlAdapter.VisitorHolder holder, int position) {
        Visitor currentVisitor = visitors.get(position);
        holder.visitorName.setText(currentVisitor.getFirstname() + " " + currentVisitor.getSurname());

        //make date from long
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss  MMM dd, YYYY");

        //Check status
        if (currentVisitor.getTimeLoggedOut() > 100) {
            Calendar statusCal = Calendar.getInstance();
            statusCal.setTimeInMillis(currentVisitor.getTimeLoggedOut());
            Log.d("Adapter", statusCal.toString());
            holder.status.setText("Logged Out at " + dateFormat.format(statusCal.getTime()));
        } else if (currentVisitor.getTimeLoggedIn() > 100) {
            Calendar statusCal = Calendar.getInstance();
            statusCal.setTimeInMillis(currentVisitor.getTimeLoggedIn());
            Log.d("Adapter", statusCal.toString());
            //Toast.makeText(holder.itemView.getContext(), statusCal.toString(), Toast.LENGTH_SHORT).show();
            holder.status.setText("Logged in at " + dateFormat.format(statusCal.getTime()));
        } else {
            holder.status.setText("Not yet Logged in.");
        }
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    public Visitor getVisitorAt(int position) {
        return visitors.get(position);
    }

    public void setOnItemClickListener(VisitorControlAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Visitor visitor);
    }

    class VisitorHolder extends RecyclerView.ViewHolder {
        private TextView visitorName;
        private TextView status;
        private FloatingActionButton fabEditVisitor;

        public VisitorHolder(@NonNull View itemView) {
            super(itemView);
            visitorName = itemView.findViewById(R.id.visitor_name);
            status = itemView.findViewById(R.id.status);

            /*fabEditVisitor = itemView.findViewById(R.id.fab_edit_visitor);
            fabEditVisitor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(visitors.get(position));
                    }
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(visitors.get(position));
                    }
                }
            });
        }
    }
}
