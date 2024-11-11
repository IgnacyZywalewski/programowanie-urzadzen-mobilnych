package com.example.lista_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lista_2.databinding.FragmentBBinding;


public class FragmentB extends Fragment {

    private FragmentBBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBBinding.inflate(inflater);

        binding.registerButton.setOnClickListener(view -> {
            NavDirections action = FragmentBDirections.actionFragmentBToFragmentC();
            Navigation.findNavController(requireView()).navigate(action);
        });

        return binding.getRoot();
    }
}