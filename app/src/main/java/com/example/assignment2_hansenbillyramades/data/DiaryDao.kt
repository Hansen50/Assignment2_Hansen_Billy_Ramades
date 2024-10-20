package com.example.assignment2_hansenbillyramades.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diaryEntity: DiaryEntity)

    @Query("SELECT * FROM diary_entity")
    suspend fun getDiary(): List<DiaryEntity>

    @Query("SELECT * FROM diary_entity ORDER BY date DESC")
    suspend fun getDiaryLatest(): List<DiaryEntity>

    @Query("SELECT * FROM diary_entity ORDER BY date ASC")
    suspend fun getDiaryOldest(): List<DiaryEntity>

    @Update
    suspend fun updateDiary(diaryEntity: DiaryEntity)

    @Delete
    suspend fun deleteDiary(diaryEntity: DiaryEntity)

    @Query("SELECT * FROM diary_entity WHERE title LIKE '%' || :search || '%'")
    suspend fun searchByName(search: String): List<DiaryEntity>
}