package com.example.quizapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView questionText, questionCounter, result;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private ProgressBar progressBar;

    private final String[] questions = {
            "Jednostką siły w układzie SI jest:",
            "Wzór na prędkość w ruchu jednostajnym prostoliniowym to:",
            "Czym jest praca w fizyce?",
            "Jakie jest przyspieszenie ziemskie na powierzchni Ziemi?",
            "Co to jest pętla w programowaniu?",
            "Co oznacza skrót RAM?",
            "Która z poniższych jednostek jest największa?",
            "Który z poniższych języków programowania jest językiem niskiego poziomu?",
            "W jakim systemie liczbowym działają komputery?",
            "Co oznacza skrót HTML?"
    };

    private final String[][] answers = {
            {"Kilogram", "Niuton", "Dżul", "Wat"},
            {"v = a ⋅ t", "v = s / t", "v = m ⋅ g", "v = t / s"},
            {"Wynikiem siły działającej na ciało na odcinku drogi", "Objętością ciała", "Ilością ciepła w procesie", "Czasem trwania ruchu"},
            {"8,9 m/s²", "10,8 m/s²", "9,8 m/s²", "11,8 m/s²"},
            {"Funkcja do przechowywania danych", "Instrukcja umożliwiająca wielokrotne wykonanie bloku kodu", "Typ danych", "Kod źródłowm programu"},
            {"Read-Only Memory", "Random Access Memory", "Random Action Module", "Remote Access Memory"},
            {"Gigabajt", "Megabajt", "Bajt", "Kilobajt"},
            {"Python", "C++", "Java", "Assembler"},
            {"Dziesiętnym", "Binarnym", "Ósemkowym", "Szesnastkowym"},
            {"Hyperlink and Text Markup Language", "Hyper Transfer Markup Language", "Hyper Text Markup Language", "High Text Markup Language"}
    };

    private final int[] correctAnswers = {1, 1, 0, 2, 1, 1, 0, 3, 1, 2};
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
        questionText.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

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
