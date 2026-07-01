package com.example.flashcardquiz.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcardquiz.data.model.FlashcardData
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: FlashcardData)

    @Query  ("SELECT * FROM cards ")
    fun getAllCard(): Flow<List<FlashcardData>>


    @Delete
    suspend fun delete(card: FlashcardData)

    @Update
    suspend fun update(card: FlashcardData)
}