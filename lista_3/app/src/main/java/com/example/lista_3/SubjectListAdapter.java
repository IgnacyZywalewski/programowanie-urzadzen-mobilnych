package com.example.lista_3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder> {

    private final List<ExerciseList> exerciseLists;
    private final Set<String> uniqueSubjects;

    public SubjectListAdapter(List<ExerciseList> exerciseLists) {
        this.exerciseLists = exerciseLists;

        this.uniqueSubjects = new HashSet<>();
        for (ExerciseList list : exerciseLists) {
            uniqueSubjects.add(list.subject);
        }
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_list, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        String subject = (String) uniqueSubjects.toArray()[position];
        int listCount = getListCountForSubject(subject);

        double averageGrade = getAverageGradeForSubject(subject);
        String roundedAverage = String.format("%.1f", averageGrade);

        holder.subjectNameTextView.setText(subject);
        holder.listNumberTextView.setText("Ilość list: " + listCount);
        holder.avgerageTextView.setText("Średnia: " + roundedAverage);
    }

    @Override
    public int getItemCount() {
        return uniqueSubjects.size();
    }

    private int getListCountForSubject(String subject) {
        int count = 0;
        for (ExerciseList list : exerciseLists) {
            if (list.subject.equals(subject)) {
                count++;
            }
        }
        return count;
    }

    private double getAverageGradeForSubject(String subject) {
        double totalGrade = 0;
        int count = 0;
        for (ExerciseList list : exerciseLists) {
            if (list.subject.equals(subject)) {
                totalGrade += list.grade;
                count++;
            }
        }
        return count > 0 ? totalGrade / count : 0.0;
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView;
        TextView listNumberTextView;
        TextView avgerageTextView;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            listNumberTextView = itemView.findViewById(R.id.listNumberTextView);
            avgerageTextView = itemView.findViewById(R.id.avgerageTextView);
        }
    }
}

