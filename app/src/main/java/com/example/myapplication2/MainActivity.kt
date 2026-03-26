package com.example.myapplication2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication2.data.repository.InMemoryTaskRepositoryImpl
import com.example.myapplication2.domain.usecase.AddTaskUseCase
import com.example.myapplication2.domain.usecase.GetTasksUseCase
import com.example.myapplication2.domain.model.Task as DomainTask
import com.example.myapplication2.presentation.viewmodel.TasksViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication2.domain.model.Task
import com.example.myapplication2.presentation.tasks.TasksUiState

@Composable
fun TaskCard(
    task: DomainTask,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая заметка", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Заголовок заметки") },
                    placeholder = { Text("Введите заголовок") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание заметки") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) onConfirm(title.trim(), description.trim())
                onDismiss()
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun FloatingActionButton5(onAddClick: () -> Unit) {
    FloatingActionButton(
        onClick = onAddClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.minimumInteractiveComponentSize(),
        content = { Icon(Icons.Default.Add, contentDescription = "Добавить") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    title: String = "Заметки",
    onThemeToggle: () -> Unit
) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        actions = {
            IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Поиск") }
            IconButton(onClick = onThemeToggle) { Icon(Icons.Default.Add, contentDescription = "Тема") }
        }
    )
}


interface ThemeManager {
    fun getThemeMode(): ThemeMode
    fun setThemeMode(mode: ThemeMode)
}

class ThemeManagerImpl : ThemeManager {
    private var currentThemeMode: ThemeMode = ThemeMode.System
    override fun getThemeMode() = currentThemeMode
    override fun setThemeMode(mode: ThemeMode) { currentThemeMode = mode }
}

enum class ThemeMode { Light, Dark, System }

@Composable
fun MyApplicationTheme(
    themeManager: ThemeManager,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeManager.getThemeMode()) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme, typography = MaterialTheme.typography, content = content)
}

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onTaskClick: (Task) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ошибка: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Retry */ }) {
                    Text("Повторить")
                }
            }
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(items = uiState.tasks) { task ->
                    TaskCard(
                        task = task,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onTaskClick(task) }
                    )
                }

                if (uiState.tasks.isEmpty()) {
                    item {
                        Text(
                            text = "Нет заметок",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = remember { InMemoryTaskRepositoryImpl() }
            val getTasksUseCase = remember { GetTasksUseCase(repository) }
            val addTaskUseCase = remember { AddTaskUseCase(repository) }
            val viewModel = remember { TasksViewModel(getTasksUseCase, addTaskUseCase) }
            val themeManager = remember { ThemeManagerImpl() }
            val uiState by viewModel.uiState.collectAsState()

            var showDialog by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                viewModel.loadTasks()
            }

            MyApplicationTheme(themeManager = themeManager) {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton5(onAddClick = { showDialog = true })
                    },
                    topBar = {
                        NotesTopAppBar(
                            title = "Заметки",
                            onThemeToggle = {
                                val newMode = when (themeManager.getThemeMode()) {
                                    ThemeMode.System -> ThemeMode.Light
                                    ThemeMode.Light -> ThemeMode.Dark
                                    ThemeMode.Dark -> ThemeMode.System
                                }
                                themeManager.setThemeMode(newMode)
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NotesScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        onTaskClick = { viewModel.onTaskClick(it) }
                    )
                }

                if (showDialog) {
                    AddTaskDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { title, description ->
                            viewModel.addTask(title, description)
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
}