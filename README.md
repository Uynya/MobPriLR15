Настройка областей видимости:
  1. Добавлены аннотации @Singleton в RepositoryModule и UseCaseModule для указания времени жизни зависимостей на уровне всего приложения

Создание квалификаторов:
  1. Создан пакет di.qualifiers с собственными аннотациями-квалификаторами
  2. Добавлен @IoDispatcher — для Dispatchers.IO
  3. Добавлен @DefaultDispatcher — для Dispatchers.Default

Создание DispatcherModule:
  1. Создан DispatcherModule в пакете di для предоставления CoroutineDispatcher
  2. Добавлены провайдеры provideIoDispatcher() и provideDefaultDispatcher() с соответствующими квалификаторами и аннотацией @Singleton
     
Обновление UseCase:
  1. Обновлён GetTasksUseCase — добавлен параметр @IoDispatcher ioDispatcher: CoroutineDispatcher в конструктор
  2. Обновлён AddTaskUseCase — добавлен параметр @IoDispatcher ioDispatcher: CoroutineDispatcher в конструктор
     
Обновление UseCaseModule:
  1. Добавлены параметры @IoDispatcher ioDispatcher: CoroutineDispatcher в функции provideGetTasksUseCase() и provideAddTaskUseCase()
  2. UseCase теперь создаются с двумя зависимостями: репозиторий и dispatcher
