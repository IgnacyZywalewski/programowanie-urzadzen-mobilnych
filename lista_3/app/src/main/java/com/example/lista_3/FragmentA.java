package com.example.lista_3;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lista_3.databinding.FragmentABinding;

import java.io.Serializable;
import java.util.List;

public class FragmentA extends Fragment {

    private RecyclerView recyclerView;
    private ExerciseListAdapter adapter;
    private FragmentABinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentABinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerViewA;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ExerciseList> exerciseLists = ((MainActivity) requireActivity()).getExerciseLists();

        adapter = new ExerciseListAdapter(exerciseLists, exerciseList -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectedExerciseList", exerciseList);

            NavDirections action = FragmentADirections.actionFragmentAToFragmentC();
            Navigation.findNavController(requireView()).navigate(action.getActionId(), bundle);
        });
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
