package com.example.myapplication2.domain.repository

import com.example.myapplication2.domain.model.Task

interface TaskRepository {
    fun getAllTasks(): List<Task>
    fun addTask(task: Task)
    fun deleteTask(id: String)
    fun updateTask(task: Task)
}