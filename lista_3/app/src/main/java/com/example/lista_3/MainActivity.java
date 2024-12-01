package com.example.lista_3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.lista_3.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private List<ExerciseList> exerciseLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
        }

        BottomNavigationView bottomNavigationView = binding.bottomNavView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        setContentView(binding.getRoot());
    }

    public List<ExerciseList> getExerciseLists() {
        if (exerciseLists == null) {
            exerciseLists = generateExerciseLists(20);
        }
        return exerciseLists;
    }

    public List<ExerciseList> generateExerciseLists(int count) {
        Random random = new Random();
        String[] subjects = {"Matematyka", "Pum", "Fizyka", "Elektronika", "Algorytmy"};
        String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin ac urna vitae orci.";

        Map<String, Integer> subjectListCounters = new HashMap<>();
        for (String subject : subjects) {
            subjectListCounters.put(subject, 0);
        }

        List<ExerciseList> lists = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String subject = new String(subjects[random.nextInt(subjects.length)]);

            int listNumber = subjectListCounters.get(subject) + 1;
            subjectListCounters.put(subject, listNumber);

            int numOfExercises = random.nextInt(10) + 1;

            List<Exercise> exercises = new ArrayList<>();

            for (int j = 0; j < numOfExercises; j++) {
                String content = loremIpsum.substring(0, random.nextInt(loremIpsum.length() - 10) + 10);
                int points = random.nextInt(10) + 1;
                exercises.add(new Exercise(content, points));
            }
            double grade = 3.0 + (random.nextInt(5) * 0.5);
            lists.add(new ExerciseList(exercises, subject, grade, listNumber));
        }
        return lists;
    }
}

