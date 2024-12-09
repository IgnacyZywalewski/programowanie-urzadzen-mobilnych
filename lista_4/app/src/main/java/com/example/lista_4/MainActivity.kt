package com.example.lista_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    private val questions = listOf(
        "Jednostką siły w układzie SI jest:",
        "Wzór na prędkość w ruchu jednostajnym prostoliniowym to:",
        "Czym jest praca w fizyce?",
        "Jakie jest przyspieszenie ziemskie na powierzchni Ziemi?",
        "Co to jest pętla w programowaniu?",
        "Co oznacza skrót RAM?",
        "Która z poniższych jednostek jest największa?",
        "Który z poniższych języków programowania jest językiem najniższego poziomu?",
        "W jakim systemie liczbowym działają komputery?",
        "Co oznacza skrót HTML?"
    )

    private val answers = listOf(
        listOf("Kilogram", "Niuton", "Dżul", "Wat"),
        listOf("v = a ⋅ t", "v = s / t", "v = m ⋅ g", "v = t / s"),
        listOf("Wynikiem siły działającej na ciało na odcinku drogi", "Objętością ciała", "Ilością ciepła w procesie", "Czasem trwania ruchu"),
        listOf("8,9 m/s²", "10,8 m/s²", "9,8 m/s²", "11,8 m/s²"),
        listOf("Funkcja do przechowywania danych", "Instrukcja umożliwiająca wielokrotne wykonanie bloku kodu", "Typ danych", "Kod źródłowy programu"),
        listOf("Read-Only Memory", "Random Access Memory", "Random Action Module", "Remote Access Memory"),
        listOf("Gigabajt", "Megabajt", "Bajt", "Kilobajt"),
        listOf("Python", "C++", "Java", "Assembler"),
        listOf("Dziesiętnym", "Binarnym", "Ósemkowym", "Szesnastkowym"),
        listOf("Hyperlink and Text Markup Language", "Hyper Transfer Markup Language", "Hyper Text Markup Language", "High Text Markup Language")
    )

    private val correctAnswers = listOf(1, 1, 0, 2, 1, 1, 0, 3, 1, 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizApp(questions, answers, correctAnswers)
        }
    }
}

@Composable
fun QuizApp(questions: List<String>, answers: List<List<String>>, correctAnswers: List<Int>) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }

    if (showResult) {
        ResultScreen(score = score, totalQuestions = questions.size) {
            currentQuestionIndex = 0
            score = 0
            selectedOption = null
            showResult = false
        }
    } else {
        QuestionScreen(
            question = questions[currentQuestionIndex],
            answers = answers[currentQuestionIndex],
            questionIndex = currentQuestionIndex,
            totalQuestions = questions.size,
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
            onNextClicked = {
                if (selectedOption == correctAnswers[currentQuestionIndex]) {
                    score++
                }
                if (currentQuestionIndex + 1 < questions.size) {
                    currentQuestionIndex++
                    selectedOption = null
                } else {
                    showResult = true
                }
            }
        )
    }
}

@Composable
fun QuestionScreen(
    question: String,
    answers: List<String>,
    questionIndex: Int,
    totalQuestions: Int,
    selectedOption: Int?,
    onOptionSelected: (Int) -> Unit,
    onNextClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Pytanie ${questionIndex + 1}/$totalQuestions",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        LinearProgressIndicator(
            progress = {(questionIndex + 1).toFloat() / totalQuestions.toFloat()},
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = question,
                fontSize = 25.sp,
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        answers.forEachIndexed { index, answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                RadioButton(
                    selected = selectedOption == index,
                    onClick = { onOptionSelected(index) },
                )
                Text(
                    text = answer,
                    fontSize = 20.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNextClicked,
            enabled = selectedOption != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Następne",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ResultScreen(score: Int, totalQuestions: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gratulacje!",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Twój wynik: $score/$totalQuestions",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart) {
            Text(text = "Zrestartuj quiz")
        }
    }
}
