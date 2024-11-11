package com.example.lista_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lista_2.databinding.FragmentDBinding;

public class FragmentD extends Fragment {

    private FragmentDBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDBinding.inflate(inflater);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            binding.username.setText("Witaj\n" + username);
        }

        binding.logoutButton.setOnClickListener(view -> {
            NavDirections action = FragmentDDirections.actionFragmentDToFragmentA();
            Navigation.findNavController(requireView()).navigate(action);
        });

        return binding.getRoot();
    }
}