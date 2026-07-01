package com.example.flashcardquiz.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardquiz.data.model.FlashcardData
import com.example.flashcardquiz.data.repository.FlashcardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: FlashcardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadFlashcards()
    }

    fun addCard(question: String, answer: String) = viewModelScope.launch {
        repository.addCard(
            FlashcardData(
                question = question, answer = answer,
                id = 0,
                category = "General"
            )
        )
    }

    fun updateCard(card: FlashcardData)= viewModelScope.launch{
        repository.updateCard(card)
    }

    fun deleteCard(card: FlashcardData)= viewModelScope.launch {
        repository.deleteCard(card)
    }
    private fun loadFlashcards() {
        viewModelScope.launch {
            repository.getFlashcards().collect { cards ->
                _uiState.update { it.copy(flashcards = cards, isLoading = false) }
            }
        }
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.ToggleAnswer -> {
                _uiState.update { it.copy(isAnswerVisible = !it.isAnswerVisible) }
            }

            is QuizEvent.NextCard -> {
                _uiState.update {
                    val nextIndex = (it.currentIndex + 1).coerceAtMost(it.flashcards.size - 1)
                    it.copy(currentIndex = nextIndex, isAnswerVisible = false)
                }
            }

            is QuizEvent.PreviousCard -> {
                _uiState.update {
                    val prevIndex = (it.currentIndex - 1).coerceAtLeast(0)
                    it.copy(currentIndex = prevIndex, isAnswerVisible = false)
                }
            }

            is QuizEvent.SubmitFeedback -> {
                // Handle feedback
                _uiState.update { it.copy(isAnswerVisible = false) }
                onEvent(QuizEvent.NextCard)
            }

            is QuizEvent.AddCard -> {
                addCard(event.question, event.answer)
            }

            is QuizEvent.UpdateCard -> {
                updateCard(event.card)
            }

            is QuizEvent.DeleteCard -> {
                deleteCard(event.card)
                _uiState.update { 
                    it.copy(
                        currentIndex = (it.currentIndex).coerceAtMost((it.flashcards.size - 2).coerceAtLeast(0)),
                        isAnswerVisible = false
                    )
                }
            }

            is QuizEvent.OpenDrawer -> {
                // Navigation or state for drawer handled in UI
            }
        }
    }
}
