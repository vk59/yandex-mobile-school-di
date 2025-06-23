# Документация проекта Yandex Mobile School DI

## Обзор проекта

Проект демонстрирует внедрение Dependency Injection (DI) в Android приложение. Содержит примеры работы с фрагментами, ViewModel, репозиториями и Jetpack Compose.

### Текущая структура проекта

```
yandex-mobile-school-di/
├── app/                          # Основной модуль приложения
│   ├── src/main/java/com/yandex/mobile_school/example/
│   │   ├── data/                 # Слой данных (репозитории, API)
│   │   ├── di/                   # DI компоненты и модули (которых пока нет)
│   │   ├── domain/               # Бизнес-логика (интеракторы)
│   │   ├── ui/                   # UI слой
│   │   │   ├── compose/          # Jetpack Compose компоненты
│   │   │   ├── login/            # Экран авторизации
│   │   │   └── profile/          # Экран профиля
│   │   └── YandexMobileSchoolApplication.kt
```

---

## Ветка: start-dependencies

### Задача
Внедрить Dependency Injection в проект для замены прямых зависимостей на инжектируемые.

### Текущее состояние проекта

В настоящее время проект использует статические методы для получения зависимостей (а-ля ServiceLocator):

```kotlin
// YandexMobileSchoolApplication.kt
class YandexMobileSchoolApplication : Application() {
    companion object {
        fun getUserRepository(): UserRepository = MockUserRepository
        fun getSettingsRepository() = SettingsRepositoryImpl.getInstance()
    }
}
```

```kotlin
// ComposeSettingsViewModel.kt
class ComposeSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository: SettingsRepository = 
        YandexMobileSchoolApplication.getSettingsRepository()
}
```

### Проблемы текущего подхода
- Жесткая связанность компонентов
- Сложность тестирования (нельзя легко подменить зависимости)
- Нарушение принципа инверсии зависимостей
- Отсутствие контроля жизненного цикла объектов

### Цель задачи
Заменить статические вызовы на инжекцию зависимостей через DI фреймворк (Dagger 2 или Hilt), обеспечив:
- Слабую связанность компонентов
- Легкость тестирования
- Контроль жизненного цикла объектов
- Возможность конфигурирования зависимостей

---

## Ветка: workshop-step-2

### Задача
Вынести функциональность ComposeSettingsActivity в отдельный модуль и создать отдельный DI компонент.

### Текущее состояние ComposeSettingsActivity

Функциональность настроек в Compose находится в основном модуле `app`:

```kotlin
// ComposeSettingsActivity.kt
class ComposeSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSettingsScreen(onNavigateBack = { finish() })
        }
    }
}
```

```kotlin
// ComposeSettingsViewModel.kt
class ComposeSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository: SettingsRepository = 
        YandexMobileSchoolApplication.getSettingsRepository()
    
    // StateFlow для управления состоянием настроек
    private val _darkModeEnabled = MutableStateFlow(false)
    private val _notificationsEnabled = MutableStateFlow(true)
    private val _language = MutableStateFlow("en")
    // ...
}
```

```kotlin
// ComposeSettingsScreen.kt
@Composable
fun ComposeSettingsScreen(onNavigateBack: () -> Unit) {
    // UI компоненты для настроек:
    // - Переключатель темной темы
    // - Переключатель уведомлений  
    // - Выбор языка
    // - Кнопка сброса настроек
}
```

### Цель задачи
Создать отдельный модуль `settings-compose` и вынести туда всю функциональность Compose настроек.

### Предполагаемые модули для реализации

Для упражнения можно выбрать свою структуру модулей, либо попробовать использовать предлагаемую

#### 1. `settings-api` модуль
**Назначение**: API для работы с настройками
**Содержимое**:
- `SettingsRepository` интерфейс
- `SettingsInteractor` интерфейс  
- Модели данных настроек
- DI компонент для предоставления API

#### 2. `settings-impl` модуль
**Назначение**: Реализация бизнес-логики настроек
**Содержимое**:
- `SettingsRepositoryImpl` 
- `SettingsInteractorImpl`
- DI модули для предоставления реализаций

#### 3. `settings-compose` модуль
**Назначение**: UI компоненты настроек на Compose
**Содержимое**:
- `ComposeSettingsActivity`
- `ComposeSettingsScreen` 
- `ComposeSettingsViewModel`
- DI компонент для Compose UI

#### 4. Обновление `app` модуля
**Изменения**:
- Удаление Compose настроек из основного модуля
- Подключение зависимости на `settings-compose`
- Обновление навигации для запуска настроек

### Архитектурные преимущества
- **Модульность**: Четкое разделение ответственности
- **Переиспользование**: API и реализация могут использоваться в других модулях
- **Тестируемость**: Каждый модуль можно тестировать независимо
- **Масштабируемость**: Легко добавлять новые типы UI (например, XML-версию настроек)
- **Инкапсуляция**: Внутренние детали реализации скрыты за API

### Ожидаемая структура после рефакторинга

```
yandex-mobile-school-di/
├── app/                          # Основной модуль (без Compose настроек)
├── settings-api/                 # API для настроек
├── settings-impl/                # Реализация настроек
└── core/                         # Общий модуль с CoreModule
```

---

## Архитектура DI с CoreModule

### CoreModule в core модуле

Общий модуль `core` содержит переиспользуемые DI компоненты:

```kotlin
// core/src/main/java/com/yandex/mobile_school/core/di/CoreModule.kt
@Module
class CoreModule {
    
    @Provides
    @Singleton
    fun provideDaggerViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return DaggerViewModelFactory(creators)
    }
}

// core/src/main/java/com/yandex/mobile_school/core/di/DaggerViewModelFactory.kt
class DaggerViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("Unknown model class $modelClass")
        
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
```

### AppComponent подключает CoreModule

```kotlin
// app/src/main/java/com/yandex/mobile_school/example/di/AppComponent.kt
@Singleton
@Component(modules = [
    CoreModule::class,           // Подключаем общий модуль
    AppModule::class, 
    RepositoryModule::class, 
    ViewModelModule::class
])
interface AppComponent {
    fun inject(application: YandexMobileSchoolApplication)
    fun inject(activity: MainActivity)
    
    // Предоставляем зависимости для settings модуля
    fun settingsRepository(): SettingsRepository
    fun settingsInteractor(): SettingsInteractor
}
```

### SettingsComponent тоже подключает CoreModule

```kotlin
// settings-impl/src/main/java/com/yandex/mobile_school/settings/di/SettingsComponent.kt
@SettingsScope
@Component(modules = [
    CoreModule::class,           // Тот же общий модуль
    SettingsViewModelModule::class
])
interface SettingsComponent {
    
    fun inject(activity: ComposeSettingsActivity)
    
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance settingsRepository: SettingsRepository,
            @BindsInstance settingsInteractor: SettingsInteractor
        ): SettingsComponent
    }
}
```