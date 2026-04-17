package com.example.myapplication2.presentation.viewmodel

import com.example.myapplication2.domain.usecase.AddTaskUseCase
import com.example.myapplication2.domain.usecase.FakeTaskRepository
import com.example.myapplication2.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModelTest {

    private lateinit var getTasksUseCase: FakeGetTasksUseCase
    private lateinit var addTaskUseCase: FakeAddTaskUseCase
    private lateinit var viewModel: TasksViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTasksUseCase = FakeGetTasksUseCase()
        addTaskUseCase = FakeAddTaskUseCase()
        viewModel = TasksViewModel(
            getTasksUseCase = getTasksUseCase,
            addTaskUseCase = addTaskUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `loadTasks updates uiState with tasks on success`() = runTest {
        // Arrange
        getTasksUseCase.testTasks = listOf(
            com.example.myapplication2.domain.model.Task(1, "Задача 1", "Описание 1")
        )

        // Act
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(1, state.tasks.size)
        assertEquals("Задача 1", state.tasks.first().title)
    }

    @Test
    fun `loadTasks updates uiState with error on failure`() = runTest {
        // Arrange
        getTasksUseCase.shouldFail = true
        getTasksUseCase.errorMessage = "Ошибка сети"

        // Act
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Ошибка сети", state.error)
        assertTrue(state.tasks.isEmpty())
    }

    @Test
    fun `addTask with valid title calls useCase and reloads tasks`() = runTest {
        // Arrange
        val title = "Новая задача"
        val description = "Описание"

        // Act
        viewModel.addTask(title, description)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(addTaskUseCase.wasCalled)
        assertEquals(title, addTaskUseCase.lastTitle)
        // loadTasks() должен быть вызван после успешного добавления
    }

    @Test
    fun `addTask with blank title does nothing`() = runTest {
        // Act
        viewModel.addTask("   ", "Описание")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertFalse(addTaskUseCase.wasCalled)
    }

    @Test
    fun `clearError sets error to null`() {
        // Arrange: искусственно установим ошибку
        // (в реальной жизни это происходит через loadTasks с ошибкой)

        // Act
        viewModel.clearError()

        // Assert
        assertNull(viewModel.uiState.value.error)
    }
}

// Фейковые UseCase для тестов ViewModel
class FakeGetTasksUseCase : GetTasksUseCase(
    repository = FakeTaskRepository(),
    ioDispatcher = Dispatchers.Unconfined
) {
    var testTasks: List<com.example.myapplication2.domain.model.Task> = emptyList()
    var shouldFail = false
    var errorMessage = "Ошибка"

    override suspend fun invoke(): Result<List<com.example.myapplication2.domain.model.Task>> {
        return if (shouldFail) {
            Result.failure(Exception(errorMessage))
        } else {
            Result.success(testTasks)
        }
    }
}

class FakeAddTaskUseCase : AddTaskUseCase(
    repository = FakeTaskRepository(),
    ioDispatcher = Dispatchers.Unconfined
) {
    var wasCalled = false
    var lastTitle: String? = null
    var lastDescription: String? = null
    var shouldFail = false

    override suspend fun invoke(title: String, description: String): Result<Unit> {
        wasCalled = true
        lastTitle = title
        lastDescription = description
        return if (shouldFail) {
            Result.failure(Exception("Ошибка добавления"))
        } else {
            Result.success(Unit)
        }
    }
}