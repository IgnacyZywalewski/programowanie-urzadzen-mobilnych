package com.example.lista_6

import kotlin.random.Random

object ExerciseData {

    private var exerciseLists: List<ExerciseList>? = null

    fun getExerciseLists(): List<ExerciseList> {
        if (exerciseLists == null) {
            exerciseLists = generateExerciseLists(20)
        }
        return exerciseLists!!
    }

    private fun generateExerciseLists(count: Int): List<ExerciseList> {
        val subjects = listOf("Matematyka", "Pum", "Fizyka", "Elektronika", "Algorytmy")
        val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin ac urna vitae orci."
        val subjectListCounters = mutableMapOf<String, Int>().apply {
            subjects.forEach { put(it, 0) }
        }

        val lists = mutableListOf<ExerciseList>()

        repeat(count) {
            val subject = subjects.random()
            val listNumber = (subjectListCounters[subject] ?: 0) + 1
            subjectListCounters[subject] = listNumber

            val numOfExercises = Random.nextInt(1, 11)
            val exercises = List(numOfExercises) {
                val contentLength = Random.nextInt(10, loremIpsum.length)
                val content = loremIpsum.substring(0, contentLength)
                val points = Random.nextInt(1, 11)
                Exercise(content = content, points = points)
            }

            val grade = 3.0 + Random.nextInt(0, 5) * 0.5 // Random grade from 3.0 to 5.0
            lists.add(ExerciseList(exercises = exercises, subject = subject, grade = grade, listNumber = listNumber))
        }

        return lists
    }
}
