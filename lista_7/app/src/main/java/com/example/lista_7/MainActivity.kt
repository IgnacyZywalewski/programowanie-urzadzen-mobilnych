package com.example.lista_7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

data class Student(
    val indexNumber: Int,
    val firstName: String,
    val lastName: String,
    val averageGrade: Double,
    val yearOfStudy: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val students = generateRandomStudents(10)

    NavHost(navController = navController, startDestination = "studentList") {
        composable(route = "studentList") {
            StudentListScreen(students, navController)
        }
        composable(route = "studentDetail/{indexNumber}") { backStackEntry ->
            val indexNumber = backStackEntry.arguments?.getString("indexNumber")
            val student = students.find { "${it.indexNumber}" == indexNumber }
            student?.let {
                StudentDetailScreen(it)
            }
        }
    }
}

@Composable
fun StudentListScreen(students: List<Student>, navController: NavHostController) {
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
            )
            {
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

fun generateRandomStudents(count: Int): List<Student> {
    val firstNames = listOf("Jan", "Anna", "Piotr", "Katarzyna", "Michał", "Ewa")
    val lastNames = listOf("Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kamiński", "Zieliński")

    return List(count) {
        Student(
            indexNumber = Random.nextInt(100000, 999999),
            firstName = firstNames.random(),
            lastName = lastNames.random(),
            averageGrade = Random.nextDouble(2.0, 5.0).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble(),
            yearOfStudy = Random.nextInt(1, 6)
        )
    }
}
