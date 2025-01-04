package com.example.lista_6

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screens(val route: String) {
    data object ExerciseListScreen : Screens("home")
    data object GradesScreen : Screens("first")
    data object TaskScreen : Screens("second") {
        fun createRoute(subject: String, listNumber: Int): String {
            return "/$subject/$listNumber"
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.ExerciseListScreen.route
    ) {
        composable(route = Screens.ExerciseListScreen.route) {
            ExerciseListScreen(navController = navController)
        }
        composable(route = Screens.GradesScreen.route) {
            GradesScreen(navController = navController)
        }
        composable(
            route = "/{subject}/{listNumber}",
            arguments = listOf(
                navArgument("subject") { type = NavType.StringType },
                navArgument("listNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject") ?: ""
            val listNumber = backStackEntry.arguments?.getInt("listNumber") ?: 0
            TaskScreen(
                subject = subject,
                listNumber = listNumber,
                navController = navController
            )
        }
    }
}

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomBar(Screens.ExerciseListScreen.route, "Listy zadań", Icons.Filled.Home)
    data object First : BottomBar(Screens.GradesScreen.route, "Oceny", Icons.Filled.Info)
}

@Composable
fun BottomMenu(navController: NavHostController) {
    val screens = listOf(
        BottomBar.Home, BottomBar.First
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = screen.title) },
                icon = { Icon(imageVector = screen.icon, contentDescription = "icon") },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}
