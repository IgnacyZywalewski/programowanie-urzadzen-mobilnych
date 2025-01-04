package com.example.lista_6

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun ExerciseListScreen(navController: NavHostController) {
    val exerciseLists = ExerciseData.getExerciseLists()

    Scaffold(
        topBar = {
            Text(
                text = "Listy Zadań",
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 70.dp),
                textAlign = TextAlign.Center
            )
        },
        bottomBar = { BottomMenu(navController = navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exerciseLists) { exerciseList ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screens.TaskScreen.createRoute(
                                        subject = exerciseList.subject,
                                        listNumber = exerciseList.listNumber
                                    )
                                )
                            },
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${exerciseList.subject}\n" +
                                        "Lista ${exerciseList.listNumber}\n" +
                                        "Liczba zadań: ${exerciseList.exercises.size}\n" +
                                        "Ocena: ${exerciseList.grade}",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun GradesScreen(navController: NavHostController) {
    val exerciseLists = ExerciseData.getExerciseLists()

    val subjectStats = exerciseLists.groupBy { it.subject }.map { (subject, lists) ->
        val averageGrade = lists.map { it.grade }.average()
        val listCount = lists.size
        SubjectStats(subject, averageGrade, listCount)
    }

    Scaffold(
        topBar = {
            Text(
                text = "Oceny",
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 70.dp),
                textAlign = TextAlign.Center
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subjectStats) { subjectStat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screens.ExerciseListScreen.route
                                )
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = subjectStat.subject,
                                fontSize = 25.sp
                            )
                            Text(
                                text = "Średnia ocen: %.2f".format(subjectStat.averageGrade),
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Liczba list: ${subjectStat.listCount}",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { BottomMenu(navController = navController) },
    )
}

@Composable
fun TaskScreen(subject: String, listNumber: Int, navController: NavHostController) {
    val exercises = getExercisesForList(subject, listNumber)

    Scaffold(
        topBar = {
            Text(
                text = "$subject Lista $listNumber",
                fontSize = 35.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 70.dp),
                textAlign = TextAlign.Center
            )
        },
        bottomBar = { BottomMenu(navController = navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(exercises) { index, exercise ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Zadanie ${index + 1}", fontSize = 25.sp)
                            Text(text = exercise.content, fontSize = 20.sp)
                            Text(text = "${exercise.points} pkt", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    )
}

fun getExercisesForList(subject: String, listNumber: Int): List<Exercise> {
    val exerciseLists = ExerciseData.getExerciseLists()
    return exerciseLists.firstOrNull { it.subject == subject && it.listNumber == listNumber }?.exercises
        ?: emptyList()
}