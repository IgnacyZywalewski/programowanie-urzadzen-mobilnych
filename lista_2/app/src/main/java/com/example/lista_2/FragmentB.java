package com.example.lista_2;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lista_2.databinding.FragmentBBinding;

public class FragmentB extends Fragment {

    private FragmentBBinding binding;
    private final UserDatabase userDatabase = new UserDatabase();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBBinding.inflate(inflater);
        binding.registerButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFields();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        binding.registerLogin.addTextChangedListener(textWatcher);
        binding.registerPassword.addTextChangedListener(textWatcher);
        binding.registerPaswordRepeate.addTextChangedListener(textWatcher);

        binding.registerButton.setOnClickListener(view -> {
            String login = binding.registerLogin.getText().toString();
            String password = binding.registerPassword.getText().toString();
            String repeatedPassword = binding.registerPaswordRepeate.getText().toString();

            if (password.equals(repeatedPassword)) {
                User newUser = new User(login, password);

                if (userDatabase.addUser(newUser)) {
                    NavDirections action = FragmentBDirections.actionFragmentBToFragmentC();
                    Navigation.findNavController(view).navigate(action);
                } else {
                    binding.registerLogin.setError("Użytkownik z takim loginem już istnieje");
                }
            } else {
                binding.registerPaswordRepeate.setError("Hasła się nie zgadzają");
            }
        });

        return binding.getRoot();
    }

    private void validateFields() {
        String login = binding.registerLogin.getText().toString();
        String password = binding.registerPassword.getText().toString();
        String repeatedPassword = binding.registerPaswordRepeate.getText().toString();

        boolean allFieldsFilled = !login.isEmpty() && !password.isEmpty() && !repeatedPassword.isEmpty();
        binding.registerButton.setEnabled(allFieldsFilled);
    }
}
