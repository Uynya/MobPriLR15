package com.example.myapplication2.di

import com.example.myapplication2.di.qualifiers.IoDispatcher
import com.example.myapplication2.domain.repository.TaskRepository
import com.example.myapplication2.domain.usecase.AddTaskUseCase
import com.example.myapplication2.domain.usecase.GetTasksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTasksUseCase(
        repository: TaskRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetTasksUseCase {
        return GetTasksUseCase(repository, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAddTaskUseCase(
        repository: TaskRepository,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AddTaskUseCase {
        return AddTaskUseCase(repository, ioDispatcher)
    }
}