package com.example.bookmanagementapp.di

import com.example.bookmanagementapp.room.BookDao
import com.example.bookmanagementapp.network.BookApiService
import com.example.bookmanagementapp.repository.BookRepository
import com.example.bookmanagementapp.repository.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBookRepository(
        bookDao: BookDao,
        bookApiService: BookApiService
    ): BookRepository {
        return BookRepositoryImpl(bookDao, bookApiService)
    }
}