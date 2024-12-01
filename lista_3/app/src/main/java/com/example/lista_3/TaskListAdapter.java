package com.example.lista_3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {

    private final List<Exercise> exercises;

    public TaskListAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.taskNumTextView.setText("Zadanie " + (position + 1));
        holder.contentTextView.setText(exercise.content);
        holder.pointsTextView.setText("pkt: " + exercise.points);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        TextView taskNumTextView;
        TextView contentTextView;
        TextView pointsTextView;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNumTextView = itemView.findViewById(R.id.taskNumTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            pointsTextView = itemView.findViewById(R.id.pointsTextView);
        }
    }
}
