package com.example.myapplication2.di

import com.example.myapplication2.data.repository.InMemoryTaskRepositoryImpl
import com.example.myapplication2.domain.repository.TaskRepository
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
    fun provideTaskRepository(): TaskRepository {
        return InMemoryTaskRepositoryImpl()
    }
}