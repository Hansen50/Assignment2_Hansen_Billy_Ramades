package com.example.assignment2_hansenbillyramades

import com.example.assignment2_hansenbillyramades.data.DiaryEntity

interface ItemDiaryListener {
    fun onClick(diary: DiaryEntity)
    fun onDelete(diary: DiaryEntity)
    fun onEdit(diary: DiaryEntity)
}