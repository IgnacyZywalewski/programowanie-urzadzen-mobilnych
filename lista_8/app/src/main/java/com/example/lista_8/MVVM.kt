package com.example.lista_8

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GradeRepository(private val dao: GradeDao) {
    suspend fun getAllGrades() = dao.getAllGrades()
    suspend fun insertGrade(grade: Grade) = dao.insertGrade(grade)
    suspend fun updateGrade(grade: Grade) = dao.updateGrade(grade)
    suspend fun deleteGrade(grade: Grade) = dao.deleteGrade(grade)
}

class GradeViewModel(private val repository: GradeRepository) : ViewModel() {

    private val _grades = MutableStateFlow<List<Grade>>(emptyList())
    val grades: StateFlow<List<Grade>> get() = _grades

    init {
        viewModelScope.launch {
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
