package com.example.bookmanagementapp.di

import com.example.bookmanagementapp.network.BookApiService
import com.example.bookmanagementapp.network.service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideBookApiService(): BookApiService {
        return service
    }
}