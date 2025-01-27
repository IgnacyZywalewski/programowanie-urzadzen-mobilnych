package com.example.projekt

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
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

    val originalTasks = remember { tasks }
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("Sort by") }

    var filteredTasks by remember { mutableStateOf(tasks) }
    var isFilterMenuExpanded by remember { mutableStateOf(false) }
    var isPrioritySubMenuExpanded by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf<Int?>(null) }

    fun applyFiltersAndSorting() {
        val baseTasks = if (selectedPriority == null) {
            originalTasks
        } else {
            originalTasks.filter { it.priority == selectedPriority }
        }

        filteredTasks = when (selectedSortOption) {
            "Date (Earliest)" -> baseTasks.sortedBy { it.date }
            "Date (Latest)" -> baseTasks.sortedByDescending { it.date }
            "Priority (Lowest)" -> baseTasks.sortedBy { it.priority }
            "Priority (Highest)" -> baseTasks.sortedByDescending { it.priority }
            else -> baseTasks
        }
    }


    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Tasks to do",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Sortowanie
                    Box(
                        Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        Button(
                            onClick = { isSortMenuExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = selectedSortOption,
                                fontSize = 20.sp
                            )
                        }

                        DropdownMenu(
                            expanded = isSortMenuExpanded,
                            onDismissRequest = { isSortMenuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Default", fontSize = 20.sp) },
                                onClick = {
                                    selectedSortOption = "Sort by"
                                    applyFiltersAndSorting()
                                    isSortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Date (Earliest)", fontSize = 20.sp) },
                                onClick = {
                                    selectedSortOption = "Date (Earliest)"
                                    applyFiltersAndSorting()
                                    isSortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Date (Latest)", fontSize = 20.sp) },
                                onClick = {
                                    selectedSortOption = "Date (Latest)"
                                    applyFiltersAndSorting()
                                    isSortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Priority (Lowest)", fontSize = 20.sp) },
                                onClick = {
                                    selectedSortOption = "Priority (Lowest)"
                                    applyFiltersAndSorting()
                                    isSortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Priority (Highest)", fontSize = 20.sp) },
                                onClick = {
                                    selectedSortOption = "Priority (Highest)"
                                    applyFiltersAndSorting()
                                    isSortMenuExpanded = false
                                }
                            )
                        }
                    }

                    //Filtrowanie
                    Box(
                        Modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        Button(
                            onClick = { isFilterMenuExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Filter by", fontSize = 20.sp)
                        }

                        DropdownMenu(
                            expanded = isFilterMenuExpanded,
                            onDismissRequest = { isFilterMenuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Clear Filter", fontSize = 20.sp) },
                                onClick = {
                                    selectedPriority = null
                                    applyFiltersAndSorting()
                                    isFilterMenuExpanded = false
                                }
                            )

                            Box(

                            ){
                                DropdownMenuItem(
                                    text = { Text("Priority 1-10", fontSize = 20.sp) },
                                    onClick = { isPrioritySubMenuExpanded = true }
                                )

                                DropdownMenu(
                                    expanded = isPrioritySubMenuExpanded,
                                    onDismissRequest = { isPrioritySubMenuExpanded = false }
                                ) {
                                    (1..10).forEach { priority ->
                                        DropdownMenuItem(
                                            text = { Text("Priority $priority", fontSize = 20.sp) },
                                            onClick = {
                                                selectedPriority = priority
                                                applyFiltersAndSorting()
                                                isPrioritySubMenuExpanded = false
                                                isFilterMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }


                            DropdownMenuItem(
                                text = { Text("Date (range)", fontSize = 20.sp) },
                                onClick = {
                                    selectedPriority = null
                                    isFilterMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },

        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
            ) {
                items(filteredTasks) { task ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable { onEdit(task) }
                    ) {
                        Row {
                            Text(
                                text = task.title,
                                Modifier.weight(1f),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = task.priority.toString(),
                                fontSize = 30.sp
                            )
                        }
                        if (task.content.isNotBlank()) {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = task.content,
                                fontSize = 25.sp,
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = formatDateToString(task.date),
                            fontSize = 25.sp,
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
                    .padding(20.dp)
                    .padding(bottom = 30.dp)
            ) {
                Text(text = "Add Task", fontSize = 35.sp)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(task: Task?, onSave: (Task?) -> Unit, onDelete: (Task?) -> Unit) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var content by remember { mutableStateOf(task?.content ?: "") }
    var priority by remember { mutableStateOf(task?.priority?.toString() ?: "") }
    var date by remember { mutableStateOf(task?.date ?: Date()) }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

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
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp)
                    .padding(top = 50.dp),
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = "Title",
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp)
                )
                Spacer(Modifier.height(32.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = {
                        Text(
                            text = "Content",
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp)
                )
                Spacer(Modifier.height(32.dp))
                TextField(
                    value = priority,
                    onValueChange = { if (it.all { char -> char.isDigit() }) priority = it },
                    label = {
                        Text(
                            text = "Priority (1-10)",
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 30.sp),
                )
                Spacer(Modifier.height(32.dp))

                TextField(
                    value = formatDateToString(date),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Date", fontSize = 25.sp) },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
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
                                .padding(20.dp)
                                .padding(bottom = 60.dp)
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
                                            date = Date(millis)
                                            showDatePicker = false
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .fillMaxWidth()
                                ) {
                                    Text(text = "Confirm", fontSize = 30.sp)
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 15.dp)
            ) {
                Button(
                    onClick = {
                        if (title.isNotBlank() && priority.toIntOrNull() in 1..10) {
                            onSave(
                                task?.copy(
                                    title = title,
                                    content = content,
                                    priority = priority.toInt(),
                                    date = date
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Save", fontSize = 30.sp)
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onDelete(task) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Delete", fontSize = 30.sp)
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

    val isFormValid = remember(title, priority, date) {
        title.isNotBlank() && priority.toIntOrNull()?.let { it in 1..10 } == true && date.isNotBlank()
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
                    label = { Text("Date", fontSize = 25.sp) },
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
                                .padding(20.dp)
                                .padding(bottom = 60.dp)
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
                                            date = convertMillisToString(millis)
                                            showDatePicker = false
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .fillMaxWidth()
                                ) {
                                    Text(text = "Confirm", fontSize = 30.sp)
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
                    .padding(20.dp)
                    .padding(bottom = 30.dp),
                enabled = isFormValid
            ) {
                Text(text = "Save", fontSize = 35.sp)
            }
        }
    )
}


fun formatDateToString(date: Date): String {
    val formatter = SimpleDateFormat("EEEE, dd-MM-yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun convertMillisToString(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(date)
}