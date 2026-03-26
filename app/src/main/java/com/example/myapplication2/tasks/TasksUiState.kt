package com.example.myapplication2.presentation.tasks

import com.example.myapplication2.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)