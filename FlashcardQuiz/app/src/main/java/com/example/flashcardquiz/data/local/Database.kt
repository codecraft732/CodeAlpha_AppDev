package com.example.flashcardquiz.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flashcardquiz.data.model.FlashcardData

@Database(entities = [FlashcardData::class], version = 1)
abstract class CardDatabase : RoomDatabase() {
    abstract fun flashCardDao(): FlashCardDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Example migration if needed in future
                // db.execSQL("ALTER TABLE cards ADD COLUMN color INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): CardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardDatabase::class.java,
                    "cards_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
