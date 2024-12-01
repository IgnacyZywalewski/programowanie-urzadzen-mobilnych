package com.example.lista_3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder> {

    private final List<ExerciseList> exerciseLists;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ExerciseList exerciseList);
    }

    public ExerciseListAdapter(List<ExerciseList> exerciseLists, OnItemClickListener onItemClickListener) {
        this.exerciseLists = exerciseLists;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ExerciseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_list, parent, false);
        return new ExerciseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseListViewHolder holder, int position) {
        ExerciseList exerciseList = exerciseLists.get(position);

        holder.subjectNameTextView.setText(exerciseList.subject);
        holder.taskCountTextView.setText("Ilość zadań: " + exerciseList.exercises.size());
        holder.gradeTextView.setText("Ocena: " + exerciseList.grade);
        holder.listNumberTextView.setText("Lista " + exerciseList.listNumber);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(exerciseList));
    }

    @Override
    public int getItemCount() {
        return exerciseLists.size();
    }

    public static class ExerciseListViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView;
        TextView taskCountTextView;
        TextView gradeTextView;
        TextView listNumberTextView;

        public ExerciseListViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            taskCountTextView = itemView.findViewById(R.id.taskCountTextView);
            gradeTextView = itemView.findViewById(R.id.gradeTextView);
            listNumberTextView = itemView.findViewById(R.id.listNumberTextView);
        }
    }
}
