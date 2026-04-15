-- Задание 1 (UiState) - выполнено:
  TasksUiState с полями: tasks, isLoading, error
  
-- Задание 2 (ViewModel) - выполнено:
  TasksViewModel с @HiltViewModel
  MutableStateFlow<TasksUiState>
  Методы: loadTasks(), addTask(), onTaskClick(), clearError()
  Использование UseCase через конструктор с Hilt
  
-- Задание 3 (Compose экран) - выполнено:
  Подписка на состояние через collectAsState()
  LaunchedEffect(Unit) для загрузки данных
  Отображение loading/error/контента
  Вызов методов ViewModel из UI
  
-- Задание 4 (DI и навигация) - выполнено:
  @HiltAndroidApp в Application
  DI модули для Repository, UseCase, Dispatchers
  hiltViewModel() в MainActivity

-- Выводы:
1. MVVM упрощает тестирование ViewModel без Android-зависимостей
2. StateFlow автоматически обновляет UI при изменении данных
3. Разделение ответственности: ViewModel не знает о Compose
4. Hilt упрощает внедрение зависимостей
5. По сравнению с MVP: меньше boilerplate кода, нет необходимости в интерфейсе View
