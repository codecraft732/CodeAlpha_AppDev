package com.example.flashcardquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flashcardquiz.data.local.CardDatabase
import com.example.flashcardquiz.data.repository.FlashcardRepositoryImpl
import com.example.flashcardquiz.ui.screens.quiz.QuizScreen
import com.example.flashcardquiz.ui.screens.quiz.QuizViewModel
import com.example.flashcardquiz.ui.theme.FlashcardQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = CardDatabase.getDatabase(this)
        val repository = FlashcardRepositoryImpl(database.flashCardDao())
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuizViewModel(repository) as T
            }
        })[QuizViewModel::class.java]

        setContent {
            FlashcardQuizTheme {
                QuizScreen(viewModel = viewModel)
            }
        }
    }
}
