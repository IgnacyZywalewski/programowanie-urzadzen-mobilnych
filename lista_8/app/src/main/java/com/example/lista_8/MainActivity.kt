package com.example.lista_8

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ROOM Setup
@Entity
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val grade: Float
)

@Dao
interface GradeDao {
    @Query("SELECT * FROM Grade")
    suspend fun getAllGrades(): List<Grade>

    @Insert
    suspend fun insertGrade(grade: Grade)

    @Update
    suspend fun updateGrade(grade: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)
}

@Database(entities = [Grade::class], version = 1)
abstract class GradeDatabase : RoomDatabase() {
    abstract fun gradeDao(): GradeDao
}

// Repository
class GradeRepository(private val dao: GradeDao) {
    suspend fun getAllGrades() = dao.getAllGrades()
    suspend fun insertGrade(grade: Grade) = dao.insertGrade(grade)
    suspend fun updateGrade(grade: Grade) = dao.updateGrade(grade)
    suspend fun deleteGrade(grade: Grade) = dao.deleteGrade(grade)
}

// ViewModel
class GradeViewModel(private val repository: GradeRepository) : ViewModel() {
    private val _grades = MutableStateFlow<List<Grade>>(emptyList())
    val grades: StateFlow<List<Grade>> get() = _grades

    init {
        viewModelScope.launch {
            if (repository.getAllGrades().isEmpty()) {
                repository.insertGrade(Grade(subject = "Matematyka", grade = 4.0f))
                repository.insertGrade(Grade(subject = "Fizyka", grade = 5.0f))
                repository.insertGrade(Grade(subject = "Chemia", grade = 3.5f))
                repository.insertGrade(Grade(subject = "Biologia", grade = 4.5f))
            }
            loadGrades()
        }
    }

    private fun loadGrades() {
        viewModelScope.launch {
            _grades.value = repository.getAllGrades()
        }
    }

    fun addGrade(subject: String, grade: Float) {
        viewModelScope.launch {
            repository.insertGrade(Grade(subject = subject, grade = grade))
            loadGrades()
        }
    }

    fun updateGrade(grade: Grade) {
        viewModelScope.launch {
            repository.updateGrade(grade)
            loadGrades()
        }
    }

    fun deleteGrade(grade: Grade) {
        viewModelScope.launch {
            repository.deleteGrade(grade)
            loadGrades()
        }
    }
}



class GradeViewModelFactory(private val repository: GradeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GradeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@Composable
fun GradeApp(viewModel: GradeViewModel) {
    val grades by viewModel.grades.collectAsState(initial = emptyList())
    var currentScreen by remember { mutableStateOf("list") }
    var selectedGrade by remember { mutableStateOf<Grade?>(null) }

    when (currentScreen) {
        "list" -> GradeListScreen(grades, onEdit = {
            selectedGrade = it
            currentScreen = "edit"
        }, onAdd = {
            currentScreen = "add"
        })
        "edit" -> EditGradeScreen(selectedGrade, onSave = {
            if (it != null) viewModel.updateGrade(it)
            currentScreen = "list"
        }, onDelete = {
            if (it != null) viewModel.deleteGrade(it)
            currentScreen = "list"
        })
        "add" -> AddGradeScreen(onSave = {
            viewModel.addGrade(it.first, it.second)
            currentScreen = "list"
        })
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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(applicationContext, GradeDatabase::class.java, "grade-db").build()
        val repository = GradeRepository(db.gradeDao())
        val viewModel: GradeViewModel = ViewModelProvider(this, GradeViewModelFactory(repository))[GradeViewModel::class.java]

        setContent {
            MaterialTheme {
                GradeApp(viewModel)
            }
        }
    }
}

