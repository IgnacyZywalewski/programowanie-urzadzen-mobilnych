package com.example.quizapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private TextView questionText, questionCounter, result;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private ProgressBar progressBar;

    private final String[] questions = {
            "Pytanie 1", "Pytanie 2", "Pytanie 3", "Pytanie 4", "Pytanie 5",
            "Pytanie 6", "Pytanie 7", "Pytanie 8", "Pytanie 9", "Pytanie 10"
    };

    private final String[][] answers = {
            {"Odpowiedź 1a", "Odpowiedź 1b", "Odpowiedź 1c", "Odpowiedź 1d"},
            {"Odpowiedź 2a", "Odpowiedź 2b", "Odpowiedź 2c", "Odpowiedź 2d"},
            {"Odpowiedź 3a", "Odpowiedź 3b", "Odpowiedź 3c", "Odpowiedź 3d"},
            {"Odpowiedź 4a", "Odpowiedź 4b", "Odpowiedź 4c", "Odpowiedź 4d"},
            {"Odpowiedź 5a", "Odpowiedź 5b", "Odpowiedź 5c", "Odpowiedź 5d"},
            {"Odpowiedź 6a", "Odpowiedź 6b", "Odpowiedź 6c", "Odpowiedź 6d"},
            {"Odpowiedź 7a", "Odpowiedź 7b", "Odpowiedź 7c", "Odpowiedź 7d"},
            {"Odpowiedź 8a", "Odpowiedź 8b", "Odpowiedź 8c", "Odpowiedź 8d"},
            {"Odpowiedź 9a", "Odpowiedź 9b", "Odpowiedź 9c", "Odpowiedź 9d"},
            {"Odpowiedź 10a", "Odpowiedź 10b", "Odpowiedź 10c", "Odpowiedź 10d"}
    };

    private final int[] correctAnswers = {0, 1, 2, 3, 0, 1, 2, 3, 0, 1};
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.question);
        questionCounter = findViewById(R.id.question_counter);
        result = findViewById(R.id.result);
        radioGroup = findViewById(R.id.radio_group);
        option1 = findViewById(R.id.button_1);
        option2 = findViewById(R.id.button_2);
        option3 = findViewById(R.id.button_3);
        option4 = findViewById(R.id.button_4);
        nextButton = findViewById(R.id.button_next);
        progressBar = findViewById(R.id.progress_bar);

        displayQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOptionId = radioGroup.getCheckedRadioButtonId();
                if (selectedOptionId == -1) {
                    return;
                }

                int selectedAnswerIndex = radioGroup.indexOfChild(findViewById(selectedOptionId));

                if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
                    score++;
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    displayQuestion();
                }
                else {
                    showResult();
                }
            }
        });
    }

    private void displayQuestion() {
        questionText.setText(questions[currentQuestionIndex]);
        option1.setText(answers[currentQuestionIndex][0]);
        option2.setText(answers[currentQuestionIndex][1]);
        option3.setText(answers[currentQuestionIndex][2]);
        option4.setText(answers[currentQuestionIndex][3]);

        questionCounter.setText("Pytanie: " + (currentQuestionIndex + 1) + "/" + questions.length);

        radioGroup.clearCheck();

        progressBar.setProgress((currentQuestionIndex) * 10);
    }

    private void showResult() {
        questionText.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        questionCounter.setText("Gratulacje");

        result.setText("Twój wynik: " + score + "/10");
        result.setVisibility(View.VISIBLE);

        nextButton.setText("Restart");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetQuiz();
            }
        });
    }

    private void resetQuiz() {
        currentQuestionIndex = 0;
        score = 0;

        questionText.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        questionCounter.setVisibility(View.VISIBLE);
        result.setVisibility(View.INVISIBLE);

        nextButton.setText("Następne");

        displayQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOptionId = radioGroup.getCheckedRadioButtonId();
                if (selectedOptionId == -1) {
                    return;
                }

                int selectedAnswerIndex = radioGroup.indexOfChild(findViewById(selectedOptionId));
                if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
                    score++;
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    displayQuestion();
                }
                else {
                    showResult();
                }
            }
        });
    }
}
