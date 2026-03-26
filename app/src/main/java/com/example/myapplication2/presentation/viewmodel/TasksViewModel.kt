package com.example.myapplication2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.usecase.AddTaskUseCase
import com.example.myapplication2.domain.usecase.GetTasksUseCase
import com.example.myapplication2.presentation.tasks.TasksUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val tasks = getTasksUseCase()
                _uiState.update {
                    it.copy(tasks = tasks, isLoading = false, error = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error")
                }
            }
        }
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            try {
                addTaskUseCase(title, description)
                loadTasks()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Ошибка добавления")
                }
            }
        }
    }


    fun onTaskClick(task: Task) {

    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}