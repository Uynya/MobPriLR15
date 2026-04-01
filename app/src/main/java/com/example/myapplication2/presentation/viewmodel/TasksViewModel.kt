package com.example.myapplication2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.di.qualifiers.IoDispatcher
import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.usecase.AddTaskUseCase
import com.example.myapplication2.domain.usecase.GetTasksUseCase
import com.example.myapplication2.presentation.tasks.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getTasksUseCase()
                .onSuccess { tasks ->
                    _uiState.update {
                        it.copy(tasks = tasks, isLoading = false, error = null)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Неизвестная ошибка")
                    }
                }
        }
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            addTaskUseCase(title, description)
                .onSuccess {
                    loadTasks() // Перезагрузить список после добавления
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Ошибка добавления")
                    }
                }
        }
    }

    fun onTaskClick(task: Task) {
        // Обработка клика по задаче
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}