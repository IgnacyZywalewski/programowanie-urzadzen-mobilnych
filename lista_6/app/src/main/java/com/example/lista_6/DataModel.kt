package com.example.lista_6

data class Exercise(
    val content: String,
    val points: Int
)

data class ExerciseList(
    val exercises: List<Exercise>,
    val subject: String,
    val grade: Double,
    val listNumber: Int
)