package com.example.myapplication2.domain.usecase

import com.example.myapplication2.di.qualifiers.IoDispatcher
import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<List<Task>> = withContext(ioDispatcher) {
        try {
            val tasks = repository.getAllTasks()
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}