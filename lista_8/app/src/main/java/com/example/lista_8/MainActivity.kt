package com.example.lista_8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(applicationContext, GradeDatabase::class.java, "grade-db").build()
        val repository = GradeRepository(db.gradeDao())
        val viewModel: GradeViewModel = ViewModelProvider(this, GradeViewModelFactory(repository))[GradeViewModel::class.java]

        setContent {
            Navigation(viewModel)
        }
    }
}