package com.example.myapplication2.domain.usecase

import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.repository.TaskRepository
import java.util.UUID


class AddTaskUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(title: String, description: String) {
        if (title.isBlank()) return

        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            description = description.trim()
        )
        repository.addTask(task)
    }
}