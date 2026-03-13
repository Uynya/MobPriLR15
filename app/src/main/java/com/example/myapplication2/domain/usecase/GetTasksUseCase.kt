package com.example.myapplication2.domain.usecase

import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.repository.TaskRepository

class GetTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): List<Task> = repository.getAllTasks()
}