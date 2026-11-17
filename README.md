# Смачно

Крос-платформний клієнт тестового REST API [dummyjson](https://dummyjson.com/),
галузь - рецепти. Проєкт на Kotlin Multiplatform + Compose Multiplatform,
збирається під Android, iOS і десктоп (JVM).

## Модулі

* `shared` - спільний код: тема, ресурси, екрани, логіка;
* `androidApp` - точка входу для Android;
* `desktopApp` - точка входу для десктопа.

## Запуск

```
./gradlew :desktopApp:run
```

Для Android - конфігурація `androidApp` в Android Studio.

## Лабораторні роботи

### Лабораторна 4

* новий KMP проєкт з трьома модулями;
* кастомна тема: кольори згенеровані будівельником тем (Material Theme Builder)
  з базового кольору `#B4502B`, шрифти Unbounded (заголовки) та Inter (текст);
* бібліотека kermit для логування (`core/logging/AppLogger.kt`);
* бібліотека multiplatform-settings для налаштувань (`data/settings/AppSettings.kt`);
* екран онбордінгу на компоненті `HorizontalPager`, 4 слайди, картинки намальовані
  на `Canvas` (`ui/onboarding/`);
* локалізація двома мовами: `composeResources/values` (англійська) та
  `composeResources/values-uk` (українська).

### Лабораторна 5

* бібліотека Navigation 3 (`navigation3-ui` + `lifecycle-viewmodel-navigation3`),
  граф у `App.kt`: ключі `Onboarding` та `Login`, стек переживає поворот екрана
  завдяки `SavedStateConfiguration`;
* бібліотека Koin для ін'єкції залежностей, модулі розбиті по рівнях
  (`di/AppModule.kt`), запуск - `di/KoinInit.kt`;
* онбордінг переведено на MVI: `OnboardingAction`, `OnboardingEvent`,
  `OnboardingViewModel`; завершення онбордінгу зберігається варіантом
  використання `MarkOnboardingPassedUseCase` через `PreferencesRepository`;
* `AppViewModel` через `IsOnboardingPassedUseCase` обирає стартовий екран:
  якщо онбордінг уже проходили, застосунок одразу відкриває вхід;
* екран входу з власною MVI-архітектурою: валідація імені користувача
  (від 3 символів) і пароля (від 6), дія клавіатури в полі імені переводить
  фокус на пароль, а в полі пароля виконує вхід.
