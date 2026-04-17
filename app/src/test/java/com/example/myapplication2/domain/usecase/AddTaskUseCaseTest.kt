package com.example.myapplication2.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

open class AddTaskUseCaseTest {

    private lateinit var repository: FakeTaskRepository
    private lateinit var useCase: AddTaskUseCase

    @Before
    fun setup() {
        repository = FakeTaskRepository()
        useCase = AddTaskUseCase(repository, kotlinx.coroutines.Dispatchers.Unconfined)
    }

    @Test
    fun `invoke with valid title adds task and returns success`() = runTest {
        // Act
        val result = useCase("Новая задача", "Описание задачи")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(1, repository.getAllTasks().size)
        assertEquals("Новая задача", repository.getAllTasks().first().title)
    }

    @Test
    fun `invoke with blank title returns failure`() = runTest {
        // Act
        val result = useCase("   ", "Описание")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals(0, repository.getAllTasks().size)
    }

    @Test
    fun `invoke trims title and description before saving`() = runTest {
        // Act
        useCase("  Заголовок  ", "  Описание  ")

        // Assert
        val task = repository.getAllTasks().first()
        assertEquals("Заголовок", task.title)
        assertEquals("Описание", task.description)
    }
}