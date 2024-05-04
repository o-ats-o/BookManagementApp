package com.example.bookmanagementapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookmanagementapp.dao.BookDao
import com.example.bookmanagementapp.model.BookInfoEntity

@Database(entities = [BookInfoEntity::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}