package com.example.flashcardquiz.ui.screens.quiz

import com.example.flashcardquiz.data.model.FlashcardData

sealed class QuizEvent {
    object ToggleAnswer : QuizEvent()
    object NextCard : QuizEvent()
    object PreviousCard : QuizEvent()
    object OpenDrawer : QuizEvent()
    data class AddCard(val question: String, val answer: String) : QuizEvent()
    data class UpdateCard(val card: FlashcardData) : QuizEvent()
    data class DeleteCard(val card: FlashcardData) : QuizEvent()
    data class SubmitFeedback(val rating: Int) : QuizEvent()
}
