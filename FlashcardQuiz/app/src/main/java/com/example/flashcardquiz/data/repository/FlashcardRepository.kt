package com.example.flashcardquiz.data.repository

import com.example.flashcardquiz.data.local.FlashCardDao
import com.example.flashcardquiz.data.model.FlashcardData
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getFlashcards(): Flow<List<FlashcardData>>
    suspend fun addCard(card: FlashcardData)
    suspend fun updateCard(card: FlashcardData)
    suspend fun deleteCard(card: FlashcardData)
}

class FlashcardRepositoryImpl(private val dao: FlashCardDao) : FlashcardRepository {
    override fun getFlashcards(): Flow<List<FlashcardData>> = dao.getAllCard()
    override suspend fun addCard(card: FlashcardData) = dao.insert(card)
    override suspend fun updateCard(card: FlashcardData) = dao.update(card)
    override suspend fun deleteCard(card: FlashcardData) = dao.delete(card)
}
