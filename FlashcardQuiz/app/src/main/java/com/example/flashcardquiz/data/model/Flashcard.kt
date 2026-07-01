package com.example.flashcardquiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class FlashcardData(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val question: String,
    val answer: String,
    val category: String = "General"
)
