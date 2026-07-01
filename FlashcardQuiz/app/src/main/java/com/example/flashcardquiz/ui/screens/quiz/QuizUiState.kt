package com.example.flashcardquiz.ui.screens.quiz


import com.example.flashcardquiz.data.model.FlashcardData

data class QuizUiState(
    val flashcards: List<FlashcardData> = emptyList(),
    val currentIndex: Int = 0,
    val isAnswerVisible: Boolean = false,
    val isLoading: Boolean = true,
    val timeLeft: String = "8m left"
) {
    val currentCard: FlashcardData?
        get() = if (flashcards.isNotEmpty() && currentIndex in flashcards.indices) {
            flashcards[currentIndex]
        } else null

    val progress: Float
        get() = if (flashcards.isNotEmpty()) {
            (currentIndex + 1).toFloat() / flashcards.size
        } else 0f
    
    val currentPositionText: String
        get() = "Card ${currentIndex + 1} of ${flashcards.size}"
}
