package com.example.myapplication2.data.repository

import com.example.myapplication2.data.mapper.TaskMapper
import com.example.myapplication2.domain.model.Task
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

open class InMemoryTaskRepositoryImplTest {

    private lateinit var repository: InMemoryTaskRepositoryImpl

    @Before
    fun setup() {
        repository = InMemoryTaskRepositoryImpl()
    }

    @Test
    fun `getAllTasks returns empty list when no tasks added`() {
        // Act
        val tasks = repository.getAllTasks()

        // Assert
        assertTrue(tasks.isEmpty())
    }

    @Test
    fun `addTask then getAllTasks returns added task`() {
        // Arrange
        val task = Task(id = 1, title = "Тест", description = "Описание")

        // Act
        repository.addTask(task)
        val result = repository.getAllTasks()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Тест", result.first().title)
    }

    @Test
    fun `deleteTask removes task by id`() {
        // Arrange
        val task1 = Task(id = 1, title = "Задача 1", description = "Описание 1")
        val task2 = Task(id = 2, title = "Задача 2", description = "Описание 2")
        repository.addTask(task1)
        repository.addTask(task2)

        // Act
        repository.deleteTask("1")
        val result = repository.getAllTasks()

        // Assert
        assertEquals(1, result.size)
        assertEquals(2, result.first().id)
    }

    @Test
    fun `updateTask modifies existing task`() {
        // Arrange
        val task = Task(id = 1, title = "Старый заголовок", description = "Старое описание")
        repository.addTask(task)

        // Act
        val updatedTask = task.copy(title = "Новый заголовок")
        repository.updateTask(updatedTask)
        val result = repository.getAllTasks().first()

        // Assert
        assertEquals("Новый заголовок", result.title)
        assertEquals("Старое описание", result.description) // описание не менялось
    }
}