package com.example.lista_6

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ExerciseListScreen(navController: NavHostController) {
    val exerciseLists = ExerciseData.getExerciseLists()

    Column (modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Listy Zadań",
            fontSize = 40.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 120.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exerciseLists) { exerciseList ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                Screens.TaskScreen.createRoute(
                                    subject = exerciseList.subject,
                                    listNumber = exerciseList.listNumber
                                )
                            )
                        },
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${exerciseList.subject}\n" + "Lista ${exerciseList.listNumber}\n" +
                                    "Liczba zadań: ${exerciseList.exercises.size}\n" + "Ocena: ${exerciseList.grade}",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GradesScreen(navController: NavHostController) {
    val exerciseLists = ExerciseData.getExerciseLists()

    val subjectStats = exerciseLists.groupBy { it.subject }.map { (subject, lists) ->
        val averageGrade = lists.map { it.grade }.average()
        val listCount = lists.size
        SubjectStats(subject, averageGrade, listCount)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Oceny",
            fontSize = 40.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 160.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
    }
}

// Data class for holding statistics per subject
data class SubjectStats(
    val subject: String,
    val averageGrade: Double,
    val listCount: Int
)



@Composable
fun TaskScreen(subject: String, listNumber: Int) {
    val exercises = getExercisesForList(subject, listNumber)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "$subject Lista $listNumber",
            fontSize = 35.sp,
            modifier = Modifier.padding(top = 60.dp, start = 100.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
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
}

fun getExercisesForList(subject: String, listNumber: Int): List<Exercise> {
    val exerciseLists = ExerciseData.getExerciseLists()
    return exerciseLists.firstOrNull { it.subject == subject && it.listNumber == listNumber }?.exercises
        ?: emptyList()
}