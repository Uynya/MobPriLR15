package com.example.myapplication2.data.repository


import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.repository.TaskRepository
import com.example.myapplication2.data.mapper.TaskMapper

class InMemoryTaskRepositoryImpl : TaskRepository {

    private val tasks = mutableListOf<TaskEntity>()

    override fun getAllTasks(): List<Task> {
        return tasks.map { TaskMapper.toDomain(it) }
    }

    override fun addTask(task: Task) {
        val entity = TaskMapper.toEntity(task)
        tasks.add(entity)
    }

    override fun deleteTask(id: String) {
        tasks.removeAll { it.id == id }
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = TaskMapper.toEntity(task)
        }
    }
}