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
