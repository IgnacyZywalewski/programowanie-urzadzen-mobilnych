package com.example.lista_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.lista_2.databinding.FragmentABinding;

public class FragmentA extends Fragment {

    private FragmentABinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentABinding.inflate(inflater);

        binding.logButton.setOnClickListener(view -> {
            NavDirections action = FragmentADirections.actionFragmentAToFragmentC();
            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.registerButton.setOnClickListener(view -> {
            NavDirections action = FragmentADirections.actionFragmentAToFragmentB();
            Navigation.findNavController(requireView()).navigate(action);
        });

        return binding.getRoot();
    }
}