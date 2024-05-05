package com.example.bookmanagementapp.di

import android.content.Context
import androidx.room.Room
import com.example.bookmanagementapp.dao.BookDao
import com.example.bookmanagementapp.database.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideBookDao(@ApplicationContext context: Context): BookDao {
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "database-name"
        ).build().bookDao()
    }
}