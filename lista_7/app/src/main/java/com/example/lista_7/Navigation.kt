package com.example.lista_7

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(studentViewModel: StudentViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "studentList") {
        composable(route = "studentList") {
            StudentListScreen(studentViewModel, navController)
        }
        composable(route = "studentDetail/{indexNumber}") { backStackEntry ->
            val indexNumber = backStackEntry.arguments?.getString("indexNumber")?.toIntOrNull()
            val student = indexNumber?.let { studentViewModel.getStudentByIndex(it) }
            student?.let {
                StudentDetailScreen(it)
            }
        }
    }
}