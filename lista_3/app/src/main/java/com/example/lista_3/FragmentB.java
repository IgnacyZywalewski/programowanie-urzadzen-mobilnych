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


import java.util.List;

public class FragmentB extends Fragment {

    private RecyclerView recyclerView;
    private SubjectListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewB);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ExerciseList> exerciseLists = ((MainActivity) requireActivity()).getExerciseLists();

        adapter = new SubjectListAdapter(exerciseLists);
        recyclerView.setAdapter(adapter);
    }
}