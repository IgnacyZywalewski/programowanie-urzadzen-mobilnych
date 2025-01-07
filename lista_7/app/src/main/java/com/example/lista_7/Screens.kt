package com.example.lista_7

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun StudentListScreen(viewModel: StudentViewModel, navController: NavHostController) {
    val students = viewModel.students

    Scaffold(
        topBar = {
            Text(
                text = "Lista studentów",
                fontSize = 35.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 16.dp),
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
            ) {
                items(students.size) { index ->
                    val student = students[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("studentDetail/${student.indexNumber}")
                            }
                            .padding(start = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = student.firstName, fontSize = 25.sp)
                        Text(text = student.lastName, fontSize = 25.sp)
                        Text(text = "${student.indexNumber}", fontSize = 25.sp)
                    }
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StudentDetailScreen(student: Student) {
    Scaffold(
        topBar = {
            Text(
                text = "Szczegóły studenta",
                fontSize = 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 50.dp),
                textAlign = TextAlign.Center
            )
        },
        content = { paddingValues ->
            Column (
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Imię: ${student.firstName}", fontSize = 25.sp)
                Text(text = "Nazwisko: ${student.lastName}", fontSize = 25.sp)
                Text(text = "Numer indeksu: ${student.indexNumber}", fontSize = 25.sp)
                Text(text = "Średnia ocen: ${student.averageGrade}", fontSize = 25.sp)
                Text(text = "Rok studiów: ${student.yearOfStudy}", fontSize = 25.sp)
            }
        }
    )
}