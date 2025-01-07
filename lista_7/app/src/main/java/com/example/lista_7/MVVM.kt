package com.example.lista_7

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import kotlin.random.Random

data class Student(
    val indexNumber: Int,
    val firstName: String,
    val lastName: String,
    val averageGrade: Double,
    val yearOfStudy: Int
)

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


class StudentViewModel : ViewModel() {
    private val _students = mutableStateListOf<Student>()
    val students: SnapshotStateList<Student> get() = _students

    init {
        _students.addAll(generateRandomStudents(10))
    }

    fun getStudentByIndex(indexNumber: Int): Student? {
        return _students.find { it.indexNumber == indexNumber }
    }
}