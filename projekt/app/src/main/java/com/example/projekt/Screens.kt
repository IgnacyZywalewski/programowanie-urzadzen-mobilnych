package com.example.projekt

import android.annotation.SuppressLint
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun Navigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TaskListScreen(
                tasks = viewModel.tasks.collectAsState().value,
                onEdit = { task ->
                    navController.navigate("edit/${task.id}")
                },
                onAdd = {
                    navController.navigate("add")
                }
            )
        }

        composable("edit/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            val task = viewModel.tasks.collectAsState().value.find { it.id == taskId }

            EditTaskScreen(
                task = task,
                onSave = {
                    if (it != null) viewModel.updateTask(it)
                    navController.popBackStack()
                },
                onDelete = {
                    if (it != null) viewModel.deleteTask(it)
                    navController.popBackStack()
                }
            )
        }

        composable("add") {
            AddTaskScreen(
                onSave = { title, content, priority, date ->
                    viewModel.addTask(title, content, priority, date)
                    navController.popBackStack()
                }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TaskListScreen(tasks: List<Task>, onEdit: (Task) -> Unit, onAdd: () -> Unit) {
    Scaffold(
        topBar = {
            Text(
                text = "Tasks to do",
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
                items(tasks) { task ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onEdit(task) }
                            .padding(20.dp)
                    ) {
                        Text(
                            text = task.title,
                            Modifier.weight(1f),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = task.priority.toString(),
                            fontSize = 25.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            Button(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Add Task", fontSize = 25.sp)
            }
        }
    )
}

@Composable
fun EditTaskScreen(task: Task?, onSave: (Task?) -> Unit, onDelete: (Task?) -> Unit) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var content by remember { mutableStateOf(task?.content ?: "") }
    var priority by remember { mutableStateOf(task?.priority?.toString() ?: "") }

    Scaffold(
        topBar = {
            Text(
                text = "Edit Task",
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
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp)
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp)
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = priority,
                    onValueChange = { if (it.all { char -> char.isDigit() }) priority = it },
                    label = { Text("Priority (1-5)") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp)
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank() && priority.toIntOrNull() in 1..5) {
                            onSave(task?.copy(title = title, content = content, priority = priority.toInt()))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Save", fontSize = 20.sp)
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onDelete(task) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Delete", fontSize = 20.sp)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun AddTaskScreen(onSave: (String, String, Int, Date) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    val isFormValid = remember(title, content, priority, date) {
        title.isNotBlank() &&
                content.isNotBlank() &&
                priority.toIntOrNull()?.let { it in 1..10 } == true &&
                date.matches(Regex("\\d{2}-\\d{2}-\\d{4}"))
    }

    Scaffold(
        topBar = {
            Text(
                text = "Add New Task",
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
                    .padding(20.dp)
                    .padding(top = 50.dp),
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title", fontSize = 25.sp, textAlign = TextAlign.Center) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp)
                )
                Spacer(Modifier.height(32.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(text = "Content", fontSize = 25.sp, textAlign = TextAlign.Center) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp)
                )
                Spacer(Modifier.height(32.dp))
                TextField(
                    value = priority,
                    onValueChange = { if (it.all { char -> char.isDigit() }) priority = it },
                    label = { Text(text = "Priority (1-10)", fontSize = 25.sp, textAlign = TextAlign.Center) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp),
                )
                Spacer(Modifier.height(32.dp))

                TextField(
                    value = date,
                    onValueChange = { date = it },
                    readOnly = true,
                    label = { Text("Date (DD-MM-YYYY)", fontSize = 25.sp) },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp)
                )

                if (showDatePicker) {
                    Popup(
                        onDismissRequest = { showDatePicker = false },
                        alignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 64.dp)
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp, bottom = 40.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                DatePicker(state = datePickerState)

                                Button(
                                    onClick = {
                                        datePickerState.selectedDateMillis?.let { millis ->
                                            date = convertMillisToDate(millis)
                                            showDatePicker = false
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text("Confirm")
                                }
                            }
                        }
                    }

                }
            }
        },

        bottomBar = {
            Button(
                onClick = {
                    onSave(title, content, priority.toInt(), SimpleDateFormat("dd-MM-yyyy").parse(date)!!)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, bottom = 40.dp),
                enabled = isFormValid
            ) {
                Text(text = "Save", fontSize = 35.sp)
            }
        }
    )
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


