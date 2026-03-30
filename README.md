  Настройка Gradle:
1. Добавлены плагины Hilt (com.google.dagger.hilt.android) и KSP в build.gradle.kts
2. Добавлены зависимости Hilt (hilt-android, hilt-android-compiler, hilt-navigation-compose) в файл зависимостей
3. Настроен libs.versions.toml с версиями Hilt 2.51.1 и совместимыми версиями Kotlin и KSP

  Создание Application класса:
1. Создан класс MyApplication с аннотацией @HiltAndroidApp
2. Зарегистрирован MyApplication в AndroidManifest.xml через атрибут android:name=".MyApplication"
   
  Создание Hilt-модулей:
1. Создан RepositoryModule в пакете di — предоставляет TaskRepository через InMemoryTaskRepositoryImpl с аннотациями @Module, @InstallIn(SingletonComponent::class), @Provides и @Singleton
2. Создан UseCaseModule в пакете di — предоставляет GetTasksUseCase и AddTaskUseCase с теми же аннотациями
   
  Обновление ViewModel:
1. Добавлена аннотация @HiltViewModel к классу TasksViewModel
2. Добавлена аннотация @Inject к конструктору ViewModel
3. ViewModel теперь получает зависимости (GetTasksUseCase, AddTaskUseCase) через конструктор автоматически
   
  Обновление MainActivity:
1. Добавлена аннотация @AndroidEntryPoint к классу MainActivity
2. Удалён код ручного создания зависимостей (repository, getTasksUseCase, addTaskUseCase, viewModel)
3. ViewModel теперь получается через hiltViewModel() вместо ручного создания через remember
4. Удалены неиспользуемые импорты классов из пакета data.repository и domain.usecase
   
  Обновление TasksViewModel:
1. Добавлены необходимые импорты для Hilt (dagger.hilt.android.lifecycle.HiltViewModel, javax.inject.Inject)
2. ViewModel теперь полностью полагается на внедрение зависимостей через Hilt

  Общие изменения:
1. Используется существующий InMemoryTaskRepositoryImpl для хранения данных в памяти
2. Проект теперь следует принципу Dependency Injection: зависимости создаются в Hilt-модулях и автоматически внедряются в компоненты приложения
