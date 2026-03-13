package com.example.myapplication2.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.usecase.AddTaskUseCase
import com.example.myapplication2.domain.usecase.GetTasksUseCase

class TasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase
) {
    var tasks by mutableStateOf<List<Task>>(emptyList())
        private set

    fun loadTasks() {
        tasks = getTasksUseCase()
    }

    fun addTask(title: String, description: String) {
        addTaskUseCase(title, description)
        loadTasks()
    }

    fun onTaskClick(task: Task) {

    }
}