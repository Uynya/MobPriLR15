Изменения, что были произведены:
Создан TasksUiState с полями: tasks, isLoading, error
ViewModel использует MutableStateFlow и viewModelScope.launch
ViewModel наследуется от ViewModel
Экран подписывается на uiState.collectAsState()
Отображаются состояния: загрузка, ошибка, список
Нет импортов из data в presentation
Dependency injection в MainActivity (уже есть)
