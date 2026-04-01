package com.example.myapplication2.domain.usecase

import com.example.myapplication2.di.qualifiers.IoDispatcher
import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(title: String, description: String): Result<Unit> = withContext(ioDispatcher) {
        try {
            if (title.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Title cannot be blank"))
            }

            val task = Task(
                id = System.currentTimeMillis(), // Long тип
                title = title.trim(),
                description = description.trim(),
                isCompleted = false,
                createdAt = System.currentTimeMillis()
            )

            repository.addTask(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}