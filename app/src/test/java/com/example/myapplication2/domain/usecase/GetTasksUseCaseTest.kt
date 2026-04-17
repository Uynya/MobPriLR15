package com.example.myapplication2.domain.usecase

import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.domain.repository.TaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

open class FakeTaskRepository : TaskRepository {
    private val tasks = mutableListOf<Task>()

    override fun getAllTasks(): List<Task> = tasks.toList()

    override fun addTask(task: Task) {
        tasks.add(task)
    }

    override fun deleteTask(id: String) {
        tasks.removeAll { it.id.toString() == id }
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    fun clear() = tasks.clear()
    fun addTestTasks() {
        tasks.add(Task(1, "Тест 1", "Описание 1"))
        tasks.add(Task(2, "Тест 2", "Описание 2"))
    }
}

class GetTasksUseCaseTest {

    private lateinit var repository: FakeTaskRepository
    private lateinit var useCase: GetTasksUseCase

    @Before
    fun setup() {
        repository = FakeTaskRepository()
        useCase = GetTasksUseCase(repository, kotlinx.coroutines.Dispatchers.Unconfined)
    }

    @Test
    fun `invoke when repository has tasks returns success with list`() = runTest {
        // Arrange
        repository.addTestTasks()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Тест 1", result.getOrNull()?.first()?.title)
    }

    @Test
    fun `invoke when repository is empty returns success with empty list`() = runTest {
        // Arrange
        repository.clear()

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
}