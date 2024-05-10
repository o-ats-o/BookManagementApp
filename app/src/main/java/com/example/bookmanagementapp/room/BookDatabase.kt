package com.example.bookmanagementapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookmanagementapp.model.BookInfoEntity

@Database(entities = [BookInfoEntity::class], version = 2)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}