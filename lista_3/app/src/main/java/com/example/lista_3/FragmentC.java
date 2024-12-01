package com.example.lista_3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lista_3.databinding.FragmentCBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class FragmentC extends Fragment {

    private FragmentCBinding binding;
    private RecyclerView recyclerView;
    private TaskListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerViewC;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ExerciseList selectedExerciseList = (ExerciseList) getArguments().getSerializable("selectedExerciseList");

        binding.textViewC.setText(selectedExerciseList.subject + "\n Lista " + selectedExerciseList.listNumber);
        adapter = new TaskListAdapter(selectedExerciseList.exercises);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
