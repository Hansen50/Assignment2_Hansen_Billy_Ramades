package com.example.assignment2_hansenbillyramades.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [DiaryEntity::class], version = 1, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao() : DiaryDao

    companion object {
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context) : DiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_dao"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}