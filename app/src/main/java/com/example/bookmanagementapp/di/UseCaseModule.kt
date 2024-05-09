package com.example.bookmanagementapp.di

import com.example.bookmanagementapp.repository.BookRepository
import com.example.bookmanagementapp.usecase.GetBookInfoUseCase
import com.example.bookmanagementapp.usecase.GetBookInfoUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetBookInfoUseCase(
        bookRepository: BookRepository
    ): GetBookInfoUseCase {
        return GetBookInfoUseCaseImpl(bookRepository)
    }
}