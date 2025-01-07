package com.example.lista_7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(studentViewModel)
        }
    }
}