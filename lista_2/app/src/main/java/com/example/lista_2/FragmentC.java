package com.example.lista_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lista_2.databinding.FragmentCBinding;

public class FragmentC extends Fragment {

    public FragmentC() {
    }

    private FragmentCBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCBinding.inflate(inflater);

        binding.loginButton.setOnClickListener(view -> {
            NavDirections action = FragmentCDirections.actionFragmentCToFragmentD();
            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.registrationButton.setOnClickListener(view -> {
            NavDirections action = FragmentCDirections.actionFragmentCToFragmentB();
            Navigation.findNavController(requireView()).navigate(action);
        });

        return binding.getRoot();
    }
}