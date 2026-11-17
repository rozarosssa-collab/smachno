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

### Лабораторна 6

* бібліотеки ktor (клієнт, Auth, ContentNegotiation, Logging) та KSafe
  для безпечного зберігання токенів;
* мережевий стек авторизації: запити `LoginRequest`/`RefreshRequest`,
  відповіді `LoginResponse`/`RefreshResponse`, сервіс `AppAuthApiService`,
  репозиторій `AppAuthRepository`, варіанти використання `LoginUseCase`,
  `IsSignedInUseCase`, `SignOutUseCase`, `SignedInUsernameUseCase`;
* власний тип `Result` з `map`, `onSuccess`, `onFailure` і обгорткою
  `safeRequest`, щоб мережеві винятки не виходили за рівень даних;
* токени зберігаються через KSafe (Keystore на Android, Keychain на iOS,
  зашифрований файл на десктопі), оновлення токена підвішене на
  `refreshTokens` плагіна Auth і спрацьовує автоматично;
* екран входу з'єднано з `LoginUseCase`, помилки показуються спливним
  повідомленням через одноразові події;
* порожній домашній екран `ui/home/`;
* `AppViewModel` обирає стартовий екран з урахуванням того, чи є збережений
  токен: домашній -> вхід -> онбордінг.

API: `https://dummyjson.com`. Свої акаунти там не реєструються, тому на екрані
входу є підказка з тестовим користувачем `emilys` / `emilyspass` і кнопка
«Підставити», яка заповнює обидва поля.

### Лабораторна 7

Адаптивна розмітка онбордінгу та екрана входу на `WindowSizeClass`:

* `ui/common/WindowLayout.kt` - перевірка ширини вікна
  (`currentWindowAdaptiveInfoV2().windowSizeClass.isWidthAtLeastBreakpoint`),
  поріг `WIDTH_DP_MEDIUM_LOWER_BOUND`, тобто 600 dp;
* `ui/common/CenteredScrollColumn.kt` - колонка, яка центрує вміст, поки він
  вміщається, і прокручується, коли місця бракує;
* онбордінг: на вузькому вікні картинка, текст і керування йдуть однією
  колонкою, на широкому - картинка займає ліву половину, а текст поточного
  слайда з кнопками стоїть праворуч;
* вхід: на вузькому вікні форма по центру екрана, на широкому - ліворуч
  кольорова заставка з назвою застосунку, праворуч сама форма.

Стан екранів при цьому один і той самий, змінюється лише розмітка.

### Понад програму

Домашній екран заповнено списком рецептів з `/auth/recipes`. Це захищена
адреса, тому заголовок `Authorization` туди підставляє плагін Auth із
збереженого токена - тобто список видно лише після справжнього входу.

* `data/recipes/` - модель, сервіс і репозиторій;
* `domain/recipes/ObtainRecipesUseCase.kt` - варіант використання;
* `ui/home/` - стан завантаження, помилка з кнопкою «Спробувати ще»
  і сам список; на широкому вікні картки стають у два стовпці, на вузькому -
  в один, за тим самим `isWideWindow()` з сьомої лабораторної.
