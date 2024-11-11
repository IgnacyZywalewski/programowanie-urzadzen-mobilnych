package com.example.lista_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lista_2.databinding.FragmentCBinding;

public class FragmentC extends Fragment {

    public FragmentC() {
    }

    private FragmentCBinding binding;
    private final UserDatabase userDatabase = new UserDatabase();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCBinding.inflate(inflater);

        binding.loginButton.setOnClickListener(view -> {
            String login = binding.login.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if (userDatabase.validateUser(login, password)) {
                Bundle bundle = new Bundle();
                bundle.putString("username", login);

                NavDirections action = FragmentCDirections.actionFragmentCToFragmentD();
                action.getArguments().putAll(bundle);
                Navigation.findNavController(view).navigate(action);
            } else {
                Toast.makeText(requireContext(), "Błędny login lub hasło", Toast.LENGTH_SHORT).show();
            }
        });

        binding.registrationButton.setOnClickListener(view -> {
            NavDirections action = FragmentCDirections.actionFragmentCToFragmentB();
            Navigation.findNavController(requireView()).navigate(action);
        });

        return binding.getRoot();
    }
}