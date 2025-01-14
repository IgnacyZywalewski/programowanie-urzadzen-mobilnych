package com.example.lista_8

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(viewModel: GradeViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            GradeListScreen(
                grades = viewModel.grades.collectAsState().value,
                onEdit = { grade ->
                    navController.navigate("edit/${grade.id}")
                },
                onAdd = {
                    navController.navigate("add")
                }
            )
        }

        composable("edit/{gradeId}") { backStackEntry ->
            val gradeId = backStackEntry.arguments?.getString("gradeId")?.toIntOrNull()
            val grade = viewModel.grades.collectAsState().value.find { it.id == gradeId }

            EditGradeScreen(
                grade = grade,
                onSave = {
                    if (it != null) viewModel.updateGrade(it)
                    navController.popBackStack()
                },
                onDelete = {
                    if (it != null) viewModel.deleteGrade(it)
                    navController.popBackStack()
                }
            )
        }

        composable("add") {
            AddGradeScreen(
                onSave = {
                    viewModel.addGrade(it.first, it.second)
                    navController.popBackStack()
                }
            )
        }
    }
}



@SuppressLint("DefaultLocale")
@Composable
fun GradeListScreen(grades: List<Grade>, onEdit: (Grade) -> Unit, onAdd: () -> Unit) {
    val averageGrade = grades.map { it.grade }.average()
    val formattedAverage = String.format("%.2f", averageGrade)

    Scaffold (
        topBar = {
            Text(
                text = "Moje Oceny",
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 20.dp)
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
            ) {
                items(grades) { grade ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onEdit(grade) }
                            .padding(20.dp)
                    ) {
                        Text(grade.subject, Modifier.weight(1f), fontSize = 30.sp)
                        Text(grade.grade.toString(), fontSize = 30.sp)
                    }
                }
            }
        },
        bottomBar = {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Text("Średnia ocen ", Modifier.weight(1f), fontSize = 30.sp)
                    Text(formattedAverage, fontSize = 30.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onAdd,
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Text("NOWY", fontSize = 30.sp)
                }
            }
        }
    )
}

@Composable
fun EditGradeScreen(grade: Grade?, onSave: (Grade?) -> Unit, onDelete: (Grade?) -> Unit) {
    var subject by remember { mutableStateOf(grade?.subject ?: "") }
    var gradeValue by remember { mutableStateOf(grade?.grade?.toString() ?: "") }

    Scaffold(
        topBar = {
            Text(
                text = "Edytuj",
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            )
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 80.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .fillMaxWidth(0.6f),
                    value = subject,
                    onValueChange = { subject = it },
                    textStyle = TextStyle(
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(40.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .fillMaxWidth(0.6f),
                    value = gradeValue,
                    onValueChange = { gradeValue = it },
                    textStyle = TextStyle(
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        },

        bottomBar = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ){
                val isSaveEnabled = subject.isNotBlank() &&
                        gradeValue.toFloatOrNull()?.let { it in 1f..5f } == true

                Button(
                    onClick = {
                        onSave(grade?.copy(subject = subject, grade = gradeValue.toFloat()))
                    },
                    Modifier.fillMaxWidth(),
                    enabled = isSaveEnabled
                ) {
                    Text(text = "ZAPISZ", fontSize = 30.sp)
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onDelete(grade) },
                    Modifier.padding(bottom = 20.dp).fillMaxWidth()
                ) {
                    Text(text = "USUŃ", fontSize = 30.sp)
                }
            }
        }
    )
}


@Composable
fun AddGradeScreen(onSave: (Pair<String, Float>) -> Unit) {
    var subject by remember { mutableStateOf("") }
    var gradeValue by remember { mutableStateOf("") }

    fun isGradeValid(): Boolean {
        return try {
            val grade = gradeValue.toFloat()
            grade in 1f..5f
        } catch (e: Exception) {
            false
        }
    }

    fun isSubjectValid(): Boolean {
        return subject.isNotBlank()
    }

    Scaffold (
        topBar = {
            Text(
                text = "Dodaj Nowy",
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 80.dp, start = 20.dp),
            ) {
                Text(
                    text = "Nazwa przedmiotu:",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
                TextField(
                    value = subject,
                    onValueChange = { subject = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    textStyle = TextStyle(
                        fontSize = 35.sp
                    )
                )

                Spacer(Modifier.height(50.dp))

                Text(
                    text = "Ocena:",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
                TextField(
                    value = gradeValue,
                    onValueChange = { if (it.matches(Regex("\\d*\\.?\\d*"))) gradeValue = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    textStyle = TextStyle(
                        fontSize = 35.sp
                    )
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    if (isSubjectValid() && isGradeValid()) {
                        onSave(Pair(subject, gradeValue.toFloat()))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .padding(20.dp),
                enabled = isSubjectValid() && isGradeValid()
            ) {
                Text(text = "Dodaj", fontSize = 30.sp)
            }
        }
    )
}